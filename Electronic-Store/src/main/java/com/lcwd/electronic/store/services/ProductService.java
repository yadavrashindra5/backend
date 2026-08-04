package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.response.PageableResponse;

public interface ProductService {
    //create
    ProductDto createProduct(ProductDto productDto);
    //update
    ProductDto updateProduct(ProductDto productDto, String productId);
    //delete
    void deleteProduct(String id);

    // get single product by id
    ProductDto getProduct(String id);

    // get all products
    PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);

    // get all live products
    PageableResponse<ProductDto> getAllLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto> searchByTitle(String title, int pageNumber, int pageSize, String sortBy, String sortDir);

    ProductDto createWithCategory(ProductDto productDto,String categoryId);

    ProductDto updateCategory(String productId,String categoryId);

    PageableResponse<ProductDto> getAllCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
}
