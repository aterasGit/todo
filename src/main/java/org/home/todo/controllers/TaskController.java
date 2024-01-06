package org.home.todo.controllers;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.home.todo.dto.TaskDTO;
import org.home.todo.models.Addon;
import org.home.todo.models.Task;
import org.home.todo.services.AddonsService;
import org.home.todo.services.TasksService;
import org.home.todo.util.CheckUtil;
import org.home.todo.util.exceptions.EntityRejectedException;
import org.home.todo.util.responses.GenericResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@RestController
@RequestMapping("/task")
public class TaskController {

    @Value("${accessKey}")
    private String accessKey;
    @Value("${secretKey}")
    private String secretKey;
    @Value("${bucketName}")
    private String bucketName;
    @Value("${serviceEndpoint}")
    private String serviceEndpoint;
    @Value("${signingRegion}")
    private String signingRegion;
    @Value("${urlExpirationDays}")
    private int urlExpirationDays;
    private final ModelMapper modelMapper;
    private final TasksService tasksService;
    private final AddonsService addonsService;
    private final CheckUtil checkUtil;

    @Autowired
    public TaskController(ModelMapper modelMapper, TasksService tasksService,
                          AddonsService addonsService, CheckUtil checkUtil) {
        this.modelMapper = modelMapper;
        this.tasksService = tasksService;
        this.addonsService = addonsService;
        this.checkUtil = checkUtil;
    }

    @PostMapping("/add")
    public ResponseEntity<TaskDTO> addTask(@RequestBody @Valid TaskDTO taskDTO,
                                           BindingResult bindingResult) {
        Task task = taskDTO2Task(taskDTO);
        checkUtil.checkForValidationErrors(bindingResult);
        tasksService.addTask(task);
        return new ResponseEntity<>(
                addLinks(task2TaskDTO(task)),
                HttpStatus.OK
        );
    }

    @PostMapping("/addWith")
    public ResponseEntity<TaskDTO> addTaskWith(@RequestParam("json") String jsonString,
                                               @RequestParam("file") MultipartFile[] files,
                                               @RequestHeader("Authorization") String authorizationHeader) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authorizationHeader);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);
        Task task;
        try {
            task = new RestTemplate().postForObject(
                    ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
                            .replaceAll("/addWith", "/add"),
                    requestEntity,
                    Task.class
            );
        } catch (Exception e) {
            Matcher matcher = Pattern
                    .compile("(?<=message\\W{0,10})[\\w;\\s-]+")
                    .matcher(e.getMessage());
            String errorMessage = e.getMessage();
            if (matcher.find()) errorMessage = matcher.group();
            throw new EntityRejectedException(errorMessage);
        }
        assert task != null;

        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(serviceEndpoint,signingRegion)
                )
                .build();

        for (MultipartFile file : files) {
            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                s3Client.putObject(
                        new PutObjectRequest(
                                bucketName, file.getOriginalFilename(), file.getInputStream(), metadata
                        )
                );
            } catch (Exception e) {
                addonsService.deleteAddonByTask(task);
                tasksService.delete(task);
                throw new EntityRejectedException(e.getMessage());
            }
            Date expiration = new Date(System.currentTimeMillis() + urlExpirationDays * 86400000L);
            URL url = s3Client.generatePresignedUrl(
                    new GeneratePresignedUrlRequest(
                            bucketName, file.getOriginalFilename()).withExpiration(expiration)
            );
            addonsService.addAddon(new Addon(url.toString(), LocalDateTime.now(), task));
        }

        return new ResponseEntity<>(
                addLinks(task2TaskDTO(tasksService.getTask(task.getId()))),
                HttpStatus.OK
        );

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable("id") int id) {
        return new ResponseEntity<>(
                addLinks(task2TaskDTO(tasksService.getTask(id))),
                HttpStatus.OK
        );
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<TaskDTO>> getTasks() {
        List<Task> tasks = tasksService.getAllUsersTasks();
        if (!tasks.isEmpty())
            return new ResponseEntity<>
                    (tasks.stream()
                            .map(this::task2TaskDTO)
                            .map(this::addLinks)
                            .collect(Collectors.toList()), HttpStatus.OK);
        else throw new EntityRejectedException("tasks not found");
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable("id") int id,
                                              @RequestBody @Valid TaskDTO taskDTO,
                                              BindingResult bindingResult) {
        Task task = taskDTO2Task(taskDTO);
        checkUtil.checkForValidationErrors(bindingResult);
        tasksService.updateTask(id, task);
        return new ResponseEntity<>(
                addLinks(task2TaskDTO(task)),
                HttpStatus.OK
        );
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<GenericResponse> deleteTask(@PathVariable("id") int id) {
        tasksService.deleteTask(id);
        return new ResponseEntity<>(
                new GenericResponse("task deleted", System.currentTimeMillis()),
                HttpStatus.OK
        );
    }

    private Task taskDTO2Task(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }

    private TaskDTO task2TaskDTO(Task task) {
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        taskDTO.setOwnedBy(task.getUser().getUsername());
        if (task.getAddons() != null)
            taskDTO.setAddons(
                task.getAddons()
                        .stream()
                        .map(Addon::getUrl)
                        .collect(Collectors.toList())
        );
        else taskDTO.setAddons(Collections.emptyList());
        return taskDTO;
    }

    private TaskDTO addLinks(TaskDTO taskDTO) {
        taskDTO.add(
                linkTo(TaskController.class)
                        .slash("add")
                        .withRel("addTask")
                        .withTitle("add new task")
        );
        taskDTO.add(
                linkTo(TaskController.class)
                        .slash("addWith")
                        .withRel("addTaskWith")
                        .withTitle("add new task with addons")
        );
        taskDTO.add(
                linkTo(methodOn(TaskController.class)
                        .updateTask(taskDTO.getId(), taskDTO, null))
                        .withRel("updateTask")
                        .withTitle("update task")
        );
        taskDTO.add(
                linkTo(methodOn(TaskController.class)
                        .deleteTask(taskDTO.getId()))
                        .withRel("deleteTask")
                        .withTitle("delete task")
        );
        taskDTO.add(
                linkTo(methodOn(TaskController.class)
                        .getTask(taskDTO.getId()))
                        .withRel("getTask")
                        .withTitle("get task")
        );
        return taskDTO;
    }

}
