package com.dislinkt.agents.service;

import com.dislinkt.agents.dto.CreateJobOfferOnDislinktDto;
import com.dislinkt.agents.dto.JobOfferCommentDTO;
import com.dislinkt.agents.dto.JobOfferDTO;
import com.dislinkt.agents.model.ApplicationUser;
import com.dislinkt.agents.model.JobOffer;
import com.dislinkt.agents.model.JobOfferComment;
import com.dislinkt.agents.model.Post;
import com.dislinkt.agents.model.enums.PostType;
import com.dislinkt.agents.security.JwtUtil;
import com.dislinkt.agents.service.interfaces.ConverterService;
import com.dislinkt.agents.service.interfaces.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobOfferServiceImpl implements JobOfferService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ConverterService converterService;

    private final JwtUtil jwtUtil;

    private final String dislinktUrl = "https://localhost:9000";

    @Override
    public List<JobOfferDTO> findAllDTO() {
        List<JobOfferDTO> retVal = new ArrayList<>();
        for (JobOffer jobOffer : mongoTemplate.findAll(JobOffer.class)) {
            retVal.add(converterService.jobOfferToDto(jobOffer));
        }
        return retVal;
    }

    @Override
    public JobOfferDTO findByIdDTO(String offerId) {
        JobOffer jobOffer = mongoTemplate.findById(offerId, JobOffer.class);
        if (jobOffer != null) {
            return converterService.jobOfferToDto(jobOffer);
        } else {
            return null;
        }
    }

    @Override
    public List<JobOfferCommentDTO> findCommentsByIdDTO(String offerId) {
        List<JobOfferCommentDTO> retVal = new ArrayList<>();
        for (JobOfferComment comment : mongoTemplate.findAll(JobOfferComment.class)) {
            if (comment.getJobOfferId().equals(offerId)) {
                retVal.add(converterService.commentToDto(comment));
            }
        }
        return retVal;
    }

    @Override
    public JobOffer postNewOffer(JobOfferDTO jobOffer) {
        JobOffer newOffer = new JobOffer();
        newOffer.setCompanyId(jobOffer.company.id);
        newOffer.setDescription(jobOffer.getDescription());
        newOffer.setPosition(jobOffer.getPosition());
        newOffer.setTechnologies(jobOffer.getTechnologies());
        newOffer.setSeniority(jobOffer.getSeniority());
        newOffer.setPosition(jobOffer.getPosition());
        newOffer.setUserId(jobOffer.company.user.id);
        newOffer.setPromotedOnDislinkt(jobOffer.isPublishToDislinkt());
        newOffer = mongoTemplate.save(newOffer);

        Post post = new Post();
        post.setJobOfferId(newOffer.getId());
        post.setPostType(PostType.NEW_JOB_OFFER);
        mongoTemplate.save(post);
        if (jobOffer.isPublishToDislinkt()) {
            promoteOnDislinkt(jobOffer, newOffer.getId());
        }

        return newOffer;
    }

    private void promoteOnDislinkt(JobOfferDTO jobOffer, String offerId) {
        HttpComponentsClientHttpRequestFactory requestFactory = prepareRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String url = dislinktUrl + "/api/job-offer";
        ApplicationUser user = mongoTemplate.findById(jobOffer.getCompany().getUser().getId(), ApplicationUser.class);
        HttpHeaders headers = new HttpHeaders();
        CreateJobOfferOnDislinktDto dto = new CreateJobOfferOnDislinktDto();
        dto.setApiToken(user.apiToken);
        dto.setCompanyName(jobOffer.company.name);
        dto.setTechnologies(jobOffer.getTechnologies());
        dto.setSeniority(jobOffer.getSeniority());
        dto.setPosition(jobOffer.getPosition());
        dto.setDescription(jobOffer.getDescription());
        dto.setId(offerId);
        headers.add("Authorization", "Bearer " + jwtUtil.generateApiToken("API", user.apiToken));
        final HttpEntity<CreateJobOfferOnDislinktDto> entity = new HttpEntity<CreateJobOfferOnDislinktDto>(dto, headers);
        ResponseEntity<String> ret = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(ret.getStatusCode());
        System.out.println(ret);
    }

    private HttpComponentsClientHttpRequestFactory prepareRequestFactory() {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        return requestFactory;
    }

    @Override
    public JobOfferComment postNewComment(JobOfferCommentDTO comment) {
        JobOfferComment jobOfferComment = new JobOfferComment();
        jobOfferComment.setComment(comment.getComment());
        jobOfferComment.setJobOfferId(comment.getJobOffer().id);
        jobOfferComment.setRating(comment.getRating());
        jobOfferComment.setSalary(comment.getSalary());
        jobOfferComment.setUserId(comment.getUser().id);
        return mongoTemplate.save(jobOfferComment);
    }

    @Override
    public JobOffer updateOffer(JobOfferDTO jobOffer) {
        JobOffer updatedOffer = mongoTemplate.findById(jobOffer.id, JobOffer.class);
        if (updatedOffer != null) {
            updatedOffer.setDescription(jobOffer.getDescription());
            updatedOffer.setPosition(jobOffer.getPosition());
            updatedOffer.setTechnologies(jobOffer.getTechnologies());
            updatedOffer.setSeniority(jobOffer.getSeniority());
            updatedOffer.setPosition(jobOffer.getPosition());
            updatedOffer = mongoTemplate.save(updatedOffer);
        }
        if (updatedOffer.isPromotedOnDislinkt()){
            updateOfferOnDislinkt(jobOffer);
        }

        return updatedOffer;
    }

    private void updateOfferOnDislinkt(JobOfferDTO jobOffer){
        HttpComponentsClientHttpRequestFactory requestFactory = prepareRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String url = dislinktUrl + "/api/job-offer";
        ApplicationUser user = mongoTemplate.findById(jobOffer.getCompany().getUser().getId(), ApplicationUser.class);
        HttpHeaders headers = new HttpHeaders();
        CreateJobOfferOnDislinktDto dto = new CreateJobOfferOnDislinktDto();
        dto.setApiToken(user.apiToken);
        dto.setCompanyName(jobOffer.company.name);
        dto.setTechnologies(jobOffer.getTechnologies());
        dto.setSeniority(jobOffer.getSeniority());
        dto.setPosition(jobOffer.getPosition());
        dto.setDescription(jobOffer.getDescription());
        dto.setId(jobOffer.id);
        headers.add("Authorization", "Bearer " + jwtUtil.generateApiToken("API", user.apiToken));
        final HttpEntity<CreateJobOfferOnDislinktDto> entity = new HttpEntity<CreateJobOfferOnDislinktDto>(dto, headers);
        ResponseEntity<String> ret = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        System.out.println(ret.getStatusCode());
        System.out.println(ret);
    }

    @Override
    public boolean deleteOffer(JobOfferDTO jobOffer) {
        JobOffer offer = mongoTemplate.findById(jobOffer.id, JobOffer.class);
        if (offer != null) {
            for (Post post : mongoTemplate.findAll(Post.class)) {
                if (post.getPostType() == PostType.NEW_JOB_OFFER) {
                    if (post.getJobOfferId().equals(offer.getId())) {
                        mongoTemplate.remove(post);
                    }
                }
            }

            for (JobOfferComment comment : mongoTemplate.findAll(JobOfferComment.class)) {
                if (comment.getJobOfferId().equals(offer.getId())) {
                    mongoTemplate.remove(comment);
                }
            }

            mongoTemplate.remove(offer);
            if (offer.isPromotedOnDislinkt()){
                removeFromDislinkt(jobOffer);
            }
            return true;
        } else {
            return false;
        }
    }

    private void removeFromDislinkt(JobOfferDTO jobOffer) {
        HttpComponentsClientHttpRequestFactory requestFactory = prepareRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String url = dislinktUrl + "/api/job-offer/"+jobOffer.id;
        ApplicationUser user = mongoTemplate.findById(jobOffer.getCompany().getUser().getId(), ApplicationUser.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtUtil.generateApiToken("API", user.apiToken));
        final HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> ret = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
        System.out.println(ret.getStatusCode());
        System.out.println(ret);
    }

    @Override
    public List<JobOfferDTO> findAllByCompanyIdDTO(String companyId) {
        List<JobOfferDTO> retVal = new ArrayList<>();
        for (JobOfferDTO dto : findAllDTO()) {
            if (dto.company.id.equals(companyId)) {
                retVal.add(dto);
            }
        }
        return retVal;
    }


}
