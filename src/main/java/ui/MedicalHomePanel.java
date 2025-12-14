package ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;
import core.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MedicalHomePanel extends Base {

    private final MainFrame mainFrame;
    private User user;
    private Pet pet;
    protected ArrayList<UpcomingCardData> list;

    public MedicalHomePanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.pet = mainFrame.getLoggedInUserPet();
        this.user = mainFrame.getLoggedInUser();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 헤더 + 내용
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));

        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new HomePanel(mainFrame))), BorderLayout.NORTH);

        contentWrapper.add(createContent(), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }


    /* ================================
     *  가운데 전체 콘텐츠
     * ================================ */
    private JComponent createContent() {

        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));  // ★ 수정

        /* 제목 */
        JLabel title = new JLabel("의료 기록");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        listPanel.add(title);
        listPanel.add(Box.createVerticalStrut(16));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createHorizontalGlue());
        header.add(UIComponents.createSearchButton(mainFrame, this));

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(20));

        /* 메뉴 섹션 */
        JLabel menu = createSectionLabel("의료 기록 목록");
        menu.setAlignmentX(Component.LEFT_ALIGNMENT);
//        listPanel.add(menu);
//        listPanel.add(Box.createVerticalStrut(12));

        JPanel btn1 = createMenuButton(
                "진료 기록",
                "icons/medical.svg",
                32, 32,
                () -> mainFrame.switchPanel(new MedicalRecordPanel(mainFrame))
        );
        btn1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel btn2 = createMenuButton("예방접종",
                "icons/syringe.svg",
                36, 36,
                () -> mainFrame.switchPanel(
                        new VaccineRecordPanel(mainFrame)));
        btn2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel btn3 = createMenuButton("복용 기록",
                "icons/pill.svg",
                32, 32,
                () -> mainFrame.switchPanel(
                        new MedicineRecordPanel(mainFrame)));
        btn3.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel menuGrid = new JPanel(new GridLayout(1, 3, 16, 0));
        menuGrid.setOpaque(false);
        menuGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        menuGrid.add(btn1);
        menuGrid.add(btn2);
        menuGrid.add(btn3);

        listPanel.add(menuGrid);
        listPanel.add(Box.createVerticalStrut(36));

        /* 🔔 임박한 일정 */
        List<UpcomingCardData> upcoming = collectUpcoming();

        if (!upcoming.isEmpty()) {
            JLabel up = createSectionLabel("임박한 일정");
            up.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(up);
            listPanel.add(Box.createVerticalStrut(16));

            JPanel card = createUpcomingCard(upcoming);
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(card);

            listPanel.add(Box.createVerticalStrut(28));
        }


        /* 래퍼: BoxLayout 폭 제한 해결 */
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);
        listWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));  // ★ 수정

        /* 스크롤 */
        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(30);

        return scroll;
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_SEMIBOLD_18);
        label.setForeground(UIConstants.TEXT_PRIMARY);
        return label;
    }


    /* 🔔 임박한 일정 카드 */
    private JPanel createUpcomingCard(List<UpcomingCardData> list) {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        card.setBorder(new FlatLineBorder(
                new Insets(16, 16, 16, 16),
                UIConstants.GRAY_SOFT, 0.5f, 10
        ));

        for (int i = 0; i < list.size(); i++) {
            UpcomingCardData data = list.get(i);

            // 한 줄(제목 + D-day) 패널
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);

            // 왼쪽: 제목 + 병원명
            JPanel left = new JPanel();
            left.setOpaque(false);
            left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));

            JLabel title = new JLabel(data.title);
            title.setFont(UIConstants.FONT_SEMIBOLD_14);
            title.setForeground(UIConstants.TEXT_PRIMARY);

            JLabel hospital = new JLabel(data.hospital);
            hospital.setFont(UIConstants.FONT_REGULAR_14);
            hospital.setForeground(UIConstants.TEXT_LIGHT);

            left.add(title);
            left.add(Box.createHorizontalStrut(8)); // 간격!
            left.add(hospital);

            // 오른쪽: 디데이
            JLabel dday = new JLabel(data.ddayText);
            dday.setFont(UIConstants.FONT_SEMIBOLD_14);
            dday.setForeground(UIConstants.PRIMARY);

            row.add(left, BorderLayout.WEST);
            row.add(dday, BorderLayout.EAST);

            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(row);

            // 줄 사이 간격 (마지막 줄 제외)
            if (i != list.size() - 1) {
                card.add(Box.createVerticalStrut(10));
            }
        }

        return card;
    }

    /* 메뉴 버튼 */
    private JPanel createMenuButton(String text, String iconPath, int iconW, int iconH,
                                    Runnable action) {
        JPanel tile = new JPanel();
        tile.setOpaque(true);
        tile.setBackground(Color.WHITE);
        tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));

        int w = 98;
        int h = 110;
        tile.setPreferredSize(new Dimension(w, h));
        tile.setMinimumSize(new Dimension(w, h));
        tile.setMaximumSize(new Dimension(w, h));

        tile.setBorder(new FlatLineBorder(
                new Insets(8, 8, 8, 8),
                UIConstants.GRAY_SOFT, 0.5f, 10
        ));

        JLabel icon = new JLabel(new FlatSVGIcon(iconPath, iconW, iconH));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_SEMIBOLD_16);
        label.setForeground(UIConstants.TEXT_PRIMARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        tile.add(Box.createVerticalStrut(8));
        tile.add(icon);

        tile.add(Box.createVerticalGlue());

        tile.add(label);
        tile.add(Box.createVerticalStrut(8));

        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });

        return tile;
    }


    /* 🔔 임박 일정 수집기 */
    private List<UpcomingCardData> collectUpcoming() {

        list = new ArrayList<>();

        // 진료 기록
        for (MedicalRecord m : medicalMgr.getAllByOwner(user)) {
            if (m.getDDay() >= 0) {
                list.add(new UpcomingCardData(
                        String.format("%s 진료", m.getCategory()),
                        m.getDDay(),
                        m.getDDayText(),
                        m.getHospital()
                ));
            }
        }

        // 예방접종 기록
        for (VaccineRecord v : vaccineMgr.getAllByOwner(user)) {
            if (v.getDDay() >= 0) {
                list.add(new UpcomingCardData(
                        String.format("%s 백신 접종", v.getVaccine()),
                        v.getDDay(),
                        v.getDDayText(),
                        v.getHospital()
                ));
            }
        }

        list.sort(Comparator.comparingLong(a -> a.dDay));

        if (list.size() > 5)
            return list.subList(0, 5);

        return list;
    }

    private record UpcomingCardData(String title, long dDay, String ddayText,
                                    String hospital) { }
}

