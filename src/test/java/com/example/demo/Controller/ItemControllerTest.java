package com.example.demo.Controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();

        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        List<Item> items = new ArrayList<>();

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setPrice(BigDecimal.valueOf(1.1));
        item1.setDescription("This is item 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");
        item2.setPrice(BigDecimal.valueOf(2.2));
        item2.setDescription("This is item 2");

        items.add(item1);
        items.add(item2);

        List<Item> itemsTmp = new ArrayList<>();
        itemsTmp.add(item1);

        when(itemRepository.findAll()).thenReturn(items);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findByName("Item 1")).thenReturn(itemsTmp);
        when(itemRepository.findByName("testing item")).thenReturn(null);
        when(itemRepository.findByName("testing item2")).thenReturn(new ArrayList<>());
    }

    @Test
    public void getItemsTest_HappyPath() {
        ResponseEntity<List<Item>> response = itemController.getItems();

        List<Item> body = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, body.size());

        Item item1 = body.get(0);
        assertEquals(Long.valueOf(1), item1.getId());
        assertEquals("Item 1", item1.getName());
        assertEquals(BigDecimal.valueOf(1.1), item1.getPrice());
        assertEquals("This is item 1", item1.getDescription());

        Item item2 = body.get(1);
        assertEquals(Long.valueOf(2), item2.getId());
        assertEquals("Item 2", item2.getName());
        assertEquals(BigDecimal.valueOf(2.2), item2.getPrice());
        assertEquals("This is item 2", item2.getDescription());

    }

    @Test
    public void getItemByIdTest_HappyPath() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        Item body = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Long.valueOf(1), body.getId());
        assertEquals("Item 1", body.getName());
        assertEquals(BigDecimal.valueOf(1.1), body.getPrice());
        assertEquals("This is item 1", body.getDescription());
    }

    @Test
    public void getItemsByNameTest_HappyPath() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item 1");
        List<Item> body = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, body.size());

        Item item1 = body.get(0);
        assertEquals(Long.valueOf(1), item1.getId());
        assertEquals("Item 1", item1.getName());
        assertEquals(BigDecimal.valueOf(1.1), item1.getPrice());
        assertEquals("This is item 1", item1.getDescription());
    }

    @Test
    public void getItemsByNameTest_ItemsNull() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("testing item");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByNameTest_ItemsEmpty() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("testing item2");
        assertEquals(404, response.getStatusCodeValue());
    }
}
