package uiutil;

import ui.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DatePickerPanel extends JPanel {

    private final JComboBox<Integer> yearBox;
    private final JComboBox<Integer> monthBox;
    private final JComboBox<Integer> dayBox;

    public DatePickerPanel() {
        this("날짜"); // 기본값
    }

    public DatePickerPanel(String title) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 라벨
        if (title != null && !title.isBlank()) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
            titleLabel.setForeground(UIConstants.TEXT_PRIMARY);
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            add(titleLabel);
            add(Box.createVerticalStrut(8));
        }

        // 내부 row
        JPanel dateRow = new JPanel();
        dateRow.setOpaque(false);
        dateRow.setLayout(new BoxLayout(dateRow, BoxLayout.X_AXIS));
        dateRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 날짜 값 생성
        LocalDate today = LocalDate.now();
        int year = today.getYear();

        Integer[] years = {year - 2, year - 1, year, year + 1};
        Integer[] months = new Integer[12];
        for (int i = 0; i < 12; i++) months[i] = i + 1;
        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) days[i] = i + 1;

        // 컴포넌트 생성
        yearBox = new JComboBox<>(years);
        monthBox = new JComboBox<>(months);
        dayBox = new JComboBox<>(days);

        yearBox.setSelectedItem(year);
        monthBox.setSelectedItem(today.getMonthValue());
        dayBox.setSelectedItem(today.getDayOfMonth());

        // 크기
        Dimension comboSize = new Dimension(80, 36);
        yearBox.setPreferredSize(comboSize);
        monthBox.setPreferredSize(comboSize);
        dayBox.setPreferredSize(comboSize);

        dateRow.add(yearBox);
        dateRow.add(Box.createHorizontalStrut(8));
        dateRow.add(monthBox);
        dateRow.add(Box.createHorizontalStrut(8));
        dateRow.add(dayBox);

        add(dateRow);
        add(Box.createVerticalStrut(20));
    }

    public LocalDate getDate() {
        return LocalDate.of(
                (int) yearBox.getSelectedItem(),
                (int) monthBox.getSelectedItem(),
                (int) dayBox.getSelectedItem()
        );
    }

    public void setDate(LocalDate date) {
        yearBox.setSelectedItem(date.getYear());
        monthBox.setSelectedItem(date.getMonthValue());
        dayBox.setSelectedItem(date.getDayOfMonth());
    }
}
