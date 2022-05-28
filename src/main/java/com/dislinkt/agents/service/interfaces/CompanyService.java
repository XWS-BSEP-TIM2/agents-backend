package com.dislinkt.agents.service.interfaces;

import com.dislinkt.agents.dto.CompanyDTO;
import com.dislinkt.agents.model.Company;

import java.util.List;

public interface CompanyService {

    List<Company> findAll();

    List<CompanyDTO> findAllDTO();

    CompanyDTO findByIdDTO(String companyId);

    boolean update(CompanyDTO company);
}
