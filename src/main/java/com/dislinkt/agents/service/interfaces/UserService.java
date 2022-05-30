package com.dislinkt.agents.service.interfaces;

import com.dislinkt.agents.dto.CompanyDTO;
import com.dislinkt.agents.dto.CompanyOwnerRequestDTO;
import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.model.ApplicationUser;

import java.util.List;

public interface UserService {

    List<ApplicationUser> findAll();

    ApplicationUser findById(String id);

    ApplicationUser findByEmail(String email);

    ApplicationUser registerNewUser(UserDTO newUser);

    boolean sendCompanyOwnerRequest(CompanyDTO company);

    boolean acceptCompanyOwnerRequest(CompanyDTO company);

    boolean rejectCompanyOwnerRequest(CompanyDTO company);

    UserDTO findByIdDTO(String userId);

    List<UserDTO> findAllDTO();

    List<CompanyOwnerRequestDTO> getCompanyOwnerRequests();

    boolean updateApiToken(UserDTO user);
}
