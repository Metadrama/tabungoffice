# Tabung Office

A small web app for keeping track of shared office expenses and voluntary contributions - for dinners, sports, farewells, and similar events.

## Run it

The project requires Java 21. Spring Boot and the other dependencies are defined in 'pom.xml', so no separate Spring Boot installation is needed.

Choose whichever is easier:

**Using Eclipse**

1. Import the project into Eclipse.
2. Run 'Main.java' as a Java application.
3. Open http://localhost:8080

**Using the runnable JAR**

Download the latest JAR from the Github Release page, then run:

```bash
java -jar tabungoffice-0.0.1-SNAPSHOT.jar
```

Open [http://localhost:8080](http://localhost:8080) when the application starts.

## How to use

For example, when coworkers are collecting money for a farewell or office dinner, one person can create a tabung and everyone can record their contribution in one shared place.

1. Register with your name, phone number, and password, then log in.
2. Create a tabung for an event.
3. Open the tabung and add each contribution by member and amount.

The REST API is served from the same address:

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `POST` | `/register` | Register a user |
| `POST` | `/login` | Log in |
| `GET` | `/tabungs` | List tabungs |
| `POST` | `/tabungs` | Create a tabung |
| `GET` | `/tabungs/{id}` | View a tabung summary |
| `POST` | `/tabungs/{id}/contributions` | Add a contribution |
| `GET` | `/tabungs/{id}/contributions` | List contributions |


## Database

The app stores its H2 database in `data/tabungoffice.mv.db` and the folder is ignored by Git in '.gitignore'.

## Stack

Spring Boot, Spring Data JPA, H2, and a small HTML/CSS/JavaScript frontend.