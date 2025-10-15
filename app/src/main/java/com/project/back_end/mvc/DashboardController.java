package com.project.back_end.mvc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController {

    // 2. Autowire the shared service (assumed name: TokenValidationService)
    @Autowired
    private TokenValidationService tokenValidationService;

    // 3. Admin dashboard handler
    @GetMapping("/adminDashboard/{token}")
    public ModelAndView adminDashboard(@PathVariable("token") String token) {
        boolean isValid = tokenValidationService.validateToken(token, "admin");

        if (isValid) {
            return new ModelAndView("admin/adminDashboard");
        } else {
            return new ModelAndView("redirect:/");
        }
    }

    // 4. Doctor dashboard handler
    @GetMapping("/doctorDashboard/{token}")
    public ModelAndView doctorDashboard(@PathVariable("token") String token) {
        boolean isValid = tokenValidationService.validateToken(token, "doctor");

        if (isValid) {
            return new ModelAndView("doctor/doctorDashboard");
        } else {
            return new ModelAndView("redirect:/");
        }
    }
}

