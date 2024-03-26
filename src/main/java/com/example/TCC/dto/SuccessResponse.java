package com.example.TCC.dto;

public record SuccessResponse<T>(
        int status,
        String message,
        T data
) {
    public static <T> SuccessResponse<T> of(
            final SuccessMessage successMessage,
            final T data
    ) {
        return new SuccessResponse<T>(
                successMessage.getStatus(),
                successMessage.getMessage(),
                data);
    }

    public static SuccessResponse of(
            final SuccessMessage successMessage
    ) {
        return new SuccessResponse(
                successMessage.getStatus(),
                successMessage.getMessage(),
                ""
        );
    }
}
