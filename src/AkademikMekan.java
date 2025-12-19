public abstract class AkademikMekan implements IRezervasyon {
    // Değişkenler private (Encapsulation) ve Wrapper sınıf (Integer)
    private String isim;
    private String konum;
    private Integer kapasite;
    // Constructor (Yapıcı Metod)
    public AkademikMekan(String isim, String konum, Integer kapasite) {
        this.isim = isim;
        this.konum = konum;
        this.kapasite = kapasite;
    }
    // Soyut Metod: Her alt sınıf bunu kendine göre DOLDURMAK ZORUNDA
    public abstract void ozellikleriListele();

    // Yeni metot: verilen kisiSayisi ile mekanda ders işlenip işlenemeyeceğini sorgular
    // Alt sınıflar bunu override ederek kendi kararlarını verebilir. Burada bir varsayılan
    // (default) uygulama sağlanıyor; böylece tüm alt sınıfları değiştirmeye gerek kalmaz.
    public boolean kapasiteSorgula(int kisiSayisi) {
        boolean uygun = kisiSayisi <= this.kapasite;
        if (uygun) {
            System.out.println(isim + " için " + kisiSayisi + " kişilik etkinlik/ders yapılabilir (varsayılan kontrol).");
        } else {
            System.out.println(isim + " için " + kisiSayisi + " kişilik etkinlik/ders yapılamaz. Kapasite: " + kapasite);
        }
        return uygun;
    }

    // Yeni metot: Ders ataması yapmak için alt sınıflar override edebilir.
    // Eğer alt sınıf override etmezse bu varsayılan davranış çalışır.
    public boolean dersAtama(String dersAdi, String onaylayanAdi) {
        System.out.println("[AkademikMekan] " + getIsim() + " bu mekan türü ders atamayı desteklemiyor veya özel davranışı yok.");
        return false;
    }

    // Getter ve Setter Metodları (Erişim için)
    public String getIsim() { return isim; }
    public Integer getKapasite() { return kapasite; }
    // Interface'den gelen metodu burada genel bir şekilde ezebiliriz
    // veya alt sınıflara bırakabiliriz. Burada örnek gövde yazalım:
    @Override
    public void rezervasyonYap(String zaman, int kisiSayisi) throws KapasiteHatasiException {
        if (kisiSayisi > this.kapasite) {
            // Hata fırlatıldığı anda metod burada kesilir, alt satıra geçmez.
            throw new KapasiteHatasiException(
                    "HATA: " + isim + " kapasitesi aşıldı! Kapasite: " + kapasite + ", Talep: " + kisiSayisi
            );
        }
        System.out.println(isim + " için " + zaman + " saatine " + kisiSayisi + " kişilik onay verildi.");
    }

    @Override
    public void rezervasyonIptal(String zaman) {
        System.out.println(this.isim + " rezervasyonu iptal edildi.");
    }
}
