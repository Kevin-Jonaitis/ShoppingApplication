package com.shopping.demo.ShoppingBackend;

import com.shopping.demo.ShoppingBackend.data.rest.RESTUser;
import com.shopping.demo.ShoppingBackend.data.db.User;
import com.shopping.demo.ShoppingBackend.repository.UserRepository;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import sun.jvm.hotspot.memory.HeapBlock;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Various methods used for authenticating and verifying users.
 */
@Service
public class AuthenticationService {

    public static final String AUTH_TOKEN = "AuthToken";

    @Autowired
    UserRepository userRepository;

    /**
     * Generates a random, secure salt
     */
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * Takes a password, takes a salt, and hashes them both
     */
    public static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        byte[] passwordBytes = password.getBytes();
        byte[] combined = new byte[salt.length + passwordBytes.length];

        //Combine the two arrays
        for (int i = 0; i < combined.length; ++i) {
            combined[i] = i < salt.length ? salt[i] : passwordBytes[i - salt.length];
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(combined);
    }


    public static User createUserFromRestUser(RESTUser restUser) throws NoSuchAlgorithmException {
        byte[] salt = AuthenticationService.generateSalt();
        byte[] passwordHash = hashPassword(restUser.password, salt);
        return new User(restUser.userName, passwordHash, salt);
    }

    public static void addAuthenticatedUserHeader(User user, HttpServletResponse response) {
        response.addHeader(AUTH_TOKEN, user.userName + ":" +
                convertBytesToHex(user.passwordHash));
    }

    /**
     * This method takes in the incoming request, parses the authentication cookie from it,
     * and verifies that the user in the cookie matches the user stored in our DB(including the password)
     */
    public User authenticateAndGetUserFromAuthToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader(AUTH_TOKEN);
            if (authHeader == null) {
                return null;
            }

            String[] userAndPasswordHash = authHeader.split(":");
            String userName = userAndPasswordHash[0];
            String passwordHash = userAndPasswordHash[1];

            User user = userRepository.findByUserName(userName);
            if (user != null && Arrays.equals(user.passwordHash, covertHexToBytes(passwordHash))) {
                return user;
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to parse user from cookie", e);
        }
        return null;
    }

    public User authenticateUserFromPassword(RESTUser user) {
        User lookedUpUser = userRepository.findByUserName(user.userName);
        try {
            if (lookedUpUser != null && Arrays.equals(hashPassword(user.password, lookedUpUser.salt), lookedUpUser.passwordHash)) {
                return lookedUpUser;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String convertBytesToHex(byte[] bytes) {
        char[] chars = Hex.encodeHex(bytes);
        return new String(chars);
    }

    private static byte[] covertHexToBytes(String hex) throws DecoderException {
        return Hex.decodeHex(hex.toCharArray());
    }

}
