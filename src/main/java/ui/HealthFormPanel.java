package ui;

import core.Pet;
import core.User;
import uiutil.DatePickerPanel;
import uiutil.LabeledTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;

public class HealthFormPanel extends Base {
    private DatePickerPanel dateField;
    private LabeledTextField mealField;
    private LabeledTextField waterField;
    private LabeledTextField weightField;
    private JTextArea memoArea;
    private JToggleButton[] brushButtons;

    private User user;
    private Pet pet;

    private final MainFrame mainFrame;

    public HealthFormPanel(MainFrame mainFrame) {
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

        JLabel title = new JLabel("건강 기록 추가");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);
        header.add(Box.createHorizontalGlue());

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        //날짜
        dateField = new DatePickerPanel();
        listPanel.add(dateField);

        //식사량
        mealField = new LabeledTextField("식사 횟수", "예) 3 ");
        listPanel.add(mealField);

        //음수량
        waterField = new LabeledTextField("음수량 (ml)", "예) 100" );
        listPanel.add(waterField);

        //몸무게
        weightField = new LabeledTextField("몸무게", "예) 3.5");
        listPanel.add(weightField);

        //빗질 여부
        JLabel brushLabel = new JLabel("빗질 여부");
        brushLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        brushLabel.setForeground(UIConstants.TEXT_PRIMARY);
        brushLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(brushLabel);
        listPanel.add(Box.createVerticalStrut(8));

        String[] brushed = {"YES", "NO"};
        brushButtons = new JToggleButton[brushed.length];

        ButtonGroup group = new ButtonGroup();
        group.add(brushButtons[0]); // YES
        group.add(brushButtons[1]); // NO

        JPanel brushRow = new JPanel();
        brushRow.setOpaque(false);
        brushRow.setLayout(new BoxLayout(brushRow, BoxLayout.X_AXIS));
        brushRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < brushed.length; i++) {
            JToggleButton btn = new JToggleButton(brushed[i]);
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

            brushRow.add(btn);
            if (i < brushed.length - 1)
                brushRow.add(Box.createHorizontalStrut(8));

            brushButtons[i] = btn;
        }
        listPanel.add(brushRow);
        listPanel.add(Box.createVerticalStrut(20));

        /* ==== 메모 ==== */
        JLabel memoLabel = new JLabel("메모");
        memoLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        memoLabel.setForeground(UIConstants.TEXT_PRIMARY);
        memoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(memoLabel);
        listPanel.add(Box.createVerticalStrut(8));

        memoArea = new JTextArea(4, 20);
        memoArea.setLineWrap(true);
        memoArea.setWrapStyleWord(true);
        memoArea.setFont(UIConstants.FONT_REGULAR_14);

        JScrollPane memoScroll = new JScrollPane(memoArea);
        memoScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        memoScroll.setPreferredSize(new Dimension(360, 130));
        memoScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        memoScroll.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(220, 220, 220), 1, true),
                        new EmptyBorder(8, 4, 8, 4)
                )
        );
        listPanel.add(memoScroll);
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
        scroll.getVerticalScrollBar().setUnitIncrement(20); // 숫자 커질수록 빠름
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
                // 1. 유저/펫 기본 전제 체크 (항상 있음)
                if (user == null || pet == null) {
                    JOptionPane.showMessageDialog(mainFrame, "유저 또는 펫 정보를 확인할 수 없습니다.");
                    return;
                }

                // 2. 날짜 읽기
                LocalDate date = dateField.getDate();
                if (date == null) {
                    JOptionPane.showMessageDialog(mainFrame, "날짜를 선택해주세요.");
                    return;
                }

                // 3. 숫자값 파싱
                int meal = mealField.getIntOrDefault(-1);
                int water = waterField.getIntOrDefault(-1);

                double weight;
                try {
                    weight = Double.parseDouble(weightField.getText().trim());
                } catch (Exception ex2) {
                    JOptionPane.showMessageDialog(mainFrame, "몸무게는 숫자로 입력해야 합니다.");
                    return;
                }

                // 4. 필수 입력 유효성 검사
                if (meal < 0) {
                    JOptionPane.showMessageDialog(mainFrame, "식사 횟수를 입력해주세요.");
                    return;
                }
                if (water < 0) {
                    JOptionPane.showMessageDialog(mainFrame, "음수량을 입력해주세요.");
                    return;
                }
                if (weight <= 0) {
                    JOptionPane.showMessageDialog(mainFrame, "몸무게를 입력해주세요.");
                    return;
                }

                // 5. 빗질 여부 처리 (YES/NO 두 개 중 하나만 선택)
                String brushed = null;
                if (brushButtons[0].isSelected()) brushed = "yes";
                if (brushButtons[1].isSelected()) brushed = "no";

                if (brushed == null) {
                    JOptionPane.showMessageDialog(mainFrame, "빗질 여부를 선택해주세요.");
                    return;
                }

                // 6. 메모 (없으면 "0")
                String memo = memoArea.getText().trim();
                if (memo.isEmpty()) memo = "0";

                // 7. 디버그 출력
                System.out.println("===== [HealthForm] 입력 디버그 =====");
                System.out.println("date = " + date);
                System.out.println("meal = " + meal);
                System.out.println("water = " + water);
                System.out.println("weight = " + weight);
                System.out.println("brushed = " + brushed);
                System.out.println("memo = " + memo);
                System.out.println("=================================");

                // 8. 실제 저장
                healthMgr.addNewRecord(
                        pet,
                        date,
                        meal,
                        water,
                        weight,
                        brushed,
                        memo
                );

                // 성공 메시지 + 이동
                JOptionPane.showMessageDialog(mainFrame, "건강 기록이 저장되었습니다!");
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
