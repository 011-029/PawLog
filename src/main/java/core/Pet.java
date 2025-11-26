package core;

import facade.UIData;
import mgr.Manageable;

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

        if (tokens.length == 7)
            imagePath = tokens[6];
        else imagePath = "";
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
        return name.contains(kwd) || species.contains(kwd);
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
    }

    @Override
    public String[] getUITexts() {
        return new String[]{
                ownerId, name, species, gender,
                birthDate.toString(), Double.toString(weight),
                imagePath == null ? "" : imagePath
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
