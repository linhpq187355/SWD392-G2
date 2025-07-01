package models;

import java.time.LocalDate;

public class Image {
    private int imageId;
    private int productId;
    private String url;
    private boolean isThumbnail;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Image() {
    }

    public Image(int imageId, int productId, String url, boolean isThumbnail, LocalDate createdAt, LocalDate updatedAt) {
        this.imageId = imageId;
        this.productId = productId;
        this.url = url;
        this.isThumbnail = isThumbnail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isThumbnail() {
        return isThumbnail;
    }

    public void setThumbnail(boolean thumbnail) {
        isThumbnail = thumbnail;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", productId=" + productId +
                ", url='" + url + '\'' +
                ", isThumbnail=" + isThumbnail +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
