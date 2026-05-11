package Domain;
import java.awt.Color;
/**
 * Representa una moneda recolectable en el tablero de juego.
 * Tipos: YELLOW (estandar), SKIN_RED, SKIN_BLUE, SKIN_GREEN
 * (cambian temporalmente el skin del jugador).
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class Coin extends Rectangle {

    private static final long serialVersionUID = 1L;

    /** Tipos de moneda disponibles. */
    public enum CoinType { YELLOW, SKIN_RED, SKIN_BLUE, SKIN_GREEN }

    private CoinType type;
    private boolean collected;

    /**
     * Crea una moneda en la posicion (x, y) con el tipo indicado.
     *
     * @param x columna de la moneda
     * @param y fila de la moneda
     * @param type tipo de moneda
     */
    public Coin(int x, int y, CoinType type) {
        super(x, y, 1, 1);
        this.type = type;
        this.collected = false;
    }

    /**
     * Recolecta esta moneda y aplica su efecto al jugador.
     * Las monedas de skin cambian temporalmente el skin del jugador.
     *
     * @param player jugador que recolecta la moneda
     */
    public void collect(Player player) {
        if (collected) {
            return;
        }
        collected = true;
        player.collectCoin();

        switch (type) {
            case SKIN_RED:
                player.applyTemporarySkin(Player.Skin.BLINKY);
                break;
            case SKIN_BLUE:
                player.applyTemporarySkin(Player.Skin.INKY);
                break;
            case SKIN_GREEN:
                player.applyTemporarySkin(Player.Skin.CLYDE);
                break;
            default:
                break;
        }
    }

    /**
     * Verifica si el jugador esta sobre esta moneda y no ha sido recolectada.
     *
     * @param player jugador a verificar
     * @return true si la moneda esta en la posicion del jugador y no fue recolectada
     */
    public boolean isAtPlayer(Player player) {
        return !collected && (int) x == player.getCol() && (int) y == player.getRow();
    }

    /** @return true si la moneda ya fue recolectada */
    public boolean isCollected() { return collected; }

    /** @return tipo de moneda */
    public CoinType getType() { return type; }

    /** @return columna como entero */
    public int getCol() { return (int) x; }

    /** @return fila como entero */
    public int getRow() { return (int) y; }

    /**
     * Retorna el color de visualizacion segun el tipo de moneda.
     * @return color de la moneda
     */
    public Color getColor() {
        switch (type) {
            case YELLOW:     return Color.YELLOW;
            case SKIN_RED:   return Color.RED;
            case SKIN_BLUE:  return new Color(0, 100, 255);
            case SKIN_GREEN: return new Color(0, 180, 0);
            default:         return Color.YELLOW;
        }
    }
}
