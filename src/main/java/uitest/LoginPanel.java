package uitest;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import core.User;
import util.DataLoader;
import util.PlaceholderPasswordField;
import util.PlaceholderTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends Base {
    private final MainFrame mainFrame;

    private JTextField idField;
    private JPasswordField pwField;

    public LoginPanel(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 전체 중앙 영역 (위쪽 여백 + 가운데 정렬)
        JPanel centerWrapper = new JPanel();
        centerWrapper.setOpaque(false);
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBorder(new EmptyBorder(20, 50, 32, 50));

        // 위 여백 조금
        centerWrapper.add(Box.createVerticalStrut(80));

        // 로고
        JComponent logo = createLogoArea();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerWrapper.add(logo);

        // 로고와 폼 사이 간격
        centerWrapper.add(Box.createVerticalStrut(70));

        // ID/PW/버튼 폼 (가로 폭 고정 + 가운데 정렬)
        JComponent form = createFormArea();
        form.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerWrapper.add(form);

        // 아래 남는 공간은 전부 아래로
        centerWrapper.add(Box.createVerticalGlue());

        add(centerWrapper, BorderLayout.CENTER);
    }

    /* 발바닥 아이콘 영역 (가운데 정렬) */
    private JComponent createLogoArea() {
        JLabel logo = new JLabel("Paw Log");
        logo.setIcon(new FlatSVGIcon("icons/paw.svg", 80, 80));
        logo.setHorizontalTextPosition(SwingConstants.CENTER); // 텍스트를 가운데
        logo.setVerticalTextPosition(SwingConstants.BOTTOM);   // 텍스트를 아래로
        logo.setIconTextGap(12);
        logo.setFont(UIConstants.FONT_BOLD_32);
        logo.setForeground(UIConstants.TEXT_PRIMARY);
        return logo;
    }

    private JComponent createFormArea() {

        // ⭐ form을 감싸는 wrapper — 중앙정렬 유지
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));

        // ⭐ 왼쪽·오른쪽 여백 확보
        wrapper.add(Box.createHorizontalGlue());

        // ⭐ 진짜 폼이 들어갈 formPanel (왼쪽 정렬 유지 가능)
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);  // 이게 핵심!

        /* --- ID 입력창 (가운데 정렬) --- */
        idField = new PlaceholderTextField("아이디 입력");
        idField.setPreferredSize(new Dimension(360, 45));
        idField.setMaximumSize(new Dimension(360, 45));
        idField.setAlignmentX(Component.LEFT_ALIGNMENT);
        idField.putClientProperty("FlatLaf.style", "arc:10");
        idField.setBorder(
                BorderFactory.createCompoundBorder(
                        idField.getBorder(),
                        BorderFactory.createEmptyBorder(0, 5, 0, 5)  // top, left, bottom, right
                )
        );
        form.add(idField);

        form.add(Box.createVerticalStrut(5));

        /* --- PW 입력창 --- */
        pwField = new PlaceholderPasswordField("비밀번호 입력");
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

        form.add(Box.createVerticalStrut(30));

        /* --- 로그인 버튼 (둥근 버튼) --- */
        JButton loginBtn = new JButton("로그인");
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(360, 48));
        loginBtn.setMaximumSize(new Dimension(360, 48));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setBackground(UIConstants.PRIMARY);
        loginBtn.setForeground(UIConstants.TEXT_WHITE);
        loginBtn.setFont(UIConstants.FONT_BOLD_14);
        pwField.addActionListener(e -> loginBtn.doClick());
        loginBtn.putClientProperty("FlatLaf.style", "arc:10");
        loginBtn.addActionListener(e -> doLogin());
        form.add(loginBtn);

        form.add(Box.createVerticalStrut(10));

        /* --- 회원가입 버튼 --- */
        JButton signUpBtn = new JButton("회원가입");
        signUpBtn.setForeground(UIConstants.TEXT_SECONDARY);
        signUpBtn.setFocusPainted(false);
        signUpBtn.setPreferredSize(new Dimension(360, 20));
        signUpBtn.setMaximumSize(new Dimension(360, 20));
        signUpBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        signUpBtn.setBackground(Color.WHITE);
        signUpBtn.setBorder(BorderFactory.createEmptyBorder());
        signUpBtn.setFont(UIConstants.FONT_REGULAR_12);

        signUpBtn.addActionListener(e -> mainFrame.switchPanel(new SignUpPanel(mainFrame)));
        form.add(signUpBtn);

        // ⭐ formPanel을 wrapper 중앙에 배치
        wrapper.add(form);
        wrapper.add(Box.createHorizontalGlue());

        return wrapper;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow(); // 패널 자체에 포커스 요청 (패널은 포커스 불가 → 아무 곳에도 안 감)
    }

    /* 로그인 로직 */
    private void doLogin() {
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword());

        User user = userMgr.login(id, pw);

        if(id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디를 입력하세요.");
            return;
        }
        if (pw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요.");
            return;
        }

        if (user == null) {
            JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호를 확인해주세요.");
            idField.setText("");
            pwField.setText("");
            idField.requestFocus();
        } else {
            mainFrame.setLoggedInUser(user);
            mainFrame.switchPanel(new PetHomePanel(mainFrame));
            DataLoader.loadAllData();
            // TODO: 아래 테스트용 코드 추후 삭제 (1줄)
            System.out.println("로그인 ID: " + user.getId());
        }
    }
}
