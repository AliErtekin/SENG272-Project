import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

//3.adım
public class Step3PlanPanel extends JPanel {

    private final AppState  appState;
    private final MainFrame mainFrame;
    private JPanel contentPanel;

    public Step3PlanPanel(AppState appState, MainFrame mainFrame) {
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

        JLabel title = Step1ProfilePanel.label("Plan Measurement", UITheme.FONT_TITLE, UITheme.TEXT_PRIMARY);
        JLabel sub   = Step1ProfilePanel.label("Review dimensions and metrics for the selected scenario. (Read-only)",
                UITheme.FONT_BODY, UITheme.TEXT_MUTED);

        for (JLabel l : new JLabel[]{title, sub}) {
            l.setAlignmentX(Component.LEFT_ALIGNMENT); p.add(l); p.add(Box.createVerticalStrut(4));
        }
        return p;
    }

    // Geri ileri navigasyon butonlarını oluşturma
    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        p.setBackground(UITheme.BG_DARK);
        p.setBorder(new EmptyBorder(14, 0, 0, 0));

        JButton back = Step1ProfilePanel.createSecondaryButton("\u2190 Back");
        back.addActionListener(e -> mainFrame.navigateTo(2));

        JButton next = Step1ProfilePanel.createAccentButton("Next \u2192");
        next.addActionListener(e -> { appState.completeStep(3); mainFrame.navigateTo(4); });

        p.add(back); p.add(next);
        return p;
    }

    public void refresh() {
        contentPanel.removeAll();
        Scenario scenario = appState.getScenario();
        if (scenario == null) {
            contentPanel.add(errLabel("No scenario selected. Please go back to Step 2."));
            contentPanel.revalidate(); contentPanel.repaint();
            return;
        }

        contentPanel.add(buildBadge(scenario));
        contentPanel.add(Box.createVerticalStrut(14));

        for (QDimension dim : scenario.getDimensions()) {
            contentPanel.add(buildDimSection(dim));
            contentPanel.add(Box.createVerticalStrut(12));
        }
        contentPanel.revalidate(); contentPanel.repaint();
    }

    private JPanel buildBadge(Scenario s) {
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        badge.setBackground(new Color(30, 60, 100));
        badge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.ACCENT, 1),
                new EmptyBorder(3, 8, 3, 8)));
        badge.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        addChip(badge, "Scenario",   s.getName(),                                UITheme.ACCENT);
        addChip(badge, "Type",       s.getQualityType(),                         UITheme.TEXT_MUTED);
        addChip(badge, "Mode",       s.getMode(),                                UITheme.TEXT_MUTED);
        addChip(badge, "Dimensions", String.valueOf(s.getDimensions().size()),   UITheme.SUCCESS);
        return badge;
    }

    private void addChip(JPanel p, String k, String v, Color vc) {
        JLabel lk = new JLabel(k + ": "); lk.setFont(UITheme.FONT_SMALL); lk.setForeground(UITheme.TEXT_MUTED);
        JLabel lv = new JLabel(v + "  "); lv.setFont(UITheme.FONT_SMALL.deriveFont(Font.BOLD)); lv.setForeground(vc);
        p.add(lk); p.add(lv);
    }

    private JPanel buildDimSection(QDimension dim) {
        JPanel sec = new JPanel();
        sec.setLayout(new BoxLayout(sec, BoxLayout.Y_AXIS));
        sec.setBackground(UITheme.BG_CARD);
        sec.setAlignmentX(Component.LEFT_ALIGNMENT);
        sec.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2000));
        sec.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(14, 18, 14, 18)));

        JPanel hdr = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hdr.setBackground(UITheme.BG_CARD);
        hdr.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLbl  = Step1ProfilePanel.label(dim.getName() + "  ", UITheme.FONT_SUBTITLE, UITheme.ACCENT_LIGHT);
        JLabel coeffLbl = new JLabel("Coefficient: " + dim.getCoefficient());
        coeffLbl.setFont(UITheme.FONT_SMALL);
        coeffLbl.setForeground(UITheme.WARNING);
        coeffLbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.WARNING, 1), new EmptyBorder(2, 6, 2, 6)));

        hdr.add(nameLbl); hdr.add(coeffLbl);
        sec.add(hdr);
        sec.add(Box.createVerticalStrut(10));
        sec.add(buildMetricTable(dim.getMetrics()));
        return sec;
    }

    private JScrollPane buildMetricTable(List<Metric> metrics) {
        String[] cols = {"Metric", "Coefficient", "Direction", "Range", "Unit"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Metric m : metrics) {
            model.addRow(new Object[]{m.getName(), m.getCoefficient(),
                    m.getDirectionLabel(), m.getRangeLabel(), m.getUnit()});
        }

        JTable table = createStyledTable(model);
        int[] w = {190, 100, 120, 100, 80};
        for (int i = 0; i < w.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < 5; i++) table.getColumnModel().getColumn(i).setCellRenderer(center);

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new java.awt.Dimension(0, metrics.size() * 30 + 30));
        sp.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, metrics.size() * 30 + 30));
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleScrollPane(sp);
        return sp;
    }


    static JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                c.setBackground(row % 2 == 0 ? UITheme.BG_CARD : UITheme.TABLE_ROW_ALT);
                c.setForeground(UITheme.TEXT_PRIMARY);
                ((JComponent) c).setBorder(new EmptyBorder(6, 10, 6, 10));
                return c;
            }
        };
        table.setBackground(UITheme.BG_CARD);
        table.setForeground(UITheme.TEXT_PRIMARY);
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new java.awt.Dimension(0, 1));
        table.setSelectionBackground(UITheme.ACCENT.darker());
        table.setSelectionForeground(Color.WHITE);
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(UITheme.TABLE_HEADER);
        header.setForeground(UITheme.ACCENT_LIGHT);
        header.setFont(UITheme.FONT_SUBTITLE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));
        header.setReorderingAllowed(false);
        return table;
    }

    static void styleScrollPane(JScrollPane sp) {
        sp.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        sp.setBackground(UITheme.BG_CARD);
        sp.getViewport().setBackground(UITheme.BG_CARD);
    }

    private JLabel errLabel(String msg) {
        JLabel l = new JLabel(msg);
        l.setForeground(UITheme.DANGER);
        l.setFont(UITheme.FONT_BODY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}
