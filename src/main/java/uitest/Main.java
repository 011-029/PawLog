package uitest;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.FontUIResource;

public class Main {

    public static void main(String[] args) {
        try {
            // 포인트 색상 (선택)
            FlatMacLightLaf.setGlobalExtraDefaults(
                    java.util.Collections.singletonMap("@accentColor", "#A4C3DE")
            );
            FlatMacLightLaf.setup();

            // 탭 색 커스터마이징 (탭/프레임 만들기 전에!)
            UIManager.put("TabbedPane.background", UIConstants.PRIMARY);
//            UIManager.put("TabbedPane.selectedBackground", Color.decode("#5887D1"));
//            UIManager.put("TabbedPane.foreground", Color.WHITE);
//            UIManager.put("TabbedPane.selectedForeground", Color.WHITE);
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
