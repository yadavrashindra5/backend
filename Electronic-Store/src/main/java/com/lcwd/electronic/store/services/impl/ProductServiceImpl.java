package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helpers.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.response.PageableResponse;
import com.lcwd.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        productDto.setProductId(UUID.randomUUID().toString());

        logger.info("Product DTO: {}",productDto);

        Product product = mapper.map(productDto, Product.class);

        Product savedProduct = productRepository.save(product);

        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {

        Product product = productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product not found"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setQuantity(productDto.getQuantity());
        product.setProductImageName(productDto.getProductImageName());

        Product updatedProduct = productRepository.save(product);
        ProductDto productDto1 = mapper.map(updatedProduct, ProductDto.class);
        return productDto1;
    }

    @Override
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        productRepository.delete(product);
    }

    @Override
    public ProductDto getProduct(String id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=Sort.by(sortBy);
        sort=sortDir.equalsIgnoreCase("asc")?sort.ascending():sort.descending();

        Pageable pageRequest= PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> all = productRepository.findAll(pageRequest);

        PageableResponse<ProductDto> pageableResponse= Helper.getPageableResponse(all, ProductDto.class);

        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> getAllLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort=Sort.by(sortBy);
        sort=sortDir.equalsIgnoreCase("asc")?sort.ascending():sort.descending();

        Pageable pageRequest= PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> all = productRepository.findByLiveTrue(pageRequest);

        PageableResponse<ProductDto> pageableResponse= Helper.getPageableResponse(all, ProductDto.class);

        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String title, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=Sort.by(sortBy);
        sort=sortDir.equalsIgnoreCase("asc")?sort.ascending():sort.descending();

        Pageable pageRequest= PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> all = productRepository.findByTitleContaining(title,pageRequest);

        PageableResponse<ProductDto> pageableResponse= Helper.getPageableResponse(all, ProductDto.class);

        return pageableResponse;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        productDto.setProductId(UUID.randomUUID().toString());
        productDto.setAddedDate(new Date());

        logger.info("Product DTO: {}",productDto);

        Product product = mapper.map(productDto, Product.class);

        Product savedProduct = productRepository.save(product);
        savedProduct.setCategory(category);

        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return mapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Sort sort=Sort.by(sortBy);
        sort=sortDir.equalsIgnoreCase("asc")?sort.ascending():sort.descending();

        Pageable pageRequest= PageRequest.of(pageNumber,pageSize,sort);


        Page<Product> all = productRepository.findByCategory(category,pageRequest);

        PageableResponse<ProductDto> pageableResponse= Helper.getPageableResponse(all, ProductDto.class);

        return pageableResponse;
    }
}
