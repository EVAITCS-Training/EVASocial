package com.horrorcore.eva_social.exceptions;

import java.net.URI;
import java.time.LocalDateTime;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        URI uri,
        String message
) {
}
