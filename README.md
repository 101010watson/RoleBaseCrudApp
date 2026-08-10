# RoleBaseCrudApp

RoleBaseCrudApp is a Spring Boot + Spring MVC application demonstrating role-based CRUD operations and access control. It includes a simple Thymeleaf UI and REST endpoints to manage users and roles while enforcing permissions.

What it does
- Provides CRUD operations for Users and Roles.
- Enforces role-based authorization (e.g., ADMIN vs USER).
- Demonstrates form-based authentication and session handling.
- Shows layered architecture: Controllers -> Services -> Repositories.

Key features
- User registration and login
- Role-based access control (ADMIN, USER)
- Create / Read / Update / Delete users and roles
- Password hashing (BCrypt)
- Thymeleaf-based server-rendered UI + REST endpoints
- Configurable persistence (H2 by default, changeable to MySQL/Postgres)

Application flow
1. User starts the application and reaches the landing page/login screen.
2. A visitor can register (creates a USER by default) or an ADMIN can be pre-seeded.
3. After login, the user's role determines available UI actions and endpoint access.
4. ADMIN users can manage (create/update/delete) other users and assign roles.
5. Controller methods call Services which enforce business rules and call Repositories (Spring Data JPA) to persist data.

Run
- Build and run with Maven: mvn spring-boot:run
- Default profile uses an in-memory H2 database for quick testing. Update src/main/resources/application.properties to configure a production database.

Notes
- This project is intended as a learning/demo app for role-based security in Spring Boot. Contributions and improvements are welcome.
