package com.horrorcore.eva_social.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSummaryResponse {
    private String email;
    private String displayName;
    private String profilePictureUrl;
    private boolean isVerified;
}
