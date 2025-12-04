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

    public MedicalHomePanel(MainFrame mainFrame) {
        super(mainFrame);

        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 헤더 + 내용
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));

        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new HomePanel(mainFrame))), BorderLayout.NORTH);

        contentWrapper.add(createContent(MedicalMgr.getInstance().mList), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }


    /* ================================
     *  가운데 전체 콘텐츠
     * ================================ */
    private JComponent createContent(List<MedicalRecord> medical) {

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
        listPanel.add(menu);
        listPanel.add(Box.createVerticalStrut(12));

        JPanel btn1 = createMenuButton(
                "진료 기록",
                "icons/medical.svg",
                () -> mainFrame.switchPanel(new MedicalRecordPanel(mainFrame))
        );
        btn1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel btn2 = createMenuButton("복용 기록", "icons/pill.svg",
                () -> mainFrame.switchPanel(
                        new MedicineRecordPanel(mainFrame)));
        btn2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel btn3 = createMenuButton("예방접종 기록", "icons/syringe.svg",
                () -> mainFrame.switchPanel(
                        new VaccineRecordPanel(mainFrame)));
        btn3.setAlignmentX(Component.LEFT_ALIGNMENT);

        listPanel.add(btn1);
        listPanel.add(Box.createVerticalStrut(16));
        listPanel.add(btn2);
        listPanel.add(Box.createVerticalStrut(16));
        listPanel.add(btn3);
        listPanel.add(Box.createVerticalStrut(16));

        /* 🔔 임박한 일정 */
        List<UpcomingCardData> upcoming = collectUpcoming(medical);

        if (!upcoming.isEmpty()) {
            JLabel up = createSectionLabel("임박한 일정");
            up.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(up);
            listPanel.add(Box.createVerticalStrut(10));

            for (UpcomingCardData data : upcoming) {
                JPanel c = createUpcomingCard(data);
                c.setAlignmentX(Component.LEFT_ALIGNMENT);
                listPanel.add(c);
                listPanel.add(Box.createVerticalStrut(12));
            }

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
    private JPanel createUpcomingCard(UpcomingCardData data) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        int h = 80;
        card.setPreferredSize(new Dimension(310, h));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, h));   // ★ 수정

        card.setBorder(new FlatLineBorder(
                new Insets(16, 16, 16, 16),
                UIConstants.GRAY_SOFT, 0.5f, 10
        ));

        JLabel title = new JLabel(data.title);
        title.setFont(UIConstants.FONT_SEMIBOLD_14);
        title.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel dday = new JLabel(data.ddayText);
        dday.setFont(UIConstants.FONT_REGULAR_14);
        dday.setForeground(Color.GRAY);

        card.add(title, BorderLayout.NORTH);
        card.add(dday, BorderLayout.SOUTH);

        return card;
    }


    /* 메뉴 버튼 */
    private JPanel createMenuButton(String text, String iconPath, Runnable action) {
        JPanel btn = new JPanel(new BorderLayout());
        btn.setOpaque(true);
        btn.setBackground(Color.WHITE);

        int h = 70;
        btn.setPreferredSize(new Dimension(310, h));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, h));    // ★ 수정

        btn.setBorder(new FlatLineBorder(
                new Insets(16, 16, 16, 16),
                UIConstants.GRAY_SOFT, 0.5f, 10
        ));

        JLabel icon = new JLabel(new FlatSVGIcon(iconPath, 24, 24));
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_SEMIBOLD_16);
        label.setForeground(UIConstants.TEXT_PRIMARY);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
        left.add(icon);
        left.add(Box.createHorizontalStrut(12));
        left.add(label);

        btn.add(left, BorderLayout.WEST);

        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });

        return btn;
    }


    /* 🔔 임박 일정 수집기 */
    private List<UpcomingCardData> collectUpcoming(List<MedicalRecord> medical) {

        List<UpcomingCardData> list = new ArrayList<>();

        for (MedicalRecord r : medical) {
            if (r.getDDay() >= 0) {
                list.add(new UpcomingCardData(
                        r.getCategory(),
                        r.getDDay(),
                        r.getDDayText()
                ));
            }
        }

        list.sort(Comparator.comparingLong(a -> a.dDay));

        if (list.size() > 3)
            return list.subList(0, 3);

        return list;
    }

    private record UpcomingCardData(String title, long dDay, String ddayText) { }
}

