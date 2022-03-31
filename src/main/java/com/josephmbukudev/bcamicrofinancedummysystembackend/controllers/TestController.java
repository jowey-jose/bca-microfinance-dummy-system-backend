package com.josephmbukudev.bcamicrofinancedummysystembackend.controllers;

// Test Auth APIs:
    // - "/api/test/all" for public access.
    // - "/api/test/loan_applicant" for users who have ROLE_LOAN_APPLICANT or ROLE_SUPERVISOR or ROLE_CREDIT_OFFICER or ROLE_OFFICE_ADMIN
    // - "/api/test/supervisor" for users who have ROLE_SUPERVISOR
    // - "/api/test/credit_officer" for users who have ROLE_CREDIT_OFFICER
    // - "/api/test/office_admin" for users who have ROLE_OFFICE_ADMIN

// Spring Security is configured with @EnableGlobalMethodSecurity(prePostEnabled = true) for WebSecurityConfig class.
// Therefore, methods in our APIs can be secured with @PreAuthorize annotation easily.

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/loan_applicant")
    @PreAuthorize("hasRole('LOAN_APPLICANT') or hasRole('SUPERVISOR') or hasRole('CREDIT_OFFICER') or hasRole('OFFICE_ADMIN')")
    public String loanApplicantAccess() {
        return "Loan Applicant Content";
    }

    @GetMapping("/supervisor")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public  String supervisorAccess() {
        return "Supervisor Board";
    }

    @GetMapping("/credit_officer")
    @PreAuthorize("hasRole('CREDIT_OFFICER')")
    public String creditOfficerAccess() {
        return "Credit Officer Board";
    }

    @GetMapping("/office_admin")
    @PreAuthorize("hasRole('OFFICE_ADMIN')")
    public String officeAdminAccess() {
        return "Office Admin Board";
    }
}
