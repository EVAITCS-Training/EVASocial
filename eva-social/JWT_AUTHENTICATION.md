# JWT Authentication Setup Guide

This guide explains how to use the JWT-based authentication system in EVASocial.

## Environment Setup

### Required Environment Variable

Before running the application, set the JWT secret key:

```bash
export JWT_SECRET=YOUR_BASE64_ENCODED_SECRET_HERE
```

To generate a secure JWT secret:

```bash
# Using Python
python3 -c "import secrets; import base64; key = secrets.token_bytes(32); print(base64.b64encode(key).decode())"

# Using OpenSSL
openssl rand -base64 32
```

The secret must be at least 256 bits (32 bytes) when Base64 decoded.

## API Endpoints

### Register a New User

**Endpoint:** `POST /api/v1/auth/register`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "MySecurePass123!"
}
```

**Password Requirements:**
- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one digit
- At least one special character (@$!%*?&)

**Response:** `201 Created` (no body)

**Example:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"john.doe@example.com","password":"SecurePass123!"}'
```

### Login

**Endpoint:** `POST /api/v1/auth/login`

**Request Body:**
```json
{
  "username": "user@example.com",
  "password": "MySecurePass123!"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john.doe@example.com","password":"SecurePass123!"}'
```

### Access Protected Endpoints

All endpoints under `/api/v1/**` (except `/api/v1/auth/**`) require authentication.

**Example:**
```bash
# Get posts (protected endpoint)
curl http://localhost:8080/api/v1/posts \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## Token Information

- **Expiration:** 1 hour from issuance
- **Storage:** Store the token securely on the client side (e.g., in memory or secure storage)
- **Format:** Bearer token in Authorization header

## Security Features

- **Stateless Authentication:** No server-side session storage
- **BCrypt Password Hashing:** Secure password storage
- **Input Validation:** Email format and password strength validation
- **CORS Configuration:** Allows requests from localhost:5173 and localhost:3000
- **Token Validation:** Automatic validation on each request to protected endpoints

## Public Endpoints

The following endpoints are accessible without authentication:

- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/login` - User login
- `GET /actuator/**` - Application actuator endpoints
- `GET /` - Home page
- `GET /static/**` - Static resources
- `GET /posts` - View posts page
- `GET /home` - Home page

## Error Handling

### Registration Errors

- **Email already exists:** `400 Bad Request` with error message
- **Invalid email format:** `400 Bad Request` with validation error
- **Weak password:** `400 Bad Request` with validation error

### Login Errors

- **Invalid credentials:** `401 Unauthorized`
- **Missing fields:** `400 Bad Request`

### Protected Endpoint Errors

- **Missing token:** `401 Unauthorized`
- **Invalid token:** `401 Unauthorized`
- **Expired token:** `401 Unauthorized`

## Testing

Run tests with:

```bash
./gradlew test
```

All tests use testcontainers to spin up a MySQL database automatically.

## Development Setup

1. Ensure MySQL is running (or use testcontainers for testing)
2. Set the JWT_SECRET environment variable
3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## Production Deployment

1. Generate a strong, unique JWT secret
2. Store the JWT secret in your secrets management system
3. Configure the JWT_SECRET environment variable in your deployment
4. Update CORS origins in `SecurityConfig.java` to match your production domains
5. Consider using HTTPS in production for secure token transmission
