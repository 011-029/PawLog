package core;

import mgr.Factory;
import mgr.PetRecordMgr;

import java.time.LocalDate;
import java.util.ArrayList;

// TODO: 클래스 필드, 메서드 작성 (현재는 필수 구현해야 하는 메서드만 자동완성해 둔 상태)
public class MedicalMgr extends PetRecordMgr<MedicalRecord> {
    private static MedicalMgr mgr = null;
    private final String FILE_PATH = "data/medical_records.txt";

    public static MedicalMgr getInstance() {
        if (mgr == null)
            mgr = new MedicalMgr();
        return mgr;
    }

    public void addNewRecord(Pet pet, LocalDate date,
                             String hospital, String category, int cost,
                             String prescribedMedicine, Integer dosage, String routineTime, LocalDate startDate, LocalDate endDate) {
        System.out.println("[DEBUG] addNewRecord() called with: ");
        System.out.println("Pet=" + pet.getName() + ", Date=" + date + ", Hospital=" + hospital
                + ", Category=" + category + ", Cost=" + cost +
                ", Medicine=" + prescribedMedicine + ", Dosage=" + dosage +
                ", RoutineTime=" + routineTime +
                ", Start=" + startDate + ", End=" + endDate);

        MedicalRecord r = new MedicalRecord();
        r.apply(pet, date, hospital, category, cost, prescribedMedicine, dosage, routineTime, startDate, endDate);
        saveWithIndexId(r);
    }

    public ArrayList<MedicalRecord> searchPeriod(LocalDate start, LocalDate end) {
        ArrayList<MedicalRecord> result = new ArrayList<>();
        for(MedicalRecord r : mList) {
            if(r.matchesPeriod(start,end))
                result.add(r);
        }
        return result;
    }

    @Override
    public void addNewRow(String[] uiTexts) {
        MedicalRecord r = new MedicalRecord();
        r.set(uiTexts);
        mList.add(r);
    }

    public void loadFromFile() {
        readAll(FILE_PATH, new Factory<>() {
            @Override
            public MedicalRecord create() {
                return new MedicalRecord();
            }
        });
        initNextIndexId();
    }

    public void saveAll() {
        saveToFile(FILE_PATH);
    }

    @Override
    protected String getFilePath() {
        return FILE_PATH;
    }

    public MedicalRecord getLastRecord() {
        if (mList.isEmpty()) return null;
        return mList.get(mList.size() - 1);
    }

}
