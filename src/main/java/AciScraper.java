import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AciScraper {
    public static void main(String[] args) throws Exception {

        File file = new File("aci.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> codes = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                codes.add(line.trim());
            }
        }
        br.close();

        Pattern eanPattern = Pattern.compile("EAN:\\s*([0-9]+)");


        List<String> results = new ArrayList<>();
        for (String code : codes){
            String searchUrl = "https://www.acihellas.gr/search?adv=false&cid=0&q=" + code + "&sid=true&isc=true";
            String ean = "NOT FOUND";
            try {
                Document doc = Jsoup.connect(searchUrl) // Τραβάει ολο το κείμενο απο την σελ. και το περνάει στο doc
                        .userAgent("Mozilla/5.0")
                        .timeout(15000)
                        .get();

                Matcher m = eanPattern.matcher(doc.text()); // ΔΗΜΙΟΥΡΓΕΊ Matcher, κάνοντας χρήση το txt
                if (m.find()) {
                    ean = m.group(1);
                }
            } catch (Exception e) {
                ean = "ERROR";
            }

            results.add(code + ";" + ean); //ωστε να σπάσει μετα σε excel transformation. (Update να γίνει apply εκει απευθείας)
            System.out.println(code + " -> " + ean);
        }

        //Πατάμε(γράφουμε) πάνω στο ίδιο αρχείο
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (String res : results) {
            bw.write(res);
            bw.newLine();
        }
        bw.close();

        System.out.println("Ολοκληρώθηκε! Το aci.txt ενημερώθηκε με barcodes. Credits skk137");

    }

}
