package ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;
import core.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class HomePanel extends Base {

    private final MainFrame mainFrame;
    private User user;  // 로그인한 유저
    private Pet pet;    // 로그인한 유저의 펫

    public HomePanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.user = mainFrame.getLoggedInUser();
        this.pet = mainFrame.getLoggedInUserPet();

        // 로그인한 user, pet 받아오기 (없으면 null일 수 있음)
        this.user = mainFrame.getLoggedInUser();
        this.pet = mainFrame.getLoggedInUserPet();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(
                UIComponents.createHeader(this::doLogout),
                        BorderLayout.NORTH);
        contentWrapper.add(createScrollableContent(), BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

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

        JLabel welcomeLabel = new JLabel(user.getName() + "님 안녕하세요!");
        welcomeLabel.setFont(UIConstants.FONT_SEMIBOLD_18);
        welcomeLabel.setForeground(UIConstants.TEXT_PRIMARY);
        content.add(welcomeLabel, gbc);

        // 0: 펫 카드
        gbc.gridy++;
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
        content.add(createSmallCard("오늘 복용 루틴",
                        new String[]{
                                "오늘 먹어야 할",
                                "약 체크하기"
                        },
                        () -> mainFrame.switchPanel(new MedicineRoutinePanel(mainFrame))
                ),
                gbc
        );

        gbc.gridx = 1;
        gbc.insets = cardInsetsRight;
        content.add(createSmallCard("건강 기록",
                        new String[]{
                                "식사량, 음수량,",
                                "체중, 이상 증상,",
                                "메모 기록"
                        },
                        () -> mainFrame.switchPanel(new HealthPanel(mainFrame))
                ),
                gbc
        );


        // 3: 2x2 카드 (아래 줄)
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.insets = cardInsets;
        content.add(createSmallCard("의료 기록",
                        new String[]{
                                "복약 기록,",
                                "예방접종, 진료 기록을",
                                "한 곳에서 관리"
                        },
                        () -> mainFrame.switchPanel(new MedicalHomePanel(mainFrame
                        ))
                ),
                gbc);

        if (pet.getSpecies().contains("고양이")) {
            gbc.gridx = 1;
            gbc.insets = cardInsetsRight;
            content.add(createSmallCard("놀이 기록",
                            new String[]{
                                    "놀이 시간, 산책 방식,",
                                    "메모 기록"
                            },
                            () -> mainFrame.switchPanel(new PlayPanel(mainFrame))
                    ),
                    gbc);
        } else {
            gbc.gridx = 1;
            gbc.insets = cardInsetsRight;
            content.add(createSmallCard("산책 기록",
                            new String[]{
                                    "산책 거리, 산책 시간,",
                                    "사진 등록"
                            },
                            () -> mainFrame.switchPanel(new WalkPanel(mainFrame))
                    ),
                    gbc);
        }

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
        card.setBorder(new FlatLineBorder(new Insets(10, 10, 10, 10),
                UIConstants.GRAY_SOFT, 0.5f, 20));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 손모양
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
//                mainFrame.switchPanel(new PetInfoPanel(mainFrame));
                System.out.println("구현중");
            }
        });

        JComponent profileArea = createProfileArea(pet.getImagePath());
        profileArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(profileArea, BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(0, 4, 0, 0));

        // 펫 정보 표시 영역
        JLabel name = new JLabel(pet.getName());
        name.setFont(UIConstants.FONT_BOLD_18);
        name.setForeground(UIConstants.TEXT_PRIMARY);

        // 성별 아이콘
        String genderIconPath = pet.getGender().contains("암컷")
                ? "icons/female.svg" : "icons/male.svg";
        JLabel genderIcon = new JLabel(new FlatSVGIcon(genderIconPath, 12, 12));
        genderIcon.setBorder(new EmptyBorder(0, 5, 0, 0));

        // 펫 이름 + 성별
        JPanel nameRow = new JPanel();
        nameRow.setOpaque(false);
        nameRow.setLayout(new BoxLayout(nameRow, BoxLayout.X_AXIS));
        nameRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameRow.add(name);
        nameRow.add(genderIcon);

        // 종 + 생일
        String line = pet.getSpecies() + " · " + pet.getBirthDate();
        JLabel breed = new JLabel(line);
        breed.setFont(UIConstants.FONT_REGULAR_12);
        breed.setForeground(UIConstants.TEXT_LIGHT);

        // 생일 d-day
        long days = pet.getBirthDateDDay();
        String dDayText = (days == 0) ? "생일 축하합니다!" : "생일 D-" + days;

        JLabel dday = new JLabel(dDayText);
        dday.setFont(UIConstants.FONT_SEMIBOLD_14);
        dday.setForeground(UIConstants.PRIMARY);

        center.add(Box.createVerticalStrut(10));
        center.add(nameRow);
        center.add(Box.createVerticalStrut(4));
        center.add(breed);
        center.add(Box.createVerticalStrut(8));
        center.add(dday);

        card.add(center, BorderLayout.CENTER);

        JPanel east = new JPanel();
        east.setOpaque(false);
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        east.setMaximumSize(new Dimension(40, Integer.MAX_VALUE));

        // 위·아래 중앙 정렬 유지
        east.add(Box.createVerticalGlue());
        east.add(Box.createVerticalGlue());

        card.add(east, BorderLayout.EAST);

        return card;
    }

    /* ================== 알림 배너 ================== */
    private JComponent createAlertBanner() {
        JPanel banner = new JPanel(new BorderLayout(8, 0));
        banner.setBackground(UIConstants.PRIMARY_LIGHT);
        banner.setBorder(new FlatLineBorder(new Insets(10, 10, 10, 10),
                UIConstants.GRAY_SOFT, 0.5f, 20));

        JLabel icon = new JLabel("⚠");
        JLabel text = new JLabel("4일 후 병원 진료가 예정되어 있습니다.");
        JButton btn = new JButton("진료 예약정보");
        btn.setOpaque(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.putClientProperty(FlatClientProperties.STYLE,
                "arc:10; background:#FFFFFF; borderColor:#C8C8C8; borderWidth:1;" +
                "minimumWidth:0; margin:3,6,3,6");
//        btn.setBorder(new FlatLineBorder(new Insets(5, 5, 5, 5),
//                UIConstants.GRAY_SOFT, 0.5f, 10));
        btn.addActionListener(e -> JOptionPane.showMessageDialog(
                this, "구현중"));

        banner.add(icon, BorderLayout.WEST);
        banner.add(text, BorderLayout.CENTER);
        banner.add(btn, BorderLayout.EAST);

        return banner;
    }

    /* ================== 작은 카드 (2x2 영역) ================== */
    private JComponent createSmallCard(String title, String[] lines, Runnable onClick) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new FlatLineBorder(new Insets(15, 15, 15, 15),
                UIConstants.GRAY_SOFT, 0.5f, 20));

        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 손모양
        card.setBackground(new Color(253, 253, 253));

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
            card.add(Box.createVerticalStrut(2));
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
        box.setBackground(new Color(253, 253, 253));
        box.putClientProperty("FlatLaf.style", "arc:20");
        box.setBorder(new FlatLineBorder(
                new Insets(15, 15, 15, 15),
                UIConstants.GRAY_SOFT,
                0.5f,
                20
        ));

        JLabel title = new JLabel("최근 활동 타임라인");
        title.setFont(UIConstants.FONT_SEMIBOLD_16);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(title);
        box.add(Box.createVerticalStrut(10));

        // 데이터 가져오기
        java.util.List<TimelineItem> timeline = collectTimelineData();

        if (timeline.isEmpty()) {
            JLabel empty = new JLabel("최근 30일 동안 활동이 없습니다.");
            empty.setFont(UIConstants.FONT_REGULAR_14);
            empty.setForeground(UIConstants.TEXT_SECONDARY);
            box.add(empty);
            return box;
        }

        // 타임라인 추가
        for (TimelineItem item : timeline) {
            box.add(createTimelineCard(item));
            box.add(Box.createVerticalStrut(10));
        }

        return box;
    }

    private void doLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "로그아웃하시겠습니까?",
                "로그아웃",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            mainFrame.switchPanel(new LoginPanel(mainFrame));
        }
    }

    /* ================== 타임라인 아이템 DTO ================== */
    private static class TimelineItem {
        LocalDate date;
        String type;
        String title;
        String detail;

        TimelineItem(LocalDate date, String type, String title, String detail) {
            this.date = date;
            this.type = type;
            this.title = title;
            this.detail = detail;
        }
    }

    /* ================== 최근 30일 데이터 모으기 ================== */
    private java.util.List<TimelineItem> collectTimelineData() {

        java.util.List<TimelineItem> list = new java.util.ArrayList<>();

        // 최근 90일 내 데이터만 수집
        LocalDate today = LocalDate.now();
        LocalDate minDate = today.minusDays(90);

        /* ----------- 진료 기록 ----------- */
        for (MedicalRecord r : medicalMgr.getAllByOwner(user)) {
            if (r.getDate().isBefore(minDate)) continue;
            list.add(new TimelineItem(
                    r.getDate(),
                    "진료",
                    r.getCategory(),
                    r.getHospital()
            ));
        }

        /* ----------- 복용 기록 ----------- */
        for (MedicineRecord r : medicineRecordMgr.getAllByOwner(user)) {
            if (r.getTakenDate().isBefore(minDate)) continue;
            list.add(new TimelineItem(
                    r.getTakenDate(),
                    "복용",
                    r.getMedicineName(),
                    r.getTakenTime() + " | " + r.getDosage() + "mg"
            ));
        }

        /* ----------- 백신 기록 ----------- */
        for (VaccineRecord r : vaccineMgr.getAllByOwner(user)) {
            if (r.getDate().isBefore(minDate)) continue;
            list.add(new TimelineItem(
                    r.getDate(),
                    "백신",
                    r.getVaccine(),
                    r.getHospital()
            ));
        }

        /* ----------- 산책 기록 ----------- */
        for (WalkRecord r : walkMgr.getAllByOwner(user)) {
            if (r.getRecordDate().isBefore(minDate)) continue;
            list.add(new TimelineItem(
                    r.getRecordDate(),
                    "산책",
                    r.getWalkTime() + "분 산책",
                    r.getMemo() == null ? "" : r.getMemo()
            ));
        }

        /* ----------- 건강 기록 ----------- */
        for (HealthRecord r : healthMgr.getAllByOwner(user)) {
            if (r.getRecordDate().isBefore(minDate)) continue;
            list.add(new TimelineItem(
                    r.getRecordDate(),
                    "건강",
                    "몸무게: " + r.getWeight() + "kg",
                    r.getMemo()
            ));
        }

        // 날짜 최신순 정렬
        list.sort((a, b) -> b.date.compareTo(a.date));

        // 미래 기록 포함 안함
        list.removeIf(item -> item.date.isAfter(today));

        // 최근 10건만 노출
        int limit = 10;
        if (list.size() > limit)
            list = list.subList(0, limit);

        return list;
    }

    /* ================== 타임라인 카드 UI ================== */
    private JPanel createTimelineCard(TimelineItem item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(Color.WHITE);

        card.setBorder(new FlatLineBorder(
                new Insets(10, 12, 10, 12),
                UIConstants.GRAY_LIGHT,
                0.8f,
                16
        ));

        // 아이콘 영역
        JPanel iconBox = new JPanel(new GridBagLayout());
        iconBox.setOpaque(false);
        iconBox.setPreferredSize(new Dimension(52, 50));
        iconBox.setBorder(new FlatLineBorder(
                new Insets(0, 0, 0, 0),
                UIConstants.GRAY_LIGHT,
                0,
                12
        ));
        iconBox.putClientProperty(
                FlatClientProperties.STYLE,
                "arc: 10"
        );

        String iconPath;
        Color iconColor;
        Color iconBoxColor;
        switch (item.type) {
            case "진료" -> {
                iconPath = "icons/medical.svg";
                iconColor = new Color(110, 122, 104);
                iconBoxColor = new Color(242, 245, 242);
            }
            case "복용" -> {
                iconPath = "icons/pill.svg";
                iconColor = new Color(99, 105, 125);
                iconBoxColor = new Color(241, 243, 245);
            }
            case "백신" -> {
                iconPath = "icons/syringe.svg";
                iconColor = new Color(112, 96, 116);
                iconBoxColor = new Color(238, 237, 242);
            }
            case "산책" -> {
                iconPath = "icons/walking-dog.svg";
                iconColor = new Color(94, 119, 106);
                iconBoxColor = new Color(237, 242, 240);
            }
            case "건강" -> {
                iconPath = "icons/heart.svg";
                iconColor = new Color(112, 89, 89);
                iconBoxColor = new Color(245, 241, 241);
            }
            default -> {
                iconPath = "icons/paw2.svg";
                iconColor = new Color(92, 92, 92);
                iconBoxColor = new Color(247, 247, 247);
            }
        }
        FlatSVGIcon icon = new FlatSVGIcon(iconPath, 20, 20);
        icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> iconColor));
        iconBox.setBackground(iconBoxColor);

        JLabel iconLabel = new JLabel(icon);
        iconBox.add(iconLabel);

        // 텍스트 영역
        JPanel textArea = new JPanel();
        textArea.setOpaque(false);
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
        textArea.setBorder(new EmptyBorder(0, 12, 0, 0));

        JLabel title = new JLabel(item.type + " · " + item.title);
        title.setFont(UIConstants.FONT_SEMIBOLD_14);
        title.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel detail = new JLabel(item.detail == null ? "" : item.detail);
        detail.setFont(UIConstants.FONT_REGULAR_12);
        detail.setForeground(UIConstants.TEXT_SECONDARY);

        JLabel date = new JLabel(item.date.toString());
        date.setFont(UIConstants.FONT_REGULAR_12);
        date.setForeground(Color.GRAY);

        textArea.add(title);
        textArea.add(Box.createVerticalStrut(4));
        textArea.add(detail);
        textArea.add(Box.createVerticalStrut(2));
        textArea.add(date);

        card.add(iconBox, BorderLayout.WEST);
        card.add(textArea, BorderLayout.CENTER);

//        card.add(title, BorderLayout.NORTH);
//        card.add(detail, BorderLayout.CENTER);
//        card.add(date, BorderLayout.SOUTH);

        return card;
    }

    private JComponent createProfileArea(String imagePath) {
        final Image image;
        final FlatSVGIcon svgIcon;

        Image tmpImage = null;
        FlatSVGIcon tmpSvg = null;

        java.net.URL url = null;
        try {
            if (imagePath != null && !imagePath.isBlank()) {
                url = getClass().getResource(imagePath);
            }
            if (url != null) {
                tmpImage = new ImageIcon(url).getImage();
            }
        } catch (Exception ignored) {}

        if (tmpImage == null) {
            tmpSvg = new FlatSVGIcon("icons/default-profile.svg", 78, 78);
        }

        image = tmpImage;
        svgIcon = tmpSvg;

        JPanel panel = new JPanel() {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(96, 96);
            }

            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = Math.min(getWidth(), getHeight());
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                // 배경 원
                g2.setColor(new Color(0xF5F7FB));
                g2.fillOval(x, y, size, size);

                Shape clip = new java.awt.geom.Ellipse2D.Double(x, y, size, size);
                g2.setClip(clip);

                if (image != null) {
                    g2.drawImage(image, x, y, size, size, this);
                } else if (svgIcon != null) {
                    svgIcon.paintIcon(this, g2, x, y);
                }

                g2.setClip(null);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return panel;
    }
}
