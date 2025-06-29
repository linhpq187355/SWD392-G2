package controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Category;
import models.Product;
import services.ProductService;
import services.SettingService;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductServlet", urlPatterns = {
        "/productList",
        "/addProduct",
        "/productUpdate"
})
@MultipartConfig
public class ProductServlet extends HttpServlet {

    private ProductService productService = new ProductService();
    private SettingService settingService = new SettingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/productList":
                handleGetProducts(request, response);
                break;
            case "/addProduct":
                handleGetAddProduct(request, response);
                break;
            case "/productUpdate":
                handleGetUpdateProduct(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/addProduct":
                handlePostAddProduct(request, response);
                break;
            case "/productUpdate":
                handlePostUpdateProduct(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleGetProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productService.getAllProduct();

        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                            (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
                    .create();
            String json = gson.toJson(products);
            response.getWriter().write(json);
        } else {
            List<Category> categoryList = settingService.getAllCategories();
            request.setAttribute("categoryList", categoryList);
            request.getRequestDispatcher("/product-list.jsp").forward(request, response);
        }
    }

    private void handleGetAddProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categoryList = settingService.getAllCategories();
        request.setAttribute("categoryList", categoryList);

        request.getRequestDispatcher("/add-product.jsp").forward(request, response);
    }

    private void handleGetUpdateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        Product product = productService.getProductById(Integer.parseInt(id));
        List<String> productImage = productService.getProductImages(Integer.parseInt(id));

        List<Category> categoryList = settingService.getAllCategories();
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("product", product);
        request.setAttribute("productImage", productImage);
        request.getRequestDispatcher("/update-product.jsp").forward(request, response);
    }

    private void handlePostAddProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = productService.validateProduct(request, false, null);

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);

            Map<String, String> oldValues = new HashMap<>();
            request.getParameterMap().forEach((key, value) -> {
                if (value.length > 0) oldValues.put(key, value[0]);
            });
            request.setAttribute("oldValues", oldValues);

            List<Category> categoryList = settingService.getAllCategories();
            request.setAttribute("categoryList", categoryList);

            request.getRequestDispatcher("/add-product.jsp").forward(request, response);
            return;
        }

        try {
            productService.addProduct(request);
            response.sendRedirect("productList?message=addSuccess");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to add product.");
            request.getRequestDispatcher("/product-form.jsp").forward(request, response);
        }
    }

    private void handlePostUpdateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Map<String, String> errors = productService.validateProduct(request, true, id);

        if (!errors.isEmpty()) {
            // Giữ lại các giá trị người dùng đã nhập
            Map<String, String> oldValues = new HashMap<>();
            request.getParameterMap().forEach((key, value) -> {
                if (value.length > 0) oldValues.put(key, value[0]);
            });

            // Lấy lại danh mục, sản phẩm, hình ảnh cũ
            Product product = productService.getProductById(id);
            List<String> productImages = productService.getProductImages(id);
            List<Category> categoryList = settingService.getAllCategories();

            request.setAttribute("errors", errors);
            request.setAttribute("oldValues", oldValues);
            request.setAttribute("product", product);
            request.setAttribute("productImage", productImages);
            request.setAttribute("categoryList", categoryList);

            request.getRequestDispatcher("/update-product.jsp").forward(request, response);
            return;
        }


        try {
            productService.updateProduct(request);
            response.sendRedirect("productList?message=updateSuccess");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update product.");

            // Gửi lại về trang cập nhật
            Product product = productService.getProductById(id);
            List<String> productImages = productService.getProductImages(id);
            List<Category> categoryList = settingService.getAllCategories();

            request.setAttribute("product", product);
            request.setAttribute("productImage", productImages);
            request.setAttribute("categoryList", categoryList);

            request.getRequestDispatcher("/update-product.jsp").forward(request, response);
        }
    }
}