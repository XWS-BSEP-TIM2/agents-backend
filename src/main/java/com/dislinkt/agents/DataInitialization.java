package com.dislinkt.agents;

import com.dislinkt.agents.model.*;
import com.dislinkt.agents.model.enums.ApplicationUserRole;
import com.dislinkt.agents.model.enums.PostType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitialization {

    @Autowired
    private MongoTemplate mongoTemplate;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        try {
            mongoTemplate.dropCollection(ApplicationUser.class);
            mongoTemplate.dropCollection(Company.class);
            mongoTemplate.dropCollection(JobOffer.class);
            mongoTemplate.dropCollection(JobOfferComment.class);
            mongoTemplate.dropCollection(Post.class);
            mongoTemplate.dropCollection(CompanyOwnerRequest.class);
        } catch (Exception e) {

        }

        // USERS
        String simplePassword = "$2a$12$eWlLSJGnjzzaAUOevPWpBetvfgbQOZliAEe4iQM1kSPNGby3V8Dhu";
        Permission permission1=new Permission(82272036854775810L,"POST_NEW_OFFER");
        Permission permission2=new Permission(82272036854775811L,"UPDATE_OFFER");
        Permission permission3=new Permission(82272036854775812L,"POST_OFFER_COMMENT");
        Permission permission4=new Permission(82272036854775813L,"DELETE_OFFER");
        Permission permission5=new Permission(82272036854775814L,"SEND_COMPANY_OWNER_REQUEST");
        Permission permission6=new Permission(82272036854775815L,"PROCESS_COMPANY_OWNER_REQUESTS");
        Permission permission7=new Permission(82272036854775816L,"UPDATE_API_TOKEN");

        mongoTemplate.save(permission1);
        mongoTemplate.save(permission2);
        mongoTemplate.save(permission3);
        mongoTemplate.save(permission4);
        mongoTemplate.save(permission5);
        mongoTemplate.save(permission6);
        mongoTemplate.save(permission7);

        Set<Permission> adminPermissions= new HashSet<>();
        Set<Permission> userPermissions= new HashSet<>();
        Set<Permission> companyOwnerPermissions= new HashSet<>();
        companyOwnerPermissions.add(permission1);
        companyOwnerPermissions.add(permission2);
        companyOwnerPermissions.add(permission4);
        companyOwnerPermissions.add(permission7);
        userPermissions.add(permission3);
        userPermissions.add(permission5);
        adminPermissions.add(permission6);


        Role role1= new Role(92272036854775807L,"ADMIN",adminPermissions);
        Role role2= new Role(92272036854775808L,"USER",userPermissions);
        Role role3= new Role(92272036854775809L,"COMPANY_OWNER", companyOwnerPermissions);

        mongoTemplate.save(role1);
        mongoTemplate.save(role2);
        mongoTemplate.save(role3);

        List<Role> adminRoles=new ArrayList<>();
        List<Role> userRoles=new ArrayList<>();
        List<Role> companyOwnerRoles=new ArrayList<>();
        adminRoles.add(role1);
        userRoles.add(role2);
        companyOwnerRoles.add(role3);

        ApplicationUser user1 = new ApplicationUser("62933e04552855741fcb6752", "Jack", "Smith", "djordje1499@gmail.com", simplePassword, "", userRoles,"CUW732US7Y7CPRB3",true, false, true);
        ApplicationUser user2 = new ApplicationUser("62933e04552855741fcb6753", "Alvin", "Ellison", "dislinktx+alvin@gmail.com", simplePassword, "", userRoles,"",false, false, true);
        ApplicationUser user3 = new ApplicationUser("62933e04552855741fcb6754", "Eadie", "Martins", "dislinktx+eadie@gmail.com", simplePassword, "", companyOwnerRoles,"",false, false, true);
        ApplicationUser user4 = new ApplicationUser("62933e04552855741fcb6755", "India", "Olsen", "dislinktx+india@gmail.com", simplePassword, "", companyOwnerRoles,"",false, false, false);
        ApplicationUser user5 = new ApplicationUser("62933e04552855741fcb6756", "Saskia", "Rees", "dislinktx+saskia@gmail.com", simplePassword, "", companyOwnerRoles,"",false, false, true);
        ApplicationUser user6 = new ApplicationUser("62933e04552855741fcb6757", "Emmett", "Lutz", "dislinktx+emmet@gmail.com", simplePassword, "adadqbek123krmtgk123e1rff", companyOwnerRoles,"",false, false, true);
        ApplicationUser user7 = new ApplicationUser("62933e04552855741fcb6758", "Maddie", "Gray", "dislinktx+maddie@gmail.com", simplePassword, "adadqbek123krmtgk123e1rfd", companyOwnerRoles,"",false, false, true);
        ApplicationUser user8 = new ApplicationUser("62933e04552855741fcb6759", "Tara", "Pogancev", "dislinktx+tara@gmail.com", simplePassword, "", adminRoles,"",false, false, true);

        user1 = mongoTemplate.save(user1);
        user2 = mongoTemplate.save(user2);
        user3 = mongoTemplate.save(user3);
        user4 = mongoTemplate.save(user4);
        user5 = mongoTemplate.save(user5);
        user6 = mongoTemplate.save(user6);
        user7 = mongoTemplate.save(user7);
        user8 = mongoTemplate.save(user8);

        // COMPANIES
        Company company1 = new Company(null, user3.getId(), "Microsoft", "Microsoft Corporation is an American multinational technology corporation which produces computer software, consumer electronics, personal computers, and related services.",
                "Window to the future.", new ArrayList<String>(){
            {
                add("C#");
                add("C++");
                add("JavaScript");
                add("Rust");
                add("Bootstrap");
            }
        }, new ArrayList<String>(){
            {
                add("careers@microsoft.com");
                add("hr@microsoft.com");
            }
        }, new ArrayList<String>(){
            {
                add("507-855-1711");
                add("419-536-2208");
            }
        }, true);

        Company company2 = new Company(null, user4.getId(), "Levi9", "Do you want to learn, be challenged and make an impact with technology? View our jobs. Levi9 offers great career opportunities & a lot of innovation. View our jobs and join us!",
                "Nobody makes impact alone.", new ArrayList<String>(){
            {
                add("Java");
                add("Kafka");
                add("Spring Boot");
                add("Sass");
            }
        }, new ArrayList<String>(){
            {
                add("careers@levi9.com");
                add("hr@levi9.com");
            }
        }, new ArrayList<String>(){
            {
                add("321-265-1583");
                add("412-229-5315");
            }
        }, true);

        Company company3 = new Company(null, user5.getId(), "Apple", "Apple Inc. is an American multinational technology company that specializes in consumer electronics, software and online services headquartered in Cupertino, California, United States.",
                "Discover the innovative world of Apple.", new ArrayList<String>(){
            {
                add("Tensorflow");
                add("C++");
                add("Java");
                add("Kotlin");
                add("Mongo DB");
            }
        }, new ArrayList<String>(){
            {
                add("careers@apple.com");
                add("hr@apple.com");
            }
        }, new ArrayList<String>(){
            {
                add("740-917-3275");
                add("610-912-3139");
            }
        }, true);

        Company company4 = new Company(null, user6.getId(), "Amazon", "Amazon.com, Inc. is an American multinational technology company which focuses on e-commerce, cloud computing, digital streaming, and artificial intelligence.",
                "Now with free shipping on qualifying orders over $49USD", new ArrayList<String>(){
            {
                add("C#");
                add("Java");
                add("GCP");
                add("Jenkins");
                add("Angular");
            }
        }, new ArrayList<String>(){
            {
                add("careers@amazon.com");
                add("hr@amazon.com");
            }
        }, new ArrayList<String>(){
            {
                add("605-690-4224");
                add("919-497-6907");
            }
        }, true);

        Company company5 = new Company(null, user7.getId(), "AMD Radeon Software", "AMD Radeon Software is a device driver and utility software package for Advanced Micro Devices's graphics cards and APUs.",
                "Engineered for Gamers.", new ArrayList<String>(){
            {
                add("C#");
                add("C++");
                add("Tailwind");
                add("Angular");
                add("PostgreSQL");
            }
        }, new ArrayList<String>(){
            {
                add("careers@radeon.com");
                add("hr@radeon.com");
            }
        }, new ArrayList<String>(){
            {
                add("939-349-6271");
                add("229-623-3508");
            }
        }, true);

        company1 = mongoTemplate.save(company1);
        company2 = mongoTemplate.save(company2);
        company3 = mongoTemplate.save(company3);
        company4 = mongoTemplate.save(company4);
        company5 = mongoTemplate.save(company5);

        // JOB OFFERS
        JobOffer offer1 = new JobOffer(null, company1.getId(), company1.getUserId(),".NET Developer", "Medior", "Join the team of Schneider Electric Software Engineers delivering customized solutions for utility companies around the world. We are helping utilities to modernize their management of distribution systems. The meaningful and leading software solution is something that takes us from “good” to “great”. Discover the opportunity to join a dynamic, inspiring, and responsible team that fosters the development of its people.",
                new ArrayList<String>(){
                    {
                        add("C#");
                        add("C++");
                        add(".NET");
                    }
                },false);

        JobOffer offer2 = new JobOffer(null, company2.getId(), company2.getUserId(),"Frontend Software Engineer", "Junior", "Software Developers at IBM are the backbone of our strategic initiatives to design, code, test, and provide industry-leading solutions that make the world run today - planes and trains take off on time.",
                new ArrayList<String>(){
                    {
                        add("iOS");
                        add("Angular");
                        add("React");
                    }
                },false);

        JobOffer offer3 = new JobOffer(null, company3.getId(), company3.getUserId(), "Mobile Developer", "Junior", "On the Front-End side, you will work on a React based UI which is tuned for high performance using a modern reactive approach. At Instana, we process and analyze millions of spans, traces.",
                new ArrayList<String>(){
                    {
                        add("Swift");
                        add("Cypress");
                        add("Kotlin");
                    }
                },false);

        JobOffer offer4 = new JobOffer(null, company4.getId(), company4.getUserId(),"UI/UX Designer", "Senior", "Spiralyze is a data-driven A/B testing company headquartered in the United States with clients from major brands such as Pepsi, Netflix, General Electric, Abbott, American Express, Dream Host, TechSmith, VWO, and many more. We use design and optimization to help our clients generate more revenue from their website traffic.",
                new ArrayList<String>(){
                    {
                        add("Jenkins");
                        add("Docker");
                    }
                },false);

        JobOffer offer5 = new JobOffer(null, company5.getId(), company5.getUserId(),"Full-stack Engineer", "Junior", "The bulk of the work we do every day is a mix of Back-end work in Ruby and Front-end work in React. If you have back-end skills and if you are comfortable getting your hands dirty in the front-end as well, this is the right opportunity for you.",
                new ArrayList<String>(){
                    {
                        add("Java");
                        add("C++");
                        add("Jest");
                    }
                },false);

        offer1 = mongoTemplate.save(offer1);
        offer2 = mongoTemplate.save(offer2);
        offer3 = mongoTemplate.save(offer3);
        offer4 = mongoTemplate.save(offer4);
        offer5 = mongoTemplate.save(offer5);

        // JOB OFFER COMMENTS
        JobOfferComment comment1 = new JobOfferComment(null, user1.getId(), offer1.getId(), 5, 890.0, "Maecenas vehicula purus nec sagittis egestas. Donec iaculis sagittis pharetra. Fusce eget erat id mi placerat auctor. Nunc eget metus sed dolor vulputate elementum nec non sem. Nulla eleifend elementum mauris a aliquam. Sed cursus lorem sapien, et dapibus nunc pharetra quis. ");
        JobOfferComment comment2 = new JobOfferComment(null, user2.getId(), offer1.getId(), 4, 780.0, "Curabitur at rutrum sapien, id cursus nunc. Aliquam vehicula at lacus nec dignissim. Sed et orci dictum, bibendum mi ut, iaculis justo. Etiam quam sapien, mollis quis rhoncus at, varius ut elit.");
        JobOfferComment comment3 = new JobOfferComment(null, user1.getId(), offer2.getId(), 5, 890.0, "Praesent commodo pellentesque hendrerit. Nunc cursus orci ut gravida ullamcorper. Maecenas ut ultricies quam. Cras in velit diam. Sed posuere dui ut felis ultricies, nec pellentesque lorem porttitor. Integer in sem diam. Aliquam erat volutpat. Vestibulum condimentum massa rutrum ex auctor, et vulputate enim egestas.");
        JobOfferComment comment4 = new JobOfferComment(null, user1.getId(), offer3.getId(), 4, 2600.0, "Quisque viverra gravida libero ut bibendum. Sed ac varius lorem. Sed ac dictum ligula. Maecenas eleifend sem dolor, vel ullamcorper metus consectetur nec. Ut pulvinar ultricies erat in semper.");
        JobOfferComment comment5 = new JobOfferComment(null, user1.getId(), offer5.getId(), 3, 1650.0, "Maecenas dignissim nulla ac erat ultricies, sed dignissim enim posuere. Pellentesque ac metus id diam mattis interdum imperdiet eu sem. Praesent quis pellentesque risus. Nulla facilisi. Morbi leo neque, pretium dapibus nibh ullamcorper, sagittis aliquam neque.");
        JobOfferComment comment6 = new JobOfferComment(null, user2.getId(), offer3.getId(), 5, 1200.0, "Fusce nec leo in leo varius feugiat sed at urna. In consectetur lorem vitae efficitur tristique. Cras vestibulum nulla ac sem laoreet malesuada.");

        comment1 = mongoTemplate.save(comment1);
        comment2 = mongoTemplate.save(comment2);
        comment3 = mongoTemplate.save(comment3);
        comment4 = mongoTemplate.save(comment4);
        comment5 = mongoTemplate.save(comment5);
        comment6 = mongoTemplate.save(comment6);

        // POSTS
        Post post1 = new Post();
        post1.setUserId(user1.getId());
        post1.setPostType(PostType.NEW_USER);
        mongoTemplate.save(post1);

        Post post2 = new Post();
        post2.setUserId(user2.getId());
        post2.setPostType(PostType.NEW_USER);
        mongoTemplate.save(post2);

        Post post3 = new Post();
        post3.setUserId(user3.getId());
        post3.setPostType(PostType.NEW_USER);
        mongoTemplate.save(post3);

        Post post4 = new Post();
        post4.setUserId(user4.getId());
        post4.setPostType(PostType.NEW_USER);
        mongoTemplate.save(post4);

        Post post5 = new Post();
        post5.setUserId(user5.getId());
        post5.setPostType(PostType.NEW_USER);
        mongoTemplate.save(post5);

        Post post6 = new Post();
        post6.setUserId(user6.getId());
        post6.setPostType(PostType.NEW_USER);
        mongoTemplate.save(post6);

        Post post7 = new Post();
        post7.setUserId(user7.getId());
        post7.setPostType(PostType.NEW_USER);
        mongoTemplate.save(post7);

        Post companyPost1 = new Post();
        companyPost1.setPostType(PostType.NEW_COMPANY);
        companyPost1.setCompanyId(company1.getId());
        mongoTemplate.save(companyPost1);

        Post companyPost2 = new Post();
        companyPost2.setPostType(PostType.NEW_COMPANY);
        companyPost2.setCompanyId(company2.getId());
        mongoTemplate.save(companyPost2);

        Post companyPost3 = new Post();
        companyPost3.setPostType(PostType.NEW_COMPANY);
        companyPost3.setCompanyId(company3.getId());
        mongoTemplate.save(companyPost3);

        Post companyPost4 = new Post();
        companyPost4.setPostType(PostType.NEW_COMPANY);
        companyPost4.setCompanyId(company4.getId());
        mongoTemplate.save(companyPost4);

        Post companyPost5 = new Post();
        companyPost5.setPostType(PostType.NEW_COMPANY);
        companyPost5.setCompanyId(company5.getId());
        mongoTemplate.save(companyPost5);

        Post offerPost1 = new Post();
        offerPost1.setPostType(PostType.NEW_JOB_OFFER);
        offerPost1.setJobOfferId(offer1.getId());
        mongoTemplate.save(offerPost1);

        Post offerPost2 = new Post();
        offerPost2.setPostType(PostType.NEW_JOB_OFFER);
        offerPost2.setJobOfferId(offer2.getId());
        mongoTemplate.save(offerPost2);

        Post offerPost3 = new Post();
        offerPost3.setPostType(PostType.NEW_JOB_OFFER);
        offerPost3.setJobOfferId(offer3.getId());
        mongoTemplate.save(offerPost3);

        Post offerPost4 = new Post();
        offerPost4.setPostType(PostType.NEW_JOB_OFFER);
        offerPost4.setJobOfferId(offer4.getId());
        mongoTemplate.save(offerPost4);

        Post offerPost5 = new Post();
        offerPost5.setPostType(PostType.NEW_JOB_OFFER);
        offerPost5.setJobOfferId(offer5.getId());
        mongoTemplate.save(offerPost5);

        System.out.println("Data initialization complete.");
    }
}
