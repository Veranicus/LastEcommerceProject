package com.example.ecommerce.model.persistence.repositories;


import com.example.ecommerce.model.persistence.Cart;
import com.example.ecommerce.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
