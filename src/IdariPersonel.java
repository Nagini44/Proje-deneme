import java.time.LocalDate;

public class IdariPersonel extends Kisi {
    private String departman; // Örn: Öğrenci İşleri

    public IdariPersonel(long id, String ad, String soyad, String departman) {
        super(id, ad, soyad, LocalDate.now(),Unvan.MEMUR);
        this.departman = departman;
    }

    @Override
    public void bilgileriGoster() {
        System.out.println("İdari Personel: " + getAd() + " " + getSoyad() + " (" + departman + ")");
    }

    @Override
    public String getRolAdi() {
        return "İdari Personel";
    }

    // İdari personelin yetenekleri (Main'de çağrılacak)
    public void dersAtamaYap(String dersAdi, String derslikKodu) {
        System.out.println(dersAdi + " dersi " + derslikKodu + " dersliğine atandı.");
    }

    // Yeni getter: departman
    public String getDepartman() {
        return this.departman;
    }
}