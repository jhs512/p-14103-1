package com.back.domain.order.order.controller;

import com.back.domain.order.order.entity.Order;
import com.back.domain.order.order.service.OrderService;
import com.back.domain.order.order.service.TossPaymentsService;
import com.back.global.exceptions.BusinessException;
import com.back.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class ApiV1OrderController {
    private final OrderService orderService;
    private final TossPaymentsService tossPaymentsService;


    public record ConfirmPaymentByTossPaymentsReqBody(
            @NotBlank String paymentKey,
            @NotBlank String orderId,
            @NotNull int amount
    ) {
    }

    @CrossOrigin(
            origins = {
                    "https://cdpn.io",
                    "https://codepen.io"
            },
            allowedHeaders = "*",
            methods = {RequestMethod.POST}
    )
    @PostMapping("/{id}/payment/confirm/by/tossPayments")
    @Transactional
    public RsData<Void> confirmPaymentByTossPayments(
            @PathVariable int id,
            @Valid @RequestBody ConfirmPaymentByTossPaymentsReqBody reqBody
    ) {
        Order order = orderService.findById(id).get();

        if (order.getSalePrice() != reqBody.amount())
            throw new BusinessException("400-1", "결제할 금액이 일치하지 않습니다.");

        if (order.getId() != Integer.parseInt(reqBody.orderId.split("-", 3)[1]))
            throw new BusinessException("400-1", "주문번호가 일치하지 않습니다.");

        tossPaymentsService.confirmPayment(
                reqBody.paymentKey(),
                reqBody.orderId(),
                reqBody.amount()
        );

        orderService.completePayment(order);

        return new RsData<>("201-1", "결제성공");
    }
}