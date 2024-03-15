package com.project.server.request.user;

import com.project.server.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserRequest {
    private UserDTO userDTO;
}
