package com.project.server.model.dto;

import com.project.server.constraint.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDTO {
    @NotNull
    @NotBlank(message = "Id is mandatory")
    @ValidUUID
    private UUID id;
    @NotNull
    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_]{3,29}$", message = "module name not valid")
    private String name;

    private String description;

    public StudentModuleDTO convert(boolean registered) {
        return StudentModuleDTO.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .registered(registered)
                .build();
    }
}
