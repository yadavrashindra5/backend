package com.lcwd.electronic.store.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {

    private String cartId;

    private String userId;

    private String orderStatus="PENDING";

    private String paymentStatus="NOT-PAID";

    private String billingAddress;

    private String billingPhone;

    private String billingName;

}
