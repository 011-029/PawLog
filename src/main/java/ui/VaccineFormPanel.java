package ui;

import core.Pet;
import core.User;
import uiutil.DatePickerPanel;
import uiutil.LabeledTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;

public class VaccineFormPanel extends Base {
    private final MainFrame mainFrame;
    private JTextArea memoArea;
    private DatePickerPanel dateField;
    private LabeledTextField vaccineField;
    private LabeledTextField hospitalField;


    private User user;
    private Pet pet;

    public VaccineFormPanel(MainFrame mainFrame){
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.user = mainFrame.getLoggedInUser();
        this.pet = mainFrame.getLoggedInUserPet();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단바 + 내용에만 패딩
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new AddRecordMenuPanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createFormContent(), BorderLayout.CENTER);
        contentWrapper.add(createSaveButtonBar(), BorderLayout.SOUTH);   // ⬅⬅ 추가!

        add(contentWrapper, BorderLayout.CENTER);
    }

    private JComponent createFormContent(){
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        /* ==== 제목 ==== */
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("백신 기록 추가");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);
        header.add(Box.createHorizontalGlue());

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        //날짜
        dateField = new DatePickerPanel();
        listPanel.add(dateField);

        //백신명
        vaccineField = new LabeledTextField("백신명", "예) 심장사상충");
        listPanel.add(vaccineField);

        //병원명
        hospitalField = new LabeledTextField("병원","예) 우끼우끼동물병원");
        listPanel.add(hospitalField);

        /* ==== 메모 ==== */
        JLabel memoLabel = new JLabel("메모");
        memoLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        memoLabel.setForeground(UIConstants.TEXT_PRIMARY);
        memoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(memoLabel);
        listPanel.add(Box.createVerticalStrut(8));

        memoArea = new JTextArea(4, 20);
        memoArea.setLineWrap(true);
        memoArea.setWrapStyleWord(true);
        memoArea.setFont(UIConstants.FONT_REGULAR_14);
//        memoArea.putClientProperty("FlatLaf.style", "arc:10");
//        memoArea.setBorder(new FlatLineBorder(new Insets(0, 0, 0, 0),
//                UIConstants.GRAY_LIGHT, 1f, 10));

        JScrollPane memoScroll = new JScrollPane(memoArea);
        memoScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        memoScroll.setPreferredSize(new Dimension(360, 130));
        memoScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        memoScroll.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(220, 220, 220), 1, true),
                        new EmptyBorder(8, 4, 8, 4)
                )
        );
        listPanel.add(memoScroll);
        listPanel.add(Box.createVerticalStrut(20));

        //래퍼 + 스크롤
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        //스크롤 속도 개선용
        scroll.getVerticalScrollBar().setUnitIncrement(20); // 숫자 커질수록 빠름
        listPanel.setDoubleBuffered(true);
        listWrapper.setDoubleBuffered(true);

        return scroll;
    }

    private JComponent createSaveButtonBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(0, 0, 15, 0));

        JButton saveBtn = new JButton("저장");
        saveBtn.setFocusPainted(false);
        saveBtn.setBackground(UIConstants.PRIMARY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.putClientProperty("FlatLaf.style", "arc:10");

        // 폭은 가로 전체를 차지하게 (반응형)
        saveBtn.setPreferredSize(new Dimension(0, 48));
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        saveBtn.addActionListener(e -> {
            try {
                // 1️⃣ 로그인 유저·펫 확인
                User user = mainFrame.getLoggedInUser();
                Pet pet = mainFrame.getLoggedInUserPet();

                if (user == null || pet == null) {
                    JOptionPane.showMessageDialog(mainFrame, "유저 또는 펫 정보가 없습니다.");
                    return;
                }

                // 2️⃣ 날짜 (필수)
                LocalDate date = dateField.getDate();
                if (date == null) {
                    JOptionPane.showMessageDialog(mainFrame, "날짜를 선택해주세요.");
                    return;
                }

                // 3️⃣ 백신명 (필수)
                String vaccine = vaccineField.getText().trim();
                if (vaccine.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "백신명을 입력해주세요.");
                    return;
                }

                // 4️⃣ 병원명 (필수)
                String hospital = hospitalField.getText().trim();
                if (hospital.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "병원명을 입력해주세요.");
                    return;
                }

                // 5️⃣ 메모 (선택 → 없으면 0)
                String memo = memoArea.getText().trim();
                if (memo.isEmpty()) memo = "0";

                // 6️⃣ 디버그 출력
                System.out.println("===== [VaccineForm DEBUG] =====");
                System.out.println("date = " + date);
                System.out.println("vaccine = " + vaccine);
                System.out.println("hospital = " + hospital);
                System.out.println("memo = " + memo);
                System.out.println("================================");

                // 7️⃣ 저장
                vaccineMgr.addNewRecord(
                        pet,
                        date,
                        vaccine,
                        hospital,
                        memo
                );

                // 8️⃣ 완료 메시지 + 메뉴로 이동
                JOptionPane.showMessageDialog(mainFrame, "백신 기록이 저장되었습니다!");
                mainFrame.switchPanel(new AddRecordMenuPanel(mainFrame));

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "저장 중 오류 발생: " + ex.getMessage(),
                        "오류",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        bar.add(saveBtn, BorderLayout.CENTER);
        return bar;
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
