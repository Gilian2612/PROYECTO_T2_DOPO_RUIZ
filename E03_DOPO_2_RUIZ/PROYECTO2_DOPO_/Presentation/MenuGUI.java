package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Pantalla del menu principal de The DOPO Hardest Game.
 * Muestra un banner visual, y los botones de jugar y salir.
 * Contiene el metodo main() como punto de entrada de la aplicacion.
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class MenuGUI extends JFrame {

    private DOPOButton playButton;
    private DOPOButton exitButton;

    /**
     * Crea y muestra el menu principal con el banner visual.
     */
    public MenuGUI() {
        setTitle("The DOPO Hardest Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(30, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        ImageIcon bannerIcon = loadBanner();
        if (bannerIcon != null) {
            JLabel bannerLabel = new JLabel(bannerIcon);
            gbc.gridy = 0;
            add(bannerLabel, gbc);
        }

        playButton = new DOPOButton("PLAY");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(playButton, gbc);

        exitButton = new DOPOButton("EXIT");
        gbc.gridy = 2;
        add(exitButton, gbc);

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showModeSelection();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Carga el banner desde la carpeta sprays y lo escala para el menu.
     * Busca el archivo en "sprays/BY_W_R.png" relativo al directorio de ejecucion.
     *
     * @return ImageIcon escalado o null si no se encuentra la imagen
     */
    private ImageIcon loadBanner() {
        ImageIcon original = new ImageIcon("sprays/BY_W_R.png");
        if (original.getIconWidth() <= 0) {
            return null;
        }
        Image scaled = original.getImage().getScaledInstance(550, 310, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * Abre la pantalla de seleccion de modo de juego.
     */
    private void showModeSelection() {
        dispose();
        new ModeSelectionGUI();
    }

    /**
     * Punto de entrada principal de la aplicacion.
     *
     * @param args argumentos de linea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MenuGUI();
            }
        });
    }
}
