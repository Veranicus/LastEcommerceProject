package com.example.ecommerce.controllers;


import com.example.ecommerce.TestUtils;
import com.example.ecommerce.model.persistence.Item;
import com.example.ecommerce.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemControllerTest {


    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);


    @BeforeEach
    void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    void getItems() {
        when(itemRepository.findAll()).thenReturn(createTestItemList());
        assertNotNull(itemController.getItems());
        assertEquals(2, itemController.getItems().getBody().size());

    }

    @Test
    void getItemById() {
        Optional<Item> testOptionalItem = Optional.of(createTestItemList().get(0));
        when(itemRepository.findById(1L)).thenReturn(testOptionalItem);

        assertNotNull(itemController.getItemById(1L));
        assertEquals("banana", itemController.getItemById(1L).getBody().getName());

    }

    @Test
    void getItemsByName() {
        List<Item> orangeReturned = new ArrayList<>();
        orangeReturned.add(createTestItemList().get(1));
        when(itemRepository.findByName("orange")).thenReturn(orangeReturned);

        assertNotNull(itemController.getItemsByName("orange"));
        assertEquals("orange", itemController.getItemsByName("orange").getBody().get(0).getName());
        assertEquals(orangeReturned.size(), itemController.getItemsByName("orange").getBody().size());

        assertEquals(404, itemController.getItemsByName("badName").getStatusCodeValue());
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