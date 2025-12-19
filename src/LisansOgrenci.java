import java.time.LocalDate;

// Var olan Ogrenci sınıfından türetiyoruz
public class LisansOgrenci extends Ogrenci {
    private int sinif; // 1, 2, 3, 4

    public LisansOgrenci(long id, String ad, String soyad, int no, int sinif) {
        super(id, ad, soyad,LocalDate.now(), no,sinif);
        this.sinif = sinif;
    }

    @Override
    public void bilgileriGoster() {
        super.bilgileriGoster(); // Üst sınıfın çıktısını yaz
        System.out.println("   Seviye: Lisans (" + sinif + ". Sınıf)");
    }
}