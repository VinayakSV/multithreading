package com.vcomp.se.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.vcomp.se.entity.User;
import com.vcomp.se.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
    
    Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Async
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws Exception{

        long start = System.currentTimeMillis();
        //logger.info();
        List<User> users = parseCSV(file);

        logger.info("saving list of users of size {}" +  users.size() +  " " + Thread.currentThread().getName() );
        userRepository.saveAll(users);
        long end = System.currentTimeMillis();
        logger.info("Total time taken is: " + (end-start));

        return CompletableFuture.completedFuture(users);
    }


    @Async
    public CompletableFuture<List<User>> fetchAllUser(){
        logger.info("get list of user by " + Thread.currentThread().getName());
        List<User> users = userRepository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    public List<User> parseCSV(MultipartFile file) throws Exception{

        final List<User> users = new ArrayList<>();
        try{
            try(final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){

                String line;
                while((line = br.readLine()) != null){
                    final String[] data = line.split(",");
                    final User user = new User();
                    user.setName(data[0]);
                    user.setEmail(data[1]);
                    user.setGender(data[2]); 

                    users.add(user);
                }
            }
        }catch(final IOException e){
            logger.info("Failed to parse csv file {} ", e);
            throw new Exception("Failed to parse csv file {} ", e);
        }
        return users;

    }
}
