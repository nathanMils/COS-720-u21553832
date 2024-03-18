package com.project.server.response.module;

import com.project.server.model.dto.ModuleDTO;
import com.project.server.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateModuleResponse {
    private List<ModuleDTO> moduleDTOS;
}
