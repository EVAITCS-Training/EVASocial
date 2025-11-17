package com.horrorcore.dtos;

/**
 * Simple request DTO holding the information required to create a new Post.
 */
public record PostNewPostRequest(
        String status,
        String username
) {
}
