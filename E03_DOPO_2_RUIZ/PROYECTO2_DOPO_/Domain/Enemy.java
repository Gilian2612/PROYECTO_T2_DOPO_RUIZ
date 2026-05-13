package Domain;

import java.awt.Color;

/**
 * Representa un enemigo en el tablero de juego.
 * Usa el patron Strategy para delegar su movimiento a una estrategia concreta.
 * Tipos: BASIC, PATROL, VERTICAL y FAST.
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
    private EnemyMovementStrategy movementStrategy;

    /**
     * Crea un enemigo en la posicion indicada, con direccion y tipo.
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
        configureStrategy(type);
    }

    /**
     * Configura la velocidad y estrategia de movimiento segun el tipo de enemigo.
     *
     * @param type tipo de enemigo
     */
    private void configureStrategy(EnemyType type) {
        switch (type) {
            case FAST:
                this.speed = 2.0;
                this.movementStrategy = new LinearMovementStrategy();
                break;

            case VERTICAL:
                this.dx = 0;
                this.speed = 1.0;
                this.movementStrategy = new verticalMovementStrategy();
                break;

            case PATROL:
                this.speed = 1.0;
                this.movementStrategy = new patrolMovementStrategy();
                break;

            case BASIC:
            default:
                this.speed = 1.0;
                this.movementStrategy = new LinearMovementStrategy();
                break;
        }
    }

    /**
     * Mueve al enemigo usando su estrategia de movimiento.
     *
     * @param table tablero de juego
     */
    public void move(Table table) {
        movementStrategy.move(this, table);
    }

    /**
     * Cambia la estrategia de movimiento del enemigo.
     * Permite modificar el comportamiento sin crear otro enemigo.
     *
     * @param strategy nueva estrategia de movimiento
     */
    public void setMovementStrategy(EnemyMovementStrategy strategy) {
        this.movementStrategy = strategy;
    }

    /** @return tipo de enemigo */
    public EnemyType getType() {
        return type;
    }

    /** @return columna actual como entero */
    public int getCol() {
        return (int) x;
    }

    /** @return fila actual como entero */
    public int getRow() {
        return (int) y;
    }

    /** @return direccion horizontal */
    public double getDx() {
        return dx;
    }

    /** @param dx nueva direccion horizontal */
    public void setDx(double dx) {
        this.dx = dx;
    }

    /** @return direccion vertical */
    public double getDy() {
        return dy;
    }

    /** @param dy nueva direccion vertical */
    public void setDy(double dy) {
        this.dy = dy;
    }

    /** @return velocidad del enemigo */
    public double getSpeed() {
        return speed;
    }

    /** @param speed nueva velocidad */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /** @return ruta de patrullaje */
    public double[] getPatrolPath() {
        return patrolPath;
    }

    /**
     * Establece la ruta de patrullaje para enemigos tipo PATROL.
     *
     * @param path arreglo de coordenadas [x1, y1, x2, y2, ...]
     */
    public void setPatrolPath(double[] path) {
        this.patrolPath = path;
    }

    /** @return indice actual dentro de la ruta de patrullaje */
    public int getPatrolIndex() {
        return patrolIndex;
    }

    /** @param patrolIndex nuevo indice de patrullaje */
    public void setPatrolIndex(int patrolIndex) {
        this.patrolIndex = patrolIndex;
    }

    /**
     * Retorna el color de visualizacion segun el tipo de enemigo.
     *
     * @return color del enemigo
     */
    public Color getColor() {
        switch (type) {
            case FAST:
                return new Color(0, 0, 200);
            case PATROL:
                return new Color(0, 50, 180);
            default:
                return new Color(0, 80, 255);
        }
    }
}