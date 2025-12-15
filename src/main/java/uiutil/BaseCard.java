package uiutil;

import com.formdev.flatlaf.ui.FlatLineBorder;
import ui.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class BaseCard extends JPanel {

    private boolean hovered = false;

    protected JPanel textPanel;
    protected JPanel rightPanel;

    public BaseCard() {

        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.WHITE);

        // === 원래 카드 테두리 (복용루틴 카드와 동일) ===
        setBorder(new FlatLineBorder(
                new Insets(16, 16, 16, 16),
                UIConstants.GRAY_SOFT,
                0.5f,
                10
        ));

        // 왼쪽 텍스트
        textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        add(textPanel, BorderLayout.CENTER);

        // 오른쪽 체크박스/아이콘 영역
        rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(0, 8, 0, 0));
        add(rightPanel, BorderLayout.EAST);

        // Max width 유지
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // === Hover 이벤트 ===
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hovered = true;
                applyHoverStyle(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hovered = false;
                applyHoverStyle(false);
            }
        });
    }

    /* 카드 '떠오르는 효과' 구현 */
    private void applyHoverStyle(boolean on) {
        if (on) {
            // 위쪽 padding 줄이고 아래 padding 늘림 → 실제로 카드가 떠보임
//            setBorder(new FlatLineBorder(
//                    new Insets(8, 16, 24, 16),
//                    UIConstants.GRAY_SOFT,
//                    0.5f,
//                    10
//            ));

            setBackground(new Color(250, 250, 250)); // 살짝 밝게
        } else {
            // 원래 상태로 복구
            setBorder(new FlatLineBorder(
                    new Insets(16, 16, 16, 16),
                    UIConstants.GRAY_SOFT,
                    0.5f,
                    10
            ));

            setBackground(Color.WHITE);
        }

        revalidate();
        repaint();
    }

    protected void addTitle(String text) {
        JLabel titleLabel = new JLabel(text);
        titleLabel.setFont(UIConstants.FONT_SEMIBOLD_14);
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(2));
    }

    protected void addLine(String text) {
        textPanel.add(Box.createVerticalStrut(2));

        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_REGULAR_14);
        label.setForeground(UIConstants.TEXT_SECONDARY);
        textPanel.add(label);
    }

    protected void addRightCheckBox() {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setOpaque(false);
        checkBox.setFocusPainted(false);
        checkBox.setBorderPainted(false);
        checkBox.setContentAreaFilled(false);
        checkBox.putClientProperty("JCheckBox[styleClass]", "round");

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(checkBox);
        rightPanel.add(Box.createVerticalGlue());
    }

    protected void addDDayLabel(String dDay) {
        JLabel label = new JLabel(dDay);
        label.setFont(UIConstants.FONT_SEMIBOLD_18);
        label.setForeground(UIConstants.PRIMARY);

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(label);
        rightPanel.add(Box.createVerticalGlue());
    }
}
