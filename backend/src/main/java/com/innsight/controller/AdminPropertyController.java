package com.innsight.controller;

import com.innsight.dto.AdminPropertyResponse;
import com.innsight.model.Property;
import com.innsight.service.AdminPropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/properties")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPropertyController {

    private final AdminPropertyService adminPropertyService;

    public AdminPropertyController(AdminPropertyService adminPropertyService) {
        this.adminPropertyService = adminPropertyService;
    }

    @GetMapping
    public List<AdminPropertyResponse> listProperties() {
        return adminPropertyService.getAllProperties();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminPropertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }
}

