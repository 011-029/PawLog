package core;

import mgr.Factory;
import mgr.PetRecordMgr;

import java.time.LocalDate;
import java.util.ArrayList;

public class HealthMgr extends PetRecordMgr<HealthRecord> {
    private static HealthMgr mgr = null;
    private final String FILE_PATH = "data/health_records.txt";
    private int nextIndex = 1;


    private HealthMgr() { }

    public static HealthMgr getInstance() {
        if (mgr == null)
            mgr = new HealthMgr();
        return mgr;
    }

    public void addNewRecord(Pet pet, LocalDate date, int meal,
                             int water, double weight, String brushed, String memo) {
        HealthRecord r = new HealthRecord();
        r.apply(pet, date, meal, water, weight, brushed, memo);
        saveWithIndexId(r);
    }

    public ArrayList<HealthRecord> searchPeriod(LocalDate start, LocalDate end) {
        ArrayList<HealthRecord> result = new ArrayList<>();
        for(HealthRecord r : mList) {
            if(r.matchesPeriod(start,end))
                result.add(r);
        }
        return result;
    }

    public ArrayList<HealthRecord> findByPet(Pet pet) {
        ArrayList<HealthRecord> result = new ArrayList<>();
        String ownerId = pet.getOwnerId();
        String petName = pet.getName();

        for (HealthRecord r : mList) {
            if( r.ownerId.equals(ownerId) && r.petName.equals(petName)) {
                result.add(r);
            }
        }
        return result;
    }

    public String getHealthAlertsText(Pet pet) {
        ArrayList<HealthRecord> records = findByPet(pet);
        if (records.isEmpty()) return "";

        WalkMgr walkMgr = WalkMgr.getInstance();
        int weeklyWalkCount = walkMgr.getWeeklyWalkCount(pet);

        StringBuilder sb = new StringBuilder();
        String todayStr = LocalDate.now().toString();

        HealthRecord latest = getLatestRecord(records);
        if (latest != null) {
            applyWeightRiskAlert(sb, latest, pet, todayStr);
        }

        if (weeklyWalkCount < 1) {
            appendAlertBlock(
                    sb,
                    "활동 부족 위험",
                    "최근 1주일 동안 산책이 거의 없어요.",
                    todayStr
            );
        }

        if(isDehydrationRisk(records)) {
            appendAlertBlock(
                    sb,
                    "탈수 위험",
                    "최근 며칠간 물 섭취량이 너무 적어요.",
                    todayStr
            );
        }

        if (isDigestiveIssue(records)) {
            appendAlertBlock(
                    sb,
                    "소화 이상 위험",
                    "최근 설사/구토 기록이 있어요.",
                    todayStr
            );
        }

        return sb.toString().trim();
    }

    private void appendAlertBlock(StringBuilder sb,
                                  String title,
                                  String message,
                                  String dateStr) {
        if (sb.length() > 0)
            sb.append("\n\n");
        sb.append("⚠ ").append(title).append("\n");
        sb.append(message).append("\n");
        sb.append(dateStr);
    }

    private HealthRecord getLatestRecord(ArrayList<HealthRecord> records) {
        HealthRecord latest = null;
        for (HealthRecord r : records) {
            if (latest == null || r.date.isAfter(latest.date)) {
                latest = r;
            }
        }
        return latest;
    }
    private void applyWeightRiskAlert(StringBuilder sb,
                                      HealthRecord record,
                                      Pet pet,
                                      String dateStr) {

        double normal = pet.getWeight();  // Pet에 정의돼 있다고 가정
        double w = record.weight;

        // 정상 체중 정보가 없거나, 기록에 몸무게가 없으면 스킵
        if (normal <= 0 || w <= 0) return;

        double upper = normal * 1.10; // +10%
        double lower = normal * 0.90; // -10%

        if (w > upper) {
            appendAlertBlock(
                    sb,
                    "비만 위험",
                    "기준 체중보다 많이 나가고 있어요.",
                    dateStr
            );
        } else if (w < lower) {
            appendAlertBlock(
                    sb,
                    "저체중 위험",
                    "기준 체중보다 너무 적게 나가고 있어요.",
                    dateStr
            );
        }
    }


    private boolean isDehydrationRisk(ArrayList<HealthRecord> records) {
        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(2); // 최근 3일

        int low = 0;
        int total = 0;

        for (HealthRecord r : records) {
            if (!r.date.isBefore(from) && !r.date.isAfter(today)) {
                total++;
                if (r.water <= 1) {
                    low++;
                }
            }
        }

        return (total > 0 && low >= 2);
    }

    private boolean isDigestiveIssue(ArrayList<HealthRecord> records) {
        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(2); // 최근 3일

        int zeroMeal = 0;
        boolean memoDanger = false;

        for (HealthRecord r : records) {
            if (!r.date.isBefore(from) && !r.date.isAfter(today)) {
                if (r.meal == 0) zeroMeal++;

                if (r.memo != null) {
                    String m = r.memo;
                    if (m.contains("설사") || m.contains("구토") || m.contains("토함")) {
                        memoDanger = true;
                    }
                }
            }
        }

        return (zeroMeal >= 2 || memoDanger);
    }

    @Override
    public void addNewRow(String[] uiTexts) {
        HealthRecord r = new HealthRecord();
        r.set(uiTexts);
        r.indexId = nextIndex++;

        mList.add(r);

    }

    public void loadFromFile() {
        readAll(FILE_PATH, new Factory<HealthRecord>() {
            public HealthRecord create() {
                return new HealthRecord();
            }
        });
        initNextIndexId();
    }

    @Override
    protected String getFilePath() {
        return FILE_PATH;
    }
}
