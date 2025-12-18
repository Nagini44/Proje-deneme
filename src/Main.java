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
    private static Akademisyen aktifHoca = hocaListesi.isEmpty() ? new Akademisyen(1, "Demo", "Hoca", LocalDate.now(), "001", "Genel", 0) : hocaListesi.get(0);

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

                    case "2": akademisyenMenusu(); break;
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
    private static void akademisyenMenusu() {
        System.out.println("\n--- AKADEMİSYEN PANELİ ---");
        System.out.println("1. Bilgilerimi Göster");
        System.out.println("2. Yayın Listesi");
        System.out.println("3. Verdiğim Dersler");
        System.out.println("4. Not Girişi Yap");
        System.out.println("0. Ana Menü");
        System.out.print("Seçim: ");

        String secim = scanner.nextLine();
        switch(secim) {

            case "1":
                System.out.println("Doç. Dr. Ahmet Yılmaz - Bilgisayar Müh.");
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
                System.out.println("--- NOT GİRİŞ EKRANI ---");
                System.out.println("İşlem Yapan: " + aktifHoca.getAd() + " " + aktifHoca.getSoyad() + " (" + aktifHoca.getBrans() + ")");

                System.out.print("Notu girilecek Öğrenci Numarası: ");
                String ogrNoStr = scanner.nextLine();

                // Öğrenciyi bulalım (ogrenciDeposu Main sınıfında static tanımlıydı)
                Ogrenci bulunanOgr = null;
                // Not: ogrenciDeposu.getListe() ile listeden arıyoruz
                for(Ogrenci o : ogrenciDeposu.getListe()) {
                    if(String.valueOf(o.getOgrenciNo()).equals(ogrNoStr)) {
                        bulunanOgr = o;
                        break;
                    }
                }

                if (bulunanOgr != null) {
                    System.out.println("Seçilen Öğrenci: " + bulunanOgr.getAd());
                    System.out.print("Ders Kodu (Örn: Mat101V): ");
                    String dersKodu = scanner.nextLine();

                    System.out.print("Not Değeri: ");
                    try {
                        double notDegeri = Double.parseDouble(scanner.nextLine());

                        // AKADEMİSYEN SINIFINDAKİ METODU ÇAĞIRIYORUZ
                        aktifHoca.notGir(bulunanOgr, dersKodu, notDegeri);

                    } catch (NumberFormatException e) {
                        System.out.println("Hata: Sayısal bir değer giriniz.");
                    } catch (Exception e) { // Validation hatası gelirse yakalar
                        System.out.println("İşlem Başarısız: " + e.getMessage());
                    }
                } else {
                    System.out.println("Hata: Bu numaraya sahip öğrenci bulunamadı.");
                }
                break;
            case "0": return;
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
        // Mekanlar


        // Dersler
        dersListesi.add(new Ders("NYP101", "Nesne Yönelimli Programlama", 5));
        dersListesi.add(new Ders("MAT101", "Matematik I", 4));

        // Öğrenci (Polimorfizm örneği: Listeye LisansOgrenci ekliyoruz)
        ogrenciDeposu.ekle(new LisansOgrenci(1001, "Mehmet", "Can", 123, 2));
    }
}