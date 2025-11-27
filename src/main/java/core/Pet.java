package core;

import facade.UIData;
import mgr.Manageable;
import util.ReadUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Pet implements Manageable, UIData {

    private String ownerId;
    private String name;
    private String species;
    private String gender;
    private LocalDate birthDate;
    private double weight;
    private String imagePath;


    private final ArrayList<String> personalityTags = new ArrayList<>();
    private final ArrayList<HealthRecord> healthRecords = new ArrayList<>();
    private final ArrayList<MedicalRecord> medicalRecords = new ArrayList<>();
    private final ArrayList<MedicineRecord> medicineRecords = new ArrayList<>();
    private final ArrayList<MedicineRoutine> medicineRoutines = new ArrayList<>();
    private final ArrayList<PlayRecord> playRecords = new ArrayList<>();
    private final ArrayList<VaccineRecord> vaccineRecords = new ArrayList<>();
    private final ArrayList<WalkRecord> walkRecords = new ArrayList<>();

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

        if (tokens.length >= 7)
            imagePath = tokens[6];
        else imagePath = "";

        if (tokens.length >= 8) {
            setPersonalityTagsFormCSV(tokens[7]);
        }
    }

    private void setPersonalityTagsFormCSV(String csv) {
        personalityTags.clear();
        if (csv == null || csv.isBlank()) return;

        String[] parts = csv.split(",");
        for (String p : parts) {
            String tag = p.trim();
            if (!tag.isEmpty())
                personalityTags.add(tag);
        }
    }

    private String getPersonalityTagsCSV() {
        return String.join(",", personalityTags);
    }

    public void addMedicalRecord(MedicalRecord r) {
        medicalRecords.add(r);
    }

    @Override
    public void print() {
        System.out.printf("[Pet] %s (%s) %.1fkg, %s\n",
                name, species, weight, gender);
    }

    @Override
    public String[] toTextArray() {
        return new String[] {
                ownerId,
                name,
                species,
                gender,
                String.valueOf(birthDate),
                String.valueOf(weight),
                imagePath == null ? "" : imagePath
        };
    }

    @Override
    public boolean matches(String kwd) {
        if (name.contains(kwd) || species.contains(kwd)) return true;
        for (String tag : personalityTags) {
            if (tag.contains(kwd))
                return true;
        }
        return false;
    }

    public void setProfileImage(String imagePath) {
        this.imagePath = imagePath;
    }


    @Override
    public void set(String[] uitexts) {
        // uitexts = {ownerId, name, species, gender, birthDateStr, weightStr}
        ownerId = uitexts[0];
        name = uitexts[1];
        species = uitexts[2];
        gender = uitexts[3];
        birthDate = LocalDate.parse(uitexts[4]);
        weight = Double.parseDouble(uitexts[5]);

        if (uitexts.length > 6) {
            setPersonalityTagsFormCSV(uitexts[6]);
        }
    }

    @Override
    public String[] getUITexts() {
        return new String[]{
                ownerId, name, species, gender,
                birthDate.toString(), Double.toString(weight),
                imagePath == null ? "" : imagePath, getPersonalityTagsCSV()
        };
    }

    // getter
    public String getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public String getGender() { return gender; }
    public LocalDate getBirthDate() { return birthDate; }
    public double getWeight() { return weight; }
    public String getImagePath() { return imagePath; }
    public ArrayList<MedicalRecord> getMedicalRecords(){
        return medicalRecords;
    }

    public ArrayList<String> getPersonalityTags() {
        return personalityTags;
    }

    public void setPersonalityTags(ArrayList<String> tags) {
        personalityTags.clear();
        if (tags != null) {
            personalityTags.addAll(tags);
        }
    }
}
