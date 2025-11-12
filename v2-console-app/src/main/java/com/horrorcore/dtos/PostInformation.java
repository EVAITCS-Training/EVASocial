package com.horrorcore.dtos;

import com.horrorcore.models.Comment;

import java.util.List;

public record PostInformation(
        long id,
        String status,
        String username,
        int likes,
        int dislikes,
        List<Comment> comments
) {
}
