package be.fastrada.Exception;

public class FastradaException extends Exception {
    public FastradaException() {
    }

    public FastradaException(String detailMessage) {
        super(detailMessage);
    }

    public FastradaException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
