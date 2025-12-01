package uitest;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.Random;

public class PetTipsDetailPanel extends JPanel {

    private final MainFrame mainFrame;

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

    public PetTipsDetailPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단/내용 래퍼
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));

        // 공용 헤더 (뒤로가기 → 펫 가이드 리스트로 이동)
        contentWrapper.add(
                UIComponents.createHeader(() ->
                        mainFrame.switchPanel(new PetTipsPanel(mainFrame))
                ),
                BorderLayout.NORTH
        );

        // 가운데 내용
        contentWrapper.add(createContent(), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);

        // 공용 하단 탭바
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    /** 가운데 전체 스크롤 영역 */
    private JComponent createContent() {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(24, 10, 24, 10));

        // ── 히어로(빨간) 영역 ─────────────────────────────
        String heroImagePath = HERO_IMAGE_PATHS[RND.nextInt(HERO_IMAGE_PATHS.length)];
        Image heroImage = loadHeroImage(heroImagePath);

        HeroPanel heroPanel = new HeroPanel(
                heroImage,
                "행동",
                "고양이는 왜\n높은 곳을 좋아할까?"
        );
        heroPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(heroPanel);

        container.add(Box.createVerticalStrut(32));

        // ── 본문 텍스트 ─────────────────────────────────
        String bodyText =
                "고양이는 높은 곳에서 주변을 내려다보면 안정감을 느끼고 영역을 파악하기 쉬워요. " +
                        "또 잠재적인 위험을 피할 수 있다고 느끼기 때문에 자연스럽게 높은 곳을 찾게 돼요.\n" +
                        "캣타워나 선반 같은 수직 공간을 충분히 제공하면 스트레스 감소와 안정감 향상에 큰 도움이 됩니다.";

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

    // 이미지 로더 메서드 수정
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

    /** 상단 영역 (배경 이미지 + 카테고리 배지 + 제목 + 오른쪽 원형 아이콘) */
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

            // ── 아래쪽(기준) 텍스트 영역 ──
            JPanel textArea = new JPanel();
            textArea.setOpaque(false);
            textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
            // ★ 아래에서 20px 띄우기
            textArea.setBorder(new EmptyBorder(0, 20, 20, 0));

            // 카테고리 배지
            JLabel badge = new JLabel(category, SwingConstants.CENTER);
            badge.setFont(UIConstants.FONT_REGULAR_12);
            badge.setForeground(UIConstants.TEXT_DARKGRAY);
//            badge.setOpaque(true);
            badge.setBackground(Color.WHITE);
            badge.setBorder(new EmptyBorder(4, 8, 4, 8));
            badge.putClientProperty("FlatLaf.style", "arc:999");

            // 제목
            JLabel titleLabel = new JLabel("<html>" + title.replace("\n", "<br>") + "</html>");
            titleLabel.setFont(UIConstants.FONT_EXTRABOLD_28);
            titleLabel.setForeground(Color.WHITE);

            textArea.add(badge);
            textArea.add(Box.createVerticalStrut(4));
            textArea.add(titleLabel);

            // ★ 글자 전체를 아래에 붙이기
            add(textArea, BorderLayout.SOUTH);


            // 오른쪽 상단 원형 아이콘 (그대로)
            JPanel circle = new JPanel(new BorderLayout());
            circle.setOpaque(false);
            circle.setBackground(Color.WHITE);
            circle.putClientProperty("FlatLaf.style", "arc:999");
            int d = 48;
            circle.setPreferredSize(new Dimension(d, d));  // 원형 크기 작게
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
            circleWrap.setBorder(new EmptyBorder(20, 0, 0, 20)); // 오른쪽 위 위치
            circleWrap.add(circle, BorderLayout.NORTH);

            add(circleWrap, BorderLayout.EAST);
        }


        /** 파란 히어로 영역 오른쪽에 쓰는 진짜 원형 아이콘 패널 */
        private static class CircleIconPanel extends JPanel {

            private final Icon icon;

            public CircleIconPanel(Icon icon) {
                this.icon = icon;
                setOpaque(false);                         // 네모 배경 안 칠하기!

                int d = 48;
                setPreferredSize(new Dimension(d, d));
                setMaximumSize(new Dimension(d, d));
                setLayout(new GridBagLayout());          // 아이콘을 정확히 가운데로

                JLabel label = new JLabel(icon);
                add(label);                               // 가운데 배치
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

            // 2) 그 위에 "위는 투명, 아래는 까만색" 세로 그라디언트 덮기
            Color top = new Color(255, 255, 255, 0);       // 완전 투명
            Color bottom = new Color(41, 41, 41, 162);  // 꽤 진한 검정 (0~255)

            GradientPaint gp = new GradientPaint(
                    0, 0,   top,      // 위쪽은 투명
                    0, h,   bottom    // 아래로 갈수록 검정
            );
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);  // 사진 전체에 덮어 씌우기

            g2.dispose();
        }
    }

}
