package ui;

import core.User;
import uiutil.PlaceholderPasswordField;
import uiutil.PlaceholderTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserInfoFormPanel extends Base {

    private final MainFrame mainFrame;
    private User user;  // 로그인한 유저

    private JTextField nameField;
    private JPasswordField pwField;
    private JPasswordField pwConfirmField;

    public UserInfoFormPanel(MainFrame mainFrame) {
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
                        mainFrame.switchPanel(new SettingPanel(mainFrame))
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
        JLabel title = new JLabel("유저 정보 수정");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);   // 요청대로 TEXT_PRIMARY
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        listPanel.add(title);
        listPanel.add(Box.createVerticalStrut(30));

        /* ==== 이름 ==== */
        JLabel nameLabel = new JLabel("이름");
        nameLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        nameLabel.setForeground(UIConstants.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(nameLabel);
        listPanel.add(Box.createVerticalStrut(8));

        nameField = new PlaceholderTextField("이름을 입력하세요");
        nameField.setPreferredSize(new Dimension(360, 45));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.putClientProperty("FlatLaf.style", "arc:10");
        nameField.setBorder(
                BorderFactory.createCompoundBorder(
                        nameField.getBorder(),
                        BorderFactory.createEmptyBorder(0, 4, 0, 4)
                )
        );
        listPanel.add(nameField);
        listPanel.add(Box.createVerticalStrut(20));

        /* ==== 비밀번호 ==== */
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        pwLabel.setForeground(UIConstants.TEXT_PRIMARY);
        pwLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(pwLabel);
        listPanel.add(Box.createVerticalStrut(8));

        pwField = new PlaceholderPasswordField("비밀번호를 입력하세요");
        pwField.setPreferredSize(new Dimension(360, 45));
        pwField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        pwField.setAlignmentX(Component.LEFT_ALIGNMENT);
        pwField.putClientProperty("FlatLaf.style", "arc:10");
        pwField.setBorder(
                BorderFactory.createCompoundBorder(
                        pwField.getBorder(),
                        BorderFactory.createEmptyBorder(0, 4, 0, 4)
                )
        );
        listPanel.add(pwField);
        listPanel.add(Box.createVerticalStrut(20));

        /* ==== 비밀번호 확인 ==== */
        JLabel pwConfirmLabel = new JLabel("비밀번호 확인");
        pwConfirmLabel.setFont(UIConstants.FONT_SEMIBOLD_16);
        pwConfirmLabel.setForeground(UIConstants.TEXT_PRIMARY);
        pwConfirmLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPanel.add(pwConfirmLabel);
        listPanel.add(Box.createVerticalStrut(8));

        pwConfirmField = new PlaceholderPasswordField("비밀번호를 다시 입력하세요");
        pwConfirmField.setPreferredSize(new Dimension(360, 45));
        pwConfirmField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        pwConfirmField.setAlignmentX(Component.LEFT_ALIGNMENT);
        pwConfirmField.putClientProperty("FlatLaf.style", "arc:10");
        pwConfirmField.setBorder(
                BorderFactory.createCompoundBorder(
                        pwConfirmField.getBorder(),
                        BorderFactory.createEmptyBorder(0, 4, 0, 4)
                )
        );
        listPanel.add(pwConfirmField);

        // 아래 공간 확보 (저장 버튼과 겹치지 않게)
        listPanel.add(Box.createVerticalStrut(180));

        // 항상 위쪽에 붙도록 래핑 + 스크롤
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
            String pw = new String(pwField.getPassword());
            String pw2 = new String(pwConfirmField.getPassword());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "이름을 입력해주세요.",
                        "유효성 검사",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            } else {
                user.setName(name);
            }

            if (!pw.isEmpty() || !pw2.isEmpty()) {
                if (!pw.equals(pw2)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "비밀번호가 일치하지 않습니다.",
                            "유효성 검사",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                } else {
                    user.setPassword(pw);
                }
            }

            userMgr.saveToFile(userMgr.getFilePath());

            JOptionPane.showMessageDialog(
                    this,
                    "유저 정보가 저장되었습니다.",
                    "저장 완료",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mainFrame.switchPanel(new SettingPanel(mainFrame));
        });

        bar.add(saveBtn, BorderLayout.CENTER);
        return bar;
    }
}
