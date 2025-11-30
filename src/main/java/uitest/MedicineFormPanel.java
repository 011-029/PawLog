package uitest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MedicineFormPanel extends JPanel {
    private final MainFrame mainFrame;
    private JToggleButton[] timeButtons;

    public MedicineFormPanel(MainFrame mainFrame) {
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

        JLabel title = new JLabel("복용 기록 추가");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);
        header.add(Box.createHorizontalGlue());

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        //날짜
        DatePickerPanel dateField = new DatePickerPanel();
        listPanel.add(dateField);

        //약품명
        LabeledTextField medicineField = new LabeledTextField("약 이름", "예) 심장사상충약");
        listPanel.add(medicineField);

        //복용량
        LabeledTextField doseField = new LabeledTextField("복용량 (mg)", "예) 20");
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
