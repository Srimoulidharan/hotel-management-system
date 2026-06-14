# Hotel Management System - JSP + Servlet

A complete end-to-end Hotel Management System built with JSP, Servlet, JDBC, H2 Database and Maven.

## Features

- Admin login and logout
- Dashboard with room, guest and booking statistics
- Room management: add, update, delete, status tracking
- Guest management: add, update, delete
- Booking management: reserve rooms, check in, check out, cancel
- Date-overlap validation to prevent double booking
- Auto-created H2 database with seed data
- Clean JSP UI with local CSS, no internet dependency for styling

## Default Login

```text
Username: admin
Password: admin123
```

## Requirements

- JDK 11 or higher
- Apache Maven 3.8+
- Apache Tomcat 9.x

> This project uses `javax.servlet`, so Tomcat 9 is recommended. Tomcat 10+ uses `jakarta.servlet` and will require package migration.

## Run Option 1: Build WAR and deploy to Tomcat

1. Extract this ZIP.
2. Open a terminal inside the project folder.
3. Build the WAR:

   ```bash
   mvn clean package
   ```

4. Copy the generated WAR file:

   ```bash
   target/hotel-management-system.war
   ```

   into your Tomcat `webapps` folder.

5. Start Tomcat:

   ```bash
   # Linux/macOS
   <tomcat-folder>/bin/startup.sh

   # Windows
   <tomcat-folder>\bin\startup.bat
   ```

6. Open:

   ```text
   http://localhost:8080/hotel-management-system
   ```

## Run Option 2: Run from an IDE

1. Import the folder as a Maven project in IntelliJ IDEA, Eclipse or NetBeans.
2. Configure Apache Tomcat 9 as the server.
3. Deploy the artifact as a WAR exploded deployment.
4. Start the server and open:

   ```text
   http://localhost:8080/hotel-management-system
   ```

## Database

The app uses an embedded H2 database stored in your home directory:

```text
~/hotel_management_system.mv.db
```

The database tables and sample data are created automatically on first run.

To reset the database, stop Tomcat and delete:

```text
~/hotel_management_system.mv.db
~/hotel_management_system.trace.db
```

Then restart the application.

## Project Structure

```text
src/main/java/com/hotel/config      Database connection and initialization
src/main/java/com/hotel/dao         DAO/JDBC classes
src/main/java/com/hotel/filter      Authentication filter
src/main/java/com/hotel/model       Model classes
src/main/java/com/hotel/servlet     Servlet controllers
src/main/java/com/hotel/util        Utility classes
src/main/webapp/WEB-INF/views       JSP pages
src/main/webapp/assets/css          CSS
```

## Notes for College/Project Submission

This project is intentionally simple and easy to explain:

- Servlet works as Controller
- DAO works as database access layer
- JSP works as View
- H2 stores the hotel data locally
- Authentication is session based

For production use, add stronger security, role management, audit logging, and password reset flows.
