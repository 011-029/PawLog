package content;

import facade.UIData;
import mgr.Manageable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class UnsafePetFood implements Manageable, UIData {
    String foodName;
    ArrayList<PetType> petType = new ArrayList<>();
    AllowanceLevel allowanceLevel;
    RiskLevel riskLevel;
    String description;
    String foodImage = null;

    public void read(Scanner scan) {
        String line = scan.nextLine();
        readLine(line);
    }

    public void readLine(String line) {
        String[] tokens = line.split("\\|");
        String[] petTypes = tokens[1].split(",");

        foodName = tokens[0];
        for (String s : petTypes)
            petType.add(PetType.valueFromKo(s.trim()));
        allowanceLevel = AllowanceLevel.valueFromKo(tokens[2]);
        riskLevel = RiskLevel.valueFromKo(tokens[3]);
        description = tokens[4];

        loadFoodImage();
    }

    public void loadFoodImage() {
        String basePath ="/images/foods/";
        String[] exts = {".jpg", ".jpeg", "png"};

        for (String ext : exts) {
            String path = basePath + foodName + ext;
            URL url = getClass().getResource(path);
            if (url != null) {
                foodImage = path;
                return;
            }
        }
    }

    public void print() {
        System.out.println("음식이름: " + foodName);
        System.out.println("해당동물: " + petType.toString());
        System.out.println("허용레벨: " + allowanceLevel);
        System.out.println("위험레벨: " + riskLevel);
        System.out.println("설명: " + description);
        System.out.println("이미지: " + foodImage);
        System.out.println("-------------------------------------");
    }

    public boolean matches(String kwd) {
        return (foodName.contains(kwd) || petType.contains(PetType.valueFromKo(kwd))
                || description.contains(kwd));
    }

    public boolean hasPetType(PetType type) {
        return petType != null && petType.contains(type);
    }

    public String[] toTextArray() {
        return new String[] {
        };
    }

    @Override
    public void set(String[] uitexts) {

    }

    @Override
    public String[] getUITexts() {
        return new String[0];
    }

    // getter
    public String getFoodName() {
        return foodName;
    }

    public ArrayList<PetType> getPetType() {
        return petType;
    }

    public AllowanceLevel getAllowanceLevel() {
        return allowanceLevel;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public String getDescription() {
        return description;
    }
}
