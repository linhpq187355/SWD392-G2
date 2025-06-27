package services;

import daos.CartDAO;
import daos.OrderDAO;
import daos.ProductDAO;
import models.Cart;
import models.Order;
import models.OrderItem;
import models.Product;

import java.util.List;

public class ProductService {

    private final ProductDAO ProductDAO;

    public ProductService() {
        this.ProductDAO = new ProductDAO();
    }

    public List<Product> getLatestProducts() {
        return ProductDAO.getLatestProducts();
    }
}
