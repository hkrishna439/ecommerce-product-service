package com.ecommerce.productservice.services;

import com.ecommerce.productservice.dtos.FakeStoreProductDTO;
import com.ecommerce.productservice.models.Category;
import com.ecommerce.productservice.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements ProductService{
    private RestTemplate restTemplate;

    FakeStoreProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
    @Override
    //FakeStoreProductDTO is contract between our service and other third party service
    public Product getProductById(Long id) {
        FakeStoreProductDTO fakeStoreProductDTO = restTemplate.getForObject("https://fakestoreapi.com/products/"+id, FakeStoreProductDTO.class);
        return convertToProduct(fakeStoreProductDTO);
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductDTO[] fakeStoreProducts = restTemplate.getForObject("https://fakestoreapi.com/products", FakeStoreProductDTO[].class);

        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDTO fakeStoreProductDTO : fakeStoreProducts){
            products.add(convertToProduct(fakeStoreProductDTO));
        }
        return products;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        FakeStoreProductDTO fakeStoreProductDTO = convertToDTO(product);
        fakeStoreProductDTO = restTemplate.patchForObject("https://fakestoreapi.com/products/"+id, fakeStoreProductDTO, FakeStoreProductDTO.class);
        return convertToProduct(fakeStoreProductDTO);
    }

    @Override
    public Product replaceProduct() {
        return null;
    }

    @Override
    public Product createProduct(Product product) {
        FakeStoreProductDTO fakeStoreProductDTO = convertToDTO(product);
        fakeStoreProductDTO = restTemplate.postForObject("https://fakestoreapi.com/products", fakeStoreProductDTO, FakeStoreProductDTO.class);

        return convertToProduct(fakeStoreProductDTO);
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

    public static FakeStoreProductDTO convertToDTO(Product product) {
        if (product == null) return null;

        FakeStoreProductDTO dto = new FakeStoreProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());

        // Set the category title (if category is not null)
        if (product.getCategory() != null) {
            dto.setCategory(product.getCategory().getTitle());
        } else {
            dto.setCategory(null);
        }

        dto.setDescription(product.getDescription());
        dto.setImage(null);  // Assuming the image is not present in Product entity
        return dto;
    }
}
