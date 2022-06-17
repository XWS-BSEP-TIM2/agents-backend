package com.dislinkt.agents.service;


import com.dislinkt.agents.dto.CompanyDTO;
import com.dislinkt.agents.dto.CompanyOwnerRequestDTO;
import com.dislinkt.agents.dto.RecoveryPasswordDTO;
import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.email.service.EmailService;
import com.dislinkt.agents.email.service.EmailServiceImpl;
import com.dislinkt.agents.model.*;
import com.dislinkt.agents.model.enums.ApplicationUserRole;
import com.dislinkt.agents.model.enums.PostType;
import com.dislinkt.agents.security.RandomCodeUtil;
import com.dislinkt.agents.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.AttributeList;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ConverterService converterService;

    @Autowired
    private RoleService roleService;

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    public static String APP_NAME = "DISLINKT_AGENTS";

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
            List<Role> roleList= new ArrayList<>();
            Role userRole=roleService.findRoleByID(92272036854775808L);
            roleList.add(userRole);
            ApplicationUser user = new ApplicationUser(null, newUser.name,
                    newUser.surname, newUser.email.toLowerCase(Locale.ROOT), new BCryptPasswordEncoder().encode(newUser.password), newUser.apiToken, roleList,"",false, false, false);

            String verificationCode = RandomCodeUtil.getCode(32);
            user.setNewVerificationCode(verificationCode);
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
            mongoTemplate.save(request);

            return true;

        } else {
            return false;
        }
    }

    @Override
    public boolean acceptCompanyOwnerRequest(CompanyDTO company) {
        for (CompanyOwnerRequest request : mongoTemplate.findAll(CompanyOwnerRequest.class)) {
            if (request.getCompanyId().equals(company.getId())) {
                mongoTemplate.remove(request);

                Company newCompany = mongoTemplate.findById(company.getId(), Company.class);
                newCompany.setVerified(true);
                newCompany = mongoTemplate.save(newCompany);

                ApplicationUser companyOwner = mongoTemplate.findById(newCompany.getUserId(), ApplicationUser.class);
                Role companyOwnerRole = roleService.findRoleByID(92272036854775809L);
                List<Role> newRoles = new ArrayList<>();
                newRoles.add(companyOwnerRole);
                companyOwner.setRoles(newRoles);
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

    @Override
    public List<CompanyOwnerRequestDTO> getCompanyOwnerRequests() {
        List<CompanyOwnerRequestDTO> retVal = new ArrayList<>();
        for (CompanyOwnerRequest request: mongoTemplate.findAll(CompanyOwnerRequest.class)) {
            retVal.add(converterService.requestToDto(request));
        }
        return retVal;
    }

    @Override
    public boolean updateApiToken(UserDTO user) {
        ApplicationUser userToUpdate = mongoTemplate.findById(user.id, ApplicationUser.class);
        if (userToUpdate != null) {
            userToUpdate.setApiToken(user.apiToken);
            mongoTemplate.save(userToUpdate);
            return true;
        }
        return false;
    }

    @Override
    public String generateQUrl(String userId) {
        ApplicationUser user=mongoTemplate.findById(userId, ApplicationUser.class);
        try {
            return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, user.getEmail(), user.getSecret(), APP_NAME), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(ApplicationUser user) {
        mongoTemplate.save(user);
    }

    @Override
    public boolean verifyAcc(String userId, String verificationCode) {
        ApplicationUser user = findById(userId);
        if(user == null) return false;

        if(user.isVerificationCodeNotExpired()){
            if(user.getVerificationCode().equals(verificationCode)){
                user.setVerified(true);
                save(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public ApplicationUser resendVerificationCode(String email) {
        ApplicationUser user = findByEmail(email);
        if(user == null) return null;
        String newVerificationCode = RandomCodeUtil.getCode(32);
        user.setNewVerificationCode(newVerificationCode);
        save(user);

        return user;
    }

    @Override
    public ApplicationUser recoveryPassword(String email) {
        ApplicationUser user = findByEmail(email);
        if(user == null) return null;
        if(!user.isVerified()) return null;
        String recoveryCode = RandomCodeUtil.getCode(8);
        user.setNewRecoveryCode(recoveryCode);
        save(user);

        return user;
    }

    @Override
    public ApplicationUser recoverPassword(RecoveryPasswordDTO recoveryPasswordDTO) {
        if(!recoveryPasswordDTO.passwordMatch()) return null;
        ApplicationUser user = findByEmail(recoveryPasswordDTO.getEmail());
        if(user == null) return null;

        if(user.isRecoveryCodeNotExpired()){
            if(user.getRecoveryPasswordCode().equals(recoveryPasswordDTO.getRecoveryCode())){
                user.setPassword(new BCryptPasswordEncoder().encode(recoveryPasswordDTO.getNewPassword()));
                user.resetNumOfErrTryLogin();
                user.setLocked(false);
                save(user);
            }
        }

        return user;
    }
}
