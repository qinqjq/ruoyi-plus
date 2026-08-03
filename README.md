# RuoYi Plus

An admin management platform built on **RuoYi-Vue-Plus 2.4.0**. The project uses a Spring Boot backend and a Vue 2 frontend, and provides a practical foundation for building role-based internal systems, dashboards, and CRUD applications.

## Features

- User, role, menu, department, and position management
- Role-based access control with Spring Security and JWT
- Data-scope permissions for organizational data
- Dictionary, system configuration, and notice management
- Login, operation, online-user, cache, and database-pool monitoring
- Code generation for backend and frontend CRUD modules
- Redis integration, distributed locking, and multiple data-source support
- API documentation powered by Knife4j/Swagger
- Responsive Vue dashboard with Element UI
- Tree and map demonstration pages

## Technology Stack

| Layer | Technology |
| --- | --- |
| Backend | Java 8, Spring Boot 2.4.7, Spring Security |
| Persistence | MyBatis-Plus 3.4.3, Druid, MySQL |
| Cache | Redis, Redisson |
| Authentication | JWT |
| API documentation | Knife4j 3.0.2 / Swagger |
| Frontend | Vue 2.6.12, Vue Router, Vuex |
| UI | Element UI 2.15.6, ECharts |
| Build tools | Maven, Vue CLI 4 |

## Project Structure

```text
ruoyi-plus/
|-- ruoyi-admin/       # Backend application entry point and REST controllers
|-- ruoyi-common/      # Shared models, annotations, constants, and utilities
|-- ruoyi-framework/   # Security, web, persistence, and infrastructure config
|-- ruoyi-generator/   # CRUD code generator
|-- ruoyi-system/      # Users, roles, menus, departments, and system services
|-- ruoyi-ui/          # Vue 2 frontend
|-- sql/               # Database initialization and optional demo scripts
`-- pom.xml            # Maven parent project
```

## Prerequisites

Install the following software before starting:

- JDK 8
- Maven 3.6 or later
- MySQL or a compatible database
- Redis
- Node.js 8.9 or later and npm 3 or later, as declared by the frontend package

> This project uses an older Vue CLI toolchain. If dependency installation fails with a recent Node.js release, use a compatible LTS release through a Node version manager.

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/qinqjq/ruoyi-plus.git
cd ruoyi-plus
```

### 2. Initialize the database

Create a database, then import:

```text
sql/ry_20210210.sql
```

Optional scripts:

- `sql/test.sql` adds demonstration tables, menus, roles, and users.
- `sql/quartz.sql` creates Quartz tables. The Quartz module is disabled in the current Maven configuration and should only be imported if you enable that module.

### 3. Configure the backend

The default active Spring profile is `prod`. Update the database and Redis settings in:

```text
ruoyi-admin/src/main/resources/application-prod.yml
```

General application settings are located in:

```text
ruoyi-admin/src/main/resources/application.yml
```

The backend uses port `9099` and context path `/ruoyi` by default:

```text
http://localhost:9099/ruoyi
```

Before deploying, replace all environment-specific addresses and credentials, change the default JWT secret, and configure a suitable upload directory.

### 4. Start the backend

For development, run the following class from your IDE:

```text
ruoyi-admin/src/main/java/com/ruoyi/RuoYiApplication.java
```

Alternatively, package and start the application with Maven and Java:

```bash
mvn clean package -DskipTests
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

### 5. Configure and start the frontend

The development proxy in `ruoyi-ui/vue.config.js` currently points to a LAN address. Change its `target` to your backend address when running locally:

```js
target: 'http://localhost:9099/ruoyi'
```

Then install the dependencies and start the development server:

```bash
cd ruoyi-ui
npm install
npm run dev
```

The frontend development server uses port `80` by default. You can override the port if it is already in use or requires elevated permissions.

## Default Account

After importing the main SQL script, use the default administrator account:

```text
Username: admin
Password: admin123
```

Change the default password immediately in any shared or production environment.

## API Documentation

When Swagger is enabled and the backend is running, Knife4j documentation is available at:

```text
http://localhost:9099/ruoyi/doc.html
```

## Production Build

Build the frontend with:

```bash
cd ruoyi-ui
npm run build:prod
```

The generated static files are written to `ruoyi-ui/dist`. Deploy them with a web server such as Nginx and proxy the production API path to the backend.

## Security Notes

- Do not commit production database, Redis, or third-party service credentials.
- Replace default credentials and cryptographic secrets before deployment.
- Disable Swagger and demo features when they are not required in production.
- Review CORS, upload paths, logging, and reverse-proxy settings for your environment.

## Contributing

1. Fork this repository.
2. Create a feature branch.
3. Commit your changes with a clear message.
4. Push the branch to your fork.
5. Open a pull request describing the change and how it was verified.

## Acknowledgements

This project is based on [RuoYi-Vue-Plus](https://gitee.com/JavaLionLi/RuoYi-Vue-Plus) and the broader [RuoYi](https://gitee.com/y_project/RuoYi-Vue) ecosystem.

## License

This repository does not currently include a standalone license file. Review the upstream project's licensing terms and add an appropriate `LICENSE` file before redistributing or using this project commercially.
