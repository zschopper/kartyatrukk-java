package kartya;

import java.util.Random;
import java.util.Scanner;

public class Jatek {

    private String[] pakli;
    private int oszlop = 0;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_ORANGE = "\033[38;5;208m";

    private static String[] szamok = { "VII", "VIII", "IX", "X", "A", "F", "K" };
    // private static String[] figurak = { "t", "z", "p", "m" }; // Tök, Zöld, Piros, Makk
    private static String[] figurak = { "T", "Z", "P", "M" }; // Tök, Zöld, Piros, Makk
    private static Scanner sc = new Scanner(System.in);
    private static Random rnd = new Random();

    public static void main(String[] args) {
        new Jatek();
    }

    // Visszaadja, hogy a megadott kártyalap hányadik elem a pakliban.
    // Ha a tömb még feltöltetlen, az első null elemnél abbahagyja a keresést.
    private int index(String kartyalap) {

        for (int i = 0; i < this.pakli.length; i++) {
            if (this.pakli[i] == null) {
                return -1;
            }
            if (this.pakli[i].equals(kartyalap)) {
                return i;
            }
        }
        return -1;
    }

    public void kezd() {
        System.out.println(ANSI_BLUE + "\n\nVálassz egy kártyát, " +
                "ha háromszor megmondod, hogy melyik oszlopban van, " +
                "és kitalálom, hogy melyikre gondoltál!" + ANSI_RESET);
        int cnt = 0;
        this.pakli = new String[21];
        while (cnt < 21) {

            String kartyalap = figurak[rnd.nextInt(figurak.length)] + "|" + szamok[rnd.nextInt(szamok.length)];
            int idx = index(kartyalap);
            if (idx == -1) {
                this.pakli[cnt] = kartyalap;
                cnt++;
            }
        }
    }

    public String szin(String kartyalap) {
        switch (kartyalap.toLowerCase().charAt(0)) {
            case 't': // Tök
                return ANSI_YELLOW;
            case 'z': // Zöld
                return ANSI_GREEN;
            case 'p': // Piros
                return ANSI_RED;
            case 'm': // Makk
                return ANSI_ORANGE;
            default:
                return "";
        }
    }

    public String szinez(String kartyalap) {
        return szin(kartyalap) + String.format("%-8s", kartyalap) + ANSI_RESET;
    }

    public void kirak() {
        System.out.println();
        for (int i = 0; i < 7; i++) {
            System.out.println(szinez(this.pakli[i]) + szinez(this.pakli[i + 7]) + szinez(this.pakli[i + 14]));
        }
        System.out.println();
    }

    public void melyik() {
        System.out.print(ANSI_BLUE + "Melyik oszlopban van a kártya?" + ANSI_RESET + "\n> ");

        this.oszlop = -1;
        while (this.oszlop == -1) {
            try {
                this.oszlop = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                this.oszlop = -1; // hiba esetén beállítjuk érvénytelennek
            }
            if (this.oszlop < 1 || this.oszlop > 3) {
                System.out.print(ANSI_RED + "Érvénytelen választás, " +
                        "az oszlopszám csak 1, 2 vagy 3 lehet.\n" + ANSI_RESET);
                System.out.print(ANSI_BLUE + "Melyik oszlopban van a kártya?" + ANSI_RESET + "\n> ");
                this.oszlop = -1;
            }
        }
    }

    public void kever() {
        String[] felvett_pakli = new String[21];
        // Ebben a tömbben tároljuk, hogy milyen sorrendben vesszük fel a lapokat.
        // a középső a választott oszlop indexe lesz (this.oszlop -1)
        int[] sorrend = new int[3];

        switch (this.oszlop) {
            case 1:
                sorrend[0] = 1;
                sorrend[1] = 0;
                sorrend[2] = 2;
                break;
            case 2:
                sorrend[0] = 0;
                sorrend[1] = 1;
                sorrend[2] = 2;
                break;
            case 3:
                sorrend[0] = 0;
                sorrend[1] = 2;
                sorrend[2] = 1;
                break;
        }

        // Pakli "felvétele": új többe másolása úgy, hogy a választott oszlop
        // középre (a tömb közepébe) kerüljön.

        int pi = 0; // pakli index
        for (int i = 0; i < 3; i++) {
            int si = sorrend[i];
            for (int j = 0; j < 7; j++) {
                felvett_pakli[pi++] = this.pakli[si * 7 + j];
            }
        }

        // Pakli "letétele": új tömb másolása a this.pakli-ba soronkénti logikával
        // (0, 7, 14, 1, 8, 15...)

        pi = 0;
        for (int i = 0; i < 21; i++) {
            pi = (i % 3) * 7 + i / 3;
            this.pakli[pi] = felvett_pakli[i];
        }
    }

    public void ezVolt() {
        String kartyalap = this.pakli[10];
        System.out.println(String.format(ANSI_BLUE + "A választott kártya a %s%s" +
                ANSI_BLUE + " volt.\n" + ANSI_RESET, szin(kartyalap), kartyalap));
    }

    public Jatek() {
        kezd();
        kirak();
        for (int i = 0; i < 3; i++) {
            melyik();
            kever();
            kirak();
        }

        ezVolt();
    }

}
