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

public class PlayFormPanel extends Base {
    private final MainFrame mainFrame;
    private JTextArea memoArea;
    private DatePickerPanel dateField;
    private LabeledTextField timeField;
    private LabeledTextField typeField;

    private User user;
    private Pet pet;

    public PlayFormPanel(MainFrame mainFrame) {
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

        JLabel title = new JLabel("놀이 기록 추가");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);
        header.add(Box.createHorizontalGlue());

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        //날짜
        dateField = new DatePickerPanel();
        listPanel.add(dateField);

        //놀이 시간
        timeField = new LabeledTextField("놀이시간(분)", "예) 20 ");
        listPanel.add(timeField);

        //놀이 종류
        typeField = new LabeledTextField("놀이종류", "예) 레이저포인터");
        listPanel.add(typeField);

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
                if (user == null || pet == null) {
                    JOptionPane.showMessageDialog(mainFrame, "유저 또는 펫 정보를 확인할 수 없습니다.");
                    return;
                }

                // 날짜
                LocalDate date = dateField.getDate();

                // 놀이 시간 (숫자)
                int playTime = timeField.getIntOrDefault(-1);
                if (playTime <= 0) {
                    JOptionPane.showMessageDialog(mainFrame, "놀이 시간을 숫자로 입력해주세요.");
                    return;
                }

                // 놀이 종류 (입력 없으면 0)
                String playType = typeField.getText().trim();
                if (playType.isEmpty()) playType = "0";

                // 메모
                String memo = memoArea.getText().trim();
                if (memo.isEmpty()) memo = "0";

                // 디버그
                System.out.println("======= [PlayForm DEBUG] =======");
                System.out.println("date = " + date);
                System.out.println("playTime = " + playTime);
                System.out.println("playType = " + playType);
                System.out.println("memo = " + memo);
                System.out.println("================================");

                // 저장
                playMgr.addNewRecord(pet, date, playTime, playType, memo);

                JOptionPane.showMessageDialog(mainFrame, "놀이 기록이 저장되었습니다!");
                mainFrame.switchPanel(new AddRecordMenuPanel(mainFrame));

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainFrame, "저장 중 오류 발생: " + ex.getMessage());
            }
        });

        bar.add(saveBtn, BorderLayout.CENTER);
        return bar;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
