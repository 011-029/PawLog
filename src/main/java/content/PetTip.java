package content;

import mgr.Manageable;

import java.util.ArrayList;
import java.util.Scanner;

public class PetTip implements Manageable {
    String title;
    Category category;
    ArrayList<PetType> petType = new ArrayList<>();
    String content;

    public void read(Scanner scan) {
        String line = scan.nextLine();
        readLine(line);
    }

    public void readLine(String line) {
        String[] tokens = line.split("\\|");
        title = tokens[0];
        category = Category.valueFromKo(tokens[1]);
        if (tokens[2].equals("고양이") || tokens[2].equals("강아지"))
            petType.add(PetType.valueFromKo(tokens[2]));
        else {
            petType.add(PetType.CAT);
            petType.add(PetType.DOG);
        }
        content = tokens[3];
    }

    public void print() {
        System.out.println("글 제목: " + title);
        System.out.println("카테고리: " + category.getKoName());
        System.out.println("해당동물: " + petType.toString());
        System.out.println(content);
        System.out.println("-------------------------------------");
    }

    public boolean matches(String kwd) {
        return (title.contains(kwd) || petType.contains(PetType.valueFromKo(kwd))
        || content.contains(kwd));
    }

    public boolean hasPetType(PetType type) {
        return petType != null && petType.contains(type);
    }

    public String[] toTextArray() {
        return new String[0];
    }

    // getter
    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public ArrayList<PetType> getPetType() {
        return petType;
    }

    public String getContent() {
        return content;
    }
}
