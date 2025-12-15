package ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import content.PetTip;
import content.PetType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PetTipsDetailPanel extends Base {

    private final MainFrame mainFrame;
    private ArrayList<PetTip> petTips = petTipMgr.getAll();
    private PetTip tip;
    private PetType petType;

    private static final String[] HERO_IMAGE_PATHS = {
            "/images/pet_tips/cat-1.jpg",
            "/images/pet_tips/cat-2.jpg",
            "/images/pet_tips/cat-3.jpg",
            "/images/pet_tips/cat-4.jpg",
            "/images/pet_tips/cat-5.jpg",
            "/images/pet_tips/cat-6.jpg",
            "/images/pet_tips/cat-7.jpg",
            "/images/pet_tips/cat-8.jpg"
    };

    private static final Random RND = new Random();

    public PetTipsDetailPanel(MainFrame mainFrame, String title) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        String inputTitle = title.replace(" ", "")
                .replace("\\n", "");
        for (PetTip t : petTips) {
            String thisTitle = t.getTitle().replace(" ", "")
                    .replace("\\n", "");
            if (inputTitle.equals(thisTitle)) {
                        tip = t;
            }
        }

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));

        contentWrapper.add(
                UIComponents.createHeader(() ->
                        mainFrame.switchPanel(new PetTipsPanel(mainFrame))
                ),
                BorderLayout.NORTH
        );

        contentWrapper.add(createContent(), BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    /* 가운데 전체 스크롤 영역 */
    private JComponent createContent() {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(24, 10, 24, 10));

        // ── 사진 영역 ─────────────────────────────
        String heroImagePath = HERO_IMAGE_PATHS[RND.nextInt(HERO_IMAGE_PATHS.length)];
        Image heroImage = loadHeroImage(heroImagePath);

        HeroPanel heroPanel = new HeroPanel(
                heroImage,
                tip.getCategory().getKoName(),
                tip.getTitle()
        );
        heroPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(heroPanel);

        container.add(Box.createVerticalStrut(32));

        // ── 본문 텍스트 ─────────────────────────────────
        String bodyText = tip.getContent();

        JTextArea bodyArea = new JTextArea(bodyText);
        bodyArea.setEditable(false);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        bodyArea.setOpaque(false);
        bodyArea.setFont(UIConstants.FONT_REGULAR_16);
        bodyArea.setForeground(UIConstants.TEXT_DARKGRAY);
        bodyArea.setBorder(new EmptyBorder(8, 16, 16, 16));
        bodyArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyArea.setHighlighter(null);
        bodyArea.setFocusable(false);

        container.add(bodyArea);

        // 스크롤에 감싸기 (위쪽 정렬)
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(container, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(30);

        return scroll;
    }

    private Image loadHeroImage(String path) {
        try {
            var url = PetTipsDetailPanel.class.getResource(path);  // Maven: src/main/resources 기준
            if (url == null) {
                System.out.println("Hero image not found: " + path);
                return null;
            }
            ImageIcon icon = new ImageIcon(url);
            return icon.getImage();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 이미지 못 찾으면 그냥 단색 배경만 사용
        }
    }

    /* 상단 영역 (배경 이미지 + 카테고리 배지 + 제목) */
    private static class HeroPanel extends JPanel {

        private final Image heroImage;
        private final String category;
        private final String title;

        public HeroPanel(Image heroImage, String category, String title) {
            this.heroImage = heroImage;
            this.category = category;
            this.title = title;

            setOpaque(false);
            setPreferredSize(new Dimension(0, 230));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
            setLayout(new BorderLayout());

            // ── 아래쪽 텍스트 영역 ──
            JPanel textArea = new JPanel();
            textArea.setOpaque(false);
            textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
            textArea.setBorder(new EmptyBorder(0, 20, 20, 0));

            // 카테고리 배지
            JLabel badge = new JLabel(category, SwingConstants.CENTER);
            badge.setFont(UIConstants.FONT_REGULAR_12);
            badge.setForeground(UIConstants.TEXT_DARKGRAY);
            badge.setBackground(Color.WHITE);
            badge.setBorder(new EmptyBorder(4, 8, 4, 8));
            badge.putClientProperty("FlatLaf.style", "arc:999");

            // 제목
            JLabel titleLabel = new JLabel("<html>" + title.replace("\\n", "<br>") + "</html>");
            titleLabel.setFont(UIConstants.FONT_EXTRABOLD_28);
            titleLabel.setForeground(Color.WHITE);

            textArea.add(badge);
            textArea.add(Box.createVerticalStrut(4));
            textArea.add(titleLabel);

            add(textArea, BorderLayout.SOUTH);


            // 오른쪽 상단 원형 아이콘
            JPanel circle = new JPanel(new BorderLayout());
            circle.setOpaque(false);
            circle.setBackground(Color.WHITE);
            circle.putClientProperty("FlatLaf.style", "arc:999");
            int d = 48;
            circle.setPreferredSize(new Dimension(d, d));
            circle.setMaximumSize(new Dimension(d, d));

            FlatSVGIcon icon = new FlatSVGIcon("icons/cat.svg", 24, 24);
            FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
            colorFilter.add(new Color(0x383838), UIConstants.TEXT_DARKGRAY);
            icon.setColorFilter(colorFilter);

            JLabel iconLabel = new JLabel(icon);
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iconLabel.setVerticalAlignment(SwingConstants.CENTER);

            circle.add(iconLabel);

            JPanel circleWrap = new JPanel(new BorderLayout());
            circleWrap.setOpaque(false);
            circleWrap.setBorder(new EmptyBorder(20, 0, 0, 20));
            circleWrap.add(circle, BorderLayout.NORTH);

            // 보류
//            add(circleWrap, BorderLayout.EAST);
        }


        /* 원형 아이콘 패널 */
        private static class CircleIconPanel extends JPanel {

            private final Icon icon;

            public CircleIconPanel(Icon icon) {
                this.icon = icon;
                setOpaque(false);

                int d = 48;
                setPreferredSize(new Dimension(d, d));
                setMaximumSize(new Dimension(d, d));
                setLayout(new GridBagLayout());

                JLabel label = new JLabel(icon);
                add(label);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int d = Math.min(getWidth(), getHeight());
                int x = (getWidth() - d) / 2;
                int y = (getHeight() - d) / 2;

                // 흰색 원
                g2.setColor(Color.WHITE);
                g2.fillOval(x, y, d, d);

                // 테두리
                g2.setColor(UIConstants.GRAY_SOFT);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(x, y, d, d);

                g2.dispose();
                super.paintComponent(g);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            int w = getWidth();
            int h = getHeight();

            // 1) 먼저 사진 그리기
            if (heroImage != null) {
                g2.drawImage(heroImage, 0, 0, w, h, this);
            } else {
                g2.setColor(UIConstants.PRIMARY);
                g2.fillRect(0, 0, w, h);
            }

            // 2) 그 위에 세로 그라디언트 덮기
            Color top = new Color(255, 255, 255, 0);    // 투명
            Color bottom = new Color(41, 41, 41, 162);  // 반투명한 검은색

            GradientPaint gp = new GradientPaint(
                    0, 0,   top,
                    0, h,   bottom
            );
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);  // 사진 전체에 덮어 씌우기

            g2.dispose();
        }
    }
}
