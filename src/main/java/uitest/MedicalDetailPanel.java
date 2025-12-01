package uitest;

import core.MedicalMgr;
import core.MedicalRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MedicalDetailPanel extends JPanel {

    private final MainFrame mainFrame;

    public MedicalDetailPanel(MainFrame mainFrame, MedicalRecord r) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 헤더 + 내용
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new MedicalRecordListPanel(mainFrame, MedicalMgr.getInstance().mList))), BorderLayout.NORTH);
        contentWrapper.add(createContent(r), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    private JComponent createContent(MedicalRecord r) {
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

        // ====== 내용 표시 ======
        listPanel.add(makeInfoRow("증상", r.getCategory() + "   /  " + r.getDate()));
        listPanel.add(Box.createVerticalStrut(12));

        listPanel.add(makeInfoRow("병원", r.getHospital()));
        listPanel.add(Box.createVerticalStrut(8));

        String costText = (r.getCost() == -1) ? "미정" : r.getCost() + "원";
        listPanel.add(makeInfoRow("비용", costText));
        listPanel.add(Box.createVerticalStrut(24));

// 구분선
        listPanel.add(new JSeparator());
        listPanel.add(Box.createVerticalStrut(24));

// ====== 처방약 정보 ======
        JLabel medTitle = new JLabel("처방약");
        medTitle.setFont(UIConstants.FONT_EXTRABOLD_20);
        medTitle.setForeground(UIConstants.TEXT_PRIMARY);
        medTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(medTitle);
        listPanel.add(Box.createVerticalStrut(16));

        if (r.getPrescribedMedicine() == null) {
            JLabel noMed = new JLabel("처방된 약이 없습니다.");
            noMed.setFont(UIConstants.FONT_REGULAR_16);
            noMed.setForeground(UIConstants.TEXT_SECONDARY);
            noMed.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(noMed);
        } else {
            // 약 이름 + 용량
            String medLine = r.getPrescribedMedicine();
            if (r.getDosage() != null)
                medLine += "   (" + r.getDosage() + "mg)";

            listPanel.add(makeInfoRow("약 이름", medLine));
            listPanel.add(Box.createVerticalStrut(10));

            // 먹는 시간
            if (r.getRoutineTime() != null)
                listPanel.add(makeInfoRow("복용 시간", r.getRoutineTime()));

            listPanel.add(Box.createVerticalStrut(10));

            // 기간
            String period = "";
            if (r.getStartDate() != null)
                period += r.getStartDate().toString();
            else
                period += "-";

            period += "  ~  ";

            if (r.getEndDate() != null)
                period += r.getEndDate().toString();
            else
                period += "-";

            listPanel.add(makeInfoRow("복용 기간", period));
        }

        listPanel.add(Box.createVerticalStrut(24));

// 구분선
        listPanel.add(new JSeparator());
        listPanel.add(Box.createVerticalStrut(24));

// ====== 버튼 영역 ======
        JButton registerBtn = new JButton("처방약 루틴으로 등록하기");
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerBtn.setFont(UIConstants.FONT_SEMIBOLD_16);
        registerBtn.addActionListener(e -> {
            System.out.println("루틴 등록 클릭됨");
            // TODO: 루틴 연결 기능 넣기
        });

        JButton deleteBtn = new JButton("삭제");
        deleteBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteBtn.setFont(UIConstants.FONT_SEMIBOLD_16);
        deleteBtn.setForeground(Color.RED);
        deleteBtn.addActionListener(e -> {
            System.out.println("삭제 클릭됨");
            // TODO: 삭제 기능 넣기
        });

        listPanel.add(registerBtn);
        listPanel.add(Box.createVerticalStrut(12));
        listPanel.add(deleteBtn);
        listPanel.add(Box.createVerticalStrut(40));


        // 스크롤에 감싸기 + 항상 위에 붙게 래퍼 사용
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
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
