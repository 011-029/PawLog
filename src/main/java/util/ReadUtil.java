package util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

public class ReadUtil {
    public  static LocalDate readDate(Scanner scan) {
        while (true){
            String input = scan.next();
            if (input.equals("0")) return null; // 주의점: 다른 date 입력에서도 0하면 null 됨
            try {
                return LocalDate.parse(input);
            } catch (DateTimeException e) {
                System.out.println("잘못된 날짜형식 : yyyy-mm-dd 로 입력할 것");
            }
        }
    }

    public static String readHospital(Scanner scan) {
        while (true){
            String input = scan.next();
            if (input.length() >= 3) return input;
            System.out.println("병원명을 세글자 이상 입력하세요");
        }
    }
}
