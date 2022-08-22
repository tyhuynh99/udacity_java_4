package com.example.demo.Controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();

        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

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

        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(item1);
        cart.addItem(item2);

        User user = new User();
        user.setId(1);
        user.setUsername("user1");
        user.setPassword("password");
        user.setCart(cart);
        cart.setUser(user);

        when(userRepository.findByUsername("user1")).thenReturn(user);
        when(userRepository.findByUsername("testingUser")).thenReturn(null);
    }

    @Test
    public void submitTest_HappyPath() {
        String username = "user1";
        ResponseEntity<UserOrder> response = orderController.submit(username);
        UserOrder body = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, body.getItems().size());
        assertEquals(username, body.getUser().getUsername());
        assertEquals(BigDecimal.valueOf(3.3), body.getTotal());
    }
}
