package com.horrorcore.utils;

import com.horrorcore.dtos.PostInformation;
import com.horrorcore.models.Post;

public class PostMapper {
    public static PostInformation toDto(Post post) {
        return new PostInformation(
                post.getId(),
                post.getStatus(),
                post.getUsername(),
                post.getLikes(),
                post.getDislikes(),
                post.getComments()
        );
    }
}
