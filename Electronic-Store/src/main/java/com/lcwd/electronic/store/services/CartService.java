package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {
//    add items to cart
//    case 1: cart for user if not available: we will create the cart and add the items on it
//    case 2: cart available add the items to the cart

    CartDto addItemToCart(String userId, AddItemToCartRequest addItemToCartRequest);

//    remove item from cart
    void removeItemFromCart(String userId,int cartItem);

//    remove all items from cart
    void clearCart(String userId);

    CartDto getCartByUser(String userId);
}
