package com.back.domain.cart.cartItem.entity;

import com.back.domain.cart.cart.entity.Cart;
import com.back.domain.member.member.entity.Member;
import com.back.domain.product.product.entity.Product;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Getter
public class CartItem extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Cart cart;
    @ManyToOne(fetch = LAZY)
    private Member buyer;
    @ManyToOne(fetch = LAZY)
    private Product product;

    public CartItem(Cart cart, Member buyer, Product product) {
        this.cart = cart;
        this.buyer = buyer;
        this.product = product;
    }
}
