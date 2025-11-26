package core;

import facade.DataEngineImpl;
import mgr.Factory;

import java.util.ArrayList;

public class PetMgr extends DataEngineImpl<Pet> {
    private static PetMgr mgr = null;
    private static final String FILE_PATH = "data/pets.txt";

    public static PetMgr getInstance() {
        if (mgr == null)
            mgr = new PetMgr();
        return mgr;
    }

    // ownerId와 petName 으로 펫 객체 찾기
    public Pet findPet(String ownerId, String petName) {
        for (Pet p: mList) {
            if (p.getOwnerId().equals(ownerId)
                    && p.getName().equals(petName))
                return p;
        }
        return null;
    }

    @Override
    public void addNewRow(String[] uiTexts) {
        Pet p = new Pet();
        p.set(uiTexts);
        mList.add(p);
    }

    public void registerPet(String[] uiTexts) {
        addNewRow(uiTexts);
        saveToFile(FILE_PATH);
    }

    public void updateProfileImage(String ownerId, String petName, String imagePath) {
        Pet p = findPet(ownerId, petName);
        p.setProfileImage(imagePath);
        saveToFile(FILE_PATH);
    }

    public Pet getPetByOwner(String ownerId) {
        for (Pet p: mList) {
            if (p.getOwnerId().equals(ownerId))
                return p;
        }
        return null;
    }

    public ArrayList<Pet> getPetsByOwner(String ownerId) {
        ArrayList<Pet> result = new ArrayList<>();
        for (Pet p : mList) {
            if (p.getOwnerId().equals(ownerId))
                result.add(p);
        }
        return result;
    }

    public boolean deletePet(String ownerId, String petName) {
        Pet p = findPet(ownerId, petName);
        if (p == null)
            return false;

        mList.remove(p);
        saveToFile(FILE_PATH);
        return true;
    }

    public void loadFromFile() {
        readAll(FILE_PATH, new Factory<Pet>() {
            public Pet create() {
                return new Pet();
            }
        });
    }

    protected String getFilePath() {
        return FILE_PATH;
    }
}
