package model;

import exception.KapasiteHatasiException;

import java.util.ArrayList;
import java.util.List;

public class Laboratuvar extends AkademikMekan {
    private Integer pcSayisi; // Wrapper sınıf
    private List<String> verilenDersler; // laboratuvarda verilen dersleri tutmak için

    // Constructor'a "throws" ekledik. Hatalı veri varsa nesne oluşmayacak.
    public Laboratuvar(String isim, String konum, Integer kapasite, Integer pcSayisi) throws KapasiteHatasiException {
        super(isim, konum, kapasite);

        // Validasyon (Doğrulama) Kuralı
        if (pcSayisi > kapasite) {
            throw new KapasiteHatasiException("Lab Hatası: PC sayısı (" + pcSayisi + ") kapasiteden (" + kapasite + ") büyük olamaz!");
        }

        this.pcSayisi = pcSayisi;
        this.verilenDersler = new ArrayList<>();
    }
    @Override
    public void rezervasyonYap(String zaman) {
        System.out.println(getIsim() + " için " + zaman + " tarihine laboratuvar rezervasyonu alındı.");
    }

    @Override
    public void rezervasyonIptal(String zaman) {
        System.out.println(getIsim() + " rezervasyonu iptal edildi: " + zaman);
    }
    @Override
    public void ozellikleriListele() {
        System.out.println("--- LABORATUVAR BİLGİSİ ---");
        System.out.println("İsim: " + getIsim());
        System.out.println("PC Sayısı: " + pcSayisi);
        // PC sayısı kapasiteye uygun mu kontrolü (Wrapper karşılaştırması)
        if(pcSayisi < getKapasite()) {
            System.out.println("Uyarı: PC sayısı kapasiteden az!");
        }
        System.out.println("İşlenen Dersler: " + verilenDersler);
    }

    // model.AkademikMekan'deki soyut metodu override ediyoruz.
    // model.Laboratuvar için hem kapasite hem de PC sayısı göz önünde bulundurulur.
    @Override
    public boolean kapasiteSorgula(int kisiSayisi) {
        boolean kapasiteUygun = kisiSayisi <= getKapasite();
        boolean pcUygun = kisiSayisi <= pcSayisi;
        if (kapasiteUygun && pcUygun) {
            System.out.println(getIsim() + " için " + kisiSayisi + " kişilik laboratuvar dersi yapılabilir.");
            return true;
        }
        if (!kapasiteUygun) {
            System.out.println(getIsim() + " için " + kisiSayisi + " kişilik ders yapılamaz. Kapasite: " + getKapasite());
        }
        if (!pcUygun) {
            System.out.println(getIsim() + " için " + kisiSayisi + " kişilik ders yapılamaz. PC sayısı: " + pcSayisi);
        }
        return false;
    }

    // Idari personelin dersi buraya ataması: eğer laboratuvarda henüz ders yoksa atamayı onayla
    @Override
    public boolean dersAtama(String dersAdi, String onaylayanAdi) {
        if (this.verilenDersler.isEmpty()) {
            this.verilenDersler.add(dersAdi);
            System.out.println("İdari personel " + onaylayanAdi + " tarafından " + getIsim() + " laboratuvarına '" + dersAdi + "' ataması yapıldı.");
            return true;
        } else {
            System.out.println(getIsim() + " zaten ders/etkinlik içeriyor: " + verilenDersler + ". Yeni atama için önce mevcut atama kaldırılmalı.");
            return false;
        }
    }
}
