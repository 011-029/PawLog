package core;

import facade.UIData;
import mgr.Manageable;

import java.util.Scanner;

public class User implements Manageable, UIData {

    private String id;
    private String password;
    private String name;

    @Override
    public void read(Scanner scan) {
        id = scan.next();
        password = scan.next();
        name = scan.next();
    }

    @Override
    public void print() {
        System.out.printf("[User] %s (%s)\n", name, id);
    }

    @Override
    public String[] toTextArray() {
        return new String[] {
                id, password, name
        };
    }

    @Override
    public boolean matches(String kwd) {
        return id.contains(kwd) || name.contains(kwd);
    }

    @Override
    public void set(String[] uitexts) {
        // uitexts = {id, password, name}
        id = uitexts[0];
        password = uitexts[1];
        name = uitexts[2];
    }

    @Override
    public String[] getUITexts() {
        return new String[]{id, password, name};
    }

    public boolean setName(String name) {
        if (name.isBlank())
            return false;
        this.name = name;

        return true;
    }

    public boolean setPassword(String password) {
        if (password.isBlank())
            return false;
        this.password = password;
        return true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean checkPassword(String pw) {
        return password.equals(pw);
    }
}
