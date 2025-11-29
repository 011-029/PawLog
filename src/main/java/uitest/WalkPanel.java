package uitest;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class WalkPanel extends JPanel {

    private final MainFrame mainFrame;

    public WalkPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new PetHomePanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createContent(), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    /* ================== 상단바 ================== */
//    private JComponent createHeader() {
//        JPanel header = new JPanel(new BorderLayout());
//        header.setOpaque(false);
//
//        JButton backBtn = new JButton();
//        backBtn.setIcon(new FlatSVGIcon("icons/arrow-prev.svg", 20, 20));
//        backBtn.setFocusPainted(false);
//        backBtn.setBorderPainted(false);
//        backBtn.setContentAreaFilled(false);
//        backBtn.addActionListener(e -> mainFrame.switchPanel(new PetHomePanel(mainFrame)));
//        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//
//        JLabel logo = new JLabel("PawLog", SwingConstants.CENTER);
//        logo.setFont(UIConstants.FONT_BOLD_24);
//        logo.setForeground(UIConstants.TEXT_PRIMARY);
//
//        JButton bellBtn = new JButton("아이콘");
//        bellBtn.setFocusPainted(false);
//        bellBtn.setPreferredSize(new Dimension(40, 40));
//        bellBtn.setBackground(Color.WHITE);
//        bellBtn.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));
//
//        header.add(backBtn, BorderLayout.WEST);
//        header.add(logo, BorderLayout.CENTER);
//        header.add(bellBtn, BorderLayout.EAST);
//
//        return header;
//    }

    /* ================== 가운데 리스트 ================== */
    private JComponent createContent() {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("산책 기록");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);

        header.add(Box.createHorizontalGlue());
        header.add(createSearchButton());

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        // 🔹 예시 카드들 (이미지 있는 것 / 없는 것)
        JPanel card1 = createWalkCard(
                "산책 기록 1",
                "강아지랑 공원 한 바퀴 산책했어요",
                "2025-11-29 · 30분",
                "walk1.jpg"              // ✅ 사진 있는 경우
        );
        listPanel.add(card1);
        listPanel.add(Box.createVerticalStrut(16));

        JPanel card2 = createWalkCard(
                "산책 기록 2",
                "비가 와서 오늘은 짧게 다녀왔어요",
                "2025-11-28 · 15분",
                null                     // ✅ 사진 없는 경우
        );
        listPanel.add(card2);
        listPanel.add(Box.createVerticalStrut(16));

        JPanel card3 = createWalkCard(
                "산책 기록 2",
                "비가 와서 오늘은 짧게 다녀왔어요",
                "2025-11-28 · 15분",
                "walk1.jpg"
        );
        listPanel.add(card3);
        listPanel.add(Box.createVerticalStrut(16));

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(30);

        return scroll;
    }

    /** 개별 산책 기록 카드 (사진 선택적) */
    private JPanel createWalkCard(String title, String desc, String timeText, String imagePath) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setPreferredSize(new Dimension(310, 130));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        card.setMinimumSize(new Dimension(310, 120));
        card.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16),
                UIConstants.GRAY_SOFT, 0.5f, 10));


        // 왼쪽 텍스트 영역
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_SEMIBOLD_14);
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(UIConstants.FONT_REGULAR_14);
        descLabel.setForeground(UIConstants.TEXT_SECONDARY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(descLabel);

        if (timeText != null && !timeText.isEmpty()) {
            JLabel timeLabel = new JLabel(timeText);
            timeLabel.setFont(UIConstants.FONT_REGULAR_14);
            timeLabel.setForeground(UIConstants.TEXT_SECONDARY);
            textPanel.add(Box.createVerticalStrut(4));
            textPanel.add(timeLabel);
        }

        card.add(textPanel, BorderLayout.CENTER);

        // 🔹 오른쪽 사진 영역 (선택적)
        if (imagePath != null && !imagePath.isBlank()) {
            ImageIcon raw = new ImageIcon(imagePath);
            ImageIcon thumb = resizeIcon(raw, 95, 95);

            JLabel photoLabel = new JLabel(thumb);
            photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            photoLabel.setVerticalAlignment(SwingConstants.CENTER);

            JPanel photoPanel = new JPanel(new BorderLayout());
            photoPanel.setOpaque(false);
            photoPanel.setPreferredSize(new Dimension(95, 95)); // 카드 오른쪽 영역 고정 폭
            photoPanel.add(photoLabel, BorderLayout.CENTER);

            card.add(photoPanel, BorderLayout.EAST);
        }
        // 👉 사진이 없으면 EAST에 아무 것도 안 붙이니까 자연스럽게 비어 있음

        return card;
    }

    private JButton createSearchButton() {
        JButton btn = new JButton();
        btn.setIcon(new FlatSVGIcon("icons/search.svg", 22, 22));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(32, 32));
        btn.setMargin(new Insets(0, 0, 0, 0));

        // 나중에 검색 기능 연결
        // btn.addActionListener(...);

        return btn;
    }

    /* ================== 하단 네비게이션 그대로 재사용 ================== */
//    private JComponent createTabbedNav() {
//        JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM);
//
//        tabs.putClientProperty("JTabbedPane.tabWidthMode", "equal");
//        tabs.putClientProperty("JTabbedPane.tabAreaAlignment", "fill");
//        tabs.setPreferredSize(new Dimension(0, 60));
//        tabs.putClientProperty("JTabbedPane.tabHeight", 59);
//        tabs.putClientProperty("JTabbedPane.tabAreaInsets", "0,0,0,0");
//        tabs.putClientProperty("JTabbedPane.contentAreaInsets", "0,0,0,0");
//        tabs.setBorder(null);
//
//        tabs.addTab("홈", new JPanel());
//        tabs.setTabComponentAt(0, createTab("홈", "calendar.png"));
//
//        tabs.addTab("캘린더", new JPanel());
//        tabs.setTabComponentAt(1, createTab("캘린더", "calendar.png"));
//
//        tabs.addTab("기록", new JPanel());
//        tabs.setTabComponentAt(2, createTab("기록추가", "calendar.png"));
//
//        tabs.addTab("매거진", new JPanel());
//        tabs.setTabComponentAt(3, createTab("펫 간단팁", "calendar.png"));
//
//        tabs.addTab("설정", new JPanel());
//        tabs.setTabComponentAt(4, createTab("설정", "calendar.png"));
//
//        return tabs;
//    }
//
    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
//
//    private Component createTab(String title, String iconPath) {
//        JPanel tab = new JPanel(new BorderLayout());
//        tab.setOpaque(false);
//
//        ImageIcon original = new ImageIcon(iconPath);
//        ImageIcon smallIcon = resizeIcon(original, 20, 20);
//
//        JLabel label = new JLabel(title, smallIcon, JLabel.CENTER);
//        label.setFont(UIConstants.FONT_SEMIBOLD_12);
//        label.setHorizontalTextPosition(JLabel.CENTER);
//        label.setVerticalTextPosition(JLabel.BOTTOM);
//        label.setIconTextGap(5);
//        label.setForeground(Color.WHITE);
//
//        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
//        tab.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
//
//        tab.add(label, BorderLayout.CENTER);
//        return tab;
//    }
}
