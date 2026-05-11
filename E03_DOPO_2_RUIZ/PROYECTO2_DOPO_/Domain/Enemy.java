package Domain;

import java.awt.Color;

/**
 * Representa un enemigo en el tablero de juego.
 * Tipos: BASIC (lineal), PATROL (ruta geometrica), VERTICAL (solo vertical),
 * FAST (doble velocidad).
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class Enemy extends Rectangle {

    private static final long serialVersionUID = 1L;

    /** Tipos de enemigo disponibles. */
    public enum EnemyType { BASIC, PATROL, VERTICAL, FAST }

    private EnemyType type;
    private double dx;
    private double dy;
    private double speed;
    private double[] patrolPath;
    private int patrolIndex;

    /**
     * Crea un enemigo en la posicion (x, y) con la direccion (dx, dy).
     *
     * @param x posicion horizontal inicial
     * @param y posicion vertical inicial
     * @param dx direccion horizontal
     * @param dy direccion vertical
     * @param type tipo de enemigo
     */
    public Enemy(double x, double y, double dx, double dy, EnemyType type) {
        super(x, y, 1, 1);
        this.dx = dx;
        this.dy = dy;
        this.type = type;
        this.patrolIndex = 0;

        switch (type) {
            case FAST:
                this.speed = 2.0;
                break;
            case VERTICAL:
                this.dx = 0;
                this.speed = 1.0;
                break;
            default:
                this.speed = 1.0;
                break;
        }
    }

    /**
     * Mueve al enemigo un paso segun su tipo, rebotando en paredes.
     *
     * @param table tablero de juego para verificar limites
     */
    public void move(Table table) {
        switch (type) {
            case BASIC:
            case FAST:
                moveLinear(table);
                break;
            case VERTICAL:
                moveVertical(table);
                break;
            case PATROL:
                movePatrol();
                break;
        }
    }

    /**
     * Movimiento lineal con rebote en paredes.
     *
     * @param table tablero de juego
     */
    private void moveLinear(Table table) {
        double nx = x + dx * speed;
        double ny = y + dy * speed;

        if (nx <= 1 || nx >= table.getCols() - 2) {
            dx *= -1;
            nx = x + dx * speed;
        }
        if (ny <= 0 || ny >= table.getRows() - 1) {
            dy *= -1;
            ny = y + dy * speed;
        }
        if (table.isWall((int) ny, (int) nx)) {
            dx *= -1;
            dy *= -1;
            return;
        }

        this.x = nx;
        this.y = ny;
    }

    /**
     * Movimiento exclusivamente vertical con rebote.
     *
     * @param table tablero de juego
     */
    private void moveVertical(Table table) {
        double ny = y + dy * speed;
        if (ny <= 0 || ny >= table.getRows() - 1) {
            dy *= -1;
            ny = y + dy * speed;
        }
        this.y = ny;
    }

    /**
     * Movimiento siguiendo una ruta de patrullaje predefinida.
     */
    private void movePatrol() {
        if (patrolPath == null || patrolPath.length < 4) {
            return;
        }
        double targetX = patrolPath[patrolIndex * 2];
        double targetY = patrolPath[patrolIndex * 2 + 1];

        double diffX = targetX - x;
        double diffY = targetY - y;

        if (Math.abs(diffX) < speed && Math.abs(diffY) < speed) {
            x = targetX;
            y = targetY;
            patrolIndex = (patrolIndex + 1) % (patrolPath.length / 2);
        } else if (Math.abs(diffX) > Math.abs(diffY)) {
            x += Math.signum(diffX) * speed;
        } else {
            y += Math.signum(diffY) * speed;
        }
    }

    /** @return tipo de enemigo */
    public EnemyType getType() { return type; }

    /** @return columna actual como entero */
    public int getCol() { return (int) x; }

    /** @return fila actual como entero */
    public int getRow() { return (int) y; }

    /**
     * Establece la ruta de patrullaje para enemigos tipo PATROL.
     * @param path arreglo de coordenadas [x1, y1, x2, y2, ...]
     */
    public void setPatrolPath(double[] path) { this.patrolPath = path; }

    /**
     * Retorna el color de visualizacion segun el tipo de enemigo.
     * @return color del enemigo
     */
    public Color getColor() {
        switch (type) {
            case FAST:   return new Color(0, 0, 200);
            case PATROL: return new Color(0, 50, 180);
            default:     return new Color(0, 80, 255);
        }
    }
}
