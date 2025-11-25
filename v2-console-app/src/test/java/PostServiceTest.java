import com.horrorcore.dtos.PostInformation;
import com.horrorcore.dtos.UpdatePostRequest;
import com.horrorcore.exceptions.PostNotFoundException;
import com.horrorcore.models.Post;
import com.horrorcore.repositories.InMemoryPostRepository;
import com.horrorcore.repositories.PostRepository;
import com.horrorcore.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PostServiceTest {

    private PostRepository postRepository;
    private PostService postService;

    @BeforeEach
    void setup() {
        postRepository = new InMemoryPostRepository();
        postService = new PostService(postRepository);
    }

    //@Test or @ParameterizedTest
    @Test
    void PostService_updatePost_shouldSucceed() {
        Post post = new Post();
        post.setId(1l);
        post.setDraft(true);
        post.setComments(new ArrayList<>());
        post.setCreatedAt(LocalDateTime.now());
        post.setDislikes(4);
        post.setLikes(1);
        post.setStatus("Test");
        post.setUsername("testuser001");
        postRepository.save(post);
        UpdatePostRequest request = new UpdatePostRequest(1l, "We have changed");
        PostInformation updatePost = postService.updatePost(request);

        assertNotEquals("Test", updatePost.status());
    }

    @ParameterizedTest
    @ValueSource(longs = {-10l, 0l, 100l})
    void PostService_updatePost_InvalidIdThrowsException(long id) {
        UpdatePostRequest request = new UpdatePostRequest(id, "Test");
        PostNotFoundException exception = assertThrows(
                PostNotFoundException.class,
                () -> postService.updatePost(request)
        );

        assertEquals("Post with id of " + id + " not found", exception.getMessage());
    }
}
