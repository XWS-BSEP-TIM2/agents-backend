package com.dislinkt.agents.controller;

import com.dislinkt.agents.dto.CompanyDTO;
import com.dislinkt.agents.dto.CompanyOwnerRequestDTO;
import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/company-owner-request")
    @PreAuthorize("hasAuthority('SEND_COMPANY_OWNER_REQUEST')")
    public boolean sendCompanyOwnerRequest(@RequestBody CompanyDTO company) {
        return this.userService.sendCompanyOwnerRequest(company);
    }

    @PostMapping("/company-owner-request/accept")
    @PreAuthorize("hasAuthority('PROCESS_COMPANY_OWNER_REQUESTS')")
    public boolean acceptCompanyOwnerRequest(@RequestBody CompanyDTO company) {
        return this.userService.acceptCompanyOwnerRequest(company);
    }

    @PutMapping("/company-owner-request/reject")
    @PreAuthorize("hasAuthority('PROCESS_COMPANY_OWNER_REQUESTS')")
    public boolean rejectCompanyOwnerRequest(@RequestBody CompanyDTO company) {
        return this.userService.rejectCompanyOwnerRequest(company);
    }

    @GetMapping("/company-owner-requests")
    @PreAuthorize("hasAuthority('PROCESS_COMPANY_OWNER_REQUESTS')")
    public List<CompanyOwnerRequestDTO> getCompanyOwnerRequests() {
        return this.userService.getCompanyOwnerRequests();
    }

    @PutMapping("/apiToken")
    @PreAuthorize("hasAuthority('UPDATE_API_TOKEN')")
    public boolean updateApiToken(@RequestBody UserDTO user) {
        return this.userService.updateApiToken(user);
    }


}
