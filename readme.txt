���������� ��������� REST API ��� To-Do ������ �� ���������� ���������:
	- ����������� ������ ������������;
	- ����������� ������������;
	- ����������� ����������, ���������, ���������, �������� ������ � ������;
	- ����������� �������� ������ � �������������� �������.
	 ������������� ����� ����������� � private bucket ������� Yandex.Cloud. 

������������ ����: 
	Java SE 8, Maven, Spring Boot 2.7.17, PostgreSQL, Spring Data, Spring HATEOAS, Spring Security, AWS S3 ��� ������ � Object Storage.

������������ ���������� �������������� ����� �������� ������ � ����� resources/application.properties
	accessKey=			������������� ����� ������� � ������� Yandex.Cloud(�� ����������)
	secretKey=			��������� ���� ������� � ������� Yandex.Cloud(�� ����������)
	bucketName=			������������ ���������� bucket(�� ����������)
	urlExpirationDays=		���� �������� ������ �� ����� � ������
	userSessionExpirationDays=	���� ���������������� JWT ������ ������������


�������� ������� ����������

�������� ������ ������������
	�����: POST
	URL: /todo/user/add
	HTTP-Headers: Content-Type: application/json
	��������: ������� ������ ������������ � �������.
	���������:
		username(������, ������������) - ��� ������������(4-64 ��������)
		password(������, ������������) - ������ ������������(4-128 ��������)
	�����:
	��� ���������: 200 OK
	���� ������:
 		id		- �������������, ����������� ������������
		username  	- ��� ���������� ������������
		password 	- ��� ������ ���������� ������������
		jwt 		- ����� ������������ ������������
	��� ���������: 400 BAD REQUEST
	���� ������:
 		message		- ����� ��������� �� ������
		timestamp	- ����� ������������� ������(����)
	������ �������:
		{ 
   			"username" : "user1",
   			"password" : "password"
		}
	�����:
		{
    			"id": 1,
    			"username": "user1",
    			"password": "$2a$10$2bt397TBemRAS.7Y5KkmEODEhAB5RQaIyzy4PnSmjydQAOpGZIL1m",
   			"jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyRGV0YWlscyIsInVzZXJuYW1lIjoidXNlcjUiLCJpc3MiOiJvcmcuaG9tZSIsImlhdCI6MTcwNDQ1NzEwNCwiZXhwIjoxNzA0NTQzNTA0fQ.OKwusoc4F7B9z6o60PiXTuIr-FHqKrcblj7ICWs7T3w",		
        	}

����������� ������������
	�����: POST
	URL: /todo/user/login
	HTTP-Header: Content-Type: application/json
	��������: ����������  ����� �������������� ����� ������������ ������������
	���������:
		username(������, ������������) - ��� ������������(4-64 ��������)
		password(������, ������������) - ������ ������������(4-128 ��������)
	�����:
	��� ���������: 200 OK
	���� ������:
 		id		- �������������, ����������� ������������
		username  	- ��� ���������� ������������
		password 	- ��� ������
		jwt 		- ����� ������������ ������������
	��� ���������: 400 BAD REQUEST
	���� ������:
 		message		- ����� ��������� �� ������
		timestamp	- ����� ������������� ������(����)
	��� ���������: 401 UNAUTHORIZED
	���� ������:
 		message		- ����� ��������� �� ������
		timestamp	- ����� ������������� ������(����)
	������ �������:
		{ 
    			"username" : "user1",
    			"password" : "password"
		}
	�����:
		{
    			"id": 1,
    			"username": "user1",
    			"password": "$2a$10$2bt397TBemRAS.7Y5KkmEODEhAB5RQaIyzy4PnSmjydQAOpGZIL1m",
    			"jwt": "pXVCJ9.eyJzdWIiOiJ1c2VyRGV0YWlscyIsInVzZXJuYW1lIjoidXNlcjUiLCJpc3MiOiJvcmcuaG9tZSIsImlhdCI6MTcwNDQ1NzEwNCwiZXhwIjoxNzA0NTQzNTA0fQ.OKwusoc4F7B9z6o60PiXTuIr-FHqKrcblj7ICWs7T3weyJhbGciOiJIUzI1NiIsInR5cCI6Ik",
   		}

�������� ������������
	�����: POST
	URL: /todo/user/delete
	HTTP-Header: Authorization: Bearer + JWT
	��������: ������� ������������, ����� ������������ �������� ������� � http-��������� Authorization(������� "Bearer ")  ������� 
	�����:
	��� ���������: 200 OK
	���� ������:
		message		- ����� ��������� �� �������� �������� ������������
		timestamp	- ����� ���������� ��������(����)
	��� ���������: 401 UNAUTHORIZED
	���� ������:
		message		- ����� ��������� �� ������
		timestamp	- ����� ������������� ������(����)

	������ ����������� http-��������� Authorization ������� � ���������� ������� ������������:
		Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyRGV0YWlscyIsInVzZXJuYW1lIjoidXNlcjEiLCJpc3MiOiJvcmcuaG9tZSIsImlhdCI6MTcwNDQ1ODY5NCwiZXhwIjoxNzA0NTQ1MDk0fQ.Mx09EM6l2t6QK4RSxUjabOSZP8_wFre_SJVXnUSjbzY
	�����:
		{
    			"message": "user deleted",
    			"timestamp": 1704458710428
		}	

�������� ����� ������
	�����: POST
	URL: /todo/task/add
	HTTP-Header: Content-Type: application/json, Authorization: Bearer + JWT
	��������: ������� ����� ������ ��� ������������
	���������:
		header(������, ������������) - ������������ ������(4-256 ��������)
		info(������, ��������������) - �������� ������(0-1024 ��������)
		dateTime(������, ������������) - ����� ���������� ������ � ������� ��-��-���� 		��:��
	�����:
	��� ���������: 200 OK
	���� ������:
 		id		- �������������, ����������� ������
		header  	- ������������ ������
		info	 	- �������� ������
		dateTime 	- ����� ���������� ������ � ������� ��-��-����  ��:��
		ownedBy		- ��� ������������, ���������� ������
		addons		- url-������ �� ����������� �����(��� ������� addWith)
	��� ���������: 400 BAD REQUEST
	���� ������:
 		message - ����� ��������� �� ������
		timestamp - ����� ������������� ������(����)
	������ �������:
		{ 
    			"header" : "Pay a credit card",
    			"info" : "",
    			"dateTime" : "05-06-2024 15:00"
		}
	�����:
		{
    			"id": 72,
    			"header": "Pay a credit card",
    			"info": "",
    			"dateTime": "05-06-2024 15:00",
    			"ownedBy": "user1",
    			"addons": [],
    		}


�������� ����� ������ � ����������� �����(������)
	�����: POST
	URL: /todo/task/addWith
	HTTP-Headers: Content-Type: multipart/form-data; Authorization: Bearer + JWT
	��������: ������� ����� ������ ��� ������������. ������� ���������� multipart/form-data ��� �������� � ��-�� ������� ���������� JSON ��������� � ��������� �������.
	���������:
		header(������, ������������) - ������������ ������(4-256 ��������)
		info(������, ��������������) - �������� ������(0-1024 ��������)
		dateTime(������, ������������) - ����� ���������� ������ � ������� ��-��-���� 		��:��
	����� form-data:
		json(�����, ������������) - ���������
		file(����, ������������) - ����������� ����. ��������� ��������� ������ file
	�����:
	��� ���������: 200 OK
	���� ������:
 		id		- �������������, ����������� ������
		header  		- ������������ ������
		info	 	- �������� ������
		dateTime 	- ����� ���������� ������ � ������� ��-��-����  ��:��
		ownedBy		- ��� ������������, ���������� ������
		addons		- url-������ �� ����������� �����
	��� ���������: 400 BAD REQUEST
	���� ������:
 		message	- ����� ��������� �� ������
		timestamp	- ����� ������������� ������(����)
	������ �������:
	���� json:
		{ 
    			"header" : "Pay a credit card",
    			"info" : "",
    			"dateTime" : "05-06-2024 15:00"
		}
	���� file:
		card_information.txt
	�����:
		{
    			"id": 76,
    			"header": "Pay a credit card",
    			"info": "",
    			"dateTime": "05-06-2024 15:00",
    			"ownedBy": "user1",
		    	"addons": ["https://privatebucket.storage.yandexcloud.net/readme.txt?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240106T120234Z&X-Amz-SignedHeaders=host&X-Amz-Expires=86400&X-Amz-Credential=YCAJEi3SGbdpoNybjZN9tiU2Y%2F20240106%2Fru-central1%2Fs3%2Faws4_request&X-Amz-Signature=1339499b9572826971658c8fc342a7aa2c917968f0668a7beb876f5d6a6d2ef5"],
		}

��������� ������
	�����: POST
	URL: /todo/task/update/{id}
	HTTP-Header: Content-Type: application/json, Authorization: Bearer + JWT
	��������: �������� ������ � ��������� id, ����������� � ������ ����� �� ����������
	���������:
		header(������, ������������) - ������������ ������(4-256 ��������)
		info(������, ��������������) - �������� ������(0-1024 ��������)
		dateTime(������, ������������) - ����� ���������� ������ � ������� ��-��-���� 		��:��
	�����:
	��� ���������: 200 OK
	���� ������:
 		id		- ������������� ������
		header  		- ������������ ������
		info	 	- �������� ������
		dateTime 	- ����� ���������� ������ � ������� ��-��-����  ��:��
		ownedBy		- ��� ������������, ���������� ������
		addons		- url-������ �� ����������� �����(��� ������� addWith)
	��� ���������: 400 BAD REQUEST
	���� ������:
 		message - ����� ��������� �� ������
		timestamp - ����� ������������� ������(����)
	��� ���������: 404 NOT FOUND
	���� ������:
 		message - ����� ��������� �� ������
		timestamp - ����� ������������� ������(����)

	������ �������:
		{ 
    			"header" : "Pay another credit card",
    			"info" : "",
    			"dateTime" : "05-06-2025 15:00"
		}
	�����:
		{
    			"id": 76,
    			"header": "Pay another credit card",
    			"info": "",
    			"dateTime": "05-06-2025 15:00",
    			"ownedBy": "user1",
    			"addons": [],
		}

��������� ������
	�����: GET
	URL: /todo/task/get/{id}
	HTTP-Header: Authorization: Bearer + JWT
	��������: ��������� ������ ������������ �� ���������� id

	���������:
		id - id ������������� ������
	�����:
	��� ���������: 200 OK
	���� ������:
 		id		- ������������� ������
		header  		- ������������ ������
		info	 	- �������� ������
		dateTime 	- ����� ���������� ������ � ������� ��-��-����  ��:��
		ownedBy		- ��� ������������, ���������� ������
		addons		- url-������ �� ����������� �����(��� ������� addWith)
	��� ���������: 404 NOT FOUND
	���� ������:
 		message - ����� ��������� �� ������
		timestamp - ����� ������������� ������(����)
	������ �������:
		http://localhost:8080/todo/task/get/76
	�����:
		{
    			"id": 76,
    			"header": "Pay a credit card",
    			"info": "",
    			"dateTime": "05-06-2024 15:00",
    			"ownedBy": "user1",
    			"addons": ["https://privatebucket.storage.yandexcloud.net/readme.txt?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240106T120234Z&X-Amz-SignedHeaders=host&X-Amz-Expires=86400&X-Amz-Credential=YCAJEi3SGbdpoNybjZN9tiU2Y%2F20240106%2Fru-central1%2Fs3%2Faws4_request&X-Amz-Signature=1339499b9572826971658c8fc342a7aa2c917968f0668a7beb876f5d6a6d2ef5"],
  		}

�������� ������
	�����: POST
	URL: /todo/task/delete/{id}
	HTTP-Header: Authorization: Bearer + JWT
	��������: �������� ������ ������������ �� ���������� id
	���������:
		id - id ��������� ������
	�����:
	��� ���������: 200 OK
	���� ������:
 		message - ����� ��������� �� �������� �������� ������
		timestamp - ����� ���������� ��������(����)
	��� ���������: 404 NOT FOUND
	���� ������:
 		message - ����� ��������� �� ������
		timestamp - ����� ������������� ������(����)
	������ �������:
		http://localhost:8080/todo/task/delete/72
	�����:
		{
    			"message": "task deleted",
    			"timestamp": 1704544614437
		}
