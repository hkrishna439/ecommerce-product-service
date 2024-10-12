package com.ecommerce.productservice.services;

import com.ecommerce.productservice.dtos.FakeStoreProductDTO;
import com.ecommerce.productservice.models.Category;
import com.ecommerce.productservice.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FakeStoreProductService implements ProductService{
    private RestTemplate restTemplate;

    FakeStoreProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
    @Override
    //FakeStoreProductDTO - contract between our service and other service
    public Product getProductById(Long id) {
        FakeStoreProductDTO fakeStoreProductDTO = restTemplate.getForObject("https://fakestoreapi.com/products/"+id, FakeStoreProductDTO.class);
        return convertToProduct(fakeStoreProductDTO);
    }

    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    @Override
    public Product updateProduct() {
        return null;
    }

    @Override
    public Product replaceProduct() {
        return null;
    }

    @Override
    public Product createProduct() {
        return null;
    }

    @Override
    public Product deleteProduct() {
        return null;
    }

    private static Product convertToProduct(FakeStoreProductDTO dto) {
        if(dto == null){
            return null;
        }

        Product product = new Product();

        // Set product fields from DTO
        product.setId(dto.getId());
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());

        // Map category from String to Category object
        Category category = new Category();
        category.setId(0);
        category.setTitle(dto.getCategory());
        product.setCategory(category);

        return product;
    }
}
