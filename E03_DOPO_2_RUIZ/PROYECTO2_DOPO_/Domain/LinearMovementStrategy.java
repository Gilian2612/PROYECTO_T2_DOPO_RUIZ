package Domain;

/**
 * Estrategia de movimiento linear (con rebote)
 * Usada por enemigos basicos y rapidos.
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class LinearMovementStrategy implements EnemyMovementStrategy {

    private static final long serialVersionUID = 1L;

    /**
     * Mueve el enemigo en linea recta y cambia la direccion al tocar bordes o paredes.
     *
     * @param enemy enemigo a mover
     * @param table tablero del juego
     */
    public void move(Enemy enemy, Table table) {
        double nx = enemy.getX() + enemy.getDx() * enemy.getSpeed();
        double ny = enemy.getY() + enemy.getDy() * enemy.getSpeed();

        if (nx <= 1 || nx >= table.getCols() - 2) {
            enemy.setDx(enemy.getDx() * -1);
            nx = enemy.getX() + enemy.getDx() * enemy.getSpeed();
        }

        if (ny <= 0 || ny >= table.getRows() - 1) {
            enemy.setDy(enemy.getDy() * -1);
            ny = enemy.getY() + enemy.getDy() * enemy.getSpeed();
        }

        if (table.isWall((int) ny, (int) nx)) {
            enemy.setDx(enemy.getDx() * -1);
            enemy.setDy(enemy.getDy() * -1);
            return;
        }

        enemy.setX(nx);
        enemy.setY(ny);
    }
}