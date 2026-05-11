import javax.swing.*;
import java.awt.*;

//yukarıdaki 5 adımlı gösterge
public class StepIndicatorPanel extends JPanel {

    //adım isimlerini sabit hizada tutma
    private static final String[] STEP_NAMES = {
        "Profile", "Define", "Plan", "Collect", "Analyse"
    };
    private static final int CIRCLE_R = 18;
    private static final int CIRCLE_D = CIRCLE_R * 2;

    private final AppState appState;

    public StepIndicatorPanel(AppState appState) {
        this.appState = appState;
        setBackground(UITheme.BG_DARK);
        setPreferredSize(new Dimension(0, UITheme.INDICATOR_H));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int n       = STEP_NAMES.length;
        int w       = getWidth();
        int h       = getHeight();
        int spacing = w / (n + 1);
        int cy      = h / 2 - 6;

        for (int i = 0; i < n; i++) {
            int step = i + 1;
            int cx   = spacing * step;
            boolean active    = (step == appState.getCurrentStep());
            boolean completed = appState.isStepCompleted(step);

            //daire rengi: tamamlandı=yeşil aktif=mavi bekliyor=koyu
            g2.setColor(completed ? UITheme.SUCCESS : active ? UITheme.ACCENT : UITheme.BG_CARD);
            g2.fillOval(cx - CIRCLE_R, cy - CIRCLE_R, CIRCLE_D, CIRCLE_D);

            g2.setColor(completed ? UITheme.SUCCESS : active ? UITheme.ACCENT_LIGHT : UITheme.BORDER);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(cx - CIRCLE_R, cy - CIRCLE_R, CIRCLE_D, CIRCLE_D);

            //daire içine tamamlandıysa tik değilse adım numarası yaz
            g2.setFont(UITheme.FONT_STEP_NUM);
            String lbl = completed ? "\u2713" : String.valueOf(step);
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(UITheme.TEXT_PRIMARY);
            g2.drawString(lbl, cx - fm.stringWidth(lbl) / 2, cy + fm.getAscent() / 2 - 1);

            //dairenin altına adım adını yaz
            g2.setFont(active ? UITheme.FONT_SUBTITLE : UITheme.FONT_SMALL);
            g2.setColor(active ? UITheme.ACCENT_LIGHT : completed ? UITheme.SUCCESS : UITheme.TEXT_MUTED);
            FontMetrics nfm = g2.getFontMetrics();
            String name = STEP_NAMES[i];
            g2.drawString(name, cx - nfm.stringWidth(name) / 2, cy + CIRCLE_R + 16);
        }

        g2.dispose();
    }
}
