package Exceptions;

public class RepoException extends RuntimeException {
    /*
    * Exceptiile folosite pentru functiile din repo
    * */

    public RepoException(String message) {
        super(message);
    }
}
