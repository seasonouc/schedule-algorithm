# Schedule Algorithm

This is a Spring Boot project that implements a schedule algorithm. It provides a RESTful API for managing schedules.

## Prerequisites

- Java 8 or higher
- Maven

## Getting Started

To run the project locally, follow these steps:

1. Clone the repository:

   ```shell
   git clone https://github.com/your-username/schedule-algorithm.git
   ```

2. Navigate to the project directory:

   ```shell
   cd schedule-algorithm
   ```

3. Build the project:

   ```shell
   mvn clean install
   ```

4. Run the application:

   ```shell
   mvn spring-boot:run
   ```

5. Open your web browser and access the Swagger UI documentation at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

## API Endpoints

The following API endpoints are available:

- `GET /api/schedules`: Get all schedules.
- `GET /api/schedules/{id}`: Get a schedule by ID.
- `POST /api/schedules`: Create a new schedule.
- `PUT /api/schedules/{id}`: Update a schedule by ID.
- `DELETE /api/schedules/{id}`: Delete a schedule by ID.

For detailed information about the request and response formats, refer to the Swagger UI documentation.

## Configuration

The application can be configured by modifying the `application.properties` file located in the `src/main/resources` directory.

## Testing

To run the tests, execute the following command:

```shell
mvn test
```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.