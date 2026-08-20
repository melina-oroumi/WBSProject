//
// WBSException.java: Handles invalid WBS data and operations

public class WBSException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public WBSException(String message) {
        super(message);
    }

    public WBSException(String message, Throwable cause) {
        super(message, cause);
    }
}