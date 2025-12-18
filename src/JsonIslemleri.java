import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class JsonIslemleri {
    private static final String DOSYA_YOLU = "ogrenciler.json";

    // JsonIslemleri.java dosyasının içine (class parantezleri arasına) ekleyin
    public static void notlariRaporla(Ogrenci ogr) {
        // Dosya adı öğrenci numarasına göre dinamik olsun (Örn: 101_transkript.txt)
        String dosyaAdi = ogr.getOgrenciNo() + "_transkript.txt";

        // FileWriter ve BufferedWriter kullanarak dosyayı oluşturup yazıyoruz
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dosyaAdi))) {

            writer.write("--- ÖĞRENCİ NOT DÖKÜMÜ ---");
            writer.newLine(); // Alt satıra geç
            writer.write("Öğrenci: " + ogr.getAd() + " " + ogr.getSoyad());
            writer.newLine();
            writer.write("Numara : " + ogr.getOgrenciNo());
            writer.newLine();
            writer.write("Tarih  : " + java.time.LocalDate.now());
            writer.newLine();
            writer.write("--------------------------");
            writer.newLine();

            // Notları yazdırma döngüsü
            if (ogr.getDersNotlari().isEmpty()) {
                writer.write("Kayıtlı ders notu bulunmamaktadır.");
            } else {
                for (Map.Entry<String, Double> ders : ogr.getDersNotlari().entrySet()) {
                    // Örnek çıktı: Mat101 : 85.0
                    writer.write(String.format("%-10s : %.2f", ders.getKey(), ders.getValue()));
                    writer.newLine();
                }
            }

            writer.write("--------------------------");
            writer.newLine();
            writer.write("Genel Ortalama: " + ogr.notOrtalamasiHesapla());

            System.out.println("Başarılı: Notlar '" + dosyaAdi + "' dosyasına kaydedildi.");

        } catch (IOException e) {
            System.err.println("Dosya yazma hatası oluştu: " + e.getMessage());
        }
    }
    public static List<Akademisyen> akademisyenleriYukle() {
        List<Akademisyen> liste = new ArrayList<>();
        File file = new File("akademisyenler.json"); // Dosya adını kontrol edin

        if (!file.exists()){
            System.out.println("UYARI:akademisyenler.json dosyası bulunamadı.");
            return liste;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line.trim());

            String fullText = sb.toString().replace("[", "").replace("]", "").replace("\"", "");
            String[] objeler = fullText.split("}");

            for (String objeStr : objeler) {
                String temizObje = objeStr.replace(",{", "").replace("{", "").replace(",", " , ");
                if (temizObje.trim().isEmpty()) continue;

                String[] parcalar = temizObje.split(",");

                long id = 0;
                String ad = "", soyad = "", sicil = "", brans = "";
                double maas = 0.0;
                String unvan = "AR_GOR"; // Varsayılan

                for (String parca : parcalar) {
                    if (!parca.contains(":")) continue;
                    String[] kv = parca.split(":");
                    String key = kv[0].trim();
                    String val = kv[1].trim();

                    switch (key) {
                        case "id": id = Long.parseLong(val); break;
                        case "unvan": unvan = val; break; // JSON'dan string olarak oku
                        case "ad": ad = val; break;
                        case "soyad": soyad = val; break;
                        case "sicilNo": sicil = val; break;
                        case "brans": brans = val; break;
                        case "maas": maas = Double.parseDouble(val); break;
                    }
                }
                // String olarak gelen unvanı Enum'a çeviriyoruz
                Unvan unvanEnum;
                try {
                    unvanEnum = Unvan.valueOf(unvan.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Bilinmeyen unvan: " + unvan+ ". Varsayılan atandı.");
                    unvanEnum = Unvan.AR_GOR;
                }

                if (id != 0) {
                    // Akademisyen constructor sırası: id, ad, soyad, tarih, sicil, brans, maas, unvan
                    liste.add(new Akademisyen(id, ad, soyad, LocalDate.now(), sicil, brans, maas));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    public static List<Ogrenci> ogrencileriYukle() {
        List<Ogrenci> liste = new ArrayList<>();
        File file = new File(DOSYA_YOLU);

        if (!file.exists()) {
            System.out.println("UYARI: " + DOSYA_YOLU + " dosyası bulunamadı!");
            return liste;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // 1. ADIM: Dosyadaki tüm satırları tek bir uzun metin haline getir
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line.trim());
            }

            String fullText = sb.toString();
            // Köşeli parantezleri ve tırnakları temizle
            fullText = fullText.replace("[", "").replace("]", "").replace("\"", "");

            // 2. ADIM: Objeleri birbirinden ayır ("}," işaretine göre)
            // JSON yapısında objeler "}" ile biter. Buna göre bölüyoruz.
            String[] objeler = fullText.split("}");

            for (String objeStr : objeler) {
                // Başlangıçtaki süslü parantezleri veya virgülleri temizle
                String temizObje = objeStr.replace(",{", "").replace("{", "").replace(",", " , ");
                // (Virgüllerin etrafına boşluk koyduk ki split hata yapmasın)

                if (temizObje.trim().isEmpty()) continue;

                // 3. ADIM: Özellikleri ayır
                String[] parcalar = temizObje.split(",");

                long id = 0;
                String ad = "", soyad = "";
                int no = 0;
                int sinif = 1;
                Map<String, Double> tempNotlar = new HashMap<>();

                for (String parca : parcalar) {
                    if (!parca.contains(":")) continue;

                    String[] kv = parca.split(":");
                    String key = kv[0].trim();
                    String val = kv[1].trim();

                    // Boş veri kontrolü
                    if(key.isEmpty() || val.isEmpty()) continue;

                    switch (key) {
                        case "id": id = Long.parseLong(val); break;
                        case "ad": ad = val; break;
                        case "soyad": soyad = val; break;
                        case "no": no = Integer.parseInt(val); break;
                        case "sinif": sinif = Integer.parseInt(val); break;
                        default:
                            // Bilinmeyen alanlar ders notudur (Mat101V vb.)
                            try {
                                // "G" gibi metinleri filtrelemek için kontrol
                                double notDegeri = Double.parseDouble(val);
                                tempNotlar.put(key, notDegeri);
                            } catch (NumberFormatException e) {
                                // Sayısal olmayan notları (G, - vb.) atla
                            }
                            break;
                    }
                }

                // 4. ADIM: Öğrenciyi oluştur ve listeye ekle
                if (no != 0) { // Geçerli bir öğrenciyse
                    Ogrenci ogr = new Ogrenci(id, ad, soyad, LocalDate.now(), no, sinif);
                    for (Map.Entry<String, Double> entry : tempNotlar.entrySet()) {
                        ogr.notEkle(entry.getKey(), entry.getValue());
                    }
                    liste.add(ogr);
                }
            }

        } catch (Exception e) {
            System.err.println("JSON Okuma Hatası: " + e.getMessage());
            e.printStackTrace();
        }
        return liste;
    }
}