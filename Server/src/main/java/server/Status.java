package server;

/**
 * Status for the client
 */
public enum Status {
    /**
     * Already login in. Available to send and read message
     */
    LOGIN,
    /**
     * Already logout. Unavailable to send and read message
     */
    LOGOUT,
    /**
     * Need to relogin due to message limit per session. Unavailable to send and read message
     */
    RELOGIN,
    /**
     * Already to login. Available to read message and Unavailable to send message due to message limit per second
     */
    IGNORE
}
