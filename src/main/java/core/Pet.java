package core;

import mgr.Manageable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Pet implements Manageable {

    private String ownerId;
    private String name;
    private String species;
    private String gender;
    private LocalDate birthDate;
    private double weight;
    private String imagePath;
    private final ArrayList<String> personalityTags = new ArrayList<>();

    @Override
    public void read(Scanner scan) {
        String line = scan.nextLine();
        String[] tokens = line.split(" ");
        ownerId = tokens[0];
        name = tokens[1];
        species = tokens[2];
        gender = tokens[3];
        birthDate = LocalDate.parse(tokens[4]);
        weight = Double.parseDouble(tokens[5]);

        if (tokens[6].equals("0"))
            imagePath = null;
        else imagePath = tokens[6];

        personalityTags.clear();
        if (tokens.length >= 8) {               // 태그가 있을 때만
            String[] tags = tokens[7].split(",");
            for (String t : tags) {
                t = t.trim();
                if (!t.isEmpty())
                    personalityTags.add(t);
            }
        }
    }

    @Override
    public void print() {
        System.out.printf("[Pet] %s (%s) %.1fkg,  %s,",
                name, species, weight, gender);
        if(!personalityTags.isEmpty())
            System.out.println(" 성격태그 : " + String.join(", ", personalityTags));
        System.out.println();
    }

    @Override
    public String[] toTextArray() {
        // { ownerId, name, species, gender, birthDate, weight, imagePath, tags }
        return new String[] {
                ownerId,
                name,
                species,
                gender,
                String.valueOf(birthDate),
                String.valueOf(weight),
                imagePath == null ? "0" : imagePath,
                joinPersonalityTags()
        };
    }

    public void apply(String[] arr) {
        // arr = { ownerId, name, species, gender, birthDate, weight, imagePath }
        this.ownerId = arr[0];
        this.name = arr[1];
        this.species = arr[2];
        this.gender = arr[3];
        this.birthDate = LocalDate.parse(arr[4]);
        this.weight = Double.parseDouble(arr[5]);
        this.imagePath = (imagePath.equals("0") ? "null" : arr[6]);
    }

    @Override
    public boolean matches(String kwd) {
        return name.contains(kwd) || species.contains(kwd);
    }

    // getter
    public String getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public String getGender() { return gender; }
    public LocalDate getBirthDate() { return birthDate; }
    public double getWeight() { return weight; }
    public String getImagePath() { return imagePath; }

    public long getBirthDateDDay() {
        // 생일 d-day 계산
        LocalDate today = LocalDate.now();
        LocalDate nextBirthday = birthDate.withYear(today.getYear());

        if (nextBirthday.isBefore(today)) {
            nextBirthday = nextBirthday.plusYears(1);
        }
        long days = ChronoUnit.DAYS.between(today, nextBirthday);

        return days;
    }

    public ArrayList<String> getPersonalityTags() {
        return personalityTags;
    }

    private String joinPersonalityTags() {
        if (personalityTags.isEmpty()) return "";
        return personalityTags.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(","));
    }

    public boolean setName(String name) {
        if (name.isBlank())
            return false;
        this.name = name;
        return true;
    }

    public boolean setBirthDate(LocalDate date) {
        if (birthDate.isAfter(LocalDate.now()))
            return false;
        this.birthDate = date;
        return true;
    }

    public boolean setSpecies(String species) {
        if (species.isBlank())
            return false;
        this.species = species;
        return true;
    }

    public boolean setWeight(double weight) {
        if (weight <= 0)
            return false;
        this.weight = weight;
        return true;
    }

    public void setProfileImage(String imagePath) {
        this.imagePath = imagePath;
    }
}
