package ui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

public class Main {

    public static void main(String[] args) {
        try {
            FlatMacLightLaf.setGlobalExtraDefaults(
                    java.util.Collections.singletonMap("@accentColor", "#A4C3DE")
            );
            FlatMacLightLaf.setup();

            UIManager.put("TabbedPane.background", UIConstants.PRIMARY);
            UIManager.put("defaultFont", new FontUIResource(UIConstants.FONT_REGULAR_14));
        } catch (Exception e) {
            System.out.println("FlatLaf 적용 오류: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
