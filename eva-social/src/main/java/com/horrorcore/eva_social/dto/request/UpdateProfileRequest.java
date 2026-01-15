package com.horrorcore.eva_social.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {
    @Size(max = 100, message = "Display name must not exceed 100 characters")
    private String displayName;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    private String profilePictureUrl;

    private String coverPhotoUrl;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    private String website;

    private LocalDate dateOfBirth;

    private Boolean isPrivate;
}
