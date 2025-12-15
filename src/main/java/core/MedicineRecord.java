package core;

import facade.UIData;
import mgr.Manageable;
import mgr.PetOwned;
import mgr.RecordSearchable;
import util.DateUtil;
import util.ReadUtil;

import java.time.LocalDate;
import java.util.Scanner;

public class MedicineRecord implements Manageable, UIData, PetOwned, RecordSearchable {
    int indexId;     // 인덱스 번호 (고유)
    String ownerId;  // 어떤 유저의
    String petName;  // 어떤 펫의 기록인지

    String medicineName;  // 약품명
    LocalDate takenDate;  // 복용일
    String takenTime;     // 복용 시간대 (아침, 점심, 저녁, 자기전)
    int dosage;           // 복용량 (단위: mg)

    public void read(Scanner scan) {
        indexId = scan.nextInt();
        ownerId = scan.next();
        petName = scan.next();
        medicineName = scan.next();
        takenDate = ReadUtil.readDate(scan);
        takenTime = scan.next();
        dosage = scan.nextInt();
    }

    public void apply(Pet pet, String medicineName,
                      LocalDate takenDate, String takenTime, int dosage) {
        this.ownerId = pet.getOwnerId();
        this.petName = pet.getName();
        this.medicineName = medicineName;
        this.takenDate = takenDate;
        this.takenTime = takenTime;
        this.dosage = dosage;
    }

    public void print() {
        System.out.printf("#%d [%s] %s | %s | %dmg",
                indexId, takenDate, medicineName, takenTime, dosage);
        System.out.println();
    }

    @Override
    public String[] toTextArray() {
        return new String[] {
                String.valueOf(indexId),
                ownerId,
                petName,
                medicineName,
                String.valueOf(takenDate),
                takenTime,
                String.valueOf(dosage)
        };
    }

    public boolean matches(String kwd) {
        if (kwd.isEmpty())
            return true;
        return medicineName.contains(kwd) || takenTime.contains(kwd);
    }

    @Override
    public boolean matchesPeriod(LocalDate start, LocalDate end){
        return DateUtil.matchesInPeriod(takenDate, start, end);
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public LocalDate getRecordDate() {
        return takenDate;
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
        return new String[]{
                String.valueOf(indexId),//0
                String.valueOf(medicineName),//1
                takenDate.toString(),//2
                String.valueOf(takenTime),//3
                (""+dosage)//4
        };
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getDosage() {
        return dosage;
    }

    public LocalDate getTakenDate() {
        return takenDate;
    }

    public String getTakenTime() {
        return takenTime;
    }
}
