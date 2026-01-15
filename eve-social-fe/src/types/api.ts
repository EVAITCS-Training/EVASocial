// API Response Types
export interface UserSummaryResponse {
  id: number;
  displayName: string;
  email: string;
  profilePictureUrl?: string;
}

export interface PostResponse {
  id: number;
  status: string;
  author: UserSummaryResponse;
  createdAt: string;
  updatedAt: string;
  likes: number;
  dislikes: number;
  commentCount: number;
  likedByCurrentUser?: boolean;
  dislikedByCurrentUser?: boolean;
  hashtags: string[];
}

export interface FeedPostResponse extends PostResponse {
  // Feed posts might have additional properties
}

export interface HashtagResponse {
  tag: string;
  postCount: number;
}

export interface CreatePostRequest {
  status: string;
}

export interface UpdatePostRequest {
  status: string;
}

export interface AuthRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
}

export interface PostNewUser {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  bio?: string;
  profilePictureUrl?: string;
}

export interface UserProfile {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  bio?: string;
  profilePictureUrl?: string;
  followersCount: number;
  followingCount: number;
  postsCount: number;
  isFollowedByCurrentUser?: boolean;
}

export interface ApiError {
  timestamp: string;
  status: number;
  uri: string;
  message: string;
}