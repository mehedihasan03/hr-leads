# HR Application 🧑‍💼

A Spring Boot–based Human Resource (HR) management system.  
It provides employee management features such as **XML import**, **search**, and **REST APIs** to interact with employee data.

---

## 🚀 Features
- Import employees from XML
- CRUD operations on employees
- Search employees by first/last name
- Pagination & sorting support
- Oracle Database integration
- REST API endpoints for easy integration

---

## 🛠️ Tech Stack
- **Java 17+**
- **Spring Boot 3 / Spring Data JPA**
- **Hibernate**
- **Oracle Database**
- **Lombok**
- **ModelMapper**
- **Maven**
- **Swagger 3**

---

## 📂 Project Structure
```
src
 └── main
     ├── java/com/hr/hrapplication
     │   ├── config
     │   │   ├── ApiExceptionHandler.java
     │   │   ├── ModelMapperConfig.java
     │   │   └── OpenApiConfig.java
     │   ├── controller
     │   │   ├── EmployeeController.java
     │   │   └── XmlImportController.java
     │   ├── dao
     │   │   ├── EmployeeEntity.java
     │   │   ├── EmployeeRepository.java
     │   │   └── XmlImportResultDto.java
     │   ├── domain
     │   │   ├── Employee.java
     │   │   ├── EmployeesXml.java
     │   │   └── EmployeeXml.java
     │   ├── exception
     │   │   └── NotFoundException.java
     │   ├── service
     │   │   ├── EmployeeService.java
     │   │   └── XmlImportService.java
     │   └── HrApplication.java
     └── resources
         ├── static/
         ├── templates/
         ├── xsd/
         │   └── employees.xsd
         ├── application.properties
         └── data.xml
 └── test/...
```

# 🧩 Endpoints (Representative)

Exact paths come from EmployeeController & XmlImportController. If you keep the conventional naming, they will look like:

Employee :
```

GET /api/employees — list employees (optional pagination/filter)

GET /api/employees/{id} — get by id

POST /api/employees — create
Body: application/json → Employee

PUT /api/employees/{id} — update

DELETE /api/employees/{id} — delete
```

XML Import :
```

POST /api/import/xml — upload & import employees XML
```

Returns XmlImportResultDto with counts (created/updated/failed) and error details
