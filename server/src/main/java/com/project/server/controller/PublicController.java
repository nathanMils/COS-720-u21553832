package com.project.server.controller;

import com.project.server.response.APIResponse;
import com.project.server.response.open.FetchCoursesResponse;
import com.project.server.service.PublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value="*")
@RequestMapping(path = "/app/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final PublicService publicService;

    @GetMapping("/fetchCourses")
    public ResponseEntity<APIResponse<FetchCoursesResponse>> fetchAllCourses() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponse.success(
                               publicService.getAllCourses(),
                               "SUCCESS"
                        )
                );
    }
}
