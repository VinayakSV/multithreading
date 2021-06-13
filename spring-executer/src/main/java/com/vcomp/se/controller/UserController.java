package com.vcomp.se.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.vcomp.se.entity.User;
import com.vcomp.se.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UserController {
    
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(value = "/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception{

        for(MultipartFile file : files){
            userService.saveUsers(file);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    } 

    @GetMapping(value = "/users", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers(){
       return userService.fetchAllUser().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/usersbymlt", produces = "application/json")
    public ResponseEntity getUsers(){
        CompletableFuture<List<User>> users1 = userService.fetchAllUser(); 
        CompletableFuture<List<User>> users2 = userService.fetchAllUser(); 
        CompletableFuture<List<User>> users3 = userService.fetchAllUser(); 

        CompletableFuture.allOf(users1, users2, users3).join();

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
