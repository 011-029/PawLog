package ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

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

        FlatSVGIcon logoSVG = new FlatSVGIcon("icons/pawlog-logo.svg", 66, 24);
        JLabel logo = new JLabel(logoSVG, SwingConstants.CENTER);
        logo.setBorder(new EmptyBorder(4, 0, 0, 0));

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

    public static JPanel createCollapsibleHeader(JLabel sectionLabel, JComponent contentPanel) {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        FlatSVGIcon iconExpanded = new FlatSVGIcon("icons/tap-collapse.svg", 12, 12);
        FlatSVGIcon iconCollapsed = new FlatSVGIcon("icons/tap-expand.svg", 12, 12);

        JButton toggleBtn = new JButton();
        toggleBtn.setIcon(iconExpanded); // 기본: 펼친 상태
        toggleBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleBtn.setContentAreaFilled(false);
        toggleBtn.setBorderPainted(false);
        toggleBtn.setFocusPainted(false);
        toggleBtn.setOpaque(false);
        toggleBtn.setPreferredSize(new Dimension(22, 22)); // 클릭 영역

        toggleBtn.addActionListener(e -> {
            boolean nowVisible = !contentPanel.isVisible();
            contentPanel.setVisible(nowVisible);

            toggleBtn.setIcon(nowVisible ? iconExpanded : iconCollapsed);

            contentPanel.revalidate();
            contentPanel.repaint();
        });

        header.add(sectionLabel);
        header.add(toggleBtn);

        return header;
    }

    public static JPanel createEmptyMessagePanel(String message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JLabel label = new JLabel(message);
        label.setFont(UIConstants.FONT_REGULAR_16);
        label.setForeground(UIConstants.TEXT_LIGHT);

        panel.add(label);
        return panel;
    }

    public static class WeightChartPanel extends JPanel {
        private final double[] weights;
        private final String[] labels;

        public WeightChartPanel(double[] weights, String[] labels) {
            this.weights = weights;
            this.labels = labels;
            setPreferredSize(new Dimension(310, 150));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (weights == null || weights.length == 0) return;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            int left = (getWidth() < 220) ? 5 : 30;
            int right = (getWidth() < 220) ? 5 : 15;
            int top = (getWidth() < 220) ? 5 : 15;
            int bottom = (getWidth() < 220) ? 5 : 18;

            int chartW = w - left - right;
            int chartH = h - top - bottom;
            int x0 = left;
            int y0 = top + chartH;

            // 배경
            g2.setColor(new Color(248, 248, 248));
            g2.fillRoundRect(left, top, chartW, chartH, 12, 12);

            // 최소/최대 값 먼저 계산
            double min = weights[0], max = weights[0];
            for (double v : weights) {
                min = Math.min(min, v);
                max = Math.max(max, v);
            }
            if (Math.abs(max - min) < 0.01) {
                max += 0.05;
                min -= 0.05;
            }
            double mid = (min + max) / 2.0;
            double[] lineValues = { min, mid, max };

            g2.setFont(g2.getFont().deriveFont(11f));
            FontMetrics fm = g2.getFontMetrics();
            Stroke oldStroke = g2.getStroke();
            Stroke dashed = new BasicStroke(
                    1f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10f,
                    new float[]{4f, 4f}, // 점선 패턴
                    0f
            );

            for (double value : lineValues) {
                double ratio = (value - min) / (max - min);
                int y = y0 - (int) (ratio * chartH);

                // 점선 보조 라인
                g2.setColor(new Color(210, 210, 210));
                g2.setStroke(dashed);
                g2.drawLine(left, y, left + chartW, y);

                // 숫자 라벨
                String text = String.format("%.2f", value);
                int strW = fm.stringWidth(text);
                int textX = left - strW - 6;
                int textY = y + fm.getAscent() / 2 - 2;

                g2.setStroke(oldStroke);
                g2.setColor(new Color(120, 120, 120));
                g2.drawString(text, textX, textY);
            }

            Path2D path = new Path2D.Double();
            int n = weights.length;
            for (int i = 0; i < n; i++) {
                double t = (double) i / (n - 1);
                int x = x0 + (int) (t * chartW);
                double ratio = (weights[i] - min) / (max - min);
                int y = y0 - (int) (ratio * chartH);

                if (i == 0) path.moveTo(x, y);
                else path.lineTo(x, y);
            }

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(UIConstants.PRIMARY);
            g2.draw(path);

            g2.setColor(UIConstants.PRIMARY);
            for (int i = 0; i < n; i++) {
                double t = (double) i / (n - 1);
                int x = x0 + (int) (t * chartW);
                double ratio = (weights[i] - min) / (max - min);
                int y = y0 - (int) (ratio * chartH);
                g2.fillOval(x - 3, y - 3, 6, 6);
            }

            g2.setFont(g2.getFont().deriveFont(10f));
            g2.setColor(new Color(120, 120, 120));
            for (int i = 0; i < n; i++) {
                double t = (double) i / (n - 1);
                int x = x0 + (int) (t * chartW);
                String label = labels != null && i < labels.length ? labels[i] : "";
                int strW = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, x - strW / 2, y0 + 16);
            }

            g2.dispose();
        }
    }
}
