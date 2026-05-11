package Presentation;

import Domain.*;
import Domain.worldHardestGame.GameMode;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Pantalla principal del juego. Muestra el tablero, jugador, enemigos,
 * monedas, zonas seguras, elementos especiales y el HUD con informacion del estado.
 * Soporta controles WASD/flechas para P1, IJKL para P2, y movimiento diagonal.
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class whdGUI extends JFrame {

    private static final int CELL_SIZE = 40;

    private worldHardestGame game;
    private JLabel hud;
    private GamePanel gamePanel;
    private Timer gameTimer;
    private Timer secondTimer;
    private DOPOButton pauseButton;
    private java.util.Set<Integer> pressedKeys;
    private int pendingDx1;
    private int pendingDy1;
    private int pendingDx2;
    private int pendingDy2;

    /**
     * Crea y muestra la pantalla principal del juego con el estado dado.
     *
     * @param game instancia del controlador de juego
     */
    public whdGUI(worldHardestGame game) {
        this.game = game;
        setTitle("The DOPO Hardest Game - Nivel " + game.getCurrentLevelNum());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(50, 50, 50));

        hud = new JLabel();
        hud.setFont(new Font("Consolas", Font.BOLD, 14));
        hud.setForeground(Color.WHITE);
        hud.setBackground(new Color(50, 50, 50));
        hud.setOpaque(true);
        hud.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        updateHUD();
        topPanel.add(hud, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(new Color(50, 50, 50));

        DOPOButton archivoButton = new DOPOButton("Archivo", 80, 30);
        JPopupMenu archivoMenu = new JPopupMenu();
        JMenuItem guardarItem = new JMenuItem("Guardar partida");
        JMenuItem abrirItem = new JMenuItem("Abrir partida");
        guardarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { saveGame(); }
        });
        abrirItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { loadGame(); }
        });
        archivoMenu.add(guardarItem);
        archivoMenu.add(abrirItem);
        archivoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                archivoMenu.show(archivoButton, 0, archivoButton.getHeight());
            }
        });
        controlPanel.add(archivoButton);

        pauseButton = new DOPOButton("Pausa", 80, 30);
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { togglePause(); }
        });
        controlPanel.add(pauseButton);

        DOPOButton menuButton = new DOPOButton("Menu", 80, 30);
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { returnToMenu(); }
        });
        controlPanel.add(menuButton);

        topPanel.add(controlPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setFocusable(true);
        setVisible(true);

        pressedKeys = new java.util.HashSet<>();
        setupKeyBindings();

        gameTimer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameTick();
            }
        });
        gameTimer.start();

        secondTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (game.tick()) {
                    gameTimer.stop();
                    secondTimer.stop();
                    showTimeUpDialog();
                }
                updateHUD();
            }
        });
        secondTimer.start();
    }

    /**
     * Constructor por defecto para compatibilidad con el codigo original.
     */
    public whdGUI() {
        this(new worldHardestGame());
    }

    /**
     * Ejecuta un ciclo del juego: procesa teclas presionadas, mueve enemigos,
     * maquina, verifica colisiones.
     */
    private void gameTick() {
        processKeys();
        game.moveEnemies();
        if (game.getMode() == GameMode.PVM) {
            game.moveMachine();
        }
        game.checkCollisions();
        updateHUD();

        if (game.isLevelComplete()) {
            gameTimer.stop();
            secondTimer.stop();
            showWinDialog();
        }
        gamePanel.repaint();
    }

    /**
     * Procesa el movimiento de cada jugador. Usa el movimiento pendiente
     * si existe (pulsacion rapida), sino lee las teclas actualmente presionadas.
     * Se ejecuta una sola vez por tick del timer.
     */
    private void processKeys() {
        if (game.isPaused()) {
            return;
        }

        int dx1 = pendingDx1;
        int dy1 = pendingDy1;

        if (dx1 == 0 && dy1 == 0) {
            if (pressedKeys.contains(KeyEvent.VK_UP)    || pressedKeys.contains(KeyEvent.VK_W)) { dy1 = -1; }
            if (pressedKeys.contains(KeyEvent.VK_DOWN)  || pressedKeys.contains(KeyEvent.VK_S)) { dy1 = 1; }
            if (pressedKeys.contains(KeyEvent.VK_LEFT)  || pressedKeys.contains(KeyEvent.VK_A)) { dx1 = -1; }
            if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) { dx1 = 1; }

            if (pressedKeys.contains(KeyEvent.VK_Q)) { dx1 = -1; dy1 = -1; }
            if (pressedKeys.contains(KeyEvent.VK_E)) { dx1 = 1;  dy1 = -1; }
            if (pressedKeys.contains(KeyEvent.VK_Z)) { dx1 = -1; dy1 = 1; }
            if (pressedKeys.contains(KeyEvent.VK_C)) { dx1 = 1;  dy1 = 1; }
        }

        if (dx1 != 0 || dy1 != 0) {
            game.movePlayer(dx1, dy1);
        }
        pendingDx1 = 0;
        pendingDy1 = 0;

        if (game.getMode() == GameMode.PVP) {
            int dx2 = pendingDx2;
            int dy2 = pendingDy2;

            if (dx2 == 0 && dy2 == 0) {
                if (pressedKeys.contains(KeyEvent.VK_I)) { dy2 = -1; }
                if (pressedKeys.contains(KeyEvent.VK_K)) { dy2 = 1; }
                if (pressedKeys.contains(KeyEvent.VK_J)) { dx2 = -1; }
                if (pressedKeys.contains(KeyEvent.VK_L)) { dx2 = 1; }
            }

            if (dx2 != 0 || dy2 != 0) {
                game.movePlayer2(dx2, dy2);
            }
            pendingDx2 = 0;
            pendingDy2 = 0;
        }
    }

    /**
     * Actualiza la etiqueta del HUD con el estado actual del juego.
     */
    private void updateHUD() {
        StringBuilder sb = new StringBuilder();
        sb.append("  NIVEL: ").append(game.getCurrentLevelNum());
        sb.append("    Muertes: ").append(game.getPlayer1().getDeaths());
        sb.append("    Monedas: ").append(game.coinsCollected()).append("/").append(game.totalCoins());
        sb.append("    Tiempo: ").append(formatTime(game.getTimeRemaining()));

        if (game.getMode() != GameMode.PLAYER && game.getPlayer2() != null) {
            sb.append("    |  P2 Muertes: ").append(game.getPlayer2().getDeaths());
        }

        if (game.isPaused()) {
            sb.append("    [PAUSADO]");
        }

        hud.setText(sb.toString());
    }

    /**
     * Formatea segundos como minutos:segundos.
     *
     * @param seconds tiempo en segundos
     * @return cadena formateada "m:ss"
     */
    private String formatTime(int seconds) {
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    /**
     * Alterna el estado de pausa del juego.
     */
    private void togglePause() {
        game.togglePause();
        if (game.isPaused()) {
            pauseButton.setText("Reanudar");
        } else {
            pauseButton.setText("Pausa");
        }
        updateHUD();
        requestFocusInWindow();
    }

    /**
     * Guarda el estado actual del juego en un archivo seleccionado por el usuario.
     */
    private void saveGame() {
        gameTimer.stop();
        secondTimer.stop();
        game.togglePause();

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Guardar Partida");
        fc.setSelectedFile(new java.io.File("dopo_save.dat"));
        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                game.saveGame(fc.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this,
                    "Partida guardada exitosamente.",
                    "Guardar", JOptionPane.INFORMATION_MESSAGE);
            } catch (DOPOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        game.togglePause();
        gameTimer.start();
        secondTimer.start();
        requestFocusInWindow();
    }

    /**
     * Carga un estado de juego desde un archivo seleccionado por el usuario.
     */
    private void loadGame() {
        gameTimer.stop();
        secondTimer.stop();

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Abrir Partida");
        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                worldHardestGame loaded = worldHardestGame.loadGame(fc.getSelectedFile().getAbsolutePath());
                this.game = loaded;
                setTitle("The DOPO Hardest Game - Nivel " + game.getCurrentLevelNum());
                gamePanel.updateSize();
                pack();
                updateHUD();
                gamePanel.repaint();
                JOptionPane.showMessageDialog(this,
                    "Partida cargada exitosamente.",
                    "Abrir", JOptionPane.INFORMATION_MESSAGE);
            } catch (DOPOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al cargar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        gameTimer.start();
        secondTimer.start();
        requestFocusInWindow();
    }

    /**
     * Regresa al menu principal, pidiendo confirmacion al usuario.
     */
    private void returnToMenu() {
        gameTimer.stop();
        secondTimer.stop();
        int option = JOptionPane.showConfirmDialog(this,
            "Volver al menu principal? El progreso actual se perdera.",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            new MenuGUI();
        } else {
            gameTimer.start();
            secondTimer.start();
            requestFocusInWindow();
        }
    }

    /**
     * Muestra el dialogo de victoria al completar un nivel.
     */
    private void showWinDialog() {
        Player winner = game.getWinner();
        String msg = "Nivel Completado!";
        if (winner != null && game.getMode() != GameMode.PLAYER) {
            msg = winner.getName() + " gana!";
        }
        msg += "\nMuertes: " + game.getPlayer1().getDeaths();
        msg += "\nTiempo: " + formatTime(game.getCurrentLevel().getTimeLimit() - game.getTimeRemaining());

        int option = JOptionPane.showOptionDialog(this, msg, "Victoria",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
            new String[]{"Sig. Nivel", "Menu"}, "Sig. Nivel");

        if (option == 0) {
            if (game.nextLevel()) {
                setTitle("The DOPO Hardest Game - Nivel " + game.getCurrentLevelNum());
                gamePanel.updateSize();
                pack();
                gameTimer.start();
                secondTimer.start();
                updateHUD();
                gamePanel.repaint();
                requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Felicidades! Has completado todos los niveles!\nMuertes totales: " + game.getPlayer1().getDeaths(),
                    "Juego Completo!", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new MenuGUI();
            }
        } else {
            dispose();
            new MenuGUI();
        }
    }

    /**
     * Muestra el dialogo cuando se acaba el tiempo del nivel.
     */
    private void showTimeUpDialog() {
        JOptionPane.showMessageDialog(this,
            "Se acabo el tiempo!\nMuertes: " + game.getPlayer1().getDeaths(),
            "Game Over", JOptionPane.WARNING_MESSAGE);
        dispose();
        new MenuGUI();
    }

    /**
     * Configura los Key Bindings en el gamePanel para capturar teclas
     * sin depender del foco del JFrame. Al presionar una tecla se registra
     * tanto en el conjunto de teclas activas como en el movimiento pendiente,
     * garantizando que pulsaciones rapidas no se pierdan entre ticks.
     */
    private void setupKeyBindings() {
        InputMap im = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = gamePanel.getActionMap();

        int[] keys = {
            KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
            KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D,
            KeyEvent.VK_Q, KeyEvent.VK_E, KeyEvent.VK_Z, KeyEvent.VK_C,
            KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L,
            KeyEvent.VK_P
        };

        for (int key : keys) {
            String name = "key_" + key;
            im.put(KeyStroke.getKeyStroke(key, 0, false), name + "_pressed");
            im.put(KeyStroke.getKeyStroke(key, 0, true), name + "_released");

            final int k = key;
            am.put(name + "_pressed", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (k == KeyEvent.VK_P) {
                        togglePause();
                    } else {
                        pressedKeys.add(k);
                        applyPending(k);
                    }
                }
            });
            am.put(name + "_released", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    pressedKeys.remove(k);
                }
            });
        }
    }

    /**
     * Registra un movimiento pendiente segun la tecla presionada.
     * Esto asegura que pulsaciones rapidas entre ticks no se pierdan.
     *
     * @param key codigo de la tecla presionada
     */
    private void applyPending(int key) {
        switch (key) {
            case KeyEvent.VK_UP:    case KeyEvent.VK_W: pendingDy1 = -1; break;
            case KeyEvent.VK_DOWN:  case KeyEvent.VK_S: pendingDy1 = 1;  break;
            case KeyEvent.VK_LEFT:  case KeyEvent.VK_A: pendingDx1 = -1; break;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_D: pendingDx1 = 1;  break;
            case KeyEvent.VK_Q: pendingDx1 = -1; pendingDy1 = -1; break;
            case KeyEvent.VK_E: pendingDx1 = 1;  pendingDy1 = -1; break;
            case KeyEvent.VK_Z: pendingDx1 = -1; pendingDy1 = 1;  break;
            case KeyEvent.VK_C: pendingDx1 = 1;  pendingDy1 = 1;  break;
            case KeyEvent.VK_I: pendingDy2 = -1; break;
            case KeyEvent.VK_K: pendingDy2 = 1;  break;
            case KeyEvent.VK_J: pendingDx2 = -1; break;
            case KeyEvent.VK_L: pendingDx2 = 1;  break;
            default: break;
        }
    }

    /**
     * Panel interno que dibuja el tablero de juego, zonas seguras,
     * paredes, monedas, enemigos, elementos especiales y jugadores.
     */
    private class GamePanel extends JPanel {

        /**
         * Crea el panel de juego con el tamano del tablero actual.
         */
        public GamePanel() {
            updateSize();
        }

        /**
         * Actualiza el tamano preferido del panel segun las dimensiones del tablero.
         */
        public void updateSize() {
            setPreferredSize(new Dimension(
                game.getCols() * CELL_SIZE,
                game.getRows() * CELL_SIZE));
            revalidate();
        }

        /**
         * Dibuja todos los elementos del juego en el panel.
         *
         * @param g contexto grafico
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Level level = game.getCurrentLevel();
            Table table = level.getTable();
            int cols = table.getCols();
            int rows = table.getRows();

            drawBoard(g2, cols, rows, table);
            drawZones(g2, table);
            drawGrid(g2, cols, rows);
            drawSpecialElements(g2, level);
            drawCoins(g2, level);
            drawEnemies(g2, level);
            drawPlayer(g2, game.getPlayer1(), "P1");

            if (game.getPlayer2() != null) {
                drawPlayer(g2, game.getPlayer2(), "P2");
            }

            if (game.isPaused()) {
                drawPauseOverlay(g2);
            }
        }

        /**
         * Dibuja el fondo del tablero con patron de cuadricula y paredes.
         */
        private void drawBoard(Graphics2D g2, int cols, int rows, Table table) {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    if (table.isWall(j, i)) {
                        g2.setColor(new Color(60, 60, 60));
                    } else if ((i + j) % 2 == 0) {
                        g2.setColor(new Color(245, 245, 245));
                    } else {
                        g2.setColor(new Color(230, 230, 230));
                    }
                    g2.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        /**
         * Dibuja las zonas seguras sobre el tablero.
         */
        private void drawZones(Graphics2D g2, Table table) {
            for (Zone zone : table.getZones()) {
                Color zoneColor;
                switch (zone.getType()) {
                    case INITIAL:
                        zoneColor = new Color(144, 238, 144, 160);
                        break;
                    case INTERMEDIATE:
                        zoneColor = new Color(144, 238, 200, 160);
                        break;
                    case FINAL:
                        zoneColor = new Color(100, 220, 100, 160);
                        break;
                    default:
                        zoneColor = new Color(144, 238, 144, 160);
                }
                g2.setColor(zoneColor);
                g2.fillRect((int) zone.getX() * CELL_SIZE, (int) zone.getY() * CELL_SIZE,
                            (int) zone.getBroad() * CELL_SIZE, (int) zone.getHeight() * CELL_SIZE);
            }
        }

        /**
         * Dibuja las lineas de la grilla.
         */
        private void drawGrid(Graphics2D g2, int cols, int rows) {
            g2.setColor(new Color(200, 200, 200));
            for (int i = 0; i <= cols; i++) {
                g2.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, rows * CELL_SIZE);
            }
            for (int j = 0; j <= rows; j++) {
                g2.drawLine(0, j * CELL_SIZE, cols * CELL_SIZE, j * CELL_SIZE);
            }
        }

        /**
         * Dibuja los elementos especiales activos (fuentes de vida, bombas).
         */
        private void drawSpecialElements(Graphics2D g2, Level level) {
            for (SpecialElement se : level.getSpecialElements()) {
                if (!se.isActive()) {
                    continue;
                }
                int sx = se.getCol() * CELL_SIZE;
                int sy = se.getRow() * CELL_SIZE;

                g2.setColor(se.getColor());
                g2.fillOval(sx + 5, sy + 5, CELL_SIZE - 10, CELL_SIZE - 10);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                switch (se.getType()) {
                    case LIFE_SOURCE:
                        g2.drawString("+", sx + 13, sy + 26);
                        break;
                    case BOMB:
                        g2.drawString("B", sx + 12, sy + 26);
                        break;
                    default:
                        break;
                }
            }
        }

        /**
         * Dibuja las monedas no recolectadas.
         */
        private void drawCoins(Graphics2D g2, Level level) {
            for (Coin coin : level.getCoins()) {
                if (!coin.isCollected()) {
                    int cx = coin.getCol() * CELL_SIZE + CELL_SIZE / 4;
                    int cy = coin.getRow() * CELL_SIZE + CELL_SIZE / 4;
                    g2.setColor(coin.getColor());
                    g2.fillOval(cx, cy, CELL_SIZE / 2, CELL_SIZE / 2);
                    g2.setColor(new Color(200, 180, 0));
                    g2.drawOval(cx, cy, CELL_SIZE / 2, CELL_SIZE / 2);
                }
            }
        }

        /**
         * Dibuja los enemigos. Los enemigos rapidos muestran una "A" (Acelerado).
         */
        private void drawEnemies(Graphics2D g2, Level level) {
            for (Enemy enemy : level.getEnemies()) {
                int ex = enemy.getCol() * CELL_SIZE + CELL_SIZE / 4;
                int ey = enemy.getRow() * CELL_SIZE + CELL_SIZE / 4;
                g2.setColor(enemy.getColor());
                g2.fillOval(ex, ey, CELL_SIZE / 2, CELL_SIZE / 2);

                if (enemy.getType() == Enemy.EnemyType.FAST) {
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 10));
                    g2.drawString("A", ex + 6, ey + 14);
                }
            }
        }

        /**
         * Dibuja un jugador como un cuadrado con borde y etiqueta.
         * Muestra un brillo amarillo si Clyde tiene escudo activo.
         *
         * @param g2 contexto grafico
         * @param player jugador a dibujar
         * @param label etiqueta del jugador ("P1" o "P2")
         */
        private void drawPlayer(Graphics2D g2, Player player, String label) {
            int px = player.getCol() * CELL_SIZE;
            int py = player.getRow() * CELL_SIZE;
            int margin = 4;
            int size = CELL_SIZE - margin * 2;

            g2.setColor(player.getSkinColor());
            g2.fillRect(px + margin, py + margin, size, size);

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(px + margin, py + margin, size, size);
            g2.setStroke(new BasicStroke(1));

            if (player.hasShield()) {
                g2.setColor(new Color(255, 255, 0, 100));
                g2.fillOval(px + 2, py + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            }

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 10));
            g2.drawString(label, px + margin + 4, py + margin + 12);
        }

        /**
         * Dibuja la superposicion de pausa sobre el tablero.
         */
        private void drawPauseOverlay(Graphics2D g2) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial Black", Font.BOLD, 36));
            String txt = "PAUSADO";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(txt, (getWidth() - fm.stringWidth(txt)) / 2, getHeight() / 2);
            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            String sub = "Presiona P para reanudar";
            fm = g2.getFontMetrics();
            g2.drawString(sub, (getWidth() - fm.stringWidth(sub)) / 2, getHeight() / 2 + 30);
        }
    }
}
