import type { 
  PostResponse, 
  FeedPostResponse, 
  HashtagResponse, 
  CreatePostRequest, 
  UpdatePostRequest,
  AuthRequest,
  AuthResponse,
  PostNewUser,
  UserProfile
} from '../types/api';

const API_BASE_URL = 'http://localhost:8080';

class ApiService {
  private getAuthHeaders(): HeadersInit {
    const token = localStorage.getItem('authToken');
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
    };
    
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    
    return headers;
  }

  private async handleResponse<T>(response: Response): Promise<T> {
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`HTTP ${response.status}: ${errorText}`);
    }

    // Handle empty responses
    if (response.status === 204 || response.status === 201) {
      return {} as T;
    }

    return response.json();
  }

  // Authentication endpoints
  async register(userData: PostNewUser): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api/v1/auth/register`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(userData),
    });

    await this.handleResponse<void>(response);
  }

  async login(credentials: AuthRequest): Promise<AuthResponse> {
    const response = await fetch(`${API_BASE_URL}/api/v1/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credentials),
    });

    const authResponse = await this.handleResponse<AuthResponse>(response);
    
    // Store the token in localStorage
    if (authResponse.token) {
      localStorage.setItem('authToken', authResponse.token);
    }
    
    return authResponse;
  }

  logout(): void {
    localStorage.removeItem('authToken');
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('authToken');
  }

  // Post endpoints
  async getPublicPosts(page: number = 0, size: number = 20): Promise<PostResponse[]> {
    const response = await fetch(
      `${API_BASE_URL}/api/v1/posts?page=${page}&size=${size}`,
      {
        headers: this.getAuthHeaders(),
      }
    );

    return this.handleResponse<PostResponse[]>(response);
  }

  async getFeed(page: number = 0, size: number = 20): Promise<FeedPostResponse[]> {
    const response = await fetch(
      `${API_BASE_URL}/api/v1/posts/feed?page=${page}&size=${size}`,
      {
        headers: this.getAuthHeaders(),
      }
    );

    return this.handleResponse<FeedPostResponse[]>(response);
  }

  async getPost(id: number): Promise<PostResponse> {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts/${id}`, {
      headers: this.getAuthHeaders(),
    });

    return this.handleResponse<PostResponse>(response);
  }

  async createPost(postData: CreatePostRequest): Promise<PostResponse> {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(postData),
    });

    return this.handleResponse<PostResponse>(response);
  }

  async updatePost(id: number, postData: UpdatePostRequest): Promise<PostResponse> {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts/${id}`, {
      method: 'PUT',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(postData),
    });

    return this.handleResponse<PostResponse>(response);
  }

  async deletePost(id: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts/${id}`, {
      method: 'DELETE',
      headers: this.getAuthHeaders(),
    });

    await this.handleResponse<void>(response);
  }

  async likePost(id: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts/${id}/like`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
    });

    await this.handleResponse<void>(response);
  }

  async unlikePost(id: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts/${id}/like`, {
      method: 'DELETE',
      headers: this.getAuthHeaders(),
    });

    await this.handleResponse<void>(response);
  }

  async dislikePost(id: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts/${id}/dislike`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
    });

    await this.handleResponse<void>(response);
  }

  async undislikePost(id: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts/${id}/dislike`, {
      method: 'DELETE',
      headers: this.getAuthHeaders(),
    });

    await this.handleResponse<void>(response);
  }

  async searchPosts(query: string): Promise<PostResponse[]> {
    const response = await fetch(
      `${API_BASE_URL}/api/v1/posts/search?q=${encodeURIComponent(query)}`,
      {
        headers: this.getAuthHeaders(),
      }
    );

    return this.handleResponse<PostResponse[]>(response);
  }

  async getPostsByHashtag(tag: string): Promise<PostResponse[]> {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts/hashtag/${tag}`, {
      headers: this.getAuthHeaders(),
    });

    return this.handleResponse<PostResponse[]>(response);
  }

  async getTrendingHashtags(): Promise<HashtagResponse[]> {
    const response = await fetch(`${API_BASE_URL}/api/v1/posts/trending-hashtags`, {
      headers: this.getAuthHeaders(),
    });

    return this.handleResponse<HashtagResponse[]>(response);
  }
}

export const apiService = new ApiService();