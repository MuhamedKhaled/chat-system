# chats

Chats is a system that create chatting systems.

## Stack

| Category         | Tool                                                                | Usage                                        |
|:-----------------|:--------------------------------------------------------------------|:---------------------------------------------|
| Backend server   | [Java Spring](https://spring.io/)                                   |                                              |
| Migration        | Flyway                                                            |                                              |            
| Database         | [MySQL](https://www.mysql.com/)                                     |                                              |
| Chaching         | [Redis](https://redis.io/)                                          |                                              |
| Queueing         | [RabbitMQ](https://www.rabbitmq.com/)                               | Queuing/consuming  chats & messages to be created                          |
| Containerization | [Docker](https://www.docker.com/)                                   | Base environment for the stack to run inside |

### Solution Overview
#### Design
![architecture](https://github.com/MuhamedKhaled/chat-system/blob/main/architecture.jpg)

### How to run the project ?

- You need to have [docker](https://www.docker.com/) and [docker-compose](https://docs.docker.com/compose/) installed
- Run

  ```shell
   docker-compose up
   ```

- get a cup of coffee this is going to take a while --build

#### Endpoints
| Method | URL                                                                              | Body                                                 | Description                                                                                                                                                     |
|--------|----------------------------------------------------------------------------------|------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| GET    | /api/applications?page=1&limit=10                                        | None                                                 | Retreive all applications, page and limit defaults are (1,10).                                                                                                  |
| GET    | /api/applications/:application_token                                             | None                                                 | Get single application by token.                                                                                                                                |
| POST   | /api/applications/:application_token                                             | { "name": "Application 1" }                                      | Create application.                                                                                                                                             |
| PUT    | /api/applications/:application_token                                             | { "name": "New App Name" }                        | Update application.                                                                                                                                             |
| DELETE | /api/applications/:application_token                                             | None                                     | Delete application.                                                                                                                                             |
| GET    | /api/applications/:application_token/chats                                       | None                                                 | Get all chats            |
| GET    | /api/applications/:application_token/chats/:chat_number                          | None                                                 | Get chat.                                                                                                                                                       |
| POST   | /api/applications/:application_token/chats                                       | None                                                 | Create chat.                                                                                                                                                    |
| GET    | /api/applications/:application_token/chats/:chat_number/messages?query=xx        | None                                                 | Retrieve all messages or search for specific message If you included query parameter, also you can change page, limit parameters which are defaulted to (1,10). |
| GET    | /api/applications/:application_token/chats/:chat_number/messages/:message_number | None                                                 | Get message.                                                                                                                                                    |
| POST   | /api/applications/:application_token/chats/:chat_number/messages                 | { "body": "message_body" }                           | Create message.                                                                                                                                                 |

## To Do
- [ ] use Swagger open Api
