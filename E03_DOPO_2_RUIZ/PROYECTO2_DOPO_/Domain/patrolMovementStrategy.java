package Domain;

/**
 * Estrategia de movimiento por patrullaje.
 * El enemigo sigue una ruta definida por varios puntos.
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class patrolMovementStrategy implements EnemyMovementStrategy {

    private static final long serialVersionUID = 1L;

    /**
     * Mueve el enemigo siguiendo su ruta de patrullaje.
     *
     * @param enemy enemigo a mover
     * @param table tablero del juego
     */
    public void move(Enemy enemy, Table table) {
        double[] patrolPath = enemy.getPatrolPath();

        if (patrolPath == null || patrolPath.length < 4) {
            return;
        }

        int patrolIndex = enemy.getPatrolIndex();

        double targetX = patrolPath[patrolIndex * 2];
        double targetY = patrolPath[patrolIndex * 2 + 1];

        double diffX = targetX - enemy.getX();
        double diffY = targetY - enemy.getY();

        if (Math.abs(diffX) < enemy.getSpeed() && Math.abs(diffY) < enemy.getSpeed()) {
            enemy.setX(targetX);
            enemy.setY(targetY);
            enemy.setPatrolIndex((patrolIndex + 1) % (patrolPath.length / 2));
        } else if (Math.abs(diffX) > Math.abs(diffY)) {
            enemy.setX(enemy.getX() + Math.signum(diffX) * enemy.getSpeed());
        } else {
            enemy.setY(enemy.getY() + Math.signum(diffY) * enemy.getSpeed());
        }
    }
}