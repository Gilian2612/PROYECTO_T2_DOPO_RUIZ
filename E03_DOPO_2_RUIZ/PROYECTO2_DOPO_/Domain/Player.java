package Domain;

import java.awt.Color;

/**
 * Representa un jugador en el tablero de juego.
 * Cada jugador tiene un skin que determina su velocidad, tamano y habilidades.
 * Skins disponibles: BLINKY (rojo, estandar), INKY (azul, rapido y grande),
 * CLYDE (verde, absorbe primer golpe).
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class Player extends Rectangle {

    private static final long serialVersionUID = 1L;

    /** Tipos de skin disponibles para el jugador. */
    public enum Skin { BLINKY, INKY, CLYDE }

    private Skin skin;
    private Skin originalSkin;
    private double speed;
    private double originalSpeed;
    private int deaths;
    private int startX;
    private int startY;
    private boolean hasShield;
    private int coinsCollected;
    private String name;
    private boolean temporarySkin;

    /**
     * Crea un jugador con el skin dado en la posicion (x, y).
     *
     * @param x columna inicial
     * @param y fila inicial
     * @param skin tipo de skin del jugador
     */
    public Player(int x, int y, Skin skin) {
        super(x, y, 1, 1);
        this.skin = skin;
        this.originalSkin = skin;
        this.startX = x;
        this.startY = y;
        this.deaths = 0;
        this.coinsCollected = 0;
        this.temporarySkin = false;
        this.name = skin.name();
        applySkinAttributes(skin);
    }

    /**
     * Aplica los atributos correspondientes a cada tipo de skin.
     *
     * @param s skin a aplicar
     */
    private void applySkinAttributes(Skin s) {
        switch (s) {
            case BLINKY:
                this.speed = 1.0;
                this.broad = 1.0;
                this.height = 1.0;
                this.hasShield = false;
                break;
            case INKY:
                this.speed = 1.5;
                this.broad = 1.5;
                this.height = 1.5;
                this.hasShield = false;
                break;
            case CLYDE:
                this.speed = 1.0;
                this.broad = 1.0;
                this.height = 1.0;
                this.hasShield = true;
                break;
        }
        this.originalSpeed = this.speed;
    }

    /**
     * Mueve al jugador por (dx, dy) escalado por su velocidad.
     * Valida limites del tablero y paredes antes de moverse.
     *
     * @param dx delta horizontal (-1, 0, 1)
     * @param dy delta vertical (-1, 0, 1)
     * @param table tablero de juego para validar limites
     * @return true si el movimiento fue aplicado
     */
    public boolean move(int dx, int dy, Table table) {
        double nx = x + dx * speed;
        double ny = y + dy * speed;
        if (table.inBounds(nx, ny) && !table.isWall((int) ny, (int) nx)) {
            this.x = nx;
            this.y = ny;
            return true;
        }
        return false;
    }

    /**
     * Maneja el impacto con un enemigo.
     * Clyde absorbe el primer golpe perdiendo velocidad en vez de morir.
     *
     * @return true si el jugador murio, false si absorbio el golpe
     */
    public boolean hitByEnemy() {
        if (hasShield) {
            hasShield = false;
            speed = 0.7;
            return false;
        }
        deaths++;
        respawn();
        return true;
    }

    /**
     * Reaparece al jugador en su punto de inicio actual.
     */
    public void respawn() {
        this.x = startX;
        this.y = startY;
        if (temporarySkin) {
            restoreOriginalSkin();
        }
        if (originalSkin == Skin.CLYDE) {
            hasShield = true;
            speed = originalSpeed;
        }
    }

    /**
     * Actualiza el punto de reaparicion del jugador (al llegar a zona intermedia).
     *
     * @param cx columna del checkpoint
     * @param cy fila del checkpoint
     */
    public void setCheckpoint(int cx, int cy) {
        this.startX = cx;
        this.startY = cy;
    }

    /**
     * Cambia temporalmente el skin del jugador (por monedas de skin).
     *
     * @param newSkin nuevo skin temporal
     */
    public void applyTemporarySkin(Skin newSkin) {
        this.temporarySkin = true;
        this.skin = newSkin;
        applySkinAttributes(newSkin);
    }

    /**
     * Restaura el skin original del jugador.
     */
    public void restoreOriginalSkin() {
        this.temporarySkin = false;
        this.skin = originalSkin;
        applySkinAttributes(originalSkin);
    }

    /**
     * Incrementa el contador de monedas recolectadas.
     */
    public void collectCoin() { coinsCollected++; }

    /** @return skin actual del jugador */
    public Skin getSkin() { return skin; }

    /** @return skin original del jugador */
    public Skin getOriginalSkin() { return originalSkin; }

    /** @return velocidad actual */
    public double getSpeed() { return speed; }

    /** @return contador de muertes */
    public int getDeaths() { return deaths; }

    /** @return monedas recolectadas */
    public int getCoinsCollected() { return coinsCollected; }

    /** @return columna de inicio */
    public int getStartX() { return startX; }

    /** @return fila de inicio */
    public int getStartY() { return startY; }

    /** @return true si el jugador tiene escudo activo */
    public boolean hasShield() { return hasShield; }

    /** @return nombre del jugador */
    public String getName() { return name; }

    /** @return columna actual como entero */
    public int getCol() { return (int) x; }

    /** @return fila actual como entero */
    public int getRow() { return (int) y; }

    /**
     * Establece el nombre del jugador.
     * @param n nuevo nombre
     */
    public void setName(String n) { this.name = n; }

    /**
     * Retorna el color de visualizacion segun el skin actual.
     * @return color del skin
     */
    public Color getSkinColor() {
        switch (skin) {
            case BLINKY: return Color.RED;
            case INKY:   return new Color(0, 100, 255);
            case CLYDE:  return new Color(0, 180, 0);
            default:     return Color.RED;
        }
    }
}

