# Filmorate

---

### An app for interacting with film productions

## Database diagram
<img alt="DB diagram" height="600" src="FilmorateDiagram.svg" width="900"/>

## Endpoints 

---

## Film

| Method | Endpoint                  | Description               |
|--------|---------------------------|---------------------------|
| POST   | /films                    | Creates a new film        |
| PUT    | /films                    | Updated film              |
| PUT    | /films/{id}/like/{userId} | Like the Film             |
| GET    | /films/{id}               | Get film by id            |
| GET    | /films/popular            | Get popular films         |
| GET    | /films                    | Get all films             |
| DELETE | /films/{id}               | Remove film by id         |
| DELETE | /films/{id}/like/{userId} | Remove film's like by id  |

## User 

| Method | Endpoint                             | Description                    |
|--------|--------------------------------------|--------------------------------|
| POST   | /users                               | Creates a new user             |
| PUT    | /users                               | Updated user                   |
| PUT    | /users/{id}/friends/{friendId}       | Adds a friend                  |
| GET    | /users/{id}                          | Get users by id                |
| GET    | /users                               | Get all users                  |
| GET    | /users/{id}/friends                  | Get friends by user id         |
| GET    | /users/{id}/friends/common/{otherId} | Get common friends by user id  |
| DELETE | /users/{id}/friends/{friendId}       | Remove from friends by user id |
| DELETE | /users/{id}                          | Remove user by id              |

## Mpa

| Method | Endpoint                             | Description     |
|--------|--------------------------------------|-----------------|
| GET    | /mpa                                 | Get all mpa     |
| GET    | /mpa/{id}                            | Get mpa by id   |

## Genre

| Method | Endpoint                       | Description     |
|--------|--------------------------------|-----------------|
| GET    | /genre                         | Get all genres  |
| GET    | /genre/{id}                    | Get genre by id |




