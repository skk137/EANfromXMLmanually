import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AciGUI extends JFrame {

    public static JTextArea logArea;
    private JScrollPane logScrollPane;

    public AciGUI() {
        setTitle("EAN Matcher");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JLabel title = new JLabel("EAN Matcher", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));


        JButton openFileBtn = new JButton("Επεξεργασία Αρχείου πρός ματσάρισμα");
        JButton matchNowBtn = new JButton("MATCH NOW");
        //JButton openTelikoFileBtn = new JButton("Προβολή Τελικού file");

        openFileBtn.addActionListener(e -> openTxtFile());
        matchNowBtn.addActionListener(e -> runMatcher());
        //openTelikoFileBtn.addActionListener(e -> openTxtFile()); //same με το openteliko που καταργήθηκε!

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setRows(15); // πόσες γραμμές φαίνονται οπτικά

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.add(title);
        panel.add(openFileBtn);
        panel.add(matchNowBtn);
        //panel.add(openTelikoFileBtn);

        // Log area
        logArea.setPreferredSize(new Dimension(400, 200)); // πλάτος x ύψος
        panel.add(new JScrollPane(logArea)); // scroll για τα logs

        add(panel);
    }

    private void openTxtFile() {
        try {
            File file = new File(System.getProperty("user.dir"), "aci.txt");

            // Αν δεν υπάρχει, το δημιουργούμε
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    if (logArea != null) logArea.append("Δημιουργήθηκε νέο αρχείο aci.txt\n");
                    else System.out.println("Δημιουργήθηκε νέο αρχείο aci.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "Το αρχείο aci.txt δεν βρέθηκε!");
                return;
            }
            Desktop.getDesktop().open(file);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα κατά το άνοιγμα του αρχείου.");
        }
    }

    private void runMatcher() {
        new Thread(() -> {
            try {
                logArea.append("Ξεκινάει το matching...\n");
                AciScraper.main(null);
                logArea.append("✅ Ολοκληρώθηκε! Το aci.txt ενημερώθηκε. Credits skk137. \n");
                logArea.append(" Για να δείτε το αρχέιο ξαναπατήστε, επεξεργασία αρχείου! Credits skk137. \n");
            } catch (Exception ex) {
                logArea.append("⚠️ Σφάλμα: " + ex.getMessage() + "\n");
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AciGUI().setVisible(true));
    }
}