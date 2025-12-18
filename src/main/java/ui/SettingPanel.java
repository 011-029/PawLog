package ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettingPanel extends JPanel {

    private final MainFrame mainFrame;

    public SettingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 0, 16));

        contentWrapper.add(
                UIComponents.createHeader(() ->
                        mainFrame.switchPanel(new HomePanel(mainFrame))
                ),
                BorderLayout.NORTH
        );
        contentWrapper.add(createContent(), BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);

        add(UIComponents.createTabbedNav(mainFrame), BorderLayout.SOUTH);
    }

    /* 가운데 전체 영역 */
    private JComponent createContent() {
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(24, 10, 24, 10));

        // 제목
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("설정");
        title.setFont(UIConstants.FONT_EXTRABOLD_24);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        header.add(title);

        header.add(Box.createHorizontalGlue());

        listPanel.add(header);
        listPanel.add(Box.createVerticalStrut(24));

        // 섹션 라벨
        JLabel accountLabel = createSectionLabel("계정 설정");
        listPanel.add(accountLabel);
        listPanel.add(Box.createVerticalStrut(10));

        // 유저 정보 수정
        listPanel.add(createSettingItem(
                "유저 정보 수정",
                "아이디 / 이름 / 비밀번호 등을 수정해요",
                new FlatSVGIcon("icons/user.svg", 22, 22),
                () -> {
                    mainFrame.switchPanel(new UserInfoFormPanel(mainFrame));
                }
        ));
        listPanel.add(Box.createVerticalStrut(12));

        // 펫 정보 수정
        listPanel.add(createSettingItem(
                "펫 정보 수정",
                "등록된 반려동물 정보를 관리해요",
                new FlatSVGIcon("icons/paw2.svg", 20, 20),
                () -> {
                    // TODO: 펫 정보 수정 화면으로 전환
                    JOptionPane.showMessageDialog(
                            this,
                            "GUI 구현중 ㅎㅎ",
                            "펫 정보 수정",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
        ));
        listPanel.add(Box.createVerticalStrut(24));

        JLabel etcLabel = createSectionLabel("기타");
        listPanel.add(etcLabel);
        listPanel.add(Box.createVerticalStrut(10));

        // 회원탈퇴
        listPanel.add(createSettingItem(
                "회원탈퇴",
                "계정과 모든 데이터를 삭제해요",
                new FlatSVGIcon("icons/alert-triangle.svg", 22, 22),
                () -> {
                    int result = JOptionPane.showConfirmDialog(
                            this,
                            "정말 회원탈퇴 하시겠어요?\n삭제 후에는 복구할 수 없어요.",
                            "회원탈퇴",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.ERROR_MESSAGE
                    );
                    if (result == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(
                                this,
                                "안돼!!!!",
                                "회원탈퇴",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
        ));
        listPanel.add(Box.createVerticalStrut(12));

        // 로그아웃
        listPanel.add(createSettingItem(
                "로그아웃",
                "현재 계정에서 로그아웃해요",
                new FlatSVGIcon("icons/logout.svg", 18, 18),
                () -> {
                    int result = JOptionPane.showConfirmDialog(
                            this,
                            "로그아웃할까요?",
                            "로그아웃",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (result == JOptionPane.YES_OPTION) {
                        mainFrame.logout();
                        JOptionPane.showMessageDialog(
                                this,
                                "로그인 화면으로 돌아갑니다.",
                                "로그아웃",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        mainFrame.switchPanel(new LoginPanel(mainFrame));
                    }
                }
        ));
        listPanel.add(Box.createVerticalStrut(16));

        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(listPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(listWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(30);

        return scroll;
    }

    /* 섹션 라벨 */
    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_SEMIBOLD_18);
        label.setForeground(UIConstants.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 4, 4, 4));
        return label;
    }

    /* 설정 항목 카드 */
    private JPanel createSettingItem(String title,
                                     String desc,
                                     Icon leadingIcon,
                                     Runnable onClick) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setPreferredSize(new Dimension(310, 70));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setBorder(new FlatLineBorder(
                new Insets(12, 14, 12, 14),
                UIConstants.GRAY_SOFT,
                0.5f,
                10
        ));

        // 왼쪽 아이콘
        if (leadingIcon != null) {
            JLabel iconLabel = new JLabel(leadingIcon);
            iconLabel.setBorder(new EmptyBorder(0, 0, 0, 12));
            iconLabel.setOpaque(false);
            card.add(iconLabel, BorderLayout.WEST);
        }

        // 가운데 텍스트 영역
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_SEMIBOLD_14);
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(UIConstants.FONT_REGULAR_12);
        descLabel.setForeground(UIConstants.TEXT_SECONDARY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(descLabel);

        card.add(textPanel, BorderLayout.CENTER);

        // 오른쪽 화살표 아이콘 (옵션)
        JLabel arrow = new JLabel(new FlatSVGIcon("icons/arrow-next.svg", 18, 18));
        arrow.setOpaque(false);
        arrow.setBorder(new EmptyBorder(0, 8, 0, 0));
        card.add(arrow, BorderLayout.EAST);

        // 클릭 가능하게 처리
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 250, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }
}
