package Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa el tablero de juego con zonas seguras y paredes.
 * Extiende Rectangle ya que el tablero en si es un area rectangular.
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class Table extends Rectangle {

    private static final long serialVersionUID = 1L;

    private int rows;
    private int cols;
    private boolean[][] walls;
    private List<Zone> zones;

    /**
     * Crea el tablero de juego con las dimensiones dadas.
     * La posicion del rectangulo es (0,0) y su tamano coincide con rows x cols.
     *
     * @param rows numero de filas
     * @param cols numero de columnas
     */
    public Table(int rows, int cols) {
        super(0, 0, rows, cols);
        this.rows = rows;
        this.cols = cols;
        this.walls = new boolean[rows][cols];
        this.zones = new ArrayList<>();
    }

    /**
     * Retorna el numero de filas.
     * @return filas del tablero
     */
    public int getRows() { return rows; }

    /**
     * Retorna el numero de columnas.
     * @return columnas del tablero
     */
    public int getCols() { return cols; }

    /**
     * Verifica si la posicion (px, py) esta dentro de los limites del tablero.
     *
     * @param px coordenada x
     * @param py coordenada y
     * @return true si la posicion esta dentro del tablero
     */
    public boolean inBounds(double px, double py) {
        return px >= 0 && px < cols && py >= 0 && py < rows;
    }

    /**
     * Establece o quita una pared en la posicion indicada.
     *
     * @param row fila de la pared
     * @param col columna de la pared
     * @param isWall true para colocar pared, false para quitarla
     */
    public void setWall(int row, int col, boolean isWall) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            walls[row][col] = isWall;
        }
    }

    /**
     * Verifica si hay una pared en la posicion indicada.
     * Las posiciones fuera de los limites se consideran pared.
     *
     * @param row fila a verificar
     * @param col columna a verificar
     * @return true si hay pared o esta fuera de limites
     */
    public boolean isWall(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return true;
        }
        return walls[row][col];
    }

    /**
     * Agrega una zona segura al tablero.
     *
     * @param zone zona a agregar
     */
    public void addZone(Zone zone) {
        zones.add(zone);
    }

    /**
     * Retorna la lista de zonas seguras del tablero.
     * @return lista de zonas
     */
    public List<Zone> getZones() {
        return zones;
    }

    /**
     * Retorna la zona en la posicion (col, row), o null si no hay ninguna.
     *
     * @param col columna a consultar
     * @param row fila a consultar
     * @return la zona encontrada o null
     */
    public Zone getZoneAt(int col, int row) {
        for (Zone z : zones) {
            if (z.containsCell(col, row)) {
                return z;
            }
        }
        return null;
    }
}
