package com.project.server.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


/**
 * APIResponse is a generic class for wrapping the response of the API.
 * It contains the status of the response, an internal code, the data of the response,
 * and maps for errors and warnings.
 *
 * @param <T> the type of the data in the response
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse<T> {
    private ResponseCode status;
    private String internalCode;
    private T data;
    private Map<String,String> errors;
    private Map<String,String> warnings;

    /**
     * Creates a success APIResponse with the given data and message.
     *
     * @param data the data of the response
     * @param message the message of the response
     * @param <T> the type of the data in the response
     * @return a success APIResponse
     */
    public static <T> APIResponse<T> success(T data, String message) {
        return APIResponse.<T> builder()
                .data(data)
                .status(ResponseCode.success)
                .internalCode(message)
                .build();
    }

    /**
     * Creates a success APIResponse with the given data and message.
     *
     * @param data the data of the response
     * @param <T> the type of the data in the response
     * @return a success APIResponse
     */
    public static <T> APIResponse<T> success(T data) {
        return APIResponse.<T> builder()
                .data(data)
                .status(ResponseCode.success)
                .internalCode("SUCCESS")
                .build();
    }

    /**
     * Creates a success APIResponse with the given data and message.
     *
     * @param <T> the type of the data in the response
     * @return a success APIResponse
     */
    public static <T> APIResponse<T> success() {
        return APIResponse.<T> builder()
                .data(null)
                .status(ResponseCode.success)
                .internalCode("SUCCESS")
                .build();
    }

    /**
     * Creates an error APIResponse with the given error message.
     *
     * @param errorMessage the error message of the response
     * @param <T> the type of the data in the response
     * @return an error APIResponse
     */
    public static <T> APIResponse<T> error(String errorMessage) {
        return APIResponse.<T>builder()
                .status(ResponseCode.failed)
                .internalCode(errorMessage)
                .build();
    }
}
