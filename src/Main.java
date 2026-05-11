import javax.swing.SwingUtilities;
import javax.swing.UIManager;

//kodun çalıştıgı yer
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
