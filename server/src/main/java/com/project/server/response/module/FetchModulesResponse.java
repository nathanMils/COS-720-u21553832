package com.project.server.response.module;

import com.project.server.model.dto.ModuleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchModulesResponse {
    private List<ModuleDTO> moduleDTOS;
}
