# Project Context — edtech-erp

## 1. Project Overview
- **EdTech ERP**: Nền tảng quản trị trung tâm đào tạo (tin học, ngoại ngữ, kỹ năng mềm) dạng SaaS
- Mục tiêu: số hóa & tự động hóa toàn bộ quy trình vận hành — quản lý học viên, khóa học, điểm số, tài chính
- Kiến trúc Microservices gồm 5 services: API Gateway, Enrollment Service, Academic Service, Assessment Service, Finance Service
- Xác thực tập trung qua Keycloak (SSO, RBAC, Client Credentials Grant)
- 4 nhóm người dùng: Admin, Admission (Tuyển sinh), Teacher (Giáo viên), Student (Học viên)
- Roadmap: MVP (4 Sprints) → Tích hợp thanh toán/thông báo → Multi-tenancy & Cloud deployment
- Ưu tiên: code sạch, dễ mở rộng

## 2. Project Structure
- Multi-module Gradle project (database-per-service)
- Package gốc: `com.example.edtecherp`
- Cấu trúc:
  ```
  edtech-erp/
  ├── common/                              → Shared library (no Spring Boot)
  │   └── src/main/java/.../common/
  │       └── entity/
  │           └── BaseEntity.java
  │
  ├── api-gateway/                         → Port 9000, Spring Cloud Gateway (WebFlux)
  │   └── src/main/java/.../gateway/
  │       ├── ApiGatewayApplication.java
  │       └── config/
  │
  ├── enrollment-service/                  → Port 8081, enrollment_db
  │   └── src/main/java/.../enrollment/
  │       ├── EnrollmentServiceApplication.java
  │       ├── entity/
  │       │   ├── Lead.java
  │       │   └── Student.java
  │       ├── enums/
  │       │   ├── LeadStatus.java
  │       │   ├── LeadSource.java
  │       │   └── Gender.java
  │       ├── repository/
  │       │   ├── LeadRepository.java
  │       │   └── StudentRepository.java
  │       ├── service/
  │       │   ├── LeadService.java
  │       │   └── StudentService.java
  │       ├── service/impl/
  │       │   ├── LeadServiceImpl.java
  │       │   └── StudentServiceImpl.java
  │       ├── controller/
  │       │   ├── LeadController.java
  │       │   └── StudentController.java
  │       ├── dto/
  │       │   ├── request/
  │       │   └── response/
  │       ├── config/
  │       │   └── SecurityConfig.java
  │       └── exception/
  │           └── GlobalExceptionHandler.java
  │
  ├── academic-service/                    → Port 8082, academic_db
  │   └── src/main/java/.../academic/
  │       ├── AcademicServiceApplication.java
  │       ├── entity/
  │       │   ├── Teacher.java
  │       │   ├── Course.java
  │       │   ├── Clazz.java               ← tên tránh java.lang.Class
  │       │   └── ClassEnrollment.java
  │       ├── enums/
  │       │   ├── CourseStatus.java
  │       │   ├── ClassStatus.java
  │       │   └── EnrollmentStatus.java
  │       ├── repository/
  │       ├── service/ + service/impl/
  │       ├── controller/
  │       ├── dto/
  │       └── config/
  │
  ├── assessment-service/                  → Port 8083, assessment_db
  │   └── src/main/java/.../assessment/
  │       ├── AssessmentServiceApplication.java
  │       ├── entity/
  │       │   ├── Assessment.java
  │       │   └── Grade.java
  │       ├── enums/
  │       │   └── AssessmentType.java
  │       ├── repository/
  │       ├── service/ + service/impl/
  │       ├── controller/
  │       ├── dto/
  │       └── config/
  │
  ├── finance-service/                     → Port 8084, finance_db
  │   └── src/main/java/.../finance/
  │       ├── FinanceServiceApplication.java
  │       ├── entity/
  │       │   ├── Invoice.java
  │       │   └── Payment.java
  │       ├── enums/
  │       │   ├── InvoiceStatus.java
  │       │   ├── PaymentMethod.java
  │       │   └── PaymentStatus.java
  │       ├── repository/
  │       ├── service/ + service/impl/
  │       ├── controller/
  │       ├── dto/
  │       └── config/
  │
  ├── docker/
  │   └── init-databases.sh                → Tạo 4 databases on startup
  ├── documents/                           → Tài liệu dự án
  ├── build.gradle                         → Root: subprojects + service config
  ├── settings.gradle                      → include 6 modules
  ├── compose.yaml                         → PostgreSQL 16 + Keycloak 26.2
  ├── .env                                 → DB + Keycloak credentials
  ├── .env.example                         → Template .env (committed)
  └── .antigravity/
      └── rules.md
  ```

## 3. Coding Conventions & Standards
- **Stack**: Spring Boot 4.1.0, Java 21, Lombok, Spring Data JPA, Spring Security OAuth2 Resource Server, OpenFeign, Springdoc OpenAPI
- **Naming**:
  - camelCase cho field/method
  - PascalCase cho class
  - UPPER_SNAKE_CASE cho constants và enum values
- **Database**: PostgreSQL, table names số nhiều (students, courses, classes...)
- **Lombok**: Dùng `@Builder`, `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Getter`, `@Setter`
- **Injection**: Constructor injection qua `@RequiredArgsConstructor`, **không dùng** `@Autowired` field injection
- **BaseEntity**: Tất cả entities nên extend BaseEntity (id, createdAt, updatedAt)
- **Validation**: Dùng `@Valid`, `@NotNull`, `@NotBlank`... từ `spring-boot-starter-validation`
- **API Documentation**: Swagger/OpenAPI qua `springdoc-openapi-starter-webmvc-ui`
- **Environment**: Biến môi trường quản lý qua `spring-dotenv` (file `.env`)

## 4. Architecture Patterns
- **Layered Architecture bắt buộc**: Repository → Service (interface) → ServiceImpl → Controller
- **DTO Pattern**: Không trả Entity trực tiếp ra Controller — luôn map qua DTO (Request/Response riêng biệt)
- **Security**: Keycloak làm IAM trung tâm, Spring Security OAuth2 Resource Server validate JWT
- **Service-to-Service**: Dùng OpenFeign cho giao tiếp giữa các Microservices, Client Credentials Grant qua Keycloak
- **Event-driven** (giai đoạn sau): RabbitMQ/Kafka cho luồng bất đồng bộ (VD: Academic → Finance sinh hóa đơn)
- **Exception Handling**: GlobalExceptionHandler với `@RestControllerAdvice`, custom exceptions, response format thống nhất
- **Pagination**: Dùng `Pageable` của Spring Data, response wrap trong Page object

## 5. Response Style
- Trả về code Java hoàn chỉnh, copy-paste được ngay
- Không thêm dependency ngoài `build.gradle` hiện tại trừ khi được yêu cầu trực tiếp
- Không tự ý sửa file config (`application.yml`, `.env`...) trừ khi được yêu cầu
- Review code: dùng format có cấu trúc (bullet points, bảng), không viết đoạn văn dài
- Comment bằng tiếng Việt hoặc tiếng Anh tùy context, ưu tiên tiếng Anh cho code comment
- Giải thích lý do khi có nhiều cách tiếp cận, đề xuất phương án tốt nhất

## 6. Workflows & Modes
- **Task phức tạp** (feature mới, refactor nhiều file, tích hợp Keycloak/OpenFeign): đưa Implementation Plan trước, chờ xác nhận mới code
- **Task đơn giản** (fix 1 bug, sửa 1 method, thêm field): code ngay không cần plan
- **Trước khi sửa file**: đọc qua file hiện tại để hiểu context, không suy đoán nội dung
- **Sau khi code xong**: nhắc chạy `./gradlew build` hoặc `./gradlew test` để verify
- **Khi tạo entity mới**: tạo đồng bộ cả Entity → Repository → Service → ServiceImpl → Controller → DTOs
- **Khi tích hợp Keycloak**: luôn kiểm tra SecurityConfig, đảm bảo endpoint mới được phân quyền đúng Role

## 7. Module-specific Rules

### Enrollment Service
- Quản lý Leads (học viên tiềm năng) và luồng chuyển đổi thành Học viên chính thức
- Lead status: `NEW`, `CONTACTED`, `QUALIFIED`, `CONVERTED`, `LOST`
- Khi convert Lead → Student: đồng bộ tạo user trên Keycloak với role STUDENT
- Tích hợp Keycloak Admin API để tạo tài khoản tự động

### Academic Service
- Quản lý Khóa học (Course), Lớp học (Class/Section), Thời khóa biểu (Schedule)
- Phân công Giáo viên vào lớp
- API xem danh sách lớp đang dạy theo role Teacher
- Gán học viên vào lớp học

### Assessment Service
- Quản lý điểm số, đánh giá quá trình học tập
- Fine-grained Authorization: chỉ giáo viên phụ trách lớp mới được nhập điểm
- Database có thể dùng MongoDB hoặc PostgreSQL

### Finance Service
- Quản lý học phí, sinh hóa đơn tự động
- Nhận event từ Academic Service (khi gán học viên vào lớp) để tạo invoice
- Đối soát trạng thái thanh toán
- Tích hợp thanh toán online (VNPay/MoMo) ở giai đoạn 2

### API Gateway
- Chốt chặn bảo mật, định tuyến request đến từng Microservice
- Validate JWT Token từ Keycloak
- Dùng Spring Cloud Gateway

### Session Management
Cuối mỗi session, tự động tạo summary với format:
- Đang làm gì?
- Đã xong gì?
- Decision đã chốt
- Task tiếp theo
