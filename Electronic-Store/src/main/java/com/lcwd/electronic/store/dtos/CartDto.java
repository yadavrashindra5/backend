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
public class CartDto {
    private String cartId;
    private Date createAt;
    private UserDto user;
    private List<CartItemDto> items=new ArrayList<>();
}
