package util;

import content.UnsafePetFoodMgr;
import core.*;

public class DataLoader {
    public static void loadAllData() {
        UserMgr.getInstance().loadFromFile();
        PetMgr.getInstance().loadFromFile();

        HealthMgr.getInstance().loadFromFile();
        MedicalMgr.getInstance().loadFromFile();
        MedicineRecordMgr.getInstance().loadFromFile();
        MedicineRoutineMgr.getInstance().loadFromFile();
        PlayMgr.getInstance().loadFromFile();
        VaccineMgr.getInstance().loadFromFile();
        WalkMgr.getInstance().loadFromFile();

        UnsafePetFoodMgr.getInstance().loadFromFile();
    }
}
