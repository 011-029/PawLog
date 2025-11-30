package uitest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class RecordCard extends JPanel {

    public RecordCard(String title, List<String> lines) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel header = new JLabel(title);
        header.setFont(UIConstants.FONT_BOLD_16);
        add(header);
        add(Box.createVerticalStrut(6));

        for (String line : lines) {
            JLabel lbl = new JLabel(line);
            lbl.setFont(UIConstants.FONT_REGULAR_14);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(lbl);
        }
    }
}
