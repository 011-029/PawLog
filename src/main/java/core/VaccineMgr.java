package core;

import java.time.LocalDate;
import java.util.ArrayList;

import mgr.Factory;
import mgr.PetRecordMgr;

// 예방접종 기록 관리 클래스
public class VaccineMgr extends PetRecordMgr<VaccineRecord> {
    private static VaccineMgr mgr = null;
    private static final String FILE_PATH = "data/vaccine_records.txt";

    private VaccineMgr() {
        super();
    }

    public static VaccineMgr getInstance() {
        if (mgr == null) {
            mgr = new VaccineMgr();
        }
        return mgr;
    }

    public void addNewRecord(Pet pet, LocalDate date, String vaccine,
                             String hospital, String memo) {
        VaccineRecord r = new VaccineRecord();
        r.apply(pet, date, vaccine, hospital, memo);
        saveWithIndexId(r);
    }

    public ArrayList<VaccineRecord> searchPeriod(LocalDate start, LocalDate end) {
        ArrayList<VaccineRecord> result = new ArrayList<>();
        for(VaccineRecord r : mList) {
            if(r.matchesPeriod(start,end))
                result.add(r);
        }
        return result;
    }

    public void printAll() {
        for (int i = 0; i < mList.size(); i++) {
            System.out.printf("[%d] ", i);
            mList.get(i).print();
        }
    }

    @Override
    public void addNewRow(String[] uiTexts) {
        VaccineRecord rec = new VaccineRecord();
        rec.set(uiTexts);

        mList.add(rec);
    }

    public void loadFromFile() {
        readAll(FILE_PATH, new Factory<VaccineRecord>() {
            public VaccineRecord create() {
                return new VaccineRecord();
            }
        });
        initNextIndexId();
    }

    @Override
    protected String getFilePath() {
        return FILE_PATH;
    }
}