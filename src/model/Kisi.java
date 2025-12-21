package model;

import exception.HataliVeriException;
import java.time.LocalDate;

//Abstract sınıf
public abstract class Kisi {
    // Gereksinim 3: Private alanlar (Encapsulation)
    private long id;
    private String ad;
    private String soyad;
    private LocalDate dogumTarihi;
    private String sifre;

    private Unvan unvan;

    //Constructor Overloading
    public Kisi() {
        this.id = System.currentTimeMillis();
        this.sifre = "123"; // Varsayılan şifre
    }

    public Kisi(long id, String ad, String soyad, LocalDate dogumTarihi, Unvan unvan,String sifre) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.dogumTarihi = dogumTarihi;
        this.unvan = unvan;
        this.sifre = sifre;
    }

    public String getSifre() { return sifre; }
    public void setSifre(String sifre) { this.sifre = sifre; }

    //Abstract metotlar
    public abstract void bilgileriGoster();
    public abstract String getRolAdi();

    // Getter - Setter Metotları
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getAd() { return ad; }
    public void setAd(String ad) throws HataliVeriException {
        if (ad == null || ad.trim().length() < 2) {
            throw new HataliVeriException("İsim en az 2 karakter olmalıdır.");
        }
        this.ad = ad;
    }
    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }

    public LocalDate getDogumTarihi() { return dogumTarihi; }
    public void setDogumTarihi(LocalDate dogumTarihi) { this.dogumTarihi = dogumTarihi; }

    public Unvan getUnvan() { return unvan; }
    public void setUnvan(Unvan unvan) { this.unvan = unvan; }
}