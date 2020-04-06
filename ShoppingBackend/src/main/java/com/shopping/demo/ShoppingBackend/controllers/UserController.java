package com.shopping.demo.ShoppingBackend.controllers;

import com.shopping.demo.ShoppingBackend.AuthenticationService;
import com.shopping.demo.ShoppingBackend.data.rest.RESTUser;
import com.shopping.demo.ShoppingBackend.data.db.User;
import com.shopping.demo.ShoppingBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    /**
     * Creates a new user object using the username and password of the incoming user.
     * We hash the incoming user's password and save that hash.
     */
    @PostMapping("/users/register")
    public void addUser(@RequestBody RESTUser restUser,
                        HttpServletRequest request, HttpServletResponse response) {
        if (userRepository.findByUserName(restUser.userName) != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username already exists"
            );
        }

        User user = createUserAndAddAuthHeader(restUser, response);
        userRepository.save(user);

    }
    @PostMapping("/users/login")
    public void signIn(@RequestBody RESTUser restUser,
                        HttpServletRequest request, HttpServletResponse response) {

        if (userRepository.findByUserName(restUser.userName) == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username not found"
            );
        }
        User user = authenticationService.authenticateUserFromPassword(restUser);
        AuthenticationService.addAuthenticatedUserHeader(user, response);
    }

    private User createUserAndAddAuthHeader(@RequestBody RESTUser restUser, HttpServletResponse response) {
        User user;
        try {
            user = AuthenticationService.createUserFromRestUser(restUser);
            AuthenticationService.addAuthenticatedUserHeader(user, response);
        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Problem with saving user"
            );
        }
        return user;
    }

}
