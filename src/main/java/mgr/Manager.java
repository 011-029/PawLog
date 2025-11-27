// TODO: 예시 코드 임시로 붙여넣기 한 것임. 추후 수정 필요
package mgr;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Manager<T extends Manageable> {
    // Manager 는 Manageable 을 상속/구현한 클래스만 관리 가능

    public List<T> mList = new ArrayList<>();

    // find(): 키워드로 객체 찾아서 일치하는 첫 번째 객체 반환
    public T find(String kwd) {
        for (T m : mList)
            if (m.matches(kwd))
                return m;
        return null;
    }

    // readAll(): 파일 데이터를 읽어서 객체를 리스트로 저장
    public void readAll(String filename, Factory<T> fac) {
        Scanner fileIn = openFile(filename);
        T m = null;
        while (fileIn.hasNext()) {
            m = fac.create();
            m.read(fileIn);
            mList.add(m);
        }
        fileIn.close();
    }

    // printAll(): 리스트 내 모든 객체에서 print() 실행
    public void printAll() {
        for (T m : mList) {
            m.print();
        }
    }

    // search(): 키워드로 객체 검색해서 출력, end 입력 시 종료
    public void search(Scanner keyScanner) {
        String kwd = null;
        while (true) {
            System.out.print(">> ");
            kwd = keyScanner.next();
            if (kwd.equals("end"))
                break;
            for (T m : mList) {
                if (m.matches(kwd))
                    m.print();
            }
        }
    }

    // findAll(): 키워드로 검색해서 해당하는 객체를 모두 찾아 리스트로 반환
    public List<T> findAll(String kwd) {
        List<T> result = new ArrayList<>();
        for (T m : mList) {
            if (m.matches(kwd))
                result.add(m);
        }
        return result;
    }

    // openFile(): 해당 파일을 읽을 수 있는 Scanner
    public Scanner openFile(String filename) {
        Scanner fileIn = null;
        try {
            fileIn = new Scanner(new File(filename));
        } catch (Exception e) {
            System.out.println(filename + ": 파일 없음");
            System.exit(0);
        }
        return fileIn;
    }

    // addElement(): 리스트에 객체를 추가
    public void addElement(T e) {
        mList.add(e);
    }

    // saveToFile(): txt 파일로 저장
    public void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (T m : mList) {
                String[] arr = m.toTextArray();
                pw.println(String.join(" ", arr));
            }
        } catch (IOException e) {
            System.out.println(filename + ": 파일 저장 중 오류 발생");
        }
    }
}
