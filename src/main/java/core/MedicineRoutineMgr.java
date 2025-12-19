package core;

import mgr.Factory;
import mgr.PetRecordMgr;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Iterator;
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

        LocalDate today = LocalDate.now();
        if (m.getEndDate() != null && m.getEndDate().isBefore(today)) {
            System.out.println("▶ 해당 처방 약의 복용 기간이 이미 종료되어 루틴이 생성되지 않습니다.");
            return null;
        }

        MedicineRoutine r = new MedicineRoutine();
        r.applyFromMedicalRecord(m);
        saveWithIndexId(r);

        System.out.println("[DEBUG] createRoutineFromMedicalRecord 호출됨");
        System.out.println("[DEBUG] 대상 MedicalRecord id = " + m.getIndexId());

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

    public void removeExpiredRoutines(){
        LocalDate today = LocalDate.now();

        Iterator<MedicineRoutine> it = mList.iterator();
        boolean removed = false;

        while (it.hasNext()){
            MedicineRoutine r = it.next();

            // endDate가 있고, 오늘보다 이전이면 삭제
            if (r.endDate != null && r.endDate.isBefore(today)) {
                System.out.printf("기간 만료 루틴 자동삭제: #%d (%s)\n", r.getIndexId(), r.medicineName);
                it.remove();
                removed = true;
            }
        }

        if (removed) {
            saveToFile(getFilePath());
        }
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
