package ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;
import content.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class PetTipsPanel extends Base {
    private final MainFrame mainFrame;
    private JPanel searchResultContainer;
    private ArrayList<PetTip> petTips = petTipMgr.getAll();
    private ArrayList<UnsafePetFood> foods = unsafePetFoodMgr.getAll();

    public PetTipsPanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));

        contentWrapper.add(
                UIComponents.createHeader(() ->
                        mainFrame.switchPanel(new HomePanel(mainFrame))
                ),
                BorderLayout.NORTH
        );

        contentWrapper.add(createContent(), BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    /* 가운데 전체 영역 */
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

        String[] titles = {
                "반려견은\n왜 산책이\n필요할까?",
                "고양이도\n산책이\n필요할까?",
                "반려견에게\n장난감이\n필요한 이유",
                "사료를\n갑자기 바꾸면\n안 되는 이유",
                "고양이는 왜\n높은 곳을\n좋아할까?",
                "강아지가\n밥을 너무\n빨리 먹을 때",
                "고양이가\n물을 잘\n안 마시는\n이유",
                "고양이가\n밤에 활발해지는\n이유",
                "강아지가\n배를 보이며\n눕는 이유",
                "고양이가\n물 그릇을\n자꾸 엎는 이유",
                "고양이가\n‘야간 질주’를\n하는 이유",
                "강아지가\n흙을 파는\n이유",
                "고양이가\n모래를 많이\n흩뿌리는 이유",
                "고양이가\n물그릇을\n멀리 두면 잘\n마시는 이유",
                "강아지가\n자꾸 하품하는\n이유",
                "고양이가\n갑자기 우다다\n뛰는 이유",
                "강아지가\n다른 강아지의\n뒤를 킁킁거리는\n이유",
                "강아지가\n신발끈이나\n옷감을 물어뜯는\n이유",
                "고양이가\n창밖을\n오래 보는\n이유",
                "고양이가\n모래 대신\n바닥에 배변하는\n이유",
                "고양이가\n침대를 좋아하는\n이유",
                "고양이가\n사람을 빤히\n바라보는 이유",
                "고양이가\n배를 보이지만\n만지면 싫어하는\n이유",
                "고양이가\n물을 손으로\n휘젓는 이유",
                "반려견은\n하루 이틀 정도\n혼자 있어도\n될까?"
        };

        List<String> picked = pickTwoDistinct(titles);
        PetTip pickedTip1 = petTipMgr.findByTitle(picked.get(0));
        PetTip pickedTip2 = petTipMgr.findByTitle(picked.get(1));

        List<Color[]> cardColor = pickTwoDistinct(GRADIENT_SETS);
        JPanel card1 = createArticleCard(
                picked.get(0),
                pickedTip1,
                cardColor.get(0)
        );

        JPanel card2 = createArticleCard(
                picked.get(1),
                pickedTip2,
                cardColor.get(1)
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

        JLabel searchLabel2 = new JLabel("궁금한 음식을 검색해 보세요");
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
        infoHeader.add(Box.createHorizontalGlue());
        infoHeader.add(viewAllFood);

        listPanel.add(infoHeader);
        listPanel.add(Box.createVerticalStrut(16));

        // ── 검색 박스 ──────────────────────────────────
        listPanel.add(createSearchBox());
        listPanel.add(Box.createVerticalStrut(16));

        // 검색 결과 카드들이 들어갈 영역 (처음엔 비어 있음)
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

    /* 짤막 상식 카드 UI */
    private JPanel createArticleCard(String text, PetTip tip, Color[] cardColor) {
        Color borderColor = UIConstants.GRAY_LIGHT;

        Icon icon;
        FlatSVGIcon dog = new FlatSVGIcon("icons/dog.svg", 22, 22);
        FlatSVGIcon cat = new FlatSVGIcon("icons/cat.svg", 28, 28);
        FlatSVGIcon both = new FlatSVGIcon("icons/heart.svg", 22, 22);

        if (tip.getPetType().size() >= 2)
            icon = both;
        else if (tip.getPetType().contains(PetType.CAT))
            icon = cat;
        else
            icon = dog;

        JPanel card = new DiagonalGradientPanel(
                cardColor[0],
                cardColor[1]
        );

        card.setLayout(new BorderLayout());
        card.setOpaque(true);
        card.setPreferredSize(new Dimension(100, 200));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        card.setBorder(new FlatLineBorder(
                new Insets(16, 16, 16, 16),
                borderColor,
                1.0f,
                20
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.switchPanel(new PetTipsDetailPanel(mainFrame, tip));
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
        circle.setBackground(Color.WHITE);
        circle.setOpaque(false);
        circle.setPreferredSize(new Dimension(58, 58));
        circle.setMaximumSize(new Dimension(58, 58));
        circle.setBorder(new FlatLineBorder(
                new Insets(2, 2, 2, 2),
                borderColor,
                1f,
                100
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
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            int w = getWidth();
            int h = getHeight();

            GradientPaint gp = new GradientPaint(
                    0, 0, start,
                    w, h, end
            );

            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w, h, 20, 20);
            g2.dispose();
        }
    }

    private static final Color[][] GRADIENT_SETS = {
            { UIConstants.PRIMARY, new Color(206, 238, 203, 255) },
            { UIConstants.PRIMARY, UIConstants.ACCENT_PINK },
            { new Color(0x95e0e1), new Color(0xffeac2) },
            { new Color(0xEFB499), new Color(0x95e0e1) },
            { new Color(0xa1cacf), new Color(0xfadbce) },
            { new Color(0xfadbce), new Color(0xa1cacf) },
            { new Color(0xa1cacf), new Color(0xaba4c6) },
            { new Color(0xcdc5ec), new Color(0xc5e2ba) },
            { new Color(0xc5e2ba), new Color(0xECC5EA) },
            { new Color(0xEBB5C7), new Color(0xFFEEDB) }
    };

    /* 검색 박스 (네모 + 오른쪽 검색 아이콘) */
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
                16
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

        field.addActionListener(e -> searchBtn.doClick());

        searchBtn.addActionListener(e -> {

            String kwd = field.getText();

            if (kwd.isBlank()) {
                searchResultContainer.removeAll();
                // 입력 안하고 그냥 검색 누르면 다 출력
                for (UnsafePetFood f : foods) {
                    JPanel card = createFoodResultCard(f);
                    searchResultContainer.add(card);
                }
            } else {
                searchResultContainer.removeAll();
                for (UnsafePetFood f : foods) {
                    if (f.matches(kwd)) {
                        JPanel card = createFoodResultCard(f);
                        searchResultContainer.add(card);
                    }
                }
            }
            searchResultContainer.revalidate();
            searchResultContainer.repaint();
        });

        box.add(searchBtn, BorderLayout.EAST);

        return box;
    }

    /* 음식 검색 결과 카드 UI */
    private JPanel createFoodResultCard(UnsafePetFood f) {
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
        String imagePath = f.getFoodImage();
        ImageIcon raw = null;
        JLabel imgLabel;

        // 1) imagePath null / 빈 문자열 방어
        if (imagePath == null || imagePath.isBlank()) {
            // 이미지 없는 경우용 플레이스홀더
            imgLabel = new JLabel();
            imgLabel.setOpaque(false);
            imgLabel.setAlignmentX(0.5f);
            imgLabel.setAlignmentY(0.5f);
            imgLabel.setBackground(UIConstants.GRAY_LIGHT);
            imgLabel.setBorder(new FlatLineBorder(new Insets(0, 0, 0, 0),
                    UIConstants.GRAY_LIGHT));
        } else {
            String path = imagePath.startsWith("/") ? imagePath : "/" + imagePath;

            var url = getClass().getResource(path);

            if (url != null) {
                raw = new ImageIcon(url);

                Image scaled = raw.getImage()
                        .getScaledInstance(140, 140, Image.SCALE_SMOOTH);

                imgLabel = new JLabel(new ImageIcon(scaled));
                imgLabel.setOpaque(false);
                imgLabel.setAlignmentX(0.5f);
                imgLabel.setAlignmentY(0.5f);
                imgLabel.setBorder(new FlatLineBorder(new Insets(0, 0, 0, 0),
                        UIConstants.GRAY_LIGHT));
            } else {
                // 리소스 못 찾았을 때도 안전하게 처리
                imgLabel = new JLabel();
                imgLabel.setOpaque(true);
                imgLabel.setAlignmentX(0.5f);
                imgLabel.setAlignmentY(0.5f);
                imgLabel.setBackground(Color.WHITE);
                imgLabel.setBorder(new FlatLineBorder(new Insets(0, 0, 0, 0),
                        UIConstants.GRAY_LIGHT));
            }
        }

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

        JLabel nameLabel = new JLabel(f.getFoodName());
        nameLabel.setFont(UIConstants.FONT_REGULAR_14);
        nameLabel.setForeground(UIConstants.TEXT_PRIMARY);

        /* 위험 태그 */
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
//        bottom.add(Box.createHorizontalStrut(8));
//        bottom.add(xLabel);

        card.add(bottom, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showFoodPopup(f);
            }
        });

        return card;
    }

    private void showFoodPopup(UnsafePetFood f) {
        Window owner = SwingUtilities.getWindowAncestor(this);

        JDialog dialog = new JDialog(owner);
        dialog.setModal(true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));
        ((JComponent) dialog.getContentPane()).setOpaque(false);
        dialog.setLayout(new BorderLayout());

        // 팝업 카드
        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(true);
        root.setBackground(Color.WHITE);
        root.setBorder(new com.formdev.flatlaf.ui.FlatLineBorder(
                new Insets(26, 26, 26, 26),
                UIConstants.GRAY_SOFT, 1.0f, 20
        ));

        // 상단: 제목 + 닫기 버튼
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel name = new JLabel(f.getFoodName());
        name.setFont(UIConstants.FONT_EXTRABOLD_20);
        name.setForeground(UIConstants.TEXT_PRIMARY);

        JButton close = new JButton();
        close.setIcon(new FlatSVGIcon("icons/close.svg", 14, 14));
        close.setContentAreaFilled(false);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.setOpaque(false);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dialog.dispose());

        top.add(name, BorderLayout.WEST);
        top.add(close, BorderLayout.EAST);

        // 가운데: 이미지 + 정보(위험단계/허용여부) + 설명
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(12));

        // 위험단계 / 허용여부 (라벨 2개)
        JLabel risk = new JLabel("위험단계: "
                + f.getRiskLevel().getKoName());
        if (f.getRiskLevel() == RiskLevel.HIGH) {
            risk.setForeground(new Color(182, 31, 31));
        } else if (f.getRiskLevel() == RiskLevel.MEDIUM) {
            risk.setForeground(new Color(199, 127, 42));
        } else {
            risk.setForeground(new Color(94, 147, 80));
        }
        risk.setFont(UIConstants.FONT_SEMIBOLD_14);
        risk.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel allowed = new JLabel("허용여부: "
                + f.getAllowanceLevel().getKoName());
        if (f.getAllowanceLevel() == AllowanceLevel.FORBIDDEN) {
            allowed.setForeground(new Color(182, 31, 31));
        } else if (f.getAllowanceLevel() == AllowanceLevel.CAUTION) {
            allowed.setForeground(new Color(199, 127, 42));
        } else {
            allowed.setForeground(new Color(94, 147, 80));
        }
        allowed.setFont(UIConstants.FONT_SEMIBOLD_14);
        allowed.setAlignmentX(Component.LEFT_ALIGNMENT);

        center.add(risk);
        center.add(Box.createVerticalStrut(8));
        center.add(allowed);
        center.add(Box.createVerticalStrut(12));

        // 설명
        JTextArea desc = new JTextArea(f.getDescription());
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setFont(UIConstants.FONT_REGULAR_14);
        desc.setForeground(UIConstants.TEXT_SECONDARY);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane descScroll = new JScrollPane(desc);
        descScroll.setBorder(null);
        descScroll.setOpaque(false);
        descScroll.getViewport().setOpaque(false);
        descScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        descScroll.setPreferredSize(new Dimension(360, 120));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        center.add(descScroll);

        root.add(top, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        dialog.add(root, BorderLayout.CENTER);

        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private static <T> List<T> pickTwoDistinct(T[] arr) {
        Random rand = new Random();

        int i = rand.nextInt(arr.length);
        int j;
        do {
            j = rand.nextInt(arr.length);
        } while (j == i);

        return List.of(arr[i], arr[j]);
    }
}
