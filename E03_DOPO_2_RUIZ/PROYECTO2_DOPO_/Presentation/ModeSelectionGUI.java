package Presentation;

import Domain.*;
import Domain.worldHardestGame.GameMode;
import Domain.Player.Skin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Pantalla de seleccion de modo de juego y configuracion.
 * Permite elegir modalidad, color de jugadores y nivel.
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class ModeSelectionGUI extends JFrame {

    private GameMode selectedMode;
    private Skin selectedSkin1;
    private Skin selectedSkin2;
    private int selectedLevel;

    /**
     * Crea y muestra la pantalla de seleccion de modo.
     */
    public ModeSelectionGUI() {
        this.selectedMode = GameMode.PLAYER;
        this.selectedSkin1 = Skin.ROJO;
        this.selectedSkin2 = Skin.AZUL;
        this.selectedLevel = 1;

        setTitle("The DOPO Hardest Game - Setup");
        setSize(500, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("CONFIGURACION DE JUEGO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        center.add(createSectionLabel("Modo de juego"));

        JPanel modePanel = new JPanel(new FlowLayout());
        ButtonGroup modeGroup = new ButtonGroup();

        JRadioButton playerBtn = new JRadioButton("1 Jugador", true);
        JRadioButton pvpBtn = new JRadioButton("Jugador vs Jugador", false);
        JRadioButton pvmBtn = new JRadioButton("Jugador vs Maquina", false);

        modeGroup.add(playerBtn);
        modeGroup.add(pvpBtn);
        modeGroup.add(pvmBtn);

        modePanel.add(playerBtn);
        modePanel.add(pvpBtn);
        modePanel.add(pvmBtn);

        center.add(modePanel);

        center.add(createSectionLabel("Color Jugador 1"));
        JPanel skin1Panel = createSkinPanel(1);
        center.add(skin1Panel);

        JLabel p2Label = createSectionLabel("Color Jugador 2");
        JPanel skin2Panel = createSkinPanel(2);
        p2Label.setVisible(false);
        skin2Panel.setVisible(false);
        center.add(p2Label);
        center.add(skin2Panel);

        center.add(createSectionLabel("Nivel"));

        JPanel levelPanel = new JPanel(new FlowLayout());
        String[] levelNames = {"Lvl1", "Lvl2", "Lvl3"};
        JComboBox<String> levelCombo = new JComboBox<>(levelNames);
        levelCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        levelCombo.setPreferredSize(new Dimension(200, 28));
        levelPanel.add(levelCombo);
        center.add(levelPanel);

        add(new JScrollPane(center), BorderLayout.CENTER);

        playerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedMode = GameMode.PLAYER;
                p2Label.setVisible(false);
                skin2Panel.setVisible(false);
            }
        });
        
        pvpBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedMode = GameMode.PVP;
                p2Label.setVisible(true);
                skin2Panel.setVisible(true);
            }
        });

        pvmBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedMode = GameMode.PVM;
                p2Label.setVisible(false);
                skin2Panel.setVisible(false);
            }
        });

        levelCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedLevel = levelCombo.getSelectedIndex() + 1;
            }
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        DOPOButton startBtn = new DOPOButton("INICIAR JUEGO", 180, 40);
        startBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        bottomPanel.add(startBtn);
        DOPOButton backBtn = new DOPOButton("VOLVER", 100, 40);
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MenuGUI();
            }
        });
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
    /**
     * Crea un panel de seleccion de color para el jugador indicado.
     *
     * @param playerNum numero del jugador
     * @return panel con los radio buttons de color
     */
    private JPanel createSkinPanel(int playerNum) {
        JPanel panel = new JPanel(new FlowLayout());
        ButtonGroup group = new ButtonGroup();
        JRadioButton red = new JRadioButton("Rojo", playerNum == 1);
        JRadioButton blue = new JRadioButton("Azul", playerNum == 2);
        JRadioButton green = new JRadioButton("Verde", false);
        group.add(red);
        group.add(blue);
        group.add(green);
        panel.add(red);
        panel.add(blue);
        panel.add(green);

        if (playerNum == 1) {
            red.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedSkin1 = Skin.ROJO;
                }
            });

            blue.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedSkin1 = Skin.AZUL;
                }
            });

            green.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedSkin1 = Skin.VERDE;
                }
            });
        } else {
            red.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedSkin2 = Skin.ROJO;
                }
            });

            blue.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedSkin2 = Skin.AZUL;
                }
            });

            green.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedSkin2 = Skin.VERDE;
                }
            });
        }

        return panel;
    }
    /**
     * Crea una etiqueta de seccion con formato consistente.
     *
     * @param text texto de la seccion
     * @return etiqueta formateada
     */
    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
        return label;
    }
    /**
     * Inicia el juego con la configuracion seleccionada.
     * Si el modo o nivel seleccionado no esta disponible, muestra un mensaje.
     */
    private void startGame() {
        if (selectedMode == GameMode.PVP) {
            JOptionPane.showMessageDialog(
                this,
                "El modo Jugador vs Jugador aun se esta trabajando.",
                "Modo no disponible",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        if (selectedLevel != 1) {
            JOptionPane.showMessageDialog(
                this,
                "Este nivel aun se esta trabajando.",
                "Nivel no disponible",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        dispose();
        worldHardestGame game;
        switch (selectedMode) {
            case PVM:
                game = new worldHardestGame(selectedSkin1);
                break;
            default:
                game = new worldHardestGame(GameMode.PLAYER, selectedSkin1);
                break;
        }
        new whdGUI(game);
    }
}