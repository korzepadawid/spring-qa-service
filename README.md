# Spring Q&A Service

[![CircleCI](https://circleci.com/gh/korzepadawid/spring-qa-service/tree/master.svg?style=svg)](https://circleci.com/gh/korzepadawid/spring-qa-service/tree/master)
[![License: CC0-1.0](https://img.shields.io/badge/License-CC0%201.0-lightgrey.svg)](http://creativecommons.org/publicdomain/zero/1.0/)
[![GitHub issues](https://img.shields.io/github/issues/korzepadawid/spring-qa-service.svg)](https://GitHub.com/korzepadawid/spring-qa-service/issues/)
[![GitHub issues-closed](https://img.shields.io/github/issues-closed/korzepadawid/spring-qa-service.svg)](https://GitHub.com/korzepadawid/spring-qa-service/issues?q=is%3Aissue+is%3Aclosed)
[![Live Demo](https://img.shields.io/badge/demo-online-green.svg)](https://spring-qa-service.herokuapp.com/swagger-ui.html)
[![GitHub latest commit](https://badgen.net/github/last-commit/korzepadawid/spring-qa-service)](https://GitHub.com/korzepadawid/spring-qa-service/commit/)
[![Docker](https://badgen.net/badge/icon/docker?icon=docker&label)](https://https://docker.com/)
[![CircleCI](https://badgen.net/badge/icon/circleci?icon=circleci&label)](https://https://circleci.com/)
[![Maven](https://badgen.net/badge/icon/maven?icon=maven&label)](https://https://maven.apache.org/)

REST API for Q&A service built with Spring Boot. It provides the main features you'd expect from a
Q&A service, e.g. Quora.

[Live demo (Swagger)](https://spring-qa-service.herokuapp.com/swagger-ui.html)

> The project has been developed only for learning purposes.

## Table of content

- [Technologies](#technologies)
- [Features](#features)
- [Database schema](#database-schema)
- [Swagger](#swagger)
    - [How to use JWT authentication?](#how-to-use-jwt-authentication)
    - [How to use REST API?](#how-to-use-rest-api)
- [Launch](#launch)
- [License](#license)

## Technologies

This project built using Java 11 and the following tools:

- [Spring Boot](https://spring.io/projects/spring-boot) - Server side framework
- [Spring Security](https://spring.io/projects/spring-security) - Authentication and access-control
  framework
- [Maven](https://maven.apache.org/) - Build automation tool
- [Hibernate](https://hibernate.org/) - ORM / JPA implementation
- [Testcontainers](https://www.testcontainers.org/) - Docker-based tool for providing instances of
  common database systems
- [PostgreSQL](https://www.postgresql.org/docs/) - Object-relational database system
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - The top layer over Hibernate
- [CircleCI](https://circleci.com/) - CI/CD platform
- [Docker](https://www.docker.com/) - Platform for developing, shipping, and running applications

## Features

- JWT authentication.
- Users can ask questions.
- Questions can be anonymous, which means the author won't be displayed for others.
- Searching for questions by the specific keyword.
- It's possible to update/delete the question/answer/vote, but only by its author.
- Users can answer the question.
- Users can vote for answers.

## Database schema

![https://i.imgur.com/TmSernM.png](https://i.imgur.com/TmSernM.png)

## Swagger

### How to use JWT authentication?

Please, make sure you've already created an account.

![https://i.imgur.com/OgXflVS.gif](https://i.imgur.com/OgXflVS.gif)

- Send ``POST`` request to ``/api/v1/auth/login`` with your credentials
- Copy received access token
- Click button "Authorize"
- Type ``Bearer PASTE_ACCESS_TOKEN_HERE``
- You're ready to play with API!

### How to use REST API?

![https://i.imgur.com/xTz4oF2.png](https://i.imgur.com/xTz4oF2.png)

It's fairly easy to understand, you will find more
details [here (demo)](https://spring-qa-service.herokuapp.com/swagger-ui.html).

## Launch

### Prerequisites

- Java 11 or newer (full JDK not a JRE).
- Docker
- docker-compose

```
$ git clone https://github.com/korzepadawid/spring-qa-service.git
```

```
$ cd spring-qa-service
```

```
$ chmod +x mvnw
```

```
$ ./mvnw clean package
```

```
$ docker-compose up
```

## License

[Creative Commons Zero v1.0 Universal](https://creativecommons.org/publicdomain/zero/1.0/)