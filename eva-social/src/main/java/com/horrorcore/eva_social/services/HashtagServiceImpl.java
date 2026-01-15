package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.dto.response.HashtagResponse;
import com.horrorcore.eva_social.dto.response.PostResponse;
import com.horrorcore.eva_social.entites.Hashtag;
import com.horrorcore.eva_social.entites.Post;
import com.horrorcore.eva_social.entites.UserCredential;
import com.horrorcore.eva_social.repositories.HashtagRepository;
import com.horrorcore.eva_social.repositories.PostRepository;
import com.horrorcore.eva_social.utils.HashtagExtractor;
import com.horrorcore.eva_social.utils.PostMapper;
import com.horrorcore.eva_social.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final PostRepository postRepository;
    private final HashtagExtractor hashtagExtractor;
    private final PostMapper postMapper;

    @Override
    @Transactional
    public void extractAndSaveHashtags(Post post, String statusText) {
        List<String> tags = hashtagExtractor.extractHashtags(statusText);
        Set<Hashtag> hashtags = new HashSet<>();

        for (String tagString : tags) {
            Hashtag hashtag = hashtagRepository.findByTag(tagString)
                    .orElseGet(() -> {
                        Hashtag newHashtag = Hashtag.builder()
                                .tag(tagString)
                                .useCount(0)
                                .build();
                        return hashtagRepository.save(newHashtag);
                    });

            hashtag.setUseCount(hashtag.getUseCount() + 1);
            hashtag.getPosts().add(post);
            hashtags.add(hashtagRepository.save(hashtag));
        }

        post.setHashtags(hashtags);
    }

    @Override
    public List<HashtagResponse> getTrendingHashtags() {
        List<Hashtag> hashtags = hashtagRepository.findTop10ByOrderByUseCountDesc();
        return hashtags.stream()
                .map(hashtag -> HashtagResponse.builder()
                        .id(hashtag.getId())
                        .tag(hashtag.getTag())
                        .useCount(hashtag.getUseCount())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getPostsByHashtag(String tag) {
        UserCredential currentUser = SecurityUtils.isAuthenticated() ? SecurityUtils.getCurrentUser() : null;
        List<Post> posts = postRepository.findByHashtagOrderByCreatedAtDesc(tag.toLowerCase());
        return posts.stream()
                .map(post -> postMapper.toPostResponse(post, currentUser))
                .collect(Collectors.toList());
    }
}
