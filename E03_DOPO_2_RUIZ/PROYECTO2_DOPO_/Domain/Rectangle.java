package Domain;

import java.io.Serializable;

/**
 * Clase abstracta base que representa un area rectangular en el tablero de juego.
 * Todas las entidades del juego (jugadores, enemigos, monedas, zonas) heredan de esta clase.
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public abstract class Rectangle implements Serializable {

    private static final long serialVersionUID = 1L;

    protected double x;
    protected double y;
    protected double height;
    protected double broad;

    /**
     * Crea un rectangulo con la posicion y dimensiones dadas.
     *
     * @param x coordenada horizontal
     * @param y coordenada vertical
     * @param height alto del rectangulo
     * @param broad ancho del rectangulo
     */
    public Rectangle(double x, double y, double height, double broad) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.broad = broad;
    }

    /**
     * Retorna la coordenada x.
     * @return posicion horizontal
     */
    public double getX() { return x; }

    /**
     * Retorna la coordenada y.
     * @return posicion vertical
     */
    public double getY() { return y; }

    /**
     * Retorna la altura del rectangulo.
     * @return altura
     */
    public double getHeight() { return height; }

    /**
     * Retorna el ancho del rectangulo.
     * @return ancho
     */
    public double getBroad() { return broad; }

    /**
     * Establece la coordenada x.
     * @param x nueva posicion horizontal
     */
    public void setX(double x) { this.x = x; }

    /**
     * Establece la coordenada y.
     * @param y nueva posicion vertical
     */
    public void setY(double y) { this.y = y; }

    /**
     * Verifica si el punto (px, py) esta dentro de este rectangulo.
     *
     * @param px coordenada x del punto
     * @param py coordenada y del punto
     * @return true si el punto esta contenido
     */
    public boolean contains(double px, double py) {
        return px >= x && px <= x + broad && py >= y && py <= y + height;
    }

    /**
     * Verifica si este rectangulo se intersecta con otro.
     *
     * @param other otro rectangulo
     * @return true si hay interseccion
     */
    public boolean intersects(Rectangle other) {
        return this.x < other.x + other.broad &&
               this.x + this.broad > other.x &&
               this.y < other.y + other.height &&
               this.y + this.height > other.y;
    }
}
