package core;

import java.time.LocalDate;
import java.util.Scanner;

import mgr.Manageable;
import mgr.PetOwned;
import mgr.RecordSearchable;
import util.DateUtil;
import util.ReadUtil;

public class VaccineRecord implements Manageable, PetOwned, RecordSearchable {
    int indexId;     // 인덱스 번호 (고유)
    String ownerId;  // 어떤 유저의
    String petName;  // 어떤 펫의 기록인지

    LocalDate date;
    String vaccine;
    String hospital;
    String memo;

    public VaccineRecord() { }

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
        // { indexId, ownerId, petName, date, vaccine, hospital, memo }
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

    public LocalDate getDate() {
        return date;
    }

    public String getHospital() {
        return hospital;
    }

    public long getDDay() {
        return DateUtil.getDDay(date);
    }

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
