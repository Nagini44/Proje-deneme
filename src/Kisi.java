import java.time.LocalDate;

// Gereksinim 4.2: Abstract sınıf
public abstract class Kisi {
    // Gereksinim 3: Private alanlar (Encapsulation)
    private long id;
    private String ad;
    private String soyad;
    private LocalDate dogumTarihi; // Gereksinim 6: LocalDate kullanımı

    private Unvan unvan;

    // Gereksinim 3: Constructor Overloading (1)
    public Kisi() {
        this.id = System.currentTimeMillis();
    }

    public Kisi(long id, String ad, String soyad, LocalDate dogumTarihi,Unvan unvan) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.dogumTarihi = dogumTarihi;
        this.unvan = unvan; // Atama yapıldı
    }

    // Gereksinim 4.2: Abstract metotlar
    public abstract void bilgileriGoster();
    public abstract String getRolAdi();

    // Getter - Setter Metotları
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }

    public LocalDate getDogumTarihi() { return dogumTarihi; }
    public void setDogumTarihi(LocalDate dogumTarihi) { this.dogumTarihi = dogumTarihi; }

    public Unvan getUnvan() { return unvan; }
    public void setUnvan(Unvan unvan) { this.unvan = unvan; }
}