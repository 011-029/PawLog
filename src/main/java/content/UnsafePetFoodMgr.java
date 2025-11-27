package content;

import facade.DataEngineImpl;
import mgr.Factory;

import java.io.InputStream;
import java.util.Scanner;

public class UnsafePetFoodMgr extends DataEngineImpl<UnsafePetFood> {
    private static UnsafePetFoodMgr mgr = null;
    private static final String FILE_PATH = "/static_data/unsafe_pet_foods.txt";

    public static UnsafePetFoodMgr getInstance() {
        if (mgr == null)
            mgr = new UnsafePetFoodMgr();
        return mgr;
    }

    @Override
    public void readAll(InputStream is, Factory<UnsafePetFood> fac) {
        Scanner fileIn = new Scanner(is);
        while (fileIn.hasNextLine()) {
            String line = fileIn.nextLine();
            if (line.startsWith("#") || line.isBlank())
                continue;

            UnsafePetFood food = new UnsafePetFood();
            food.readLine(line);
            mList.add(food);
        }
        fileIn.close();
    }

    public void loadFromFile() {
        InputStream is = getClass().getResourceAsStream(FILE_PATH);
        if (is == null)
            System.out.println(FILE_PATH + ": 파일 없음");
        readAll(is, new Factory<UnsafePetFood>() {
            public UnsafePetFood create() {
                return new UnsafePetFood();
            }
        });
    }

    @Override
    public void addNewRow(String[] uiTexts) {
    }
}
