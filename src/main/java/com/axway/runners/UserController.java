package com.axway.runners;

import com.axway.runners.model.User;
import com.axway.runners.service.UserService;
import com.axway.runners.strava.OAuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

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
        logger.debug("Azure attributes : {}", attributes);
        String email = (String) attributes.get("email");
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
        OAuthToken oAuthToken = user.getOAuthToken();
        if( oAuthToken != null) {
            oAuthToken.setAccess_token("");
            oAuthToken.setRefresh_token("");
        }
        return  user;
    }

//    @GetMapping( produces = "application/json")
//    @ResponseBody
//    public Iterable<User> listUser(){
//
//        return userService.findAll();
//    }

    @GetMapping("/{id}")

    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }
}
