package com.example.ecommerce.controllers;


import com.example.ecommerce.TestUtils;
import com.example.ecommerce.model.persistence.Cart;
import com.example.ecommerce.model.persistence.Item;
import com.example.ecommerce.model.persistence.User;
import com.example.ecommerce.model.persistence.repositories.CartRepository;
import com.example.ecommerce.model.persistence.repositories.ItemRepository;
import com.example.ecommerce.model.persistence.repositories.UserRepository;
import com.example.ecommerce.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @BeforeEach
    void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    void addTocart() {
        when(userRepository.findByUsername(createTestUser().getUsername())).thenReturn(createTestUser());

        when(itemRepository.findById(createTestModifyCartRequest(false, false).getItemId()))
                .thenReturn(Optional.of(createTestItemList().get(0)));

        assertEquals(1, cartController.addTocart(
                createTestModifyCartRequest(false, false))
                .getBody().getItems().size());

        assertEquals(HttpStatus.NOT_FOUND, cartController.addTocart(createTestModifyCartRequest(true, false))
                .getStatusCode());

        assertEquals(HttpStatus.NOT_FOUND, cartController.addTocart(
                createTestModifyCartRequest(false, true))
                .getStatusCode());

        assertEquals(HttpStatus.OK, cartController.addTocart(
                createTestModifyCartRequest(false, false))
                .getStatusCode());

        assertEquals("banana", cartController.addTocart(
                createTestModifyCartRequest(false, false))
                .getBody().getItems().get(1).getName());


    }

    @Test
    void removeFromCart() {
        when(userRepository.findByUsername(createTestUser().getUsername())).thenReturn(createTestUser());
        when(itemRepository.findById(createTestModifyCartRequest(false, false).getItemId()))
                .thenReturn(Optional.of(createTestItemList().get(0)));

        assertEquals(HttpStatus.NOT_FOUND, cartController.removeFromcart(createTestModifyCartRequest(true,
                false))
                .getStatusCode());

        assertEquals(HttpStatus.NOT_FOUND, cartController.removeFromcart(createTestModifyCartRequest(false,
                true))
                .getStatusCode());

        assertEquals(HttpStatus.OK, cartController.removeFromcart(
                createTestModifyCartRequest(false, false))
                .getStatusCode());

        assertEquals(0, cartController.removeFromcart(
                createTestModifyCartRequest(false, false))
                .getBody().getItems().size());
    }


    private User createTestUser() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setId(1);
        Cart cart = new Cart();
        cart.setId(1L);

//        List<Item> testItems = new ArrayList<>();
//        Item testItem1 = new Item();
//        testItem1.setDescription("Fresh From Garden");
//        testItem1.setId(0L);
//        testItem1.setName("Apple");
//        testItems.add(testItem1);
//        cart.setItems(testItems);
        cart.setTotal(new BigDecimal(2));
        cart.setUser(user);
        user.setCart(cart);
        return user;
    }

    private ModifyCartRequest createTestModifyCartRequest(Boolean nullUsername, Boolean nullItem) {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        if (nullUsername) {
            modifyCartRequest.setUsername("testNullUsername");
        } else {
            modifyCartRequest.setUsername(createTestUser().getUsername());
        }
        if (nullItem) {
            modifyCartRequest.setItemId(4564);
        } else {
            modifyCartRequest.setItemId(1L);
        }
        modifyCartRequest.setQuantity(1);
        return modifyCartRequest;
    }

    private List<Item> createTestItemList() {
        Item item1 = new Item();
        item1.setName("banana");
        item1.setId(1L);
        item1.setDescription("tasty banana");
        item1.setPrice(new BigDecimal(1));

        Item item2 = new Item();
        item2.setName("orange");
        item2.setId(2L);
        item2.setDescription("tasty orange");
        item2.setPrice(new BigDecimal(2));

        List<Item> testItems = new ArrayList<>();
        testItems.add(item1);
        testItems.add(item2);

        return testItems;
    }
}