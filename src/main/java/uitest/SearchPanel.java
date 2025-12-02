package uitest;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SearchPanel extends JPanel {
    private final MainFrame mainFrame;
//    private PlaceholderTextField searchField;
    private final JPanel prevPanel; // 이전 화면 (접근경로)
    private JPanel searchResultContainer; // 검색 결과 컨테이너

    public SearchPanel(MainFrame mainFrame, JPanel prevPanel) {
        this.mainFrame = mainFrame;
        this.prevPanel = prevPanel;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);   // 없으면 Color.WHITE 써도 됨

        // ⬇ 헤더 + 스크롤 콘텐츠만 패딩을 주는 래퍼
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(prevPanel)), BorderLayout.NORTH);
        contentWrapper.add(createContent(), BorderLayout.CENTER);

        // 가운데는 패딩 있는 래퍼
        add(contentWrapper, BorderLayout.CENTER);

        // ⬇ 하단 탭바는 패딩 없는 SOUTH에 바로!
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    /** 가운데 전체 영역 */
    private JComponent createContent() {
        // 스크롤 가능 리스트로 만들기
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        // 🔹 제목 + 검색 아이콘 한 줄
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

//        searchField = new PlaceholderTextField("검색어를 입력하세요");
//        searchField.setPreferredSize(new Dimension(320, 45));
//        searchField.setMaximumSize(new Dimension(320, 45));
//        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);
//        searchField.putClientProperty("FlatLaf.style", "arc:10");
//        header.add(searchField);

//        ImageIcon rawFilter = new ImageIcon("filter.png");
//        ImageIcon filterIcon = resizeIcon(rawFilter, 24, 24);

//        JButton filterBtn = new JButton(filterIcon);
//        filterBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        filterBtn.setContentAreaFilled(false);
//        filterBtn.setBorderPainted(false);
//        filterBtn.setFocusPainted(false);
//        filterBtn.setOpaque(false);
//        filterBtn.setPreferredSize(new Dimension(32, 32));

        // 오른쪽 정렬 후 버튼 추가
        header.add(Box.createHorizontalGlue());
//        header.add(filterBtn);   // ⬅ 이거!
//        header.add(Box.createHorizontalStrut(4));   // ← 여기에 8px 여백 추가!
//        header.add(createSearchButton());
        header.add(createSearchBox());


        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        searchResultContainer = new JPanel();
//        searchResultContainer.add(MedicalRecordListPanel.create)


//        // --- 아래는 그대로 카드들 추가 ---
//        JPanel card1 = createRoutineCard(
//                "복용루틴 1",
//                "심장사상충약 / 하루 2알",
//                "10:00AM , 2:00PM"
//        );
//        listPanel.add(card1);
//        listPanel.add(Box.createVerticalStrut(16));
//
//        JPanel card2 = createRoutineCard(
//                "복용루틴 2",
//                "약 정보를 입력해주세요",
//                ""
//        );
//        listPanel.add(card2);
//        listPanel.add(Box.createVerticalStrut(16));
//
//        JPanel card3 = createRoutineCard(
//                "복용루틴 2",
//                "약 정보를 입력해주세요",
//                ""
//        );
//        listPanel.add(card3);
//        listPanel.add(Box.createVerticalStrut(16));
//
//        JPanel card4 = createRoutineCard(
//                "복용루틴 2",
//                "약 정보를 입력해주세요",
//                ""
//        );
//        listPanel.add(card4);
//        listPanel.add(Box.createVerticalStrut(16));
//
//        JPanel card5 = createRoutineCard(
//                "복용루틴 2",
//                "약 정보를 입력해주세요",
//                ""
//        );
//        listPanel.add(card5);
//        listPanel.add(Box.createVerticalStrut(16));

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(30);

        return scroll;
    }


    /** 개별 복용 루틴 카드 */
//    private JPanel createRoutineCard(String title, String desc, String timeText) {
//        JPanel card = new JPanel(new BorderLayout());
//        card.setOpaque(true);
//        card.setBackground(Color.WHITE);
//        card.setAlignmentX(Component.LEFT_ALIGNMENT);
//        card.setPreferredSize(new Dimension(310, 100));
//        card.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16),
//                UIConstants.GRAY_SOFT, 0.5f, 10));
//
//        // 텍스트 영역 (왼쪽)
//        JPanel textPanel = new JPanel();
//        textPanel.setOpaque(false);
//        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
//
//        JLabel titleLabel = new JLabel(title);
//        titleLabel.setFont(UIConstants.FONT_SEMIBOLD_14);
//        titleLabel.setForeground(UIConstants.TEXT_PRIMARY);
//
//        JLabel descLabel = new JLabel(desc);
//        descLabel.setFont(UIConstants.FONT_REGULAR_14);
//        descLabel.setForeground(UIConstants.TEXT_SECONDARY);
//
//        textPanel.add(titleLabel);
//        textPanel.add(Box.createVerticalStrut(4));
//        textPanel.add(descLabel);
//
//        if (!timeText.isEmpty()) {
//            JLabel timeLabel = new JLabel(timeText);
//            timeLabel.setFont(UIConstants.FONT_REGULAR_14);
//            timeLabel.setForeground(UIConstants.TEXT_SECONDARY);
//            textPanel.add(Box.createVerticalStrut(4));
//            textPanel.add(timeLabel);
//        }
//
//        card.add(textPanel, BorderLayout.CENTER);
//
//        // 삭제( X ) 버튼 영역 (오른쪽)
//        JButton checkBtn = new JButton("○");
//        checkBtn.setFocusPainted(false);
//        checkBtn.setBorder(null);
//        checkBtn.setContentAreaFilled(false);
//        checkBtn.setFont(UIConstants.FONT_REGULAR_20);
//        checkBtn.setForeground(UIConstants.TEXT_LIGHT);
//        checkBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        checkBtn.setToolTipText("복용 완료 체크");
//
//        // 나중에 실제 삭제 로직 연결
//        // checkBtn.addActionListener(e -> checkRoutine(...));
//
//        JPanel right = new JPanel();
//        right.setOpaque(false);
//        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
//        right.add(Box.createVerticalGlue());
//        right.add(checkBtn);
//        right.add(Box.createVerticalGlue());
//        right.setBorder(new EmptyBorder(0, 8, 0, 0)); // 카드 오른쪽 여백 조금만
//
//        card.add(right, BorderLayout.EAST);
//
//        return card;
//    }

    /** 검색 박스 (네모 + 오른쪽 검색 아이콘) */
    private JComponent createSearchBox() {
        JPanel box = new JPanel(new BorderLayout());
        box.setOpaque(true);
        box.setBackground(Color.WHITE);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.setPreferredSize(new Dimension(310, 44));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        box.setBorder(new FlatLineBorder(
                new Insets(8, 12, 8, 4),
                UIConstants.GRAY_LIGHT,
                1.0f,
                16   // ← 검색 네모 둥근 모서리
        ));

        JTextField field = new JTextField();
        field.setBorder(null);
        field.setOpaque(false);
        field.setFont(UIConstants.FONT_REGULAR_14);
        field.setForeground(UIConstants.TEXT_PRIMARY);
        field.setCaretColor(UIConstants.TEXT_PRIMARY);
        field.setColumns(10);
        field.putClientProperty("JTextField.placeholderText", "검색어를 입력하세요");

        box.add(field, BorderLayout.CENTER);

        JButton searchBtn = new JButton();
        searchBtn.setIcon(new FlatSVGIcon("icons/search.svg", 20, 20));
        searchBtn.setContentAreaFilled(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setFocusPainted(false);
        searchBtn.setOpaque(false);
        searchBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchBtn.setPreferredSize(new Dimension(36, 36));

        searchBtn.addActionListener(e -> {
            // 아무것도 입력 안하면 전 화면으로 돌아감
            if (field.getText().isBlank()) {
                mainFrame.switchPanel(prevPanel);
            } else {

            }

            // TODO: 검색 로직 구현
//
//            searchResultContainer.revalidate();
//            searchResultContainer.repaint();
        });

        box.add(searchBtn, BorderLayout.EAST);

        return box;
    }

//    private JButton createSearchButton() {
//        JButton btn = new JButton();
//        btn.setIcon(new FlatSVGIcon("icons/search.svg", 22, 22));
//        btn.setBorderPainted(false);
//        btn.setContentAreaFilled(false);
//        btn.setFocusPainted(false);
//        btn.setOpaque(false);
//        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btn.setPreferredSize(new Dimension(32, 32));
//        btn.setMargin(new Insets(0, 0, 0, 0));
//
//        // 🔹 클릭 시 동작
//        btn.addActionListener(e -> {
//            String text = searchField.getText().trim();
//            if (text.isEmpty()) {
//                // TODO: 연결
//                // 검색어가 없으면 이전 화면으로 돌아가기
//                mainFrame.switchPanel(prevPanel);
//            } else {
//                // TODO: 검색어 있을 때의 검색 로직
//            }
//        });
//
//        return btn;
//    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

}
