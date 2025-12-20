package util;

public class IslemSonucu<T> {
    private boolean basariliMi;
    private String mesaj;
    private T veri;

    // Constructor 1: Sadece durum ve mesaj
    public IslemSonucu(boolean basariliMi, String mesaj) {
        this(basariliMi, mesaj, null);
    }

    // Constructor 2: Durum, mesaj ve veri (Constructor Overloading)
    public IslemSonucu(boolean basariliMi, String mesaj, T veri) {
        this.basariliMi = basariliMi;
        this.mesaj = mesaj;
        this.veri = veri;
    }

    // Generic Metot
    public void sonucuYazdir() {
        if (basariliMi) {
            System.out.println("[BAŞARILI] " + mesaj);
            if (veri != null) {
                System.out.println("Veri: " + veri.toString());
            }
        } else {
            System.out.println("[HATA] " + mesaj);
        }
    }

    // Getter Metotları
    public boolean isBasariliMi() { return basariliMi; }
    public String getMesaj() { return mesaj; }
    public T getVeri() { return veri; }
}