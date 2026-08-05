package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest item) {

        int quantity=item.getQuantity();
        String productId=item.getProductId();

        if(quantity<=0){
            throw new BadApiRequest("Requested quantity is not valid!!");
        }

//        fetched product
        Product product= productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Given productid does not found"));

//        fetched user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User id does not found"));

        Cart cart=null;

        try{
            cart=cartRepository.findByUser(user).get();
        }catch (NoSuchElementException ex){
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreateAt(new Date());
        }

        List<CartItem> items = cart.getItems();

        AtomicBoolean updated= new AtomicBoolean(false);

        List<CartItem> updatedItems = items.stream().map(cartItem -> {
            if (cartItem.getProduct().getProductId().equals(product.getProductId())) {
//                Item already present in cart
                cartItem.setQuantity(quantity);
                cartItem.setTotalPrice(quantity*product.getPrice());
                updated.set(true);
            }
            return cartItem;
        }).collect(Collectors.toList());


        if(!updated.get()){
            CartItem cartItem= CartItem.builder().product(product).cart(cart).quantity(quantity).totalPrice(product.getPrice() * quantity).build();
            updatedItems.add(cartItem);
        }

        cart.setItems(updatedItems);


        cart.setUser(user);

        Cart savedCart = cartRepository.save(cart);

        return modelMapper.map(savedCart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {

        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("Cart Item not found"));

        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User id does not found"));

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found!!"));

        cart.getItems().clear();

        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User id does not found"));

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found!!"));


        return modelMapper.map(cart,CartDto.class);
    }
}
