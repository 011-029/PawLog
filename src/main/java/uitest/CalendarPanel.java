package uitest;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;
import com.toedter.calendar.JCalendar;
import core.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DateFormatSymbols;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class CalendarPanel extends Base {

    private final MainFrame mainFrame;

    // 캘린더/라벨/리스트
    private JCalendar calendar;
    private JLabel dateLabel;
    private JLabel ddayLabel;
    private JPanel recordListPanel;

    // 날짜별 더미 기록 데이터
    private final Map<LocalDate, List<RecordItem>> recordsByDate = new HashMap<>();

    // 펫 생일 (예시)
    private static final LocalDate PET_BIRTHDAY = LocalDate.of(2020, 1, 10);

    private static final DateTimeFormatter HEADER_FORMAT =
            DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN);


    public CalendarPanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

//        loadDummyRecords(); // 예시 기록 데이터

        collectRecordsByDate();

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
        JPanel root = new JPanel();
        root.setOpaque(false);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(24, 10, 24, 10));

        // 제목 행 + D-Day
        JPanel titleRow = new JPanel();
        titleRow.setOpaque(false);
        titleRow.setLayout(new BoxLayout(titleRow, BoxLayout.X_AXIS));
        titleRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        dateLabel = new JLabel();
        dateLabel.setFont(UIConstants.FONT_EXTRABOLD_24);
        dateLabel.setForeground(UIConstants.TEXT_PRIMARY);

        ddayLabel = new JLabel("D-DAY");
        ddayLabel.setFont(UIConstants.FONT_SEMIBOLD_14);
        ddayLabel.setForeground(UIConstants.TEXT_SECONDARY);

        titleRow.add(dateLabel);
        titleRow.add(Box.createHorizontalGlue());
        titleRow.add(ddayLabel);

        // 캘린더 카드
        JPanel calendarCard = createCalendarCard();

        // 기록 리스트 패널
        recordListPanel = new JPanel();
        recordListPanel.setOpaque(false);
        recordListPanel.setLayout(new BoxLayout(recordListPanel, BoxLayout.Y_AXIS));
        recordListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        recordListPanel.setBorder(new EmptyBorder(24, 0, 0, 0));

        root.add(calendarCard);
        root.add(Box.createVerticalStrut(24));
        root.add(titleRow);
        root.add(recordListPanel);

        // 스크롤 + 위쪽 고정
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(root, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(10);

        // 초기 상태 업데이트
        LocalDate today = LocalDate.now();
        updateHeaderAndList(today);
        updateCalendarDots();

        return scroll;
    }

    /* ================== 캘린더 카드 ================== */
    private JPanel createCalendarCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        int cardHeight = 400;
        card.setPreferredSize(new Dimension(310, cardHeight));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardHeight));
        card.setMinimumSize(new Dimension(310, cardHeight));

        card.setBorder(new FlatLineBorder(
                new Insets(16, 16, 16, 16),
                UIConstants.GRAY_SOFT, 0.5f, 20
        ));

        // JCalendar 생성
        calendar = new JCalendar();

        // 기본 헤더 숨기기
        calendar.getMonthChooser().setVisible(false);
        calendar.getYearChooser().setVisible(false);

        // 배경/옵션
        calendar.setBackground(Color.WHITE);
        calendar.setOpaque(true);
        calendar.setWeekOfYearVisible(false);
        calendar.setDecorationBackgroundVisible(false);
        calendar.setDecorationBordersVisible(false);          // ← 추가!
        calendar.getDayChooser().setDayBordersVisible(false); // ← 이것도 있으면 좋아요

        // 요일/날짜 영역 여백 + 배경
        calendar.getDayChooser().setBorder(new EmptyBorder(20, 0, 0, 0));
        calendar.getDayChooser().setBackground(Color.WHITE);
        calendar.getDayChooser().getDayPanel().setBackground(Color.WHITE);
        calendar.getDayChooser().getDayPanel().setOpaque(true);

        // 날짜 버튼 스타일 + 패널 크기(정사각형 느낌)
        styleDayButtons();
        updateCalendarDots();
        updateSelectedDayHighlight();

        JPanel dayPanel = calendar.getDayChooser().getDayPanel();
        int cell = 32;
        int rows = 6;
        int cols = 7;
        Dimension panelSize = new Dimension(cell * cols, cell * rows);
        dayPanel.setPreferredSize(panelSize);
        dayPanel.setMinimumSize(panelSize);
        dayPanel.setMaximumSize(panelSize);

        // 상단 커스텀 헤더 + 캘린더 본체
        JComponent calendarHeader = createCalendarHeader();
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(calendarHeader, BorderLayout.NORTH);
        center.add(calendar, BorderLayout.CENTER);
        center.setBorder(new EmptyBorder(8, 0, 0, 0));
        card.add(center, BorderLayout.CENTER);

        // 날짜 선택 변경 리스너  🔧 수정 버전
        calendar.getDayChooser().addPropertyChangeListener("day", evt -> {
            SwingUtilities.invokeLater(() -> {
                LocalDate selected = toLocalDate(calendar.getDate());
                updateHeaderAndList(selected);   // 아래 카드/헤더 갱신
                updateCalendarDots();            // 점 다시 그림
                updateSelectedDayHighlight();    // 🔥 마지막에 우리 색 입히기
            });
        });

        // 월/년 변경 리스너도 색 다시 칠하도록
        calendar.getMonthChooser().addPropertyChangeListener("month", evt ->
                SwingUtilities.invokeLater(() -> {
                    updateCalendarDots();
                    updateSelectedDayHighlight();
                })
        );

        calendar.getYearChooser().addPropertyChangeListener("year", evt ->
                SwingUtilities.invokeLater(() -> {
                    updateCalendarDots();
                    updateSelectedDayHighlight();
                })
        );

        // calendar 전체 변경 시 (가끔 월/년 이동할 때 같이 호출됨)
        calendar.addPropertyChangeListener("calendar", evt -> {
            styleDayButtons();
            updateCalendarDots();
            updateSelectedDayHighlight();
        });


        // 초기 선택일 하이라이트
        updateSelectedDayHighlight();

        return card;
    }

    // 선택된 날짜 하이라이트 (DotIcon이랑 충돌 안 함)
    private void updateSelectedDayHighlight() {
        JPanel dayPanel = calendar.getDayChooser().getDayPanel();
        int selectedDay = calendar.getDayChooser().getDay(); // 현재 선택된 날짜 숫자

        for (Component comp : dayPanel.getComponents()) {
            if (!(comp instanceof JButton)) continue;
            JButton btn = (JButton) comp;

            int day;
            try {
                day = Integer.parseInt(btn.getText().trim());
            } catch (NumberFormatException e) {
                // 요일 헤더 같은 건 건너뛰기
                continue;
            }

            if (day == selectedDay) {
                // ✅ 선택된 날짜
                btn.setOpaque(true);
                btn.setContentAreaFilled(true);
                btn.setBackground(UIConstants.PRIMARY_LIGHT);
                btn.setForeground(Color.WHITE);
            } else {
                // 🔹 나머지 날짜
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBackground(Color.WHITE);
                btn.setForeground(UIConstants.TEXT_PRIMARY);
            }
        }
    }



    // 날짜 버튼 기본 스타일
    private void styleDayButtons() {
        JPanel dayPanel = calendar.getDayChooser().getDayPanel();

        for (Component comp : dayPanel.getComponents()) {
            if (!(comp instanceof JButton)) continue;
            JButton btn = (JButton) comp;

            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setHorizontalAlignment(SwingConstants.CENTER);
            btn.setFont(UIConstants.FONT_REGULAR_14);

            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBackground(Color.WHITE);
            btn.setForeground(UIConstants.TEXT_PRIMARY);
        }
    }

    // CalendarPanel 안에 추가
    private JComponent createCalendarHeader() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));

        // ◀ 이전 달 버튼
        JButton prevBtn = new JButton();
        prevBtn.setIcon(new FlatSVGIcon("icons/arrow-prev.svg", 12, 12));
        prevBtn.setFocusPainted(false);
        prevBtn.setContentAreaFilled(false);
        prevBtn.setBorderPainted(false);
        prevBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        prevBtn.setForeground(UIConstants.TEXT_SECONDARY);
        prevBtn.setFont(UIConstants.FONT_SEMIBOLD_14);

        // ▶ 다음 달 버튼
        JButton nextBtn = new JButton();
        nextBtn.setIcon(new FlatSVGIcon("icons/arrow-next.svg", 12, 12));
        nextBtn.setFocusPainted(false);
        nextBtn.setContentAreaFilled(false);
        nextBtn.setBorderPainted(false);
        nextBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextBtn.setForeground(UIConstants.TEXT_SECONDARY);
        nextBtn.setFont(UIConstants.FONT_SEMIBOLD_14);

        // 월 콤보박스 (영문 1~12월)
        String[] months = new DateFormatSymbols(Locale.ENGLISH).getMonths();
        String[] monthNames = Arrays.copyOf(months, 12); // 0~11만 사용
        JComboBox<String> monthCombo = new JComboBox<>(monthNames);
        monthCombo.setFont(UIConstants.FONT_REGULAR_14);

        // 년도 콤보박스 (예: 2020~2030)
        JComboBox<Integer> yearCombo = new JComboBox<>();
        for (int y = 2020; y <= 2030; y++) {
            yearCombo.addItem(y);
        }
        yearCombo.setFont(UIConstants.FONT_REGULAR_14);

        // 🔹 현재 JCalendar 상태와 동기화
        Calendar cal = calendar.getCalendar();
        monthCombo.setSelectedIndex(cal.get(Calendar.MONTH));
        yearCombo.setSelectedItem(cal.get(Calendar.YEAR));

        // 레이아웃 배치
        header.add(prevBtn);
        header.add(Box.createHorizontalStrut(8));
        header.add(monthCombo);
        header.add(Box.createHorizontalStrut(8));
        header.add(yearCombo);
        header.add(Box.createHorizontalStrut(8));
        header.add(nextBtn);

        // 🔹 버튼/콤보박스 클릭 시 JCalendar 갱신
        prevBtn.addActionListener(e -> {
            Calendar c = calendar.getCalendar();
            c.add(Calendar.MONTH, -1);
            calendar.setCalendar(c);

            monthCombo.setSelectedIndex(c.get(Calendar.MONTH));
            yearCombo.setSelectedItem(c.get(Calendar.YEAR));

            updateCalendarDots(); // 기록 점 다시 그리기
        });

        nextBtn.addActionListener(e -> {
            Calendar c = calendar.getCalendar();
            c.add(Calendar.MONTH, 1);
            calendar.setCalendar(c);

            monthCombo.setSelectedIndex(c.get(Calendar.MONTH));
            yearCombo.setSelectedItem(c.get(Calendar.YEAR));

            updateCalendarDots();
        });

        monthCombo.addActionListener(e -> {
            int m = monthCombo.getSelectedIndex();
            Calendar c = calendar.getCalendar();
            c.set(Calendar.MONTH, m);
            calendar.setCalendar(c);
            updateCalendarDots();
        });

        yearCombo.addActionListener(e -> {
            Integer y = (Integer) yearCombo.getSelectedItem();
            if (y == null) return;
            Calendar c = calendar.getCalendar();
            c.set(Calendar.YEAR, y);
            calendar.setCalendar(c);
            updateCalendarDots();
        });

        return header;
    }


    /* ================== 날짜/디데이/리스트 업데이트 ================== */
    private void updateHeaderAndList(LocalDate date) {
        // 상단 날짜 텍스트
        dateLabel.setText(date.format(HEADER_FORMAT));

        // 펫 생일 D-Day 계산
        LocalDate today = LocalDate.now();
        LocalDate nextBirthday = PET_BIRTHDAY.withYear(today.getYear());
        if (nextBirthday.isBefore(today)) {
            nextBirthday = nextBirthday.plusYears(1);
        }
        long diff = Duration.between(today.atStartOfDay(), nextBirthday.atStartOfDay()).toDays();

        String dText;
        if (diff == 0) dText = "🎂 D-DAY";
        else dText = "생일까지 D-" + diff;

        ddayLabel.setText(dText);

        // 기록 카드 갱신
        recordListPanel.removeAll();

        List<RecordItem> list = recordsByDate.getOrDefault(date, Collections.emptyList());
        if (list.isEmpty()) {
            JLabel empty = new JLabel("이 날짜에는 기록이 없어요");
            empty.setFont(UIConstants.FONT_REGULAR_14);
            empty.setForeground(UIConstants.TEXT_SECONDARY);
            empty.setBorder(new EmptyBorder(16, 4, 0, 0));
            recordListPanel.add(empty);
        } else {
            for (RecordItem item : list) {
                JPanel card = createRecordCard(item);
                recordListPanel.add(card);
                recordListPanel.add(Box.createVerticalStrut(12));
            }
        }

        recordListPanel.revalidate();
        recordListPanel.repaint();
    }

    /* ================== 달력 날짜 밑 점 표시 ================== */
    private void updateCalendarDots() {
        if (calendar == null) return;

        LocalDate shown = toLocalDate(calendar.getDate());
        int year = shown.getYear();
        int month = shown.getMonthValue();

        // 이번 달에 기록이 있는 날짜들
        Set<Integer> daysWithRecord = new HashSet<>();
        for (LocalDate d : recordsByDate.keySet()) {
            if (d.getYear() == year && d.getMonthValue() == month) {
                daysWithRecord.add(d.getDayOfMonth());
            }
        }

        JPanel dayPanel = calendar.getDayChooser().getDayPanel();

        for (Component comp : dayPanel.getComponents()) {
            if (!(comp instanceof JButton)) continue;
            JButton b = (JButton) comp;

            String txt = b.getText();
            int day;
            try {
                day = Integer.parseInt(txt.trim());
            } catch (NumberFormatException e) {
                // 요일 헤더 같은 건 건너뜀
                continue;
            }

            // 기본 상태
            b.setIcon(null);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setVerticalTextPosition(SwingConstants.CENTER);

            if (daysWithRecord.contains(day)) {
                // ● 아이콘 추가
                b.setIcon(new DotIcon(5, UIConstants.TEXT_PRIMARY));
                b.setHorizontalTextPosition(SwingConstants.CENTER);
                b.setVerticalTextPosition(SwingConstants.TOP);   // 숫자는 위, 점은 아래
            }
        }
    }

    /* ================== 기록 카드 ================== */
    private JPanel createRecordCard(RecordItem item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        int cardHeight = 100;
        card.setPreferredSize(new Dimension(310, cardHeight));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardHeight));
        card.setMinimumSize(new Dimension(310, cardHeight));

        card.setBorder(new FlatLineBorder(
                new Insets(16, 16, 16, 16),
                UIConstants.GRAY_SOFT, 0.5f, 20
        ));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(item.type + " · " + item.title);
        title.setFont(UIConstants.FONT_SEMIBOLD_14);
        title.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel detail = new JLabel(item.detail == null ? "" : item.detail);
        detail.setFont(UIConstants.FONT_REGULAR_12);
        detail.setForeground(UIConstants.TEXT_SECONDARY);

        JLabel date = new JLabel(item.date.toString());
        date.setFont(UIConstants.FONT_REGULAR_12);
        date.setForeground(Color.GRAY);

        card.add(title, BorderLayout.NORTH);
        card.add(detail, BorderLayout.CENTER);
        card.add(date, BorderLayout.SOUTH);

//        JLabel date = new JLabel(item.date.toString());
//        date.setFont(UIConstants.FONT_SEMIBOLD_14);
//        date.setForeground(UIConstants.TEXT_PRIMARY);
//
//        JLabel type = new JLabel(item.type);
//        type.setFont(UIConstants.FONT_REGULAR_14);
//        type.setForeground(UIConstants.TEXT_SECONDARY);
//
//        JLabel title = new JLabel(item.title);
//        title.setFont(UIConstants.FONT_SEMIBOLD_14);
//        title.setForeground(UIConstants.TEXT_PRIMARY);
//
//        JLabel detail = new JLabel(item.detail);
//        detail.setFont(UIConstants.FONT_SEMIBOLD_14);
//        detail.setForeground(UIConstants.TEXT_PRIMARY);
//
//        textPanel.add(date);
//        textPanel.add(Box.createVerticalStrut(4));
//        textPanel.add(type);
//        textPanel.add(Box.createVerticalStrut(4));
//        textPanel.add(title);
//        textPanel.add(Box.createVerticalStrut(4));
//        textPanel.add(detail);
//        textPanel.add(Box.createVerticalStrut(4));

//        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    /* ================== 유틸/더미 데이터 ================== */
    private LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

//    private void loadDummyRecords() {
//        LocalDate d1 = LocalDate.of(2025, 11, 29);
//        LocalDate d2 = LocalDate.of(2025, 11, 28);
//
//        recordsByDate.put(d1, Arrays.asList(
//                new RecordItem("산책 기록", "30분 · 1.2km"),
//                new RecordItem("건강 기록", "체중 6.2kg · 사료 잘 먹음")
//        ));
//
//        recordsByDate.put(d2, Collections.singletonList(
//                new RecordItem("약 복용", "심장사상충약 복용 완료")
//        ));
//
//        // 원하는 만큼 계속 추가 가능 ♡
//    }

    private void collectRecordsByDate() {

        recordsByDate.clear();

        LocalDate today = LocalDate.now();
        LocalDate minDate = today.minusDays(30);

        /* ─────────── 진료 기록 ─────────── */
        for (MedicalRecord r : medicalMgr.mList) {
            LocalDate d = r.getDate();
            if (d.isBefore(minDate)) continue;

            RecordItem item = new RecordItem(
                    d,
                    "진료",
                    r.getCategory(),
                    r.getHospital()
            );

            recordsByDate.computeIfAbsent(d, key -> new ArrayList<>())
                    .add(item);
        }

        /* ─────────── 복용 기록 ─────────── */
        for (MedicineRecord r : medicineRecordMgr.mList) {
            LocalDate d = r.getTakenDate();
            if (d.isBefore(minDate)) continue;

            RecordItem item = new RecordItem(
                    d,
                    "복용",
                    r.getMedicineName(),
                    r.getTakenTime() + " | " + r.getDosage() + "mg"
            );

            recordsByDate.computeIfAbsent(d, key -> new ArrayList<>())
                    .add(item);
        }

        /* ─────────── 백신 기록 ─────────── */
        for (VaccineRecord r : vaccineMgr.mList) {
            LocalDate d = r.getDate();
            if (d.isBefore(minDate)) continue;

            RecordItem item = new RecordItem(
                    d,
                    "백신",
                    r.getVaccine(),
                    r.getHospital()
            );

            recordsByDate.computeIfAbsent(d, key -> new ArrayList<>())
                    .add(item);
        }

        /* ─────────── 산책 기록 ─────────── */
        for (WalkRecord r : walkMgr.mList) {
            LocalDate d = r.getRecordDate();
            if (d.isBefore(minDate)) continue;

            RecordItem item = new RecordItem(
                    d,
                    "산책",
                    r.getWalkTime() + "분 산책",
                    r.getMemo() == null ? "" : r.getMemo()
            );

            recordsByDate.computeIfAbsent(d, key -> new ArrayList<>())
                    .add(item);
        }

        /* ─────────── 건강 기록 ─────────── */
        for (HealthRecord r : healthMgr.mList) {
            LocalDate d = r.getRecordDate();
            if (d.isBefore(minDate)) continue;

            RecordItem item = new RecordItem(
                    d,
                    "건강",
                    "몸무게: " + r.getWeight(),
                    r.getMemo()
            );

            recordsByDate.computeIfAbsent(d, key -> new ArrayList<>())
                    .add(item);
        }
    }

    /* ================== 타임라인 아이템 DTO ================== */
    private static class RecordItem {
        LocalDate date;
        String type;
        String title;
        String detail;

        RecordItem(LocalDate date, String type, String title, String detail) {
            this.date = date;
            this.type = type;
            this.title = title;
            this.detail = detail;
        }
    }

    // 날짜 아래에 작은 점을 그려주는 Border
    private static class DotBorder implements javax.swing.border.Border {
        private final Color color;

        DotBorder(Color color) {
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int d = 12;                  // 점 지름 (필요하면 10~12까지 키워도 됨!)
            int cx = x + width / 2;
            int cy = y + height - 6;    // 아래쪽에 배치

            g2.setColor(color);
            g2.fillOval(cx - d / 2, cy - d / 2, d, d);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            // 숫자 밑에 점이 들어갈 공간 확보
            return new Insets(0, 0, 12, 0);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    // 클래스 맨 아래쪽, RecordItem 아래 정도에 추가
    private static class DotIcon implements Icon {
        private final int size;
        private final Color color;

        DotIcon(int size, Color color) {
            this.size = size;
            this.color = UIConstants.PRIMARY;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillOval(x, y, size, size);
            g2.dispose();
        }
    }
}
