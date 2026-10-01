package GMCA.gmca_api.common;

import java.time.OffsetDateTime;

public record ApiError(String message, OffsetDateTime timestamp) {
    public ApiError(String message) { this(message, OffsetDateTime.now()); }
}
