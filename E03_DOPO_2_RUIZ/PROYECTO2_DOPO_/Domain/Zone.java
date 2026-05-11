package Domain;

/**
 * Representa una zona segura en el tablero de juego.
 * Tipos: INITIAL (inicio), INTERMEDIATE (checkpoint), FINAL (meta).
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class Zone extends Rectangle {

    private static final long serialVersionUID = 1L;

    /** Tipos posibles de zona segura. */
    public enum ZoneType { INITIAL, INTERMEDIATE, FINAL }

    private ZoneType type;

    /**
     * Crea una zona segura con posicion, dimensiones y tipo.
     *
     * @param x columna inicial de la zona
     * @param y fila inicial de la zona
     * @param height alto en celdas
     * @param broad ancho en celdas
     * @param type tipo de zona segura
     */
    public Zone(int x, int y, int height, int broad, ZoneType type) {
        super(x, y, height, broad);
        this.type = type;
    }
    
    /**
     * Retorna el tipo de zona segura.
     * @return tipo de zona
     */
    public ZoneType getType() { return type; }

    /**
     * Verifica si la celda (col, row) esta dentro de esta zona.
     *
     * @param col columna a verificar
     * @param row fila a verificar
     * @return true si la celda esta dentro de la zona
     */
    public boolean containsCell(int col, int row) {
        return col >= x && col < x + broad && row >= y && row < y + height;
    }
}

