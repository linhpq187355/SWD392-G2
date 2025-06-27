package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Product;
import services.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet("/HomePage")
public class HomePageS extends HttpServlet {
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService(); // khởi tạo 1 lần
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> latestProducts = productService.getLatestProducts(); // gọi service

        request.setAttribute("latestProducts", latestProducts); // đẩy lên JSP
        request.getRequestDispatcher("home-page.jsp").forward(request, response);
    }
}
