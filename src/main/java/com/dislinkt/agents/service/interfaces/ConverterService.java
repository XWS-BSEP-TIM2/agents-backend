package com.dislinkt.agents.service.interfaces;

import com.dislinkt.agents.dto.*;
import com.dislinkt.agents.model.*;

public interface ConverterService {

    PostDTO postToDto(Post post);

    UserDTO userToDto(ApplicationUser user);

    CompanyDTO companyToDto(Company company);

    JobOfferDTO jobOfferToDto(JobOffer jobOffer);

    JobOfferCommentDTO commentToDto(JobOfferComment comment);

}
