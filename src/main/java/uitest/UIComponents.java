// 예: uitest/UIComponents.java
package uitest;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class UIComponents {

    private UIComponents() { }

    public static JComponent createHeader(Runnable onBackClick) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JButton backBtn = new JButton();
        backBtn.setIcon(new FlatSVGIcon("icons/arrow-prev.svg", 20, 20));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backBtn.addActionListener(e -> {
            if (onBackClick != null) {
                onBackClick.run();
            }
        });

        JLabel logo = new JLabel("PawLog", SwingConstants.CENTER);
        logo.setFont(UIConstants.FONT_BOLD_24);
        logo.setForeground(UIConstants.TEXT_PRIMARY);

        JButton bellBtn = new JButton();
        bellBtn.setIcon(new FlatSVGIcon("icons/bell.svg", 20, 20));
        bellBtn.setFocusPainted(false);
        bellBtn.setBorderPainted(false);
        bellBtn.setContentAreaFilled(false);
        bellBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        header.add(backBtn, BorderLayout.WEST);
        header.add(logo, BorderLayout.CENTER);
        header.add(bellBtn, BorderLayout.EAST);

        return header;
    }

    public static JComponent createTabbedNav(MainFrame mainFrame) {
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM);

        tabs.putClientProperty("JTabbedPane.tabWidthMode", "equal");
        tabs.putClientProperty("JTabbedPane.tabAreaAlignment", "fill");
        tabs.setPreferredSize(new Dimension(0, 60));
        tabs.putClientProperty("JTabbedPane.tabHeight", 59);
        tabs.putClientProperty("JTabbedPane.tabAreaInsets", "0,0,0,0");
        tabs.putClientProperty("JTabbedPane.contentAreaInsets", "0,0,0,0");
        tabs.setBorder(null);

        tabs.addTab("홈", new JPanel());
        tabs.setTabComponentAt(0, createTab("홈", "icons/home.svg"));

        tabs.addTab("캘린더", new JPanel());
        tabs.setTabComponentAt(1, createTab("캘린더", "icons/calendar.svg"));

        tabs.addTab("기록", new JPanel());
        tabs.setTabComponentAt(2, createTab("기록추가", "icons/add.svg"));

        tabs.addTab("매거진", new JPanel());
        tabs.setTabComponentAt(3, createTab("펫가이드", "icons/notes.svg"));

        tabs.addTab("설정", new JPanel());
        tabs.setTabComponentAt(4, createTab("설정", "icons/setting.svg"));

        tabs.addChangeListener(e -> switchTab(tabs.getSelectedIndex(), mainFrame));

        tabs.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                switchTab(tabs.getSelectedIndex(), mainFrame);
            }
        });

        return tabs;
    }

    static public void switchTab(int idx, MainFrame mainFrame) {
        switch (idx) {
            case 0 -> mainFrame.switchPanel(new HomePanel(mainFrame));
            case 1 -> mainFrame.switchPanel(new CalendarPanel(mainFrame));
            case 2 -> mainFrame.switchPanel(new AddRecordMenuPanel(mainFrame));
            case 3 -> mainFrame.switchPanel(new PetTipsPanel(mainFrame));
            case 4 -> mainFrame.switchPanel(new SettingPanel(mainFrame));
            default -> {}
        }
    }

    private static Component createTab(String title, String iconPath) {
        JPanel tab = new JPanel(new BorderLayout());
        tab.setOpaque(false);

        ImageIcon icon = new FlatSVGIcon(iconPath, 20, 20);
        JLabel label = new JLabel(title, icon, JLabel.CENTER);
        label.setFont(UIConstants.FONT_SEMIBOLD_12);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setIconTextGap(5);
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));

        tab.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        tab.add(label, BorderLayout.CENTER);
        return tab;
    }

    public static JButton createSearchButton(MainFrame mainFrame, JPanel thisPanel) {
        JButton btn = new JButton();
        btn.setIcon(new FlatSVGIcon("icons/search.svg", 22, 22));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setPreferredSize(new Dimension(32, 32));
        btn.setMargin(new Insets(0, 0, 0, 0));

        btn.addActionListener(e -> mainFrame.switchPanel(new SearchPanel(mainFrame, thisPanel)));

        return btn;
    }
}
