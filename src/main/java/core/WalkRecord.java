package core;

import mgr.Manageable;
import mgr.PetOwned;
import mgr.RecordSearchable;
import util.DateUtil;
import util.ReadUtil;

import java.time.LocalDate;
import java.util.Scanner;

public class WalkRecord implements Manageable, PetOwned, RecordSearchable {
    int indexId;     // 인덱스 번호 (고유)
    String ownerId;  // 어떤 유저의
    String petName;  // 어떤 펫의 기록인지

    LocalDate date;
    int walkTime;
    double distance;
    String photoPath;
    String memo;

    public void read(Scanner scan) {
        indexId = scan.nextInt();
        ownerId = scan.next();
        petName = scan.next();
        date = ReadUtil.readDate(scan);
        walkTime = scan.nextInt();
        distance = scan.nextDouble();
        photoPath = scan.next();
        memo = scan.nextLine().trim();
    }

    public void apply(Pet pet, LocalDate date, int walkTime,
                      double distance, String photoPath, String memo) {
        this.ownerId = pet.getOwnerId();
        this.petName = pet.getName();
        this.date = date;
        this.walkTime = walkTime;
        this.distance = distance;
        this.photoPath = photoPath;
        this.memo = memo;
    }

    public void print() {
        System.out.printf("[%s] %d분 | %.2fkm ",
                date, walkTime, distance);
        if(!memo.equals("0")) System.out.printf("| 메모: %s\n", memo);
    }

    @Override
    public String[] toTextArray() {
        return new String[] {
                String.valueOf(indexId),
                ownerId,
                petName,
                String.valueOf(date),
                photoPath,
                memo
        };
    }

    public boolean matches(String kwd) {
        if(kwd.isEmpty())
            return true;
        if((""+walkTime).equals(kwd))
            return true;
        return memo.contains(kwd) || ("" + distance).contains(kwd);
    }

    public boolean matchesPeriod(LocalDate start, LocalDate end){
        return DateUtil.matchesInPeriod(date, start, end);
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

    public int getWalkTime() {
        return walkTime;
    }

    public double getDistance() {
        return distance;
    }

    public String getPhotoPath() {
        if (photoPath.equals("0"))
            return null;
        return photoPath;
    }

    public String getMemo() {
        if (memo.equals("0"))
            return null;
        return memo;
    }
}
