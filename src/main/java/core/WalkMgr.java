package core;

import mgr.Factory;
import mgr.PetRecordMgr;

import java.time.LocalDate;
import java.util.ArrayList;

public class WalkMgr extends PetRecordMgr<WalkRecord> {
    private static WalkMgr mgr = null;
    private static final String FILE_PATH = "data/walk_records.txt";

    public static WalkMgr getInstance() {
        if (mgr == null)
            mgr = new WalkMgr();
        return mgr;
    }

    public void addNewRecord(Pet pet, LocalDate date, int walkTime,
                             double distance, String photoPath, String memo) {
        WalkRecord r = new WalkRecord();
        r.apply(pet, date, walkTime, distance, photoPath, memo);
        saveWithIndexId(r);
    }

    public ArrayList<WalkRecord> searchPeriod(LocalDate start, LocalDate end) {
        ArrayList<WalkRecord> result = new ArrayList<>();
        for(WalkRecord r : mList) {
            if(r.matchesPeriod(start,end))
                result.add(r);
        }
        return result;
    }

    public ArrayList<WalkRecord> findBy(Pet pet) {
        ArrayList<WalkRecord> result = new ArrayList<>();
        String ownerId = pet.getOwnerId();
        String petName = pet.getName();

        for (WalkRecord r : mList) {
            if (r.getOwnerId().equals(ownerId) &&
            r.getPetName().equals(petName)) {
                result.add(r);
            }
        }
        return result;
    }

    public int getWalkCountInPeriod(Pet pet, LocalDate start, LocalDate end) {
        int count = 0;
        String ownerId = pet.getOwnerId();
        String petName = pet.getName();

        for (WalkRecord r : mList) {
            LocalDate d = r.getRecordDate();
            if (!d.isBefore(start) && !d.isAfter(end) &&
                    r.getOwnerId().equals(ownerId) &&
                    r.getPetName().equals(petName)) {
                count++;
            }
        }
        return count;
    }

    public int getWeeklyWalkCount(Pet pet) {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6); // 오늘 포함 7일
        return getWalkCountInPeriod(pet, start, today);
    }

    @Override
    public void addNewRow(String[] uiTexts) {

    }

    public void loadFromFile() {
        readAll(FILE_PATH, new Factory<>() {
            @Override
            public WalkRecord create() {
                return new WalkRecord();
            }
        });
        initNextIndexId();
    }

    @Override
    protected String getFilePath() {
        return FILE_PATH;
    }
}
