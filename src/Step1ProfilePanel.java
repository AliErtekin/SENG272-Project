import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

//kullanıcıdan üç bilgi alır: kullanıcı adı okul adı ve oturum adı
//Next butonuna tıklanmadan önce tüm alanların dolu olması kontrol edilir
//eksik alan varsa kullanıcıya açıklayıcı bir uyarı mesajı gösterilir
public class Step1ProfilePanel extends JPanel {

    private final AppState  appState;
    private final MainFrame mainFrame;

    //veri girilen kutular
    private JTextField tfUsername;
    private JTextField tfSchool;
    private JTextField tfSession;

    public Step1ProfilePanel(AppState appState, MainFrame mainFrame) {
        this.appState  = appState;
        this.mainFrame = mainFrame;
        buildUI();//arayüzü olustur
    }

    //üst başlık, orta form, alt butonlar
    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);
        setBorder(new EmptyBorder(30, 60, 30, 60));
        add(buildHeader(),  BorderLayout.NORTH);
        add(buildForm(),    BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
    }

    //sayfa başlığı ve açıklama metnini oluşturur
    private JPanel buildHeader() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UITheme.BG_DARK);
        p.setBorder(new EmptyBorder(0, 0, 28, 0));

        //başlık ve alt başlık etiketleri sola hizalanır
        JLabel title = label("User Profile",  UITheme.FONT_TITLE,    UITheme.TEXT_PRIMARY);
        JLabel sub   = label("Enter your information to begin the measurement session.",
                              UITheme.FONT_BODY, UITheme.TEXT_MUTED);

        for (JLabel l : new JLabel[]{title, sub}) {
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            p.add(l);
            p.add(Box.createVerticalStrut(4));
        }
        return p;
    }

    private JPanel buildForm() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets  = new Insets(10, 0, 10, 0);
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.gridx   = 0;

        gc.gridy = 0; card.add(buildFieldRow("Username",     ""),                       gc);
        gc.gridy = 1; card.add(buildFieldRow("School",       ""),  gc);
        gc.gridy = 2; card.add(buildFieldRow("Session Name", ""),       gc);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(UITheme.BG_DARK);
        wrap.setBorder(new EmptyBorder(0, 0, 20, 0));
        wrap.add(card, BorderLayout.NORTH);
        return wrap;
    }

    private JPanel buildFieldRow(String labelText, String placeholder) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setBackground(UITheme.BG_CARD);

        JLabel lbl = label(labelText, UITheme.FONT_SUBTITLE, UITheme.TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField tf = createTextField(placeholder);
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);

        if      (labelText.equals("Username"))     tfUsername = tf;
        else if (labelText.equals("School"))       tfSchool   = tf;
        else if (labelText.equals("Session Name")) tfSession  = tf;

        row.add(lbl);
        row.add(Box.createVerticalStrut(5));
        row.add(tf);
        return row;
    }

    private JTextField createTextField(String placeholder) {
        JTextField tf = new JTextField(30) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(UITheme.TEXT_MUTED);
                    g2.setFont(UITheme.FONT_BODY.deriveFont(Font.ITALIC));
                    Insets ins = getInsets();
                    g2.drawString(placeholder, ins.left + 2,
                            getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
                    g2.dispose();
                }
            }
        };
        tf.setBackground(new Color(20, 32, 50));
        tf.setForeground(UITheme.TEXT_PRIMARY);
        tf.setCaretColor(UITheme.ACCENT_LIGHT);
        tf.setFont(UITheme.FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(8, 10, 8, 10)));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return tf;
    }

    //Next butonunu içeren alt paneli oluşturur
    private JPanel buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        p.setBackground(UITheme.BG_DARK);
        JButton next = createAccentButton("Next \u2192");
        next.addActionListener(e -> onNext());
        p.add(next);
        return p;
    }

    /**
     Next butonuna tıklandığında çalışır
     tüm alanlar doluysa profili AppState'e kaydeder ve 2. adıma geçer
     eksik alan varsa hangi alanın boş olduğunu belirten uyarı gösterir
     */
    private void onNext() {
        String username = tfUsername.getText().trim();
        String school   = tfSchool.getText().trim();
        String session  = tfSession.getText().trim();

        if (username.isEmpty()) { warn("Please enter your username to continue.");    tfUsername.requestFocus(); return; }
        if (school.isEmpty())   { warn("Please enter your school name to continue."); tfSchool.requestFocus();   return; }
        if (session.isEmpty())  { warn("Please enter a session name to continue.");   tfSession.requestFocus();  return; }

        //profili kaydet, adımı tamamlandı işaretle, 2. adıma geç

        appState.setProfile(new Profile(username, school, session));
        appState.completeStep(1);
        mainFrame.navigateTo(2);
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Missing Information", JOptionPane.WARNING_MESSAGE);
    }

    public void refresh() {
        Profile p = appState.getProfile();
        if (p != null && !p.getUsername().isEmpty()) {
            tfUsername.setText(p.getUsername());
            tfSchool.setText(p.getSchool());
            tfSession.setText(p.getSessionName());
        }
    }


    static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    static JButton createAccentButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? UITheme.ACCENT_LIGHT : UITheme.ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleBtn(btn, UITheme.TEXT_PRIMARY);
        return btn;
    }

    static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? UITheme.BORDER : UITheme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(UITheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleBtn(btn, UITheme.TEXT_MUTED);
        return btn;
    }

    private static void styleBtn(JButton btn, Color fg) {
        btn.setFont(UITheme.FONT_SUBTITLE);
        btn.setForeground(fg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 40));
    }
}
