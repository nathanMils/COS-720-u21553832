package com.project.server.response.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchLectureResponse {
    private String fileName;
    private String fileType;
    private String description;
    private byte[] content;
}
