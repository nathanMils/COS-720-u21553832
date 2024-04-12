package com.project.server.controller;

import com.project.server.request.elastic.SendUserAlertRequest;
import com.project.server.response.APIResponse;
import com.project.server.service.ElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/elastic")
@RequiredArgsConstructor
public class ElasticController {

    private final ElasticService elasticService;
    @GetMapping("/sendAlert")
    public ResponseEntity<APIResponse<Void>> sendAlert(
            @RequestBody SendUserAlertRequest request
    ) {
        elasticService.sendUserAlert(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                                null,
                                "SUCCESS"
                        )
                );
    }
}
