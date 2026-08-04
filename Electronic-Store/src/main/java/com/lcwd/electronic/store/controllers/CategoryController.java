package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.response.ApiResponse;
import com.lcwd.electronic.store.response.PageableResponse;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryDto>>createCategory(@RequestBody CategoryDto categoryDto){
        CategoryDto category = categoryService.createCategory(categoryDto);
        ApiResponse<CategoryDto> userCreatedSuccessfully = ApiResponse.success("User created Successfully", category);
        return new ResponseEntity<>(userCreatedSuccessfully, HttpStatus.CREATED);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable(value = "categoryId") String categoryId){
        CategoryDto category = categoryService.updateCategory(categoryDto, categoryId);
        ApiResponse<CategoryDto> userUpdatedSuccessfully = ApiResponse.success("User Updated Successfully", category);
        return new ResponseEntity<>(userUpdatedSuccessfully, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategory(@PathVariable(value = "categoryId") String categoryId){
        CategoryDto category = categoryService.getCategory(categoryId);
        ApiResponse<CategoryDto> userFound = ApiResponse.success("User Found", category);
        return new ResponseEntity<>(userFound, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable(value = "categoryId")String categoryId){
        categoryService.deleteCategory(categoryId);
        ApiResponse<String> userDeletedSuccessfully = ApiResponse.success("User Deleted Successfully", null);
        return new ResponseEntity<>(userDeletedSuccessfully, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PageableResponse<CategoryDto>>> getAllCategories(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    )
    {
        PageableResponse<CategoryDto> allCategories = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        ApiResponse<PageableResponse<CategoryDto>> allCategoriesFound = ApiResponse.success("All Categories Found", allCategories);
        return new ResponseEntity<>(allCategoriesFound, HttpStatus.OK);
    }

    // create product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ApiResponse<ProductDto>> createWithCategory(@RequestBody ProductDto productDto, @PathVariable(value = "categoryId") String categoryId){
        ProductDto withCategory = productService.createWithCategory(productDto, categoryId);
        ApiResponse<ProductDto> productCreatedSuccessfully = ApiResponse.success("Product Created Successfully", withCategory);
        return new ResponseEntity<>(productCreatedSuccessfully, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> updateCategoryOfProducts(@PathVariable(value = "categoryId") String categoryId,@PathVariable(value = "productId") String productId){
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        ApiResponse<ProductDto> productUpdatedSuccessfully = ApiResponse.success("Product Updated Successfully", productDto);
        return new ResponseEntity<>(productUpdatedSuccessfully, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<ApiResponse<PageableResponse<ProductDto>>> getProductByCategoryId(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir,
            @PathVariable(value = "categoryId") String categoryId
    ){
        PageableResponse<ProductDto> allCategory= productService.getAllCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);

        ApiResponse<PageableResponse<ProductDto>> allCategoryFound = ApiResponse.success("All Category Found", allCategory);
        return new ResponseEntity<>(allCategoryFound, HttpStatus.OK);
    }
}
