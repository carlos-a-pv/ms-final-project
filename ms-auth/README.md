🔐 Role-Based JWT Authentication & Authorization

This project implements secure authentication and authorization using JSON Web Tokens (JWT) with role-based access control (RBAC). The system ensures that users can only access resources permitted to their assigned roles.

🚀 Features

User Authentication using JWT

Secure login mechanism that generates a JWT upon successful authentication.

Stateless authentication without server-side session storage.

Role-Based Access Control (RBAC)

Users are assigned roles such as ADMIN, CUSTOMER, etc.

API endpoints are protected based on role permissions.

Secure Token Handling

JWT tokens contain encoded user identity and roles.

Tokens are validated on every request using middleware/filter.

Protected APIs

Endpoints are accessible only when a valid token is provided.

Unauthorized access is prevented with proper HTTP responses.

Password Encryption

User passwords are stored securely using hashing (e.g., BCrypt).

🔑 Authentication Flow

User sends login request with credentials.

Server validates credentials.

If valid, a JWT token is generated and returned.

Client includes the token in the Authorization header:

Authorization: Bearer <JWT_TOKEN>

Server verifies the token for each request.

Access is granted or denied based on the user's role.

🛡 Example Role Permissions
Role	Access
ADMIN	Full access to all endpoints
USER	Access to limited endpoints
📌 Benefits

Stateless and scalable authentication

Fine-grained access control

Secure API communication

Reduced server overhead (no sessions)

📌 Future Improvements

Refresh token implementation
