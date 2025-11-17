package com.horrorcore.utils;

import com.horrorcore.dtos.PostInformation;
import com.horrorcore.models.Post;

/**
 * Simple mapper converting domain {@link Post} objects into presentation
 * {@link PostInformation} DTOs. Keeps the UI layer decoupled from internal
 * domain types.
 */
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
