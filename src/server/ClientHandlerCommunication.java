package server;

/**
 * methods need in clienthandler class to communicate with client
 *  commandHandler : handle client commands (like client request for signup)
 *  unknownCommand : for bad (unexisted) command from client, sending proper answer.
 */
public interface ClientHandlerCommunication {


    void commandHandler();

    void unknownCommand();

}
