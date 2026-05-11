import java.awt.Color;
import java.awt.Font;

public final class UITheme {
    //renkler

    public static final Color BG_DARK       = new Color(18,  26,  38);
    public static final Color BG_CARD       = new Color(28,  40,  58);
    public static final Color ACCENT        = new Color(56,  142, 255);
    public static final Color ACCENT_LIGHT  = new Color(100, 175, 255);
    public static final Color SUCCESS       = new Color(34,  197, 94);
    public static final Color DANGER        = new Color(239, 68,  68);
    public static final Color WARNING       = new Color(245, 158, 11);
    public static final Color TEXT_PRIMARY  = new Color(230, 240, 255);
    public static final Color TEXT_MUTED    = new Color(120, 145, 175);
    public static final Color TABLE_HEADER  = new Color(22,  34,  52);
    public static final Color TABLE_ROW_ALT = new Color(24,  38,  55);
    public static final Color BORDER        = new Color(45,  65,  90);

    //yazı tipleri
    public static final Font FONT_TITLE    = new Font("SansSerif", Font.BOLD,  20);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD,  14);
    public static final Font FONT_BODY     = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_SMALL    = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FONT_MONO     = new Font("Monospaced",Font.PLAIN, 12);
    public static final Font FONT_STEP_NUM = new Font("SansSerif", Font.BOLD,  13);

    //boyutlar
    public static final int  WINDOW_W      = 950;
    public static final int  WINDOW_H      = 720;
    public static final int  INDICATOR_H   = 72;

    private UITheme() {}
}
