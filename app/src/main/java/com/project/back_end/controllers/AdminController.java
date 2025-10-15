package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.Service;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 1. Mark as REST controller
@RequestMapping("${api.path}admin") // Base URL: /api/admin if api.path=/api/
public class AdminController {

    private final Service service;

    // 2. Constructor-based injection
    //@Autowired
    public AdminController(Service service) {
        this.service = service;
    }

    // 3. Admin login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody Admin admin) {
        return service.validateAdmin(admin.getUsername(), admin.getPassword());
    }
}


