package com.dislinkt.agents.service;

import com.dislinkt.agents.dto.JobOfferDTO;
import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.exceptions.GenericException;
import com.dislinkt.agents.service.interfaces.ValidationService;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    public void validateUser(UserDTO dto) {
        validateMail(dto.email);
        validatePassword(dto.password);
        validateName(dto.getName());
        validateName(dto.getSurname());
    }




    private void validateMail(String mail) {
        if (!mail.matches("\\S+@\\S+\\.\\S+")) {
            throw new GenericException("Wrong mail format");
        }

    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new GenericException("Password must contain at least 8 characters");
        }
        if (!password.matches("(.*[a-z].*)")) {
            throw new GenericException("Password must contain small letters");
        }
        if (!password.matches("(.*[A-Z].*)")) {
            throw new GenericException("Password must contain capital letters");
        }
        if (!password.matches("(.*[0-9].*)")) {
            throw new GenericException("Password must contain numbers");
        }
        if (!password.matches("(.*[!@#$%^&*(){}\\[:;\\]<>,\\.?~_+\\-\\\\=|/].*)")) {
            throw new GenericException("Password must contain special characters");
        }
    }

    private void validateName(String name) {
        if (!name.matches("[A-Za-z ,.'-]+")) {
            throw new GenericException("Input is not valid");
        }
    }
}
