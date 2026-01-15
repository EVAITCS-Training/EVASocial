package com.horrorcore.eva_social.services;

import com.horrorcore.eva_social.dto.response.HashtagResponse;
import com.horrorcore.eva_social.dto.response.PostResponse;
import com.horrorcore.eva_social.entites.Post;

import java.util.List;

public interface HashtagService {
    void extractAndSaveHashtags(Post post, String statusText);
    List<HashtagResponse> getTrendingHashtags();
    List<PostResponse> getPostsByHashtag(String tag);
}
