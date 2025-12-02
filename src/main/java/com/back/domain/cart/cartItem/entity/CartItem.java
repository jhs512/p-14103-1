package com.back.domain.cart.cartItem.entity;

import com.back.domain.cart.cart.entity.Cart;
import com.back.domain.member.member.entity.Member;
import com.back.domain.product.product.entity.Product;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class CartItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member buyer;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public CartItem(Cart cart, Member buyer, Product product) {
        this.cart = cart;
        this.buyer = buyer;
        this.product = product;
    }
}
