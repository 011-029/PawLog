package uitest;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;
import core.MedicineRoutine;
import core.Pet;
import core.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class MedicineRoutinePanel extends Base {

    private final MainFrame mainFrame;
    private User user;
    private Pet pet;
    private ArrayList<MedicineRoutine> records;

    public MedicineRoutinePanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.user = mainFrame.getLoggedInUser();
        this.pet = mainFrame.getLoggedInUserPet();
        this.records = medicineRoutineMgr.getAllByOwner(user);

        // TODO: 아래 테스트용 코드 추후 삭제 (2줄)
        System.out.println("루틴패널 ID: " + user.getId());
        System.out.println("루틴패널 펫: " + pet.getName());

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

    /** 가운데 전체 영역 */
    private JComponent createContent() {
        // 스크롤 가능 리스트로 만들기
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));
        listPanel.setAlignmentY(0f);

        // 헤더: 제목 + 검색버튼 + 필터버튼
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 제목
        JLabel title = new JLabel("복용 루틴");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);

        // 필터 버튼
        JButton filterBtn = new JButton();
        filterBtn.setIcon(new FlatSVGIcon("icons/filter.svg", 22, 22));
        filterBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        filterBtn.setContentAreaFilled(false);
        filterBtn.setBorderPainted(false);
        filterBtn.setFocusPainted(false);
        filterBtn.setOpaque(false);
        filterBtn.setPreferredSize(new Dimension(32, 32));

        header.add(Box.createHorizontalGlue());
        header.add(filterBtn);
        header.add(Box.createHorizontalStrut(8));
        header.add(UIComponents.createSearchButton(mainFrame, this));

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        // 오늘 복용 루틴 섹션 -----------------------
        JLabel todayLabel = createSectionLabel("오늘 복용 루틴");
        listPanel.add(todayLabel);
        listPanel.add(Box.createVerticalStrut(10));

        String todayDOW = LocalDate.now()
                .getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        // 데이터 불러와서 카드 리스트 생성
        for (MedicineRoutine m : records) {
            String medicineName = m.getMedicineName();
            ArrayList<String> takenDOW = m.getTakenDOW();
            String takenDOWString;
            if (takenDOW.size() == 7) {
                takenDOWString = "매일";
            } else {
                takenDOWString = takenDOW.stream()
                        .map(d -> d + "요일")
                        .collect(Collectors.joining(", "));
            }
            String dosage = String.format("%dmg", m.getDosage());
            boolean isTaken = m.getIsTaken();
            if (takenDOW.contains(todayDOW)) {
                JPanel card = createRoutineCard(
                        medicineName,
                        takenDOWString,
                        dosage,
                        isTaken,
                        m
                );
                listPanel.add(card);
                listPanel.add(Box.createVerticalStrut(16));
            }
        }

        // 전체 루틴 섹션 -----------------------
        listPanel.add(Box.createVerticalStrut(8));
        JLabel allLabel = createSectionLabel("전체 루틴");
        listPanel.add(allLabel);
        listPanel.add(Box.createVerticalStrut(10));

        // 데이터 불러와서 카드 리스트 생성
        for (MedicineRoutine m : records) {
            String medicineName = m.getMedicineName();
            ArrayList<String> takenDOW = m.getTakenDOW();
            String takenDOWString;
            if (takenDOW.size() == 7) {
                takenDOWString = "매일";
            } else {
                takenDOWString = takenDOW.stream()
                        .map(d -> d + "요일")
                        .collect(Collectors.joining(", "));
            }
            String dosage = String.format("%dmg", m.getDosage());
            boolean isTaken = m.getIsTaken();

            JPanel card = createRoutineCard(
                    medicineName,
                    takenDOWString,
                    dosage,
                    isTaken,
                    m
            );
            listPanel.add(card);
            listPanel.add(Box.createVerticalStrut(16));
        }

        // 리스트를 한 번 더 싸서 항상 위쪽에 붙도록
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);

        // 스크롤 옵션: 스크롤바 X, 속도 30
        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(30);

        return scroll;
    }

    /** 오늘 복용 루틴 / 전체 루틴 섹션 제목 라벨 */
    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_SEMIBOLD_18);
        label.setForeground(UIConstants.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 4, 4, 4));
        return label;
    }

    /** 개별 복용 루틴 카드 */
    private JPanel createRoutineCard(String title, String desc, String timeText,
                                     boolean isTaken, MedicineRoutine m) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setPreferredSize(new Dimension(310, 105));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 105));
        card.setBorder(new FlatLineBorder(new Insets(16, 16, 16, 16),
                UIConstants.GRAY_SOFT, 0.5f, 10));

        // 텍스트 영역
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_SEMIBOLD_14);
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(UIConstants.FONT_REGULAR_14);
        descLabel.setForeground(UIConstants.TEXT_SECONDARY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(descLabel);

        if (!timeText.isEmpty()) {
            JLabel timeLabel = new JLabel(timeText);
            timeLabel.setFont(UIConstants.FONT_REGULAR_14);
            timeLabel.setForeground(UIConstants.TEXT_SECONDARY);
            textPanel.add(Box.createVerticalStrut(4));
            textPanel.add(timeLabel);
        }

        card.add(textPanel, BorderLayout.CENTER);

        // 복용 체크 체크박스
        JCheckBox checkBox = new JCheckBox();
        checkBox.setOpaque(false);
        checkBox.setFocusPainted(false);
        checkBox.setBorderPainted(false);
        checkBox.setContentAreaFilled(false);
        checkBox.putClientProperty("JCheckBox[styleClass]", "round");
        checkBox.setSelected(isTaken);

        // 루틴 체크박스 체크/해제 시 복용기록 생성/삭제
        checkBox.addItemListener(e -> {
            boolean newCheck = checkBox.isSelected();
            m.toggleTaken();
            System.out.println(m.getIsTaken());

            if (!newCheck) {
                medicineRecordMgr.removeIfUnChecked(m);
            }
        });

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(Box.createVerticalGlue());
        right.add(checkBox);
        right.add(Box.createVerticalGlue());
        right.setBorder(new EmptyBorder(0, 8, 0, 0)); // 카드 오른쪽 여백 조금만

        card.add(right, BorderLayout.EAST);

        return card;
    }
}
