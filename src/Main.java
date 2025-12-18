import src.AkademikMekan;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{
    private static Scanner scanner = new Scanner(System.in);

    // Veri Depoları (Basit veritabanı simülasyonu)
    private static List<AkademikMekan> mekanlar = new ArrayList<>();
    private static VeriDeposu<Ogrenci> ogrenciDeposu = new VeriDeposu<>();
    private static List<Ders> dersListesi = new ArrayList<>();

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
                case "1": ogrenciMenusu(); break;
                case "2": akademisyenMenusu(); break;
                case "3": idariPersonelMenusu(); break;
                case "0": sistemAcik = false; break;
                default: System.out.println("Hatalı seçim!");
            }
        }
    }

    // --- 1. ÖĞRENCİ EKRANI ---
    private static void ogrenciMenusu() {
        System.out.println("\n--- ÖĞRENCİ PANELİ ---");
        System.out.println("1. Derslik/Ofis Sorgula");
        System.out.println("2. GANO Göster");
        System.out.println("3. Transkript Görüntüle");
        System.out.println("4. Ders Programı");
        System.out.println("0. Ana Menü");
        System.out.print("Seçim: ");

        String secim = scanner.nextLine();
        switch(secim) {
            case "1":
                System.out.println("Mekanlar Listeleniyor:");
                for(AkademikMekan m : mekanlar) m.ozellikleriListele();
                break;
            case "2":
                System.out.println("GANO: 3.45 (Demo Veri)");
                break;
            case "3":
                System.out.println("--- Transkript ---");
                for(Ders d : dersListesi) System.out.println(d + " : AA");
                break;
            case "4":
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
                System.out.println("Not girişi modülü aktif... (Önceki kodlarda yazmıştık)");
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
            case "0": return;
        }
    }

    // Demo verileri yükleme
    private static void veriYukle() {

        // Dersler
        dersListesi.add(new Ders("NYP101", "Nesne Yönelimli Programlama", 5));
        dersListesi.add(new Ders("MAT101", "Matematik I", 4));

        // Öğrenci (Polimorfizm örneği: Listeye LisansOgrenci ekliyoruz)
        ogrenciDeposu.ekle(new LisansOgrenci(1001, "Mehmet", "Can", 123, 2));
    }
}