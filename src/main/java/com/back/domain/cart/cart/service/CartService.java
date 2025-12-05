package com.back.domain.cart.cart.service;

import com.back.domain.cart.cart.entity.Cart;
import com.back.domain.cart.cart.repository.CartRepository;
import com.back.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public long count() {
        return cartRepository.count();
    }

    public Cart make(Member buyer) {
        Cart cart = new Cart(buyer);

        return cartRepository.save(cart);
    }

    public Optional<Cart> findByBuyer(Member buyer) {
        return cartRepository.findByBuyer(buyer);
    }

    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }
}
