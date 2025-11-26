package ui;

import core.Pet;
import core.PetMgr;
import core.User;

import javax.swing.*;

public class MainFrame extends JFrame {
    private User loggedInUser;
    private Pet loggedInUserPet;

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        // 펫 설정
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

    public MainFrame() {
        setTitle("Pet Manager");
        setSize(420, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new LoginPanel());
        setVisible(true);
    }

    public void switchPanel(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }
}
