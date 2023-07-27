# Curatorium

## О проектке

Curatorium - сервис, предоставляющий услуги для управления ведомостями. 

![Alt text](images/image.png)

## Технологии

### frontend

* Vue 3
* PrimeVue
* Axios
* Vue-router
* Js-cookie
* Jwt-decode
* Vuex 

### backend

* Spring Boot 3 (включая стартер для тестов и configuration processor)
* Spring Data jpa 
* Spring Security
* Spring MVC
* OpenApi Docs
* Hibernate Validation
* Gradle
* Liquibase
* Lombok
* Mapstruct
* JJWT
* H2  


## Исспользование

### Запуск сервиса

```
docker compose up
```

После запуска будут созданы 2 Docker-образа:

* curatorium-client
* curatorium-server

Также будут созданы 3 контейнера:

* curatorium-server-app
* curatorium-client-app
* curatorium-server-db

Сервис работает на 3 портах:

* 5173 - frontend
* 8081 - backend
* 5432 - postgresql

### Завершение работы сервиса

```
docker compose down
```

## Возможности

Благодаря Curatorium вы можете:

1. Вести учет учащихся, состоящих в одной группе.

   ![Alt text](images/image-1.png)

2. Просмотр информации о группе и её изменение.

    ![Alt text](images/image-2.png)

3. Управление предметами (дисциплинами)

    ![Alt text](images/image-3.png)

4. Создание, изменение, удаление и ведение ведомостей по группе.

    ![Alt text](images/image-4.png)

    ![Alt text](images/image-5.png)

## Изображения

![Alt text](images/image-6.png)

![Alt text](images/image-7.png)

![Alt text](images/image-8.png)

![Alt text](images/image-9.png)

![Alt text](images/image-10.png)

![Alt text](images/image-11.png)

![Alt text](images/image-12.png)

![Alt text](images/image-13.png)