package tableformat;

import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class TableFormat {

    private static final DecimalFormat format = new DecimalFormat("#0.000000");
    
    private static boolean multiply = false;
    private static double multiplier = 0.0;
    
    public static void main(String[] args) throws Exception {        
        if (args.length < 2) {
            System.out.println("Usage: format <input> <output> [multiplier]");
            System.exit(0);
        }

        for (String arg : args) {
            System.out.println(arg);
        }
        
        if(args.length > 2) {
            multiply = true;
            multiplier = Double.parseDouble(args[2]);
        }

        BufferedReader in = new BufferedReader(new FileReader(new File(args[0])));
        PrintStream ps = new PrintStream(new FileOutputStream(new File(args[1])));

        ArrayList<Double> head = new ArrayList<>();

        RowSortedTable<Integer, Integer, Double> t = TreeBasedTable.create();

        String ln;
        Scanner s;

        int cols;
        int r = 0;
        int c = 0;

        while (true) {
            ln = in.readLine();

            if (ln == null || ln.trim().isEmpty()) {
                break;
            }
            s = new Scanner(ln).useLocale(Locale.US);
            cols = 0;
            while (s.hasNextInt()) {
                s.nextInt();
                cols++;
            }

            in.readLine();
            ln = in.readLine();
            s = new Scanner(ln).useLocale(Locale.US);

            for (int i = 0; i < cols; i++) {
                head.add(s.nextDouble());
            }
            in.readLine();

            r = 0;
            for (;;) {
                ln = in.readLine();
                if (ln == null || ln.trim().isEmpty()) {
                    break;
                }
                r++;
                s = new Scanner(ln).useLocale(Locale.US);
                s.nextInt();

                for (int j = 0; j < cols; j++) {
                    double d = s.nextDouble();
                    t.put(r - 1, c + j, d);
                }
            }
            c += cols;
        }

        for (int j = 0; j < r; j++) {
            ps.print("\t" + (j + 1));
        }

        ps.println();

        for (int i = c - 1; i >= 0; i--) {
            ps.print(i + 1);
            ps.print("\t");
            for (int j = 0; j < r; j++) {
                double d = t.get(j, i);
                if(multiply) {
                    d *= multiplier;
                }
                ps.print(format.format(d));
                ps.print("\t");
            }
            ps.println();
        }

        ps.flush();
        ps.close();
        System.out.println("Done");
    }

}
