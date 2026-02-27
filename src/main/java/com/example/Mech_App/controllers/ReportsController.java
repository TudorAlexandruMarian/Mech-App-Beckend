package com.example.Mech_App.controllers;


import com.example.Mech_App.models.CountsReport;
import com.example.Mech_App.services.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final ServiceFactory serviceFactory;

    @GetMapping("/getCounts")
    private ResponseEntity<CountsReport> getCounts() {
        return ResponseEntity.ok(serviceFactory.getReportsService().getCounts());
    }
}
