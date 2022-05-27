package com.dislinkt.agents.converter;

import com.dislinkt.agents.dto.UserDTO;
import com.dislinkt.agents.model.ApplicationUser;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class UserToDTO implements Converter<ApplicationUser, UserDTO>  {
    @Override
    public UserDTO convert(ApplicationUser source) {
        UserDTO dto = new UserDTO();
        dto.setEmail(source.getEmail());
        dto.setId(source.getId());
        dto.setName(source.getName());
        dto.setSurname(source.getSurname());
        dto.setRole(source.getRole());
        return dto;
    }
}
