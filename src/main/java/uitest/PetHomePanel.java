package uitest;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;
import core.Pet;
import core.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PetHomePanel extends JPanel {

    private final MainFrame mainFrame;
    User user;  // 로그인한 유저
    Pet pet;    // 로그인한 유저의 펫

    public PetHomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // 로그인한 user, pet 받아오기 (없으면 null일 수 있음)
        this.user = mainFrame.getLoggedInUser();
        this.pet = mainFrame.getLoggedInUserPet();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ⬇ 헤더 + 스크롤 콘텐츠만 패딩을 주는 래퍼
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new LoginPanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createScrollableContent(), BorderLayout.CENTER);

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

    /* ================== 가운데 스크롤 영역 ================== */
    private JComponent createScrollableContent() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(true);
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(24, 10, 24, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2; // 전체 폭 사용

        // 0: 펫 카드
        content.add(createPetCard(), gbc);

        // 1: 알림 배너
        gbc.gridy++;
        content.add(createAlertBanner(), gbc);

        // 공통 인셋 (카드 사이에 좌우 여백도 주기)
        Insets cardInsets = new Insets(8, 0, 8, 8);
        Insets cardInsetsRight = new Insets(8, 8, 8, 0);

        // 2: 2x2 카드 (위 줄)
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.insets = cardInsets;
        content.add(createSmallCard("오늘 복용 체크",
                        new String[]{"● 복용 루틴 1", "○ 복용 루틴 2"},
                        () -> mainFrame.switchPanel(new MedicineRoutinePanel(mainFrame))
                ),
                gbc
        );

        gbc.gridx = 1;
        gbc.insets = cardInsetsRight;
        content.add(createSmallCard("건강 기록",
                new String[]{"임시 더미내용", "더미내용 더미내용"},
                () -> mainFrame.switchPanel(new HealthPanel(mainFrame))
                ),
        gbc
    );

        // 3: 2x2 카드 (아래 줄)
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.insets = cardInsets;
        content.add(createSmallCard("의료 기록",
                new String[]{"더미내용 더미내용", "임시 더미 D-10"},
            () -> mainFrame.switchPanel(new MedicineRoutinePanel(mainFrame))
            ),
    gbc);

        gbc.gridx = 1;
        gbc.insets = cardInsetsRight;
        content.add(createSmallCard("산책/놀이 기록",
                new String[]{"더미내용 더미내용 더미내용"},
            () -> mainFrame.switchPanel(new WalkPanel(mainFrame))
            ),
        gbc);

        // 4: 타임라인 (전체 폭)
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        content.add(createTimeline(), gbc);

        // 아래쪽 여백용 더미 (스크롤 위해)
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        content.add(spacer, gbc);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(30);

        return scroll;
    }

    /* ================== 펫 카드 ================== */
    private JComponent createPetCard() {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(Color.WHITE);
//        card.setBorder(new CompoundBorder(
//                new LineBorder(new Color(230, 230, 230), 1, true),
//                new EmptyBorder(12, 12, 12, 12)));
        card.setBorder(new FlatLineBorder(new Insets(10, 10, 10, 10),
                UIConstants.GRAY_SOFT, 0.5f, 20));

        JLabel photo = new JLabel("사진", SwingConstants.CENTER);
        photo.setPreferredSize(new Dimension(96, 96));
        photo.setOpaque(true);
        photo.setBackground(new Color(240, 240, 240));
        photo.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        card.add(photo, BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // 나중에 user/pet 정보로 바꾸면 됨!
        JLabel name = new JLabel("후추");
        name.setFont(UIConstants.FONT_SEMIBOLD_18);
        name.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel breed = new JLabel("갈색 푸들");
        breed.setFont(UIConstants.FONT_REGULAR_12);
        breed.setForeground(UIConstants.TEXT_LIGHT);

        JLabel dday = new JLabel("D-30");
        dday.setFont(UIConstants.FONT_REGULAR_14);

        center.add(name);
        center.add(Box.createVerticalStrut(4));
        center.add(breed);
        center.add(Box.createVerticalStrut(8));
        center.add(dday);

        card.add(center, BorderLayout.CENTER);

//        // 🔹 펫 정보 수정 아이콘 버튼 (오른쪽 상단)
//        ImageIcon editRaw = new ImageIcon("next.png");
//        Image editImg = editRaw.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
//        JButton editBtn = new JButton(new ImageIcon(editImg));

//        editBtn.setFocusPainted(false);
//        editBtn.setContentAreaFilled(false);               // 배경 투명하게
////        editBtn.setBorder(new LineBorder(UIConstants.PRIMARY, 1, true)); // 둥근 테두리
//        editBtn.setPreferredSize(new Dimension(32, 32));   // 정사각형 아이콘 버튼
//        editBtn.setToolTipText("펫 정보 수정");            // 마우스 올리면 힌트

        JPanel east = new JPanel();
        east.setOpaque(false);
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
//        east.add(Box.createVerticalStrut(4));              // 위에 살짝 여백
        // 🔥 폭 줄이기 → 아이콘 버튼 폭 + 약간의 여백
        east.setMaximumSize(new Dimension(40, Integer.MAX_VALUE));

        // 위·아래 중앙 정렬 유지
        east.add(Box.createVerticalGlue());
//        editBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//        east.add(editBtn);
        east.add(Box.createVerticalGlue());

        card.add(east, BorderLayout.EAST);

        return card;
    }

    /* ================== 알림 배너 ================== */
    private JComponent createAlertBanner() {
        JPanel banner = new JPanel(new BorderLayout(8, 0));
//        banner.setBackground(new Color(255, 224, 224));
//        banner.setBorder(new EmptyBorder(10, 12, 10, 12));
//        banner.putClientProperty("FlatLaf.style", "arc:30");
        banner.setBackground(UIConstants.PRIMARY_LIGHT);
        banner.setBorder(new FlatLineBorder(new Insets(10, 10, 10, 10),
                UIConstants.GRAY_SOFT, 0.5f, 20));

        JLabel icon = new JLabel("⚠");
        JLabel text = new JLabel("4일 후 병원 진료가 예정되어 있습니다.");
        JButton btn = new JButton("진료 예약정보");
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
//        btn.setBorder(new LineBorder(new Color(255, 192, 203), 1, true));
        btn.putClientProperty("FlatLaf.style", "arc:10");
        btn.setBorder(new FlatLineBorder(new Insets(5, 5, 5, 5),
                UIConstants.GRAY_SOFT, 0.5f, 10));

        banner.add(icon, BorderLayout.WEST);
        banner.add(text, BorderLayout.CENTER);
        banner.add(btn, BorderLayout.EAST);

        return banner;
    }

    /* ================== 작은 카드 (2x2 영역) ================== */
    private JComponent createSmallCard(String title, String[] lines, Runnable onClick) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
//        card.putClientProperty("FlatLaf.style", ""
//                + "arc:30;"
////                + "borderColor: #E0E0E0;"
////                + "borderInsets: 1,1,1,1;"
////                + "borderWidth: 1;"
//        );
        card.setBorder(new FlatLineBorder(new Insets(15, 15, 15, 15),
                UIConstants.GRAY_SOFT, 0.5f, 20));
//        card.setBackground(UIConstants.GRAY_ULTRA_LIGHT);
//        card.setOpaque(true);
//        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 손모양

        // ⭐ 카드 클릭 시 실행할 동작
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }
        });

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));

        for (String l : lines) {
            JLabel contentLabel = new JLabel(l);
            contentLabel.setFont(UIConstants.FONT_REGULAR_14);
            contentLabel.setForeground(UIConstants.TEXT_SECONDARY);
            card.add(contentLabel);
        }

        card.setPreferredSize(new Dimension(160, 150));
        card.setMaximumSize(new Dimension(160, 150));
        card.setMinimumSize(new Dimension(100, 150));

        return card;
    }

    /* ================== 타임라인 (전체 폭) ================== */
    private JComponent createTimeline() {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(UIConstants.GRAY_ULTRA_LIGHT);
        box.putClientProperty("FlatLaf.style", "arc:20");
        box.setPreferredSize(new Dimension(360, 500));
        box.setBorder(new FlatLineBorder(new Insets(15, 15, 15, 15),
                UIConstants.GRAY_SOFT, 0.5f, 20));

        JLabel title = new JLabel("최근 활동 타임라인");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setFont(UIConstants.FONT_SEMIBOLD_16);
        title.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel item1 = new JLabel("후추 산책 (4.5Km)");
        item1.setFont(UIConstants.FONT_REGULAR_14);
        item1.setForeground(UIConstants.TEXT_SECONDARY);

        JLabel item2 = new JLabel("후추 약 (1개)");
        item2.setFont(UIConstants.FONT_REGULAR_14);
        item2.setForeground(UIConstants.TEXT_SECONDARY);

        JLabel item3 = new JLabel("건강 기록 요약 …");
        item3.setFont(UIConstants.FONT_REGULAR_14);
        item3.setForeground(UIConstants.TEXT_SECONDARY);

        box.add(title);
        box.add(Box.createVerticalStrut(8));
        box.add(item1);
        box.add(Box.createVerticalStrut(4));
        box.add(item2);
        box.add(Box.createVerticalStrut(4));
        box.add(item3);

        return box;
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
//        ImageIcon icon = new FlatSVGIcon(iconPath, 20, 20);
//        JLabel label = new JLabel(title, icon, JLabel.CENTER);
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
