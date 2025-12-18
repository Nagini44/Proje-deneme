import java.time.LocalDate;

public class Akademisyen extends Kisi {
    private long id;
    private String sicilNo;
    private String brans; // YENİ ALAN
    private double maas;

    // Constructor güncellendi: Brans eklendi
    public Akademisyen(long id, String ad, String soyad, LocalDate dt, String sicilNo, String brans, double maas) {
        super(id, ad, soyad, dt, Unvan.AKADEMISYEN);
        this.id = id;
        this.sicilNo = sicilNo;
        this.brans = brans;
        this.maas = maas;
    }

    public boolean dersBransaUygunMu(String dersKodu) {
        // Branşın ilk 3 harfini al (Örn: Matematik -> MAT)
        String bransKodu = this.getBrans().substring(0, 3).toUpperCase();
        String kontrolEdilenKod = dersKodu.toUpperCase();

        // Eğer branş "Bilgisayar" ise ve ders "NYP" ise özel kontrol eklenebilir
        if (this.getBrans().contains("Bilgisayar") && kontrolEdilenKod.startsWith("NYP")) {
            return true;
        }

        // Genel kural: Ders kodu, branşın kısaltmasıyla başlıyor mu?
        return kontrolEdilenKod.startsWith(bransKodu);
    }
    // YENİ YETENEK: Öğrenciye not verme
    public void notGir(Ogrenci ogrenci, String dersKodu, double not) throws SecurityException {
        if (!dersBransaUygunMu(dersKodu)) {
            throw new SecurityException("HATA: " + getBrans() + " branşındaki hoca, " + dersKodu + " dersine not giremez!");
        }

        // Yetki varsa öğrencinin notunu güncelle
        ogrenci.notEkle(dersKodu, not);
        System.out.println("BAŞARILI: " + ogrenci.getAd() + " öğrencisinin " + dersKodu + " notu güncellendi.");
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
    public long getId() { return id; }

}