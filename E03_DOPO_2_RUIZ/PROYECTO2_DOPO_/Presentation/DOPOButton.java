package Presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Boton personalizado para The DOPO Hardest Game.
 * Extiende JButton aplicando el estilo visual del juego de forma uniforme.
 * Todas las pantallas del juego usan esta clase para mantener consistencia visual
 *En caso de que se llegue a necesitar cambiar algo del funcionamiento de los botones 
 *se puede hacer de manera general desde esta clase 
 *
 * @author William Santiago Ruiz Medina
 * @version 2.0 2026-1
 */
public class DOPOButton extends JButton {
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 15);
    private static final int DEFAULT_WIDTH = 140;
    private static final int DEFAULT_HEIGHT = 40;
    /**
     * Crea un boton con el texto dado y dimensiones por defecto (140x40).
     *
     * @param text texto a mostrar en el boton
     */
    public DOPOButton(String text) {
        super(text);
        applyStyle(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Crea un boton con el texto y dimensiones personalizadas.
     *
     * @param text texto a mostrar en el boton
     * @param width ancho del boton en pixeles
     * @param height alto del boton en pixeles
     */
    public DOPOButton(String text, int width, int height) {
        super(text);
        applyStyle(width, height);
    }

    /**
     * Aplica el estilo visual del juego al boton.
     *
     * @param width ancho deseado
     * @param height alto deseado
     */
    private void applyStyle(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setFont(BUTTON_FONT);
        setFocusable(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
