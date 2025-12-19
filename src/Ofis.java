public class Ofis extends AkademikMekan {
    private String calisanAdi;

    public Ofis(String isim, String konum, Integer kapasite, String calisanAdi) {
        super(isim, konum, kapasite);
        this.calisanAdi = calisanAdi;
    }

    @Override
    public void rezervasyonYap(String zaman) {
        System.out.println(getIsim() + " ofisi için " + zaman + " tarihine rezervasyon alındı.");
    }

    @Override
    public void ozellikleriListele() {
        System.out.println("--- OFİS BİLGİSİ ---");
        System.out.println("İsim: " + getIsim());
        System.out.println("Kapasite: " + getKapasite() + " Kişi");
        System.out.println("Çalışan Adı: " + calisanAdi);
    }

}