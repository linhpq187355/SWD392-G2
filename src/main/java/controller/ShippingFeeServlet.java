package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.GhnShippingService;

import java.io.BufferedReader;
import java.io.IOException;
@WebServlet("/calculateShippingFee")
public class ShippingFeeServlet extends HttpServlet {
    private final GhnShippingService ghnShippingService = new GhnShippingService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // --- Đọc JSON từ request body ---
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);

            JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();

            int toDistrictId = json.get("districtId").getAsInt();
            String toWardCode = json.get("wardCode").getAsString();

            // --- Log để kiểm tra ---
            System.out.println("✅ Nhận districtId = " + toDistrictId);
            System.out.println("✅ Nhận wardCode = " + toWardCode);
            // --- GHN constants ---
            int fromDistrictId = 1808  ;
            String fromWardCode;
            fromWardCode = "1B1919";
            int serviceId = ghnShippingService.getAvailableServiceId(fromDistrictId, toDistrictId);
            if (serviceId == -1) {
                throw new IOException("GHN không có dịch vụ vận chuyển giữa 2 quận đã chọn");
            }
            int height = 20, length = 30, width = 15, weight = 3000;
            int insuranceValue = 1000000;

            System.out.println("➡️ service_id = " + serviceId);

            // --- Gọi GHN API tính phí ---
            int fee = ghnShippingService.calculateShippingFee(
                    fromDistrictId,fromWardCode, serviceId, toDistrictId, toWardCode,
                    height, length, weight, width, insuranceValue
            );

            // --- Trả JSON ---
            response.setContentType("application/json");
            response.getWriter().write("{\"fee\":" + fee + "}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }
}



