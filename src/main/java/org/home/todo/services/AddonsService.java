package org.home.todo.services;

import org.home.todo.models.Addon;
import org.home.todo.models.Task;
import org.home.todo.repositories.AddonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Service
@EnableScheduling
@Transactional(readOnly = true)
public class AddonsService {

    @Value("${urlExpirationDays}")
    private int urlExpirationDays;

    private final AddonRepository addonRepository;

    @Autowired
    public AddonsService(AddonRepository addonRepository) {
        this.addonRepository = addonRepository;
    }

    @Transactional
    public void addAddon(Addon addon) {
        addonRepository.save(addon);
    }

    @Scheduled(cron = "@hourly")
    @Transactional
    public void deleteOutdatedAddons() {
        addonRepository.deleteByCreatedAtBefore(LocalDateTime.now().minusDays(urlExpirationDays));
    }

    @Transactional
    public void deleteAddonByTask(Task task) {
        addonRepository.deleteByTask(task);
    }
}
