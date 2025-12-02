package uitest;

import core.*;
import util.DataLoader;

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

        DataLoader.loadAllData();
        setContentPane(new LoginPanel(this));
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
