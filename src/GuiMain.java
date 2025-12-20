import model.*;
import service.JsonIslemleri;
import service.VeriDeposu;
import util.TarihYardimcisi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuiMain {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Veri kaynakları (GUI kendi kopyalarını kullanır)
    private List<Akademisyen> hocaListesi;
    private List<IdariPersonel> idariListesi;
    private VeriDeposu<Ogrenci> ogrenciDeposu;
    private List<AkademikMekan> mekanlar;
    private List<Ders> bolumDersleri;

    private JLabel statusLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GuiMain app = new GuiMain();
            app.initializeData();
            app.buildGui();
            app.frame.setVisible(true);
        });
    }

    private void initializeData() {
        // JSON'dan yükle
        hocaListesi = JsonIslemleri.akademisyenleriYukle();
        idariListesi = JsonIslemleri.idariPersonelYukle();
        List<Ogrenci> jsonOgr = JsonIslemleri.ogrencileriYukle();

        ogrenciDeposu = new VeriDeposu<>();
        for (Ogrenci o : jsonOgr) ogrenciDeposu.ekle(o);

        // Bölüm dersleri
        bolumDersleri = new ArrayList<>();
        bolumDersleri.add(new Ders("Bmt101", "Programlamaya Giriş", 4));
        bolumDersleri.add(new Ders("Bmt102", "Veri Yapıları", 4));
        bolumDersleri.add(new Ders("Bmt201", "Algoritma Analizi", 4));
        bolumDersleri.add(new Ders("Bmt301", "İşletim Sistemleri", 4));

        // Mekanlar (basit kopya)
        mekanlar = new ArrayList<>();
        mekanlar.add(new Derslik("D-101", "A Blok 1. Kat", 40));
        mekanlar.add(new Derslik("D-102", "A Blok 1. Kat", 35));
        mekanlar.add(new Derslik("D-201", "A Blok 2. Kat", 45));
        mekanlar.add(new Derslik("D-202", "A Blok 2. Kat", 30));
        try {
            mekanlar.add(new Laboratuvar("LB-1", "B Blok Zemin Kat", 30, 25));
            mekanlar.add(new Laboratuvar("LB-2", "B Blok 1. Kat", 20, 18));
        } catch (Exception e) {
            // ignore
        }
        mekanlar.add(new KonferansSalonu("KS-1", "Kampüs Merkezi", 200));

        for (Akademisyen a : hocaListesi) {
            String ofisAdi = "Ofis - " + a.getAd() + " " + a.getSoyad();
            Ofis of = new Ofis(ofisAdi, "Akademik Blok 3. Kat", 1, a.getAd() + " " + a.getSoyad());
            mekanlar.add(of);
        }
        Ofis ortak = new Ofis("İdari Ofis", "Yönetim Katı", 10, "Ortak İdari");
        mekanlar.add(ortak);

        ensureMekanKonumlariAtandi();
    }

    private void buildGui() {
        frame = new JFrame("Üniversite OBS - Swing Arayüz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        // Daha hoş, koyu bir arka plan ve genel font ayarı
        frame.getContentPane().setBackground(new Color(34, 47, 62));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(34, 47, 62));

        JPanel home = buildHomePanel();
        JPanel studentPanel = buildStudentPanel();
        JPanel akademisyenPanel = buildAkademisyenPanel();
        JPanel idariPanel = buildIdariPanel();

        // Panelleri doğrudan ekle (gazi.png overlay'i kaldırıldı)
        mainPanel.add(home, "home");
        mainPanel.add(studentPanel, "student");
        mainPanel.add(akademisyenPanel, "akademisyen");
        mainPanel.add(idariPanel, "idari");

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        statusLabel = new JLabel();
        statusLabel.setBorder(new EmptyBorder(6,6,6,6));
        statusLabel.setForeground(Color.WHITE);
        updateStatus();
        // Saat güncellemesi
        Timer t = new Timer(1000, e -> updateStatus());
        t.start();

        frame.getContentPane().add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel buildHomePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(34, 47, 62));

        // Başlığı istenen şekilde güncelle
        JLabel title = new JLabel("GAZİ ÜNİVERSİTESİ BİLGİ SİSTEMİNE HOŞGELDİNİZ", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(Color.WHITE);

        // Logo: önce classpath'ten, yoksa proje içinden yüklemeyi dene
        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(new Color(34,47,62));
        URL logoUrl = GuiMain.class.getResource("/images/gazi_logo.png");
        JLabel logoLabel = null;
        if (logoUrl != null) {
            ImageIcon raw = new ImageIcon(logoUrl);
            Image img = raw.getImage();
            int w = 220;
            int h = raw.getIconWidth() > 0 ? (int)((double)raw.getIconHeight()*w/raw.getIconWidth()) : 80;
            Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaled));
        } else {
            java.io.File f = new java.io.File("resources/images/gazi_logo.png");
            if (f.exists()) {
                ImageIcon raw = new ImageIcon(f.getAbsolutePath());
                Image img = raw.getImage();
                int w = 220;
                int h = raw.getIconWidth() > 0 ? (int)((double)raw.getIconHeight()*w/raw.getIconWidth()) : 80;
                Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                logoLabel = new JLabel(new ImageIcon(scaled));
            }
        }
        if (logoLabel != null) {
            logoLabel.setBorder(new EmptyBorder(12,12,6,12));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            north.add(logoLabel, BorderLayout.NORTH);
        }
        title.setBorder(new EmptyBorder(12,10,20,10));
        north.add(title, BorderLayout.SOUTH);
        p.add(north, BorderLayout.NORTH);

        // Butonları ortada dikey olarak daha büyük hale getir
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(34, 47, 62));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);
        gbc.gridx = 0; gbc.gridy = 0;

        JButton ogrBtn = new JButton("Öğrenci Girişi");
        JButton hocaBtn = new JButton("Akademisyen Girişi");
        JButton idariBtn = new JButton("İdari Personel Girişi");

        Dimension btnSize = new Dimension(260, 64);
        Font btnFont = ogrBtn.getFont().deriveFont(Font.PLAIN, 16f);
        for (JButton b : new JButton[]{ogrBtn,hocaBtn,idariBtn}) {
            b.setPreferredSize(btnSize);
            b.setFont(btnFont);
            b.setBackground(new Color(52, 152, 219));
            b.setForeground(Color.BLACK);
            b.setFocusPainted(false);
        }

        ogrBtn.addActionListener(e -> cardLayout.show(mainPanel, "student"));
        hocaBtn.addActionListener(e -> cardLayout.show(mainPanel, "akademisyen"));
        idariBtn.addActionListener(e -> cardLayout.show(mainPanel, "idari"));

        center.add(ogrBtn, gbc);
        gbc.gridy = 1; center.add(hocaBtn, gbc);
        gbc.gridy = 2; center.add(idariBtn, gbc);

        p.add(center, BorderLayout.CENTER);
        return p;
    }

    // -- ÖĞRENCİ PANELİ --
    private JPanel buildStudentPanel() {
        // Lokal kart düzeni: 'login' ve 'main'
        JPanel container = new JPanel(new CardLayout());
        container.setBackground(new Color(44,62,80));

        // --- LOGIN KARTI ---
        JPanel loginCard = new JPanel(new BorderLayout(12,12));
        loginCard.setBorder(new EmptyBorder(12,12,12,12));
        loginCard.setBackground(new Color(44,62,80));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBackground(new Color(44,62,80));
        JLabel lbl = new JLabel("Öğrenci Numarası:"); lbl.setForeground(Color.WHITE);
        JTextField noField = new JTextField(12); noField.setPreferredSize(new Dimension(140,26)); noField.setForeground(Color.BLACK);
        JButton loginBtn = new JButton("Giriş");
        JButton exitBtn = new JButton("Çıkış");
        loginBtn.setBackground(new Color(52,152,219)); loginBtn.setForeground(Color.BLACK); loginBtn.setPreferredSize(new Dimension(100,30));
        exitBtn.setBackground(new Color(149,165,166)); exitBtn.setForeground(Color.BLACK); exitBtn.setPreferredSize(new Dimension(80,30));

        top.add(lbl); top.add(noField); top.add(loginBtn); top.add(exitBtn);
        loginCard.add(top, BorderLayout.NORTH);

        JTextArea loginNote = new JTextArea("Lütfen öğrenci numaranızla giriş yapınız.");
        loginNote.setEditable(false); loginNote.setBackground(new Color(44,62,80)); loginNote.setForeground(Color.WHITE);
        loginNote.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        loginCard.add(loginNote, BorderLayout.CENTER);

        // --- MAIN KARTI (öğrenci detayları ve işlemler) ---
        JPanel mainCard = new JPanel(new BorderLayout(12,12));
        mainCard.setBorder(new EmptyBorder(12,12,12,12));
        mainCard.setBackground(new Color(44,62,80));

        JPanel mainTop = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        mainTop.setBackground(new Color(44,62,80));
        JButton backBtn = new JButton("Geri"); backBtn.setPreferredSize(new Dimension(80,30)); backBtn.setBackground(new Color(189,195,199)); backBtn.setForeground(Color.BLACK);
        JButton exitBtn2 = new JButton("Çıkış"); exitBtn2.setPreferredSize(new Dimension(80,30)); exitBtn2.setBackground(new Color(149,165,166)); exitBtn2.setForeground(Color.BLACK);
        mainTop.add(backBtn); mainTop.add(exitBtn2);
        mainCard.add(mainTop, BorderLayout.NORTH);

        JTextArea infoArea = new JTextArea(); infoArea.setEditable(false); infoArea.setLineWrap(true); infoArea.setWrapStyleWord(true);
        infoArea.setBackground(new Color(44,62,80)); infoArea.setForeground(Color.WHITE);
        infoArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70,85,100)), BorderFactory.createEmptyBorder(8,8,8,8)));
        JScrollPane sp = new JScrollPane(infoArea); sp.setBorder(BorderFactory.createEmptyBorder());
        mainCard.add(sp, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT,8,8)); actions.setBackground(new Color(44,62,80));
        JButton ganoBtn = new JButton("GANO ve Harf Notu Göster"); JButton transBtn = new JButton("Transkript Oluştur"); JButton konumBtn = new JButton("Konum Sorgula");
        for (JButton b : new JButton[]{ganoBtn,transBtn,konumBtn}) { b.setBackground(new Color(52,152,219)); b.setForeground(Color.BLACK); b.setPreferredSize(new Dimension(200,34)); }
        actions.add(ganoBtn); actions.add(transBtn); actions.add(konumBtn);
        mainCard.add(actions, BorderLayout.SOUTH);

        // Aktif öğrenci referansı
        final Ogrenci[] aktif = new Ogrenci[1];

        // Buton davranışları
        // Çıkış: her durumda ana ekrana dön
        exitBtn.addActionListener(e -> goHome());
        exitBtn2.addActionListener(e -> goHome());
        // Giriş: başarılı ise main karta geç
        loginBtn.addActionListener(e -> {
            String txt = noField.getText().trim();
            if (txt.isEmpty()) { JOptionPane.showMessageDialog(frame, "Lütfen numara girin"); return; }
            try {
                int num = Integer.parseInt(txt);
                aktif[0] = null;
                for (Ogrenci o : ogrenciDeposu.getListe()) if (o.getOgrenciNo() == num) { aktif[0] = o; break; }
                if (aktif[0] == null) JOptionPane.showMessageDialog(frame, "Öğrenci bulunamadı");
                else {
                    infoArea.setText(joinStudentInfo(aktif[0]));
                    CardLayout cl = (CardLayout) container.getLayout(); cl.show(container, "main");
                }
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Geçersiz numara"); }
        });

        // Main karttaki Geri: birim içi geri -> login kartına dön
        backBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) container.getLayout();
            cl.show(container, "login");
        });

        // Main kart butonları
        ganoBtn.addActionListener(e -> {
            if (aktif[0] == null) { JOptionPane.showMessageDialog(frame, "Önce öğrenci girişi yapın"); return; }
            StringBuilder sb = new StringBuilder(); sb.append(joinStudentInfo(aktif[0])); double ort = aktif[0].notOrtalamasiHesapla(); sb.append(String.format("\nGenel Ortalama: %.2f\n", ort)); sb.append("Harf Notu: " + hesaplaHarfNotu(ort) + "\n"); infoArea.setText(sb.toString());
        });
        transBtn.addActionListener(e -> { if (aktif[0] == null) { JOptionPane.showMessageDialog(frame, "Öğrenci seçili değil"); return; } JsonIslemleri.notlariRaporla(aktif[0]); JOptionPane.showMessageDialog(frame, "Transkript oluşturuldu (konsola ve dosyaya bakınız)"); });
        konumBtn.addActionListener(e -> { String isim = JOptionPane.showInputDialog(frame, "Konumunu öğrenmek istediğiniz mekan ismi:"); if (isim == null || isim.trim().isEmpty()) return; AkademikMekan m = findMekanByName(isim.trim()); if (m == null) JOptionPane.showMessageDialog(frame, "Mekan bulunamadı: " + isim); else JOptionPane.showMessageDialog(frame, "Mekan: " + m.getIsim() + "\nKonum: " + (m.getKonum()==null?"(Tanımlı değil)":m.getKonum())); });

        // Kartları ekle
        container.add(loginCard, "login");
        container.add(mainCard, "main");
        ((CardLayout)container.getLayout()).show(container, "login");
        return container;
    }

    // -- AKADEMİSYEN PANELİ --
    private JPanel buildAkademisyenPanel() {
        JPanel container = new JPanel(new CardLayout());
        container.setBackground(new Color(44,62,80));

        // Login kartı
        JPanel loginCard = new JPanel(new BorderLayout(12,12)); loginCard.setBorder(new EmptyBorder(12,12,12,12)); loginCard.setBackground(new Color(44,62,80));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10)); top.setBackground(new Color(44,62,80));
        JLabel lbl = new JLabel("Akademisyen ID:"); lbl.setForeground(Color.WHITE);
        JTextField idField = new JTextField(12); idField.setPreferredSize(new Dimension(140,26)); idField.setForeground(Color.BLACK);
        JButton loginBtn = new JButton("Giriş"); JButton exitBtn = new JButton("Çıkış");
        loginBtn.setBackground(new Color(52,152,219)); loginBtn.setForeground(Color.BLACK); exitBtn.setBackground(new Color(149,165,166)); exitBtn.setForeground(Color.BLACK);
        top.add(lbl); top.add(idField); top.add(loginBtn); top.add(exitBtn);
        loginCard.add(top, BorderLayout.NORTH);
        JTextArea note = new JTextArea("Lütfen ID girerek giriş yapınız."); note.setEditable(false); note.setBackground(new Color(44,62,80)); note.setForeground(Color.WHITE); note.setBorder(BorderFactory.createEmptyBorder(8,8,8,8)); loginCard.add(note, BorderLayout.CENTER);

        // Main kartı
        JPanel mainCard = new JPanel(new BorderLayout(12,12)); mainCard.setBorder(new EmptyBorder(12,12,12,12)); mainCard.setBackground(new Color(44,62,80));
        JPanel mainTop = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10)); mainTop.setBackground(new Color(44,62,80));
        JButton backBtn = new JButton("Geri"); backBtn.setPreferredSize(new Dimension(80,30)); backBtn.setBackground(new Color(189,195,199)); backBtn.setForeground(Color.BLACK);
        JButton exitBtn2 = new JButton("Çıkış"); exitBtn2.setPreferredSize(new Dimension(80,30)); exitBtn2.setBackground(new Color(149,165,166)); exitBtn2.setForeground(Color.BLACK);
        mainTop.add(backBtn); mainTop.add(exitBtn2); mainCard.add(mainTop, BorderLayout.NORTH);

        JTextArea infoArea = new JTextArea(); infoArea.setEditable(false); infoArea.setLineWrap(true); infoArea.setWrapStyleWord(true);
        infoArea.setBackground(new Color(44,62,80)); infoArea.setForeground(Color.WHITE); infoArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70,85,100)), BorderFactory.createEmptyBorder(8,8,8,8)));
        JScrollPane sp = new JScrollPane(infoArea); sp.setBorder(BorderFactory.createEmptyBorder()); mainCard.add(sp, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT,8,8)); actions.setBackground(new Color(44,62,80));
        JButton notGirBtn = new JButton("Not Girişi Yap"); JButton yayinBtn = new JButton("Akademik Yayınlar"); JButton konumBtn = new JButton("Konum Sorgula");
        for (JButton b : new JButton[]{notGirBtn, yayinBtn, konumBtn}) { b.setBackground(new Color(52,152,219)); b.setForeground(Color.BLACK); b.setPreferredSize(new Dimension(180,34)); }
        actions.add(yayinBtn); actions.add(notGirBtn); actions.add(konumBtn); mainCard.add(actions, BorderLayout.SOUTH);

        final Akademisyen[] aktif = new Akademisyen[1];

        exitBtn.addActionListener(e -> goHome());
        exitBtn2.addActionListener(e -> goHome());

        loginBtn.addActionListener(e -> {
            String txt = idField.getText().trim(); if (txt.isEmpty()) { JOptionPane.showMessageDialog(frame, "Lütfen ID girin"); return; }
            try {
                long id = Long.parseLong(txt); aktif[0] = null; for (Akademisyen a : hocaListesi) if (a.getId() == id) { aktif[0] = a; break; }
                if (aktif[0] == null) JOptionPane.showMessageDialog(frame, "Akademisyen bulunamadı"); else { infoArea.setText(akademisyenToString(aktif[0])); ((CardLayout)container.getLayout()).show(container, "main"); }
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Geçersiz ID"); }
        });

        backBtn.addActionListener(e -> { ((CardLayout)container.getLayout()).show(container, "login"); });

        yayinBtn.addActionListener(e -> {
            if (aktif[0] == null) { JOptionPane.showMessageDialog(frame, "Önce giriş yapın"); return; }
            java.util.List<String> yayinlar = aktif[0].getYayinlar();
            if (yayinlar == null || yayinlar.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Bu akademisyenin kayıtlı yayını yok.");
                return;
            }
            StringBuilder sy = new StringBuilder();
            for (String y : yayinlar) sy.append("- ").append(y).append('\n');
            JTextArea ta = new JTextArea(sy.toString()); ta.setEditable(false); ta.setLineWrap(true); ta.setWrapStyleWord(true);
            ta.setCaretPosition(0);
            JOptionPane.showMessageDialog(frame, new JScrollPane(ta), "Akademik Yayınlar - " + aktif[0].getAd() + " " + aktif[0].getSoyad(), JOptionPane.INFORMATION_MESSAGE);
        });
        notGirBtn.addActionListener(e -> {
            if (aktif[0] == null) { JOptionPane.showMessageDialog(frame, "Önce giriş yapın"); return; }
            List<Ogrenci> tum = ogrenciDeposu.getListe(); String[] ogrOptions = new String[tum.size()]; for (int i=0;i<tum.size();i++) ogrOptions[i] = tum.get(i).getOgrenciNo() + " - " + tum.get(i).getAd() + " " + tum.get(i).getSoyad();
            String sel = (String) JOptionPane.showInputDialog(frame, "Öğrenci seçin:", "Öğrenci", JOptionPane.PLAIN_MESSAGE, null, ogrOptions, ogrOptions.length>0?ogrOptions[0]:null);
            if (sel == null) return; int no = Integer.parseInt(sel.split(" ")[0]); Ogrenci hedef = null; for (Ogrenci o : tum) if (o.getOgrenciNo() == no) { hedef = o; break; }
            if (hedef == null) { JOptionPane.showMessageDialog(frame, "Öğrenci bulunamadı"); return; }
            List<String> dersler = aktif[0].getVerilenDersler(); if (dersler == null || dersler.isEmpty()) { JOptionPane.showMessageDialog(frame, "Bu akademisyenin verdiği ders yok"); return; }
            String ders = (String) JOptionPane.showInputDialog(frame, "Ders seçin:", "Ders", JOptionPane.PLAIN_MESSAGE, null, dersler.toArray(), dersler.get(0)); if (ders == null) return;
            String vStr = JOptionPane.showInputDialog(frame, ders + " Vize notu (boş bırakmak için iptal):"); if (vStr != null && !vStr.trim().isEmpty()) { try { double v = Double.parseDouble(vStr.trim()); aktif[0].notGir(hedef, ders + "V", v); } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Geçersiz vize notu veya yetki yok"); } }
            String fStr = JOptionPane.showInputDialog(frame, ders + " Final notu (boş bırakmak için iptal):"); if (fStr != null && !fStr.trim().isEmpty()) { try { double f = Double.parseDouble(fStr.trim()); aktif[0].notGir(hedef, ders + "F", f); } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Geçersiz final notu veya yetki yok"); } }
            JsonIslemleri.ogrencileriKaydet(ogrenciDeposu.getListe()); JOptionPane.showMessageDialog(frame, "Not girişi tamamlandı ve kaydedildi.");
        });

        konumBtn.addActionListener(e -> { String isim = JOptionPane.showInputDialog(frame, "Konumunu öğrenmek istediğiniz mekan ismi:"); if (isim == null || isim.trim().isEmpty()) return; AkademikMekan m = findMekanByName(isim.trim()); if (m == null) JOptionPane.showMessageDialog(frame, "Mekan bulunamadı: " + isim); else JOptionPane.showMessageDialog(frame, "Mekan: " + m.getIsim() + "\nKonum: " + (m.getKonum()==null?"(Tanımlı değil)":m.getKonum())); });

        container.add(loginCard, "login"); container.add(mainCard, "main"); ((CardLayout)container.getLayout()).show(container, "login");
        return container;
    }

    // -- İDARİ PERSONEL PANELİ --
    private JPanel buildIdariPanel() {
        JPanel container = new JPanel(new CardLayout()); container.setBackground(new Color(44,62,80));

        JPanel loginCard = new JPanel(new BorderLayout(12,12)); loginCard.setBorder(new EmptyBorder(12,12,12,12)); loginCard.setBackground(new Color(44,62,80));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10)); top.setBackground(new Color(44,62,80));
        JLabel lbl = new JLabel("ID seçin:"); lbl.setForeground(Color.WHITE);
        String[] ids = new String[idariListesi.size()]; for (int i=0;i<idariListesi.size();i++) ids[i] = idariListesi.get(i).getId() + " - " + idariListesi.get(i).getAd() + " " + idariListesi.get(i).getSoyad();
        JComboBox<String> combo = new JComboBox<>(ids);
        JButton enterBtn = new JButton("Giriş"); JButton exitBtn = new JButton("Çıkış"); enterBtn.setBackground(new Color(52,152,219)); enterBtn.setForeground(Color.BLACK); exitBtn.setBackground(new Color(149,165,166)); exitBtn.setForeground(Color.BLACK);
        top.add(lbl); top.add(combo); top.add(enterBtn); top.add(exitBtn); loginCard.add(top, BorderLayout.NORTH);
        JTextArea note = new JTextArea("Idari personel seçip giriş yapınız."); note.setEditable(false); note.setBackground(new Color(44,62,80)); note.setForeground(Color.WHITE); note.setBorder(BorderFactory.createEmptyBorder(8,8,8,8)); loginCard.add(note, BorderLayout.CENTER);

        JPanel mainCard = new JPanel(new BorderLayout(12,12)); mainCard.setBorder(new EmptyBorder(12,12,12,12)); mainCard.setBackground(new Color(44,62,80));
        JPanel mainTop = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10)); mainTop.setBackground(new Color(44,62,80)); JButton backBtn = new JButton("Geri"); backBtn.setPreferredSize(new Dimension(80,30)); backBtn.setBackground(new Color(189,195,199)); backBtn.setForeground(Color.BLACK); JButton exitBtn2 = new JButton("Çıkış"); exitBtn2.setPreferredSize(new Dimension(80,30)); exitBtn2.setBackground(new Color(149,165,166)); exitBtn2.setForeground(Color.BLACK); mainTop.add(backBtn); mainTop.add(exitBtn2); mainCard.add(mainTop, BorderLayout.NORTH);

        JTextArea infoArea = new JTextArea(); infoArea.setEditable(false); infoArea.setLineWrap(true); infoArea.setWrapStyleWord(true);
        infoArea.setBackground(new Color(44,62,80)); infoArea.setForeground(Color.WHITE); infoArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70,85,100)), BorderFactory.createEmptyBorder(8,8,8,8)));
        JScrollPane sp = new JScrollPane(infoArea); sp.setBorder(BorderFactory.createEmptyBorder()); mainCard.add(sp, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT,8,8)); actions.setBackground(new Color(44,62,80)); JButton atamaMekanBtn = new JButton("Dersi Mekana Ata"); JButton atamaHocaBtn = new JButton("Hoca Ata"); JButton mekanOzBtn = new JButton("Mekan Özellikleri"); JButton konumBtn = new JButton("Konum Sorgula"); for (JButton b : new JButton[]{atamaMekanBtn, atamaHocaBtn, mekanOzBtn, konumBtn}) { b.setBackground(new Color(52,152,219)); b.setForeground(Color.BLACK); b.setPreferredSize(new Dimension(180,34)); } actions.add(atamaMekanBtn); actions.add(atamaHocaBtn); actions.add(mekanOzBtn); actions.add(konumBtn); mainCard.add(actions, BorderLayout.SOUTH);

        final IdariPersonel[] aktif = new IdariPersonel[1];

        exitBtn.addActionListener(e -> goHome()); exitBtn2.addActionListener(e -> goHome());

        enterBtn.addActionListener(e -> {
            int idx = combo.getSelectedIndex(); if (idx < 0) return; aktif[0] = idariListesi.get(idx); infoArea.setText(aktif[0].getAd() + " " + aktif[0].getSoyad() + " - " + aktif[0].getRolAdi()); ((CardLayout)container.getLayout()).show(container, "main");
        });

        backBtn.addActionListener(e -> { ((CardLayout)container.getLayout()).show(container, "login"); });

        atamaMekanBtn.addActionListener(e -> {
            // Ders numarası soran eski prompt kaldırıldı; doğrudan ders seçimi gösterilecek
            if (aktif[0] == null) { JOptionPane.showMessageDialog(frame, "Önce giriş yapın"); return; }
            String[] dersOpt = new String[bolumDersleri.size()]; for (int i=0;i<bolumDersleri.size();i++) dersOpt[i] = bolumDersleri.get(i).getKod() + " - " + bolumDersleri.get(i).getAd();
            String dersSel = (String) JOptionPane.showInputDialog(frame, "Hangi dersi atamak istersiniz?", "Ders Seç", JOptionPane.PLAIN_MESSAGE, null, dersOpt, dersOpt[0]); if (dersSel == null) return; String dersKod = dersSel.split(" ")[0];
            String[] mekOpt = new String[mekanlar.size()]; for (int i=0;i<mekanlar.size();i++) mekOpt[i] = mekanlar.get(i).getIsim(); String mekSel = (String) JOptionPane.showInputDialog(frame, "Hangi mekana atamak istersiniz?", "Mekan Seç", JOptionPane.PLAIN_MESSAGE, null, mekOpt, mekOpt[0]); if (mekSel == null) return; AkademikMekan hedef = findMekanByName(mekSel); if (hedef == null) { JOptionPane.showMessageDialog(frame, "Mekan bulunamadı"); return; } boolean ok = hedef.dersAtama(dersKod, aktif[0].getAd() + " " + aktif[0].getSoyad()); if (ok) aktif[0].dersAtamaYap(dersSel, hedef.getIsim()); JOptionPane.showMessageDialog(frame, ok?"Atama başarılı":"Atama başarısız");
        });

        atamaHocaBtn.addActionListener(e -> {
            if (aktif[0] == null) { JOptionPane.showMessageDialog(frame, "Önce giriş yapın"); return; }
            String[] dersOpt = new String[bolumDersleri.size()]; for (int i=0;i<bolumDersleri.size();i++) dersOpt[i] = bolumDersleri.get(i).getKod() + " - " + bolumDersleri.get(i).getAd(); String dersSel = (String) JOptionPane.showInputDialog(frame, "Hoca atamak istediğiniz ders:", "Ders", JOptionPane.PLAIN_MESSAGE, null, dersOpt, dersOpt[0]); if (dersSel == null) return; String dersKod = dersSel.split(" ")[0]; String[] hocaOpt = new String[hocaListesi.size()]; for (int i=0;i<hocaListesi.size();i++) hocaOpt[i] = hocaListesi.get(i).getId() + " - " + hocaListesi.get(i).getAd() + " " + hocaListesi.get(i).getSoyad(); String hocaSel = (String) JOptionPane.showInputDialog(frame, "Hoca seçin:", "Hoca", JOptionPane.PLAIN_MESSAGE, null, hocaOpt, hocaOpt.length>0?hocaOpt[0]:null); if (hocaSel == null) return; long hid = Long.parseLong(hocaSel.split(" ")[0]); Akademisyen ak = null; for (Akademisyen a : hocaListesi) if (a.getId() == hid) { ak = a; break; } if (ak == null) { JOptionPane.showMessageDialog(frame, "Akademisyen bulunamadı"); return; } if (ak.getVerilenDersler() == null) { JOptionPane.showMessageDialog(frame, "Akademisyenin verdiği ders listesi null", "Hata", JOptionPane.ERROR_MESSAGE); return; } if (ak.getVerilenDersler().contains(dersKod)) { JOptionPane.showMessageDialog(frame, "Bu akademisyen zaten bu dersi veriyor"); return; } ak.getVerilenDersler().add(dersKod); JsonIslemleri.akademisyenleriKaydet(hocaListesi); JOptionPane.showMessageDialog(frame, "Atama başarılı: " + ak.getAd() + " -> " + dersKod);
        });

        mekanOzBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder(); for (AkademikMekan m : mekanlar) { sb.append(m.getIsim()).append(" (" + m.getClass().getSimpleName() + ")\n"); sb.append("  Konum: ").append(m.getKonum()).append("\n"); sb.append("  Kapasite: ").append(m.getKapasite()).append("\n\n"); } JTextArea ta = new JTextArea(sb.toString()); ta.setEditable(false); JOptionPane.showMessageDialog(frame, new JScrollPane(ta), "Mekan Özellikleri", JOptionPane.INFORMATION_MESSAGE);
        });

        konumBtn.addActionListener(e -> { String isim = JOptionPane.showInputDialog(frame, "Konumunu öğrenmek istediğiniz mekan ismi:"); if (isim == null || isim.trim().isEmpty()) return; AkademikMekan m = findMekanByName(isim.trim()); if (m == null) JOptionPane.showMessageDialog(frame, "Mekan bulunamadı: " + isim); else JOptionPane.showMessageDialog(frame, "Mekan: " + m.getIsim() + "\nKonum: " + (m.getKonum()==null?"(Tanımlı değil)":m.getKonum())); });

        container.add(loginCard, "login"); container.add(mainCard, "main"); ((CardLayout)container.getLayout()).show(container, "login");
        return container;
    }

    // Yardımcı: Ana ekrana dönmeyi merkezileştirme
    private void goHome() {
        if (cardLayout != null && mainPanel != null) {
            cardLayout.show(mainPanel, "home");
        }
    }

    // Basit yardımcı: öğrenci bilgilerini gösteren string
    private String joinStudentInfo(Ogrenci o) {
        if (o == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("Öğrenci: ").append(o.getAd()).append(" ").append(o.getSoyad()).append("\n");
        sb.append("Numara: ").append(o.getOgrenciNo()).append("\n");
        if (o.getDersNotlari() != null && !o.getDersNotlari().isEmpty()) {
            sb.append("Notlar:\n");
            o.getDersNotlari().forEach((k,v) -> sb.append("  ").append(k).append(": ").append(v).append("\n"));
        }
        return sb.toString();
    }

    // Basit yardımcı: akademisyeni string'e çevir
    private String akademisyenToString(Akademisyen a) {
        if (a == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("Akademisyen: ").append(a.getAd()).append(" ").append(a.getSoyad()).append("\n");
        sb.append("ID: ").append(a.getId()).append("\n");
        if (a.getVerilenDersler() != null && !a.getVerilenDersler().isEmpty()) {
            sb.append("Verdiği Dersler: ").append(String.join(", ", a.getVerilenDersler())).append("\n");
        }
        if (a.getYayinlar() != null && !a.getYayinlar().isEmpty()) {
            sb.append("Akademik Yayınlar:\n");
            for (String y : a.getYayinlar()) sb.append("  - ").append(y).append("\n");
        }
        return sb.toString();
    }

    // Görseli yükleyen yardımcı: önce classpath sonra proje dizininden bulmaya çalışır
    private JLabel createGaziLabel(int maxWidth) {
        ImageIcon raw = null;
        URL url = GuiMain.class.getResource("/gazi.png");
        if (url != null) raw = new ImageIcon(url);
        else {
            java.io.File f = new java.io.File("src/gazi.png");
            if (f.exists()) raw = new ImageIcon(f.getAbsolutePath());
        }
        if (raw == null) return new JLabel();
        Image img = raw.getImage();
        int w = raw.getIconWidth() > 0 ? Math.min(maxWidth, raw.getIconWidth()) : maxWidth;
        if (w <= 0) w = 200;
        int h = raw.getIconWidth()>0 ? (int)((double)raw.getIconHeight()*w/raw.getIconWidth()) : raw.getIconHeight();
        Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        JLabel l = new JLabel(new ImageIcon(scaled));
        l.setOpaque(false);
        l.setBorder(new EmptyBorder(4,4,4,4));
        return l;
    }

    // Verilen içerik panelini gazi.png ile sarar. isHome==true ise görsel ortalanıp içeriğin üstüne bindirilir,
    // aksi halde sağ üst köşede gösterilir.
    private JPanel wrapWithGazi(JPanel original, boolean isHome) {
        if (original == null) return null;
        if (isHome) {
            // JLayeredPane ile orijinal paneli alt katmanda, görseli üst katmanda göster
            JLayeredPane lp = new JLayeredPane();
            lp.setLayout(null);
            lp.add(original, JLayeredPane.DEFAULT_LAYER);
            // Görseli oluşturup lp içinde merkezi olarak konumlandırmak için resize listener ekle
            lp.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentResized(java.awt.event.ComponentEvent e) {
                    Dimension s = lp.getSize();
                    original.setBounds(0,0,s.width,s.height);
                    JLabel img = (JLabel) lp.getClientProperty("gaziLabel");
                    if (img == null) {
                        img = createGaziLabel(Math.min(320, s.width/2));
                        lp.putClientProperty("gaziLabel", img);
                        lp.add(img, JLayeredPane.PALETTE_LAYER);
                    }
                    Dimension isz = img.getPreferredSize();
                    int x = (s.width - isz.width) / 2;
                    int y = (s.height - isz.height) / 2;
                    // Üstteki butonların üstüne gelmesi için biraz yukarı kaydır (hafif)
                    y = Math.max(10, y - 30);
                    img.setBounds(x, y, isz.width, isz.height);
                }
            });
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(original.getBackground());
            wrapper.add(lp, BorderLayout.CENTER);
            // Başlangıçta tercih edilen boyutu aktar
            lp.setPreferredSize(original.getPreferredSize());
            return wrapper;
        } else {
            // Orijinal içeriği bozmadan görseli üst katmanda sağ-üst köşeye bindirmek için OverlayLayout kullan
            JPanel overlay = new JPanel();
            overlay.setOpaque(false);
            overlay.setLayout(new OverlayLayout(overlay));

            // Alt katman: orijinal içerik tüm alanı kaplasın
            original.setOpaque(true);
            original.setAlignmentX(0.0f);
            original.setAlignmentY(0.0f);
            original.setMinimumSize(new Dimension(0,0));
            original.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

            // Üst katman: sağ üst köşede gösterilecek şeffaf panel (benzersiz isim)
            JPanel gaziTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
            gaziTopPanel.setOpaque(false);
            JLabel img = createGaziLabel(120);
            gaziTopPanel.add(img);
            // Alignment ayarları: original alt katman, gaziTopPanel üst katman olsun
            gaziTopPanel.setAlignmentX(1.0f);
            gaziTopPanel.setAlignmentY(0.0f);

            // Ekleme: önce original (alt), sonra gaziTopPanel (üst)
            overlay.add(original);
            overlay.add(gaziTopPanel);

            // Sarıcı ile geri döndür (arkaplan rengini koru)
             JPanel wrapper = new JPanel(new BorderLayout());
             wrapper.setBackground(original.getBackground());
             wrapper.add(overlay, BorderLayout.CENTER);
             return wrapper;
          }
      }

    // Yardımcılar
    private AkademikMekan findMekanByName(String isim) {
        for (AkademikMekan m : mekanlar) if (m.getIsim().equalsIgnoreCase(isim)) return m;
        return null;
    }

    private void ensureMekanKonumlariAtandi() {
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

    private void updateStatus() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        if (statusLabel != null) statusLabel.setText("Tarih: " + date + "    Saat: " + time + "    (Veriler JSON dosyalarından yüklenir)");
    }

    // Harf notu hesaplama (JsonIslemleri.notlariRaporla ile uyumlu)
    private String hesaplaHarfNotu(double ort) {
        if (ort >= 85) return "AA";
        if (ort >= 70) return "BA";
        if (ort >= 60) return "BB";
        if (ort >= 50) return "CB";
        if (ort >= 45) return "CC";
        if (ort >= 30) return "DC";
        return "FF";
    }

}
