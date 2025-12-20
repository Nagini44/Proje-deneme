package model;

import java.util.ArrayList;
import java.util.List;
public class Derslik extends AkademikMekan {
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
    // model.AkademikMekan'deki soyut metodu override ediyoruz.
    @Override
    public boolean kapasiteSorgula(int kisiSayisi) {
        boolean uygun = kisiSayisi <= getKapasite();
        if (uygun) {
            System.out.println(getIsim() + " için " + kisiSayisi + " kişilik ders yapılabilir.");
        } else {
            System.out.println(getIsim() + " için " + kisiSayisi + " kişilik ders yapılamaz. Kapasite: " + getKapasite());
        }
        return uygun;
    }

    // Idari personelin dersi buraya ataması: eğer derslikte henüz ders yoksa atamayı onayla
    @Override
    public boolean dersAtama(String dersAdi, String onaylayanAdi) {
        if (this.verilenDersler.isEmpty()) {
            this.verilenDersler.add(dersAdi);
            System.out.println("İdari personel " + onaylayanAdi + " tarafından " + getIsim() + " dersliğine '" + dersAdi + "' ataması yapıldı.");
            return true;
        } else {
            System.out.println(getIsim() + " zaten ders/etkinlik içeriyor: " + verilenDersler + ". Yeni atama için önce mevcut atama kaldırılmalı.");
            return false;
        }
    }
}
