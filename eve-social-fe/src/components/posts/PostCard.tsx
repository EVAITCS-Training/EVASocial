import React, { useState } from 'react';
import type { PostResponse } from '../../types/api';
import { apiService } from '../../services/api';
import { useAuth } from '../../context/AuthContext';

interface PostCardProps {
  post: PostResponse;
  onUpdate?: (updatedPost: PostResponse) => void;
  onDelete?: (postId: number) => void;
}

const PostCard: React.FC<PostCardProps> = ({ post, onUpdate, onDelete }) => {
  const { isAuthenticated } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleLike = async () => {
    if (!isAuthenticated) return;
    
    setLoading(true);
    setError(null);
    try {
      if (post.likedByCurrentUser) {
        await apiService.unlikePost(post.id);
      } else {
        await apiService.likePost(post.id);
      }
      
      // Refresh the post to get updated counts
      const updatedPost = await apiService.getPost(post.id);
      if (onUpdate) {
        onUpdate(updatedPost);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update like');
    } finally {
      setLoading(false);
    }
  };

  const handleDislike = async () => {
    if (!isAuthenticated) return;
    
    setLoading(true);
    setError(null);
    try {
      if (post.dislikedByCurrentUser) {
        await apiService.undislikePost(post.id);
      } else {
        await apiService.dislikePost(post.id);
      }
      
      // Refresh the post to get updated counts
      const updatedPost = await apiService.getPost(post.id);
      if (onUpdate) {
        onUpdate(updatedPost);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update dislike');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!isAuthenticated) return;
    
    if (window.confirm('Are you sure you want to delete this post?')) {
      setLoading(true);
      try {
        await apiService.deletePost(post.id);
        if (onDelete) {
          onDelete(post.id);
        }
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to delete post');
        setLoading(false);
      }
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const renderHashtags = (hashtags: string[]) => {
    return hashtags.map((tag, index) => (
      <span key={index} className="badge bg-secondary me-1">
        #{tag}
      </span>
    ));
  };

  return (
    <div className="card mb-3">
      <div className="card-header d-flex justify-content-between align-items-center">
        <div>
          <strong>{post.author.displayName}</strong>
          <small className="text-muted ms-2">{formatDate(post.createdAt)}</small>
        </div>
        {post.updatedAt !== post.createdAt && (
          <small className="text-muted">
            Edited: {formatDate(post.updatedAt)}
          </small>
        )}
      </div>
      <div className="card-body">
        {error && (
          <div className="alert alert-danger alert-sm" role="alert">
            {error}
          </div>
        )}
        <p className="card-text">{post.status}</p>
        {post.hashtags.length > 0 && (
          <div className="mb-3">{renderHashtags(post.hashtags)}</div>
        )}
      </div>
      <div className="card-footer">
        <div className="row">
          <div className="col">
            <div className="d-flex align-items-center">
              {isAuthenticated && (
                <>
                  <button
                    className={`btn btn-sm me-2 ${
                      post.likedByCurrentUser ? 'btn-primary' : 'btn-outline-primary'
                    }`}
                    onClick={handleLike}
                    disabled={loading}
                  >
                    👍 {post.likes}
                  </button>
                  <button
                    className={`btn btn-sm me-2 ${
                      post.dislikedByCurrentUser ? 'btn-danger' : 'btn-outline-danger'
                    }`}
                    onClick={handleDislike}
                    disabled={loading}
                  >
                    👎 {post.dislikes}
                  </button>
                </>
              )}
              {!isAuthenticated && (
                <>
                  <span className="me-3">👍 {post.likes}</span>
                  <span className="me-3">👎 {post.dislikes}</span>
                </>
              )}
              <span>💬 {post.commentCount} comments</span>
            </div>
          </div>
          {isAuthenticated && (
            <div className="col-auto">
              <div className="btn-group" role="group">
                <button
                  className="btn btn-sm btn-outline-danger"
                  onClick={handleDelete}
                  disabled={loading}
                  title="Delete post"
                >
                  🗑️
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default PostCard;