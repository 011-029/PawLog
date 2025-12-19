package ui;

import com.formdev.flatlaf.ui.FlatLineBorder;
import core.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class PetRegisterPanel extends Base {

    private final MainFrame mainFrame;
    private final User user;  // 로그인한 유저

    private JTextField nameField;
    private JTextField weightField;
    private JComboBox<String> speciesBox;
    private JComboBox<Integer> yearBox;
    private JComboBox<Integer> monthBox;
    private JComboBox<Integer> dayBox;
    private JTextField photoField;
    private JRadioButton female;
    private JRadioButton male;
    private JRadioButton yesBtn;
    private JRadioButton noBtn;

    public PetRegisterPanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.user = mainFrame.getLoggedInUser();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));
        contentWrapper.add(
                UIComponents.createHeader(() ->
                        mainFrame.switchPanel(new LoginPanel(mainFrame))
                ),
                BorderLayout.NORTH
        );
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
        JLabel title = new JLabel(user.getName() + "님 환영합니다!");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title2 = new JLabel("반려동물 정보를 등록해주세요.");
        title2.setFont(UIConstants.FONT_EXTRABOLD_24);
        title2.setForeground(UIConstants.TEXT_PRIMARY);
        title2.setAlignmentX(Component.LEFT_ALIGNMENT);

        listPanel.add(title);
        listPanel.add(Box.createVerticalStrut(6));
        listPanel.add(title2);
        listPanel.add(Box.createVerticalStrut(40));

        /* ==== 이름 ==== */
        listPanel.add(sectionLabel("반려동물 이름"));
        listPanel.add(Box.createVerticalStrut(8));
        nameField = textField("이름을 입력해주세요");
        listPanel.add(nameField);
        listPanel.add(Box.createVerticalStrut(20));

        /* ==== 종 선택 ==== */
        listPanel.add(sectionLabel("종 선택"));
        listPanel.add(Box.createVerticalStrut(8));
        listPanel.add(speciesPicker());
        listPanel.add(Box.createVerticalStrut(20));

        /* ==== 생일 ==== */
        listPanel.add(sectionLabel("생일"));
        listPanel.add(Box.createVerticalStrut(8));
        JComponent dateField = datePicker();
        listPanel.add(dateField);
        listPanel.add(Box.createVerticalStrut(20));

        /* ==== 성별 선택 ==== */
        listPanel.add(sectionLabel("성별"));
        listPanel.add(Box.createVerticalStrut(8));
        female = createRadioButton("암컷");
        male = createRadioButton("수컷");
        ButtonGroup group = new ButtonGroup();
        group.add(female);
        group.add(male);

        JPanel rbPanel = createRBPanel();
        rbPanel.add(female);
        rbPanel.add(male);
        listPanel.add(rbPanel);
        listPanel.add(Box.createVerticalStrut(20));

        /* ==== 중성화 여부 선택 ==== */
        listPanel.add(sectionLabel("중성화 여부"));
        listPanel.add(Box.createVerticalStrut(8));
        yesBtn = createRadioButton("예");
        noBtn = createRadioButton("아니오");
        ButtonGroup NeuterGroup = new ButtonGroup();
        NeuterGroup.add(yesBtn);
        NeuterGroup.add(noBtn);

        JPanel NeuterPanel = createRBPanel();
        NeuterPanel.add(yesBtn);
        NeuterPanel.add(noBtn);
        listPanel.add(NeuterPanel);
        listPanel.add(Box.createVerticalStrut(20));

        /* ==== 체중 ==== */
        listPanel.add(sectionLabel("체중"));
        listPanel.add(Box.createVerticalStrut(8));
        weightField = textField("체중(kg)을 입력하세요 (예: 3.5)");
        listPanel.add(weightField);
        listPanel.add(Box.createVerticalStrut(20));

        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);

        /* ==== 프로필 사진 ==== */
        listPanel.add(sectionLabel("프로필 사진 (선택)"));
        listPanel.add(Box.createVerticalStrut(8));

        JPanel photoRow = new JPanel();
        photoRow.setOpaque(false);
        photoRow.setLayout(new BoxLayout(photoRow, BoxLayout.X_AXIS));
        photoRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        photoField = textField("프로필 사진 경로");
        int fieldHeight = 40;
        JButton browseBtn = new JButton("파일선택");
        browseBtn.setFocusPainted(false);
        browseBtn.setPreferredSize(new Dimension(90, fieldHeight));
        browseBtn.setMinimumSize(new Dimension(90, fieldHeight));
        browseBtn.setMaximumSize(new Dimension(90, fieldHeight));
        browseBtn.putClientProperty("FlatLaf.style", "arc:16");
        browseBtn.setForeground(Color.WHITE);
        browseBtn.setBackground(UIConstants.PRIMARY_LIGHT);
        browseBtn.setMargin(new Insets(0, 0, 0, 0));
        browseBtn.addActionListener(e -> openFileChooser());

        photoRow.add(photoField);
        photoRow.add(Box.createHorizontalStrut(8));
        photoRow.add(browseBtn);

        listPanel.add(photoRow);

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(10);

        return scroll;
    }

    private JRadioButton createRadioButton(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(UIConstants.FONT_REGULAR_14);
        rb.setBorder(new EmptyBorder(0, 0, 0, 14));

        return rb;
    }

    private JPanel createRBPanel() {
        JPanel rbPanel = new JPanel();
        rbPanel.setOpaque(false);
        rbPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        rbPanel.setMinimumSize(new Dimension(0, 40));
        rbPanel.setLayout(new BoxLayout(rbPanel, BoxLayout.X_AXIS));
        rbPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rbPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        return rbPanel;
    }

    private JComponent speciesPicker() {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.X_AXIS));
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] species = { "강아지", "고양이" };
        speciesBox = new JComboBox<>(species);
        speciesBox.setPreferredSize(new Dimension(120, 40));
        speciesBox.setMaximumSize(new Dimension(120, 40));
        speciesBox.setMinimumSize(new Dimension(120, 40));
        speciesBox.setSelectedIndex(0);

        wrap.add(speciesBox);
        wrap.add(Box.createHorizontalGlue());

        return wrap;
    }

    private JComponent datePicker() {
        JPanel dateRow = new JPanel();
        dateRow.setOpaque(false);
        dateRow.setLayout(new BoxLayout(dateRow, BoxLayout.X_AXIS));
        dateRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 날짜 값 생성
        LocalDate today = LocalDate.now();
        int year = today.getYear();

        Integer[] years = java.util.stream.IntStream
                .rangeClosed(year - 25, year)
                .boxed()
                .toArray(Integer[]::new);
        Integer[] months = new Integer[12];
        for (int i = 0; i < 12; i++) months[i] = i + 1;
        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) days[i] = i + 1;

        // 컴포넌트 생성
        yearBox = new JComboBox<>(years);
        monthBox = new JComboBox<>(months);
        dayBox = new JComboBox<>(days);

        yearBox.setSelectedItem(year);
        monthBox.setSelectedItem(today.getMonthValue());
        dayBox.setSelectedItem(today.getDayOfMonth());

        // 크기
        Dimension comboSize = new Dimension(80, 40);
        yearBox.setPreferredSize(comboSize);
        monthBox.setPreferredSize(comboSize);
        dayBox.setPreferredSize(comboSize);

        dateRow.add(yearBox);
        dateRow.add(Box.createHorizontalStrut(8));
        dateRow.add(monthBox);
        dateRow.add(Box.createHorizontalStrut(8));
        dateRow.add(dayBox);

        yearBox.addActionListener(e -> refreshDays());
        monthBox.addActionListener(e -> refreshDays());

        return dateRow;
    }

    private void refreshDays() {
        int y = (Integer) yearBox.getSelectedItem();
        int m = (Integer) monthBox.getSelectedItem();
        int max = java.time.YearMonth.of(y, m).lengthOfMonth();

        Integer current = (Integer) dayBox.getSelectedItem();

        dayBox.removeAllItems();
        for (int d = 1; d <= max; d++) dayBox.addItem(d);

        dayBox.setSelectedItem(Math.min(current, max));
    }

    private LocalDate getSelectedBirthDate() {
        int y = (Integer) yearBox.getSelectedItem();
        int m = (Integer) monthBox.getSelectedItem();
        int d = (Integer) dayBox.getSelectedItem();

        try {
            return LocalDate.of(y, m, d);
        } catch (java.time.DateTimeException e) {
            // 유효하지 않은 날짜면 null
            return null;
        }
    }

    private JComponent sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_SEMIBOLD_16);
        label.setForeground(UIConstants.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    private JTextField textField(String placeHolder) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(100, 40));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setOpaque(false);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setFont(UIConstants.FONT_REGULAR_14);
        field.setForeground(UIConstants.TEXT_PRIMARY);
        field.setCaretColor(UIConstants.TEXT_PRIMARY);
        field.putClientProperty("JTextField.placeholderText", placeHolder);
        field.setBorder(new FlatLineBorder(
                new Insets(2, 10, 2, 10),
                UIConstants.GRAY_LIGHT,
                1.0f,
                16
        ));

        return field;
    }

    /* ================== 저장 버튼 바 ================== */
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
            String name = nameField.getText().trim();
            String species = (String) speciesBox.getSelectedItem();
            LocalDate birth = getSelectedBirthDate();
            String gender = female.isSelected() ? "암컷" : "수컷";
            boolean neutered = yesBtn.isSelected();
            String weightText = weightField.getText().trim();
            String photoPath = photoField.getText().trim();

            if (name.isBlank()) {
                JOptionPane.showMessageDialog(
                        this,
                        "이름을 입력해주세요.",
                        "등록 오류",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (birth == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "올바른 생일을 선택해주세요.",
                        "등록 오류",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (!female.isSelected() && !male.isSelected()) {
                JOptionPane.showMessageDialog(
                        this,
                        "성별을 선택해주세요.",
                        "등록 오류",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (!yesBtn.isSelected() && !noBtn.isSelected()) {
                JOptionPane.showMessageDialog(
                        this,
                        "중성화 여부를 선택해주세요.",
                        "등록 오류",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (weightText.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "체중을 입력해주세요.",
                        "등록 오류",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            double weight;
            try {
                weight = Double.parseDouble(weightText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "체중은 숫자로 입력해주세요. (예: 3.5)",
                        "등록 오류",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (weight <= 0.0) {
                JOptionPane.showMessageDialog(
                        this,
                        "체중은 0보다 큰 값으로 입력해주세요.",
                        "등록 오류",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String imagePath = "0";
            if (!photoPath.isBlank() && !(photoPath == null))
                imagePath = saveProfileImage(name, photoPath);

            // arr = { ownerId, name, species, gender, birthDate, weight, imagePath }
            String[] arr = {
                    user.getId(),
                    name,
                    species,
                    gender + ((neutered) ? "(중성화)" : ""),
                    String.valueOf(birth),
                    String.valueOf(weight),
                    imagePath
            };

            boolean result = petMgr.registerPet(arr);

            if (result) {
                JOptionPane.showMessageDialog(
                        this,
                        "반려동물 정보가 등록되었습니다.",
                        "등록 완료",
                        JOptionPane.INFORMATION_MESSAGE
                );
                mainFrame.setLoggedInUser(user);
                mainFrame.switchPanel(new HomePanel(mainFrame));
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "등록에 실패하였습니다",
                        "등록 실패",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        bar.add(saveBtn, BorderLayout.CENTER);
        return bar;
    }

    private String saveProfileImage(String name, String photoPath) {
        if (photoPath == null || photoPath.isBlank() || photoPath.equals("0")) {
            return null;
        }

        File source = new File(photoPath);
        if (!source.exists() || !source.isFile()) {
            return null;
        }

        try {
            // 저장 대상 폴더
            Path targetDir = Paths.get("src/main/resources/images/pet_profile");
            Files.createDirectories(targetDir);

            // 파일명 생성 (중복 방지)
            String ext = getExtension(source.getName());
            String fileName = user.getId() + "_" + name + "." + ext;

            Path target = targetDir.resolve(fileName);

            // 파일 복사
            Files.copy(source.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

            return "/images/pet_profile/" + fileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getExtension(String name) {
        int idx = name.lastIndexOf('.');
        return (idx > 0) ? name.substring(idx + 1) : "png";
    }


    private void openFileChooser() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            photoField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }
}
