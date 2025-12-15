package ui;

import com.formdev.flatlaf.ui.FlatLineBorder;
import core.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class HealthPanel extends Base {

    private final MainFrame mainFrame;
    private User user;
    private Pet pet;
    protected ArrayList<HealthRecord> records;

    public HealthPanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.user = mainFrame.getLoggedInUser();
        this.pet = mainFrame.getLoggedInUserPet();
        this.records = healthMgr.getAllByOwner(user);

        // TODO: 아래 테스트용 코드 추후 삭제 (2줄)
        System.out.println("건강패널 ID: " + user.getId());
        System.out.println("건강패널 펫: " + pet.getName());

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 헤더 + 내용
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new HomePanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createContent(), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);
        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    /* ================== 가운데 내용 ================== */
    private JComponent createContent() {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        // 제목 행
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("건강 기록");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);

        header.add(Box.createHorizontalGlue());
        header.add(UIComponents.createSearchButton(mainFrame, this));

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        if (records == null || records.isEmpty()) {
            JComponent empty = UIComponents.createEmptyMessagePanel("아직 기록이 없습니다.");
            listPanel.add(Box.createVerticalStrut(250));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(empty);
        } else {
            // 위쪽 체중 변화 그래프 카드
            JPanel chartCard = createWeightChartCard();
            listPanel.add(chartCard);
            listPanel.add(Box.createVerticalStrut(24));

            // 데이터 불러와서 리스트 생성
            for (HealthRecord r : records) {
                JPanel card = createHealthCard(r);
                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(16));
            }
        }

        // 스크롤에 감싸기 + 항상 위에 붙게 래퍼 사용
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

    /* ================== 체중 그래프 카드 ================== */
    private JPanel createWeightChartCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        int cardHeight = 200;
        card.setPreferredSize(new Dimension(310, cardHeight));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardHeight));
        card.setMinimumSize(new Dimension(310, cardHeight));

        card.setBorder(new FlatLineBorder(
                new Insets(16, 16, 16, 16),
                UIConstants.GRAY_SOFT, 0.5f, 10
        ));

        JLabel title = new JLabel("체중 변화");
        title.setFont(UIConstants.FONT_SEMIBOLD_16);
        title.setForeground(UIConstants.TEXT_PRIMARY);

        card.add(title, BorderLayout.NORTH);

        ArrayList<HealthRecord> recent5 = new ArrayList<>(
        records.stream()
                .sorted(Comparator.comparing(HealthRecord::getDate).reversed())
                .limit(5)
                .toList()
        );
        double[] weights = recent5.stream()
                .mapToDouble(HealthRecord::getWeight)
                .toArray();
        for (int i = 0; i < weights.length / 2; i++) {
            double tmp = weights[i];
            weights[i] = weights[weights.length - 1 - i];
            weights[weights.length - 1 - i] = tmp;
        }

        String[] dateLabels = recent5.stream()
                .map(r -> r.getDate().toString().substring(5))
                .toArray(String[]::new);
        for (int i = 0; i < dateLabels.length / 2; i++) {
            String tmp = dateLabels[i];
            dateLabels[i] = dateLabels[dateLabels.length - 1 - i];
            dateLabels[dateLabels.length - 1 - i] = tmp;
        }

        WeightChartPanel chart = new WeightChartPanel(weights, dateLabels);
        chart.setOpaque(false);
        card.add(chart, BorderLayout.CENTER);

        return card;
    }

    /* 실제 라인 그래프 그리는 패널 */
    private static class WeightChartPanel extends JPanel {
        private final double[] weights;
        private final String[] labels;

        public WeightChartPanel(double[] weights, String[] labels) {
            this.weights = weights;
            this.labels = labels;
            setPreferredSize(new Dimension(310, 150));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (weights == null || weights.length == 0) return;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            int left = 30;
            int right = 15;
            int top = 15;
            int bottom = 18;

            int chartW = w - left - right;
            int chartH = h - top - bottom;
            int x0 = left;
            int y0 = top + chartH;

            // 배경
            g2.setColor(new Color(248, 248, 248));
            g2.fillRoundRect(left, top, chartW, chartH, 12, 12);

            // 최소/최대 값 먼저 계산
            double min = weights[0], max = weights[0];
            for (double v : weights) {
                min = Math.min(min, v);
                max = Math.max(max, v);
            }
            if (Math.abs(max - min) < 0.01) {
                max += 0.05;
                min -= 0.05;
            }
            double mid = (min + max) / 2.0;
            double[] lineValues = { min, mid, max };

            g2.setFont(g2.getFont().deriveFont(11f));
            FontMetrics fm = g2.getFontMetrics();
            Stroke oldStroke = g2.getStroke();
            Stroke dashed = new BasicStroke(
                    1f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10f,
                    new float[]{4f, 4f}, // 점선 패턴
                    0f
            );

            for (double value : lineValues) {
                double ratio = (value - min) / (max - min);
                int y = y0 - (int) (ratio * chartH);

                // 점선 보조 라인
                g2.setColor(new Color(210, 210, 210));
                g2.setStroke(dashed);
                g2.drawLine(left, y, left + chartW, y);

                // 숫자 라벨
                String text = String.format("%.2f", value);
                int strW = fm.stringWidth(text);
                int textX = left - strW - 6;
                int textY = y + fm.getAscent() / 2 - 2;

                g2.setStroke(oldStroke);
                g2.setColor(new Color(120, 120, 120));
                g2.drawString(text, textX, textY);
            }

            Path2D path = new Path2D.Double();
            int n = weights.length;
            for (int i = 0; i < n; i++) {
                double t = (double) i / (n - 1);
                int x = x0 + (int) (t * chartW);
                double ratio = (weights[i] - min) / (max - min);
                int y = y0 - (int) (ratio * chartH);

                if (i == 0) path.moveTo(x, y);
                else path.lineTo(x, y);
            }

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(UIConstants.PRIMARY);
            g2.draw(path);

            g2.setColor(UIConstants.PRIMARY);
            for (int i = 0; i < n; i++) {
                double t = (double) i / (n - 1);
                int x = x0 + (int) (t * chartW);
                double ratio = (weights[i] - min) / (max - min);
                int y = y0 - (int) (ratio * chartH);
                g2.fillOval(x - 3, y - 3, 6, 6);
            }

            g2.setFont(g2.getFont().deriveFont(10f));
            g2.setColor(new Color(120, 120, 120));
            for (int i = 0; i < n; i++) {
                double t = (double) i / (n - 1);
                int x = x0 + (int) (t * chartW);
                String label = labels != null && i < labels.length ? labels[i] : "";
                int strW = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, x - strW / 2, y0 + 16);
            }

            g2.dispose();
        }
    }

    /* ================== 건강 기록 카드 ================== */
    protected JPanel createHealthCard(HealthRecord r) {
        LocalDate date = r.getDate();
        int meal = r.getMeal();
        int water = r.getWater();
        double weight = r.getWeight();
        boolean isBrushed = r.getIsBrushed();
        String memo = r.getMemo();

        JPanel card = new JPanel(new BorderLayout());

        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        int cardHeight = 125;
        card.setPreferredSize(new Dimension(310, cardHeight));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardHeight));
        card.setMinimumSize(new Dimension(310, cardHeight));

        card.setBorder(new FlatLineBorder(
                new Insets(16, 16, 16, 16),
                UIConstants.GRAY_SOFT, 0.5f, 10
        ));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel dateLabel = new JLabel(date.toString());
        dateLabel.setFont(UIConstants.FONT_SEMIBOLD_14);
        dateLabel.setForeground(UIConstants.TEXT_PRIMARY);

        textPanel.add(dateLabel);
        textPanel.add(Box.createVerticalStrut(4));

        String line1 = "식사 " + meal +"회  |  음수량 " + water + "ml";
        JLabel label1 = new JLabel(line1);
        label1.setFont(UIConstants.FONT_REGULAR_14);
        label1.setForeground(UIConstants.TEXT_SECONDARY);

        textPanel.add(label1);
        textPanel.add(Box.createVerticalStrut(4));

        String line2 = "체중 " + weight + "kg  |  빗질 " +
                (isBrushed ? "O" : "X");
        JLabel label2 = new JLabel(line2);
        label2.setFont(UIConstants.FONT_REGULAR_14);
        label2.setForeground(UIConstants.TEXT_SECONDARY);

        textPanel.add(label2);

        if (memo != null && !memo.isBlank() && !memo.equals("0")) {
            JLabel memoLabel = new JLabel(memo);
            memoLabel.setFont(UIConstants.FONT_REGULAR_14);
            memoLabel.setForeground(UIConstants.TEXT_SECONDARY);
            textPanel.add(Box.createVerticalStrut(4));
            textPanel.add(memoLabel);
        }

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

        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }
}
