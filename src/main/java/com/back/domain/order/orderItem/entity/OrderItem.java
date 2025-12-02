package com.back.domain.order.orderItem.entity;

import com.back.domain.member.member.entity.Member;
import com.back.domain.order.order.entity.Order;
import com.back.domain.product.product.entity.Product;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
public class OrderItem extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Order order;

    @ManyToOne(fetch = LAZY)
    private Member buyer;

    @ManyToOne(fetch = LAZY)
    private Product product;

    private String productName;

    private String displayProductName;

    private int price;

    private int salePrice;

    public OrderItem(Order order, Member buyer, Product product, String productName, String displayProductName, int price, int salePrice) {
        this.order = order;
        this.buyer = buyer;
        this.product = product;
        this.productName = productName;
        this.displayProductName = displayProductName;
        this.price = price;
        this.salePrice = salePrice;
    }
}
