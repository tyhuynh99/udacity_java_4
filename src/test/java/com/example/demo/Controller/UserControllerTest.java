package com.example.demo.Controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);

    @Before
    public void setup() {
        userController = new UserController();

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);

        User user = new User();
        Cart cart = new Cart();
        user.setId(1);
        user.setUsername("user1");
        user.setPassword("password");
        user.setCart(cart);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("user1")).thenReturn(user);
        when(userRepository.findByUsername("testingUser")).thenReturn(null);
    }

    @Test
    public void findByIdTest_HappyPath() {
        ResponseEntity<User> response = userController.findById(1L);
        User body = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, body.getId());
        assertEquals("user1", body.getUsername());
        assertEquals("password", body.getPassword());
    }

    @Test
    public void findByUserNameTest_HappyPath() {
        ResponseEntity<User> response = userController.findByUserName("user1");
        User body = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, body.getId());
        assertEquals("user1", body.getUsername());
        assertEquals("password", body.getPassword());
    }

    @Test
    public void findByUserNameTest_UserNull() {
        ResponseEntity<User> response = userController.findByUserName("testingUser");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void createUserTest_HappyPath() {
        CreateUserRequest request = new CreateUserRequest(); 
        request.setUsername("user1");
        request.setPassword("password");
        ResponseEntity<User> response = userController.createUser(request);
        User body = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("user1", body.getUsername());
        assertEquals("password", body.getPassword());
    }

}
