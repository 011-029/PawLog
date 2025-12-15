package uiutil;

import ui.UIConstants;

import javax.swing.*;
import java.awt.*;

public class LabeledTextField extends JPanel {

    private final PlaceholderTextField textField;

    public LabeledTextField(String labelText, String placeholder) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        // 라벨
        JLabel label = new JLabel(labelText);
        label.setFont(UIConstants.FONT_SEMIBOLD_16);
        label.setForeground(UIConstants.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 텍스트필드
        textField = new PlaceholderTextField(placeholder);
        textField.setPreferredSize(new Dimension(360, 45));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField.putClientProperty("FlatLaf.style", "arc:10");
        textField.setBorder(
                BorderFactory.createCompoundBorder(
                        textField.getBorder(),
                        BorderFactory.createEmptyBorder(0, 8, 0, 8)
                )
        );

        add(label);
        add(Box.createVerticalStrut(8));
        add(textField);
        add(Box.createVerticalStrut(22));
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String txt) {
        textField.setText(txt);
    }

    public int getIntOrDefault(int defaultValue) {
        String txt = getText();
        if (txt == null) return defaultValue;

        txt = txt.trim();
        if (txt.isEmpty()) return defaultValue;

        try {
            return Integer.parseInt(txt);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
