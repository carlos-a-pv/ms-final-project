package co.edu.uniquindio.exception;

public class DepartmentUnavailableException extends RuntimeException{

    public DepartmentUnavailableException(Long id){
        super("Department service unavailable");
    }
}
