package content;

import mgr.Factory;
import mgr.Manager;

import java.io.InputStream;
import java.util.Scanner;

public class PetTipMgr extends Manager<PetTip> {
    private static PetTipMgr mgr = null;
    private static final String FILE_PATH = "/static_data/pet_tips.txt";

    public static PetTipMgr getInstance() {
        if (mgr == null)
            mgr = new PetTipMgr();
        return mgr;
    }

    // readAll() 오버로드
    public void readAll(InputStream is, Factory<PetTip> fac) {
        Scanner fileIn = new Scanner(is);
        while (fileIn.hasNextLine()) {
            String line = fileIn.nextLine();
            if (line.startsWith("#") || line.isBlank())
                continue;

            PetTip tip = new PetTip();
            tip.readLine(line);
            mList.add(tip);
        }
        fileIn.close();
    }

    public PetTip findByTitle(String title) {
        for (PetTip t : mList) {
            if (t == null || t.getTitle() == null) continue;
            if (normalize(title).equals(normalize(t.getTitle()))) {
                return t;
            }
        }
        return null;
    }

    private String normalize(String s) {
        return s.replaceAll("[\\s\\\\n]+", "");
    }

    public void loadFromFile() {
        InputStream is = getClass().getResourceAsStream(FILE_PATH);
        if (is == null)
            System.out.println(FILE_PATH + ": 파일 없음");
        readAll(is, new Factory<PetTip>() {
            public PetTip create() {
                return new PetTip();
            }
        });
    }
}
