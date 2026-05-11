package Domain;

/**
 * Excepcion personalizada para errores del juego The DOPO Hardest Game.
 * Define mensajes constantes para los distintos tipos de error que pueden ocurrir.
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class DOPOException extends Exception {

    public static final String FILE_NOT_FOUND = "Archivo de configuracion no encontrado: ";
    public static final String INVALID_CONFIG = "Formato de configuracion invalido: ";
    public static final String INVALID_MOVE = "Movimiento invalido del jugador: ";
    public static final String LEVEL_NOT_FOUND = "Nivel no encontrado: ";
    public static final String INVALID_SKIN = "Seleccion de skin invalida: ";
    public static final String SAVE_ERROR = "Error al guardar el estado del juego: ";
    public static final String LOAD_ERROR = "Error al cargar el estado del juego: ";
    public static final String OUT_OF_BOUNDS = "Posicion fuera de los limites del tablero.";
    public static final String TIME_EXPIRED = "El tiempo del nivel ha expirado.";

    /**
     * Crea una excepcion con el mensaje indicado.
     *
     * @param message mensaje descriptivo del error
     */
    public DOPOException(String message) {
        super(message);
    }
}
