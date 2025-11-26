package core;

import mgr.Factory;
import mgr.PetRecordMgr;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;

public class MedicineRoutineMgr extends PetRecordMgr<MedicineRoutine> {
    private static MedicineRoutineMgr mgr = null;
    private final String FILE_PATH = "data/medicine_routines.txt";
    Scanner scan = new Scanner(System.in);

    public static MedicineRoutineMgr getInstance() {
        if (mgr == null)
            mgr = new MedicineRoutineMgr();
        return mgr;
    }

    public void addNewRoutine(Pet pet, String medicineName,
                              String takenDOW, String takenTime, int dosage) {
        MedicineRoutine r = new MedicineRoutine();
        r.apply(pet, medicineName, takenDOW, takenTime, dosage);
        saveWithIndexId(r);
    }

    public MedicineRoutine createRoutineFromMedicalRecord(MedicalRecord m) {
        if (m.getPrescribedMedicine() == null)
            return null;

        MedicineRoutine r = new MedicineRoutine();
        r.applyFromMedicalRecord(m);
        saveWithIndexId(r);
        return r;
    }

    public void printTodayRoutine(String ownerId) {
        // 오늘(요일)에 해당하는 루틴 출력
        String todayDOW = LocalDate.now()
                .getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        System.out.println("오늘: " + todayDOW + "요일");
        for (MedicineRoutine r: mList) {
            if (r.takenDOW.contains(todayDOW) && r.ownerId.equals(ownerId)) {
                r.print();
            }
        }
    }

    public void checkTaken(String ownerId) {
        // 복용 여부 체크기능
        int checkIndexId;
        while (true) {
            System.out.print(">> 복용 체크할 인덱스 번호(-1 입력 시 종료): ");
            checkIndexId = scan.nextInt();
            if (checkIndexId == -1) break;

            MedicineRoutine r = findByIndexId(checkIndexId);

            if (r == null || !r.getOwnerId().equals(ownerId))
                System.out.println("유효한 인덱스 번호가 아닙니다");
            else
                r.toggleTaken();

            saveToFile(FILE_PATH);
            printTodayRoutine(ownerId);
        }
    }

    @Override
    public void addNewRow(String[] uiTexts) {

    }

    public void loadFromFile() {
        readAll(FILE_PATH, new Factory<MedicineRoutine>() {
            public MedicineRoutine create() {
                return new MedicineRoutine();
            }
        });
        initNextIndexId();
    }

    @Override
    protected String getFilePath() {
        return FILE_PATH;
    }
}
