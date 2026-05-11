package Domain;

import java.awt.Color;

/**
 * Representa un elemento especial en el tablero de juego.
 * Tipos: WALL (no se puede atravesar), LIFE_SOURCE (vida extra),
 * BOMB (destruye cualquier elemento que pase).
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class SpecialElement extends Rectangle {

    private static final long serialVersionUID = 1L;
    /** Tipos de elemento especial. */
    public enum ElementType { WALL, LIFE_SOURCE, BOMB }
    private ElementType type;
    private boolean active;

    /**
     * Crea un elemento especial en la posicion (x, y) con el tipo dado.
     *
     * @param x columna del elemento
     * @param y fila del elemento
     * @param type tipo de elemento especial
     */
    public SpecialElement(int x, int y, ElementType type) {
        super(x, y, 1, 1);
        this.type = type;
        this.active = true;
    }

    /**
     * Aplica el efecto del elemento al jugador.
     * LIFE_SOURCE desaparece al usarse, BOMB mata al jugador.
     *
     * @param player jugador afectado
     * @return true si el elemento fue consumido
     */
    public boolean applyEffect(Player player) {
        if (!active) {
            return false;
        }
        
        switch (type) {
            case LIFE_SOURCE:
                active = false;
                return true;
            case BOMB:
                player.hitByEnemy();
                active = false;
                return true;
            default:
                return false;
        }
    }

    /**
     * Verifica si un enemigo activa esta bomba.
     *
     * @param enemy enemigo a verificar
     * @return true si el enemigo debe ser destruido
     */
    public boolean affectsEnemy(Enemy enemy) {
        if (!active || type != ElementType.BOMB) {
            return false;
        }
        if ((int) x == enemy.getCol() && (int) y == enemy.getRow()) {
            active = false;
            return true;
        }
        return false;
    }

    /** @return tipo del elemento */
    public ElementType getType() { return type; }

    /** @return true si el elemento sigue activo */
    public boolean isActive() { return active; }

    /** @return columna como entero */
    public int getCol() { return (int) x; }

    /** @return fila como entero */
    public int getRow() { return (int) y; }

    /**
     * Retorna el color de visualizacion segun el tipo.
     * @return color del elemento
     */
    public Color getColor() {
        switch (type) {
            case WALL:        return Color.DARK_GRAY;
            case LIFE_SOURCE: return new Color(255, 105, 180);
            case BOMB:        return new Color(255, 69, 0);
            default:          return Color.GRAY;
        }
    }
}
