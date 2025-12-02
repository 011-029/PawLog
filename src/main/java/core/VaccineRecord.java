package core;

import java.time.LocalDate;
import java.util.Scanner;
import facade.UIData;
import mgr.Manageable;
import mgr.PetOwned;
import mgr.RecordSearchable;
import util.DateUtil;
import util.ReadUtil;

// 예방접종 기록 클래스
public class VaccineRecord implements Manageable, UIData, PetOwned , RecordSearchable {
    int indexId;     // 인덱스 번호 (고유)
    String ownerId;  // 어떤 유저의
    String petName;  // 어떤 펫의 기록인지

    LocalDate date;
    String vaccine;
    String hospital;
    String memo;

    public VaccineRecord() { }

    // 3. 생성자 - 화면용 (VaccinePanel에서 임시 데이터 만들 때 사용)
    public VaccineRecord(String vaccineName, String date, String hospital) {
        this.vaccine = vaccineName;
        this.date = LocalDate.parse(date);
        this.hospital = hospital;
    }

    @Override
    public void read(Scanner scan) {
        if(!scan.hasNext()) return;

        indexId = scan.nextInt();
        ownerId = scan.next();
        petName = scan.next();
        date = ReadUtil.readDate(scan);
        if (scan.hasNext()) vaccine = scan.next();
        if (scan.hasNext()) hospital = scan.next();

        memo = scan.hasNextLine() ? scan.nextLine().trim() : "";
    }

    public void apply(Pet pet, LocalDate date, String vaccine,
                      String hospital, String memo) {
        this.ownerId = pet.getOwnerId();
        this.petName = pet.getName();
        this.date = date;
        this.vaccine = vaccine;
        this.hospital = hospital;
        this.memo = memo;
    }

    @Override
    public void print() {
        System.out.printf("#%d %s %s %s %s (%s)\n",
                indexId, date, safe(vaccine), safe(hospital), safe(memo), getDDayText());
    }

    @Override
    public String[] toTextArray() {
        return new String[] {
                String.valueOf(indexId),
                ownerId,
                petName,
                String.valueOf(date),
                vaccine,
                hospital,
                memo
        };
    }

    @Override
    public boolean matches(String kwd) {
        if (kwd == null || kwd.isBlank()) return true;
        kwd = kwd.trim();

        return (safe(vaccine).contains(kwd)
                || safe(hospital).contains(kwd)
                || safe(memo).contains(kwd));
    }

    public boolean matchesPeriod(LocalDate start, LocalDate end){
        return DateUtil.matchesInPeriod(date, start, end);
    }

    // --- [UIData 인터페이스 구현] ---
    @Override
    public void set(String[] uiTexts) {
        if (uiTexts == null) return;

        if (uiTexts.length > 0) date     = LocalDate.parse(uiTexts[0]);
        if (uiTexts.length > 1) vaccine  = uiTexts[1];
        if (uiTexts.length > 2) hospital = uiTexts[2];
        if (uiTexts.length > 3) memo     = uiTexts[3];

    }

    @Override
    public String[] getUITexts() {
        // 변수 내용을 UI(테이블 등)에 보여주기 위해 배열로 반환
        return new String[] { vaccine, String.valueOf(date), hospital };
    }

    // --- [Getter 메서드] (VaccinePanel에서 사용) ---
    // 아까 빨간 줄 뜨던 부분을 해결해주는 핵심 함수들입니다.
    public String getvaccine() {
        return vaccine;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getHospital() {
        return hospital;
    }

    //필요할 때 쓰기 (검색 등)
    //public long getDDay() {
    //    return DateUtil.getDDay(date);
    //}

    public String getDDayText() {
        return DateUtil.getDDayText(date);
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public LocalDate getRecordDate() {
        return date;
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

    private String safe(String s) {
        return (s == null ? "" : s.trim());
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }
}

