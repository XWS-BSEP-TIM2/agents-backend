package com.dislinkt.agents.controller;

import com.dislinkt.agents.dto.CompanyDTO;
import com.dislinkt.agents.service.LoggingService;
import com.dislinkt.agents.service.interfaces.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;
    private final LoggingService loggingService;

    @PutMapping()
    public boolean updateCompany(@RequestBody CompanyDTO company) {
        loggingService.MakeInfoLog("User " +company.user.getEmail() + " updating " + company.getName() + " information.");
        return companyService.update(company);
    }

}
