package Domain;

/**
 * Estrategia de movimiento vertical con rebote.
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class verticalMovementStrategy implements EnemyMovementStrategy {

    private static final long serialVersionUID = 1L;

    /**
     * Mueve el enemigo verticalmente y cambia la direccion al tocar los bordes.
     *
     * @param enemy enemigo a mover
     * @param table tablero del juego
     */
    public void move(Enemy enemy, Table table) {
        double ny = enemy.getY() + enemy.getDy() * enemy.getSpeed();

        if (ny <= 0 || ny >= table.getRows() - 1) {
            enemy.setDy(enemy.getDy() * -1);
            ny = enemy.getY() + enemy.getDy() * enemy.getSpeed();
        }

        enemy.setY(ny);
    }
}