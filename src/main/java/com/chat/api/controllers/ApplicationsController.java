package com.chat.api.controllers;

import com.chat.api.controllers.resources.ApplicationRequest;
import com.chat.api.controllers.resources.ApplicationResponse;
import com.chat.services.ApplicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/applications")
public class ApplicationsController {

    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAllApplications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(applicationService.getAllApplications(page, limit));
    }
    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(@Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.status(201).body(applicationService.createApplication(request));
    }

    @GetMapping("/{token}")
    public ResponseEntity<ApplicationResponse> getApplication(@PathVariable String token) {
        return ResponseEntity.ok(applicationService.getApplicationByToken(token));
    }

    @PutMapping("/{token}")
    public ResponseEntity<ApplicationResponse> updateApplication(
            @PathVariable String token, @Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.update(token, request));
    }


    @DeleteMapping("/{token}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String token) {
        applicationService.deleteApplication(token);
        return ResponseEntity.ok().build();
    }
}
