package com.project.server.response.student;

import com.project.server.model.dto.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchModuleContentResponse {
    private String name;
    private String description;
    private List<PostDTO> postDTOS;
}
