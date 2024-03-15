package com.project.server.service;

import com.project.server.exception.ModuleNameAlreadyExists;
import com.project.server.model.entity.ModuleEntity;
import com.project.server.repository.ModuleRepository;
import com.project.server.request.module.AddModuleRequest;
import com.project.server.response.module.CreateModuleResponse;
import com.project.server.response.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

    public CreateModuleResponse addModule(
            AddModuleRequest request
    ) {
        if (moduleRepository.existsByModuleName(request.getModuleDTO().getModuleName())) {
            throw new ModuleNameAlreadyExists(String.format("Module with the name: '%s' already exists",request.getModuleDTO().getModuleName()));
        }
        moduleRepository.save(
                ModuleEntity.builder()
                        .moduleName(request.getModuleDTO().getModuleName())
                        .build()
        );
        return CreateModuleResponse.builder()
                .code(Code.success)
                .build();
    }
}
