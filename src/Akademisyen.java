import java.time.LocalDate;
import java.util.List;

public class Akademisyen extends Kisi {
    private long id;
    private String sicilNo;
    private String brans;
    private double maas;
    private List<String> verilenDersler; // EKLENDİ

    // Constructor GÜNCELLENDİ
    public Akademisyen(long id, String ad, String soyad, LocalDate dt, String sicilNo, String brans, double maas, List<String> verilenDersler) {
        super(id, ad, soyad, dt, Unvan.AKADEMISYEN);
        this.id = id;
        this.sicilNo = sicilNo;
        this.brans = brans;
        this.maas = maas;
        this.verilenDersler = verilenDersler;
    }

    public boolean dersBransaUygunMu(String dersKodu) {
        if (verilenDersler == null) return false;
        // Ders kodunun kökünü kontrol et (Örn: Fiz101V -> Fiz101 içeriyor mu?)
        for(String ders : verilenDersler) {
            if(dersKodu.startsWith(ders)) return true;
        }
        return false;
    }
    // Dersin akademisyenin yetki alanında olup olmadığını kontrol eder
    public boolean dersiVerebilirMi(String dersKoduBase) {
        if (verilenDersler == null) return false;
        // Listede bu ders var mı? (Örn: "Fiz101")
        return verilenDersler.contains(dersKoduBase);
    }

    public void notGir(Ogrenci ogrenci, String dersKodu, double not) throws SecurityException {
        if (!dersBransaUygunMu(dersKodu)) {
            throw new SecurityException("HATA: Bu dersi verme yetkiniz yok (" + dersKodu + ")");
        }
        ogrenci.notEkle(dersKodu, not);
        System.out.println("BAŞARILI: " + ogrenci.getAd() + " notu güncellendi.");
    }

    @Override
    public void bilgileriGoster() {
        System.out.println("--- AKADEMİSYEN KARTI ---");
        System.out.println("Ad Soyad : " + getAd() + " " + getSoyad());
        System.out.println("Branş    : " + brans);
        System.out.println("Sicil No : " + sicilNo);
        System.out.println("Verdiği Dersler: " + (verilenDersler != null ? verilenDersler : "Yok"));
    }

    public String getRolAdi() { return "Akademisyen"; }

    // Getter - Setter
    public String getBrans() { return brans; }
    public void setBrans(String brans) { this.brans = brans; }
    public double getMaas() { return maas; }
    public String getSicilNo() { return sicilNo; }
    public long getId() { return id; }
    public List<String> getVerilenDersler() { return verilenDersler; }
}