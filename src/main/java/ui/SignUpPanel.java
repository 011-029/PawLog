package ui;

import uiutil.PlaceholderPasswordField;
import uiutil.PlaceholderTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SignUpPanel extends Base {
    private final MainFrame mainFrame;

    private JTextField idField;
    private JPasswordField pwField;
    private JTextField nameField;
    private boolean idCheck = false;

    public SignUpPanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setOpaque(false);
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.X_AXIS));
        centerWrapper.setBorder(new EmptyBorder(20, 50, 32, 50));

        JPanel contentColumn = new JPanel();
        contentColumn.setOpaque(false);
        contentColumn.setLayout(new BoxLayout(contentColumn, BoxLayout.Y_AXIS));
        contentColumn.setMaximumSize(new Dimension(480, Integer.MAX_VALUE)); // 전체 폭 제한
        contentColumn.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentColumn.add(Box.createVerticalStrut(110));

        // 환영 문구
        contentColumn.add(createWelcomeText());

        // ID/PW/버튼 폼
        JComponent form = createFormArea();
        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentColumn.add(form);

        contentColumn.add(Box.createVerticalGlue());

        centerWrapper.add(Box.createHorizontalGlue());
        centerWrapper.add(contentColumn);
        centerWrapper.add(Box.createHorizontalGlue());

        add(centerWrapper, BorderLayout.CENTER);
    }

    private JComponent createWelcomeText() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea text = new JTextArea("Paw Log에\n오신 것을\n환영합니다");
        text.setMargin(new Insets(0, 0, 0, 0));
        text.setPreferredSize(new Dimension(
                360,
                text.getPreferredSize().height - 50
        ));
        text.setFont(UIConstants.FONT_BOLD_32);
        text.setForeground(UIConstants.TEXT_PRIMARY);
        text.setOpaque(false);
        text.setEditable(false);
        text.setFocusable(false);
        text.setHighlighter(null);
        text.setBorder(null);

        wrapper.add(text);
        wrapper.add(Box.createHorizontalGlue());

        return wrapper;
    }

    private JComponent createFormArea() {

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        /* ===================== ID 입력 + 중복확인 버튼 ===================== */
        JPanel idRow = new JPanel();
        idRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        idRow.setMinimumSize(new Dimension(0, 45));
        idRow.setLayout(new BoxLayout(idRow, BoxLayout.X_AXIS));
        idRow.setOpaque(false);
        idRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ID 필드
        idField = new PlaceholderTextField("아이디를 입력하세요");
        idField.setPreferredSize(new Dimension(280, 45));
        idField.setMaximumSize(new Dimension(280, 45));
        idField.putClientProperty("FlatLaf.style", "arc:10");
        idField.setBorder(
                BorderFactory.createCompoundBorder(
                        idField.getBorder(),
                        BorderFactory.createEmptyBorder(0, 8, 0, 8)
                )
        );
        idRow.add(idField);
        idRow.add(Box.createHorizontalStrut(5));

        // 중복확인 버튼
        JButton checkBtn = new JButton("중복확인");
        checkBtn.putClientProperty("FlatLaf.style", "arc:10");
        checkBtn.setBackground(UIConstants.PRIMARY_LIGHT);
        checkBtn.setForeground(UIConstants.TEXT_PRIMARY);
        checkBtn.setOpaque(true);
        checkBtn.setMargin(new Insets(0, 0, 0, 0));
        checkBtn.setFocusPainted(false);
        checkBtn.setPreferredSize(new Dimension(80, 45));
        checkBtn.setMaximumSize(new Dimension(80, 45));
        checkBtn.putClientProperty("FlatLaf.style", "arc:10; borderColor:#DDDDDD; borderWidth:1");
        checkBtn.setBackground(Color.WHITE);
        checkBtn.addActionListener(e -> doDuplicatedCheck());

        idRow.add(checkBtn);

        form.add(idRow);
        form.add(Box.createVerticalStrut(5));

        /* --- PW 입력창 --- */
        pwField = new PlaceholderPasswordField("비밀번호를 입력하세요");
        pwField.setPreferredSize(new Dimension(360, 45));
        pwField.setMaximumSize(new Dimension(360, 45));
        pwField.setAlignmentX(Component.LEFT_ALIGNMENT);
        pwField.putClientProperty("FlatLaf.style", "arc:10");
        pwField.setBorder(
                BorderFactory.createCompoundBorder(
                        pwField.getBorder(),
                        BorderFactory.createEmptyBorder(0, 5, 0, 5)
                )
        );
        form.add(pwField);
        form.add(Box.createVerticalStrut(5));

        /* --- 이름 입력창 --- */
        nameField = new PlaceholderTextField("이름을 입력하세요");
        nameField.setPreferredSize(new Dimension(360, 45));
        nameField.setMaximumSize(new Dimension(360, 45));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.putClientProperty("FlatLaf.style", "arc:10");
        nameField.setBorder(
                BorderFactory.createCompoundBorder(
                        nameField.getBorder(),
                        BorderFactory.createEmptyBorder(0, 5, 0, 5)
                )
        );
        form.add(nameField);
        form.add(Box.createVerticalStrut(20));

        /* --- 회원가입 버튼 --- */
        JButton signUpBtn = new JButton("회원가입");
        signUpBtn.setFocusPainted(false);
        signUpBtn.setPreferredSize(new Dimension(360, 48));
        signUpBtn.setMaximumSize(new Dimension(360, 48));
        signUpBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        signUpBtn.setBackground(UIConstants.PRIMARY);
        signUpBtn.putClientProperty("FlatLaf.style", "arc:10");
        signUpBtn.addActionListener(e -> doSignUp());

        form.add(signUpBtn);
        form.add(Box.createVerticalStrut(10));

        return form;
    }

    private void doSignUp() {
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword()).trim();
        String name = nameField.getText();

        if(id.isBlank()) {
            JOptionPane.showMessageDialog(this, "아이디를 입력하세요.");
            return;
        }
        if (pw.isBlank()) {
            JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요.");
            return;
        }
        if (name.isBlank()) {
            JOptionPane.showMessageDialog(this, "이름을 입력하세요.");
            return;
        }

        if (!idCheck) {
            JOptionPane.showMessageDialog(this,
                    "아이디 중복 확인을 해주세요.",
                    "회원가입 실패",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean result = userMgr.signUp(id, pw, name);

        if (result) {
            JOptionPane.showMessageDialog(this,
                    "회원가입 성공!\n"
                    + name +"님 환영합니다.");
            mainFrame.switchPanel(new LoginPanel(mainFrame));
        } else {
            JOptionPane.showMessageDialog(this,
                    "회원가입에 실패하였습니다.",
                    "회원가입 실패",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doDuplicatedCheck() {
        String id = idField.getText().trim();
        boolean result = userMgr.isDuplicatedId(id);
        if (id.isBlank()) {
            JOptionPane.showConfirmDialog(this,
                    "아이디를 입력해 주세요.",
                    "중복 확인",
                    JOptionPane.DEFAULT_OPTION);
            return;
        }
        if (!result) {
            JOptionPane.showMessageDialog(this,
                    "사용 가능한 아이디입니다.",
                    "중복 확인",
                    JOptionPane.INFORMATION_MESSAGE);
            idCheck = true;
        } else {
            JOptionPane.showMessageDialog(this,
                    "중복된 아이디입니다.",
                    "중복 확인",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }
}
