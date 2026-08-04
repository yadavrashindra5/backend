package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.response.ApiResponse;
import com.lcwd.electronic.store.response.ImageResponse;
import com.lcwd.electronic.store.response.PageableResponse;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String productImagePath;

    // create
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestBody ProductDto productDto){
        ProductDto product = productService.createProduct(productDto);
        ApiResponse<ProductDto> productCreatedSuccessfully = ApiResponse.success("Product Created Successfully", product);
        return new ResponseEntity<>(productCreatedSuccessfully, HttpStatus.CREATED);
    }

    // update
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(@RequestBody ProductDto productDto,@PathVariable(name = "productId") String productId){
        ProductDto product = productService.updateProduct(productDto, productId);
        ApiResponse<ProductDto> productUpdatedSuccessfully = ApiResponse.success("Product Updated Successfully", product);
        return new ResponseEntity<>(productUpdatedSuccessfully, HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable String productId){
        productService.deleteProduct(productId);
        ApiResponse<String> productDeletedSuccessfully = ApiResponse.success("Product Deleted Successfully", null);
        return new ResponseEntity<>(productDeletedSuccessfully, HttpStatus.NO_CONTENT);
    }

    // get single product by id
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> getSingleProduct(@PathVariable(value = "productId")String productId){
        ProductDto product = productService.getProduct(productId);
        ApiResponse<ProductDto> productFound = ApiResponse.success("Product Found", product);
        return new ResponseEntity<>(productFound, HttpStatus.OK);
    }

    // get all products
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PageableResponse<ProductDto>>> getAllProducts(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> allProducts = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        ApiResponse<PageableResponse<ProductDto>> allProductsFound = ApiResponse.success("All Products Found", allProducts);
        return new ResponseEntity<>(allProductsFound, HttpStatus.OK);
    }

    // search products by title
    @GetMapping("/search/{keyword}")
    public ResponseEntity<ApiResponse<PageableResponse<ProductDto>>> searchByTitle(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir,
            @PathVariable(value = "keyword") String keyword
    ){
        PageableResponse<ProductDto> title = productService.searchByTitle(keyword, pageNumber, pageSize, sortBy, sortDir);
        ApiResponse<PageableResponse<ProductDto>> titleFound = ApiResponse.success("Title Found", title);
        return new ResponseEntity<>(titleFound, HttpStatus.OK);
    }

    // get all live products
    @GetMapping("/live")
    public ResponseEntity<ApiResponse<PageableResponse<ProductDto>>> getAllLiveProducts(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> allLiveProducts = productService.getAllLiveProducts(pageNumber, pageSize, sortBy, sortDir);
        ApiResponse<PageableResponse<ProductDto>> allLiveProductsFound = ApiResponse.success("All Live Products Found", allLiveProducts);
        return new ResponseEntity<>(allLiveProductsFound, HttpStatus.OK);
    }


    @PostMapping("/product-image/{productId}")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("productImage") MultipartFile image, @PathVariable("productId") String productId) throws IOException {
        String imageName=fileService.uploadFile(image,productImagePath);

        ProductDto productDto = productService.getProduct(productId);
        productDto.setProductImageName(imageName);

        ProductDto productDto1= productService.updateProduct(productDto, productId);

        ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    @GetMapping("/product-image/{productId}")
    public void serveUserImage(@PathVariable("productId") String productId, HttpServletResponse response) throws IOException {
        ProductDto productDto = productService.getProduct(productId);
        InputStream resource = fileService.getResource(productImagePath, productDto.getProductImageName());
        response.setContentType("image/jpeg");
        StreamUtils.copy(resource, response.getOutputStream());
    }



}
