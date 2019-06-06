package server;

/**
 * methods need in clienthandler class to communicate with client
 *  notficationlistener : for hearing push notfications (must be a thread)
 *  commandHandler : handle client commands (like client request for signup)
 *  login : (must have a pair method in client)
 *  signup : (must have a pair method in client)
 */
public interface ClientHandlerCommunication {


    void notificationListener();
    void commandHandler();

    void login();
    void signup();


}
