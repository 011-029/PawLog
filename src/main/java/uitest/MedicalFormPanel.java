package uitest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MedicalFormPanel extends JPanel {
    private final MainFrame mainFrame;

    private JToggleButton[] timeButtons;

    public MedicalFormPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new AddRecordMenuPanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createFormContent(), BorderLayout.CENTER);
        contentWrapper.add(createSaveButtonBar(), BorderLayout.SOUTH);   // ⬅⬅ 추가!

        add(contentWrapper, BorderLayout.CENTER);
    }

    private JComponent createFormContent() {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        /* ==== 제목 ==== */
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("진료 기록 추가");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);
        header.add(Box.createHorizontalGlue());

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));


        //진료 날짜
        DatePickerPanel consultationDate = new DatePickerPanel();
        listPanel.add(consultationDate);

        //병원명
        LabeledTextField hospitalField = new LabeledTextField("병원 이름", "예) 우끼우끼동물병원");
        listPanel.add(hospitalField);

        //증상
        LabeledTextField sympField = new LabeledTextField("증상", "예) 구토");
        listPanel.add(sympField);

        //가격
        LabeledTextField costField = new LabeledTextField("진료비 (원)", "예) 50000");
        listPanel.add(costField);

        JLabel sectionTitle = new JLabel("처방약(선택)");
        sectionTitle.setFont(UIConstants.FONT_BOLD_16);
        sectionTitle.setForeground(UIConstants.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        listPanel.add(Box.createVerticalStrut(12)); // 섹션 위 여백
        listPanel.add(sectionTitle);
        listPanel.add(Box.createVerticalStrut(12)); // 섹션 아래 여백

        JCheckBox hasMedicineCheck = new JCheckBox("처방약 있음");
        hasMedicineCheck.setOpaque(false);
        hasMedicineCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(hasMedicineCheck);
        listPanel.add(Box.createVerticalStrut(12));

        JPanel prescribeMedicinePanel = new JPanel();
        prescribeMedicinePanel.setLayout(new BoxLayout(prescribeMedicinePanel, BoxLayout.Y_AXIS));
        prescribeMedicinePanel.setOpaque(false);
        prescribeMedicinePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox addRoutineCheck = new JCheckBox("복용 루틴에 추가");
        addRoutineCheck.setOpaque(false);
        addRoutineCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        prescribeMedicinePanel.add(addRoutineCheck);
        prescribeMedicinePanel.add(Box.createVerticalStrut(12));

        //처방약 이름
        LabeledTextField medicineNameField = new LabeledTextField("약 이름", "예) 구토방지제");
        prescribeMedicinePanel.add(medicineNameField);

        //복용량
        LabeledTextField doseField = new LabeledTextField("복용량 (mg)", "예) 20");
        prescribeMedicinePanel.add(doseField);

        //복용 시간대
        JLabel timeLabel = new JLabel("복용 시간대");
        timeLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        timeLabel.setForeground(UIConstants.TEXT_PRIMARY);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        prescribeMedicinePanel.add(timeLabel);
        prescribeMedicinePanel.add(Box.createVerticalStrut(8));

        String[] times = {"아침", "점심", "저녁", "자기전"};
        timeButtons = new JToggleButton[times.length];

        JPanel timeRow = new JPanel();
        timeRow.setOpaque(false);
        timeRow.setLayout(new BoxLayout(timeRow, BoxLayout.X_AXIS));
        timeRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < times.length; i++) {
            JToggleButton btn = new JToggleButton(times[i]);
            btn.setFocusPainted(false);
            btn.setOpaque(true);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.putClientProperty(
                    "FlatLaf.style",
                    "arc:999; " +
                            "borderWidth:1; borderColor:#DDDDDD; " +
                            "background:#F7F7F7; " +
                            "selectedBackground:#81B2DD; " +
                            "foreground:#555555; " +
                            "selectedForeground:#FFFFFF"
            );
            btn.setMargin(new Insets(0, 16, 0, 16));
            btn.setPreferredSize(new Dimension(70, 36));
            btn.setMaximumSize(new Dimension(90, 36));

            timeRow.add(btn);
            if (i < times.length - 1)
                timeRow.add(Box.createHorizontalStrut(8));

            timeButtons[i] = btn;
        }
        prescribeMedicinePanel.add(timeRow);
        prescribeMedicinePanel.add(Box.createVerticalStrut(20));

        //복용기간
        DatePickerPanel startDate = new DatePickerPanel("복약 시작 날짜");
        DatePickerPanel endDate = new DatePickerPanel("복약 종료 날짜");
        prescribeMedicinePanel.add(startDate);
        prescribeMedicinePanel.add(endDate);

        prescribeMedicinePanel.setEnabled(false);
        setAllEnabled(prescribeMedicinePanel, false);

        listPanel.add(prescribeMedicinePanel);
        listPanel.add(Box.createVerticalStrut(24));

        hasMedicineCheck.addActionListener(e -> {
            boolean enabled = hasMedicineCheck.isSelected();
            setAllEnabled(prescribeMedicinePanel, enabled);
        });


        //래퍼 + 스크롤
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        //스크롤 속도 개선용
        scroll.getVerticalScrollBar().setUnitIncrement(20); // 숫자 커질수록 빠름
        listPanel.setDoubleBuffered(true);
        listWrapper.setDoubleBuffered(true);

        return scroll;
    }

    private void setAllEnabled(Component comp, boolean enabled) {
        comp.setEnabled(enabled);

        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                setAllEnabled(child, enabled);
            }
        }
    }

    private JComponent createSaveButtonBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(0, 0, 15, 0));

        JButton saveBtn = new JButton("저장");
        saveBtn.setFocusPainted(false);
        saveBtn.setBackground(UIConstants.PRIMARY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.putClientProperty("FlatLaf.style", "arc:10");

        saveBtn.setPreferredSize(new Dimension(0, 48));
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        // TODO: 실제 저장 로직 연결
        // saveBtn.addActionListener(e -> { ... });

        bar.add(saveBtn, BorderLayout.CENTER);
        return bar;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
