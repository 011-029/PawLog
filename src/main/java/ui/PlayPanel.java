package ui;

import com.formdev.flatlaf.ui.FlatLineBorder;
import core.Pet;
import core.PlayRecord;
import core.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class PlayPanel extends Base {

    private final MainFrame mainFrame;
    private User user;
    private Pet pet;
    protected ArrayList<PlayRecord> records;

    public PlayPanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.user = mainFrame.getLoggedInUser();
        this.pet = mainFrame.getLoggedInUserPet();
        this.records = playMgr.getAllByOwner(user);

        // TODO: 아래 테스트용 코드 추후 삭제 (2줄)
        System.out.println("놀이패널 ID: " + user.getId());
        System.out.println("놀이패널 펫: " + pet.getName());

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new HomePanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createContent(), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    /* ================== 가운데 리스트 ================== */
    private JComponent createContent() {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("놀이 기록");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);

        header.add(Box.createHorizontalGlue());
        header.add(UIComponents.createSearchButton(mainFrame, this));

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        // 카드 생성
        if (records == null || records.isEmpty()) {
            JComponent empty = UIComponents.createEmptyMessagePanel("아직 기록이 없습니다.");
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(empty);
        } else {
            for (PlayRecord r : records) {
                JPanel card = createPlayCard(r);
                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(16));
            }
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(30);

        return scroll;
    }

    /* 개별 산책 기록 카드 */
    protected JPanel createPlayCard(PlayRecord r) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setPreferredSize(new Dimension(310, 100));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setMinimumSize(new Dimension(310, 100));
        card.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16),
                UIConstants.GRAY_SOFT, 0.5f, 10));

        LocalDate date = r.getRecordDate();
        int playTime = r.getPlayTime();
        String playType = r.getPlayType();
        String memo = r.getMemo();

        // 왼쪽 텍스트 영역
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(date.toString());
        titleLabel.setFont(UIConstants.FONT_SEMIBOLD_14);
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY);

        String line =
                ("놀이시간 " + playTime + "분")
                    + (playType == null ? "" : "  |  " + playType);

        JLabel playLabel = new JLabel(line);
        playLabel.setFont(UIConstants.FONT_REGULAR_14);
        playLabel.setForeground(UIConstants.TEXT_SECONDARY);

        JLabel memoLabel = new JLabel(memo);
        memoLabel.setFont(UIConstants.FONT_REGULAR_14);
        memoLabel.setForeground(UIConstants.TEXT_SECONDARY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(playLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(memoLabel);

        card.add(textPanel, BorderLayout.CENTER);

        // hover 효과
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            Color normalBg = Color.WHITE;
            Color hoverBg = new Color(250, 250, 250);

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(normalBg);
            }
        });

        return card;
    }
}
