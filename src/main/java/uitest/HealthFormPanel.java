package uitest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class HealthFormPanel extends JPanel {
    private final MainFrame mainFrame;
    private JTextArea memoArea;
    private JToggleButton[] brushButtons;

    public HealthFormPanel(MainFrame mainFrame) {
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

        JLabel title = new JLabel("건강 기록 추가");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);
        header.add(Box.createHorizontalGlue());

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        //날짜
        DatePickerPanel dateField = new DatePickerPanel();
        listPanel.add(dateField);

        //식사량
        LabeledTextField mealField = new LabeledTextField("식사 횟수", "예) 3 ");
        listPanel.add(mealField);

        //음수량
        LabeledTextField waterField = new LabeledTextField("음수량 (ml)", "예) 100" );
        listPanel.add(waterField);

        //몸무게
        LabeledTextField weightField = new LabeledTextField("몸무게", "예) 3.5");
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
//        memoArea.putClientProperty("FlatLaf.style", "arc:10");
//        memoArea.setBorder(new FlatLineBorder(new Insets(0, 0, 0, 0),
//                UIConstants.GRAY_LIGHT, 1f, 10));

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

        // TODO: 실제 저장 로직 연결
        // saveBtn.addActionListener(e -> { ... });

        bar.add(saveBtn, BorderLayout.CENTER);
        return bar;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
