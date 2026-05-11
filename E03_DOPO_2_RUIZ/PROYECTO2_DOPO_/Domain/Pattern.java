package Domain;

import java.io.Serializable;

/**
 * Define un patron de movimiento para enemigos tipo patrullero.
 * Consiste en una secuencia de puntos de ruta que el enemigo recorre en bucle.
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class Pattern implements Serializable {

    private static final long serialVersionUID = 1L;

    private double[] route;
    private String name;

    /**
     * Crea un patron con los waypoints dados.
     *
     * @param name nombre descriptivo del patron
     * @param route coordenadas alternadas x, y de cada punto
     */
    public Pattern(String name, double... route) {
        this.name = name;
        this.route = route;
    }

    /**
     * Retorna la ruta del patron.
     * @return arreglo de coordenadas [x1, y1, x2, y2, ...]
     */
    public double[] getRoute() { return route; }

    /**
     * Retorna el nombre del patron.
     * @return nombre descriptivo
     */
    public String getName() { return name; }

    /**
     * Crea un patron cuadrado alrededor del centro (cx, cy) con el radio dado.
     *
     * @param cx centro horizontal
     * @param cy centro vertical
     * @param radius radio del cuadrado
     * @return patron con ruta cuadrada
     */
    public static Pattern square(double cx, double cy, double radius) {
        return new Pattern("square",
            cx - radius, cy - radius,
            cx + radius, cy - radius,
            cx + radius, cy + radius,
            cx - radius, cy + radius
        );
    }

    /**
     * Crea un patron horizontal entre dos puntos.
     *
     * @param y fila del movimiento
     * @param x1 columna de inicio
     * @param x2 columna de fin
     * @return patron con ruta horizontal
     */
    public static Pattern horizontal(double y, double x1, double x2) {
        return new Pattern("horizontal", x1, y, x2, y);
    }

    /**
     * Crea un patron vertical entre dos puntos.
     *
     * @param x columna del movimiento
     * @param y1 fila de inicio
     * @param y2 fila de fin
     * @return patron con ruta vertical
     */
    public static Pattern vertical(double x, double y1, double y2) {
        return new Pattern("vertical", x, y1, x, y2);
    }
}
