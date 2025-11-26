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

    String prescribedMedicine;//처방약
    Integer dosage;
    String routineTime;
    LocalDate startDate;
    LocalDate endDate;

    public void read(Scanner scan) {
        indexId = scan.nextInt();
        ownerId = scan.next();
        petName = scan.next();
        date = ReadUtil.readDate(scan);
        hospital = ReadUtil.readHospital(scan);
        category = scan.next();
        cost = scan.nextInt(); //cost 미정일 경우 -1로 받음

        String pm = scan.next();
        prescribedMedicine = pm.equals("0") ? null : pm;

        int d = scan.nextInt();
        dosage = (d == -1 ? null : d);

        String t = scan.next();
        routineTime = t.equals("0") ? null : t;

        String start = scan.next();
        startDate = start.equals("0") ? null : LocalDate.parse(start);

        String end = scan.next();
        endDate = end.equals("0") ? null : LocalDate.parse(end);

    }

    public void apply(Pet pet, LocalDate date,
                      String hospital, String category, int cost,
                      String prescribedMedicine, Integer dosage,
                      String routineTime, LocalDate startDate, LocalDate endDate) {
        this.ownerId = pet.getOwnerId();
        this.petName = pet.getName();
        this.date = date;
        this.hospital = hospital;
        this.category = category;
        this.cost = cost;

        this.prescribedMedicine = prescribedMedicine;
        this.dosage = dosage;
        this.routineTime = routineTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void print() {
        System.out.printf("#%d [%s] %s | %s | %s ",
                indexId, date, hospital, category,
                cost== -1 ? "미정" : String.format("%,d원", cost));
        if (!getDDayText().isEmpty()) System.out.printf("| %s\n", getDDayText());
        if (prescribedMedicine != null ) {
            System.out.printf("| 처방: %s %dmg | %s | %s~%s",
                    prescribedMedicine,
                    dosage,
                    routineTime,
                    startDate,
                    endDate
            );
        }
        else System.out.println();
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
                String.valueOf(cost),
                prescribedMedicine == null ? "0" : prescribedMedicine,
                dosage == null ? "-1" : String.valueOf(dosage),
                routineTime == null ? "0" : routineTime,
                startDate == null ? "0" : startDate.toString(),
                endDate == null ? "0" : endDate.toString()
        };
    }

    public boolean matches(String kwd) {
        if(kwd.isEmpty())
            return true;
        if(hospital.contains(kwd) || category.contains(kwd) )
            return true;
        if (prescribedMedicine != null && prescribedMedicine.contains(kwd))
            return true;

        if (routineTime != null && routineTime.contains(kwd))
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
        //TODO 처방약 용 set 나중에 추가 해야함
    }

    @Override
    public String[] getUITexts() {
        return new String[] {
                date.toString(),
                hospital,
                category,
                cost == -1 ? "" : String.valueOf(cost)
                //TODO 처방약용 추가
        };
    }

    //getter
    public String getHospital(){
        return hospital;
    }
    public String getCategory(){
        return category;
    }
    public String getPrescribedMedicine() {
        return prescribedMedicine;
    }
    public Integer getDosage() {
        return dosage;
    }
    public String getRoutineTime() {
        return routineTime;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
}