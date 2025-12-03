package com.back.domain.order.order.service;

import com.back.domain.cart.cart.entity.Cart;
import com.back.domain.cash.cashLog.entity.CashLog;
import com.back.domain.cash.wallet.entity.Wallet;
import com.back.domain.cash.wallet.service.WalletService;
import com.back.domain.order.order.entity.Order;
import com.back.domain.order.order.repository.OrderRepository;
import com.back.global.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final WalletService walletService;

    public Order make(Cart cart) {
        Wallet wallet = walletService.findByHolder(cart.getBuyer()).get();

        Order order = new Order(
                cart.getBuyer(),
                wallet,
                0,
                0
        );

        order.addItemsFrom(cart.getItems());

        return orderRepository.save(order);
    }

    public long count() {
        return orderRepository.count();
    }

    public Optional<Order> findById(int id) {
        return orderRepository.findById(id);
    }

    public void completePayment(Order order) {
        if (order.isCanceled()) throw new BusinessException("400-1", "이미 취소된 주문입니다.");
        if (order.isPaid()) throw new BusinessException("400-1", "이미 결제된 주문입니다.");

        Wallet wallet = walletService.findByHolder(order.getBuyer()).get();
        Wallet systemWallet = walletService.findSystemWallet().get();

        int salePrice = order.getSalePrice();

        if (wallet.getBalance() < salePrice) {
            throw new BusinessException("400-1", "캐시가 부족합니다.");
        }

        wallet.debit(salePrice, CashLog.EventType.사용__주문결제, order);
        systemWallet.credit(salePrice, CashLog.EventType.임시보관__주문결제, order);

        order.payDone();
    }
}
