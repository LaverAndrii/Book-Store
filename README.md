# Book store
Welcome to BookStore, online shop powered by Java Spring Boot!
This project makes it easy to search and buy books.
Users have the ability to browse a wide range of books across various genres, explore their descriptions, learn about authors, and discover prices.
## Technologies Used

* **Programming Language:** Java
* **Application Configuration:** Spring Boot, Spring, Lombok, Mapstruct, Maven
* **Accessing Data:** Spring Data JPA, Hibernate, MySQL
* **Web Development:** Spring MVC, Tomcat
* **Security:** Spring Security, JWT
* **Database Migration:** Liquibase
* **Containerization:** Docker
* **API Documentation:** Swagger
* **Testing:** JUnit, Mockito, Test Containers
* **Version Control:** Git

## Functionality
### User Functionality

* **Authentication:** Register and log in.
* **Browse Books:** View and search for books.
* **Shopping Cart:** Add, view, and remove items.
* **Order Management:** Place orders and view order history.

### Admin Functionalities

* **Book Management:** Add, update, and delete books.
* **Category Management:** Manage book categories.
* **Order Management:** Update order statuses.

## Challenges Faced
The most memorable case is database migration. Implemented using Liquibase for smooth schema updates.
As a result, I got a much better understanding of databases
## How to run Book Store API
1. Install [Docker](https://www.docker.com/products/docker-desktop/)
2. Clone [current project](https://github.com/fedorovychh/book-store) repository
3. Configure a "**.env**" file with necessary environment variables:
    - change user
    - change password
4. Your spring.datasource url, username and password should match .env config
5. Run the command `mvn clean package`
6. Use `docker-compose build` to build Docker container
7. Use `docker-compose up` to run Docker container
8. Access the locally running application at http://localhost:8088/api

## Video demonstation

[![Watch the video](https://img.youtube.com/vi/YJujjJcWaGU/0.jpg)](https://www.youtube.com/watch?v=YJujjJcWaGU)

## API Documentation
### Authentication
<details>
   <summary>Registration</summary>

* HTTP Request: POST ```/api/auth/register```
* Request Body:
  ```
  {
    "email": "test.user@example.com",
    "password": "12345678",
    "repeatPassword": "12345678",
    "firstName": "Test",
    "lastName": "User",
    "shippingAddress": "123 Main St, City, Country"
  }
  ``` 
  Response Body:
  ```
  {
    "id": 1,
    "email": "test.user@example.com",
    "firstName": "Test",
    "lastName": "User",
    "shippingAddress": "123 Main St, City, Country"
  }
  ```   
</details>
<details>
   <summary>Login</summary>

* HTTP Request: POST ```/api/auth/login```
* Request Body:
  ```
  {
      "email": "test.user@example.com",
      "password": "12345678"
  }
  ```  
* Response Body:
  ```
  {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
  }
  ```
</details>

### Books and Categories
<details>
   <summary>Available books</summary>

* HTTP Request: GET ```/api/books```
* Response Body:
  ```
  [
      {
          "id": 1,
          "title": "Test Book",
          "author": "Test Author",
          "isbn": "9783161484100",
          "price": 17.50,
          "description": "Description for test book",
          "coverImage": "https://www.example.com/images/book3.jpg",
          "categories": [1]
      },
      {
          "id": 2,
          "title": "Test Book 2",
          "author": "Test Author 2",
          "isbn": "9783161484101",
          "price": 21.00,
          "description": "Description for test book 2",
          "coverImage": "https://www.example.com/images/book3.jpg",
          "categories": [1]
      }
  ]
  ```    
</details>
<details>
   <summary>Certain book</summary>

* HTTP Request: GET ```/api/books/1```
* Response Body:
  ```
  {
      "id": 1,
      "title": "Test Book",
      "author": "Test Author",
      "isbn": "9783161484100",
      "price": 17.50,
      "description": "Description for test book",
      "coverImage": "https://www.example.com/images/book3.jpg",
      "categories": [1]
  }
  ```
</details>
<details>
   <summary>Available categories</summary>

* HTTP Request: GET ```/api/categories```
* Response Body:
  ```
  [
      {
          "id": 1,
          "name": "Fantasy Adventure",
          "description": "Fantasy adventure books"
      },
      {
          "id": 2,
          "name": "Dystopian Fiction",
          "description": "Novels in a dystopian setting"
      },
      {
          "id": 3,
          "name": "Post-Apocalyptic Fiction",
          "description": "Post-apocalyptic novels"
      }
  ]
  ```
</details>
<details>
   <summary>Certain category</summary>

* HTTP Request: GET ```/api/categories/1```
* Response Body:
  ```
  {
      "id": 1,
      "name": "Fantasy Adventure",
      "description": "Fantasy adventure books"
  }
  ```
</details>
<details>
   <summary>Available books by category</summary>

* HTTP Request: GET ```/api/categories/1/books```
* Response Body:
  ```
  [
      {
          "id": 1,
          "title": "Test Book",
          "author": "Test Author",
          "isbn": "9783161484100",
          "price": 17.50,
          "description": "Description for test book",
          "coverImage": "https://www.example.com/images/book3.jpg",
      }
  ]
  ```   
</details>

### Shopping Cart
<details>
   <summary>Add book to shopping cart</summary>

* HTTP Request: POST ```/api/cart```
* Request Body:
  ```
  {
    "bookId": "1",
    "quantity": "3"
  }
  ```
* Response Body:
  ```      
  {
      "id": 1,
      "userId": 1,
      "cartItems": [
          {
              "id": 1,
              "bookId": 1,
              "bookTitle": "Test Book",
              "quantity": 3
          }
      ]
  }
  ```
</details>
<details>
   <summary>User's shopping cart</summary>

* HTTP Request: GET ```/api/cart```
* Response Body:
  ```
  [
      {
          "id": 1,
          "userId": 1,
          "cartItems": [
              {
                  "id": 1,
                  "bookId": 1,
                  "bookTitle": "Test Book",
                  "quantity": 3
              }
          ]
      }
  ]
  ``` 
</details>
<details>
   <summary>Update item in shopping cart</summary>

* HTTP Request: PUT ```/api/cart/cart-items/1```
* Request Body:
  ```
  {
    "quantity": "2"
  }
  ```
* Response Body:
  ```
  {
      {
          "id": 1,
          "userId": 1,
          "cartItems": [
              {
                  "id": 1,
                  "bookId": 1,
                  "bookTitle": "Test Book",
                  "quantity": 2
              }
          ]
      }
  }
  ```   
</details>
<details>
   <summary>Delete item from shopping cart</summary>

* HTTP Request: DELETE ```/api/cart/cart-items/1```
</details>

### Orders
<details>
   <summary>Place an order</summary>

* HTTP Request: POST ```/api/orders```
* Request Body:
  ```
  {
      "shippingAddress": "123 Main St, City, Country"
  }
  ```
* Response Body:
  ```
  {
      "id": 1,
      "userId": 1,
      "orderItems": [
          {
              "id": 1,
              "bookId": 1,
              "quantity": 3
          }
      ],
      "orderDate": "2024-01-07T12:02:51.316180965",
      "total": 52.50,
      "status": "PENDING"
  }
  ```


</details>
<details>
   <summary>Orders history</summary>

HTTP Request: GET ```/api/orders```

         Response Body:
  ```
  [
    {
        "id": 1,
        "userId": 1,
        "orderItems": [
            {
                "id": 1,
                "bookId": 1,
                "quantity": 3
            }
        ],
        "orderDate": "2024-01-07T12:02:51.316180965",
        "total": 52.50,
        "status": "PENDING"
    }
]
```   
</details>
<details>
   <summary>Certain order</summary>

* HTTP Request: GET ```/api/orders/1/items```
* Response Body:
  ```
  [
      {
          "id": 1,
          "bookId": 1,
          "quantity": 3
      }
  ]
  ```  
</details>
<details>
   <summary>Certain item from certain order</summary>

* HTTP Request: GET ```/api/orders/1/items/1```
* Response Body:
  ```
  {
      "id": 1,
      "bookId": 1,
      "quantity": 3
  }
  ``` 
</details>

## For administrators
### Books and categories
<details>
   <summary>Create new book</summary>

* HTTP Request: POST ```/api/books```
* Request Body:
  ```
  {
      "title": "Test Book",
      "author": "Test Author",
      "isbn": "9783161484100",
      "price": 17.50,
      "description": "Description for test book",
      "coverImage": "https://www.example.com/images/book3.jpg",
      "categories": [1]
  }
  ```
* Response Body:
  ```
  {
      "id": 1,
      "title": "Test Book",
      "author": "Test Author",
      "isbn": "9783161484100",
      "price": 17.50,
      "description": "Description for test book",
      "coverImage": "https://www.example.com/images/book3.jpg",
      "categories": [1]
  }
  ```
</details>
<details>
   <summary>Update existing book</summary>

* HTTP Request: PUT ```/api/books/1```
* Request Body:
  ```
  {
      "title": "Test Book",
      "author": "Test Author",
      "isbn": "9783161484100",
      "price": 27.50,
      "description": "Description for test book",
      "coverImage": "https://www.example.com/images/book3.jpg",
      "categories": [1]
  }
  ```
* Response Body:
  ```
  {
      "id": 1,
      "title": "Test Book",
      "author": "Test Author",
      "isbn": "9783161484100",
      "price": 27.50,
      "description": "Description for test book",
      "coverImage": "https://www.example.com/images/book3.jpg",
      "categories": [1]
  }
  ```   
</details>
<details>
   <summary>Delete existing book</summary>

* HTTP Request: DELETE ```/api/books/1```
</details>
<details>
   <summary>Create category</summary>

* HTTP Request: POST ```/api/categories```
* Request Body:
  ```
  {
      "name": "Horror",
      "description": "Horror books"
  }
  ```
* Response Body:
  ```
  {
      "id": 4,
      "name": "Horror",
      "description": "Horror books"
  }
  ```
</details>
<details>
   <summary>Update existing category</summary>

* HTTP Request: PUT ```/api/categories/1```
* Request Body:
  ```
  {
      "name": "Horror",
      "description": "Horror and not only books"
  }
  ```
* Response Body:
  ```
  {
      "id": 4,
      "name": "Horror",
      "description": "Horror and not only books"
  }
  ```  
</details>
<details>
   <summary>Delete existing category</summary>

* HTTP Request: DELETE ```/api/categories/1```
</details>

### Orders
<details>
   <summary>Update existing order</summary>

* HTTP Request: PUT ```/api/orders/1```
* Request Body:
  ```
  {
      "status": "CONFIRMED"
  }
  ```
* Response Body:
  ```
  [
      {
          "id": 1,
          "userId": 1,
          "orderItems": [
              {
                  "id": 1,
                  "bookId": 1,
                  "quantity": 3
              }
          ],
          "orderDate": "2024-01-07T12:02:51.316180965",
          "total": 52.50,
          "status": "CONFIRMED"
      }
]
```
</details>