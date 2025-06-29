package services;

import daos.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import models.Category;
import models.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ProductService {
    ProductDAO productDAO = new ProductDAO();
    public List<Product> getAllProduct(){
        return productDAO.getAllProduct();
    }

    public void addProduct(HttpServletRequest request) throws Exception {
        String code = request.getParameter("productCode");
        String name = request.getParameter("productName");
        int stock = Integer.parseInt(request.getParameter("stockQuantity"));
        int categoryId = Integer.parseInt(request.getParameter("category"));
        boolean isActive = "active".equals(request.getParameter("status"));
        double originalPrice = Double.parseDouble(request.getParameter("originalPrice"));
        double salePrice = Double.parseDouble(request.getParameter("salePrice"));
        String description = request.getParameter("description");

        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setStock(stock);
        product.setOriginalPrice(originalPrice);
        product.setSalePrice(salePrice);
        product.setActive(isActive);
        product.setDescription(description);

        Category category = new Category();
        category.setId(categoryId);
        product.setCategory(category);

        CloudinaryProxy cloudinary = new CloudinaryProxy();
        List<String> imageUrls = new ArrayList<>();

        for (Part part : request.getParts()) {
            if ("image".equals(part.getName()) && part.getSize() > 0) {
                File tempFile = File.createTempFile("upload_", part.getSubmittedFileName());
                try (InputStream input = part.getInputStream();
                     FileOutputStream output = new FileOutputStream(tempFile)) {
                    input.transferTo(output);
                }
                String url = cloudinary.uploadImage(tempFile);
                imageUrls.add(url);
                tempFile.delete();
            }
        }

        int id = productDAO.insertProduct(product);
        productDAO.insertImages(imageUrls, id);
    }

    public Map<String, String> validateProduct(HttpServletRequest request, boolean isUpdate, Integer currentId) {
        Map<String, String> errors = new HashMap<>();

        String code = request.getParameter("productCode");
        if (code == null || code.trim().isEmpty()) {
            errors.put("productCode", "Product code is required.");
        } else {
            List<Product> allProducts = getAllProduct();
            boolean exists = allProducts.stream()
                    .anyMatch(p -> p.getCode().equalsIgnoreCase(code) && (!isUpdate || p.getId() != currentId));
            if (exists) {
                errors.put("productCode", "Product code already exists.");
            }
        }

        String name = request.getParameter("productName");
        if (name == null || name.trim().isEmpty()) {
            errors.put("productName", "Product name is required.");
        } else if (name.length() > 50) {
            errors.put("productName", "Product name must not exceed 50 characters.");
        }

        String stockStr = request.getParameter("stockQuantity");
        if (stockStr == null || stockStr.trim().isEmpty()) {
            errors.put("stockQuantity", "Stock quantity is required.");
        } else {
            try {
                int stock = Integer.parseInt(stockStr.trim());
                if (stock <= 0) {
                    errors.put("stockQuantity", "Stock must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                errors.put("stockQuantity", "Stock must be a valid number.");
            }
        }

        if (request.getParameter("category") == null || request.getParameter("category").trim().isEmpty()) {
            errors.put("category", "Category is required.");
        }

        if (request.getParameter("status") == null || request.getParameter("status").trim().isEmpty()) {
            errors.put("status", "Status is required.");
        }

        String originalPriceStr = request.getParameter("originalPrice");
        if (originalPriceStr == null || originalPriceStr.trim().isEmpty()) {
            errors.put("originalPrice", "Original price is required.");
        } else {
            try {
                double originalPrice = Double.parseDouble(originalPriceStr.trim());
                if (originalPrice <= 0) {
                    errors.put("originalPrice", "Original price must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                errors.put("originalPrice", "Original price must be a valid number.");
            }
        }

        String salePriceStr = request.getParameter("salePrice");
        if (salePriceStr == null || salePriceStr.trim().isEmpty()) {
            errors.put("salePrice", "Sale price is required.");
        } else {
            try {
                double salePrice = Double.parseDouble(salePriceStr.trim());
                if (salePrice <= 0) {
                    errors.put("salePrice", "Sale price must be greater than 0.");
                } else if (originalPriceStr != null && !originalPriceStr.trim().isEmpty()) {
                    try {
                        double originalPrice = Double.parseDouble(originalPriceStr.trim());
                        if (salePrice >= originalPrice) {
                            errors.put("salePrice", "Sale price must be less than original price.");
                        }
                    } catch (NumberFormatException ignored) {

                    }
                }
            } catch (NumberFormatException e) {
                errors.put("salePrice", "Sale price must be a valid number.");
            }
        }

        try {
            Part[] images = request.getParts().stream()
                    .filter(p -> p.getName().equals("image") && p.getSize() > 0)
                    .toArray(Part[]::new);

            String[] existingImages = request.getParameterValues("existingImages");

            boolean hasNoNewImages = images.length == 0;
            boolean hasNoExistingImages = (existingImages == null || existingImages.length == 0);

            if (hasNoNewImages && hasNoExistingImages) {
                errors.put("image", "At least one image is required.");
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            errors.put("image", "Failed to process uploaded images.");
        }

        String description = request.getParameter("description");
        if (description != null && description.length() > 1000) {
            errors.put("description", "Description must not exceed 1000 characters.");
        }

        return errors;
    }

    public Product getProductById(int id) {
        return productDAO.getProduct(id);
    }

    public List<String> getProductImages(int id) {
        return productDAO.getProductImages(id);
    }

    public void updateProduct(HttpServletRequest request) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        String productCode = request.getParameter("productCode");
        String name = request.getParameter("productName");
        int categoryId = Integer.parseInt(request.getParameter("category"));
        boolean isActive = "active".equalsIgnoreCase(request.getParameter("status"));
        int stock = Integer.parseInt(request.getParameter("stockQuantity"));
        double originalPrice = Double.parseDouble(request.getParameter("originalPrice"));
        double salePrice = Double.parseDouble(request.getParameter("salePrice"));
        String description = request.getParameter("description");

        Product product = new Product();
        product.setId(id);
        product.setCode(productCode);
        product.setName(name);
        Category category = new Category();
        category.setId(categoryId);
        product.setCategory(category);
        product.setActive(isActive);
        product.setStock(stock);
        product.setOriginalPrice(originalPrice);
        product.setSalePrice(salePrice);
        product.setDescription(description);


        // Gọi DAO để cập nhật
        productDAO.updateProduct(product);

        String[] submittedExistingImages = request.getParameterValues("existingImages");
        List<String> submittedImages = submittedExistingImages != null
                ? Arrays.asList(submittedExistingImages)
                : new ArrayList<>();

        List<String> originalImages = productDAO.getProductImages(id);
        List<String> deletedImages = new ArrayList<>(originalImages);
        deletedImages.removeAll(submittedImages);

        CloudinaryProxy cloudinary = new CloudinaryProxy();
        for (String imageUrl : deletedImages) {
            cloudinary.deleteImageFromCloudinary(imageUrl);
        }

        productDAO.deleteProductImages(id);
        productDAO.insertImages(submittedImages, id);


        Collection<Part> parts = request.getParts();
        List<String> uploadedImages = new ArrayList<>();
        for (Part part : parts) {
            if ("image".equals(part.getName()) && part.getSize() > 0) {
                File tempFile = File.createTempFile("upload_", part.getSubmittedFileName());
                part.write(tempFile.getAbsolutePath());

                try {
                    String imageUrl = cloudinary.uploadImage(tempFile); // dùng hàm có sẵn của bạn
                    uploadedImages.add(imageUrl);
                } finally {
                    tempFile.delete(); // Xóa file tạm sau khi upload
                }
            }
        }

        if (!uploadedImages.isEmpty()) {
            productDAO.insertImages(uploadedImages, id);
        }

    }
}
