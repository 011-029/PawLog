package core;

import mgr.Manageable;
import mgr.PetOwned;
import mgr.PetRecordMgr;
import mgr.RecordSearchable;
import util.DataLoader;
import util.ReadUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Core {
    private static Core core = null;

    private Core() {
    }

    public static Core getInstance() {
        if (core == null)
            core = new Core();
        return core;
    }

    private final UserMgr userMgr = UserMgr.getInstance();
    private final PetMgr petMgr = PetMgr.getInstance();
    private User loggedInUser;
    private Pet loggedInUserPet;

    private final HealthMgr healthMgr = HealthMgr.getInstance();
    private final MedicalMgr medicalMgr = MedicalMgr.getInstance();
    private final MedicineRecordMgr medicineRecordMgr = MedicineRecordMgr.getInstance();
    private final MedicineRoutineMgr medicineRoutineMgr = MedicineRoutineMgr.getInstance();
    private final PlayMgr playMgr = PlayMgr.getInstance();
    private final VaccineMgr vaccineMgr = VaccineMgr.getInstance();
    private final WalkMgr walkMgr = WalkMgr.getInstance();

    private final Scanner scan = new Scanner(System.in);

    public void run() {
        // 데이터 불러오기
        DataLoader.loadAllData();

        // User 로그인
        loginLoop:
        while (true) {
            int opt = startMenu();
            switch (opt) {
                case 1 -> signUp();
                case 2 -> { if (login()) break loginLoop; }
                case 3 -> { return; }
                default -> System.out.println("잘못 입력하셨습니다.");
            }
        }

        // 메인 시스템
        while (true) {
            int opt = mainMenu();
            switch (opt) {
                case 1 -> healthMenu();
                case 2 -> medicalMenu();
                case 3 -> medicineRecordMenu();
                case 4 -> medicineRoutineMenu();
                case 5 -> playMenu();
                case 6 -> vaccineMenu();
                case 7 -> walkMenu();
                case 8 -> addNewRecordMenu();
                case 9 -> removeRecordMenu();
                case 10 -> registerPet();
                case 11 -> printPetsByOwner();
                case 12 -> updatePetImage();
                case 13 -> unifiedSearch();
                case 100 -> {
                    boolean deletePet = petMgr.deletePet(loggedInUser.getId(), loggedInUserPet.getName());
                    System.out.println(deletePet ? "펫 삭제 성공" : "펫 삭제 실패");
                    boolean deleteUser = userMgr.deleteUser(loggedInUser.getId());
                    System.out.println(deleteUser ? "회원탈퇴 성공" : "회원탈퇴 실패");
                }
                case 0 -> { return; }
                default -> System.out.println("잘못 입력하셨습니다.");
            }
        }
    }

    // 메뉴 출력 & opt 입력
    private int startMenu() {
        System.out.println("=================================================");
        System.out.println("🐾 Paw Log 🐾");
        while (true) {
            try {
                System.out.print("1. 회원가입 | 2. 로그인 | 3. 종료 |  ");
                return scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("잘못 입력하셨습니다.");
                scan.nextLine(); // 버퍼 비움
            }
        }
    }

    private int mainMenu() {
        System.out.println("=================================================");
        updatePet(loggedInUser.getId());
        if (loggedInUserPet == null) {
            System.out.println("(현재 등록된 반려동물이 없습니다.)");
        } else {
            System.out.printf("(현재 선택된 펫: %s)\n", loggedInUserPet.getName());
        }
        System.out.println("1. 건강 기록 기능");
        System.out.println("2. 병원 진료 기록 기능");
        System.out.println("3. 복용 기록 기능");
        System.out.println("4. 복용 루틴 기능");
        System.out.println("5. 놀이 기록 기능");
        System.out.println("6. 예방접종 기록 기능");
        System.out.println("7. 산책 기록 기능");
        System.out.println("8. 새 기록 작성 메뉴");
        System.out.println("9. 기록 삭제 메뉴");
        System.out.println("10. 내 펫 등록");
        System.out.println("11. 내 펫 조회");
        System.out.println("12. 펫 프로필 사진 등록");
        System.out.println("13. 통합 검색 기능");
        System.out.println("0. 종료");
        while (true) {
            try {
                System.out.print(">> 메뉴 입력: ");
                return scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("잘못 입력하셨습니다.");
                scan.nextLine();
            }
        }
    }

    // 회원가입
    private void signUp() {
        System.out.println("============= 회원가입 =============");
        System.out.print("ID를 입력하세요: ");
        String id = scan.next();
        System.out.print("PW를 입력하세요: ");
        String pw = scan.next();
        System.out.print("이름을 입력하세요: ");
        String name = scan.next();
        System.out.printf("회원가입 결과: %s\n",
                (userMgr.signUp(id, pw, name)) ? "성공" : "실패");
    }

    // 로그인
    private boolean login() {
        System.out.println("=============== 로그인 ===============");
        System.out.print("ID: ");
        String id = scan.next();
        System.out.print("PW: ");
        String pw = scan.next();
        User u = userMgr.login(id, pw);

        if (u != null) {
            loggedInUser = u;
            updatePet(id);
            System.out.printf("%s님 환영합니다.\n", u.getName());
            return true;
        } else {
            System.out.println("ID 또는 비밀번호가 틀렸습니다.");
            return false;
        }
    }

    // 펫 등록
    private void registerPet() {
        System.out.println("=============== 펫 등록 ===============");
        System.out.print("반려동물 이름을 입력하세요: ");
        String name = scan.next();
        System.out.print("반려동물의 종을 입력하세요: ");
        String species = scan.next();
        System.out.print("반려동물의 성별을 입력하세요(암컷/수컷): ");
        String gender = scan.next();
        System.out.print("중성화 여부를 입력하세요(y/n): ");
        boolean check = scan.next().equals("y");
        System.out.print("생일을 입력하세요(yyyy-mm-dd): ");
        LocalDate birth = ReadUtil.readDate(scan);
        System.out.print("체중을 입력하세요(kg): ");
        double weight = scan.nextDouble();

        String[] petData = {
                loggedInUser.getId(),
                name,
                species,
                gender + ((check) ? "(중성화)" : ""),
                birth.toString(),
                String.valueOf(weight)
        };
        petMgr.registerPet(petData);
        System.out.println("펫 등록 완료!");
    }

    // 내 펫 조회
    private void printPetsByOwner() {
        System.out.println("=============== 펫 목록 ===============");
        System.out.printf("%s님의 펫 목록\n", loggedInUser.getName());
        ArrayList<Pet> pets = petMgr.getPetsByOwner(loggedInUser.getId());
        if (pets.isEmpty())
            System.out.println("등록된 펫이 없습니다.");
        else {
            for (Pet p : pets)
                p.print();
        }
    }

    private void addNewRecordMenu() {
        System.out.println("============= 기록 작성 메뉴 =============");
        while (true) {
            System.out.println("1. 놀이 기록 작성");
            System.out.println("2. 병원 진료 기록 작성");
            System.out.println("나머진 귀찮아서 안만들었음..");
            System.out.println("0. 작성 메뉴 종료");
            System.out.print(">> 메뉴 입력: ");
            int opt = scan.nextInt();
            switch (opt) {
                case 1 -> addNewPlayRecord();
                case 2 -> addNewMedicalRecord();
                case 0 -> { return; }
                default -> System.out.println("잘못 입력하셨습니다");
            }
        }
    }

    // 놀이 기록 추가 기능
    private void addNewPlayRecord() {
        System.out.println("=============== 놀이 기록 작성 ===============");
        System.out.print(">> 날짜 입력: ");
        LocalDate date = ReadUtil.readDate(scan);
        System.out.print(">> 놀이시간 입력: ");
        int playTime = scan.nextInt();
        System.out.print(">> 놀이방식 입력(0 입력시 비워둠): ");
        String playType = scan.next();
        System.out.print(">> 메모 입력(0 입력시 비워둠): ");
        scan.nextLine();
        String memo = scan.nextLine();
        playMgr.addNewRecord(loggedInUserPet, date, playTime, playType, memo);
        System.out.println("새 놀이 기록 작성 완료");
        playMgr.printByOwner(loggedInUser.getId());
    }

    private void addNewMedicalRecord() {
        System.out.println("=============== 진료 기록 작성 ===============");
        System.out.print(">> 날짜 입력: ");
        LocalDate date = ReadUtil.readDate(scan);
        System.out.print(">> 병원명 입력: ");
        String hospital = scan.next();
        System.out.print(">> 증상 입력: ");
        String category = scan.next();
        System.out.print(">> 진료비 입력(-1 입력시 비워둠): ");
        int cost = scan.nextInt();
        medicalMgr.addNewRecord(loggedInUserPet, date, hospital, category, cost);
        System.out.println("새 진료 기록 작성 완료");
        medicalMgr.printByOwner(loggedInUser.getId());
    }

    private void removeRecordMenu() {
        System.out.println("============= 기록 삭제 메뉴 =============");
        while (true) {
            System.out.println("1. 건강기록");
            System.out.println("2. 진료기록");
            System.out.println("3. 복용기록");
            System.out.println("4. 복용루틴");
            System.out.println("5. 놀이기록");
            System.out.println("6. 예방접종기록");
            System.out.println("7. 산책기록");
            System.out.println("0. 삭제 메뉴 종료");
            System.out.println("※※ 주의: 레코드 파일에서 ㄹㅇ 삭제됩니다 ※※");
            System.out.print(">> 메뉴 입력: ");
            int opt = scan.nextInt();
            switch (opt) {
                case 1 -> removeRecord(healthMgr);
                case 2 -> removeRecord(medicalMgr);
                case 3 -> removeRecord(medicineRecordMgr);
                case 4 -> removeRecord(medicineRoutineMgr);
                case 5 -> removeRecord(playMgr);
                case 6 -> removeRecord(vaccineMgr);
                case 7 -> removeRecord(walkMgr);
                case 0 -> { return; }
                default -> System.out.println("잘못 입력하셨습니다");
            }
        }
    }

    // 기록 삭제 기능
    private <T extends Manageable & PetOwned> void removeRecord(PetRecordMgr<T> mgr) {
        mgr.printByOwner(loggedInUser.getId());
        System.out.print(">> 삭제할 인덱스 번호: ");
        int indexId = scan.nextInt();
        T m = mgr.findByIndexId(indexId);

        if (m == null || !m.getOwnerId().equals(loggedInUser.getId())) {
            System.out.println("유효한 인덱스 번호가 아닙니다");
            return;
        }

        boolean result = mgr.removeByIndexId(indexId);
        if (result)
            System.out.println(indexId + "번 데이터가 삭제되었습니다");
        else
            System.out.println("유효한 인덱스 번호가 아닙니다");

        mgr.printByOwner(loggedInUser.getId());
    }

    // 건강 기록 기능
    private void healthMenu() {
        System.out.println("================= 건강 기록 리스트 =================");
        healthMgr.printByOwner(loggedInUser.getId());
    }

    // 진료 기록 기능
    private void medicalMenu() {
        System.out.println("================= 병원 진료 기록 리스트 =================");
        medicalMgr.printByOwner(loggedInUser.getId());
    }

    // 복용 기록 기능
    private void medicineRecordMenu() {
        System.out.println("================= 복용 기록 리스트 =================");
        medicineRecordMgr.printByOwner(loggedInUser.getId());
    }

    // 복용 루틴 기능
    private void medicineRoutineMenu() {
        System.out.println("================= 복용 루틴 =================");
        medicineRoutineMgr.printByOwner(loggedInUser.getId());
        System.out.println("-------------- 오늘 복용해야 할 약 --------------");
        medicineRoutineMgr.printTodayRoutine(loggedInUser.getId());
        medicineRoutineMgr.checkTaken(loggedInUser.getId());
    }

    // 놀이 기록 기능
    private void playMenu() {
        System.out.println("================= 놀이 기록 리스트 =================");
        playMgr.printByOwner(loggedInUser.getId());
    }

    // 예방접종 기록 기능
    private void vaccineMenu() {
        System.out.println("================= 예방접종 기록 리스트 =================");
        vaccineMgr.printByOwner(loggedInUser.getId());
    }

    // 산책 기록 기능
    private void walkMenu() {
        System.out.println("================= 산책 기록 리스트 =================");
        walkMgr.printByOwner(loggedInUser.getId());
    }

    private void updatePetImage() {
        System.out.println("================= 프로필 사진 등록 =================");
        if (!loggedInUserPet.getImagePath().isEmpty())
            System.out.println("현재 등록된 프로필 경로: " + loggedInUserPet.getImagePath());
        System.out.print("등록할 프로필 사진 경로: ");
        String imagePath = scan.next();
        petMgr.updateProfileImage(loggedInUser.getId(), loggedInUserPet.getName(), imagePath);
        System.out.printf("등록 완료! 등록된 경로: %s\n", imagePath);
    }

    //통합 검색용 데이터 모으는 함수
    private ArrayList<RecordSearchable> collectAllRecords(){
        ArrayList<RecordSearchable> all = new ArrayList<>();

        all.addAll(healthMgr.mList);
        all.addAll(medicalMgr.mList);
        all.addAll(medicineRecordMgr.mList);
        all.addAll(playMgr.mList);
        all.addAll(vaccineMgr.mList);
        all.addAll(walkMgr.mList);

        return all;
    }

    private void unifiedSearch() {
        while (true) {
            System.out.println("\n===== 통합 검색 =====");
            System.out.println("1. 기간 검색");
            System.out.println("2. 키워드 검색");
            System.out.println("0. 종료");
            System.out.print(">> ");

            String cmd = scan.next();

            switch (cmd) {
                //기간검색
                case "1" -> {
                    System.out.print("시작일(0은 생략): ");
                    LocalDate start = ReadUtil.readDate(scan);

                    System.out.print("종료일(0은 생략): ");
                    LocalDate end = ReadUtil.readDate(scan);

                    System.out.println("\n=== 기간 검색 결과 ===");
                    for (RecordSearchable r : collectAllRecords()) {
                        if (r.getOwnerId().equals(loggedInUser.getId()) &&
                                r.matchesPeriod(start, end)) {
                            r.print();
                        }
                    }
                }

                //키워드검색
                case "2" -> {
                    scan.nextLine();
                    System.out.print("키워드 입력: ");
                    String kwd = scan.nextLine().trim();

                    System.out.println("\n=== 키워드 검색 결과 ===");
                    for (RecordSearchable r : collectAllRecords()) {
                        if (r.getOwnerId().equals(loggedInUser.getId()) &&
                                ((Manageable) r).matches(kwd)) {
                            r.print();
                        }
                    }
                }

                case "0" -> { return; }
                default -> System.out.println("잘못된 입력");
            }
        }
    }

    // 등록된 펫 업데이트
    private void updatePet(String ownerId) {
        loggedInUserPet = petMgr.getPetByOwner(ownerId);
    }

    public static void main(String[] args) {
         Core core = new Core();
            core.run();
    }
}

