package util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;


public class TarihYardimcisi {

    private static final DateTimeFormatter GORUNUM_FORMATI = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static int yasHesapla(LocalDate dogumTarihi) {
        if (dogumTarihi == null) return 0;
        return Period.between(dogumTarihi, LocalDate.now()).getYears();
    }


    public static String bugunuGetir() {
        return LocalDate.now().format(GORUNUM_FORMATI);
    }
    public static String tarihFormatla(LocalDate tarih) {
        if (tarih == null) return "Tarih Yok";
        return tarih.format(GORUNUM_FORMATI);
    }

    public static boolean tarihGecmisMi(LocalDate tarih) {
        return tarih.isBefore(LocalDate.now());
    }
}