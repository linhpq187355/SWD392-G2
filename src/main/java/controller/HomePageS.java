package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Product;
import services.ProductService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/homePage")
public class HomePageS extends HttpServlet {
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService(); // khởi tạo 1 lần
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> latestProducts = productService.getLatestProductsWithImages(); // gọi service
        List<Product> mostSellProduct = productService.getMostSellProductWithImages(); // gọi service

        HttpSession session = request.getSession();

        // Nếu chưa có cart thì tạo mới cho session
        if (session.getAttribute("guestCart") == null && session.getAttribute("user") == null) {
            Map<Integer, Integer> guestCart = new HashMap<>(); // Map<productId, quantity>
            session.setAttribute("guestCart", guestCart);
        }
        request.setAttribute("mostSellProduct", mostSellProduct); // đẩy lên JSP

        request.setAttribute("latestProducts", latestProducts); // đẩy lên JSP
        request.getRequestDispatcher("home-page.jsp").forward(request, response);
    }
}
