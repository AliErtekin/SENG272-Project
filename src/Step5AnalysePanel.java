import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

//5.adım
public class Step5AnalysePanel extends JPanel {

    private final AppState  appState;
    private final MainFrame mainFrame;
    private JPanel contentPanel;

    public Step5AnalysePanel(AppState appState, MainFrame mainFrame) {
        this.appState  = appState;
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);
        setBorder(new EmptyBorder(24, 50, 24, 50));
        add(buildHeader(), BorderLayout.NORTH);

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

        JLabel title = Step1ProfilePanel.label("Analyse Results", UITheme.FONT_TITLE, UITheme.TEXT_PRIMARY);
        JLabel sub   = Step1ProfilePanel.label(
                "Weighted averages, radar chart, and gap analysis for the selected scenario.",
                UITheme.FONT_BODY, UITheme.TEXT_MUTED);

        for (JLabel l : new JLabel[]{title, sub}) {
            l.setAlignmentX(Component.LEFT_ALIGNMENT); p.add(l); p.add(Box.createVerticalStrut(4));
        }
        return p;
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        p.setBackground(UITheme.BG_DARK);
        p.setBorder(new EmptyBorder(14, 0, 0, 0));

        JButton back = Step1ProfilePanel.createSecondaryButton("\u2190 Back");
        back.addActionListener(e -> mainFrame.navigateTo(4));

        JButton restart = Step1ProfilePanel.createAccentButton("\uD83D\uDD04 Restart");
        restart.addActionListener(e -> mainFrame.restart());

        p.add(back); p.add(restart);
        return p;
    }


    public void refresh() {
        contentPanel.removeAll();
        appState.completeStep(5);

        Scenario scenario = appState.getScenario();
        if (scenario == null) {
            contentPanel.add(Step1ProfilePanel.label(
                    "No scenario selected. Go back to Step 2.", UITheme.FONT_BODY, UITheme.DANGER));
            contentPanel.revalidate(); contentPanel.repaint();
            return;
        }

        List<QDimension> dims = scenario.getDimensions();

        contentPanel.add(sectionTitle("5a.  Dimension-Based Weighted Averages",
                "Each bar shows the weighted average score (1\u20135) for that dimension."));
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(buildDimBars(dims));
        contentPanel.add(Box.createVerticalStrut(18));

        contentPanel.add(sectionTitle("5b.  Radar Chart",
                "Spider chart showing the balance of quality across all dimensions."));
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(buildRadarWrapper(dims));
        contentPanel.add(Box.createVerticalStrut(18));

        contentPanel.add(sectionTitle("5c.  Gap Analysis",
                "The dimension furthest from the target score of 5.0 is highlighted."));
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(buildGapAnalysis(dims));

        contentPanel.revalidate(); contentPanel.repaint();
    }


    private JPanel buildDimBars(List<QDimension> dims) {
        JPanel card = card();
        for (QDimension dim : dims) {
            card.add(buildBar(dim.getName(), dim.getCoefficient(), dim.calculateScore()));
            card.add(Box.createVerticalStrut(14));
        }
        return card;
    }

    private JPanel buildBar(String name, int coeff, double score) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(UITheme.BG_CARD);
        row.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 50));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel labels = new JPanel(new GridLayout(2, 1));
        labels.setBackground(UITheme.BG_CARD);
        labels.setPreferredSize(new java.awt.Dimension(210, 44));

        JLabel lName  = Step1ProfilePanel.label(name, UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY);
        JLabel lCoeff = Step1ProfilePanel.label(
                "Coefficient: " + coeff + "  |  Score: " + String.format("%.2f", score) + " / 5.00",
                UITheme.FONT_SMALL, UITheme.TEXT_MUTED);
        labels.add(lName); labels.add(lCoeff);
        row.add(labels, BorderLayout.WEST);

        int percent = (int) Math.round((score - 1.0) / 4.0 * 100);
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(Math.max(0, Math.min(100, percent)));
        bar.setStringPainted(false);
        bar.setBackground(UITheme.TABLE_ROW_ALT);
        bar.setForeground(Step4CollectPanel.scoreColor(score));
        bar.setBorder(null);
        bar.setPreferredSize(new java.awt.Dimension(0, 22));
        row.add(bar, BorderLayout.CENTER);

        JLabel badge = Step1ProfilePanel.label(String.format(" %.2f ", score),
                UITheme.FONT_SUBTITLE, Step4CollectPanel.scoreColor(score));
        badge.setPreferredSize(new java.awt.Dimension(56, 44));
        badge.setHorizontalAlignment(JLabel.RIGHT);
        row.add(badge, BorderLayout.EAST);

        return row;
    }


    private JPanel buildRadarWrapper(List<QDimension> dims) {
        JPanel outer = card();
        RadarChartPanel radar = new RadarChartPanel(dims);
        radar.setAlignmentX(Component.LEFT_ALIGNMENT);
        radar.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 380));
        outer.add(radar);
        return outer;
    }


    private JPanel buildGapAnalysis(List<QDimension> dims) {
        QDimension worst = dims.get(0);
        for (QDimension d : dims) {
            if (d.calculateScore() < worst.calculateScore()) worst = d;
        }

        double score = worst.calculateScore();
        double gap   = 5.0 - score;
        String label = qualityLabel(score);

        JPanel card = card();

        JPanel banner = new JPanel(new BorderLayout(16, 0));
        banner.setBackground(bannerColor(score));
        banner.setBorder(new EmptyBorder(16, 20, 16, 20));
        banner.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 80));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel bannerIcon = new JLabel(gapIcon(score));
        bannerIcon.setFont(new Font("SansSerif", Font.PLAIN, 34));

        JPanel bannerTxt = new JPanel();
        bannerTxt.setLayout(new BoxLayout(bannerTxt, BoxLayout.Y_AXIS));
        bannerTxt.setOpaque(false);
        JLabel bt = Step1ProfilePanel.label("Lowest Dimension: " + worst.getName(),
                UITheme.FONT_SUBTITLE, Color.WHITE);
        JLabel bs = Step1ProfilePanel.label(
                "This dimension has the lowest score and requires the most improvement.",
                UITheme.FONT_SMALL, new Color(220, 220, 220));
        bannerTxt.add(bt); bannerTxt.add(bs);

        banner.add(bannerIcon, BorderLayout.WEST);
        banner.add(bannerTxt,  BorderLayout.CENTER);
        card.add(banner);
        card.add(Box.createVerticalStrut(16));

        JPanel grid = new JPanel(new GridLayout(1, 4, 12, 0));
        grid.setBackground(UITheme.BG_CARD);
        grid.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 80));
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        grid.add(gapCell("Dimension",     worst.getName(),                    UITheme.TEXT_PRIMARY));
        grid.add(gapCell("Score",         String.format("%.2f / 5.00", score),Step4CollectPanel.scoreColor(score)));
        grid.add(gapCell("Gap to 5.0",   String.format("%.2f", gap),         UITheme.DANGER));
        grid.add(gapCell("Quality Level", label,                              labelColor(label)));
        card.add(grid);

        return card;
    }

    private JPanel gapCell(String key, String value, Color vc) {
        JPanel cell = new JPanel();
        cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
        cell.setBackground(UITheme.TABLE_ROW_ALT);
        cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(10, 14, 10, 14)));
        JLabel lk = Step1ProfilePanel.label(key,   UITheme.FONT_SMALL,    UITheme.TEXT_MUTED);
        JLabel lv = Step1ProfilePanel.label(value, UITheme.FONT_SUBTITLE, vc);
        lk.setAlignmentX(Component.LEFT_ALIGNMENT);
        lv.setAlignmentX(Component.LEFT_ALIGNMENT);
        cell.add(lk); cell.add(Box.createVerticalStrut(4)); cell.add(lv);
        return cell;
    }


    private JPanel sectionTitle(String title, String sub) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UITheme.BG_DARK);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel t = Step1ProfilePanel.label(title, UITheme.FONT_SUBTITLE, UITheme.ACCENT_LIGHT);
        JLabel s = Step1ProfilePanel.label(sub,   UITheme.FONT_SMALL,    UITheme.TEXT_MUTED);
        t.setAlignmentX(Component.LEFT_ALIGNMENT); s.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(t); p.add(Box.createVerticalStrut(2)); p.add(s);
        return p;
    }

    private JPanel card() {
        JPanel c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBackground(UITheme.BG_CARD);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 2000));
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(18, 22, 18, 22)));
        return c;
    }

    private String qualityLabel(double score) {
        if (score >= 4.5) return "Excellent";
        if (score >= 3.5) return "Good";
        if (score >= 2.5) return "Needs Improvement";
        return "Poor";
    }

    private Color labelColor(String label) {
        switch (label) {
            case "Excellent":         return UITheme.SUCCESS;
            case "Good":              return new Color(100, 200, 120);
            case "Needs Improvement": return UITheme.WARNING;
            default:                  return UITheme.DANGER;
        }
    }

    private Color bannerColor(double score) {
        if (score >= 4.5) return new Color(21, 100, 50);
        if (score >= 3.5) return new Color(40, 100, 60);
        if (score >= 2.5) return new Color(130, 80,  10);
        return new Color(140, 20, 20);
    }

    private String gapIcon(double score) {
        if (score >= 4.5) return "\u2705";
        if (score >= 3.5) return "\uD83D\uDFE2";
        if (score >= 2.5) return "\uD83D\uDFE1";
        return "\uD83D\uDD34";
    }

    static class RadarChartPanel extends JPanel {

        private final List<QDimension> dims;

        RadarChartPanel(List<QDimension> dims) {
            this.dims = dims;
            setBackground(UITheme.BG_CARD);
            setPreferredSize(new java.awt.Dimension(600, 360));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int n = dims.size();
            if (n < 3) { g2.setColor(UITheme.TEXT_MUTED); g2.setFont(UITheme.FONT_BODY);
                         g2.drawString("Need \u22653 dimensions for radar chart.", 20, getHeight() / 2);
                         g2.dispose(); return; }

            int w = getWidth(), h = getHeight();
            int cx = w / 2, cy = h / 2 + 10;
            int R = Math.min(w, h) / 2 - 60;
            int LEVELS = 4;

            g2.setStroke(new BasicStroke(0.8f));
            for (int lvl = 1; lvl <= LEVELS; lvl++) {
                double ratio = lvl / (double) LEVELS;
                int[] xs = new int[n], ys = new int[n];
                for (int i = 0; i < n; i++) {
                    double angle = Math.toRadians(90 + i * 360.0 / n);
                    xs[i] = cx + (int) (ratio * R * Math.cos(angle));
                    ys[i] = cy - (int) (ratio * R * Math.sin(angle));
                }
                g2.setColor(UITheme.BORDER);
                g2.drawPolygon(xs, ys, n);
                double labelScore = 1.0 + (lvl / (double) LEVELS) * 4.0;
                g2.setFont(UITheme.FONT_SMALL); g2.setColor(UITheme.TEXT_MUTED);
                g2.drawString(String.format("%.0f", labelScore), cx + 4, cy - (int) (ratio * R) + 4);
            }

            g2.setColor(UITheme.BORDER); g2.setStroke(new BasicStroke(0.8f));
            for (int i = 0; i < n; i++) {
                double angle = Math.toRadians(90 + i * 360.0 / n);
                g2.drawLine(cx, cy, cx + (int) (R * Math.cos(angle)), cy - (int) (R * Math.sin(angle)));
            }

            int[] dxs = new int[n], dys = new int[n];
            for (int i = 0; i < n; i++) {
                double score = dims.get(i).calculateScore();
                double ratio = (score - 1.0) / 4.0;
                double angle = Math.toRadians(90 + i * 360.0 / n);
                dxs[i] = cx + (int) (ratio * R * Math.cos(angle));
                dys[i] = cy - (int) (ratio * R * Math.sin(angle));
            }

            g2.setColor(new Color(56, 142, 255, 60));
            g2.fillPolygon(dxs, dys, n);
            g2.setColor(UITheme.ACCENT);
            g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawPolygon(dxs, dys, n);

            for (int i = 0; i < n; i++) {
                g2.setColor(UITheme.ACCENT_LIGHT); g2.fillOval(dxs[i]-5, dys[i]-5, 10, 10);
                g2.setColor(Color.WHITE);           g2.drawOval(dxs[i]-5, dys[i]-5, 10, 10);
            }

            for (int i = 0; i < n; i++) {
                double score = dims.get(i).calculateScore();
                double angle = Math.toRadians(90 + i * 360.0 / n);
                int lx = cx + (int) ((R + 28) * Math.cos(angle));
                int ly = cy - (int) ((R + 28) * Math.sin(angle));

                String dimName  = dims.get(i).getName();
                String scoreStr = String.format("(%.2f)", score);

                g2.setFont(UITheme.FONT_SMALL.deriveFont(Font.BOLD));
                FontMetrics fm = g2.getFontMetrics();
                int ox = -fm.stringWidth(dimName) / 2;
                if (Math.cos(angle) < -0.1) ox -= 8;

                g2.setColor(UITheme.TEXT_PRIMARY); g2.drawString(dimName, lx + ox, ly - 2);
                g2.setFont(UITheme.FONT_SMALL); g2.setColor(Step4CollectPanel.scoreColor(score));
                g2.drawString(scoreStr, lx + ox, ly + 13);
            }

            g2.setColor(UITheme.ACCENT); g2.fillOval(cx - 4, cy - 4, 8, 8);
            g2.dispose();
        }
    }
}
