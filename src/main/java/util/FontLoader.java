package util;

import java.awt.*;
import java.io.InputStream;

public class FontLoader {

    private static Font pretendardRegular;
    private static Font pretendardBold;
    private static Font pretendardExtraBold;
    private static Font pretendardThin;
    private static Font pretendardSemiBold;

    static {
        loadFonts();
    }

    private static void loadFonts() {
        try {
            // Regular
            InputStream reg = FontLoader.class.getResourceAsStream("/fonts/Pretendard-Regular.otf");
            pretendardRegular = Font.createFont(Font.TRUETYPE_FONT, reg);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pretendardRegular);

            // SemiBold
            InputStream semiBold = FontLoader.class.getResourceAsStream("/fonts/Pretendard-SemiBold.otf");
            pretendardSemiBold = Font.createFont(Font.TRUETYPE_FONT, semiBold);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pretendardSemiBold);

            // Bold
            InputStream bold = FontLoader.class.getResourceAsStream("/fonts/Pretendard-Bold.otf");
            pretendardBold = Font.createFont(Font.TRUETYPE_FONT, bold);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pretendardBold);

            // ExtraBold
            InputStream exBold = FontLoader.class.getResourceAsStream("/fonts/Pretendard-ExtraBold.otf");
            pretendardExtraBold = Font.createFont(Font.TRUETYPE_FONT, exBold);  // ← 여기!
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pretendardExtraBold);

            // Thin
            InputStream thin = FontLoader.class.getResourceAsStream("/fonts/Pretendard-Thin.otf");
            pretendardThin = Font.createFont(Font.TRUETYPE_FONT, thin);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pretendardThin);

        } catch (Exception e) {
            System.out.println("폰트 로딩 실패 → 기본 폰트로 대체합니다");
            pretendardSemiBold = new Font("굴림", Font.BOLD, 14);
            pretendardRegular = new Font("굴림", Font.PLAIN, 14);
            pretendardBold = new Font("굴림", Font.BOLD, 14);
            pretendardExtraBold = new Font("굴림", Font.BOLD, 14);
            pretendardThin = new Font("굴림", Font.PLAIN, 14);
        }
    }

    public static Font regular(float size) {
        return pretendardRegular.deriveFont(size);
    }

    public static Font bold(float size) {
        return pretendardBold.deriveFont(size);
    }

    public static Font extraBold(float size) {
        return pretendardExtraBold.deriveFont(size);
    }

    public static Font thin(float size) {
        return pretendardThin.deriveFont(size);
    }

    public static Font semiBold(float size) {
        return pretendardSemiBold.deriveFont(size);
    }
}
