package services;

import daos.ProductDAO;
import models.Product;

import java.util.List;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }
    public List<Product> getLatestProducts() {
        return productDAO.getLatestProducts();
    }
    public List<Product> getLatestProductsWithImages() {
        List<Product> products = productDAO.getLatestProducts();
        for (Product product : products) {
            List<String> images = productDAO.getProductImages(product.getId());
            product.setImages(images);
        }
        return products;
    }
    public List<Product> getMostSellProductWithImages() {
        List<Product> products = productDAO.getMostSellProduct();
        for (Product product : products) {
            List<String> images = productDAO.getProductImages(product.getId());
            product.setImages(images);
        }
        return products;
    }
        public static void main(String[] args) {
            ProductService productService = new ProductService();
            List<Product> products = productService.getLatestProductsWithImages();

            for (Product product : products) {
                System.out.println("=== Product ===");
                System.out.println("ID: " + product.getId());
                System.out.println("Name: " + product.getName());
                System.out.println("Price: " + product.getSalePrice());
                System.out.println("Images:");
                if (product.getImages() != null && !product.getImages().isEmpty()) {
                    for (String img : product.getImages()) {
                        System.out.println(" - " + img);
                    }
                } else {
                    System.out.println(" - No images available");
                }
                System.out.println();
            }

    }

}
