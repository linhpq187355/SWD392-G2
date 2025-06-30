package services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class CloudinaryProxy {
    private Cloudinary cloudinary;

    public CloudinaryProxy() {
        try{
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
            props.load(input);

            String cloudName = props.getProperty("cloudinary.cloud_name");
            String apiKey = props.getProperty("cloudinary.api_key");
            String apiSecret = props.getProperty("cloudinary.api_secret");
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret
            ));
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public String uploadImage(File file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }

    public void deleteImageFromCloudinary(String imageUrl) throws IOException {
        String publicId = extractPublicId(imageUrl);
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        System.out.println("Delete result: " + result);
    }

    private String extractPublicId(String imageUrl) {
        try {
            String path = imageUrl.split("/upload/")[1];
            return path.substring(path.indexOf("/") + 1, path.lastIndexOf('.')); // folder/sample
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Cloudinary URL: " + imageUrl);
        }
    }
}
