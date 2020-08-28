package com.example.ecommerce.controllers;


import com.example.ecommerce.model.persistence.Cart;
import com.example.ecommerce.model.persistence.User;
import com.example.ecommerce.model.persistence.repositories.CartRepository;
import com.example.ecommerce.model.persistence.repositories.UserRepository;
import com.example.ecommerce.model.requests.CreateUserRequest;
import com.example.ecommerce.model.requests.LoginUserRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);


    private UserRepository userRepository;


    private CartRepository cartRepository;


    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController() {
    }

    @Autowired
    public UserController(UserRepository userRepository, CartRepository cartRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        LOGGER.info("Username set with {} ", createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        if (createUserRequest.getPassword().length() < 7 || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            System.out.println("Wrong password...");
            LOGGER.error("Creation of User with username {} fails ", createUserRequest.getUsername());
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
        LOGGER.info("User {} created. ", createUserRequest.getUsername());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<User> loginUser(@RequestBody LoginUserRequest loginUserRequest) {
        User user = new User();
        user.setUsername(loginUserRequest.getUsername());
        LOGGER.info("User {} logs in ", loginUserRequest.getUsername());
        if (loginUserRequest.getPassword().length() < 7) {
            System.out.println("Wrong password...");
            LOGGER.error("Login of User with username {} fails ", loginUserRequest.getUsername());
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(bCryptPasswordEncoder.encode(loginUserRequest.getPassword()));
        if (userRepository.findByUsername(loginUserRequest.getUsername()) != null) {
        } else {
            return ResponseEntity.badRequest().build();
        }
        LOGGER.info("User {} logged. ", loginUserRequest.getUsername());
        return ResponseEntity.ok(user);
    }

}
