package com.axway.runners;

import com.axway.runners.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

//    @PostMapping
//
//    public ResponseEntity<?> saveUser(OAuth2AuthenticationToken authToken){
//        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
//        String email = (String) attributes.get("unique_name");
//        String countryCode = (String) attributes.get("ctry");
//        String firstName = (String) attributes.get("given_name");
//        String lastName = (String) attributes.get("family_name");
//
//        User user = new User();
//        user.setCountryCode(countryCode);
//        user.setEmail(email);
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        User existingUser = userService.getUser(email);
//        if(existingUser != null){
//            logger.info("User already exist");
//            return new ResponseEntity<User>(HttpStatus.CONFLICT);
//        }
//
//        userService.save(user);
//        return new ResponseEntity<User>(HttpStatus.CREATED);
//    }

    @GetMapping(value = "/currentuser", produces = "application/json")
    @ResponseBody
    public User getUser(OAuth2AuthenticationToken authToken){
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("unique_name");
        User user = userService.getUser(email);
        if(user == null){

            String countryCode = (String) attributes.get("ctry");
            String firstName = (String) attributes.get("given_name");
            String lastName = (String) attributes.get("family_name");

            user = new User();
            user.setCountryCode(countryCode);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            return userService.save(user);
        }
        return  user;
    }

    @GetMapping( produces = "application/json")
    @ResponseBody
    public Iterable<User> listUser(){
        return userService.findAll();
    }

    @GetMapping("/{id}")

    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }
}
