package com.project.server.service;

import com.project.server.exception.ModuleDoesNotExistException;
import com.project.server.exception.ModuleNameAlreadyExistsException;
import com.project.server.model.dto.ModuleDTO;
import com.project.server.model.entity.Module;
import com.project.server.repository.ModuleRepository;
import com.project.server.request.module.CreateModuleRequest;
import com.project.server.request.module.DeleteModuleRequest;
import com.project.server.request.module.FetchModuleRequest;
import com.project.server.response.module.CreateModuleResponse;
import com.project.server.response.module.DeleteModuleResponse;

import com.project.server.response.module.FetchModuleResponse;
import com.project.server.response.module.FetchModulesResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository repository;

    public CreateModuleResponse createModule(
            CreateModuleRequest request
    ) {
        if (repository.existsByName(request.getModuleDTO().getModuleName())) {
            throw new ModuleNameAlreadyExistsException(String.format("Module with the name: '%s' already exists",request.getModuleDTO().getModuleName()));
        }
        repository.save(
                Module.builder()
                        .name(request.getModuleDTO().getModuleName())
                        .build()
        );
        return CreateModuleResponse.builder()
                .moduleDTOS(
                        repository
                                .findAll()
                                .stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public DeleteModuleResponse deleteModule(
            DeleteModuleRequest request
    ) {
        if (!repository.existsById(request.getModuleDTO().getModuleId())) {
            throw new ModuleDoesNotExistException(String.format("%d" ,request.getModuleDTO().getModuleId()));
        }
        repository.deleteById(request.getModuleDTO().getModuleId());

        return DeleteModuleResponse.builder()
                .moduleDTOS(
                        repository.findAll()
                                .stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public FetchModulesResponse fetchModules() {
        return FetchModulesResponse.builder()
                .moduleDTOS(
                        repository.findAll()
                                .stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public FetchModuleResponse fetchModule(
            FetchModuleRequest request
    ) {
        if (!repository.existsByName(request.getModuleName())) {
            throw new ModuleDoesNotExistException(request.getModuleName());
        }

        return FetchModuleResponse.builder()
                .moduleDTO(
                        convertToDTO(repository.findByName(request.getModuleName()).orElseThrow())
                )
                .build();
    }

    private ModuleDTO convertToDTO(Module moduleEntity) {
        return ModuleDTO.builder()
                .moduleId(moduleEntity.getId())
                .moduleName(moduleEntity.getName())
                .build();
    }
}
