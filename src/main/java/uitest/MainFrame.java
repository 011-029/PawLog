package uitest;

import core.Pet;
import core.PetMgr;
import core.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private User loggedInUser;
    private Pet loggedInUserPet;

    public MainFrame() {
        setTitle("Paw Log");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 830);
        setMinimumSize(new Dimension(400, 700));
        setLocationRelativeTo(null);

        // 처음에는 로그인 화면부터
        setContentPane(new LoginPanel(this));

        // TODO: 나중에 로그인 붙이면
        //  1) setLoggedInUser(...) 먼저 호출하고
        //  2) new PetHomePanel(this)로 바꾸면 됨

        // 지금은 테스트용으로 로그인 없이 바로 홈 화면
//        setContentPane(new PetHomePanel(this));
    }

    /* ===== 로그인 유저/펫 관리 ===== */
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;

        // 펫 설정 (ownerId로 1마리 가져온다고 가정)
        PetMgr petMgr = PetMgr.getInstance();
        this.loggedInUserPet = petMgr.getPetByOwner(user.getId());
    }

    public void logout() {
        this.loggedInUser = null;
        this.loggedInUserPet = null;
        // 나중에: switchPanel(new LoginPanel(this)); 이런 식으로 처리 가능
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public Pet getLoggedInUserPet() {
        return loggedInUserPet;
    }

    /* ===== 화면 전환 ===== */

    public void switchPanel(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }
}
