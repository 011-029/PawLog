package uitest;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MedicineRoutinePanel extends JPanel {

    private final MainFrame mainFrame;

    public MedicineRoutinePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);   // 없으면 Color.WHITE 써도 됨

        // ⬇ 헤더 + 스크롤 콘텐츠만 패딩을 주는 래퍼
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new PetHomePanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createContent(), BorderLayout.CENTER);

        // 가운데는 패딩 있는 래퍼
        add(contentWrapper, BorderLayout.CENTER);

        // ⬇ 하단 탭바는 패딩 없는 SOUTH에 바로!
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
//
//
//        header.add(backBtn, BorderLayout.WEST);
//        header.add(logo, BorderLayout.CENTER);
//        header.add(bellBtn, BorderLayout.EAST);
//
//        return header;
//    }

    /** 가운데 전체 영역 */
    private JComponent createContent() {
        // 스크롤 가능 리스트로 만들기
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));
        listPanel.setAlignmentY(0f);   // ★ 추가

        // 🔹 제목 + 검색 아이콘 한 줄
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("복용 루틴");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);

        JButton filterBtn = new JButton();
        filterBtn.setIcon(new FlatSVGIcon("icons/filter.svg", 22, 22));
        filterBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        filterBtn.setContentAreaFilled(false);
        filterBtn.setBorderPainted(false);
        filterBtn.setFocusPainted(false);
        filterBtn.setOpaque(false);
        filterBtn.setPreferredSize(new Dimension(32, 32));

        // 오른쪽 정렬 후 버튼 추가
        header.add(Box.createHorizontalGlue());
        header.add(filterBtn);   // ⬅ 이거!
        header.add(Box.createHorizontalStrut(8));   // ← 여기에 8px 여백 추가!
        header.add(createSearchButton());


        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        // 🔹 오늘 복용 루틴 섹션
        JLabel todayLabel = createSectionLabel("오늘 복용 루틴");
        listPanel.add(todayLabel);
        listPanel.add(Box.createVerticalStrut(10));

        // --- 아래는 그대로 카드들 추가 ---
        JPanel card1 = createRoutineCard(
                "복용루틴 1",
                "심장사상충약 / 하루 2알",
                "10:00AM , 2:00PM"
        );
        listPanel.add(card1);
        listPanel.add(Box.createVerticalStrut(16));

        JPanel card2 = createRoutineCard(
                "복용루틴 2",
                "약 정보를 입력해주세요",
                ""
        );
        listPanel.add(card2);
        listPanel.add(Box.createVerticalStrut(16));

        // 🔹 전체 루틴 섹션
        listPanel.add(Box.createVerticalStrut(8));
        JLabel allLabel = createSectionLabel("전체 루틴");
        listPanel.add(allLabel);
        listPanel.add(Box.createVerticalStrut(10));


        JPanel card3 = createRoutineCard(
                "복용루틴 2",
                "약 정보를 입력해주세요",
                ""
        );
        listPanel.add(card3);
        listPanel.add(Box.createVerticalStrut(16));

        JPanel card4 = createRoutineCard(
                "복용루틴 2",
                "약 정보를 입력해주세요",
                ""
        );
        listPanel.add(card4);
        listPanel.add(Box.createVerticalStrut(16));

        JPanel card5 = createRoutineCard(
                "복용루틴 2",
                "약 정보를 입력해주세요",
                ""
        );
        listPanel.add(card5);
        listPanel.add(Box.createVerticalStrut(16));

        JPanel card6 = createRoutineCard(
                "복용루틴 2",
                "약 정보를 입력해주세요",
                ""
        );
        listPanel.add(card6);
        listPanel.add(Box.createVerticalStrut(16));

        // 🔹 리스트를 한 번 더 싸서 항상 위쪽에 붙도록
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);   // ⭐ 중요: NORTH에 붙이기

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(30);

        return scroll;
    }

    /** 오늘 복용 루틴 / 전체 루틴 섹션 제목 라벨 */
    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_SEMIBOLD_18);
        label.setForeground(UIConstants.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 4, 4, 4)); // 살짝 패딩만
        return label;
    }

    /** 개별 복용 루틴 카드 */
    private JPanel createRoutineCard(String title, String desc, String timeText) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setPreferredSize(new Dimension(310, 100));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16),
                UIConstants.GRAY_SOFT, 0.5f, 10));

        // 텍스트 영역 (왼쪽)
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

        if (!timeText.isEmpty()) {
            JLabel timeLabel = new JLabel(timeText);
            timeLabel.setFont(UIConstants.FONT_REGULAR_14);
            timeLabel.setForeground(UIConstants.TEXT_SECONDARY);
            textPanel.add(Box.createVerticalStrut(4));
            textPanel.add(timeLabel);
        }

        card.add(textPanel, BorderLayout.CENTER);

        JCheckBox checkBox = new JCheckBox();
        checkBox.setOpaque(false);
        checkBox.setFocusPainted(false);
        checkBox.setBorderPainted(false);
        checkBox.setContentAreaFilled(false);

        // 둥근 스타일 유지
        checkBox.putClientProperty("JCheckBox[styleClass]", "round");


        // 나중에 실제 삭제 로직 연결
        // checkBtn.addActionListener(e -> checkRoutine(...));

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(Box.createVerticalGlue());
        right.add(checkBox);
        right.add(Box.createVerticalGlue());
        right.setBorder(new EmptyBorder(0, 8, 0, 0)); // 카드 오른쪽 여백 조금만

        card.add(right, BorderLayout.EAST);

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

        // 살짝 여유
        btn.setPreferredSize(new Dimension(32, 32));
        btn.setMargin(new Insets(0, 0, 0, 0));

        // TODO: 눌렀을 때 검색 기능 연결
        btn.addActionListener(e -> mainFrame.switchPanel(new SearchPanel(mainFrame)));

        return btn;
    }



    /* ================== 하단 네비게이션 ================== */
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
//        ImageIcon original = new ImageIcon(iconPath);
//        ImageIcon smallIcon = resizeIcon(original, 20, 20);
//
//        JLabel label = new JLabel(title, smallIcon, JLabel.CENTER);
//        label.setFont(UIConstants.FONT_SEMIBOLD_12);
//        label.setHorizontalTextPosition(JLabel.CENTER);
//        label.setVerticalTextPosition(JLabel.BOTTOM);
//        label.setIconTextGap(5); // 아이콘-텍스트 간격
//        label.setForeground(Color.WHITE);
//
//        // 🔹 라벨 자체 위/아래 패딩 줄이기 (탭 안 여백 감소 핵심!)
//        label.setBorder(BorderFactory.createEmptyBorder(
//                0, 0, 1, 0   // top, left, bottom, right
//        ));
//
//        // 🔹 탭 패널의 위/아래 여백도 최소화
//        tab.setBorder(BorderFactory.createEmptyBorder(
//                15, 0, 0, 0   // 위쪽만 살짝, 아래는 0
//        ));
//
//        tab.add(label, BorderLayout.CENTER);
//        return tab;
//    }
}
