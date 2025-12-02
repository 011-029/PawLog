package uitest;

import core.MedicalMgr;
import core.VaccineMgr;
import core.VaccineRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VaccineDetailPanel extends JPanel {

    private final MainFrame mainFrame;

    public VaccineDetailPanel(MainFrame mainFrame, VaccineRecord r) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 헤더 + 내용
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new VaccineRecordListPanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createContent(r), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    private JComponent createContent(VaccineRecord r) {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        // 제목 행
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("세부 기록");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        // ===== 정보 표시 =====
        listPanel.add(makeInfoRow("백신명", r.getVaccine()));
        listPanel.add(Box.createVerticalStrut(12));

        listPanel.add(makeInfoRow("날짜", r.getDate().toString()));
        listPanel.add(Box.createVerticalStrut(12));

        listPanel.add(makeInfoRow("병원명", r.getHospital()));
        listPanel.add(Box.createVerticalStrut(20));

        // 메모
        JLabel memoTitle = new JLabel("메모");
        memoTitle.setFont(UIConstants.FONT_SEMIBOLD_16);
        memoTitle.setForeground(UIConstants.TEXT_PRIMARY);
        memoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(memoTitle);
        listPanel.add(Box.createVerticalStrut(8));

        if (r.getMemo() != null && !r.getMemo().isBlank()) {
            JTextArea memoArea = new JTextArea(r.getMemo());
            memoArea.setFont(UIConstants.FONT_REGULAR_16);
            memoArea.setForeground(UIConstants.TEXT_PRIMARY);
            memoArea.setOpaque(false);
            memoArea.setEditable(false);
            memoArea.setLineWrap(true);
            memoArea.setWrapStyleWord(true);
            memoArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(memoArea);
        } else {
            JLabel emptyMemo = new JLabel("메모가 없습니다.");
            emptyMemo.setFont(UIConstants.FONT_REGULAR_16);
            emptyMemo.setForeground(UIConstants.TEXT_SECONDARY);
            emptyMemo.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(emptyMemo);
        }

        listPanel.add(Box.createVerticalStrut(30));
        listPanel.add(new JSeparator());
        listPanel.add(Box.createVerticalStrut(30));

        // ===== 삭제 버튼 =====
        JButton deleteBtn = new JButton("삭제");
        deleteBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteBtn.setFont(UIConstants.FONT_SEMIBOLD_16);
        deleteBtn.setForeground(Color.RED);

        deleteBtn.addActionListener(e -> {
            System.out.println("백신 기록 삭제 클릭됨");
            // TODO: 삭제 동작 연결
        });

        listPanel.add(deleteBtn);
        listPanel.add(Box.createVerticalStrut(40));

        // 스크롤 래퍼
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(30);

        return scroll;

    }

    private JPanel makeInfoRow(String title, String value) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel t = new JLabel(title + ": ");
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
