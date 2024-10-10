package edu.tcu.cs.hogwartsartifactsonline.system;

/**
 * Spring provides HTTP status codes but this enum can be useful to store custom HTTP status codes.
 */
public enum StatusCode {
    SUCCESS (200),
    CREATED (201),
    INVALID_ARGUMENT(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int httpStatusCodeValue;

    StatusCode(int httpStatusCodeValue) {
        this.httpStatusCodeValue = httpStatusCodeValue;
    }

    public int getHttpStatusCodeValue() {
        return this.httpStatusCodeValue;
    }
}
