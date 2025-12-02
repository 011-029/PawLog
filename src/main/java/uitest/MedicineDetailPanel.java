package uitest;

import core.MedicineRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MedicineDetailPanel extends JPanel {
    
    private final MainFrame mainFrame;
    
    public MedicineDetailPanel(MainFrame mainFrame, MedicineRecord r) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 헤더 + 내용
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new MedicineRecordListPanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createContent(r), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    private JComponent createContent(MedicineRecord r) {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        // ===== 제목 =====
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("복용 기록");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);

        header.add(title);
        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        // ===== 정보 표시 =====
        // 약품명 + 용량
        String medLine = r.getMedicineName();
        if (r.getDosage() > 0)
            medLine += " (" + r.getDosage() + "mg)";

        listPanel.add(makeInfoRow("약품명", medLine));
        listPanel.add(Box.createVerticalStrut(12));

        // 복용일
        listPanel.add(makeInfoRow("복용일", r.getTakenDate().toString()));
        listPanel.add(Box.createVerticalStrut(12));

        // 시간대
        listPanel.add(makeInfoRow("시간대", r.getTakenTime()));
        listPanel.add(Box.createVerticalStrut(30));


        listPanel.add(new JSeparator());
        listPanel.add(Box.createVerticalStrut(30));

        // ===== 삭제 버튼 =====
        JButton deleteBtn = new JButton("삭제");
        deleteBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteBtn.setFont(UIConstants.FONT_SEMIBOLD_16);
        deleteBtn.setForeground(Color.RED);

        deleteBtn.addActionListener(e -> {
            System.out.println("복용 기록 삭제 클릭됨");
            // TODO: 삭제 로직 연결
        });

        listPanel.add(deleteBtn);
        listPanel.add(Box.createVerticalStrut(40));

        // ===== 스크롤 =====
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(30);

        return scroll;
    }

    private JPanel makeInfoRow(String name, String value) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel t = new JLabel(name + ": ");
        t.setFont(UIConstants.FONT_SEMIBOLD_16);
        t.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel v = new JLabel(value);
        v.setFont(UIConstants.FONT_REGULAR_16);
        v.setForeground(UIConstants.TEXT_PRIMARY);

        p.add(t);
        p.add(Box.createHorizontalStrut(8));
        p.add(v);

        return p;
    }
}
