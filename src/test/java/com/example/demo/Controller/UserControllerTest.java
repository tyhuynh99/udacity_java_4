package com.example.demo.Controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

public class UserControllerTest {
    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();

        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);

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

        User user = new User();
        Cart cart = new Cart();
        user.setId(1);
        user.setUsername("user1");
        user.setPassword("password");
        user.setCart(cart);

        when(userRepository.findByUsername("user1")).thenReturn(user);
        when(userRepository.findByUsername("testingUser")).thenReturn(null);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item1));
        when(itemRepository.findById(2L)).thenReturn(java.util.Optional.of(item2));
    }

    @Test
    public void removeFromCartTest_HappyPath() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("user1");
        request.setItemId(2L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Cart cart = response.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(4.4), cart.getTotal());

        request.setUsername("user1");
        request.setItemId(2L);
        request.setQuantity(1);

        response = cartController.removeFromcart(request);
        cart = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(2.2), cart.getTotal());

    }

    @Test
    public void removeFromCartTest_InvalidUser() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("user1");
        request.setItemId(2L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Cart cart = response.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(4.4), cart.getTotal());

        request.setUsername("testingUser");
        request.setItemId(2L);
        request.setQuantity(1);

        response = cartController.removeFromcart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart_InvalidItemId() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("user1");
        request.setItemId(2L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Cart cart = response.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(4.4), cart.getTotal());

        request.setUsername("test");
        request.setItemId(3L);
        request.setQuantity(1);

        response = cartController.removeFromcart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCartTest_HappyPath() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("user1");
        request.setItemId(2L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Cart cart = response.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(4.4), cart.getTotal());
    }

    @Test
    public void addToCartTest_InvalidId() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("user1");
        request.setItemId(3L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCartTest_WrongUser() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testingUser");
        request.setItemId(2L);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

}
