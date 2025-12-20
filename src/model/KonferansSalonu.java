package model;

import exception.KapasiteHatasiException;

import java.util.ArrayList;
import java.util.List;
public class KonferansSalonu extends AkademikMekan {
    private List<String> etkinlikler;

    public KonferansSalonu(String isim, String konum, Integer kapasite) {
        super(isim, konum, kapasite);
        this.etkinlikler = new ArrayList<>();
    }

    public void etkinlikEkle(String etkinlikAdi) {
        this.etkinlikler.add(etkinlikAdi);
    }

    @Override
    public void rezervasyonYap(String zaman) throws KapasiteHatasiException {
        System.out.println(getIsim() + " konferans salonu için " + zaman + " tarihine rezervasyon alındı. Lütfen kapasiteyi ve teknik gereksinimleri kontrol ediniz.");
    }

    @Override
    public void ozellikleriListele() {
        System.out.println("--- KONFERANS SALONU ---");
        System.out.println("Salon: " + getIsim());
        System.out.println("Gelecek Etkinlikler: ");
        for (String etkinlik : etkinlikler) {
            System.out.println("- " + etkinlik);
        }
    }

    // model.AkademikMekan'deki soyut metodu override ediyoruz.
    @Override
    public boolean kapasiteSorgula(int kisiSayisi) {
        boolean uygun = kisiSayisi <= getKapasite();
        if (uygun) {
            System.out.println(getIsim() + " için " + kisiSayisi + " kişilik etkinlik gerçekleştirilebilir.");
        } else {
            System.out.println(getIsim() + " için " + kisiSayisi + " kişilik etkinlik gerçekleştirilemez. Kapasite: " + getKapasite());
        }
        return uygun;
    }

}
