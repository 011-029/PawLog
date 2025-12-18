package core;

import mgr.Manageable;
import mgr.PetOwned;
import mgr.RecordSearchable;
import util.DateUtil;
import util.ReadUtil;

import java.time.LocalDate;
import java.util.Scanner;

public class HealthRecord implements Manageable, PetOwned, RecordSearchable {
    int indexId;     // 인덱스 번호 (고유)
    String ownerId;  // 어떤 유저의
    String petName;  // 어떤 펫의 기록인지
    LocalDate date;
    int meal;
    int waterMl;
    double weight;
    String brushed;
    String memo;

    public HealthRecord() {}

    public void read(Scanner scan) {
        indexId = scan.nextInt();
        ownerId = scan.next();
        petName = scan.next();

        date = ReadUtil.readDate(scan);
        meal = scan.nextInt();
        waterMl = scan.nextInt();
        weight = scan.nextDouble();
        brushed = scan.next();
        memo = "";
        if (scan.hasNextLine()) {
            memo = scan.nextLine().trim();
        }
    }

    public void print() {
        System.out.printf("[%d] %s | 식사:%d 음수:%d 양치:%s | %s%n",
                indexId, date, meal, waterMl,
                brushed != null ? brushed : "",
                memo != null ? memo : ""
        );
    }

    public void apply(Pet pet, LocalDate date, int meal, int waterMl,
                      double weight, String brushed, String memo) {
        this.ownerId = pet.getOwnerId();
        this.petName = pet.getName();
        this.date = date;
        this.meal = meal;
        this.waterMl = waterMl;
        this.weight = weight;
        this.brushed = brushed;
        this.memo = memo;
    }

    @Override
    public String[] toTextArray() {
        return new String[] {
                String.valueOf(indexId),
                ownerId,
                petName,
                String.valueOf(date),
                String.valueOf(meal),
                String.valueOf(waterMl),
                String.valueOf(weight),
                brushed,
                memo
        };
    }

    public boolean matches(String kwd) {
        if (kwd == null || kwd.isBlank()) return true;
        return memo != null && memo.contains(kwd);
    }

    @Override
    public boolean matchesPeriod(LocalDate start, LocalDate end){
        return DateUtil.matchesInPeriod(date, start, end);
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

    public LocalDate getDate() {
        return date;
    }

    public int getMeal() {
        return meal;
    }

    public int getWater() {
        return waterMl;
    }

    public double getWeight() {
        return weight;
    }

    public boolean getIsBrushed() {
        return brushed.equals("yes");
    }

    public String getMemo() {
        return memo;
    }

    @Override
    public void setIndexId(int indexId) {
        this.indexId = indexId;
    }

    @Override
    public LocalDate getRecordDate() {
        return date;
    }
}
