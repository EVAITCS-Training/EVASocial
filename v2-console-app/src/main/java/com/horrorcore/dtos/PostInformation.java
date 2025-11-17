package com.horrorcore.dtos;

import com.horrorcore.models.Comment;

import java.util.List;

/**
 * DTO (data transfer object) used to present Post data to the UI.
 * Immutable record containing only the fields required for display.
 */
public record PostInformation(
        long id,
        String status,
        String username,
        int likes,
        int dislikes,
        List<Comment> comments
) {
}
