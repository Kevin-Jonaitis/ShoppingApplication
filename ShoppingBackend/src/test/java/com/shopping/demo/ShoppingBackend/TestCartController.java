package com.shopping.demo.ShoppingBackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.demo.ShoppingBackend.controllers.CartController;
import com.shopping.demo.ShoppingBackend.data.db.Cart;
import com.shopping.demo.ShoppingBackend.data.db.Item;
import com.shopping.demo.ShoppingBackend.data.db.User;
import com.shopping.demo.ShoppingBackend.repository.CartRepository;
import com.shopping.demo.ShoppingBackend.repository.ItemRepository;
import com.shopping.demo.ShoppingBackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestBody;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Here are what some sample tests would look like. This tests part of the Cart controller.
 *
 */
@WebMvcTest(CartController.class)
//@ContextConfiguration(classes = {AuthenticationService.class})
public class TestCartController {

    @Autowired private MockMvc mockMvc;
    @MockBean private CartRepository cartRepository;
    @MockBean private UserRepository userRepository;
    @MockBean private AuthenticationService authenticationService;
    @MockBean private ItemRepository itemRepository;

    @Test
    public void return400IfNoUserIsFoundWhenFetchingCart() throws Exception {
        // Setup mock
        when(cartRepository.findByUserName(anyString())).thenReturn(null);

        //Run test
        this.mockMvc.perform(get("/cart")
                .header("authtoken", "someuser:someHashString")
                .header("Origin", "testWorld")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(status().reason(containsString("Username or password incorrect")));
    }

    /**
     * Verify that when we a new cart list, it updates the cart with the new item  and item count
     * and saves it to the DB
     */
    @Test
    public void updateCartHappyCase() throws Exception {
        final int NUMBER_OF_ITEMS_TO_ADD = 2;
        final String PRODUCT_ID = "123";
        Map<String, Integer> productIdToCount  = new HashMap<>();
        productIdToCount.put(PRODUCT_ID, NUMBER_OF_ITEMS_TO_ADD);
        Item item = new Item("123", "name", "description", 123L, "sdfs");
        List<String> ids = new ArrayList<>();
        ids.add("123");
        List<Item> items = new ArrayList<>();
        items.add(item);

        Cart cart = setupMocks(ids, items);


        //Run test
        this.mockMvc.perform(put("/cart")
                .header("Origin", "testWorld")
                .content(asJsonString(productIdToCount))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        ArgumentCaptor<Cart> capturedCartSave = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository).save(capturedCartSave.capture());

        // Verify we actually saved to the DB
        assertThat(capturedCartSave.getValue()).isEqualTo(cart);
        assertThat(capturedCartSave.getValue().itemList.get(0)).isEqualTo(item);
        assertThat(capturedCartSave.getValue().itemCount.get(0)).isEqualTo(2);

    }

    /**
     * When we pass in a product count of 0, we should delete that entry completely from the db list
     */
    @Test
    public void verifyPassingInZeroProductCountDeletesFromDB() throws Exception {
        final int NUMBER_OF_ITEMS_TO_ADD = 0;
        final String PRODUCT_ID = "123";

        Map<String, Integer> productIdToCount  = new HashMap<>();
        productIdToCount.put(PRODUCT_ID, NUMBER_OF_ITEMS_TO_ADD);

        Item item = new Item("123", "name", "description", 123L, "sdfs");
        List<String> ids = new ArrayList<>();
        ids.add("123");
        List<Item> items = new ArrayList<>();
        items.add(item);
        Cart cart = setupMocks(ids, items);

        //Run test
        this.mockMvc.perform(put("/cart")
                .header("Origin", "testWorld")
                .content(asJsonString(productIdToCount))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        ArgumentCaptor<Cart> capturedCartSave = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository).save(capturedCartSave.capture());

        // Verify we saved empty lists to the DB(because of the 0 count)
        assertThat(capturedCartSave.getValue().itemList.size()).isEqualTo(0);
        assertThat(capturedCartSave.getValue().itemCount.size()).isEqualTo(0);

    }

    private Cart setupMocks(List<String> ids, List<Item> items) {
        // Setup mock
        User user = new User("kevin", new byte[0], new byte[0]);
        Cart cart = new Cart("kevin", new ArrayList<>(), new ArrayList<>());
        when(authenticationService.authenticateAndGetUserFromAuthToken(any())).thenReturn(user);
        when(itemRepository.findAllById(ids)).thenReturn(items);
        when(cartRepository.findByUserName("kevin")).thenReturn(cart);
        return cart;
    }

    /**
     * Helper method for our tests
     */
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
