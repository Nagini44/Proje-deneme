package service;

import model.Akademisyen;
import model.Ogrenci;
import model.IdariPersonel;
import model.Unvan;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class JsonIslemleri {
    private static final String DOSYA_YOLU = "ogrenciler.json";
    private static final String IDARI_DOSYA = "idariPersonel.json";

    // service/Raporlama.java (Yeni bir yardımcı sınıf oluşturabilirsiniz)

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
                        case "verdiği_dersler":
                            if(val.contains("-")) {
                                for(String d : val.split("-")) verilenDersler.add(d.trim());
                            } else {
                                verilenDersler.add(val);
                            }
                            break;
                    }
                }

                //
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

    //İdari Personel Yükle (idariPersonel.json)
    public static List<IdariPersonel> idariPersonelYukle() {
        List<IdariPersonel> list = new ArrayList<>();
        File file = new File(IDARI_DOSYA);

        if (!file.exists()) {
            System.out.println("UYARI: idariPersonel.json dosyası bulunamadı.");
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line.trim());

            String content = sb.toString().replace("[", "").replace("]", "");
            if (content.trim().isEmpty()) return list;

            String[] objects = content.split("},");
            for (String obj : objects) {
                String clean = obj.replace("{", "").replace("}", "");

                long id = 0;
                String ad = "", soyad = "", departman = "";

                String[] fields = clean.split(",");
                for (String field : fields) {
                    if (!field.contains(":")) continue;
                    String[] kv = field.split(":");
                    String key = kv[0].trim().replace("\"", "");
                    String val = kv[1].trim().replace("\"", "");
                    switch (key) {
                        case "id": id = Long.parseLong(val); break;
                        case "ad": ad = val; break;
                        case "soyad": soyad = val; break;
                        case "departman": departman = val; break;
                    }
                }

                if (id != 0) {
                    list.add(new IdariPersonel(id, ad, soyad, departman));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //İdari Personel Kaydet (basit format)
    public static void idariPersonelKaydet(List<IdariPersonel> liste) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(IDARI_DOSYA))) {
            writer.write("[\n");
            for (int i = 0; i < liste.size(); i++) {
                IdariPersonel p = liste.get(i);
                writer.write("  {\"id\":" + p.getId() + ", \"ad\":\"" + p.getAd() + "\", \"soyad\":\"" + p.getSoyad() + "\", \"departman\":\"" + p.getRolAdi() + "\"}");
                if (i < liste.size() - 1) writer.write(",");
                writer.newLine();
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println("idariPersonel dosyası yazılamadı: " + e.getMessage());
        }
    }

    // Akademisyenleri JSON'a kaydet
    public static void akademisyenleriKaydet(List<Akademisyen> liste) {
        File file = new File("akademisyenler.json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("[\n");
            for (int i = 0; i < liste.size(); i++) {
                Akademisyen a = liste.get(i);
                // verdiği_dersler'i '-' ile birleştiriyoruz
                String dersler = "";
                if (a.getVerilenDersler() != null && !a.getVerilenDersler().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < a.getVerilenDersler().size(); j++) {
                        if (j > 0) sb.append("-");
                        sb.append(a.getVerilenDersler().get(j));
                    }
                    dersler = sb.toString();
                }
                writer.write("  {\"id\":" + a.getId() + ", \"unvan\":\"" + /* try to preserve unvan? */ "AKADEMISYEN" + "\", \"ad\":\"" + a.getAd() + "\", \"soyad\":\"" + a.getSoyad() + "\", \"sicilNo\":\"" + a.getSicilNo() + "\", \"brans\":\"" + a.getBrans() + "\", \"maas\":" + a.getMaas() + ", \"verdiği_dersler\":\"" + dersler + "\"}");
                if (i < liste.size() - 1) writer.write(",");
                writer.newLine();
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println("akademisyenler.json kaydedilemedi: " + e.getMessage());
        }
    }

    // Öğrencileri Kaydet
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
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }


    public static void notlariRaporla(Ogrenci ogr) {
        if (ogr == null) {
            System.out.println("HATA: Öğrenci bilgisi boş.");
            return;
        }

        Map<String, Double> notMap = ogr.getDersNotlari();
        String dosyaAdi = ogr.getOgrenciNo() + "_transkript.txt";
        StringBuilder sb = new StringBuilder();

        sb.append("--- ÖĞRENCİ NOT DÖKÜMÜ ---\n");
        sb.append("Öğrenci: ").append(ogr.getAd()).append(" ").append(ogr.getSoyad()).append("\n");
        sb.append("Numara : ").append(ogr.getOgrenciNo()).append("\n");
        sb.append("Tarih  : ").append(java.time.LocalDate.now()).append("\n");
        sb.append("--------------------------\n");

        if (notMap == null || notMap.isEmpty()) {
            sb.append("(Bu öğrencinin kayıtlı notu yok.)\n");
            sb.append("--------------------------\n");
            sb.append("Genel Ortalama: 0.00\n");
            String out = sb.toString();
            System.out.println(out);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dosyaAdi))) {
                writer.write(out);
            } catch (IOException e) {
                System.out.println("Transkript yazılamadı: " + e.getMessage());
            }
            return;
        }

        Map<String, Double> vizeMap = new HashMap<>();
        Map<String, Double> finalMap = new HashMap<>();

        for (Map.Entry<String, Double> e : notMap.entrySet()) {
            String key = e.getKey();
            Double val = e.getValue();
            if (key == null) continue;
            key = key.trim();
            if (key.endsWith("V") || key.endsWith("v")) {
                String ders = key.substring(0, key.length() - 1);
                vizeMap.put(ders, val);
            } else if (key.endsWith("F") || key.endsWith("f")) {
                String ders = key.substring(0, key.length() - 1);
                finalMap.put(ders, val);
            } else {
                // model.Ders kodu net değilse vize olarak al
                vizeMap.put(key, val);
            }
        }

        java.util.Set<String> dersler = new java.util.HashSet<>();
        dersler.addAll(vizeMap.keySet());
        dersler.addAll(finalMap.keySet());

        double toplam = 0.0;
        int dersSayisi = 0;

        for (String d : dersler) {
            Double v = vizeMap.get(d);
            Double f = finalMap.get(d);
            double dersNotu;
            if (v != null && f != null) {
                dersNotu = v * 0.4 + f * 0.6;
            } else if (f != null) {
                dersNotu = f;
            } else if (v != null) {
                dersNotu = v;
            } else {
                continue;
            }
            toplam += dersNotu;
            dersSayisi++;

            String vStr = (v == null) ? "-" : String.format("%.2f", v);
            String fStr = (f == null) ? "-" : String.format("%.2f", f);
            sb.append(String.format("%sV    : %s\n", d, vStr));
            sb.append(String.format("%sF    : %s\n", d, fStr));
            sb.append("--------------------------\n");
        }

        double genelOrt = (dersSayisi == 0) ? 0.0 : (toplam / dersSayisi);
        sb.append("Genel Ortalama: ").append(String.format("%.2f", genelOrt)).append("\n");

        String harf;
        if (genelOrt >= 85) harf = "AA";
        else if (genelOrt >= 70) harf = "BA";
        else if (genelOrt >= 60) harf = "BB";
        else if (genelOrt >= 50) harf = "CB";
        else if (genelOrt >= 45) harf = "CC";
        else if (genelOrt >= 30) harf = "DC";
        else harf = "FF";
        sb.append("Harf Notu: ").append(harf).append("\n");

        String out = sb.toString();
        System.out.println("Transkript oluşturuldu");
        System.out.println(out);

        if (notMap == null || notMap.isEmpty()) {
            // ... (StringBuilder işlemleri aynı)
            System.out.println(out); // Konsola yazdırır

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dosyaAdi))) {
                writer.write(out);

                System.out.println("\n>> Transkript dosyası başarıyla oluşturuldu: " + dosyaAdi);
            } catch (IOException e) {
                System.out.println("Transkript yazılamadı: " + e.getMessage());
            }

    }
    }

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