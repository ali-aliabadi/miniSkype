package constants;

/**
 * methods need in communication between client and client handler
 *  notificationlistener : for hearing push notifications (must be a thread)
 */
public interface Communication {


    void notificationListener();

    void login();
    void signup();

}
