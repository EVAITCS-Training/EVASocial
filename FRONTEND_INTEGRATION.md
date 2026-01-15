# Eva Social - Frontend Backend Integration

This project connects the Eva Social React frontend with the Spring Boot backend.

## Setup Instructions

### Prerequisites
1. **Backend (Spring Boot)**:
   - Java 17 or later
   - MySQL database running on localhost:3306
   - Database name: `eva-social`
   - User: `matt` with password: `Gudmord92!`

2. **Frontend (React)**:
   - Node.js 16 or later
   - npm or pnpm package manager

### Running the Application

#### 1. Start the Backend
```bash
cd eva-social
./gradlew bootRun
```
The backend will start on `http://localhost:8080`

#### 2. Start the Frontend
```bash
cd eve-social-fe
npm install  # or pnpm install
npm run dev  # or pnpm dev
```
The frontend will start on `http://localhost:5173`

### API Endpoints

The frontend connects to these backend endpoints:

#### Authentication
- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/login` - User login

#### Posts
- `GET /api/v1/posts` - Get public posts (paginated)
- `GET /api/v1/posts/feed` - Get authenticated user feed
- `GET /api/v1/posts/{id}` - Get specific post
- `POST /api/v1/posts` - Create new post (requires auth)
- `PUT /api/v1/posts/{id}` - Update post (requires auth)
- `DELETE /api/v1/posts/{id}` - Delete post (requires auth)
- `POST /api/v1/posts/{id}/like` - Like post (requires auth)
- `DELETE /api/v1/posts/{id}/like` - Unlike post (requires auth)
- `POST /api/v1/posts/{id}/dislike` - Dislike post (requires auth)
- `DELETE /api/v1/posts/{id}/dislike` - Remove dislike (requires auth)
- `GET /api/v1/posts/search?q={query}` - Search posts
- `GET /api/v1/posts/hashtag/{tag}` - Get posts by hashtag
- `GET /api/v1/posts/trending-hashtags` - Get trending hashtags

### Features Implemented

#### Frontend Features
- ✅ User authentication (login/register)
- ✅ JWT token management
- ✅ Posts feed (public and authenticated user feed)
- ✅ Create new posts with character limit
- ✅ Like/unlike posts
- ✅ Dislike/undislike posts  
- ✅ Search posts
- ✅ Hashtag filtering
- ✅ Trending hashtags sidebar
- ✅ Responsive design with Bootstrap
- ✅ Error handling and loading states
- ✅ Form validation with Formik and Yup

#### Backend Features (Already Implemented)
- ✅ Spring Security with JWT authentication
- ✅ CORS configuration for frontend
- ✅ RESTful API endpoints
- ✅ JPA entities and repositories
- ✅ Validation and error handling
- ✅ MySQL database integration

### Default Admin User
The backend creates a default admin user on startup:
- Email: `admin@horrorcore.com`
- Password: `Gudmord92!`

### Testing
1. Open `http://localhost:5173` in your browser
2. Click "Login" to access the login form
3. Register a new user or use the admin credentials
4. Create posts, like/dislike content, search, and explore hashtags
5. Test both authenticated and public views

### Technical Stack
- **Frontend**: React 19, TypeScript, React Query, React Router, Formik, Yup, Bootstrap 5
- **Backend**: Spring Boot, Spring Security, Spring Data JPA, JWT, MySQL
- **Authentication**: JWT tokens with localStorage persistence

### CORS Configuration
The backend is configured to accept requests from `http://localhost:5173` (the Vite development server default port).