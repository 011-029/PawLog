package core;

import facade.UIData;
import mgr.Manageable;
import mgr.PetOwned;
import mgr.RecordSearchable;
import util.DateUtil;
import util.ReadUtil;

import java.time.LocalDate;
import java.util.Scanner;

public class MedicalRecord implements Manageable, UIData, PetOwned, RecordSearchable {
    int indexId;     // 인덱스 번호 (고유)
    String ownerId;  // 어떤 유저의
    String petName;  // 어떤 펫의 기록인지

    LocalDate date;
    String hospital;
    String category;
    int cost;

    public void read(Scanner scan) {
        indexId = scan.nextInt();
        ownerId = scan.next();
        petName = scan.next();
        date = ReadUtil.readDate(scan);
        hospital = ReadUtil.readHospital(scan);
        category = scan.next();
        cost = scan.nextInt(); //cost 미정일 경우 -1로 받음
    }

    public void apply(Pet pet, LocalDate date,
                      String hospital, String category, int cost) {
        this.ownerId = pet.getOwnerId();
        this.petName = pet.getName();
        this.date = date;
        this.hospital = hospital;
        this.category = category;
        this.cost = cost;
    }

    public void print() {
        System.out.printf("#%d [%s] %s | %s | %s ",
                indexId, date, hospital, category,
                cost== -1 ? "미정" : String.format("%,d원", cost));
        if (!getDDayText().isEmpty()) System.out.printf("| %s\n", getDDayText());
        else System.out.print("\n");
    }

    @Override
    public String[] toTextArray() {
        return new String[] {
                String.valueOf(indexId),
                ownerId,
                petName,
                String.valueOf(date),
                hospital,
                category,
                String.valueOf(cost)
        };
    }

    public boolean matches(String kwd) {
        if(kwd.isEmpty())
            return true;
        if(hospital.contains(kwd) || category.contains(kwd) )
            return true;
        return ("" + cost).equals(kwd);
    }

    public boolean matchesPeriod(LocalDate start, LocalDate end){
        return DateUtil.matchesInPeriod(date, start, end);
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

    @Override
    public void set(String[] uitexts) {
        // uitexts = {date, hospital, category, cost}
        date = LocalDate.parse(uitexts[0]);
        hospital = uitexts[1];
        category = uitexts[2];
        if (uitexts.length > 3 && !uitexts[3].isBlank()) {
            cost = Integer.parseInt(uitexts[3]);
        } else {
            cost = -1;
        }
    }

    @Override
    public String[] getUITexts() {
        return new String[] {
                date.toString(),
                hospital,
                category,
                cost == -1 ? "" : String.valueOf(cost)
        };
    }

    //getter
    public String getHospital(){
        return hospital;
    }
    public String getCategory(){
        return category;
    }
}