package uitest;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;
import core.MedicalMgr;
import core.Pet;
import core.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
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
            () -> mainFrame.switchPanel(new MedicalRecordListPanel(mainFrame, MedicalMgr.getInstance().mList))
        //mainFrame.switchPanel(new MedicineRoutinePanel(mainFrame))
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
        card.setBorder(new FlatLineBorder(new Insets(10, 10, 10, 10),
                UIConstants.GRAY_SOFT, 0.5f, 20));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 손모양
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.switchPanel(new PetInfoPanel(mainFrame));
            }
        });

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

        JPanel east = new JPanel();
        east.setOpaque(false);
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
//        east.add(Box.createVerticalStrut(4));              // 위에 살짝 여백
        // 🔥 폭 줄이기 → 아이콘 버튼 폭 + 약간의 여백
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
        box.setBackground((new Color(253, 253, 253)));
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
}
