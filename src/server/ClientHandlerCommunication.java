package server;

/**
 * methods need in clienthandler class to communicate with client
 *  commandHandler : handle client commands (like client request for signup)
 *  login : (must have a pair method in client)
 *  signup : (must have a pair method in client)
 *  unknownCommand : for bad (unexisted) command from client, sending proper answer.
 */
public interface ClientHandlerCommunication {


    void commandHandler();

    void login();
    void signup();

    void unknownCommand();

}
