package uitest;

import core.MedicineRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MedicineRecordListPanel extends JPanel {
    private final MainFrame mainFrame;

    public MedicineRecordListPanel(MainFrame mainFrame, List<MedicineRecord> records) {
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

    private JComponent createList(List<MedicineRecord> records) {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 16, 16, 16));

        // 제목
        JLabel title = new JLabel("복용 기록 목록");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        listPanel.add(title);
        listPanel.add(Box.createVerticalStrut(20));

        /* === 카드 리스트 === */
        for (MedicineRecord r : records) {

            MedicineCard card = new MedicineCard(r);
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

    private static class MedicineCard extends BaseCard {

        public MedicineCard(MedicineRecord r) {

            String title = r.getUITexts()[1];
            addTitle(title);

            addLine("날짜: " + r.getUITexts()[2]);
            addLine("복용량: " + r.getUITexts()[4] +"ml");
        }
    }

}
