## To start the application with all dependencies.
```bash
docker compose up
```

## Swagger UI should be visible at 
```
http://localhost:8080/swagger-ui/index.html
```

## To see the test coverage report
```bash
./mvnw clean test
```
report will be generated at `target/site/jacoco/index.html`

## Tech Stack
* Java
* Spring
* PostgreSQL
* H2 (for test db)
* Docker as a containerization tool
* Swagger for API documentation

## Design Choices
As I didn't want to overcomplicate stuff most of the logic is in the services. 
I didn't feel the need to divide the services into smaller services as the logic was simple enough to be in one service.

Most of the validation is done with `Jakarta` as IMO there's no need to do that by hand.
Also I've used `@ControllerAdvice` to handle application exceptions globally.

Other than that everything else is straightforward.

As per tests. I've written tests for services, controllers & mappers. Other tests are not needed as they are mostly boilerplate code.
