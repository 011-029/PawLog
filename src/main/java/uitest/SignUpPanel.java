package uitest;

import core.User;
import core.UserMgr;
import util.PlaceholderPasswordField;
import util.PlaceholderTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SignUpPanel extends JPanel {
    private final MainFrame mainFrame;

    private JTextField idField;
    private JPasswordField pwField;
    private JTextField nameField;

    public SignUpPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 전체 중앙 영역 (양 옆 여백 + 가운데 정렬)
        JPanel centerWrapper = new JPanel();
        centerWrapper.setOpaque(false);
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.X_AXIS));
        centerWrapper.setBorder(new EmptyBorder(20, 50, 32, 50));

        // ⭐ 안쪽에 실제 콘텐츠를 담을 세로 컬럼
        JPanel contentColumn = new JPanel();
        contentColumn.setOpaque(false);
        contentColumn.setLayout(new BoxLayout(contentColumn, BoxLayout.Y_AXIS));
        contentColumn.setMaximumSize(new Dimension(480, Integer.MAX_VALUE)); // 전체 폭 제한
        contentColumn.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 위 여백
        contentColumn.add(Box.createVerticalStrut(110));

        // 환영 문구
        contentColumn.add(createWelcomeText());

        // 환영 문구와 폼 사이 간격
//        contentColumn.add(Box.createVerticalStrut(20));

        // ID/PW/버튼 폼
        JComponent form = createFormArea();
        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentColumn.add(form);

        // 아래 남는 공간은 전부 아래로
        contentColumn.add(Box.createVerticalGlue());

        // ⭐ 컬럼을 가운데에 두기 위해 좌우 glue
        centerWrapper.add(Box.createHorizontalGlue());
        centerWrapper.add(contentColumn);
        centerWrapper.add(Box.createHorizontalGlue());

        add(centerWrapper, BorderLayout.CENTER);
    }

    /* 발바닥 아이콘 영역 (가운데 정렬) */
    private JComponent createLogoArea() {
        // 그냥 라벨 하나로 충분!
        ImageIcon icon = new ImageIcon("logo.png");
        Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaled));
        return logo;
    }

    private JComponent createWelcomeText() {
        // 전체 폭을 쓰되, 글씨는 왼쪽에 붙게 할 래퍼 패널
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

        // 왼쪽에는 글씨, 오른쪽에는 여유 공간
        wrapper.add(text);
        wrapper.add(Box.createHorizontalGlue());

        return wrapper;
    }


    private JComponent createFormArea() {

        // ⭐ 진짜 폼이 들어갈 formPanel
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
//        checkBtn.setBorderPainted(false);
        // 안쪽 여백(패딩 느낌) 줄이기
        checkBtn.setMargin(new Insets(0, 0, 0, 0));  // top, left, bottom, right
        checkBtn.setFocusPainted(false);
        checkBtn.setPreferredSize(new Dimension(80, 45));
        checkBtn.setMaximumSize(new Dimension(80, 45));
        checkBtn.putClientProperty("FlatLaf.style", "arc:10; borderColor:#DDDDDD; borderWidth:1");
        checkBtn.setBackground(Color.WHITE);

        idRow.add(checkBtn);

        // formPanel에 추가
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

        /* --- 로그인 버튼 --- */
        JButton signUpBtn = new JButton("회원가입");
        signUpBtn.setFocusPainted(false);
        signUpBtn.setPreferredSize(new Dimension(360, 48));
        signUpBtn.setMaximumSize(new Dimension(360, 48));
        signUpBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        signUpBtn.setBackground(UIConstants.PRIMARY);
        signUpBtn.putClientProperty("FlatLaf.style", "arc:10");

        signUpBtn.addActionListener(e -> mainFrame.switchPanel(new LoginPanel(mainFrame)));
        form.add(signUpBtn);
        form.add(Box.createVerticalStrut(10));

        return form;
    }


    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow(); // 패널 자체에 포커스 요청 (패널은 포커스 불가 → 아무 곳에도 안 감)
    }

}
