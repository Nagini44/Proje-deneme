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
    // Bölüm dersleri (Bilgisayar Mühendisliği - örnek 4 ders)
    private static List<Ders> bolumDersleri = new ArrayList<>();

     private static List<Akademisyen> hocaListesi = JsonIslemleri.akademisyenleriYukle();
    // Demo amaçlı ilk hocayı "Giriş Yapmış" varsayıyoruz
    private static Akademisyen aktifHoca = hocaListesi.isEmpty() ? new Akademisyen(1, "Demo", "Hoca", LocalDate.now(), "001", "Genel", 0,new ArrayList<>()) : hocaListesi.get(0);
    // İdari personel listesi JSON'dan yüklenecek
    private static List<IdariPersonel> idariListesi = JsonIslemleri.idariPersonelYukle();

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
                        // local değişkene gerek yok, sınıf seviyesindeki hocaListesi'ni kullanalım

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

                            // 3. Giriş Kontrolü (bulunanHoca kontrolü doğru olacak)
                            if (bulunanHoca != null) {
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
        boolean cikis = false;
        while (!cikis) {
            System.out.println("\n--- ÖĞRENCİ PANELİ (" + aktifOgrenci.getAd() + " " + aktifOgrenci.getSoyad() + ") ---");
            System.out.println("1. GANO ve Harf Notu Göster");
            System.out.println("2. Transkript Görüntüle");
            System.out.println("3. Ders Programı");
            System.out.println("4. Konum Sorgula");
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
                case "4":
                    konumSorgulaPrompt();
                    break;
                case "0":
                    cikis = true; // Ana menüye dön
                    break;
                default:
                    System.out.println("Hatalı seçim! Lütfen tekrar deneyin.");
            }
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
            System.out.println("5. Konum Sorgula");
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
                case "5":
                    konumSorgulaPrompt();
                    break;
                case "0":
                    cikis = true; // Ana menüye dön
                    break;
                default:
                    System.out.println("Hatalı seçim! Lütfen tekrar deneyin.");
            }
        }
    }

    // --- 3. İDARİ PERSONEL EKRANI ---
    private static void idariPersonelMenusu() {
        // JSON'dan yüklenmiş idari personel listesinden seçim yapılmasını sağlıyoruz
        if (idariListesi == null || idariListesi.isEmpty()) {
            System.out.println("UYARI: Sistemde kayıtlı idari personel bulunamadı (idariPersonel.json boş veya yok).\n");
            return;
        }

        System.out.println("\n--- İDARİ PERSONEL PANELİ ---");
        System.out.println("Sistemde kayıtlı idari personeller:");
        for (IdariPersonel p : idariListesi) {
            System.out.println("ID: " + p.getId() + " - " + p.getAd() + " " + p.getSoyad() + " (" + p.getRolAdi() + ")");
        }

        System.out.print("Giriş yapmak istediğiniz personel ID'sini giriniz: ");
        String idStr = scanner.nextLine();
        long idSecim = -1;
        try { idSecim = Long.parseLong(idStr); } catch (NumberFormatException e) { System.out.println("Geçersiz ID girdiniz."); return; }

        IdariPersonel memur = null;
        for (IdariPersonel p : idariListesi) {
            if (p.getId() == idSecim) { memur = p; break; }
        }

        if (memur == null) {
            System.out.println("HATA: Bu ID'ye sahip idari personel bulunamadı.");
            return;
        }

        // Seçilen idari personel için döngüsel menü
        boolean cikis = false;
        while (!cikis) {
            System.out.println("Aktif Personel: " + memur.getAd() + " " + memur.getSoyad());
            System.out.println("1. Dersliğe Ders Ata");
            System.out.println("2. Derse Hoca Ata");
            System.out.println("3. Mekan Özellikleri");
            System.out.println("4. Konum Sorgula");
            System.out.println("0. Ana Menü");
            System.out.print("Seçim: ");

            String secim = scanner.nextLine();
            switch(secim) {
                case "4":
                    konumSorgulaPrompt();
                    break;
                 case "1":
                     // Dersliği bir bölüm dersine atama: önce bölüm derslerini listeleriz
                     System.out.println("Mevcut Bölüm Dersleri:");
                     for (int i = 0; i < bolumDersleri.size(); i++) {
                         System.out.println((i+1) + ". " + bolumDersleri.get(i));
                     }
                     System.out.print("Atamak istediğiniz dersin numarasını giriniz: ");
                     String dsec = scanner.nextLine();
                     int dindex = -1;
                     try { dindex = Integer.parseInt(dsec) - 1; } catch (NumberFormatException e) { System.out.println("Geçersiz seçim"); break; }
                     if (dindex < 0 || dindex >= bolumDersleri.size()) { System.out.println("Geçersiz ders numarası"); break; }
                     Ders secilenDers = bolumDersleri.get(dindex);

                     // Dersliği seç
                     System.out.println("Mevcut Derslikler:");
                     for (int i = 0; i < mekanlar.size(); i++) {
                         System.out.println((i+1) + ". " + mekanlar.get(i).getIsim());
                     }
                     System.out.print("Hangi dersliğe atamak istersiniz (numara): ");
                     String msec = scanner.nextLine();
                     int mindex = -1;
                     try { mindex = Integer.parseInt(msec) - 1; } catch (NumberFormatException e) { System.out.println("Geçersiz seçim"); break; }
                     if (mindex < 0 || mindex >= mekanlar.size()) { System.out.println("Geçersiz derslik numarası"); break; }
                     AkademikMekan hedefMekan = mekanlar.get(mindex);

                     boolean atamaBasarili = hedefMekan.dersAtama(secilenDers.getKod(), memur.getAd() + " " + memur.getSoyad());
                     if (atamaBasarili) {
                         memur.dersAtamaYap(secilenDers.getAd(), hedefMekan.getIsim());
                         System.out.println("Atama başarılı: " + secilenDers.getKod() + " -> " + hedefMekan.getIsim());
                     }
                     break;
                case "2":
                    // Derse hoca atama: bölüm dersleri arasından bir ders seç, sonra akademisyen seç
                    System.out.println("Mevcut Bölüm Dersleri:");
                    for (int i = 0; i < bolumDersleri.size(); i++) {
                        System.out.println((i+1) + ". " + bolumDersleri.get(i));
                    }
                    System.out.print("Hoca atamak istediğiniz ders numarasını giriniz: ");
                    String dh = scanner.nextLine();
                    int dhIndex = -1;
                    try { dhIndex = Integer.parseInt(dh) - 1; } catch (NumberFormatException e) { System.out.println("Geçersiz seçim"); break; }
                    if (dhIndex < 0 || dhIndex >= bolumDersleri.size()) { System.out.println("Geçersiz ders numarası"); break; }
                    Ders hedefDers = bolumDersleri.get(dhIndex);

                    System.out.println("Mevcut Akademisyenler:");
                    for (Akademisyen a : hocaListesi) {
                        System.out.println("ID: " + a.getId() + " - " + a.getAd() + " " + a.getSoyad() + " (" + a.getBrans() + ")");
                    }
                    System.out.print("Atamak istediğiniz akademisyenin ID'sini giriniz: ");
                    String aid = scanner.nextLine();
                    long aidL = -1;
                    try { aidL = Long.parseLong(aid); } catch (NumberFormatException e) { System.out.println("Geçersiz ID"); break; }
                    Akademisyen secAk = null;
                    for (Akademisyen a : hocaListesi) { if (a.getId() == aidL) { secAk = a; break; } }
                    if (secAk == null) { System.out.println("Akademisyen bulunamadı"); break; }

                    // Atamayı ekle (eğer yoksa)
                    if (secAk.getVerilenDersler() == null) {
                        System.out.println("Akademisyenin verdiği ders listesi null, atama yapılamadı.");
                    } else if (secAk.getVerilenDersler().contains(hedefDers.getKod())) {
                        System.out.println("Bu akademisyen zaten bu dersi veriyor: " + hedefDers.getKod());
                    } else {
                        secAk.getVerilenDersler().add(hedefDers.getKod());
                        System.out.println("Atama başarılı: Akademisyen " + secAk.getAd() + " -> " + hedefDers.getKod());
                        // Değişiklikleri dosyaya kaydet (opsiyonel)
                        JsonIslemleri.akademisyenleriKaydet(hocaListesi);
                    }
                    break;
                case "3":
                    // Yeni: Mekan özellikleri görüntüleme menüsü (derslik/lab/salon)
                    boolean mekanCikis = false;
                    while (!mekanCikis) {
                        System.out.println("\n--- MEKAN ÖZELLİKLERİ ---");
                        System.out.println("1. Derslikleri Görüntüle");
                        System.out.println("2. Laboratuvarları Görüntüle");
                        System.out.println("3. Konferans Salonlarını Görüntüle");
                        System.out.println("0. Geri");
                        System.out.print("Seçim: ");

                        String msecim = scanner.nextLine();
                        switch (msecim) {
                            case "1": {
                                boolean found = false;
                                for (AkademikMekan m : mekanlar) {
                                    if (m instanceof Derslik) {
                                        found = true;
                                        m.ozellikleriListele();
                                        // Kapasite bilgisi (her mekan için ortak)
                                        System.out.println("Kapasite: " + m.getKapasite());
                                        // Kullanıcıya müsaitlik kontrolü sor
                                        System.out.print("Müsaitlik kontrolü için kişi sayısı girin (ENTER ile atla): ");
                                        String kisiStr = scanner.nextLine().trim();
                                        if (!kisiStr.isEmpty()) {
                                            try {
                                                int kisi = Integer.parseInt(kisiStr);
                                                m.kapasiteSorgula(kisi);
                                            } catch (NumberFormatException e) {
                                                System.out.println("Geçersiz sayı girdiniz, atlanıyor.");
                                            }
                                        }
                                        System.out.println();
                                    }
                                }
                                if (!found) System.out.println("Sistemde kayıtlı derslik bulunamadı.");
                            } break;
                            case "2": {
                                boolean found = false;
                                for (AkademikMekan m : mekanlar) {
                                    if (m instanceof Laboratuvar) {
                                        found = true;
                                        // Laboratuvar sınıfının kendi ozellik metodu pc sayısını da yazdırır
                                        m.ozellikleriListele();
                                        System.out.println("Kapasite: " + m.getKapasite());
                                        // Müsaitlik kontrolü
                                        System.out.print("Müsaitlik kontrolü için kişi sayısı girin (ENTER ile atla): ");
                                        String kisiStr = scanner.nextLine().trim();
                                        if (!kisiStr.isEmpty()) {
                                            try {
                                                int kisi = Integer.parseInt(kisiStr);
                                                m.kapasiteSorgula(kisi);
                                            } catch (NumberFormatException e) {
                                                System.out.println("Geçersiz sayı girdiniz, atlanıyor.");
                                            }
                                        }
                                        System.out.println();
                                    }
                                }
                                if (!found) System.out.println("Sistemde kayıtlı laboratuvar bulunamadı.");
                            } break;
                            case "3": {
                                boolean found = false;
                                for (AkademikMekan m : mekanlar) {
                                    if (m instanceof KonferansSalonu) {
                                        found = true;
                                        m.ozellikleriListele();
                                        System.out.println("Kapasite: " + m.getKapasite());
                                        System.out.print("Müsaitlik kontrolü için kişi sayısı girin (ENTER ile atla): ");
                                        String kisiStr = scanner.nextLine().trim();
                                        if (!kisiStr.isEmpty()) {
                                            try {
                                                int kisi = Integer.parseInt(kisiStr);
                                                m.kapasiteSorgula(kisi);
                                            } catch (NumberFormatException e) {
                                                System.out.println("Geçersiz sayı girdiniz, atlanıyor.");
                                            }
                                        }
                                        System.out.println();
                                    }
                                }
                                if (!found) System.out.println("Sistemde kayıtlı konferans salonu bulunamadı.");
                            } break;
                            case "0":
                                mekanCikis = true;
                                break;
                            default:
                                System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                        }
                    }
                    break;
                case "0":
                    cikis = true; // Ana menüye dön
                    break;
                default:
                    System.out.println("Hatalı seçim! Lütfen tekrar deneyin.");
            }
        }
    }

    // Yardımcı: Kullanıcıdan mekan ismi alıp konumu gösterir
    private static void konumSorgulaPrompt() {
        // Eksik konumları tamamla (varsa)
        ensureMekanKonumlariAtandi();
        System.out.print("Konumunu öğrenmek istediğiniz mekanın ismini giriniz: ");
        String isim = scanner.nextLine().trim();
        if (isim.isEmpty()) { System.out.println("Geçersiz isim."); return; }
        AkademikMekan m = findMekanByName(isim);
        if (m == null) {
            System.out.println("Böyle bir mekan bulunamadı: " + isim);
            return;
        }
        System.out.println("Mekan: " + m.getIsim());
        System.out.println("Konum: " + (m.getKonum() != null ? m.getKonum() : "(Tanımlı değil)"));
    }

    // Yardımcı: isim ile mekan arar (büyük/küçük harf duyarsız)
    private static AkademikMekan findMekanByName(String isim) {
        for (AkademikMekan m : mekanlar) {
            if (m.getIsim().equalsIgnoreCase(isim)) return m;
        }
        return null;
    }

    // Yardımcı: Eğer herhangi bir mekanın konumu boşsa tipine göre varsayılan konum atar
    private static void ensureMekanKonumlariAtandi() {
        String derslikOrnek = "B Blok 1. Kat";
        String labOrnek = "B Blok Zemin Kat";
        String konfOrnek = "Kampüs Merkezi Konferans Salonu";
        String ofisOrnek = "İdari/Birim Katı";
        for (AkademikMekan m : mekanlar) {
            if (m.getKonum() == null || m.getKonum().trim().isEmpty()) {
                if (m instanceof Derslik) m.setKonum(derslikOrnek);
                else if (m instanceof Laboratuvar) m.setKonum(labOrnek);
                else if (m instanceof KonferansSalonu) m.setKonum(konfOrnek);
                else m.setKonum(ofisOrnek);
            }
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

        // İdari personelleri JSON'dan yükle
        List<IdariPersonel> jsonIdari = JsonIslemleri.idariPersonelYukle();
        if (!jsonIdari.isEmpty()) {
            idariListesi.clear();
            idariListesi.addAll(jsonIdari);
        }

         // Bölüm derslerini ekle (Bilgisayar Mühendisliği örneği)
         bolumDersleri.clear();
         bolumDersleri.add(new Ders("Bmt101", "Programlamaya Giriş", 4));
         bolumDersleri.add(new Ders("Bmt102", "Veri Yapıları", 4));
         bolumDersleri.add(new Ders("Bmt201", "Algoritma Analizi", 4));
         bolumDersleri.add(new Ders("Bmt301", "İşletim Sistemleri", 4));

         System.out.println("Bilgi: JSON'dan " + jsonOgrencileri.size() + " öğrenci sisteme yüklendi.");
         // Oluşturulmuş/örnek mekanlar: 4 derslik, 2 lab, 1 konferans salonu
         mekanlar.clear();
         mekanlar.add(new Derslik("D-101", "A Blok 1. Kat", 40));
         mekanlar.add(new Derslik("D-102", "A Blok 1. Kat", 35));
         mekanlar.add(new Derslik("D-201", "A Blok 2. Kat", 45));
         mekanlar.add(new Derslik("D-202", "A Blok 2. Kat", 30));
         try {
             mekanlar.add(new Laboratuvar("LB-1", "B Blok Zemin Kat", 30, 25));
             mekanlar.add(new Laboratuvar("LB-2", "B Blok 1. Kat", 20, 18));
         } catch (Exception e) {
             System.out.println("Laboratuvar oluşturulurken hata: " + e.getMessage());
         }
         mekanlar.add(new KonferansSalonu("KS-1", "Kampüs Merkezi", 200));

         // Akademisyenlere ofis atama (her akademisyenin kendi ofisi)
         for (Akademisyen a : hocaListesi) {
             String ofisAdi = "Ofis - " + a.getAd() + " " + a.getSoyad();
             Ofis of = new Ofis(ofisAdi, "Akademik Blok 3. Kat", 1, a.getAd() + " " + a.getSoyad());
             mekanlar.add(of);
         }
         // İdari personel için ortak ofis
         Ofis ortakIdari = new Ofis("İdari Ofis", "Yönetim Katı", 10, "Ortak İdari Personel");
         mekanlar.add(ortakIdari);

         // Eksik konumları atayalım (JSON'dan gelen veya null konumlar için)
         ensureMekanKonumlariAtandi();
     }

  }
