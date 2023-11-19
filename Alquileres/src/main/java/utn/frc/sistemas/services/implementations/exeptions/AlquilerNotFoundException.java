package utn.frc.sistemas.services.implementations.exeptions;

public class AlquilerNotFoundException extends RuntimeException {
    public AlquilerNotFoundException(String message) {
        super(message);System.out.println(message);
    }
}