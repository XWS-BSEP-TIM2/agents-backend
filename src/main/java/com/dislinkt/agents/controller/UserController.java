package com.dislinkt.agents.controller;

import com.dislinkt.agents.dto.CompanyDTO;
import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/company-owner-request")
    @PreAuthorize("hasRole('USER')")
    public boolean sendCompanyOwnerRequest(@RequestBody CompanyDTO company) {
        return this.userService.sendCompanyOwnerRequest(company);
    }

    @PostMapping("company-owner-request/accept")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean acceptCompanyOwnerRequest(@RequestBody CompanyDTO company) {
        return this.userService.acceptCompanyOwnerRequest(company);
    }

    @DeleteMapping("company-owner-request/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean rejectCompanyOwnerRequest(@RequestBody CompanyDTO company) {
        return this.userService.rejectCompanyOwnerRequest(company);
    }

}
