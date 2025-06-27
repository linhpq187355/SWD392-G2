package services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GhnShippingService {

    private static final String API_URL = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
    private static final String TOKEN = "a18e846b-51b8-11f0-928a-1a690f81b498";
    private static final String SHOP_ID = "5857017";


    public int calculateShippingFee(int fromDistrictId, String fromWardCode,
                                    int serviceId, int toDistrictId, String toWardCode,
                                    int height, int length, int weight, int width, int insuranceValue) throws IOException {


        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Token", TOKEN);
        conn.setRequestProperty("ShopId", SHOP_ID);
        conn.setDoOutput(true);

        String jsonInput = String.format("""
        {
          "from_district_id": %d,
          "from_ward_code": "%s",
          "service_id": %d,
          "to_district_id": %d,
          "to_ward_code": "%s",
          "height": %d,
          "length": %d,
          "weight": %d,
          "width": %d,
          "insurance_value": %d
        }
        """, fromDistrictId, fromWardCode, serviceId, toDistrictId, toWardCode,
                height, length, weight, width, insuranceValue);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int status = conn.getResponseCode();
        InputStream inputStream = (status < HttpURLConnection.HTTP_BAD_REQUEST) ? conn.getInputStream() : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }

        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();

        if (!jsonResponse.get("code").getAsString().equals("200")) {
            throw new IOException("GHN API error: " + jsonResponse.get("message").getAsString());
        }

        JsonObject data = jsonResponse.getAsJsonObject("data");
        return data.get("total").getAsInt();
    }
    public int getAvailableServiceId(int fromDistrictId, int toDistrictId) throws IOException {
        URL url = new URL("https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Token", TOKEN);
        conn.setDoOutput(true);

        String jsonInput = String.format("""
        {
            "shop_id": %s,
            "from_district": %d,
            "to_district": %d
        }
        """, SHOP_ID, fromDistrictId, toDistrictId);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes("utf-8"));
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) response.append(line.trim());

        JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();
        if (!json.get("code").getAsString().equals("200")) return -1;

        JsonArray services = json.getAsJsonArray("data");
        if (services.size() == 0) return -1;

        return services.get(0).getAsJsonObject().get("service_id").getAsInt();
    }

}
