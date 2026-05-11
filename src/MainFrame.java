import javax.swing.*;
import java.awt.*;

//ana uygulama penceresi (JFrame)
public class MainFrame extends JFrame {

    private static final String CARD_STEP1 = "STEP1";
    private static final String CARD_STEP2 = "STEP2";
    private static final String CARD_STEP3 = "STEP3";
    private static final String CARD_STEP4 = "STEP4";
    private static final String CARD_STEP5 = "STEP5";

    //oturum verilerini tutan ortak durum nesnesi
    private AppState           appState;
    //paneller arası geçişi sağlayan düzen yöneticisi
    private final CardLayout   cardLayout;
    //CardLayout'un uygulandığı ana panel
    private final JPanel       cardPanel;
    //pencerenin üstündeki adım gösterge paneli
    private StepIndicatorPanel indicator;

    //adımlar
    private Step1ProfilePanel step1;
    private Step2DefinePanel  step2;
    private Step3PlanPanel    step3;
    private Step4CollectPanel step4;
    private Step5AnalysePanel step5;

    public MainFrame() {
        super("ISO 15939 Measurement Process Simulator");
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);

        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG_DARK);
        setSize(UITheme.WINDOW_W, UITheme.WINDOW_H);
        setMinimumSize(new Dimension(700, 560));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //ilk oturumu başlat ve 1. adımı göster
        initSession();
    }

    private void initSession() {
        appState = new AppState();

        step1 = new Step1ProfilePanel(appState, this);
        step2 = new Step2DefinePanel(appState, this);
        step3 = new Step3PlanPanel(appState, this);
        step4 = new Step4CollectPanel(appState, this);
        step5 = new Step5AnalysePanel(appState, this);

        // CardLayout a eklenmiş eski panelleri temizeme
        cardPanel.removeAll();
        //her paneli sabit ismiyle CardLayout a kaydet
        cardPanel.add(step1, CARD_STEP1);
        cardPanel.add(step2, CARD_STEP2);
        cardPanel.add(step3, CARD_STEP3);
        cardPanel.add(step4, CARD_STEP4);
        cardPanel.add(step5, CARD_STEP5);

        if (indicator != null) remove(indicator);
        indicator = new StepIndicatorPanel(appState);

        if (getContentPane().getComponentCount() == 0) {
            add(indicator, BorderLayout.NORTH);
            add(cardPanel, BorderLayout.CENTER);
        } else {
            getContentPane().remove(0);
            add(indicator, BorderLayout.NORTH, 0);
        }

        navigateTo(1);
    }

    //restart'ta her şeyi sıfırlama
    public void restart() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Start a new session? All current data will be cleared.",
                "Restart Session", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            getContentPane().removeAll();//bileşenleri temizle
            initSession();//oturumu sıfırla
            revalidate();
            repaint();//ekranı yenile
        }
    }

    public void navigateTo(int step) {
        appState.setCurrentStep(step);//aktif adımı kaydetme
        switch (step) {
            case 1: cardLayout.show(cardPanel, CARD_STEP1); step1.refresh(); break;
            case 2: cardLayout.show(cardPanel, CARD_STEP2); step2.refresh(); break;
            case 3: cardLayout.show(cardPanel, CARD_STEP3); step3.refresh(); break;
            case 4: cardLayout.show(cardPanel, CARD_STEP4); step4.refresh(); break;
            case 5: cardLayout.show(cardPanel, CARD_STEP5); step5.refresh(); break;
        }
        //üstteki adım göstergesini yeniden çizme
        indicator.repaint();
    }

    public AppState getAppState() { return appState; }
}
