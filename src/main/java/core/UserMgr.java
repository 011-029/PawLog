package core;

import mgr.Factory;
import mgr.Manager;

public class UserMgr extends Manager<User> {
    private static UserMgr mgr = null;
    private static final String FILE_PATH = "data/users.txt";

    public static UserMgr getInstance() {
        if (mgr == null)
            mgr = new UserMgr();
        return mgr;
    }

    public User findUserById(String id) {
        for (User u : mList) {
            if (u.getId().equals(id))
                return u;
        }
        return null;
    }

    public boolean isDuplicatedId(String id) {
        for (User u : mList) {
            if (u.getId().equals(id))
                return true;
        }
        return false;
    }

    public boolean signUp(String id, String pw, String name) {
        if (isDuplicatedId(id)) return false;
        else {
            User u = new User();
            u.apply(id, pw, name);
            mList.add(u);
            saveToFile(FILE_PATH);
        }
        return true;
    }

    public User login(String id, String pw) {
        for (User u : mList) {
            if (u.getId().equals(id) && u.checkPassword(pw))
                return u;
        }
        return null;
    }

    public boolean deleteUser(String id) {
        User u = findUserById(id);
        if (u == null)
            return false;

        mList.remove(u);
        saveToFile(FILE_PATH);
        return true;
    }

    public void loadFromFile() {
        readAll(FILE_PATH, new Factory<User>() {
            public User create() {
                return new User();
            }
        });
    }

    public String getFilePath() {
        return FILE_PATH;
    }
}