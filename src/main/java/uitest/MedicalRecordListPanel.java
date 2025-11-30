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
                mainFrame.switchPanel(new AddRecordMenuPanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createList(records), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
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

        /* === 카드 리스트 === */
        for (MedicalRecord r : records) {

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
