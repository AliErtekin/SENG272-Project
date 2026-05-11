import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

//adım 4 veri toplama
public class Step4CollectPanel extends JPanel {

    private final AppState  appState;
    private final MainFrame mainFrame;
    private JPanel contentPanel;

    public Step4CollectPanel(AppState appState, MainFrame mainFrame) {
        this.appState  = appState;
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);
        setBorder(new EmptyBorder(24, 50, 24, 50));
        add(buildHeader(),  BorderLayout.NORTH);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UITheme.BG_DARK);

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBackground(UITheme.BG_DARK);
        scroll.getViewport().setBackground(UITheme.BG_DARK);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UITheme.BG_DARK);
        p.setBorder(new EmptyBorder(0, 0, 18, 0));

        JLabel title = Step1ProfilePanel.label("Collect Data", UITheme.FONT_TITLE, UITheme.TEXT_PRIMARY);
        JLabel sub   = Step1ProfilePanel.label(
                "Collected values are shown below. Scores (1\u20135) are calculated automatically.",
                UITheme.FONT_BODY, UITheme.TEXT_MUTED);

        for (JLabel l : new JLabel[]{title, sub}) {
            l.setAlignmentX(Component.LEFT_ALIGNMENT); p.add(l); p.add(Box.createVerticalStrut(4));
        }
        return p;
    }

    //3. adım ve 5. adım navigasyon butonları
    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        p.setBackground(UITheme.BG_DARK);
        p.setBorder(new EmptyBorder(14, 0, 0, 0));

        JButton back = Step1ProfilePanel.createSecondaryButton("\u2190 Back");
        back.addActionListener(e -> mainFrame.navigateTo(3));

        JButton next = Step1ProfilePanel.createAccentButton("Analyse \u2192");
        next.addActionListener(e -> { appState.completeStep(4); mainFrame.navigateTo(5); });

        p.add(back); p.add(next);
        return p;
    }

    public void refresh() {
        contentPanel.removeAll();
        Scenario scenario = appState.getScenario();
        if (scenario == null) {
            JLabel err = Step1ProfilePanel.label(
                    "No scenario selected. Please go back to Step 2.", UITheme.FONT_BODY, UITheme.DANGER);
            contentPanel.add(err);
            contentPanel.revalidate(); contentPanel.repaint();
            return;
        }

        contentPanel.add(buildInfoBox(scenario));
        contentPanel.add(Box.createVerticalStrut(14));
        contentPanel.add(buildFormulaBox());
        contentPanel.add(Box.createVerticalStrut(14));

        for (QDimension dim : scenario.getDimensions()) {
            contentPanel.add(buildDimBlock(dim));
            contentPanel.add(Box.createVerticalStrut(12));
        }
        contentPanel.revalidate(); contentPanel.repaint();
    }

    private JPanel buildInfoBox(Scenario s) {
        JPanel box = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        box.setBackground(new Color(20, 50, 80));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.ACCENT, 1),
                new EmptyBorder(3, 8, 3, 8)));
        box.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 44));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        addChip(box, "Scenario", s.getName(), UITheme.ACCENT_LIGHT);
        addChip(box, "Type",     s.getQualityType(), UITheme.TEXT_MUTED);
        addChip(box, "Mode",     s.getMode(),        UITheme.TEXT_MUTED);
        return box;
    }

    private void addChip(JPanel p, String k, String v, Color vc) {
        JLabel lk = new JLabel(k + ": "); lk.setFont(UITheme.FONT_SMALL); lk.setForeground(UITheme.TEXT_MUTED);
        JLabel lv = new JLabel(v + "   "); lv.setFont(UITheme.FONT_SMALL.deriveFont(Font.BOLD)); lv.setForeground(vc);
        p.add(lk); p.add(lv);
    }

    private JPanel buildFormulaBox() {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(20, 38, 28));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.SUCCESS.darker(), 1),
                new EmptyBorder(10, 16, 10, 16)));
        box.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 80));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = Step1ProfilePanel.label("Score Calculation Formula", UITheme.FONT_SUBTITLE, UITheme.SUCCESS);
        //yüksek değer iyiyse uygulanan formül
        JLabel f1 = Step1ProfilePanel.label(
                "  Higher is better:  score = 1 + (value \u2212 min) / (max \u2212 min) \u00D7 4",
                UITheme.FONT_MONO, UITheme.TEXT_MUTED);
        //düşük değer iyiyse uygulanan formül
        JLabel f2 = Step1ProfilePanel.label(
                "  Lower is better :  score = 5 \u2212 (value \u2212 min) / (max \u2212 min) \u00D7 4",
                UITheme.FONT_MONO, UITheme.TEXT_MUTED);
        for (JLabel l : new JLabel[]{title, f1, f2}) {
            l.setAlignmentX(Component.LEFT_ALIGNMENT); box.add(l); box.add(Box.createVerticalStrut(3));
        }
        return box;
    }

    private JPanel buildDimBlock(QDimension dim) {
        JPanel sec = new JPanel();
        sec.setLayout(new BoxLayout(sec, BoxLayout.Y_AXIS));
        sec.setBackground(UITheme.BG_CARD);
        sec.setAlignmentX(Component.LEFT_ALIGNMENT);
        sec.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 2000));
        sec.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(14, 18, 14, 18)));

        JPanel hdr = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        hdr.setBackground(UITheme.BG_CARD);
        hdr.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLbl = Step1ProfilePanel.label(dim.getName() + "  ", UITheme.FONT_SUBTITLE, UITheme.ACCENT_LIGHT);
        JLabel coeffLbl = new JLabel("Coefficient: " + dim.getCoefficient());
        coeffLbl.setFont(UITheme.FONT_SMALL); coeffLbl.setForeground(UITheme.WARNING);
        coeffLbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.WARNING, 1), new EmptyBorder(2, 6, 2, 6)));

        double dimScore = dim.calculateScore();
        JLabel scoreLbl = new JLabel(String.format("  Dim. Score: %.2f", dimScore));
        scoreLbl.setFont(UITheme.FONT_SMALL.deriveFont(Font.BOLD));
        scoreLbl.setForeground(scoreColor(dimScore));

        hdr.add(nameLbl); hdr.add(coeffLbl); hdr.add(scoreLbl);
        sec.add(hdr);
        sec.add(Box.createVerticalStrut(10));
        sec.add(buildCollectTable(dim.getMetrics()));
        return sec;
    }

    private JScrollPane buildCollectTable(List<Metric> metrics) {
        String[] cols = {"Metric", "Direction", "Range", "Value", "Score (1\u20135)", "Coeff / Unit"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Metric m : metrics) {
            double score = m.calculateScore();
            model.addRow(new Object[]{
                m.getName(), m.getDirectionLabel(), m.getRangeLabel(),
                m.getValueLabel(), formatScore(score),
                m.getCoefficient() + " / " + m.getUnit()
            });
        }

        JTable table = Step3PlanPanel.createStyledTable(model);
        table.getColumnModel().getColumn(4).setCellRenderer(new ScoreCellRenderer());

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < 6; i++) {
            if (i != 4) table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        int[] w = {170, 120, 100, 70, 100, 110};
        for (int i = 0; i < w.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new java.awt.Dimension(0, metrics.size() * 30 + 30));
        sp.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, metrics.size() * 30 + 30));
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        Step3PlanPanel.styleScrollPane(sp);
        return sp;
    }

    private String formatScore(double s) {
        return (s == (int) s) ? String.valueOf((int) s) : String.valueOf(s);
    }

    /**
     skora göre renk döndürür
     yüksek skor yeşil, orta sarı, düşük kırmızı tonu
     step5AnalysePanel tarafından da kullanılır
     */
    static Color scoreColor(double score) {
        if (score >= 4.5) return UITheme.SUCCESS;
        if (score >= 3.5) return new Color(100, 200, 120);
        if (score >= 2.5) return UITheme.WARNING;
        if (score >= 1.5) return new Color(255, 140, 40);
        return UITheme.DANGER;
    }


    private static class ScoreCellRenderer extends DefaultTableCellRenderer {
        ScoreCellRenderer() { setHorizontalAlignment(JLabel.CENTER); }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            double score = 3.0;
            try { score = Double.parseDouble(String.valueOf(v)); } catch (NumberFormatException ignored) {}
            setBackground(row % 2 == 0 ? UITheme.BG_CARD : UITheme.TABLE_ROW_ALT);
            setForeground(scoreColor(score));
            setFont(UITheme.FONT_BODY.deriveFont(Font.BOLD));
            setBorder(new EmptyBorder(6, 10, 6, 10));
            return this;
        }
    }
}
