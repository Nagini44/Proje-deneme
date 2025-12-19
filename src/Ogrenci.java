import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Ogrenci extends Kisi implements IDegerlendirilebilir {

    private int ogrenciNo;
    private int sinif;
    // Notları tutan yeni depo
    private Map<String, Double> dersNotlari = new HashMap<>();
    public Map<String, Double> getDersNotlari() {
        return this.dersNotlari;
    }

    public Ogrenci(long id, String ad, String soyad, LocalDate dogumTarihi, int ogrenciNo, int sinif) {
        super(id, ad, soyad, dogumTarihi,Unvan.OGRENCI);
        this.ogrenciNo = ogrenciNo;
        this.sinif = sinif;
    }

    // JSON'dan gelen notu eklemek için
    public void notEkle(String dersKodu, Double not) {
        dersNotlari.put(dersKodu, not);
    }

    // --- EN ÖNEMLİ KISIM BURASI ---
    // GANO hesaplarken artık Map'in içini geziyoruz.
    @Override
    public double notOrtalamasiHesapla() {
        if (dersNotlari.isEmpty()) return 0.0;

        double toplamPuan = 0;
        int notSayisi = 0;

        for (Double notDe : dersNotlari.values()) {
            toplamPuan += notDe;
            notSayisi++;
        }
        return notSayisi > 0 ? toplamPuan / notSayisi : 0.0;
    }

    // Dönem notlarını (1. sınıf / 2. sınıf) ayrı hesaplamak için
    public double donemOrtalamasiHesapla(int sinifSeviyesi) {
        double toplam = 0;
        int sayac = 0;
        // 1. sınıf dersleri "10" ile (Mat101), 2. sınıf "20" ile (Mat201) başlar/içerir.
        String filtre = sinifSeviyesi == 1 ? "10" : "20";

        for (Map.Entry<String, Double> ders : dersNotlari.entrySet()) {
            if (ders.getKey().contains(filtre)) {
                toplam += ders.getValue();
                sayac++;
            }
        }
        return sayac > 0 ? toplam / sayac : 0.0;
    }

    @Override
    public void bilgileriGoster() {
        System.out.println("\n--- ÖĞRENCİ BİLGİ KARTI ---");
        System.out.println("Ad Soyad : " + getAd() + " " + getSoyad());
        System.out.println("Numara   : " + ogrenciNo);
        System.out.println("Sınıf    : " + sinif);
        System.out.println("---------------------------");

        System.out.println("DERS NOTLARI:");
        for (String k : dersNotlari.keySet()) {
            System.out.println(" * " + k + " : " + dersNotlari.get(k));
        }

        System.out.println("---------------------------");
        System.out.printf("1. Sınıf Ortalaması : %.2f\n", donemOrtalamasiHesapla(1));
        if (sinif > 1) {
            System.out.printf("2. Sınıf Ortalaması : %.2f\n", donemOrtalamasiHesapla(2));
        }
        System.out.printf("GENEL GANO          : %.2f\n", notOrtalamasiHesapla());
    }

    // Zorunlu Override metotları
    @Override public String getRolAdi() { return "Öğrenci"; }
    @Override public String harfNotuGetir() { return notOrtalamasiHesapla() >= 50 ? "Geçti" : "Kaldı"; }
    @Override public boolean gectiMi() { return notOrtalamasiHesapla() >= 50; }
    public int getOgrenciNo() { return ogrenciNo; }
    // Ogrenci sınıfının en altına veya getter metodlarının olduğu yere ekleyin:
    public int getSinif() {return sinif;}
}