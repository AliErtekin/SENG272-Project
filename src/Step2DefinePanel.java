import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//2.adım
public class Step2DefinePanel extends JPanel {

    private final AppState           appState;
    private final MainFrame          mainFrame;
    private final ScenarioRepository repo = ScenarioRepository.getInstance();

    private ButtonGroup typeGroup;
    private ButtonGroup modeGroup;
    private ButtonGroup scenarioGroup;


    private JRadioButton rbProduct;
    private JRadioButton rbProcess;
    private JRadioButton rbHealth;
    private JRadioButton rbEducation;

    private JPanel             scenarioPanel;
    private List<JRadioButton> scenarioButtons = new ArrayList<>();

    public Step2DefinePanel(AppState appState, MainFrame mainFrame) {
        this.appState  = appState;
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);
        setBorder(new EmptyBorder(30, 60, 30, 60));
        add(buildHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UITheme.BG_DARK);
        content.add(buildTypeSection());
        content.add(Box.createVerticalStrut(16));
        content.add(buildModeSection());
        content.add(Box.createVerticalStrut(16));
        content.add(buildScenarioSection());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBackground(UITheme.BG_DARK);
        scroll.getViewport().setBackground(UITheme.BG_DARK);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        add(scroll, BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UITheme.BG_DARK);
        p.setBorder(new EmptyBorder(0, 0, 22, 0));

        addLeft(p, Step1ProfilePanel.label("Define Quality Dimensions", UITheme.FONT_TITLE, UITheme.TEXT_PRIMARY));
        addLeft(p, Step1ProfilePanel.label("Select the quality type, mode, and scenario.", UITheme.FONT_BODY, UITheme.TEXT_MUTED));
        return p;
    }

    private void addLeft(JPanel p, JLabel l) { l.setAlignmentX(Component.LEFT_ALIGNMENT); p.add(l); p.add(Box.createVerticalStrut(4)); }


    //2a kalite tipi seçimi
    private JPanel buildTypeSection() {
        typeGroup = new ButtonGroup();
        JPanel card = sectionCard("2a.  Quality Type", "Select the type of quality to measure.");

        rbProduct = radioBtn("Product Quality",
                "Software characteristics: performance, security, usability, reliability");
        rbProcess = radioBtn("Process Quality",
                "Development process: sprint efficiency, code quality, team collaboration");

        typeGroup.add(rbProduct); typeGroup.add(rbProcess);
        rbProduct.setSelected(true);
        rbProduct.addActionListener(e -> refreshScenarios());
        rbProcess.addActionListener(e -> refreshScenarios());

        card.add(rbProduct); card.add(Box.createVerticalStrut(8)); card.add(rbProcess);
        return card;
    }


    //2b. mod seçimi
    private JPanel buildModeSection() {
        modeGroup = new ButtonGroup();
        JPanel card = sectionCard("2b.  Measurement Mode", "Choose a pre-built domain.");

        rbHealth    = radioBtn("Health",    "Health management system scenarios (ready-made dataset)");
        rbEducation = radioBtn("Education", "Education LMS system scenarios (ready-made dataset)");

        modeGroup.add(rbHealth); modeGroup.add(rbEducation);
        rbHealth.setSelected(true);
        rbHealth.addActionListener(e    -> refreshScenarios());
        rbEducation.addActionListener(e -> refreshScenarios());

        card.add(rbHealth); card.add(Box.createVerticalStrut(8)); card.add(rbEducation);
        return card;
    }


    //2c senaryo seçimi
    private JPanel buildScenarioSection() {
        scenarioPanel = sectionCard("2c.  Scenario", "Select the specific scenario for this session.");
        refreshScenarios();
        return scenarioPanel;
    }

    private void refreshScenarios() {
        //güncel seçimleri oku
        String qt   = rbProcess != null && rbProcess.isSelected()   ? "Process"   : "Product";
        String mode = rbEducation != null && rbEducation.isSelected() ? "Education" : "Health";

        scenarioGroup  = new ButtonGroup();
        scenarioButtons.clear();

        while (scenarioPanel.getComponentCount() > 2) {
            scenarioPanel.remove(scenarioPanel.getComponentCount() - 1);
        }

        List<Scenario> scenarios = repo.getScenarios(qt, mode);
        if (scenarios.isEmpty()) {
            JLabel none = new JLabel("No scenarios available.");
            none.setFont(UITheme.FONT_BODY);
            none.setForeground(UITheme.DANGER);
            scenarioPanel.add(none);
        } else {
            boolean first = true;
            for (Scenario sc : scenarios) {
                JRadioButton rb = radioBtn(sc.getName(),
                        sc.getDimensions().size() + " dimension(s)  \u00B7  " + qt + "  \u00B7  " + mode);
                scenarioGroup.add(rb);
                scenarioButtons.add(rb);
                if (first) { rb.setSelected(true); first = false; }
                if (scenarioPanel.getComponentCount() > 2)
                    scenarioPanel.add(Box.createVerticalStrut(8));
                scenarioPanel.add(rb);
            }
        }
        scenarioPanel.revalidate();
        scenarioPanel.repaint();
    }


    private JPanel sectionCard(String title, String sub) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.BG_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2000));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(18, 24, 18, 24)));

        JLabel t = Step1ProfilePanel.label(title, UITheme.FONT_SUBTITLE, UITheme.ACCENT_LIGHT);
        JLabel s = Step1ProfilePanel.label(sub,   UITheme.FONT_SMALL,    UITheme.TEXT_MUTED);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(t); card.add(Box.createVerticalStrut(3)); card.add(s); card.add(Box.createVerticalStrut(12));
        return card;
    }

    private JRadioButton radioBtn(String title, String desc) {
        JRadioButton rb = new JRadioButton(
                "<html><b>" + title + "</b><br>"
                + "<font color='#7891af' size='3'>" + desc + "</font></html>");
        rb.setBackground(UITheme.BG_CARD);
        rb.setForeground(UITheme.TEXT_PRIMARY);
        rb.setFont(UITheme.FONT_BODY);
        rb.setAlignmentX(Component.LEFT_ALIGNMENT);
        rb.setIconTextGap(10);
        rb.setFocusPainted(false);
        rb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return rb;
    }


    //navigasyon butonları
    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        p.setBackground(UITheme.BG_DARK);
        p.setBorder(new EmptyBorder(14, 0, 0, 0));

        JButton back = Step1ProfilePanel.createSecondaryButton("\u2190 Back");
        back.addActionListener(e -> mainFrame.navigateTo(1));

        JButton next = Step1ProfilePanel.createAccentButton("Next \u2192");
        next.addActionListener(e -> onNext());

        p.add(back); p.add(next);
        return p;
    }

    /**
     "Next" butonuna tıklandığında çalışır
     seçilen kalite tipi, mod ve senaryo AppState'e kaydedilir
     senaryo seçilmemişse uyarı gösterilir
     */
    private void onNext() {
        String qt   = rbProcess.isSelected()   ? "Process"   : "Product";
        String mode = rbEducation.isSelected()  ? "Education" : "Health";

        Scenario selected = null;
        List<Scenario> scenarios = repo.getScenarios(qt, mode);
        for (int i = 0; i < scenarioButtons.size(); i++) {
            if (scenarioButtons.get(i).isSelected()) { selected = scenarios.get(i); break; }
        }

        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a scenario before proceeding.",
                    "Missing Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //seçimleri AppState e kaydet ve 3. adıma geç
        appState.setQualityType(qt);
        appState.setMode(mode);
        appState.setScenario(selected);
        appState.completeStep(2);
        mainFrame.navigateTo(3);
    }

    public void refresh() {
        if ("Process".equals(appState.getQualityType()))   rbProcess.setSelected(true);
        else                                                rbProduct.setSelected(true);
        if ("Education".equals(appState.getMode()))        rbEducation.setSelected(true);
        else                                               rbHealth.setSelected(true);
        refreshScenarios();
    }
}
