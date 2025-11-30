package uitest;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PetTipsPanel extends JPanel {

    private final MainFrame mainFrame;
    // 🔹 검색 결과 카드가 들어갈 곳
    private JPanel searchResultContainer;

    public PetTipsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ⬇ 헤더 + 콘텐츠 패딩 래퍼 (SettingPanel과 동일)
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));

        // 상단 공용 헤더 (뒤로가기 → PetHomePanel 예시)
        contentWrapper.add(
                UIComponents.createHeader(() ->
                        mainFrame.switchPanel(new PetHomePanel(mainFrame))
                ),
                BorderLayout.NORTH
        );

        // 가운데 내용
        contentWrapper.add(createContent(), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);

        // 하단 탭바
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    /** 가운데 전체 영역 */
    private JComponent createContent() {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        // ── 상단 제목 영역 ─────────────────────────────
        JLabel title = new JLabel("펫 가이드");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("반려동물에 대한 간단한 지식을 알려드려요");
        subtitle.setFont(UIConstants.FONT_REGULAR_14);
        subtitle.setForeground(UIConstants.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        listPanel.add(title);
        listPanel.add(Box.createVerticalStrut(4));
        listPanel.add(subtitle);
        listPanel.add(Box.createVerticalStrut(40));

        // ── 섹션 제목 + 전체보기 ────────────────────────
        JPanel sectionHeader = new JPanel();
        sectionHeader.setOpaque(false);
        sectionHeader.setLayout(new BoxLayout(sectionHeader, BoxLayout.X_AXIS));
        sectionHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("반려동물 깨알 상식");
        sectionTitle.setFont(UIConstants.FONT_SEMIBOLD_18);
        sectionTitle.setForeground(UIConstants.TEXT_PRIMARY);
        JLabel viewAll = new JLabel("전체보기  >");
        viewAll.setFont(UIConstants.FONT_REGULAR_12);
        viewAll.setForeground(UIConstants.TEXT_SECONDARY);
        viewAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // TODO: 전체보기 클릭 시 리스트 화면으로 이동 연결

        sectionHeader.add(sectionTitle);
        sectionHeader.add(Box.createHorizontalGlue());
        sectionHeader.add(viewAll);

        listPanel.add(sectionHeader);
        listPanel.add(Box.createVerticalStrut(16));

        // ── 상단 카드 2개 (좌우) ─────────────────────────
        JPanel cardRow = new JPanel();
        cardRow.setOpaque(false);
        cardRow.setLayout(new BoxLayout(cardRow, BoxLayout.X_AXIS));
        cardRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel card1 = createArticleCard(
                "고양이도\n산책이\n필요할까?",
                new FlatSVGIcon("icons/cat.svg", 28, 28)  // 고양이 아이콘
        );

        JPanel card2 = createArticleCard(
                "사료를\n갑자기 바꾸면\n안 되는 이유",
                new FlatSVGIcon("icons/dog.svg", 22, 22)
        );

        cardRow.add(card1);
        cardRow.add(Box.createHorizontalStrut(16));
        cardRow.add(card2);

        listPanel.add(cardRow);
        listPanel.add(Box.createVerticalStrut(45));

        // ── 검색 안내 문구 ─────────────────────────────
        JLabel searchLabel = new JLabel("이 음식 반려동물이 먹어도 될까?");
        searchLabel.setFont(UIConstants.FONT_SEMIBOLD_18);
        searchLabel.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel searchLabel2 = new JLabel("위험한 음식을 검색해 보세요");
        searchLabel2.setFont(UIConstants.FONT_SEMIBOLD_18);
        searchLabel2.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel viewAllFood = new JLabel("전체보기  >");
        viewAllFood.setFont(UIConstants.FONT_REGULAR_12);
        viewAllFood.setForeground(UIConstants.TEXT_SECONDARY);
        viewAllFood.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        viewAllFood.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        // 왼쪽: 두 줄 텍스트 묶기
        JPanel leftText = new JPanel();
        leftText.setOpaque(false);
        leftText.setLayout(new BoxLayout(leftText, BoxLayout.Y_AXIS));
        leftText.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        leftText.add(searchLabel);
        leftText.add(Box.createVerticalStrut(2));
        leftText.add(searchLabel2);

        // 전체 묶는 패널 (왼쪽 텍스트 + 오른쪽 전체보기)
        JPanel infoHeader = new JPanel();
        infoHeader.setOpaque(false);
        infoHeader.setLayout(new BoxLayout(infoHeader, BoxLayout.X_AXIS));
        infoHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoHeader.add(leftText);
        infoHeader.add(Box.createHorizontalGlue());   // 오른쪽 끝으로 밀어주기
        infoHeader.add(viewAllFood);

        listPanel.add(infoHeader);
        listPanel.add(Box.createVerticalStrut(16));

        // ── 검색 박스 ──────────────────────────────────
        listPanel.add(createSearchBox());
        listPanel.add(Box.createVerticalStrut(16));

        // 🔹 검색 결과 카드들이 들어갈 영역 (처음엔 비어 있음)
        searchResultContainer = new JPanel();
        searchResultContainer.setOpaque(false);
        // 행 개수는 자동(0), 한 행 최대 2개, 가로 16, 세로 16 간격
        searchResultContainer.setLayout(new GridLayout(0, 2, 16, 16));
        searchResultContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        listPanel.add(searchResultContainer);
        listPanel.add(Box.createVerticalStrut(16));

        // 리스트를 NORTH에 붙여서 위정렬
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(30);

        return scroll;
    }

    /** 기사 카드 1개 (큰 네모 + 아래 원형 아이콘) */
    private JPanel createArticleCard(String text, Icon icon) {
        Color borderColor = UIConstants.GRAY_LIGHT;

        Color[] g = pickRandomGradient();

        JPanel card = new DiagonalGradientPanel(
                g[0],   // 시작 색
                g[1]    // 끝 색
        );

        card.setLayout(new BorderLayout());
        card.setOpaque(true);
//        card.setBackground(UIConstants.PRIMARY);
        card.setPreferredSize(new Dimension(140, 200));
        card.setMaximumSize(new Dimension(180, 220));
        card.setBorder(new FlatLineBorder(
                new Insets(16, 16, 16, 16),
                borderColor,
                1.0f,
                20   // ← 카드 네모 둥근 모서리
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.switchPanel(new PetTipsDetailPanel(mainFrame));
            }
        });

        // 제목 텍스트 (왼쪽 상단)
        JLabel label = new JLabel("<html>" + text.replace("\n", "<br>") + "</html>");
        label.setFont(UIConstants.FONT_BOLD_18);
        label.setForeground(UIConstants.TEXT_WHITE);

        JPanel textWrap = new JPanel(new BorderLayout());
        textWrap.setOpaque(false);
        textWrap.add(label, BorderLayout.NORTH);

        card.add(textWrap, BorderLayout.CENTER);

        // 아래쪽 원형 아이콘
        JPanel circle = new JPanel(new BorderLayout());
//        circle.setOpaque(true);
        circle.setBackground(Color.WHITE);
        circle.setOpaque(false);
        circle.setPreferredSize(new Dimension(58, 58));
        circle.setMaximumSize(new Dimension(58, 58));
        circle.setBorder(new FlatLineBorder(
                new Insets(2, 2, 2, 2),
                borderColor,
                1f,
                100  // ← 거의 원처럼 보이게
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        circle.add(iconLabel, BorderLayout.CENTER);

        JPanel circleWrapper = new JPanel();
        circleWrapper.setOpaque(false);
        circleWrapper.setLayout(new BoxLayout(circleWrapper, BoxLayout.X_AXIS));

        circleWrapper.add(Box.createHorizontalGlue());
        circleWrapper.add(circle);

        circleWrapper.setBorder(new EmptyBorder(12, 0, 0, 0));

        card.add(circleWrapper, BorderLayout.SOUTH);

        return card;
    }

    class DiagonalGradientPanel extends JPanel {
        private final Color start;
        private final Color end;

        public DiagonalGradientPanel(Color start, Color end) {
            this.start = start;
            this.end = end;
            setOpaque(false);   // 우리가 직접 배경 칠할 거라 false
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);   // ← 먼저 기본 처리

            Graphics2D g2 = (Graphics2D) g.create();

            int w = getWidth();
            int h = getHeight();

            GradientPaint gp = new GradientPaint(
                    0, 0, start,
                    w, h, end      // 대각선 그라데이션
            );

            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w, h, 20, 20);  // arc 20 유지
            g2.dispose();
        }
    }

    private static final Random RND = new Random();

    private static final Color[][] GRADIENT_SETS = {
            { UIConstants.PRIMARY, new Color(206, 238, 203, 255) },
            { new Color(206, 238, 203, 255), UIConstants.PRIMARY },
            { UIConstants.PRIMARY, UIConstants.ACCENT_PINK },
            { new Color(0x95e0e1), new Color(0xffeac2) },
            { new Color(0xEFB499), new Color(0x95e0e1) },
            { new Color(0xa1cacf), new Color(0xfadbce) },
            { new Color(0xfadbce), new Color(0xa1cacf) },
            { new Color(0xa1cacf), new Color(0xaba4c6) },
            { new Color(0xcdc5ec), new Color(0xc5e2ba) },
            { new Color(0xc5e2ba), new Color(0xcdc5ec) },
            { new Color(0xf0bfce), new Color(0xfadbce) }
    };

    private static Color[] pickRandomGradient() {
        return GRADIENT_SETS[RND.nextInt(GRADIENT_SETS.length)];
    }

    /** 검색 박스 (네모 + 오른쪽 검색 아이콘) */
    private JComponent createSearchBox() {
        Color borderColor = UIConstants.GRAY_LIGHT;

        JPanel box = new JPanel(new BorderLayout());
        box.setOpaque(true);
        box.setBackground(Color.WHITE);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.setPreferredSize(new Dimension(310, 44));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        box.setBorder(new FlatLineBorder(
                new Insets(8, 12, 8, 4),
                borderColor,
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
        field.putClientProperty("JTextField.placeholderText", "예: 포도, 양파, 초콜릿 ...");

        box.add(field, BorderLayout.CENTER);

        JButton searchBtn = new JButton();
        searchBtn.setIcon(new FlatSVGIcon("icons/search.svg", 20, 20));
        searchBtn.setContentAreaFilled(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setFocusPainted(false);
        searchBtn.setOpaque(false);
        searchBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchBtn.setPreferredSize(new Dimension(36, 36));

        // 🔹 아직 로직은 없으니, 눌렀을 때 더미 카드 3개 생성
        searchBtn.addActionListener(e -> {
            searchResultContainer.removeAll();

            searchResultContainer.add(createFoodResultCard(
                    "포도",
                    new FlatSVGIcon("icons/dog.svg", 20, 20)
            ));

            searchResultContainer.add(createFoodResultCard(
                    "양파",
                    new FlatSVGIcon("icons/cat.svg", 20, 20)
            ));
            // searchResultContainer.add(Box.createHorizontalStrut(12));

            searchResultContainer.add(createFoodResultCard(
                    "다크초콜릿",
                    new FlatSVGIcon("icons/dog.svg", 20, 20)
            ));

            searchResultContainer.revalidate();
            searchResultContainer.repaint();
        });

        box.add(searchBtn, BorderLayout.EAST);

        return box;
    }

    /** 음식 검색 결과 카드 1개 */
    private JPanel createFoodResultCard(String foodName, Icon petIcon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(false);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(150, 190));
        card.setMaximumSize(new Dimension(200, 190));
        card.setBackground(UIConstants.GRAY_ULTRA_LIGHT);

        card.setBorder(new FlatLineBorder(
                new Insets(8, 8, 2, 8),
                UIConstants.GRAY_LIGHT,
                1.0f,
                16
        ));

        /* ───────────── ① 음식 이미지 로드 ───────────── */
        // 파일명은 foodName + ".jpg" 라고 가정
        String imgPath = "/images/foods/" + foodName + ".jpg";
        ImageIcon raw = null;

        var url = getClass().getResource(imgPath);
        if (url != null) {
            raw = new ImageIcon(url);
        }

        // 정사각형으로 리사이징
        Image scaled = (raw != null)
                ? raw.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH)
                : new BufferedImage(140, 140, BufferedImage.TYPE_INT_RGB);

        JLabel imgLabel = new JLabel(new ImageIcon(scaled));
        imgLabel.setOpaque(false);
        imgLabel.setAlignmentX(0.5f);
        imgLabel.setAlignmentY(0.5f);
        imgLabel.setBorder(new FlatLineBorder(new Insets(0, 0, 0, 0),
                UIConstants.GRAY_LIGHT));

        JPanel overlay = new JPanel();
        overlay.setOpaque(false);
        overlay.setLayout(new OverlayLayout(overlay));
        overlay.setPreferredSize(new Dimension(120, 120));
        overlay.setBorder(new EmptyBorder(4, 2, 0, 2));

        overlay.add(imgLabel);

        card.add(overlay, BorderLayout.CENTER);

        /* ───────────── ③ 아래쪽 정보(이름 + 위험태그 + X아이콘) ───────────── */
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        bottom.setBorder(new EmptyBorder(10, 6, 6, 6));

        JLabel nameLabel = new JLabel(foodName);
        nameLabel.setFont(UIConstants.FONT_REGULAR_14);
        nameLabel.setForeground(UIConstants.TEXT_PRIMARY);

        /* ★ 위험 태그: 둥근 사각형 + 테두리 + TEXT_PRIMARY */
        JLabel dangerTag = new JLabel("위험");
        dangerTag.setFont(UIConstants.FONT_SEMIBOLD_12);
        dangerTag.setForeground(UIConstants.TEXT_PRIMARY);
        dangerTag.setBorder(new FlatLineBorder(
                new Insets(4, 8, 4, 8),
                UIConstants.TEXT_LIGHT,
                0.5f,
                12
        ));

        JLabel xLabel = new JLabel(new FlatSVGIcon("icons/forbidden.svg",16, 16));

        bottom.add(nameLabel);
        bottom.add(Box.createHorizontalGlue());
        bottom.add(dangerTag);
        bottom.add(Box.createHorizontalStrut(8));
        bottom.add(xLabel);

        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }


}
