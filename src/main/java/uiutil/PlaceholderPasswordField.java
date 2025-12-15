package uiutil;

import ui.UIConstants;

import javax.swing.*;
import java.awt.*;

public class PlaceholderPasswordField extends JPasswordField {
    private String placeholder;

    public PlaceholderPasswordField(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getPassword().length == 0 && !isFocusOwner()) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g2.setFont(UIConstants.FONT_REGULAR_14);
            g2.setColor(UIConstants.TEXT_LIGHT);

            FontMetrics fm = g2.getFontMetrics();
            int x = 10;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

            g2.drawString(placeholder, x, y);
            g2.dispose();
        }
    }
}
