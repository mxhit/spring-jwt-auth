# JWT Authentication and Authorization using Spring Security

The aim of this project was to tinker around with **Spring Security** and implement **JWT** using user-defined roles.

## Tools

- Java `11`
- Spring Boot `2.7.4`
- PostgreSQL `14`

## Usage

- Update `spring.datasource` values in `application.properties`
- Update the expiration time for `access_token` (`default: 10 minutes`) and `refresh_token` (`default: 30 minutes`) in `FilterUtils` according to your needs. 

## Future

This project will act as a template for my future Spring Boot projects that require JWT-based authentication and authorization. The structure of this project is fairly simple so refactoring can be done with ease, as and when necessary based on the requirements of new projects.