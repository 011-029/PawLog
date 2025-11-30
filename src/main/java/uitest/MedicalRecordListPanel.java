package uitest;

import core.MedicalRecord;

import javax.swing.*;
import java.util.List;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MedicalRecordListPanel extends JPanel {
    private final MainFrame mainFrame;

    public MedicalRecordListPanel(MainFrame mainFrame, List<MedicalRecord> records) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new PetHomePanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createList(records), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    private JComponent createList(List<MedicalRecord> records) {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 16, 16, 16));

        // 제목
        JLabel title = new JLabel("진료 기록 목록");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        listPanel.add(title);
        listPanel.add(Box.createVerticalStrut(20));

        //레코드를 미래, 과거로 분리
        List<MedicalRecord> future = new java.util.ArrayList<>();
        List<MedicalRecord> past = new java.util.ArrayList<>();

        for (MedicalRecord r : records) {
            if (r.getDDay() >= 0)
                future.add(r);
            else
                past.add(r);
        }

        // 미래 기록: D-Day 가까운 순
        future.sort(java.util.Comparator.comparingLong(MedicalRecord::getDDay));

        // 과거 기록: 날짜 최신 순
        past.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        // 미래 기록
        if (!future.isEmpty()) {
            JLabel upcomingLabel = new JLabel("예정된 기록");
            upcomingLabel.setFont(UIConstants.FONT_SEMIBOLD_18);
            upcomingLabel.setForeground(UIConstants.TEXT_PRIMARY);
            upcomingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            listPanel.add(upcomingLabel);
            listPanel.add(Box.createVerticalStrut(12));

            for (MedicalRecord r : future) {
                MedicalCard card = new MedicalCard(r);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);

                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(12));
            }

            listPanel.add(Box.createVerticalStrut(24)); // 섹션 간 여백
        }

        //과거기록
        JLabel pastLabel = new JLabel("과거 기록");
        pastLabel.setFont(UIConstants.FONT_SEMIBOLD_18);
        pastLabel.setForeground(UIConstants.TEXT_PRIMARY);
        pastLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        listPanel.add(pastLabel);
        listPanel.add(Box.createVerticalStrut(12));

        for (MedicalRecord r : past) {
            MedicalCard card = new MedicalCard(r);
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            listPanel.add(card);
            listPanel.add(Box.createVerticalStrut(12));
        }

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

    /* ============================
     *  카드 UI : MedicalCard
     * ============================ */
    private static class MedicalCard extends BaseCard {

        public MedicalCard(MedicalRecord r) {

            // ★ 타이틀 = 증상 + (D-day)
            String title = r.getCategory();
            String dday = r.getDDayText();

            if (dday != null && !dday.isBlank()) {
                title += "  (" + dday + ")";    // 예: "구토 (D-3)"
            }

            addTitle(title);

            addLine("날짜: " + r.getDate());
            addLine("병원: " + r.getHospital());
        }
    }
}
