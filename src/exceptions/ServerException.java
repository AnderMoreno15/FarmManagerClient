/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * Exception thrown to indicate an issue on the server side, such as an internal error or unexpected failure.
 * This exception is typically used to handle errors that occur during server communication or processing.
 * 
 * Usage Example:
 * if (serverError) {
 *     throw new ServerException("An error occurred on the server");
 * }
 * 
 * @see Exception
 * 
 * @author Aitziber
 */
public class ServerException extends Exception {

    /**
     * Constructs a new ServerException with null as its detail message.
     */
    public ServerException() {
    }

    /**
     * Constructs a new ServerException with the specified detail message.
     *
     * @param msg the detail message
     */
    public ServerException(String msg) {
        super(msg);
    }
}
