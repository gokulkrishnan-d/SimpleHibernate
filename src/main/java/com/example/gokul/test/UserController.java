package com.example.gokul.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getuserById(@PathVariable("id") int id){
        System.out.println("Fetching user id " + id);
        User user = userService.findById(id);
        if (user == null) {
            System.out.println("It is null");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        System.out.println("not null");
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping(value="/create",headers="Accept=application/json")
    public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder){
        System.out.println("Creating User "+user.getName());
        System.out.println("Creating User "+ ucBuilder.toString());
        userService.createUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value="/update", headers="Accept=application/json")
    public ResponseEntity<String> updateUser(@RequestBody User currentUser)
    {
        System.out.println("sd");
        User user = userService.findById(currentUser.getId());
        if (user==null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        userService.update(currentUser, currentUser.getId());
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @DeleteMapping(value="/{id}", headers ="Accept=application/json")
    public ResponseEntity<User> deleteUser(@PathVariable("id") int id){
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        userService.deleteUserById(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value="/{id}", headers="Accept=application/json")
    public ResponseEntity<User> updateUserPartially(@PathVariable("id") int id, @RequestBody User currentUser){
        User user = userService.findById(id);
        if(user ==null){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        User usr = userService.updatePartially(currentUser, id);
        return new ResponseEntity<User>(usr, HttpStatus.OK);
    }
}
