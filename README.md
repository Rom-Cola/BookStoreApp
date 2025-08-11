# BookStoreApp 

## What is this project?

Welcome to BookStoreApp! This is an API (a backend service) for an online bookshop. I built this project using Spring Boot. It lets you manage books, user accounts, shopping carts, and orders.

The main idea is to create a simple and working backend for a bookshop website or app.

## Tools I Used

Here are the main technologies and tools I used for this project:

*   **Spring Boot**: The main framework to build the application.
*   **Spring Security**: To keep the app safe and manage user login with JWT tokens.
*   **Spring Data JPA**: To work with the database easily.
*   **MySQL**: The database to save all the information.
*   **Liquibase**: To manage changes to the database structure.
*   **Lombok**: To write less repetitive code.
*   **MapStruct**: To help convert data between different parts of the app.
*   **Swagger (OpenAPI)**: To create API documentation and let you test it in the browser.
*   **Docker**: To run the app and database easily in any environment.
*   **Checkstyle**: To check the code and keep it clean.

## What the API Can Do

Here is a list of what you can do with the API, based on your role.

#### For All Visitors (no login needed)

*   `POST /api/auth/register` - Let a new user sign up.
*   `POST /api/auth/login` - Let a user log in and get a special key (token) to use the API.

#### For Logged-in Users (Role: USER)

*   `GET /api/books` - Get a list of all books.
*   `GET /api/books/{id}` - Get one book by its ID.
*   `GET /api/categories` - Get a list of all book categories.
*   `GET /api/categories/{id}` - Get one category by its ID.
*   `GET /api/categories/{id}/books` - Get all books in a specific category.
*   `GET /api/cart` - Shows what is in the user's shopping cart.
*   `POST /api/cart` - Add a book to the shopping cart.
*   `PUT /api/cart/items/{cartItemId}` - Change the number of books for an item in the cart.
*   `DELETE /api/cart/items/{cartItemId}` - Remove an item from the cart.
*   `GET /api/orders` - Shows a user's past orders.
*   `POST /api/orders` - Create an order from the shopping cart.
*   `GET /api/orders/{orderId}/items` - Shows all the items in one order.
*   `GET /api/orders/{orderId}/items/{itemId}` - Shows a specific item from an order.

#### For Admins (Role: ADMIN)

*   `POST /api/books` - Add a new book.
*   `PUT /api/books/{id}` - Update a book's information.
*   `DELETE /api/books/{id}` - Delete a book.
*   `POST /api/categories` - Create a new category.
*   `PUT /api/categories/{id}` - Update a category.
*   `DELETE /api/categories/{id}` - Delete a category.
*   `PATCH /api/orders/{id}` - Change the status of an order (like "DELIVERED").

## How to Run This Project

This project runs completely inside Docker containers. You do not need to install Java or MySQL on your machine.

**Prerequisites:**
*   Git
*   Docker
*   Docker Compose

Follow these simple steps to run the project.

1.  **Get the code:**
    ```bash
    git clone https://github.com/Rom-Cola/BookStoreApp.git
    cd BookStoreApp
    ```

2.  **Configure Your Environment:**
    In the main project folder, find the file named `.env.template`. **Rename this file to `.env`**.

    Next, open your new `.env` file and fill in the required values. Below is a description of each field.

    | Variable                | Description                                                          | Example                   |
    | :---------------------- | :------------------------------------------------------------------- | :------------------------ |
    | `MYSQLDB_ROOT_PASSWORD` | The admin password for the entire MySQL database service.            | `my_strong_root_password` |
    | `MYSQLDB_DATABASE`      | The name of the database the application will use.                   | `book_store_db`           |
    | `MYSQLDB_USER`          | The username the application will use to connect to the database.    | `my_app_user`             |
    | `MYSQLDB_PASSWORD`      | The password for the `MYSQLDB_USER`.                                 | `my_app_password`         |
    | `MYSQL_LOCAL_PORT`      | The port on your computer that connects to the MySQL container.      | `3306`                    |
    | `MYSQL_DOCKER_PORT`     | The port MySQL uses inside the Docker network. **Do not change.**    | `3306`                    |
    | `SPRING_LOCAL_PORT`     | The port on your computer that connects to the application.          | `8088`                    |
    | `SPRING_DOCKER_PORT`    | The port the app uses inside the Docker network. **Do not change.**    | `8080`                    |
    | `DEBUG_PORT`            | The port used for remote debugging with an IDE.                      | `5005`                    |

3.  **Package the Application into a .jar File:**
    This is very important first step. Before Docker can build an image of your application, the application itself must be compiled and packaged into a `.jar` file.

    To do this, run the correct command for your operating system from the root folder of the project. You do **not** need to have Maven installed, as the project uses the Maven Wrapper (`mvnw`).

    *   On **Linux, macOS, or Git Bash** for Windows:
        ```bash
        ./mvnw clean package
        ```

    *   On **Windows Command Prompt (CMD)** or **PowerShell**:
        ```bash
        mvnw clean package
        ```

    This command will download the correct Maven version (the first time only) and then build your project. It might take a minute or two. When it's done, you will see a new `target` directory in your project folder, containing the `.jar` file.


4.  **Build and Run the Application:**
    Use Docker Compose to build the images and start the containers. This command will start both the Spring Boot app and the MySQL database.
    ```bash
    docker-compose up --build
    ```

5.  **Check the API Docs:**
    After the app starts, you can see all the API documentation here: [(http://localhost:8088/swagger-ui/index.html](http://localhost:8088/swagger-ui/index.html).
    *(Note: if you changed `SPRING_LOCAL_PORT` in your `.env` file, use that port instead of `8088`)*.

6.  **How to Stop the Application:**
    To stop and remove the containers, run:
    ```bash
    docker-compose down
    ```

## Testing

All tests are designed to be completely isolated to ensure they are reliable and do not interfere with each other or any external database.

*   **Unit Tests**: We use **JUnit** and **Mockito** to test individual components in isolation. This allows us to check the business logic of services like `BookService` and `CategoryService` without needing a database or other dependencies.

*   **Integration Tests**: For integration tests on the Controller layer (`BookController`, `CategoryController`), we use **Testcontainers**. This library starts a real MySQL database in a Docker container just for the test run. This means our tests are run against a real database environment, guaranteeing that our queries and controller logic work correctly, while still being fully isolated and repeatable.

You can run all tests using the standard Maven command:
```
bash
./mvnw test
```

## Challenges and Solutions

This project involved solving interesting technical problems, which helped me improve my professional skills.

*   **Advanced Testcontainers Configuration:** The main technical challenge was to run Testcontainers inside a Docker environment. This is a complex setup that requires a deep understanding of Docker networking. To solve this, I implemented and **correctly configured a `CustomMySqlContainer`**. This solution ensured that the tests could communicate with the database container properly. This experience proves my ability to handle advanced Docker configurations and build a reliable, fully isolated testing environment.

*   **Building a Secure and Stateless API:** A key goal was to build a modern, **stateless** API. I used Spring Security to implement a robust security system based on **JWT (JSON Web Tokens)**. This approach means the server does not store any user session information, which makes the application more scalable and easier to manage. My focus was on correctly configuring the security filters to handle token generation and validation for every request. This ensures that the API is truly stateless and that all endpoints are properly protected.

While the challenges with testing and security were the most memorable, every technology in this project required a deep dive. Each step was an opportunity to learn and find the best solution, not just a working one. By solving these challenges, I not only completed the project but also developed practical, valuable skills in backend development and testing.
