package ui;

import core.*;
import uiutil.BaseCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class VaccineRecordPanel extends Base {
    private final MainFrame mainFrame;
    protected ArrayList<VaccineRecord> records;
    private User user;
    private Pet pet;

    public VaccineRecordPanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.user = mainFrame.getLoggedInUser();
        this.pet = mainFrame.getLoggedInUserPet();
        this.records = vaccineMgr.getAllByOwner(user);

        // TODO: 아래 테스트용 코드 추후 삭제 (2줄)
        System.out.println("백신패널 ID: " + user.getId());
        System.out.println("백신패널 펫: " + pet.getName());

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new MedicalHomePanel(mainFrame
                ))), BorderLayout.NORTH);
        contentWrapper.add(createList(), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    private JComponent createList() {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 16, 16, 16));

        // 제목
        JLabel title = new JLabel("예방접종 기록");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createHorizontalGlue());
        header.add(UIComponents.createSearchButton(mainFrame, this));

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(20));

        /* === 미래/과거 분리 === */
        List<VaccineRecord> future = new java.util.ArrayList<>();
        List<VaccineRecord> past = new java.util.ArrayList<>();

        LocalDate today = LocalDate.now();

        for (VaccineRecord r : records) {
            if (!r.getDate().isBefore(today)) future.add(r);
            else past.add(r);
        }

        future.sort(Comparator.comparing(VaccineRecord::getDate));
        past.sort(Comparator.comparing(VaccineRecord::getDate).reversed());

        /* === 미래(예정된 일정) 섹션 === */
        if (!future.isEmpty()) {
            JLabel upcomingLabel = new JLabel("예정된 접종 일정");
            upcomingLabel.setFont(UIConstants.FONT_SEMIBOLD_18);
            upcomingLabel.setForeground(UIConstants.TEXT_PRIMARY);
            upcomingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel upcomingContent = new JPanel();
            upcomingContent.setOpaque(false);
            upcomingContent.setLayout(new BoxLayout(upcomingContent, BoxLayout.Y_AXIS));
            upcomingContent.setAlignmentX(Component.LEFT_ALIGNMENT);

            listPanel.add(UIComponents.createCollapsibleHeader(upcomingLabel, upcomingContent));
            listPanel.add(Box.createVerticalStrut(12));

            for (VaccineRecord r : future) {
                VaccineCard card = new VaccineCard(r, mainFrame);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                upcomingContent.add(card);
                upcomingContent.add(Box.createVerticalStrut(16));
            }

            listPanel.add(upcomingContent);
            listPanel.add(Box.createVerticalStrut(24));
        }

        /* === 과거 섹션 === */
        JLabel pastLabel = new JLabel("과거 예방접종 기록");
        pastLabel.setFont(UIConstants.FONT_SEMIBOLD_18);
        pastLabel.setForeground(UIConstants.TEXT_PRIMARY);
        pastLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pastContent = new JPanel();
        pastContent.setOpaque(false);
        pastContent.setLayout(new BoxLayout(pastContent, BoxLayout.Y_AXIS));
        pastContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        listPanel.add(UIComponents.createCollapsibleHeader(pastLabel, pastContent));
        listPanel.add(Box.createVerticalStrut(12));

        for (VaccineRecord r : past) {
            VaccineCard card = new VaccineCard(r, mainFrame);
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            pastContent.add(card);
            pastContent.add(Box.createVerticalStrut(16));
        }

        listPanel.add(pastContent);

        // 스크롤 래퍼
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        // 스크롤 속도 빠르게
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        return scroll;
    }

    protected static class VaccineCard extends BaseCard {

        public VaccineCard(VaccineRecord r, MainFrame mainFrame) {
            String title = r.getUITexts()[0];
            String dday = r.getDDayText();

//            if (dday != null && !dday.isBlank()) {
//                title += "  (" + dday + ")";    // 예: "심장사상충백신 (D-3)"
//            }

            addTitle(title);

            addLine("날짜: " + r.getUITexts()[1]);
            addLine("병원: " + r.getUITexts()[2]);
            addDDayLabel(dday);

            // 클릭하면 상세 페이지
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    System.out.println("카드 클릭됨, record = " + r);
                    mainFrame.switchPanel(new VaccineDetailPanel(mainFrame, r));
                }
            });
        }


    }
}
