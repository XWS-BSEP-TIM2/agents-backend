package com.dislinkt.agents.service;

import com.dislinkt.agents.dto.CompanyDTO;
import com.dislinkt.agents.model.Company;
import com.dislinkt.agents.service.interfaces.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private MongoTemplate mongoTemplate;

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
        return null;
    }

    @Override
    public CompanyDTO findByIdDTO(String companyId) {
        return null;
    }

    @Override
    public boolean update(CompanyDTO company) {
        return false;
    }
}
