package com.horrorcore.eva_social.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentRequest {
    @NotBlank(message = "Message is required")
    @Size(min = 3, max = 300, message = "Message must be between 3 and 300 characters")
    private String message;

    @NotNull(message = "Post ID is required")
    private Long postId;

    private Long parentCommentId;
}
