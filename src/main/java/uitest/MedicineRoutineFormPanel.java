package uitest;

import core.Pet;
import core.User;
import util.PlaceholderTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MedicineRoutineFormPanel extends Base {

    private final MainFrame mainFrame;

    private JTextField nameField;    // 약 이름
    private JTextField doseField;    // 복용량
    private JToggleButton[] dayButtons;
    private JToggleButton[] timeButtons;

    private User user;
    private Pet pet;

    public MedicineRoutineFormPanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.user = mainFrame.getLoggedInUser();
        this.pet = mainFrame.getLoggedInUserPet();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단바 + 내용에만 패딩
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new AddRecordMenuPanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createFormContent(), BorderLayout.CENTER);
        contentWrapper.add(createSaveButtonBar(), BorderLayout.SOUTH);   // ⬅⬅ 추가!

        add(contentWrapper, BorderLayout.CENTER);
    }

    /* ================== 중앙 폼 ================== */
    private JComponent createFormContent() {
        // 전체 스크롤 안의 세로 레이아웃
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        /* ==== 제목 라인 (산책 기록 스타일) ==== */
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("복용 루틴 추가");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);
        header.add(Box.createHorizontalGlue());

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        /* ==== 약 이름 ==== */
        JLabel nameLabel = new JLabel("약 이름");
        nameLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        nameLabel.setForeground(UIConstants.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(nameLabel);
        listPanel.add(Box.createVerticalStrut(8));

        nameField = new PlaceholderTextField("예) 종합비타민");
        nameField.setPreferredSize(new Dimension(360, 45));
        nameField.setMaximumSize(new Dimension(360, 45));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.putClientProperty("FlatLaf.style", "arc:10");
        nameField.setBorder(
                BorderFactory.createCompoundBorder(
                        nameField.getBorder(),
                        BorderFactory.createEmptyBorder(0, 8, 0, 8)
                )
        );
        listPanel.add(nameField);
        listPanel.add(Box.createVerticalStrut(22));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));


        /* ==== 요일 선택 ==== */
        JLabel dayLabel = new JLabel("복용 요일");
        dayLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        dayLabel.setForeground(UIConstants.TEXT_PRIMARY);
        dayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(dayLabel);
        listPanel.add(Box.createVerticalStrut(8));

        String[] days = {"월", "화", "수", "목", "금", "토", "일"};
        dayButtons = new JToggleButton[days.length];

        JPanel dayRow = new JPanel();
        dayRow.setOpaque(false);
        dayRow.setLayout(new BoxLayout(dayRow, BoxLayout.X_AXIS));
        dayRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < days.length; i++) {
            JToggleButton btn = new JToggleButton(days[i]);
            btn.setFocusPainted(false);
            btn.setOpaque(true);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // 🔥 내부 패딩 완전히 제거!
            btn.setMargin(new Insets(0, 0, 0, 0));

            // 🔵 원형 버튼 크기 고정! (정사각형 = 원)
            int size = 43;   // 버튼 전체 크기
            btn.setPreferredSize(new Dimension(size, size));
            btn.setMinimumSize(new Dimension(size, size));
            btn.setMaximumSize(new Dimension(size, size));

            // FlatLaf 스타일을 원형에 맞게 설정
            btn.putClientProperty(
                    "FlatLaf.style",
                    "arc:999;" +                     // 완전 동그란 모양
                            "borderWidth:1;" +
                            "borderColor:#DDDDDD;" +
                            "background:#F7F7F7;" +
                            "selectedBackground:#81B2DD;" +
                            "foreground:#555555;" +
                            "selectedForeground:#FFFFFF;"
            );

            // 글씨 가운데 정렬
            btn.setHorizontalAlignment(SwingConstants.CENTER);

            // 버튼 추가
            dayRow.add(btn);
            if (i < days.length - 1)
                dayRow.add(Box.createHorizontalStrut(9));

            dayButtons[i] = btn;
        }
        listPanel.add(dayRow);
        listPanel.add(Box.createVerticalStrut(20));

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

        /* ==== 복용량 ==== */
        JLabel doseLabel = new JLabel("복용량 (mg)");
        doseLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        doseLabel.setForeground(UIConstants.TEXT_PRIMARY);
        doseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(doseLabel);
        listPanel.add(Box.createVerticalStrut(8));

        doseField = new PlaceholderTextField("예) 20");
        doseField.setPreferredSize(new Dimension(360, 45));
        doseField.setMaximumSize(new Dimension(360, 45));
        doseField.setAlignmentX(Component.LEFT_ALIGNMENT);
        doseField.putClientProperty("FlatLaf.style", "arc:10");
        doseField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        doseField.setBorder(
                BorderFactory.createCompoundBorder(
                        doseField.getBorder(),
                        BorderFactory.createEmptyBorder(0, 8, 0, 8)
                )
        );
        listPanel.add(doseField);
        listPanel.add(Box.createVerticalStrut(180));

        // 항상 위쪽에 붙도록 래퍼 + 스크롤
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

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

        // 폭은 가로 전체를 차지하게 (반응형)
        saveBtn.setPreferredSize(new Dimension(0, 48));
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        saveBtn.addActionListener(e -> {
            try {
                // 1️⃣ 유저/펫 체크 (전제: 무조건 존재)
                if (user == null || pet == null) {
                    JOptionPane.showMessageDialog(mainFrame, "유저 또는 펫 정보를 확인할 수 없습니다.");
                    return;
                }

                // 2️⃣ 약 이름
                String medicineName = nameField.getText().trim();
                if (medicineName.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "약 이름을 입력해주세요.");
                    return;
                }

                // 3️⃣ 요일 (최소 1개)
                StringBuilder sbDOW = new StringBuilder();
                for (JToggleButton btn : dayButtons) {
                    if (btn.isSelected()) {
                        sbDOW.append(btn.getText());   // 예: 월,화,수 → "월화수"
                    }
                }
                if (sbDOW.length() == 0) {
                    JOptionPane.showMessageDialog(mainFrame, "복용 요일을 최소 1개 이상 선택해주세요.");
                    return;
                }
                String takenDOW = sbDOW.toString();

                // 4️⃣ 복용 시간대 (필수, 1개)
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

                // 5️⃣ 복용량 (필수, 숫자)
                int dosage;
                try {
                    dosage = Integer.parseInt(doseField.getText().trim());
                } catch (Exception ex2) {
                    JOptionPane.showMessageDialog(mainFrame, "복용량은 숫자로 입력해야 합니다.");
                    return;
                }

                if (dosage <= 0) {
                    JOptionPane.showMessageDialog(mainFrame, "복용량을 정확히 입력해주세요.");
                    return;
                }

                // 6️⃣ 디버그 출력
                System.out.println("===== [RoutineForm] 입력 디버그 =====");
                System.out.println("medicineName = " + medicineName);
                System.out.println("takenDOW = " + takenDOW);
                System.out.println("takenTime = " + takenTime);
                System.out.println("dosage = " + dosage);
                System.out.println("====================================");

                // 7️⃣ 실제 저장
                medicineRoutineMgr.addNewRoutine(
                        pet,
                        medicineName,
                        takenDOW,
                        takenTime,
                        dosage
                );

                // 8️⃣ 성공 메시지 + 기록 추가 메뉴로 이동
                JOptionPane.showMessageDialog(mainFrame, "복용 루틴이 저장되었습니다!");
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

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}
