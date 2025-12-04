package ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;
import core.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SearchPanel extends JPanel {
    private final MainFrame mainFrame;
//    private PlaceholderTextField searchField;
    private final JPanel prevPanel; // 이전 화면 (접근경로)
    private JTextField searchField;
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

        searchResultContainer = new JPanel(new BorderLayout());
        searchResultContainer.setOpaque(false);
        searchResultContainer.setBorder(new EmptyBorder(0, 0, 0, 0));
        searchResultContainer.setLayout(new BoxLayout(searchResultContainer, BoxLayout.Y_AXIS));

        listPanel.add(searchResultContainer, BorderLayout.CENTER);
        listPanel.add(searchResultContainer);



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

    private void searchMedicalRecord(String kwd) {
        MedicalRecordPanel mp = new MedicalRecordPanel(mainFrame);
        for (MedicalRecord r : mp.records) {
            if (r.matches(kwd)) {
                MedicalRecordPanel.MedicalCard card
                        = new MedicalRecordPanel.MedicalCard(r, mainFrame);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                searchResultContainer.add(card);
                searchResultContainer.add(Box.createVerticalStrut(16));
            }
        }
    }

    private void searchVaccineRecord(String kwd) {
        VaccineRecordPanel vp = new VaccineRecordPanel(mainFrame);
        for (VaccineRecord r : vp.records) {
            if (r.matches(kwd)) {
                VaccineRecordPanel.VaccineCard card
                        = new VaccineRecordPanel.VaccineCard(r, mainFrame);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                searchResultContainer.add(card);
                searchResultContainer.add(Box.createVerticalStrut(16));
            }
        }
    }

    private void searchMedicineRecord(String kwd) {
        MedicineRecordPanel mp = new MedicineRecordPanel(mainFrame);
        for (MedicineRecord r : mp.records) {
            if (r.matches(kwd)) {
                MedicineRecordPanel.MedicineCard card
                        = new MedicineRecordPanel.MedicineCard(r, mainFrame);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                searchResultContainer.add(card);
                searchResultContainer.add(Box.createVerticalStrut(16));
            }
        }
    }

    private void searchHealthRecord(String kwd) {
        HealthPanel hp = new HealthPanel(mainFrame);
        for (HealthRecord r : hp.records) {
            if (r.matches(kwd)) {
                JPanel card = hp.createHealthCard(r);
                searchResultContainer.add(card);
                searchResultContainer.add(Box.createVerticalStrut(16));
            }
        }
    }

    private void searchWalkRecord(String kwd) {
        WalkPanel wp = new WalkPanel(mainFrame);
        for (WalkRecord r : wp.records) {
            if (r.matches(kwd)) {
                JPanel card = wp.createWalkCard(r);
                searchResultContainer.add(card);
                searchResultContainer.add(Box.createVerticalStrut(16));
            }
        }
    }

    private void searchMedicineRoutine(String kwd) {
        MedicineRoutinePanel wp = new MedicineRoutinePanel(mainFrame);
        for (MedicineRoutine r : wp.records) {
            if (r.matches(kwd)) {
                JPanel card = wp.createRoutineCard(r);
                searchResultContainer.add(card);
                searchResultContainer.add(Box.createVerticalStrut(16));
            }
        }
    }

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

        searchField = new JTextField();
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setFont(UIConstants.FONT_REGULAR_14);
        searchField.setForeground(UIConstants.TEXT_PRIMARY);
        searchField.setCaretColor(UIConstants.TEXT_PRIMARY);
        searchField.setColumns(10);
        searchField.putClientProperty("JTextField.placeholderText", "검색어를 입력하세요");

        box.add(searchField, BorderLayout.CENTER);

        JButton searchBtn = new JButton();
        searchBtn.setIcon(new FlatSVGIcon("icons/search.svg", 20, 20));
        searchBtn.setContentAreaFilled(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setFocusPainted(false);
        searchBtn.setOpaque(false);
        searchBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchBtn.setPreferredSize(new Dimension(36, 36));

        searchField.addActionListener(e -> searchBtn.doClick());
        searchBtn.addActionListener(e -> {
            String kwd = searchField.getText();

            // 아무것도 입력 안하면 이전 화면으로 돌아감
            if (kwd.isBlank()) {
                mainFrame.switchPanel(prevPanel);

            } else {
                searchResultContainer.removeAll();

                if(prevPanel instanceof MedicalHomePanel){
                    searchMedicalRecord(kwd);
                    searchMedicineRecord(kwd);
                    searchVaccineRecord(kwd);
                }

                if (prevPanel instanceof MedicalRecordPanel) {
                    // TODO: 테스트용 코드 추후 삭제 (아래 1줄)
                    System.out.println("@ 메디컬레코드 검색 실행");
                    searchMedicalRecord(kwd);
                }
                if (prevPanel instanceof VaccineRecordPanel) {
                    // TODO: 테스트용 코드 추후 삭제 (아래 1줄)
                    System.out.println("@ 백신패널 검색 실행");
                    searchVaccineRecord(kwd);
                }
                if (prevPanel instanceof HealthPanel) {
                    // TODO: 테스트용 코드 추후 삭제 (아래 1줄)
                    System.out.println("@ 건강기록패널 검색 실행");
                    searchHealthRecord(kwd);
                }
                if (prevPanel instanceof WalkPanel) {
                    // TODO: 테스트용 코드 추후 삭제 (아래 1줄)
                    System.out.println("@ 산책패널 검색 실행");
                    searchWalkRecord(kwd);
                }
                if (prevPanel instanceof MedicineRecordPanel) {
                    // TODO: 테스트용 코드 추후 삭제 (아래 1줄)
                    System.out.println("@ 복용기록패널 검색 실행");
                    searchMedicineRecord(kwd);
                }
                if (prevPanel instanceof MedicineRoutinePanel) {
                    // TODO: 테스트용 코드 추후 삭제 (아래 1줄)
                    System.out.println("@ 복용기록패널 검색 실행");
                    searchMedicineRoutine(kwd);
                }
                // TODO: 놀이기록 검색 연결
            }
            searchResultContainer.revalidate();
            searchResultContainer.repaint();
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
