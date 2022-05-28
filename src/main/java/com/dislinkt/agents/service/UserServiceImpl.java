package com.dislinkt.agents.service;


import com.dislinkt.agents.dto.CompanyDTO;
import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.model.Company;
import com.dislinkt.agents.model.CompanyOwnerRequest;
import com.dislinkt.agents.model.Post;
import com.dislinkt.agents.model.enums.ApplicationUserRole;
import com.dislinkt.agents.model.enums.PostType;
import com.dislinkt.agents.service.interfaces.CompanyService;
import com.dislinkt.agents.service.interfaces.ConverterService;
import com.dislinkt.agents.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ConverterService converterService;

    @Override
    public List<ApplicationUser> findAll() {
        return mongoTemplate.findAll(ApplicationUser.class);
    }

    @Override
    public ApplicationUser findById(String id) {
        return mongoTemplate.findById(id, ApplicationUser.class);
    }

    @Override
    public ApplicationUser findByEmail(String email) {
        for (ApplicationUser user : findAll()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public ApplicationUser registerNewUser(UserDTO newUser) {
        if (findByEmail(newUser.getEmail()) == null) {
            ApplicationUser user = new ApplicationUser(null, newUser.name,
                    newUser.surname, newUser.email, new BCryptPasswordEncoder().encode(newUser.password), newUser.role);
            user = mongoTemplate.save(user);

            Post post = new Post();
            post.setUserId(user.getId());
            post.setPostType(PostType.NEW_USER);
            mongoTemplate.save(post);

            return user;
        }
        return null;
    }

    @Override
    public boolean sendCompanyOwnerRequest(CompanyDTO company) {
        if (companyService.getCompanyByUserId(company.user.id) == null) {
            Company newCompany = new Company();
            newCompany.setName(company.name);
            newCompany.setDescription(company.description);
            newCompany.setEmailList(company.emailList);
            newCompany.setPhoneNumberList(company.phoneNumberList);
            newCompany.setTagline(company.tagline);
            newCompany.setTechnologies(company.technologies);
            newCompany.setUserId(company.user.id);

            newCompany = mongoTemplate.save(newCompany);

            CompanyOwnerRequest request = new CompanyOwnerRequest();
            request.setCompanyId(newCompany.getId());

            return true;

        } else {
            return false;
        }
    }

    @Override
    public boolean acceptCompanyOwnerRequest(CompanyDTO company) {
        for (CompanyOwnerRequest request : mongoTemplate.findAll(CompanyOwnerRequest.class)) {
            if (request.getCompanyId().equals(company.getId())) {
                request.setAccepted(true);
                mongoTemplate.save(request);

                Company newCompany = mongoTemplate.findById(company.getId(), Company.class);
                newCompany.setVerified(true);
                newCompany = mongoTemplate.save(newCompany);

                ApplicationUser companyOwner = mongoTemplate.findById(newCompany.getUserId(), ApplicationUser.class);
                companyOwner.setRole(ApplicationUserRole.COMPANY_OWNER);
                companyOwner = mongoTemplate.save(companyOwner);

                Post post = new Post();
                post.setCompanyId(newCompany.getId());
                post.setPostType(PostType.NEW_COMPANY);
                mongoTemplate.save(post);

                return true;
            }
        } return false;
    }

    @Override
    public boolean rejectCompanyOwnerRequest(CompanyDTO company) {
        for (CompanyOwnerRequest request : mongoTemplate.findAll(CompanyOwnerRequest.class)) {
            if (request.getCompanyId().equals(company.getId())) {
                mongoTemplate.remove(request);

                Company newCompany = mongoTemplate.findById(company.getId(), Company.class);
                mongoTemplate.remove(newCompany);

                return true;
            }
        } return false;
    }

    @Override
    public UserDTO findByIdDTO(String userId) {
        ApplicationUser user = mongoTemplate.findById(userId, ApplicationUser.class);
        if (user == null) {
            return null;
        } else {
            return converterService.userToDto(user);
        }
    }

    @Override
    public List<UserDTO> findAllDTO() {
        List<UserDTO> retVal = new ArrayList<>();
        for (ApplicationUser user: findAll()) {
            retVal.add(converterService.userToDto(user));
        }
        return retVal;
    }

}
