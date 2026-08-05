package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.response.ApiResponse;
import com.lcwd.electronic.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("{userId}")
    public ResponseEntity<ApiResponse<CartDto>> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request){
        CartDto cartDto = cartService.addItemToCart(userId, request);
        ApiResponse<CartDto> cartCreatedSuccessfully = ApiResponse.success("Cart Created Successfully", cartDto);
        return new ResponseEntity<>(cartCreatedSuccessfully, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponse<String>> removeItemFromCart(@PathVariable int itemId, @PathVariable String userId){
        cartService.removeItemFromCart(userId,itemId);
        ApiResponse<String> cartDeletedSuccessfully = ApiResponse.success("Cart Deleted Successfully", null);
        return new ResponseEntity<>(cartDeletedSuccessfully, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> clearCart(@PathVariable String userId){
        cartService.clearCart(userId);
        ApiResponse<String> cartDeletedSuccessfully = ApiResponse.success("Now cart is blank", null);
        return new ResponseEntity<>(cartDeletedSuccessfully, HttpStatus.NO_CONTENT);
    }

    @GetMapping("{userId}")
    public ResponseEntity<ApiResponse<CartDto>> getCart(@PathVariable String userId){
        CartDto cartDto = cartService.getCartByUser(userId);
        ApiResponse<CartDto> cartCreatedSuccessfully = ApiResponse.success("Cart fetch successfully", cartDto);
        return new ResponseEntity<>(cartCreatedSuccessfully, HttpStatus.CREATED);
    }
}
