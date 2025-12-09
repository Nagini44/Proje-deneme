package src;

import java.util.ArrayList;
import java.util.List;
public class Derslik extends AkademikMekan{
    // Collection Framework kullanımı
    private List<String> verilenDersler;

    public Derslik(String isim, String konum, Integer kapasite) {
        super(isim, konum, kapasite);
        // Listeyi başlatıyoruz (Null pointer hatası almamak için)
        this.verilenDersler = new ArrayList<>();
    }

    // Listeye ders eklemek için yardımcı metod
    public void dersEkle(String dersAdi) {
        this.verilenDersler.add(dersAdi);
    }
    @Override
    public void rezervasyonYap(String zaman) {
        System.out.println(getIsim() + " için " + zaman + " tarihine derslik rezervasyonu alındı.");
    }
    @Override
    public void ozellikleriListele() {
        System.out.println("--- DERSLİK BİLGİSİ ---");
        System.out.println("İsim: " + getIsim());
        System.out.println("Kapasite: " + getKapasite() + " Öğrenci");
        System.out.println("İşlenen Dersler: " + verilenDersler);
    }
}
