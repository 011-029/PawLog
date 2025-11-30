package uitest;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddRecordMenuPanel extends JPanel {

    private final MainFrame mainFrame;

    public AddRecordMenuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ⬇ 헤더 + 내용 패딩 래퍼
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new PetHomePanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createScrollableContent(), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    /* ================== 상단바 (PetHomePanel과 동일) ================== */
//    private JComponent createHeaderBar() {
//        JPanel header = new JPanel(new BorderLayout());
//        header.setOpaque(false);
//
//        JButton backBtn = new JButton();
//        backBtn.setIcon(new FlatSVGIcon("icons/arrow-prev.svg", 20, 20));
//        backBtn.setFocusPainted(false);
//        backBtn.setBorderPainted(false);
//        backBtn.setContentAreaFilled(false);
//        backBtn.addActionListener(e -> mainFrame.switchPanel(new PetHomePanel(mainFrame)));
//
//        JLabel logo = new JLabel("PawLog", SwingConstants.CENTER);
//        logo.setFont(UIConstants.FONT_BOLD_24);
//        logo.setForeground(UIConstants.TEXT_PRIMARY);
//
//        JButton bellBtn = new JButton("아이콘");
//        bellBtn.setFocusPainted(false);
//        bellBtn.setPreferredSize(new Dimension(40, 40));
//        bellBtn.setBackground(Color.WHITE);
//        bellBtn.setBorder(new FlatLineBorder(new Insets(5, 5, 5, 5),
//                UIConstants.GRAY_SOFT, 0.5f, 10));
//
//        header.add(backBtn, BorderLayout.WEST);
//        header.add(logo, BorderLayout.CENTER);
//        header.add(bellBtn, BorderLayout.EAST);
//
//        return header;
//    }

    /* ================== 가운데 스크롤 영역 ================== */
    private JComponent createScrollableContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(true);
        root.setBackground(Color.WHITE);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(24, 10, 0, 10));
        root.setAlignmentY(0f);   // ★ 추가

        // 상단 제목
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(24, 0, 16, 0));

        JLabel title = new JLabel("추가할 기록을 선택해주세요");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        titlePanel.add(title, BorderLayout.WEST);

        root.add(titlePanel, BorderLayout.NORTH);

        // 2열 카드 그리드
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.0;

        int row = 0;

        // 첫 줄: 건강 기록 / 병원 진료 기록
        gbc.gridy = row++;
        gbc.gridx = 0;
        grid.add(createRecordCard("건강 기록",
                "체중, 식사량, 이상증상 등",
                "icons/heart.svg",
                () -> mainFrame.switchPanel(new HealthFormPanel(mainFrame))), gbc); // TODO: onClick 연결

        gbc.gridx = 1;
        grid.add(createRecordCard("병원 진료 기록",
                "예약된 진료, 과거 진료 이력",
                "icons/medical.svg",
                () -> mainFrame.switchPanel(new MedicalFormPanel(mainFrame))), gbc);

        // 둘째 줄: 복용 기록 / 복용 루틴
        gbc.gridy = row++;
        gbc.gridx = 0;
        grid.add(createRecordCard("복용 기록",
                "약 이름, 용량 등 복약 이력",
                "icons/pill.svg",
                () -> mainFrame.switchPanel(new MedicineFormPanel(mainFrame))), gbc);

        gbc.gridx = 1;
        grid.add(createRecordCard("복용 루틴",
                "주기적인 복약 시 리마인더",
                "icons/reminder.svg",
                () -> mainFrame.switchPanel(new MedicineRoutineFormPanel(mainFrame))), gbc);

        // 셋째 줄: 산책 기록 / 놀이 기록
        gbc.gridy = row++;
        gbc.gridx = 0;
        grid.add(createRecordCard("산책 기록",
                "산책 시간, 산책 거리 등",
                "icons/walking-dog.svg",
                () -> mainFrame.switchPanel(new WalkFormPanel(mainFrame))), gbc);

        gbc.gridx = 1;
        grid.add(createRecordCard("놀이 기록",
                "놀이 시간, 놀이 방식 등",
                "icons/toy.svg",
                () -> mainFrame.switchPanel(new PlayFormPanel(mainFrame))), gbc);

        // 넷째 줄: 백신 기록 (왼쪽만 사용, 오른쪽은 빈칸 스페이서)
        gbc.gridy = row++;
        gbc.gridx = 0;
        grid.add(createRecordCard("예방접종 기록",
                "예정된 접종, 과거 접종 이력",
                "icons/syringe.svg",
                () -> mainFrame.switchPanel(new VaccineFormPanel(mainFrame))), gbc);

        gbc.gridx = 1;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        grid.add(spacer, gbc);

        // 아래 여백용 더미
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        JPanel bottomSpace = new JPanel();
        bottomSpace.setOpaque(false);
        grid.add(bottomSpace, gbc);

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        root.add(scroll, BorderLayout.CENTER);
        return root;
    }

    /* ================== 기록 선택 카드 ================== */
    private JComponent createRecordCard(String labelText, String description,
                                        String iconPath, Runnable onClick) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new FlatLineBorder(new Insets(20, 10, 20, 10),
                UIConstants.GRAY_SOFT, 0.5f, 20));
        card.setPreferredSize(new Dimension(160, 150));
//        card.setMaximumSize(new Dimension(160, 140));
//        card.setMinimumSize(new Dimension(100, 140));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // 호버 효과
        card.addMouseListener(new MouseAdapter() {
            Color original = card.getBackground();

            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) onClick.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(UIConstants.GRAY_ULTRA_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(original);
            }
        });

        // 아이콘 자리
        JLabel iconLabel = new JLabel();
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (iconPath != null) {
            iconLabel.setIcon(new FlatSVGIcon(iconPath, 40, 40));

        } else {
            // 아이콘 없으면 X 박스 표시
//            iconLabel = new XIconBox();
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        JLabel label = new JLabel(labelText);
        label.setFont(UIConstants.FONT_SEMIBOLD_16);
        label.setForeground(UIConstants.TEXT_PRIMARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(UIConstants.FONT_REGULAR_12);
        descLabel.setForeground(UIConstants.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(4));
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(9));
        card.add(label);
        card.add(Box.createVerticalStrut(3));
        card.add(descLabel);
//        card.add(Box.createVerticalStrut(2));

        return card;
    }


//    // X 표시 사각형 컴포넌트 (아이콘 자리)
//    private static class XIconBox extends JComponent {
//        public XIconBox() {
//            setPreferredSize(new Dimension(140, 140));
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            Graphics2D g2 = (Graphics2D) g.create();
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//            int w = getWidth() - 1;
//            int h = getHeight() - 1;
//
//            g2.setColor(Color.WHITE);
//            g2.fillRect(0, 0, w + 1, h + 1);
//
//            g2.setColor(UIConstants.TEXT_PRIMARY);
//            g2.setStroke(new BasicStroke(3f));
//            g2.drawRect(0, 0, w, h);
//            g2.drawLine(0, 0, w, h);
//            g2.drawLine(0, h, w, 0);
//
//            g2.dispose();
//        }
//    }

//    /* ================== 하단 네비게이션 (PetHomePanel과 동일 스타일) ================== */
//    private JComponent createTabbedNav() {
//        JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM);
//
//        // 폭은 균등 분배 + 전체 폭 채우기
//        tabs.putClientProperty("JTabbedPane.tabWidthMode", "equal");
//        tabs.putClientProperty("JTabbedPane.tabAreaAlignment", "fill");
//
//        // ▶ 탭 스트립 높이 자체를 줄이기
//        tabs.setPreferredSize(new Dimension(0, 60));              // 전체 바 높이
//        tabs.putClientProperty("JTabbedPane.tabHeight", 59);      // 탭 셀 높이
//
//        // ▶ 탭 영역 위·아래 여백 최소화 (콘텐츠와 탭 사이 간격)
//        tabs.putClientProperty("JTabbedPane.tabAreaInsets", "0,0,0,0");
//        // 필요하면 contentAreaInsets도 0으로
//        tabs.putClientProperty("JTabbedPane.contentAreaInsets", "0,0,0,0");
//
//        tabs.setBorder(null);
//
//        tabs.addTab("홈", new JPanel());
//        tabs.setTabComponentAt(0, createTab("홈", "icons/home.svg"));
//
//        tabs.addTab("캘린더", new JPanel());
//        tabs.setTabComponentAt(1, createTab("캘린더", "icons/calendar.svg"));
//
//        tabs.addTab("기록", new JPanel());
//        tabs.setTabComponentAt(2, createTab("기록추가", "icons/add.svg"));
//
//        tabs.addTab("매거진", new JPanel());
//        tabs.setTabComponentAt(3, createTab("펫 간단팁", "icons/notes.svg"));
//
//        tabs.addTab("설정", new JPanel());
//        tabs.setTabComponentAt(4, createTab("설정", "icons/setting.svg"));
//
//        // tabs 생성/설정들 뒤에
//        tabs.addChangeListener(e -> {
//            int idx = tabs.getSelectedIndex();
//
//            if (idx == 1)
//                mainFrame.switchPanel(new CalendarPanel(mainFrame));
//            else if (idx == 0)
//                mainFrame.switchPanel(new PetHomePanel(mainFrame));
//            else if (idx == 2)
//                mainFrame.switchPanel(new AddRecordMenuPanel(mainFrame));
//            else if (idx == 3)
//                mainFrame.switchPanel(new WalkFormPanel(mainFrame));
//            // 다른 탭들도 필요하면 else if 로 추가~
//        });
//
//        return tabs;
//    }
//
//    /* ================== 공통: 아이콘 리사이즈 / 탭 컴포넌트 ================== */
//    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
//        Image img = icon.getImage();
//        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//        return new ImageIcon(scaled);
//    }
//
//    private Component createTab(String title, String iconPath) {
//        JPanel tab = new JPanel(new BorderLayout());
//        tab.setOpaque(false);
//
//        ImageIcon icon = new FlatSVGIcon(iconPath, 20, 20);
//        JLabel label = new JLabel(title, icon, JLabel.CENTER);
//        label.setFont(UIConstants.FONT_SEMIBOLD_12);
//        label.setHorizontalTextPosition(JLabel.CENTER);
//        label.setVerticalTextPosition(JLabel.BOTTOM);
//        label.setIconTextGap(5);
//        label.setForeground(Color.WHITE);
//        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
//
//        tab.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
//        tab.add(label, BorderLayout.CENTER);
//        return tab;
//    }
}
