package uitest;

import com.formdev.flatlaf.ui.FlatLineBorder;
import core.Pet;
import core.User;
import util.PlaceholderTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class WalkFormPanel extends Base {

    private final MainFrame mainFrame;

    // 입력 필드들 (날짜 필드는 클래스 만들음 (여러번 쓰여서))
    private DatePickerPanel dateField;
    private JSpinner timeSpinner;        // 산책 시간(분)
    private JTextField distanceField;    // 산책 거리(km 등)
    private JTextArea memoArea;          // 메모
    private JTextField photoField;       // 사진 경로

    private User user;
    private Pet pet;

    public WalkFormPanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.user = mainFrame.getLoggedInUser();
        this.pet = mainFrame.getLoggedInUserPet();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(UIComponents.createHeader(() ->
                mainFrame.switchPanel(new AddRecordMenuPanel(mainFrame))), BorderLayout.NORTH);
        contentWrapper.add(createFormContent(), BorderLayout.CENTER);
        contentWrapper.add(createSaveButtonBar(), BorderLayout.SOUTH);

        add(contentWrapper, BorderLayout.CENTER);
    }

    /* ================== 중앙 폼 ================== */
    private JComponent createFormContent() {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        /* ==== 제목 ==== */
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("산책 기록 추가");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);
        header.add(Box.createHorizontalGlue());

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        /* ==== 날짜 ==== */
        DatePickerPanel date = new DatePickerPanel();
        listPanel.add(date);

        /*날짜 가져오고 싶을때는
         * LocalDate selected = datePicker.getDate();
         *쓰세요
         */


        /*
         * 👉 나중에 JCalendar/JDateChooser 쓰고 싶으면
         * 위 날짜 콤보박스 부분을 통째로 지우고
         * dateRow에 JCalendar 컴포넌트만 add 해주면 돼요!
         */

        /* ==== 산책 시간(분) ==== */
        JLabel timeLabel = new JLabel("산책 시간 (분)");
        timeLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        timeLabel.setForeground(UIConstants.TEXT_PRIMARY);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(timeLabel);
        listPanel.add(Box.createVerticalStrut(8));

        timeSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 600, 5));
        JFormattedTextField tf = ((JSpinner.DefaultEditor) timeSpinner.getEditor()).getTextField();
        tf.setHorizontalAlignment(SwingConstants.LEFT);
        timeSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        timeSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        timeSpinner.putClientProperty("FlatLaf.style", "arc:10");
        timeSpinner.setBorder(new FlatLineBorder(new Insets(5, 0, 5, 0),
                UIConstants.GRAY_LIGHT, 1f, 10));

        JComponent editor = timeSpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor de) {
            de.getTextField().setBorder(
                    BorderFactory.createEmptyBorder(0, 8, 0, 8)
            );
        }

        listPanel.add(timeSpinner);
        listPanel.add(Box.createVerticalStrut(20));

        /* ==== 산책 거리 ==== */
        JLabel distLabel = new JLabel("산책 거리 (km)");
        distLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        distLabel.setForeground(UIConstants.TEXT_PRIMARY);
        distLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(distLabel);
        listPanel.add(Box.createVerticalStrut(8));

        distanceField = new PlaceholderTextField("예) 1.5");
        distanceField.setPreferredSize(new Dimension(360, 42));
        distanceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        distanceField.setAlignmentX(Component.LEFT_ALIGNMENT);
        distanceField.putClientProperty("FlatLaf.style", "arc:10");
        listPanel.add(distanceField);
        listPanel.add(Box.createVerticalStrut(20));

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

        /* ==== 사진 업로드 ==== */
        JLabel photoLabel = new JLabel("사진 업로드");
        photoLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        photoLabel.setForeground(UIConstants.TEXT_PRIMARY);
        photoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(photoLabel);
        listPanel.add(Box.createVerticalStrut(8));

        JPanel photoRow = new JPanel();
        photoRow.setOpaque(false);
        photoRow.setLayout(new BoxLayout(photoRow, BoxLayout.X_AXIS));
        photoRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        int fieldHeight = 42;

        photoField = new PlaceholderTextField("사진 파일 경로");
        photoField.setPreferredSize(new Dimension(260, fieldHeight));
        photoField.setMinimumSize(new Dimension(0, fieldHeight));
        photoField.setMaximumSize(new Dimension(Integer.MAX_VALUE, fieldHeight));
        photoField.putClientProperty("FlatLaf.style", "arc:10");

        JButton browseBtn = new JButton("파일선택");
        browseBtn.setFocusPainted(false);
        browseBtn.setPreferredSize(new Dimension(90, fieldHeight));
        browseBtn.setMinimumSize(new Dimension(90, fieldHeight));
        browseBtn.setMaximumSize(new Dimension(90, fieldHeight));
        browseBtn.putClientProperty("FlatLaf.style", "arc:10");
        browseBtn.setForeground(Color.WHITE);
        browseBtn.setBackground(UIConstants.PRIMARY_LIGHT);
        browseBtn.setMargin(new Insets(0, 0, 0, 0));  // 내부 여백 딱 맞추기
        browseBtn.addActionListener(e -> openFileChooser());

        photoRow.add(photoField);
        photoRow.add(Box.createHorizontalStrut(8));
        photoRow.add(browseBtn);

        listPanel.add(photoRow);
        listPanel.add(Box.createVerticalStrut(120));

        // 위쪽에 붙도록 래퍼 + 스크롤
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        return scroll;
    }

    /* ================== 하단 저장 버튼 ================== */
    private JComponent createSaveButtonBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(0, 0, 15, 0));

        JButton saveBtn = new JButton("저장");
        saveBtn.setFocusPainted(false);
        saveBtn.setBackground(UIConstants.PRIMARY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.putClientProperty("FlatLaf.style", "arc:10");

        saveBtn.setPreferredSize(new Dimension(0, 48));
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        saveBtn.addActionListener(e -> {
            try {
                User user = mainFrame.getLoggedInUser();
                Pet pet = mainFrame.getLoggedInUserPet();
                if (user == null || pet == null) {
                    JOptionPane.showMessageDialog(mainFrame, "유저 또는 펫 정보가 없습니다.");
                    return;
                }

                LocalDate date = dateField.getDate();

                int walkTime = (int) timeSpinner.getValue();

                // 거리 (선택사항 → 없으면 0)
                double distance = 0;
                if (!distanceField.getText().trim().isEmpty()) {
                    distance = Double.parseDouble(distanceField.getText().trim());
                }

                // 사진 저장
                String inputPath = photoField.getText().trim();
                String savedPath = savePhotoToLocal(inputPath);

                // 메모
                String memo = memoArea.getText().trim();
                if (memo.isEmpty()) memo = "0";

                walkMgr.addNewRecord(
                        pet,
                        date,
                        walkTime,
                        savedPath,
                        memo
                );

                JOptionPane.showMessageDialog(mainFrame, "산책 기록이 저장되었습니다!");
                mainFrame.switchPanel(new AddRecordMenuPanel(mainFrame));

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainFrame, "저장 중 오류: " + ex.getMessage());
            }
        });


        bar.add(saveBtn, BorderLayout.CENTER);
        return bar;
    }

    /* ================== 유틸 ================== */
    private void openFileChooser() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            photoField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private String savePhotoToLocal(String originalPath) {
        if (originalPath == null || originalPath.isEmpty())
            return "0";  // 사진 없는 경우

        try {
            File src = new File(originalPath);
            if (!src.exists()) return "0";

            // 저장 폴더 (없으면 자동 생성)
            File dir = new File("data/walk_photos");
            if (!dir.exists()) dir.mkdirs();

            // 저장될 파일 이름 => timestamp_원본이름
            String newFileName = System.currentTimeMillis() + "_" + src.getName();
            File dest = new File(dir, newFileName);

            // 파일 복사
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return dest.getPath();   // 실제 저장된 경로 문자열 반환

        } catch (Exception ex) {
            ex.printStackTrace();
            return "0";
        }
    }


    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}
