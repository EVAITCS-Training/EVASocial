package com.horrorcore.eva_social.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HashtagResponse {
    private Long id;
    private String tag;
    private long useCount;
}
