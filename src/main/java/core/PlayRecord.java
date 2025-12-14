package core;

import facade.UIData;
import mgr.Manageable;
import mgr.PetOwned;
import mgr.RecordSearchable;
import util.DateUtil;
import util.ReadUtil;

import java.time.LocalDate;
import java.util.Scanner;

public class PlayRecord implements Manageable, UIData, PetOwned, RecordSearchable {
    int indexId;     // 인덱스 번호 (고유)
    String ownerId;  // 어떤 유저의
    String petName;  // 어떤 펫의 기록인지

    LocalDate date;  // yyyy-mm-dd
    int playTime;    // 놀이 시간 (단위: 분)
    String playType; // 놀이 종류 (없으면 0)
    String memo;     // 메모 (없으면 0)

    public void read(Scanner scan) {
        // 10 test 후추 2025-11-16 35 낚싯대 체력 좋아보임
        indexId = scan.nextInt();
        ownerId = scan.next();
        petName = scan.next();
        date = ReadUtil.readDate(scan);
        playTime = scan.nextInt();
        playType = scan.next();
        memo = scan.nextLine().trim();
    }

    public void apply(Pet pet, LocalDate date,
                      int playTime, String playType, String memo) {
        this.ownerId = pet.getOwnerId();
        this.petName = pet.getName();
        this.date = date;
        this.playTime = playTime;
        this.playType = playType;
        this.memo = memo;
    }

    public void print() {
        System.out.printf("#%d [%d년 %d월 %d일] ", indexId,
                date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        System.out.printf("%d분", playTime);
        if (!playType.equals("0")) System.out.printf(" | %s", playType);
        if (!memo.equals("0")) System.out.printf(" | 메모: %s", memo);
        System.out.println();
    }

    @Override
    public String[] toTextArray() {
        return new String[] {
                String.valueOf(indexId),
                ownerId,
                petName,
                String.valueOf(date),
                String.valueOf(playTime),
                playType,
                memo
        };
    }

    public boolean matches(String kwd) {
        if (kwd.isEmpty())
            return true;
        return memo.contains(kwd) || playType.contains(kwd);
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

    @Override
    public void set(String[] uiTexts) {

    }

    @Override
    public String[] getUITexts() {
        return new String[0];
    }

    public String getPlayType() {
        if (playType.equals("0"))
            return null;
        return playType;
    }

    public int getPlayTime() {
        return playTime;
    }

    public String getMemo() {
        if (memo.equals("0"))
            return null;
        return memo;
    }
}