REACT APPLICATION
=================

Contents
---------------------

 * Introduction
 * Requirements
 * Usage

Introduction
------------

This mini application has a single form and a REST service on the backend.
The form contains user information, which is saved to the database upon
submission. The saved data is also managed in the session, so when the user
refreshes the page, the inputted values will not be lost.

The user data consists of the user's name and a selection of business sectors,
which describe the user. The user is also required to accept the terms every time
when he submits the form. The user has to specify his/her name and provide at least
one business sector.

The backend is covered with 20 unit tests, which cover 96% of the functionality.

#### Used technologies:

* Spring MVC 4.2.7
* Spring Boot 1.3.6
* ReactJS 15.2.1
* PostgreSQL 9.5

Requirements
------------

 * Gradle (2.3+)
 * Java 8

Usage
-----
Place correct database credentials to *src/main/resources/application-prod.properties* properties file.

Build the project with
```
./gradlew build
```

Start the web application with
```
java -jar build/libs/example-application-1.0.jar
```

Navigate to the main page: [http://localhost:8080](http://localhost:8080)