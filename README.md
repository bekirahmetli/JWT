# JWT Authentication REST API

A secure Spring Boot REST API implementing JWT (JSON Web Token) authentication with refresh token mechanism. The project demonstrates layered architecture, Spring Security integration, BCrypt password encoding, and protected endpoints for employee management.

## Project Overview

This API provides secure authentication and authorization using JWT tokens:

- **User Registration**: Create new user accounts with encrypted passwords
- **User Authentication**: Login with username/password to receive JWT access token and refresh token
- **Token Refresh**: Obtain new access tokens using refresh tokens without re-authentication
- **Protected Resources**: Access employee data with valid JWT tokens
- **API Documentation**: Swagger/OpenAPI integration for interactive API testing

## Architecture

Layered architecture following best practices:

```
entities/     - JPA entities (User, Employee, Department, RefreshToken)
dao/          - Spring Data JPA repositories
business/     - Service interfaces and implementations
api/          - REST controllers
dto/          - Data Transfer Objects for request/response
config/       - Spring configuration (Security, App, Swagger)
jwt/          - JWT service, filter, and authentication components
```

### Key Components

- **JwtService**: Token generation, validation, and parsing
- **JwtAuthenticationFilter**: Custom filter for JWT validation on each request
- **AuthManager**: Authentication and registration business logic
- **RefreshTokenManager**: Refresh token generation and validation
- **SecurityConfig**: Spring Security configuration with JWT filter chain
- **AppConfig**: UserDetailsService, AuthenticationProvider, PasswordEncoder beans

## Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database access
- **PostgreSQL** - Relational database
- **JWT** - Token generation and validation
- **BCrypt** - Password hashing
- **Lombok** - Boilerplate code reduction
- **Swagger/OpenAPI** - API documentation
- **Bean Validation** - Request validation

## Setup and Run

### Prerequisites

- JDK 21
- PostgreSQL
- Maven (or use Maven Wrapper)

### Database Configuration

Configure PostgreSQL connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jwt
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=create
spring.jpa.hibernate.show-sql=true
```

### Build and Run

**Using Maven Wrapper:**

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

**Using Maven:**

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Configuration

### JWT Settings

- **Access Token Expiration**: 2 hours
- **Refresh Token Expiration**: 4 hours
- **Secret Key**: Base64 encoded (configured in `JwtService.SECRET_KEY`)
- **Algorithm**: HS256

### Security Configuration

- **Public Endpoints**: `/register`, `/authenticate`, `/refreshToken`, Swagger UI paths
- **Protected Endpoints**: All other endpoints require valid JWT token
- **Session Management**: Stateless (JWT-based)
- **CSRF**: Disabled (REST API)

## Entities and Relationships

### User
- Fields: `id`, `userName`, `password`
- Implements: `UserDetails` (Spring Security)
- Relationships: OneToMany with RefreshToken

### Employee
- Fields: `id`, `firstName`, `lastName`
- Relationships: ManyToOne with Department

### Department
- Fields: `id`, `name`, `location`
- Relationships: OneToMany with Employee

### RefreshToken
- Fields: `id`, `refreshToken` (UUID), `expireDate`
- Relationships: ManyToOne with User

## DTOs and Validation

### Request DTOs

**AuthRequest**:
- `username` (required, @NotEmpty)
- `password` (required, @NotEmpty)

**RefreshTokenRequest**:
- `refreshToken` (required)

### Response DTOs

**AuthResponse**:
- `accessToken` (JWT token)
- `refreshToken` (UUID string)

**DtoUser**:
- `id`, `userName` (password excluded)

**DtoEmployee**:
- `id`, `firstName`, `lastName`, `department` (DtoDepartment)

**DtoDepartment**:
- `id`, `name`, `location`

## Business Rules

### Authentication

1. **Registration**: 
   - Password is encrypted using BCrypt before saving
   - Username must be unique

2. **Login**:
   - Username and password validated against database
   - On success: Access token (2h) + Refresh token (4h) generated
   - Refresh token saved to database

3. **Token Refresh**:
   - Refresh token validated (exists and not expired)
   - New access token generated
   - New refresh token generated and saved (old one replaced)

### Security

- All passwords hashed with BCrypt
- JWT tokens signed with HS256 algorithm
- Token expiration enforced
- Stateless session management

## Error Handling

- **401 Unauthorized**: Invalid or missing JWT token
- **403 Forbidden**: Valid token but insufficient permissions
- **400 Bad Request**: Validation errors
- **500 Internal Server Error**: Server-side errors

## API Endpoints

### Authentication Endpoints

#### Register User
```
POST /register
Content-Type: application/json

Request Body:
{
  "username": "john_doe",
  "password": "securePassword123"
}

Response: 200 OK
{
  "id": 1,
  "userName": "john_doe"
}
```

#### Authenticate (Login)
```
POST /authenticate
Content-Type: application/json

Request Body:
{
  "username": "john_doe",
  "password": "securePassword123"
}

Response: 200 OK
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

#### Refresh Token
```
POST /refreshToken
Content-Type: application/json

Request Body:
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}

Response: 200 OK
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "new-refresh-token-uuid"
}
```

### Protected Endpoints

#### Get Employee by ID
```
GET /employees/{id}
Authorization: Bearer {accessToken}

Response: 200 OK
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "department": {
    "id": 1,
    "name": "Engineering",
    "location": "Istanbul"
  }
}
```

## Using the API

### Step 1: Register a User

```bash
curl -X POST http://localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### Step 2: Authenticate and Get Tokens

```bash
curl -X POST http://localhost:8080/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

Save the `accessToken` and `refreshToken` from the response.

### Step 3: Access Protected Endpoints

```bash
curl -X GET http://localhost:8080/employees/1 \
  -H "Authorization: Bearer {accessToken}"
```

### Step 4: Refresh Access Token

When access token expires, use refresh token:

```bash
curl -X POST http://localhost:8080/refreshToken \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "{refreshToken}"
  }'
```

## Swagger Documentation

Once the application is running, access Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

Or:

```
http://localhost:8080/swagger-ui/index.html
```

API documentation is available at:

```
http://localhost:8080/v3/api-docs
```

**Note**: Swagger UI allows you to test endpoints directly. Click "Authorize" button and enter your JWT token in the format: `Bearer {your-token}`

## Security Features

1. **Password Encryption**: BCrypt hashing
2. **JWT Tokens**: Stateless authentication
3. **Token Expiration**: Access tokens expire after 2 hours
4. **Refresh Tokens**: Long-lived tokens (4 hours) for seamless re-authentication
5. **Filter Chain**: Custom JWT filter validates tokens on every request
6. **UserDetails Integration**: Spring Security UserDetails implementation

## Project Structure

```
src/main/java/com/example/
├── api/
│   ├── impl/
│   │   ├── RestAuthControllerImpl.java
│   │   └── RestEmployeeControllerImpl.java
│   ├── IRestAuthController.java
│   └── IRestEmployeeController.java
├── business/
│   ├── abstracts/
│   │   ├── IAuthService.java
│   │   ├── IEmployeeService.java
│   │   └── IRefreshTokenService.java
│   └── concretes/
│       ├── AuthManager.java
│       ├── EmployeeManager.java
│       └── RefreshTokenManager.java
├── config/
│   ├── AppConfig.java
│   ├── SecurityConfig.java
│   └── SwaggerConfig.java
├── dao/
│   ├── EmployeeRepo.java
│   ├── RefreshTokenRepo.java
│   └── UserRepo.java
├── dto/
│   ├── DtoDepartment.java
│   ├── DtoEmployee.java
│   └── DtoUser.java
├── entities/
│   ├── Department.java
│   ├── Employee.java
│   ├── RefreshToken.java
│   └── User.java
├── jwt/
│   ├── AuthEntryPoint.java
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtService.java
│   └── RefreshTokenRequest.java
└── JwtApplication.java
```

## Important Notes

1. **Secret Key**: In production, move `SECRET_KEY` from code to environment variables or secure configuration
2. **Token Storage**: Refresh tokens are stored in database; consider cleanup strategy for expired tokens
3. **UserDetails Methods**: User entity implements all required UserDetails methods (`isEnabled`, `isAccountNonExpired`, etc.)
4. **Repository Method Naming**: `findByUserName` matches entity field name (`userName`), not `findByUsername`

## Troubleshooting

### 403 Forbidden
- Ensure JWT token is included in `Authorization` header: `Bearer {token}`
- Check token expiration
- Verify token format (should start with `Bearer `)

### 401 Unauthorized
- Invalid username/password
- Token expired (use refresh token endpoint)
- Missing or malformed token

### Database Connection Issues
- Verify PostgreSQL is running
- Check database credentials in `application.properties`
- Ensure database `jwt` exists

---

# JWT Kimlik Doğrulama REST API

JWT (JSON Web Token) kimlik doğrulama ve refresh token mekanizması içeren güvenli bir Spring Boot REST API. Proje, katmanlı mimari, Spring Security entegrasyonu, BCrypt şifre şifreleme ve çalışan yönetimi için korumalı endpoint'leri gösterir.

## Proje Özeti

Bu API, JWT token'ları kullanarak güvenli kimlik doğrulama ve yetkilendirme sağlar:

- **Kullanıcı Kaydı**: Şifrelenmiş şifrelerle yeni kullanıcı hesapları oluşturma
- **Kullanıcı Kimlik Doğrulama**: Kullanıcı adı/şifre ile giriş yaparak JWT access token ve refresh token alma
- **Token Yenileme**: Yeniden kimlik doğrulama yapmadan refresh token kullanarak yeni access token alma
- **Korumalı Kaynaklar**: Geçerli JWT token'ları ile çalışan verilerine erişim
- **API Dokümantasyonu**: İnteraktif API testi için Swagger/OpenAPI entegrasyonu

## Mimari

En iyi uygulamaları takip eden katmanlı mimari:

```
entities/     - JPA varlıkları (User, Employee, Department, RefreshToken)
dao/          - Spring Data JPA repository'leri
business/     - Servis arayüzleri ve implementasyonları
api/          - REST controller'lar
dto/          - İstek/yanıt için Veri Transfer Nesneleri
config/       - Spring yapılandırması (Security, App, Swagger)
jwt/          - JWT servisi, filtre ve kimlik doğrulama bileşenleri
```

### Önemli Bileşenler

- **JwtService**: Token üretimi, doğrulama ve ayrıştırma
- **JwtAuthenticationFilter**: Her istekte JWT doğrulaması için özel filtre
- **AuthManager**: Kimlik doğrulama ve kayıt iş mantığı
- **RefreshTokenManager**: Refresh token üretimi ve doğrulama
- **SecurityConfig**: JWT filtre zinciri ile Spring Security yapılandırması
- **AppConfig**: UserDetailsService, AuthenticationProvider, PasswordEncoder bean'leri

## Teknoloji Yığını

- **Java 21**
- **Spring Boot**
- **Spring Security** - Kimlik doğrulama ve yetkilendirme
- **Spring Data JPA** - Veritabanı erişimi
- **PostgreSQL** - İlişkisel veritabanı
- **JWT** - Token üretimi ve doğrulama
- **BCrypt** - Şifre hash'leme
- **Lombok** - Tekrarlayan kod azaltma
- **Swagger/OpenAPI** - API dokümantasyonu
- **Bean Validation** - İstek doğrulama

## Kurulum ve Çalıştırma

### Gereksinimler

- JDK 21
- PostgreSQL
- Maven (veya Maven Wrapper kullanın)

### Veritabanı Yapılandırması

PostgreSQL bağlantısını `src/main/resources/application.properties` dosyasında yapılandırın:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jwt
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=create
spring.jpa.hibernate.show-sql=true
```

### Derleme ve Çalıştırma

**Maven Wrapper Kullanarak:**

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

**Maven Kullanarak:**

```bash
mvn clean install
mvn spring-boot:run
```

Uygulama `http://localhost:8080` adresinde başlayacaktır.

## Yapılandırma

### JWT Ayarları

- **Access Token Süresi**: 2 saat
- **Refresh Token Süresi**: 4 saat
- **Gizli Anahtar**: Base64 kodlanmış (`JwtService.SECRET_KEY` içinde yapılandırılmış)
- **Algoritma**: HS256

### Güvenlik Yapılandırması

- **Herkese Açık Endpoint'ler**: `/register`, `/authenticate`, `/refreshToken`, Swagger UI yolları
- **Korumalı Endpoint'ler**: Diğer tüm endpoint'ler geçerli JWT token gerektirir
- **Oturum Yönetimi**: Durumsuz (JWT tabanlı)
- **CSRF**: Devre dışı (REST API)

## Varlıklar ve İlişkiler

### User
- Alanlar: `id`, `userName`, `password`
- Uygular: `UserDetails` (Spring Security)
- İlişkiler: RefreshToken ile OneToMany

### Employee
- Alanlar: `id`, `firstName`, `lastName`
- İlişkiler: Department ile ManyToOne

### Department
- Alanlar: `id`, `name`, `location`
- İlişkiler: Employee ile OneToMany

### RefreshToken
- Alanlar: `id`, `refreshToken` (UUID), `expireDate`
- İlişkiler: User ile ManyToOne

## DTO'lar ve Doğrulama

### İstek DTO'ları

**AuthRequest**:
- `username` (zorunlu, @NotEmpty)
- `password` (zorunlu, @NotEmpty)

**RefreshTokenRequest**:
- `refreshToken` (zorunlu)

### Yanıt DTO'ları

**AuthResponse**:
- `accessToken` (JWT token)
- `refreshToken` (UUID string)

**DtoUser**:
- `id`, `userName` (şifre hariç)

**DtoEmployee**:
- `id`, `firstName`, `lastName`, `department` (DtoDepartment)

**DtoDepartment**:
- `id`, `name`, `location`

## İş Kuralları

### Kimlik Doğrulama

1. **Kayıt**: 
   - Şifre kaydedilmeden önce BCrypt ile şifrelenir
   - Kullanıcı adı benzersiz olmalıdır

2. **Giriş**:
   - Kullanıcı adı ve şifre veritabanına karşı doğrulanır
   - Başarılı olursa: Access token (2s) + Refresh token (4s) üretilir
   - Refresh token veritabanına kaydedilir

3. **Token Yenileme**:
   - Refresh token doğrulanır (mevcut ve süresi dolmamış)
   - Yeni access token üretilir
   - Yeni refresh token üretilir ve kaydedilir (eskisi değiştirilir)

### Güvenlik

- Tüm şifreler BCrypt ile hash'lenir
- JWT token'ları HS256 algoritması ile imzalanır
- Token süresi zorunludur
- Durumsuz oturum yönetimi

## Hata Yönetimi

- **401 Unauthorized**: Geçersiz veya eksik JWT token
- **403 Forbidden**: Geçerli token ancak yetersiz yetkiler
- **400 Bad Request**: Doğrulama hataları
- **500 Internal Server Error**: Sunucu tarafı hatalar

## API Endpoint'leri

### Kimlik Doğrulama Endpoint'leri

#### Kullanıcı Kaydı
```
POST /register
Content-Type: application/json

İstek Gövdesi:
{
  "username": "john_doe",
  "password": "securePassword123"
}

Yanıt: 200 OK
{
  "id": 1,
  "userName": "john_doe"
}
```

#### Kimlik Doğrulama (Giriş)
```
POST /authenticate
Content-Type: application/json

İstek Gövdesi:
{
  "username": "john_doe",
  "password": "securePassword123"
}

Yanıt: 200 OK
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

#### Token Yenileme
```
POST /refreshToken
Content-Type: application/json

İstek Gövdesi:
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}

Yanıt: 200 OK
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "new-refresh-token-uuid"
}
```

### Korumalı Endpoint'ler

#### ID'ye Göre Çalışan Getir
```
GET /employees/{id}
Authorization: Bearer {accessToken}

Yanıt: 200 OK
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "department": {
    "id": 1,
    "name": "Engineering",
    "location": "Istanbul"
  }
}
```

## API Kullanımı

### Adım 1: Kullanıcı Kaydet

```bash
curl -X POST http://localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### Adım 2: Kimlik Doğrula ve Token Al

```bash
curl -X POST http://localhost:8080/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

Yanıttan `accessToken` ve `refreshToken` değerlerini kaydedin.

### Adım 3: Korumalı Endpoint'lere Eriş

```bash
curl -X GET http://localhost:8080/employees/1 \
  -H "Authorization: Bearer {accessToken}"
```

### Adım 4: Access Token'ı Yenile

Access token süresi dolduğunda, refresh token kullanın:

```bash
curl -X POST http://localhost:8080/refreshToken \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "{refreshToken}"
  }'
```

## Swagger Dokümantasyonu

Uygulama çalıştıktan sonra, Swagger UI'ya şu adresten erişin:

```
http://localhost:8080/swagger-ui.html
```

Veya:

```
http://localhost:8080/swagger-ui/index.html
```

API dokümantasyonu şu adreste mevcuttur:

```
http://localhost:8080/v3/api-docs
```

**Not**: Swagger UI, endpoint'leri doğrudan test etmenize olanak tanır. "Authorize" düğmesine tıklayın ve JWT token'ınızı şu formatta girin: `Bearer {your-token}`

## Güvenlik Özellikleri

1. **Şifre Şifreleme**: BCrypt hash'leme
2. **JWT Token'ları**: Durumsuz kimlik doğrulama
3. **Token Süresi**: Access token'lar 2 saat sonra sona erer
4. **Refresh Token'lar**: Sorunsuz yeniden kimlik doğrulama için uzun ömürlü token'lar (4 saat)
5. **Filtre Zinciri**: Özel JWT filtresi her istekte token'ları doğrular
6. **UserDetails Entegrasyonu**: Spring Security UserDetails implementasyonu

## Proje Yapısı

```
src/main/java/com/example/
├── api/
│   ├── impl/
│   │   ├── RestAuthControllerImpl.java
│   │   └── RestEmployeeControllerImpl.java
│   ├── IRestAuthController.java
│   └── IRestEmployeeController.java
├── business/
│   ├── abstracts/
│   │   ├── IAuthService.java
│   │   ├── IEmployeeService.java
│   │   └── IRefreshTokenService.java
│   └── concretes/
│       ├── AuthManager.java
│       ├── EmployeeManager.java
│       └── RefreshTokenManager.java
├── config/
│   ├── AppConfig.java
│   ├── SecurityConfig.java
│   └── SwaggerConfig.java
├── dao/
│   ├── EmployeeRepo.java
│   ├── RefreshTokenRepo.java
│   └── UserRepo.java
├── dto/
│   ├── DtoDepartment.java
│   ├── DtoEmployee.java
│   └── DtoUser.java
├── entities/
│   ├── Department.java
│   ├── Employee.java
│   ├── RefreshToken.java
│   └── User.java
├── jwt/
│   ├── AuthEntryPoint.java
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtService.java
│   └── RefreshTokenRequest.java
└── JwtApplication.java
```

## Önemli Notlar

1. **Gizli Anahtar**: Üretimde, `SECRET_KEY`'i kod dışına, ortam değişkenlerine veya güvenli yapılandırmaya taşıyın
2. **Token Depolama**: Refresh token'lar veritabanında saklanır; süresi dolmuş token'lar için temizleme stratejisi düşünün
3. **UserDetails Metodları**: User varlığı tüm gerekli UserDetails metodlarını uygular (`isEnabled`, `isAccountNonExpired`, vb.)
4. **Repository Metod İsimlendirme**: `findByUserName` entity alan adıyla eşleşir (`userName`), `findByUsername` değil

## Sorun Giderme

### 403 Forbidden
- JWT token'ın `Authorization` başlığında olduğundan emin olun: `Bearer {token}`
- Token süresini kontrol edin
- Token formatını doğrulayın (`Bearer ` ile başlamalı)

### 401 Unauthorized
- Geçersiz kullanıcı adı/şifre
- Token süresi dolmuş (refresh token endpoint'ini kullanın)
- Eksik veya hatalı biçimlendirilmiş token

### Veritabanı Bağlantı Sorunları
- PostgreSQL'in çalıştığını doğrulayın
- `application.properties` dosyasındaki veritabanı kimlik bilgilerini kontrol edin
- `jwt` veritabanının mevcut olduğundan emin olun

