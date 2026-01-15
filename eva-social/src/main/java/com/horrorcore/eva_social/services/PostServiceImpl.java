package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.dto.request.CreatePostRequest;
import com.horrorcore.eva_social.dto.request.UpdatePostRequest;
import com.horrorcore.eva_social.dto.response.FeedPostResponse;
import com.horrorcore.eva_social.dto.response.PostResponse;
import com.horrorcore.eva_social.entites.Follow;
import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.entites.UserProfile;
import com.horrorcore.eva_social.enums.NotificationType;
import com.horrorcore.eva_social.exceptions.ForbiddenException;
import com.horrorcore.eva_social.exceptions.PostNotFoundException;
import com.horrorcore.eva_social.exceptions.UserNotFoundException;
import com.horrorcore.eva_social.repositories.FollowRepository;
import com.horrorcore.eva_social.repositories.PostRepository;
import com.horrorcore.eva_social.repositories.UserCredentialRepository;
import com.horrorcore.eva_social.repositories.UserProfileRepository;
import com.horrorcore.eva_social.utils.PostMapper;
import com.horrorcore.eva_social.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final UserProfileRepository userProfileRepository;
    private final FollowRepository followRepository;
    private final HashtagService hashtagService;
    private final NotificationService notificationService;
    private final PostMapper postMapper;

    @Override
    @Transactional
    public PostResponse createPost(String authorEmail, CreatePostRequest request) {
        UserCredential author = userCredentialRepository.findById(authorEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = Post.builder()
                .author(author)
                .status(request.getStatus())
                .isPublic(request.isPublic())
                .isDraft(request.isDraft())
                .build();

        Post savedPost = postRepository.save(post);

        // Extract and save hashtags
        hashtagService.extractAndSaveHashtags(savedPost, request.getStatus());
        savedPost = postRepository.save(savedPost);

        // Update user's post count
        UserProfile profile = userProfileRepository.findByUserCredential(author).orElse(null);
        if (profile != null) {
            profile.setPostCount(profile.getPostCount() + 1);
            userProfileRepository.save(profile);
        }

        return postMapper.toPostResponse(savedPost, author);
    }

    @Override
    @Transactional
    public PostResponse updatePost(String authorEmail, Long postId, UpdatePostRequest request) {
        UserCredential author = userCredentialRepository.findById(authorEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        if (!post.getAuthor().getEmail().equals(author.getEmail())) {
            throw new ForbiddenException("Cannot update other users' posts");
        }

        if (request.getStatus() != null) {
            post.setStatus(request.getStatus());
            // Update hashtags
            post.getHashtags().clear();
            hashtagService.extractAndSaveHashtags(post, request.getStatus());
        }

        if (request.getIsPublic() != null) {
            post.setPublic(request.getIsPublic());
        }

        Post savedPost = postRepository.save(post);
        return postMapper.toPostResponse(savedPost, author);
    }

    @Override
    @Transactional
    public void deletePost(String authorEmail, Long postId) {
        UserCredential author = userCredentialRepository.findById(authorEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        if (!post.getAuthor().getEmail().equals(author.getEmail())) {
            throw new ForbiddenException("Cannot delete other users' posts");
        }

        postRepository.delete(post);

        // Update user's post count
        UserProfile profile = userProfileRepository.findByUserCredential(author).orElse(null);
        if (profile != null) {
            profile.setPostCount(Math.max(0, profile.getPostCount() - 1));
            userProfileRepository.save(profile);
        }
    }

    @Override
    @Transactional
    public PostResponse getPostById(Long postId, String currentUserEmail) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        UserCredential currentUser = null;
        if (currentUserEmail != null) {
            currentUser = userCredentialRepository.findById(currentUserEmail.toLowerCase()).orElse(null);
        }

        // Increment view count
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);

        return postMapper.toPostResponse(post, currentUser);
    }

    @Override
    public List<PostResponse> getUserPosts(Long userId, String currentUserEmail) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserCredential currentUser = null;
        if (currentUserEmail != null) {
            currentUser = userCredentialRepository.findById(currentUserEmail.toLowerCase()).orElse(null);
        }

        List<Post> posts = postRepository.findByAuthorOrderByCreatedAtDesc(profile.getUserCredential());
        
        UserCredential finalCurrentUser = currentUser;
        return posts.stream()
                .filter(post -> post.isPublic() || 
                       (finalCurrentUser != null && post.getAuthor().getEmail().equals(finalCurrentUser.getEmail())))
                .map(post -> postMapper.toPostResponse(post, finalCurrentUser))
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedPostResponse> getFeed(String userEmail, int page, int size) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Follow> following = followRepository.findByFollower(user);
        
        if (following.isEmpty()) {
            return new ArrayList<>();
        }

        List<UserCredential> followedUsers = following.stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());

        List<Post> posts = postRepository.findByAuthorInAndIsPublicTrueOrderByCreatedAtDesc(followedUsers);

        return posts.stream()
                .skip((long) page * size)
                .limit(size)
                .map(post -> postMapper.toFeedPostResponse(post, user, true))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getPublicPosts(int page, int size) {
        UserCredential currentUser = SecurityUtils.isAuthenticated() ? SecurityUtils.getCurrentUser() : null;

        List<Post> posts = postRepository.findByIsPublicTrueOrderByCreatedAtDesc();

        return posts.stream()
                .filter(post -> !post.isDraft())
                .skip((long) page * size)
                .limit(size)
                .map(post -> postMapper.toPostResponse(post, currentUser))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void likePost(String userEmail, Long postId) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // Remove dislike if exists
        post.getDislikedBy().remove(user);
        
        // Add like
        post.getLikedBy().add(user);
        postRepository.save(post);

        // Create notification
        if (post.getAuthor() != null) {
            notificationService.createNotification(
                post.getAuthor(),
                user,
                NotificationType.LIKE,
                post,
                null,
                user.getEmail() + " liked your post"
            );
        }
    }

    @Override
    @Transactional
    public void unlikePost(String userEmail, Long postId) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        post.getLikedBy().remove(user);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void dislikePost(String userEmail, Long postId) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // Remove like if exists
        post.getLikedBy().remove(user);
        
        // Add dislike
        post.getDislikedBy().add(user);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void undislikePost(String userEmail, Long postId) {
        UserCredential user = userCredentialRepository.findById(userEmail.toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        post.getDislikedBy().remove(user);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void incrementViewCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
    }

    @Override
    public List<PostResponse> searchPosts(String query) {
        UserCredential currentUser = SecurityUtils.isAuthenticated() ? SecurityUtils.getCurrentUser() : null;

        List<Post> posts = postRepository.findByStatusContainingIgnoreCaseAndIsPublicTrue(query);

        return posts.stream()
                .filter(post -> !post.isDraft())
                .map(post -> postMapper.toPostResponse(post, currentUser))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getPostsByHashtag(String tag) {
        return hashtagService.getPostsByHashtag(tag);
    }
}
