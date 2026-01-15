package com.horrorcore.eva_social.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequest {
    @NotBlank(message = "Status is required")
    @Size(min = 5, max = 300, message = "Status must be between 5 and 300 characters")
    private String status;

    @Builder.Default
    private boolean isPublic = true;

    @Builder.Default
    private boolean isDraft = false;
}
