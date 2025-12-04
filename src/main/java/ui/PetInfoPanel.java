package ui;

import uiutil.WrapLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PetInfoPanel extends JPanel {

    private final MainFrame mainFrame;

    public PetInfoPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단바 + 내용 + 하단 "정보 수정" 버튼에만 패딩
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));

        // 상단 헤더 (뒤로가기 → 펫 홈으로 가정)
        contentWrapper.add(
                UIComponents.createHeader(() ->
                        mainFrame.switchPanel(new HomePanel(mainFrame))
                ),
                BorderLayout.NORTH
        );

        contentWrapper.add(createContent(), BorderLayout.CENTER);
        contentWrapper.add(createEditButtonBar(), BorderLayout.SOUTH);

        add(contentWrapper, BorderLayout.CENTER);

        // 하단 탭바
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    // ================== 중앙 콘텐츠 ==================
    private JComponent createContent() {
        // 🔹 전체 영역을 GridBagLayout으로 → 항상 가로/세로 중앙 정렬
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(24, 16, 24, 16));

        // 안쪽 실제 콘텐츠 컬럼
        JPanel contentColumn = new JPanel();
        contentColumn.setOpaque(false);
        contentColumn.setLayout(new BoxLayout(contentColumn, BoxLayout.Y_AXIS));
        contentColumn.setMaximumSize(new Dimension(480, Integer.MAX_VALUE));
        contentColumn.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        contentColumn.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 위 여백 조금
//        contentColumn.add(Box.createVerticalStrut(40));

        /* ==== 프로필 사진 + 텍스트 (세로, 중앙) ==== */
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setAlignmentX(Component.CENTER_ALIGNMENT);

        JComponent profileArea = createProfileArea();
        profileArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        top.add(profileArea);

        JLabel helloLabel = new JLabel("제 이름은 후추예요!");
        helloLabel.setFont(UIConstants.FONT_EXTRABOLD_24);
        helloLabel.setForeground(UIConstants.TEXT_PRIMARY);
        helloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel typeLabel = new JLabel("강아지 (푸들)");
        typeLabel.setFont(UIConstants.FONT_REGULAR_14);
        typeLabel.setForeground(UIConstants.TEXT_SECONDARY);
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel birthLabel = new JLabel("2020-10-20");
        birthLabel.setFont(UIConstants.FONT_REGULAR_14);
        birthLabel.setForeground(UIConstants.TEXT_SECONDARY);
        birthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        top.add(Box.createVerticalStrut(20));
        top.add(helloLabel);
        top.add(Box.createVerticalStrut(10));
        top.add(typeLabel);
        top.add(Box.createVerticalStrut(4));
        top.add(birthLabel);

        contentColumn.add(top);
        contentColumn.add(Box.createVerticalStrut(32));

        /* ==== 성격 태그 ==== */
        JLabel tagTitle = new JLabel("성격 태그");
        tagTitle.setFont(UIConstants.FONT_SEMIBOLD_18);
        tagTitle.setForeground(UIConstants.TEXT_PRIMARY);
        tagTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentColumn.add(tagTitle);
        contentColumn.add(Box.createVerticalStrut(12));

        JComponent tagArea = createTagArea();
        tagArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentColumn.add(tagArea);

        contentColumn.add(Box.createVerticalStrut(16));

        // ⛔ 여기서는 더 이상 Box.createVerticalGlue() 넣지 말기!

        // 🔹 GridBagLayout으로 중앙 배치
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;   // 가로/세로 중앙
        // 🔽🔽 세로는 그대로, 가로는 꽉 채우도록
        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.fill = GridBagConstraints.NONE;       // 필요하면 HORIZONTAL 로 변경

        centerWrapper.add(contentColumn, gbc);

        return centerWrapper;
    }


    private JComponent createProfileArea() {

        Image tmpImg = null;
        try {
            java.net.URL url = getClass().getResource("/images/pet_profile/후추.jpg");
            if (url != null) {
                tmpImg = new ImageIcon(url).getImage();
            }
        } catch (Exception ignored) {}

        final Image img = tmpImg;

        JPanel panel = new JPanel() {

            // 👇 최소/선호 크기를 확실히 160x160으로 고정
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(180, 180);
            }

            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int size = Math.min(getWidth(), getHeight());
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                g2.setColor(new Color(0xF5F7FB));
                g2.fillOval(x, y, size, size);

                if (img != null) {
                    Shape clip = new java.awt.geom.Ellipse2D.Double(x, y, size, size);
                    g2.setClip(clip);
                    g2.drawImage(img, x, y, size, size, this);
                    g2.setClip(null);
                }

                g2.setColor(UIConstants.PRIMARY);
//                g2.setStroke(new BasicStroke(2f));
//                g2.drawOval(x, y, size, size);

                g2.dispose();
            }
        };

        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return panel;
    }



    private JComponent createTagArea() {
        String[] tags = {
                "장난스러운", "사람을 좋아하는", "소심한",
                "산책을 좋아하는", "용감한", "활발한",
                "얌전한", "호기심 많은", "낯가리는",
                "잘 짖는", "조용한", "식성이 좋은"
        };

        JPanel tagPanel = new JPanel(new WrapLayout());
        tagPanel.setOpaque(false);

        for (String t : tags) {
            JToggleButton btn = new JToggleButton(t);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setFont(UIConstants.FONT_REGULAR_14);
            btn.setForeground(UIConstants.TEXT_PRIMARY);

            btn.putClientProperty(
                    "FlatLaf.Style",
                    "arc:10;" +
                            "borderWidth:1;" +
                            "borderColor:#D0D7E2;" +
                            "focusWidth:0;" +
                            "innerFocusWidth:0;"
            );
            btn.setOpaque(false);
            btn.setBackground(Color.WHITE);
            btn.setMargin(new Insets(6, 6, 6, 6));

            btn.addChangeListener(e -> {
                if (btn.isSelected()) {
                    btn.setBackground(UIConstants.PRIMARY);
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(UIConstants.TEXT_PRIMARY);
                }
            });

            tagPanel.add(btn);
        }

        // 🔹 태그 영역을 스크롤 가능하게 감싸기
        JScrollPane scroll = new JScrollPane(tagPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        // 높이만 제한, 가로는 전체 폭 사용
        scroll.setPreferredSize(new Dimension(0, 160)); // 한 2~3줄 보이게
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        return scroll;
    }


    /* ================== 정보 수정 버튼 바 ================== */
    private JComponent createEditButtonBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(0, 0, 15, 0));

        JButton editBtn = new JButton("정보 수정");
        editBtn.setFocusPainted(false);
        editBtn.setBackground(UIConstants.PRIMARY);
        editBtn.setForeground(Color.WHITE);
        editBtn.putClientProperty("FlatLaf.style", "arc:10");

        editBtn.setPreferredSize(new Dimension(0, 48));
        editBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        // TODO: 펫 정보 수정 폼으로 이동 연결
        // editBtn.addActionListener(e ->
        //         mainFrame.switchPanel(new PetInfoEditFormPanel(mainFrame))
        // );

        bar.add(editBtn, BorderLayout.CENTER);
        return bar;
    }

}
