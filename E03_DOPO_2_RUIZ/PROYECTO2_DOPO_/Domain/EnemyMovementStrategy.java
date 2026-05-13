package Domain;

import java.io.Serializable;

/**
 * Estrategia de movimiento para enemigos.
 * Permite cambiar el comportamiento de movimiento sin modificar la clase Enemy
 * uso de patrón strategy 
 *
 * @author William Santiago Ruiz Medina
 * @version
 */
public interface EnemyMovementStrategy extends Serializable {

    /**
     * Mueve un enemigo segun la estrategia concreta.
     *
     * @param enemy enemigo a mover
     * @param table tablero del juego
     */
    void move(Enemy enemy, Table table);
}