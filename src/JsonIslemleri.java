import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class JsonIslemleri {
    private static final String DOSYA_YOLU = "ogrenciler.json";

    // 1. GÜNCELLENEN METOD: Akademisyenleri Yükle
    public static List<Akademisyen> akademisyenleriYukle() {
        List<Akademisyen> liste = new ArrayList<>();
        File file = new File("akademisyenler.json");

        if (!file.exists()){
            System.out.println("UYARI: akademisyenler.json dosyası bulunamadı.");
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
                String unvan = "AR_GOR";
                List<String> verilenDersler = new ArrayList<>(); // Yeni liste

                for (String parca : parcalar) {
                    if (!parca.contains(":")) continue;
                    String[] kv = parca.split(":");
                    String key = kv[0].trim();
                    String val = kv[1].trim();

                    switch (key) {
                        case "id": id = Long.parseLong(val); break;
                        case "unvan": unvan = val; break;
                        case "ad": ad = val; break;
                        case "soyad": soyad = val; break;
                        case "sicilNo": sicil = val; break;
                        case "brans": brans = val; break;
                        case "maas": maas = Double.parseDouble(val); break;
                        case "verdiği_dersler": // Yeni alan okuma
                            // "Fiz101;Fiz102" gibi gelebilir, biz virgül veya noktalı virgül ayıralım
                            // JSON örneğinizde virgül kullanılmış, ancak split mantığımız virgülleri bozabilir.
                            // Güvenli yöntem: Gelen stringi temizleyip ekleyelim.
                            // Eğer JSON yapınızda değer string ise "Fiz101;Fiz102" formatı daha güvenlidir.
                            // Burada basitçe string'i alıp varsa içindeki ayırıcıya göre bölüyoruz.
                            String[] dersler = val.split(";"); // JSON'da dersleri noktalı virgül ile ayırmanız daha sağlıklı olur
                            // Veya split logic'iniz virgülleri " , " yaptığı için burada dikkatli olmak gerek.
                            // Basit çözüm: Gelen değeri direkt ekleyelim, çoklu ders için JSON formatını
                            // "verdiği_dersler": "Fiz101-Fiz102" gibi yapıp tire ile ayırabilirsiniz.
                            // Şimdilik string içinde gelen değeri parse ediyoruz:
                            if(val.contains("-")) {
                                for(String d : val.split("-")) verilenDersler.add(d.trim());
                            } else {
                                verilenDersler.add(val);
                            }
                            break;
                    }
                }

                // ... (UnvanEnum çevrimi aynı kalacak) ...
                Unvan unvanEnum;
                try {
                    unvanEnum = Unvan.valueOf(unvan.toUpperCase());
                } catch (IllegalArgumentException e) {
                    unvanEnum = Unvan.AR_GOR;
                }

                if (id != 0) {
                    // Yeni Constructor çağrısı
                    liste.add(new Akademisyen(id, ad, soyad, LocalDate.now(), sicil, brans, maas, verilenDersler));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    // 2. YENİ EKLENEN METOD: Öğrencileri Kaydet (ogrencileriYukle metodunun altına ekleyin)
    public static void ogrencileriKaydet(List<Ogrenci> ogrenciler) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOSYA_YOLU))) {
            writer.write("[\n");
            for (int i = 0; i < ogrenciler.size(); i++) {
                Ogrenci ogr = ogrenciler.get(i);

                // 1. Satır: Temel Bilgiler (id, ad, soyad, no, sinif)
                writer.write("  {");
                writer.write("\"id\":" + ogr.getId() + ", ");
                writer.write("\"ad\":\"" + ogr.getAd() + "\", ");
                writer.write("\"soyad\":\"" + ogr.getSoyad() + "\", ");
                writer.write("\"no\":" + ogr.getOgrenciNo() + ", ");
                writer.write("\"sinif\":" + ogr.getSinif());

                // Notlar varsa alt satıra geç ve girinti yap
                if (!ogr.getDersNotlari().isEmpty()) {
                    writer.write(",\n    "); // Virgül at, aşağı in, 4 boşluk bırak

                    int c = 0;
                    for (Map.Entry<String, Double> entry : ogr.getDersNotlari().entrySet()) {
                        // Sayı tam sayı ise (100.0 yerine 100) .0 kısmını at
                        double not = entry.getValue();
                        String notYazisi = (not == Math.floor(not)) ? String.valueOf((int) not) : String.valueOf(not);

                        writer.write("\"" + entry.getKey() + "\":" + notYazisi);

                        // Son not değilse virgül koy
                        if (c++ < ogr.getDersNotlari().size() - 1) {
                            writer.write(", ");
                        }
                    }
                    // Notlar bittikten sonra süslü parantezi kapatmak için aşağı in
                    writer.write("\n  }");
                } else {
                    // Not yoksa direkt aynı satırda kapat
                    writer.write("}");
                }

                // Listede başka öğrenci varsa virgül koy
                if (i < ogrenciler.size() - 1) {
                    writer.write(",");
                }
                writer.newLine(); // Bir sonraki öğrenci için satır başı yap
            }
            writer.write("]");
            // System.out.println("Kayıt güncellendi: " + DOSYA_YOLU); // İsterseniz bu logu açabilirsiniz
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    // NOT: ogrencileriYukle ve notlariRaporla metodlarınız aynen kalabilir.
    // Ancak ogrencileriYukle metodunun en altında return liste; olduğundan emin olun.

    // (Buraya mevcut ogrencileriYukle ve notlariRaporla metodlarınızı kopyalamayı unutmayın!)
    // Kod bütünlüğü için onları buraya tekrar yazmadım ama silmeyin.
    public static void notlariRaporla(Ogrenci ogr) { /* Eski kodunuz */ }

    public static List<Ogrenci> ogrencileriYukle() {
        List<Ogrenci> list = new ArrayList<>();
        File file = new File(DOSYA_YOLU); // DOSYA_YOLU = "ogrenciler.json"

        if (!file.exists()) {
            System.out.println("UYARI: ogrenciler.json dosyası bulunamadı.");
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line.trim());

            String content = sb.toString().trim();
            // Köşeli parantezleri temizle
            content = content.replace("[", "").replace("]", "");

            if (content.isEmpty()) return list;

            // Objeleri ayır (}, yapısına göre)
            String[] objects = content.split("},");

            for (String objStr : objects) {
                // Süslü parantezleri temizle
                String cleanObj = objStr.replace("{", "").replace("}", "");

                long id = 0;
                String ad = "";
                String soyad = "";
                int no = 0;
                int sinif = 1;
                Map<String, Double> notlar = new HashMap<>();

                // Alanları virgülle ayır
                String[] fields = cleanObj.split(",");

                for (String field : fields) {
                    if (!field.contains(":")) continue;
                    String[] kv = field.split(":");
                    String key = kv[0].trim().replace("\"", "");
                    String val = kv[1].trim().replace("\"", "");

                    switch (key) {
                        case "id":
                            id = Long.parseLong(val);
                            break;
                        case "ad":
                            ad = val;
                            break;
                        case "soyad":
                            soyad = val;
                            break;
                        case "no":
                            no = Integer.parseInt(val);
                            break;
                        case "sinif":
                            sinif = Integer.parseInt(val);
                            break;
                        default:
                            // Tanımlı alanlar dışındakileri not olarak kabul et (Örn: Mat101V)
                            try {
                                notlar.put(key, Double.parseDouble(val));
                            } catch (NumberFormatException e) {
                                // Sayısal olmayan diğer alanları yoksay
                            }
                            break;
                    }
                }

                // Öğrenci nesnesini oluştur (Doğum tarihi JSON'da yok, şimdiki zaman atadık)
                Ogrenci ogr = new Ogrenci(id, ad, soyad, LocalDate.now(), no, sinif);

                // Notları ekle
                for (Map.Entry<String, Double> entry : notlar.entrySet()) {
                    ogr.notEkle(entry.getKey(), entry.getValue());
                }

                list.add(ogr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}