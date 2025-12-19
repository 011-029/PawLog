package ui;

import core.MedicalRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class MedicalDetailPanel extends Base {

    private final MainFrame mainFrame;
    private final MedicalRecord r;

    public MedicalDetailPanel(MainFrame mainFrame, MedicalRecord r) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.r = r;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));

        contentWrapper.add(
                UIComponents.createHeader(
                        () -> mainFrame.switchPanel(new MedicalRecordPanel(mainFrame))
                ),
                BorderLayout.NORTH
        );

        contentWrapper.add(createContent(), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    private JComponent createContent() {

        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        /* ========= 제목 ========= */
        JLabel title = new JLabel("세부 기록");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(title);
        listPanel.add(Box.createVerticalStrut(24));

        /* ========= 기본 정보 ========= */
        listPanel.add(withLeft(makeInfoRow("증상", r.getCategory() + " / " + r.getDate())));
        listPanel.add(Box.createVerticalStrut(12));

        listPanel.add(withLeft(makeInfoRow("병원", r.getHospital())));
        listPanel.add(Box.createVerticalStrut(8));

        String costText = (r.getCost() == -1) ?
                "미정" : String.format("%,d원", r.getCost());
        listPanel.add(withLeft(makeInfoRow("비용", costText)));

        listPanel.add(Box.createVerticalStrut(24));
        listPanel.add(new JSeparator());
        listPanel.add(Box.createVerticalStrut(24));

        /* ========= 처방약 ========= */
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
            String medLine = r.getPrescribedMedicine();
            if (r.getDosage() != null)
                medLine += " (" + r.getDosage() + "mg)";

            listPanel.add(withLeft(makeInfoRow("약 이름", medLine)));
            listPanel.add(Box.createVerticalStrut(10));

            if (r.getRoutineTime() != null)
                listPanel.add(withLeft(makeInfoRow("복용 시간", r.getRoutineTime())));
            listPanel.add(Box.createVerticalStrut(10));

            String period = (r.getStartDate() != null ? r.getStartDate() : "-")
                    + " ~ "
                    + (r.getEndDate() != null ? r.getEndDate() : "-");

            listPanel.add(withLeft(makeInfoRow("복용 기간", period)));
        }

        listPanel.add(Box.createVerticalStrut(24));
        listPanel.add(new JSeparator());
        listPanel.add(Box.createVerticalStrut(24));

        /* ========= 버튼들 ========= */
        JButton registerBtn = new JButton("처방약 루틴으로 등록하기");
        registerBtn.setFont(UIConstants.FONT_SEMIBOLD_16);
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        registerBtn.addActionListener(ev -> {
            try {
                if (r.getPrescribedMedicine() == null) {
                    JOptionPane.showMessageDialog(mainFrame,
                            "이 진료 기록에는 처방약 정보가 없습니다.");
                    return;
                }

                LocalDate today = LocalDate.now();
                if (r.getEndDate() != null && r.getEndDate().isBefore(today)) {
                    JOptionPane.showMessageDialog(mainFrame,
                            "복용 기간이 이미 종료되었습니다.");
                    return;
                }

                var routine = medicineRoutineMgr.createRoutineFromMedicalRecord(r);

                if (routine == null) {
                    JOptionPane.showMessageDialog(mainFrame,
                            "루틴 생성 실패 (처방 정보 부족)");
                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "루틴 생성 완료!");
                    mainFrame.switchPanel(new MedicalDetailPanel(mainFrame,r));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainFrame,
                        "오류: " + ex.getMessage());
            }
        });

        JButton deleteBtn = new JButton("삭제");
        deleteBtn.setFont(UIConstants.FONT_SEMIBOLD_16);
        deleteBtn.setForeground(Color.RED);
        deleteBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        deleteBtn.addActionListener(ev -> {

            int confirm = JOptionPane.showConfirmDialog(
                    mainFrame, "정말 삭제하시겠습니까?", "삭제", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            boolean removed = medicalMgr.removeByIndexId(r.getIndexId());

            if (!removed) {
                JOptionPane.showMessageDialog(mainFrame, "삭제 실패");
                return;
            }

            medicalMgr.saveAll();
            JOptionPane.showMessageDialog(mainFrame, "삭제 완료!");

            mainFrame.switchPanel(new MedicalRecordPanel(mainFrame));
        });

        listPanel.add(registerBtn);
        listPanel.add(Box.createVerticalStrut(12));
        listPanel.add(deleteBtn);
        listPanel.add(Box.createVerticalStrut(40));


        /* ========= 스크롤 ========= */
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scroll;
    }

    /* ========= 헬퍼: 왼쪽정렬 강제 ========= */
    private JComponent withLeft(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private JPanel makeInfoRow(String title, String value) {
        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel t = new JLabel(title + ": ");
        t.setFont(UIConstants.FONT_SEMIBOLD_16);

        JLabel v = new JLabel(value);
        v.setFont(UIConstants.FONT_REGULAR_16);

        row.add(t);
        row.add(Box.createHorizontalStrut(8));
        row.add(v);

        return row;
    }
}
