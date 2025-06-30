package services;

/**
 * Custom exception cho các lỗi liên quan đến GHN.
 */
class GhnException extends Exception {
    private boolean isSystemUnavailable;

    public GhnException(String message) {
        super(message);
        this.isSystemUnavailable = false;
    }

    public GhnException(String message, boolean isSystemUnavailable) {
        super(message);
        this.isSystemUnavailable = isSystemUnavailable;
    }

    public boolean isSystemUnavailable() {
        return isSystemUnavailable;
    }
}
