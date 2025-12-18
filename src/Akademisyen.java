import java.time.LocalDate;

public class Akademisyen extends Kisi {
    private String sicilNo;
    private String brans; // YENİ ALAN
    private double maas;

    // Constructor güncellendi: Brans eklendi
    public Akademisyen(long id, String ad, String soyad, LocalDate dt, String sicilNo, String brans, double maas) {
        super(id, ad, soyad, dt);
        this.sicilNo = sicilNo;
        this.brans = brans;
        this.maas = maas;
    }

    // YENİ YETENEK: Öğrenciye not verme
    public void notGir(Ogrenci ogrenci, String dersKodu, double not) {
        // İsterseniz burada "Akademisyenin branşı bu derse uygun mu?" kontrolü de yapılabilir.
        System.out.println("Sayın " + getAd() + ", " + ogrenci.getAd() + " adlı öğrenciye not girişi yapıyor...");

        // Ogrenci sınıfındaki metodu çağırıyoruz.
        // Hata yönetimi (Validation) Ogrenci sınıfı içinde veya Main'de yapılabilir.
        ogrenci.notEkle(dersKodu, not);

        System.out.println("İşlem Başarılı: " + dersKodu + " notu güncellendi.");
    }

    @Override
    public void bilgileriGoster() {
        System.out.println("--- AKADEMİSYEN KARTI ---");
        System.out.println("Ad Soyad : " + getAd() + " " + getSoyad());
        System.out.println("Branş    : " + brans);
        System.out.println("Sicil No : " + sicilNo);
    }

    @Override
    public String getRolAdi() { return "Akademisyen"; }

    // Getter - Setter
    public String getBrans() { return brans; }
    public void setBrans(String brans) { this.brans = brans; }
    public double getMaas() { return maas; }
    public String getSicilNo() { return sicilNo; }
}