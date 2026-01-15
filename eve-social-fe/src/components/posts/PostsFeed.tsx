import React, { useEffect, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { apiService } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import PostCard from './PostCard';
import CreatePostForm from './CreatePostForm';
import type { PostResponse } from '../../types/api';

const PostsFeed: React.FC = () => {
  const { isAuthenticated } = useAuth();
  const [posts, setPosts] = useState<PostResponse[]>([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedHashtag, setSelectedHashtag] = useState<string>('');

  // Query for public posts or user feed
  const {
    data: postsData,
    error,
    isLoading,
    refetch,
  } = useQuery({
    queryKey: ['posts', isAuthenticated ? 'feed' : 'public', page, searchQuery, selectedHashtag],
    queryFn: async () => {
      if (searchQuery) {
        return apiService.searchPosts(searchQuery);
      } else if (selectedHashtag) {
        return apiService.getPostsByHashtag(selectedHashtag);
      } else {
        // Always use public posts for now until authentication is fully working
        return apiService.getPublicPosts(page);
      }
    },
    refetchOnWindowFocus: false,
  });

  // Query for trending hashtags
  const { data: trendingHashtags } = useQuery({
    queryKey: ['trending-hashtags'],
    queryFn: apiService.getTrendingHashtags,
    refetchOnWindowFocus: false,
  });

  useEffect(() => {
    if (postsData) {
      if (page === 0 || searchQuery || selectedHashtag) {
        setPosts(postsData);
      } else {
        setPosts(prev => [...prev, ...postsData]);
      }
      
      // Check if we have more posts to load
      setHasMore(postsData.length === 20); // Assuming page size is 20
    }
  }, [postsData, page, searchQuery, selectedHashtag]);

  const handlePostCreated = (newPost: PostResponse) => {
    setPosts(prev => [newPost, ...prev]);
  };

  const handlePostUpdated = (updatedPost: PostResponse) => {
    setPosts(prev =>
      prev.map(post => (post.id === updatedPost.id ? updatedPost : post))
    );
  };

  const handlePostDeleted = (postId: number) => {
    setPosts(prev => prev.filter(post => post.id !== postId));
  };

  const handleSearch = (query: string) => {
    setSearchQuery(query);
    setSelectedHashtag('');
    setPage(0);
  };

  const handleHashtagClick = (hashtag: string) => {
    setSelectedHashtag(hashtag);
    setSearchQuery('');
    setPage(0);
  };

  const loadMore = () => {
    setPage(prev => prev + 1);
  };

  const clearFilters = () => {
    setSearchQuery('');
    setSelectedHashtag('');
    setPage(0);
    refetch();
  };

  return (
    <div className="container-fluid">
      <div className="row">
        {/* Sidebar with hashtags */}
        <div className="col-md-3">
          <div className="card">
            <div className="card-header">
              <h5 className="card-title mb-0">Trending Hashtags</h5>
            </div>
            <div className="card-body">
              {trendingHashtags && trendingHashtags.length > 0 ? (
                <div className="d-flex flex-column gap-2">
                  {trendingHashtags.slice(0, 10).map((hashtag, index) => (
                    <button
                      key={index}
                      className={`btn btn-sm ${
                        selectedHashtag === hashtag.tag
                          ? 'btn-primary'
                          : 'btn-outline-primary'
                      } text-start`}
                      onClick={() => handleHashtagClick(hashtag.tag)}
                    >
                      #{hashtag.tag}
                      <span className="badge bg-light text-dark ms-2">
                        {hashtag.postCount}
                      </span>
                    </button>
                  ))}
                </div>
              ) : (
                <p className="text-muted">No trending hashtags yet.</p>
              )}
            </div>
          </div>
        </div>

        {/* Main feed */}
        <div className="col-md-6">
          {/* Search bar */}
          <div className="card mb-4">
            <div className="card-body">
              <div className="d-flex gap-2">
                <input
                  type="text"
                  className="form-control"
                  placeholder="Search posts..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                      handleSearch(searchQuery);
                    }
                  }}
                />
                <button
                  className="btn btn-primary"
                  onClick={() => handleSearch(searchQuery)}
                >
                  Search
                </button>
                {(searchQuery || selectedHashtag) && (
                  <button className="btn btn-secondary" onClick={clearFilters}>
                    Clear
                  </button>
                )}
              </div>
              {searchQuery && (
                <small className="text-muted">
                  Searching for: "{searchQuery}"
                </small>
              )}
              {selectedHashtag && (
                <small className="text-muted">
                  Showing posts with #{selectedHashtag}
                </small>
              )}
            </div>
          </div>

          {/* Create post form (only for authenticated users) */}
          {isAuthenticated && !searchQuery && !selectedHashtag && (
            <CreatePostForm onPostCreated={handlePostCreated} />
          )}

          {/* Posts feed */}
          {error && (
            <div className="alert alert-danger" role="alert">
              Error loading posts: {error instanceof Error ? error.message : 'Unknown error'}
            </div>
          )}

          {isLoading && posts.length === 0 && (
            <div className="text-center py-5">
              <div className="spinner-border" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
              <p className="mt-2">Loading posts...</p>
            </div>
          )}

          {posts.length === 0 && !isLoading && (
            <div className="text-center py-5">
              <h5>No posts found</h5>
              <p className="text-muted">
                {searchQuery || selectedHashtag
                  ? 'Try adjusting your search or filters.'
                  : 'Be the first to create a post!'}
              </p>
            </div>
          )}

          {posts.map((post) => (
            <PostCard
              key={post.id}
              post={post}
              onUpdate={handlePostUpdated}
              onDelete={handlePostDeleted}
            />
          ))}

          {/* Load more button */}
          {hasMore && posts.length > 0 && !searchQuery && !selectedHashtag && (
            <div className="text-center mb-4">
              <button
                className="btn btn-outline-primary"
                onClick={loadMore}
                disabled={isLoading}
              >
                {isLoading ? 'Loading...' : 'Load More Posts'}
              </button>
            </div>
          )}
        </div>

        {/* Right sidebar - could be used for other features */}
        <div className="col-md-3">
          <div className="card">
            <div className="card-header">
              <h5 className="card-title mb-0">Welcome to Eva Social</h5>
            </div>
            <div className="card-body">
              {isAuthenticated ? (
                <>
                  <p>Welcome back!</p>
                  <p>Share your thoughts and connect with others.</p>
                </>
              ) : (
                <>
                  <p>Join Eva Social to:</p>
                  <ul>
                    <li>Create and share posts</li>
                    <li>Like and comment on content</li>
                    <li>Follow trending hashtags</li>
                  </ul>
                </>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PostsFeed;