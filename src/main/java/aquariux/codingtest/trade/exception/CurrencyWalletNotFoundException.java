package aquariux.codingtest.trade.exception;

public class CurrencyWalletNotFoundException extends Exception {

    private static final long serialVersionUID = 6831074223692986747L;

    public CurrencyWalletNotFoundException() {
        super();
    }

    public CurrencyWalletNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CurrencyWalletNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyWalletNotFoundException(String message) {
        super(message);
    }

    public CurrencyWalletNotFoundException(Throwable cause) {
        super(cause);
    }
}
