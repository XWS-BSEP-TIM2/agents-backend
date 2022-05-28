package com.dislinkt.agents.service;

import com.dislinkt.agents.dto.CompanyDTO;
import com.dislinkt.agents.model.Company;
import com.dislinkt.agents.service.interfaces.CompanyService;
import com.dislinkt.agents.service.interfaces.ConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ConverterService converterService;

    @Override
    public Company getCompanyByUserId(String id) {
        return null;
    }

    @Override
    public List<Company> findAll() {
        return mongoTemplate.findAll(Company.class);
    }

    @Override
    public List<CompanyDTO> findAllDTO() {
        List<CompanyDTO> retVal = new ArrayList<>();
        for (Company company: mongoTemplate.findAll(Company.class)) {
            retVal.add(converterService.companyToDto(company));
        }
        return retVal;
    }

    @Override
    public CompanyDTO findByIdDTO(String companyId) {
        Company company = mongoTemplate.findById(companyId, Company.class);
        if (company != null) {
            return converterService.companyToDto(company);
        } else {
            return null;
        }
    }

    @Override
    public boolean update(CompanyDTO company) {
        Company companyToUpdate = mongoTemplate.findById(company.getId(), Company.class);
        if (companyToUpdate != null) {
            companyToUpdate.setName(company.name);
            companyToUpdate.setDescription(company.description);
            companyToUpdate.setEmailList(company.emailList);
            companyToUpdate.setPhoneNumberList(company.phoneNumberList);
            companyToUpdate.setTagline(company.tagline);
            companyToUpdate.setTechnologies(company.technologies);
            companyToUpdate = mongoTemplate.save(companyToUpdate);

            return true;
        } else {
            return false;
        }

    }
}
