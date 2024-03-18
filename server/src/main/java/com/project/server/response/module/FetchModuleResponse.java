package com.project.server.response.module;

import com.project.server.model.dto.ModuleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchModuleResponse {
    private ModuleDTO moduleDTO;
}
