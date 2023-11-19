package utn.frc.sistemas.services.implementations.exeptions;

public class EstacionNotFoundException extends RuntimeException{
    public EstacionNotFoundException(String message) {
        super(message);
        System.out.println(message);
    }

}
