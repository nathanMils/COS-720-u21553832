package com.project.server.controller;

import com.project.server.request.user.UpgradeUserRequest;
import com.project.server.response.APIResponse;
import com.project.server.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/app/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    public final AdminService adminService;
    @PostMapping("/upgrade")
    public ResponseEntity<APIResponse<Void>> upgradeUser(
            @RequestBody UpgradeUserRequest request
    ) {
        try {
            adminService.upgradeUser(request);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            APIResponse.success(null,"USER_UPGRADED")
                    );
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            APIResponse.error(e.getMessage())
                    );
        }
    }
}
