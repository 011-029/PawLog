package ui;

import core.Pet;
import core.User;
import uiutil.DatePickerPanel;
import uiutil.LabeledTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class MedicineFormPanel extends Base {
    private final MainFrame mainFrame;
    private JToggleButton[] timeButtons;
    private DatePickerPanel dateField;
    private LabeledTextField medicineField;
    private LabeledTextField doseField;

    private User user;
    private Pet pet;

    public MedicineFormPanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.user = mainFrame.getLoggedInUser();
        this.pet = mainFrame.getLoggedInUserPet();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new AddRecordMenuPanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createFormContent(), BorderLayout.CENTER);
        contentWrapper.add(createSaveButtonBar(), BorderLayout.SOUTH);

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

        JLabel title = new JLabel("복용 기록 추가");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);
        header.add(Box.createHorizontalGlue());

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        //날짜
        dateField = new DatePickerPanel();
        listPanel.add(dateField);

        //약품명
        medicineField = new LabeledTextField("약 이름", "예) 심장사상충약");
        listPanel.add(medicineField);

        //복용량
        doseField = new LabeledTextField("복용량 (mg)", "예) 20");
        listPanel.add(doseField);

        /* ==== 복용 시간대 ==== */
        JLabel timeLabel = new JLabel("복용 시간대");
        timeLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        timeLabel.setForeground(UIConstants.TEXT_PRIMARY);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(timeLabel);
        listPanel.add(Box.createVerticalStrut(8));

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
        listPanel.add(timeRow);
        listPanel.add(Box.createVerticalStrut(20));

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
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        listPanel.setDoubleBuffered(true);
        listWrapper.setDoubleBuffered(true);

        return scroll;
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

        saveBtn.addActionListener(e -> {
            try {
                // 유저/펫 체크
                if (user == null || pet == null) {
                    JOptionPane.showMessageDialog(mainFrame, "유저 또는 펫 정보를 확인할 수 없습니다.");
                    return;
                }

                // 날짜
                LocalDate date = dateField.getDate();
                if (date == null) {
                    JOptionPane.showMessageDialog(mainFrame, "날짜를 선택해주세요.");
                    return;
                }

                // 약 이름
                String medicineName = medicineField.getText().trim();
                if (medicineName.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "약 이름을 입력해주세요.");
                    return;
                }

                // 복용량
                int dosage = doseField.getIntOrDefault(-1);
                if (dosage <= 0) {
                    JOptionPane.showMessageDialog(mainFrame, "복용량을 입력해주세요.");
                    return;
                }

                // 복용 시간대
                String takenTime = "";
                for (JToggleButton btn : timeButtons) {
                    if (btn.isSelected()) {
                        takenTime = btn.getText();
                    }
                }

                if (takenTime.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "복용 시간대를 선택해주세요.");
                    return;
                }

                // 디버그 출력
                System.out.println("===== [MedicineForm] 입력 디버그 =====");
                System.out.println("Pet = " + pet.getName());
                System.out.println("Date = " + date);
                System.out.println("Medicine = " + medicineName);
                System.out.println("Dosage = " + dosage);
                System.out.println("Time = " + takenTime);
                System.out.println("=====================================");

                // 실제 저장
                medicineRecordMgr.addNewRecord(
                        pet,
                        medicineName,
                        date,
                        takenTime,
                        dosage
                );

                //완료 메시지 + 이동
                JOptionPane.showMessageDialog(mainFrame, "복용 기록이 저장되었습니다!");
                mainFrame.switchPanel(new AddRecordMenuPanel(mainFrame));

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "저장 중 오류 발생: " + ex.getMessage(),
                        "오류",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        bar.add(saveBtn, BorderLayout.CENTER);
        return bar;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
