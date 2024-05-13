package ru.bebriki.sstuconnect.photo.exceptions;

public class ImageDownloadException extends RuntimeException {

    public ImageDownloadException() {
        super();
    }

    public ImageDownloadException(String message) {
        super(message);
    }

    public ImageDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageDownloadException(Throwable cause) {
        super(cause);
    }

    protected ImageDownloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
