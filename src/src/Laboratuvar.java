package src;

public class Laboratuvar extends AkademikMekan{
    private Integer pcSayisi; // Wrapper sınıf

    // Constructor'a "throws" ekledik. Hatalı veri varsa nesne oluşmayacak.
    public Laboratuvar(String isim, String konum, Integer kapasite, Integer pcSayisi) throws KapasiteHatasiException {
        super(isim, konum, kapasite);

        // Validasyon (Doğrulama) Kuralı
        if (pcSayisi > kapasite) {
            throw new KapasiteHatasiException("Lab Hatası: PC sayısı (" + pcSayisi + ") kapasiteden (" + kapasite + ") büyük olamaz!");
        }

        this.pcSayisi = pcSayisi;
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
    }
}
