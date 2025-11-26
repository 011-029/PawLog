package mgr;


import java.time.LocalDate;

public interface RecordSearchable {
    String getOwnerId();
    LocalDate getRecordDate();
    boolean matchesPeriod(LocalDate start, LocalDate end);
    void print();
}
