Приложение реализует REST API для To-Do списка со следующими функциями:
	- Регистрация нового пользователя;
	- Авторизация пользователя;
	- Возможность добавления, просмотра, изменения, удаления задачи в списке;
	- Возможность создания задачи с прикрепленными файлами.
	 Прикрепленные файлы размещаются в private bucket сервиса Yandex.Cloud. 

Используемый стек: 
	Java SE 8, Maven, Spring Boot 2.7.17, PostgreSQL, Spring Data, Spring HATEOAS, Spring Security, AWS S3 для работы с Object Storage.

Конфигурация приложения осуществляется путем указания ключей в файле resources/application.properties
	accessKey=			идентификатор ключа доступа к сервису Yandex.Cloud(не обязателен)
	secretKey=			секретный ключ доступа к сервису Yandex.Cloud(не обязателен)
	bucketName=			наименование приватного bucket(не обязателен)
	urlExpirationDays=		срок хранения ссылок на файлы в сутках
	userSessionExpirationDays=	срок действительности JWT токена пользователя


Описание функций приложения

Создание нового пользователя
	Метод: POST
	URL: /todo/user/add
	HTTP-Headers: Content-Type: application/json
	Описание: Создает нового пользователя в системе.
	Параметры:
		username(строка, обязательный) - имя пользователя(4-64 символов)
		password(строка, обязательный) - пароль пользователя(4-128 символов)
	Ответ:
	Код состояния: 200 OK
	Тело ответа:
 		id		- идентификатор, присвоенный пользователю
		username  	- имя созданного пользователя
		password 	- хэш пароля созданного пользователя
		jwt 		- токен безопасности пользователя
	Код состояния: 400 BAD REQUEST
	Тело ответа:
 		message		- текст сообщения об ошибке
		timestamp	- время возникновения ошибки(мсек)
	Пример запроса:
		{ 
   			"username" : "user1",
   			"password" : "password"
		}
	Ответ:
		{
    			"id": 1,
    			"username": "user1",
    			"password": "$2a$10$2bt397TBemRAS.7Y5KkmEODEhAB5RQaIyzy4PnSmjydQAOpGZIL1m",
   			"jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyRGV0YWlscyIsInVzZXJuYW1lIjoidXNlcjUiLCJpc3MiOiJvcmcuaG9tZSIsImlhdCI6MTcwNDQ1NzEwNCwiZXhwIjoxNzA0NTQzNTA0fQ.OKwusoc4F7B9z6o60PiXTuIr-FHqKrcblj7ICWs7T3w",		
        	}

Авторизация пользователя
	Метод: POST
	URL: /todo/user/login
	HTTP-Header: Content-Type: application/json
	Описание: возвращает  вновь сформированный токен безопасности пользователя
	Параметры:
		username(строка, обязательный) - имя пользователя(4-64 символов)
		password(строка, обязательный) - пароль пользователя(4-128 символов)
	Ответ:
	Код состояния: 200 OK
	Тело ответа:
 		id		- идентификатор, присвоенный пользователю
		username  	- имя созданного пользователя
		password 	- хэш пароля
		jwt 		- токен безопасности пользователя
	Код состояния: 400 BAD REQUEST
	Тело ответа:
 		message		- текст сообщения об ошибке
		timestamp	- время возникновения ошибки(мсек)
	Код состояния: 401 UNAUTHORIZED
	Тело ответа:
 		message		- текст сообщения об ошибке
		timestamp	- время возникновения ошибки(мсек)
	Пример запроса:
		{ 
    			"username" : "user1",
    			"password" : "password"
		}
	Ответ:
		{
    			"id": 1,
    			"username": "user1",
    			"password": "$2a$10$2bt397TBemRAS.7Y5KkmEODEhAB5RQaIyzy4PnSmjydQAOpGZIL1m",
    			"jwt": "pXVCJ9.eyJzdWIiOiJ1c2VyRGV0YWlscyIsInVzZXJuYW1lIjoidXNlcjUiLCJpc3MiOiJvcmcuaG9tZSIsImlhdCI6MTcwNDQ1NzEwNCwiZXhwIjoxNzA0NTQzNTA0fQ.OKwusoc4F7B9z6o60PiXTuIr-FHqKrcblj7ICWs7T3weyJhbGciOiJIUzI1NiIsInR5cCI6Ik",
   		}

Удаление пользователя
	Метод: POST
	URL: /todo/user/delete
	HTTP-Header: Authorization: Bearer + JWT
	Описание: удаляет пользователя, токен безопасности которого внедрен в http-заголовок Authorization(префикс "Bearer ")  запроса 
	Ответ:
	Код состояния: 200 OK
	Тело ответа:
		message		- текст сообщения об успешном удалении пользователя
		timestamp	- время выполнения операции(мсек)
	Код состояния: 401 UNAUTHORIZED
	Тело ответа:
		message		- текст сообщения об ошибке
		timestamp	- время возникновения ошибки(мсек)

	Пример содержимого http-заголовка Authorization запроса с внедренным токеном безопасности:
		Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyRGV0YWlscyIsInVzZXJuYW1lIjoidXNlcjEiLCJpc3MiOiJvcmcuaG9tZSIsImlhdCI6MTcwNDQ1ODY5NCwiZXhwIjoxNzA0NTQ1MDk0fQ.Mx09EM6l2t6QK4RSxUjabOSZP8_wFre_SJVXnUSjbzY
	Ответ:
		{
    			"message": "user deleted",
    			"timestamp": 1704458710428
		}	

Создание новой задачи
	Метод: POST
	URL: /todo/task/add
	HTTP-Header: Content-Type: application/json, Authorization: Bearer + JWT
	Описание: Создает новую задачу для пользователя
	Параметры:
		header(строка, обязательный) - наименование задачи(4-256 символов)
		info(строка, необязательный) - описание задачи(0-1024 символов)
		dateTime(строка, обязательный) - время выполнения задачи в формате ДД-ММ-ГГГГ 		ЧЧ:ММ
	Ответ:
	Код состояния: 200 OK
	Тело ответа:
 		id		- идентификатор, присвоенный задаче
		header  	- наименование задачи
		info	 	- описание задачи
		dateTime 	- время выполнения задачи в формате ДД-ММ-ГГГГ  ЧЧ:ММ
		ownedBy		- имя пользователя, создавшего задачу
		addons		- url-ссылки на приложенные файлы(для запроса addWith)
	Код состояния: 400 BAD REQUEST
	Тело ответа:
 		message - текст сообщения об ошибке
		timestamp - время возникновения ошибки(мсек)
	Пример запроса:
		{ 
    			"header" : "Pay a credit card",
    			"info" : "",
    			"dateTime" : "05-06-2024 15:00"
		}
	Ответ:
		{
    			"id": 72,
    			"header": "Pay a credit card",
    			"info": "",
    			"dateTime": "05-06-2024 15:00",
    			"ownedBy": "user1",
    			"addons": [],
    		}


Создание новой задачи с приложением файла(файлов)
	Метод: POST
	URL: /todo/task/addWith
	HTTP-Headers: Content-Type: multipart/form-data; Authorization: Bearer + JWT
	Описание: Создает новую задачу для пользователя. Функция использует multipart/form-data для передачи в те-ле запроса параметров JSON совместно с бинарными файлами.
	Параметры:
		header(строка, обязательный) - наименование задачи(4-256 символов)
		info(строка, необязательный) - описание задачи(0-1024 символов)
		dateTime(строка, обязательный) - время выполнения задачи в формате ДД-ММ-ГГГГ 		ЧЧ:ММ
	Ключи form-data:
		json(текст, обязательный) - параметры
		file(файл, обязательный) - приложенный файл. Допустимо несколько ключей file
	Ответ:
	Код состояния: 200 OK
	Тело ответа:
 		id		- идентификатор, присвоенный задаче
		header  		- наименование задачи
		info	 	- описание задачи
		dateTime 	- время выполнения задачи в формате ДД-ММ-ГГГГ  ЧЧ:ММ
		ownedBy		- имя пользователя, создавшего задачу
		addons		- url-ссылки на приложенные файлы
	Код состояния: 400 BAD REQUEST
	Тело ответа:
 		message	- текст сообщения об ошибке
		timestamp	- время возникновения ошибки(мсек)
	Пример запроса:
	Ключ json:
		{ 
    			"header" : "Pay a credit card",
    			"info" : "",
    			"dateTime" : "05-06-2024 15:00"
		}
	Ключ file:
		card_information.txt
	Ответ:
		{
    			"id": 76,
    			"header": "Pay a credit card",
    			"info": "",
    			"dateTime": "05-06-2024 15:00",
    			"ownedBy": "user1",
		    	"addons": ["https://privatebucket.storage.yandexcloud.net/readme.txt?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240106T120234Z&X-Amz-SignedHeaders=host&X-Amz-Expires=86400&X-Amz-Credential=YCAJEi3SGbdpoNybjZN9tiU2Y%2F20240106%2Fru-central1%2Fs3%2Faws4_request&X-Amz-Signature=1339499b9572826971658c8fc342a7aa2c917968f0668a7beb876f5d6a6d2ef5"],
		}

Изменение задачи
	Метод: POST
	URL: /todo/task/update/{id}
	HTTP-Header: Content-Type: application/json, Authorization: Bearer + JWT
	Описание: Изменяет задачу с указанным id, приложенные к задаче файлы не изменяются
	Параметры:
		header(строка, обязательный) - наименование задачи(4-256 символов)
		info(строка, необязательный) - описание задачи(0-1024 символов)
		dateTime(строка, обязательный) - время выполнения задачи в формате ДД-ММ-ГГГГ 		ЧЧ:ММ
	Ответ:
	Код состояния: 200 OK
	Тело ответа:
 		id		- идентификатор задачи
		header  		- наименование задачи
		info	 	- описание задачи
		dateTime 	- время выполнения задачи в формате ДД-ММ-ГГГГ  ЧЧ:ММ
		ownedBy		- имя пользователя, создавшего задачу
		addons		- url-ссылки на приложенные файлы(для запроса addWith)
	Код состояния: 400 BAD REQUEST
	Тело ответа:
 		message - текст сообщения об ошибке
		timestamp - время возникновения ошибки(мсек)
	Код состояния: 404 NOT FOUND
	Тело ответа:
 		message - текст сообщения об ошибке
		timestamp - время возникновения ошибки(мсек)

	Пример запроса:
		{ 
    			"header" : "Pay another credit card",
    			"info" : "",
    			"dateTime" : "05-06-2025 15:00"
		}
	Ответ:
		{
    			"id": 76,
    			"header": "Pay another credit card",
    			"info": "",
    			"dateTime": "05-06-2025 15:00",
    			"ownedBy": "user1",
    			"addons": [],
		}

Получение задачи
	Метод: GET
	URL: /todo/task/get/{id}
	HTTP-Header: Authorization: Bearer + JWT
	Описание: Получение задачи пользователя по указанному id

	Параметры:
		id - id запрашиваемой задачи
	Ответ:
	Код состояния: 200 OK
	Тело ответа:
 		id		- идентификатор задачи
		header  		- наименование задачи
		info	 	- описание задачи
		dateTime 	- время выполнения задачи в формате ДД-ММ-ГГГГ  ЧЧ:ММ
		ownedBy		- имя пользователя, создавшего задачу
		addons		- url-ссылки на приложенные файлы(для запроса addWith)
	Код состояния: 404 NOT FOUND
	Тело ответа:
 		message - текст сообщения об ошибке
		timestamp - время возникновения ошибки(мсек)
	Пример запроса:
		http://localhost:8080/todo/task/get/76
	Ответ:
		{
    			"id": 76,
    			"header": "Pay a credit card",
    			"info": "",
    			"dateTime": "05-06-2024 15:00",
    			"ownedBy": "user1",
    			"addons": ["https://privatebucket.storage.yandexcloud.net/readme.txt?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240106T120234Z&X-Amz-SignedHeaders=host&X-Amz-Expires=86400&X-Amz-Credential=YCAJEi3SGbdpoNybjZN9tiU2Y%2F20240106%2Fru-central1%2Fs3%2Faws4_request&X-Amz-Signature=1339499b9572826971658c8fc342a7aa2c917968f0668a7beb876f5d6a6d2ef5"],
  		}

Удаление задачи
	Метод: POST
	URL: /todo/task/delete/{id}
	HTTP-Header: Authorization: Bearer + JWT
	Описание: Удаление задачи пользователя по указанному id
	Параметры:
		id - id удаляемой задачи
	Ответ:
	Код состояния: 200 OK
	Тело ответа:
 		message - текст сообщения об успешном удалении задачи
		timestamp - время выполнения операции(мсек)
	Код состояния: 404 NOT FOUND
	Тело ответа:
 		message - текст сообщения об ошибке
		timestamp - время возникновения ошибки(мсек)
	Пример запроса:
		http://localhost:8080/todo/task/delete/72
	Ответ:
		{
    			"message": "task deleted",
    			"timestamp": 1704544614437
		}
