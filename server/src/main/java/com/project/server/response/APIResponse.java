package com.project.server.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse<T> {
    private ResponseCode status;
    private String internalCode;
    private T data;

    public static <T> APIResponse<T> success(T data, String message) {
        return APIResponse.<T> builder()
                .data(data)
                .status(ResponseCode.success)
                .internalCode(message)
                .build();
    }

    public static <T> APIResponse<T> error(String errorMessage) {
        return APIResponse.<T>builder()
                .status(ResponseCode.failed)
                .internalCode(errorMessage)
                .build();
    }
}
