import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{
    private static Scanner scanner = new Scanner(System.in);

    // Veri Depoları (Basit veritabanı simülasyonu)
    private static List<AkademikMekan> mekanlar = new ArrayList<>();
    private static VeriDeposu<Ogrenci> ogrenciDeposu = new VeriDeposu<>();
    private static List<Ders> dersListesi = new ArrayList<>();

    private static List<Akademisyen> hocaListesi = JsonIslemleri.akademisyenleriYukle();
    // Demo amaçlı ilk hocayı "Giriş Yapmış" varsayıyoruz
    private static Akademisyen aktifHoca = hocaListesi.isEmpty() ? new Akademisyen(1, "Demo", "Hoca", LocalDate.now(), "001", "Genel", 0,new ArrayList<>()) : hocaListesi.get(0);

    public static void main(String[] args) {
        veriYukle(); // Demo verilerini oluştur

        boolean sistemAcik = true;
        while (sistemAcik) {
            System.out.println("\n###################################");
            System.out.println("#    OBS SİSTEMİNE HOŞ GELDİNİZ   #");
            System.out.println("###################################");
            System.out.println("Lütfen Giriş Türünü Seçiniz:");
            System.out.println("1. Öğrenci Girişi");
            System.out.println("2. Akademisyen Girişi");
            System.out.println("3. İdari Personel Girişi");
            System.out.println("0. Kapat");
            System.out.print("Seçim: ");

            String secim = scanner.nextLine();
            switch (secim) {
                case "1":
                        // JSON'dan verileri çek
                        List<Ogrenci> kayitliListesi = JsonIslemleri.ogrencileriYukle();

                        System.out.print("Öğrenci Numaranızı Giriniz: ");
                        // Hata yönetimi için string alıp parse edelim
                        String girilenNoStr = scanner.nextLine();

                        try {
                            int girilenNo = Integer.parseInt(girilenNoStr);
                            Ogrenci bulunanOgrenci = null;

                            // Listede öğrenciyi ara
                            for (Ogrenci o : kayitliListesi) {
                                if (o.getOgrenciNo() == girilenNo) {
                                    bulunanOgrenci = o;
                                    break;
                                }
                            }

                            if (bulunanOgrenci != null) {
                                System.out.println("Giriş Başarılı! Hoşgeldin " + bulunanOgrenci.getAd());
                                ogrenciMenusu(bulunanOgrenci); // Bulunan öğrenciyi metoda gönderiyoruz
                            } else {
                                System.out.println("HATA: Bu numaraya ait kayıt bulunamadı!");
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("Lütfen geçerli bir sayı giriniz.");
                        }
                        break;

                    case "2":
                        System.out.println("\n--- AKADEMİSYEN GİRİŞİ ---");
                        // 1. JSON'dan hocaları belleğe yükle
                        List<Akademisyen> hocaListesi = JsonIslemleri.akademisyenleriYukle();

                        if (hocaListesi.isEmpty()) {
                            System.out.println("HATA: Sistemde kayıtlı akademisyen bulunamadı (akademisyenler.json boş).");
                            break;
                        }

                        System.out.print("Akademisyen ID'nizi giriniz (Örn: 2001): ");
                        String girilenIdStr = scanner.nextLine();

                        try {
                            long girilenId = Long.parseLong(girilenIdStr);
                            Akademisyen bulunanHoca = null;

                            // 2. Listede ID eşleşmesi ara
                            for (Akademisyen h : hocaListesi) {
                                if (h.getId() == girilenId) {
                                    bulunanHoca = h;
                                    break;
                                }
                            }

                            // 3. Giriş Kontrolü
                            if (aktifHoca != null) {
                                System.out.println("Giriş Başarılı!");
                                // Bulunan hocayı menüye parametre olarak gönderiyoruz
                                akademisyenMenusu(bulunanHoca);
                            } else {
                                System.out.println("HATA: Bu ID'ye sahip bir akademisyen bulunamadı!");
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("HATA: Lütfen ID alanına sadece sayı giriniz.");
                        }
                        break;
                    case "3": idariPersonelMenusu(); break;
                    case "0": sistemAcik = false; break;
                    default: System.out.println("Hatalı seçim!");

            }
        }
    }

    // --- 1. ÖĞRENCİ EKRANI ---
    // Parametre olarak giriş yapan öğrenciyi alıyoruz
    private static void ogrenciMenusu(Ogrenci aktifOgrenci) {
        System.out.println("\n--- ÖĞRENCİ PANELİ (" + aktifOgrenci.getAd() + " " + aktifOgrenci.getSoyad() + ") ---");
        System.out.println("1. GANO ve Harf Notu Göster");
        System.out.println("2. Transkript Görüntüle");
        System.out.println("3. Ders Programı");
        System.out.println("0. Ana Menü");
        System.out.print("Seçim: ");

        String secim = scanner.nextLine();
        switch(secim) {
            case "1":
                aktifOgrenci.bilgileriGoster(); // Tüm hesaplamalar burada yapılıp ekrana basılacak

                break;
            case "2":
                System.out.println("--- Transkript ---");
                // Dosyaya yazdırma işlemini çağırıyoruz:
                JsonIslemleri.notlariRaporla(aktifOgrenci);
                break;
            case "3":
                System.out.println("Pazartesi 09:00 - Nesne Yönelimli Programlama (D-101)");
                break;
            case "0": return;
        }
    }

    // --- 2. AKADEMİSYEN EKRANI ---
    private static void akademisyenMenusu(Akademisyen aktifHoca) {
        boolean cikis = false;
        while (!cikis) {
            System.out.println("\n--- AKADEMİSYEN PANELİ ---");
            System.out.println("Hoşgeldiniz, Sayın " + aktifHoca.getUnvan() + " " + aktifHoca.getAd() + " " + aktifHoca.getSoyad());
            System.out.println("Branş: " + aktifHoca.getBrans());
            System.out.println("--------------------------");
            System.out.println("1. Bilgilerimi Göster");
            System.out.println("2. Yayın Listesi");
            System.out.println("3. Verdiğim Dersler");
            System.out.println("4. Not Girişi Yap"); // Önceki adımda yazdığımız kod buraya gelecek
            System.out.println("0. Çıkış Yap");
            System.out.print("Seçim: ");

            String secim = scanner.nextLine();
            switch (secim) {

                case "1":
                    aktifHoca.bilgileriGoster();
                    break;
                case "2":
                    System.out.println("- Java Mimarileri Üzerine (2023)");
                    System.out.println("- Yapay Zeka Etiği (2024)");
                    break;
                case "3":
                    System.out.println("- Nesne Yönelimli Programlama");
                    System.out.println("- Veri Yapıları");
                    break;
                case "4":
                    System.out.println("\n--- NOT GİRİŞ/GÜNCELLEME EKRANI ---");
                    System.out.println("Aktif Akademisyen: " + aktifHoca.getAd() + " " + aktifHoca.getSoyad());
                    System.out.println("Yetkili Olduğunuz Dersler: " + aktifHoca.getVerilenDersler());

                    System.out.print("İşlem yapılacak Öğrenci Numarası: ");
                    String ogrNoStr = scanner.nextLine();

                    // 1. ÖĞRENCİYİ BULMA
                    Ogrenci hedefOgrenci = null;
                    for (Ogrenci o : ogrenciDeposu.getListe()) {
                        if (String.valueOf(o.getOgrenciNo()).equals(ogrNoStr)) {
                            hedefOgrenci = o;
                            break;
                        }
                    }

                    if (hedefOgrenci == null) {
                        System.out.println("HATA: Bu numaraya sahip öğrenci bulunamadı!");
                        break;
                    }

                    System.out.println("Seçilen Öğrenci: " + hedefOgrenci.getAd() + " " + hedefOgrenci.getSoyad());
                    boolean islemYapildi = false;

                    // 2. HOCANIN DERSLERİNİ TARIYORUZ
                    if (aktifHoca.getVerilenDersler() != null) {
                        for (String dersKodu : aktifHoca.getVerilenDersler()) {

                            // Öğrencinin not map'inde bu dersin Vize veya Final kaydı var mı?
                            // Not: JSON'da notlar "Mat101V", "Mat101F" şeklinde tutuluyor.
                            String vizeKey = dersKodu + "V";
                            String finalKey = dersKodu + "F";

                            // Öğrenci bu dersi alıyor mu? (Map'te anahtar var mı?)
                            boolean dersiAliyorMu = hedefOgrenci.getDersNotlari().containsKey(vizeKey) ||
                                    hedefOgrenci.getDersNotlari().containsKey(finalKey);

                            if (dersiAliyorMu) {
                                System.out.println("\n>>> DERS BULUNDU: " + dersKodu);
                                islemYapildi = true;

                                // --- VİZE GİRİŞİ ---
                                try {
                                    System.out.print(dersKodu + " VİZE notunu giriniz (Mevcut: " +
                                            hedefOgrenci.getDersNotlari().getOrDefault(vizeKey, 0.0) + "): ");

                                    String vizeInput = scanner.nextLine();
                                    if (!vizeInput.isEmpty()) {
                                        double yeniVize = Double.parseDouble(vizeInput);
                                        hedefOgrenci.notEkle(vizeKey, yeniVize);
                                        System.out.println("Vize güncellendi.");
                                    }

                                    // --- FİNAL GİRİŞİ ---
                                    System.out.print(dersKodu + " FİNAL notunu giriniz (Mevcut: " +
                                            hedefOgrenci.getDersNotlari().getOrDefault(finalKey, 0.0) + "): ");

                                    String finalInput = scanner.nextLine();
                                    if (!finalInput.isEmpty()) {
                                        double yeniFinal = Double.parseDouble(finalInput);
                                        hedefOgrenci.notEkle(finalKey, yeniFinal);
                                        System.out.println("Final güncellendi.");
                                    }

                                } catch (NumberFormatException e) {
                                    System.out.println("HATA: Geçersiz sayı formatı! Bu ders atlandı.");
                                }
                            }
                        }
                    }

                    if (!islemYapildi) {
                        System.out.println("UYARI: Bu öğrenci sizin verdiğiniz derslerden hiçbirini almıyor veya sistemde eşleşme bulunamadı.");
                    } else {
                        System.out.println("\nNot giriş işlemleri tamamlandı.");
                        JsonIslemleri.ogrencileriKaydet(ogrenciDeposu.getListe());
                    }
                    break;
                case "0":
                    return;
            }
        }
    }

    // --- 3. İDARİ PERSONEL EKRANI ---
    private static void idariPersonelMenusu() {
        IdariPersonel memur = new IdariPersonel(1, "Ayşe", "Demir", "Öğrenci İşleri");

        System.out.println("\n--- İDARİ PERSONEL PANELİ ---");
        System.out.println("Aktif Personel: " + memur.getAd() + " " + memur.getSoyad());
        System.out.println("1. Dersliğe Ders Ata");
        System.out.println("2. Derse Hoca Ata");
        System.out.println("3. Rezervasyon işlemi");
        System.out.println("0. Ana Menü");
        System.out.print("Seçim: ");

        String secim = scanner.nextLine();
        switch(secim) {
            case "1":
                System.out.print("Ders Adı: ");
                String ders = scanner.nextLine();
                System.out.print("Derslik Kodu: ");
                String kod = scanner.nextLine();
                memur.dersAtamaYap(ders, kod);
                break;
            case "2":
                System.out.println("Derse hoca atama işlemi başarıyla tamamlandı.");
                break;
            case "3":

                AkademikMekan A101 = new Derslik("101. Sınıf","A blok Birinci Kat",30);
                A101.ozellikleriListele();
                try {
                    A101.rezervasyonYap("14.30", 20);
                }catch (KapasiteHatasiException e){
                    System.out.println("Hata Yakalandı" + e.getMessage());
                }
                break;
            case "0":
                return;
        }
    }

    // Demo verileri yükleme
    private static void veriYukle() {
             // -- BURAYI EKLEYİN --
        // JSON dosyasındaki öğrencileri okuyup sisteme (VeriDeposu'na) dahil ediyoruz
        List<Ogrenci> jsonOgrencileri = JsonIslemleri.ogrencileriYukle();
        for (Ogrenci o : jsonOgrencileri) {
            ogrenciDeposu.ekle(o);
        }

        System.out.println("Bilgi: JSON'dan " + jsonOgrencileri.size() + " öğrenci sisteme yüklendi.");
    }

}
