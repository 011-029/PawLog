package core;

import facade.UIData;
import mgr.Manageable;
import mgr.PetOwned;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class MedicineRoutine implements Manageable, UIData, PetOwned {
    int indexId;     // 인덱스 번호 (고유)
    String ownerId;  // 어떤 유저의
    String petName;  // 어떤 펫의 기록인지

    String medicineName;     // 약품명
    ArrayList<String> takenDOW = new ArrayList<>(); // 복용 요일 (월, 화, ...)
    String takenTime;        // 복용 시간대 (아침, 점심, 저녁, 자기전)
    int dosage;              // 복용량 (단위: mg)
    boolean isTaken = false; // 복용 여부, 기본값 false
    int lastRecordId = -1;   // 루틴 → 기록 넘길 때 인덱스 번호 기억

    String hospital;
    LocalDate startDate;
    LocalDate endDate;

    public void read(Scanner scan) {
        indexId = scan.nextInt();
        ownerId = scan.next();
        petName = scan.next();
        medicineName = scan.next();
        String dow = scan.next();
        for (int i = 0; i < dow.length(); i++)
            takenDOW.add(String.valueOf(dow.charAt(i)));
        takenTime = scan.next();
        dosage = scan.nextInt();

        hospital = scan.next();
        if (hospital.equals("0")) hospital = null;

        String s = scan.next();
        startDate = s.equals("0") ? null : LocalDate.parse(s);

        String e = scan.next();
        endDate = e.equals("0") ? null : LocalDate.parse(e);
    }

    public void apply(Pet pet, String medicineName,
                      String takenDOW, String takenTime, int dosage) {
        this.ownerId = pet.getOwnerId();
        this.petName = pet.getName();
        this.medicineName = medicineName;
        for (int i = 0; i < takenDOW.length(); i++)
            this.takenDOW.add(String.valueOf(takenDOW.charAt(i)));
        this.takenTime = takenTime;
        this.dosage = dosage;
    }

    public void applyFromMedicalRecord(MedicalRecord m) {
        this.ownerId = m.ownerId;
        this.petName = m.petName;
        this.medicineName = m.getPrescribedMedicine();
        this.takenTime = m.getRoutineTime();
        this.dosage = m.getDosage();
        this.hospital = m.getHospital();
        this.startDate = m.getStartDate();
        this.endDate = m.getEndDate();
        // 요일 계산
        takenDOW.clear();

        if (startDate != null && endDate != null) {
            LocalDate cur = startDate;

            while (!cur.isAfter(endDate)) {
                String dow = cur.getDayOfWeek()
                        .getDisplayName(TextStyle.SHORT, Locale.KOREAN);

                if (!takenDOW.contains(dow))
                    takenDOW.add(dow);

                cur = cur.plusDays(1);
            }
        }
    }

    public void print() {
        System.out.printf("#%d | %s | %s | %s | %dmg | ",
                indexId, medicineName, takenDOW.toString(), takenTime, dosage);
        if (hospital != null)
            System.out.printf(" 병원:%s", hospital);

        if (startDate != null && endDate != null)
            System.out.printf(" | %s~%s | ", startDate, endDate);
        System.out.print(isTaken ? "복용 완료" : "복용 전");
        System.out.println();
    }

    @Override
    public String[] toTextArray() {
        String dow = String.join("", takenDOW);
        return new String[] {
                String.valueOf(indexId),
                ownerId,
                petName,
                medicineName,
                dow,
                takenTime,
                String.valueOf(dosage),
                hospital == null ? "0" : hospital,
                startDate == null ? "0" : startDate.toString(),
                endDate == null ? "0" : endDate.toString()
        };
    }

    public void toggleTaken() {
        // true ↔ false 변경 -> true 면 MedicineRecord 로 생성
        isTaken = !isTaken;
        MedicineRecordMgr mgr = MedicineRecordMgr.getInstance();

        if (isTaken) {
            MedicineRecord record = mgr.createFromRoutine(this);
            lastRecordId = record.getIndexId();
            System.out.printf("▶ %d번으로 Medicine Record가 생성되었습니다\n", lastRecordId);
        } else {
            if (lastRecordId != -1) {
                mgr.removeByIndexId(lastRecordId);
                System.out.printf("▶ %d번 Medicine Record가 삭제되었습니다\n", lastRecordId);
                lastRecordId = -1;
            }
        }
    }

    public MedicineRecord RoutineToRecord() {
        MedicineRecord r = new MedicineRecord();
        r.ownerId = this.ownerId;
        r.petName = this.petName;
        r.medicineName = this.medicineName;
        r.takenDate = LocalDate.now(); // 오늘
        r.takenTime = this.takenTime;
        r.dosage = this.dosage;
        return r;
    }

    public boolean matches(String kwd) {
        if (kwd.isEmpty())
            return true;
        if (takenDOW.contains(kwd))
            return true;
        return medicineName.contains(kwd) || takenTime.contains(kwd);
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public String getPetName() {
        return petName;
    }

    @Override
    public int getIndexId() {
        return indexId;
    }

    @Override
    public void setIndexId(int indexId) {
        this.indexId = indexId;
    }

    @Override
    public void set(String[] uiTexts) {

    }

    @Override
    public String[] getUITexts() {
        return new String[0];
    }
}
