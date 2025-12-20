package interfaces;

import exception.KapasiteHatasiException;

public interface IRezervasyon {
    //metod imzaları oluşturalım (abstract olacak)
    void rezervasyonYap(String zaman) throws KapasiteHatasiException;

    // Interface'den gelen metodu burada genel bir şekilde ezebiliriz
    // veya alt sınıflara bırakabiliriz. Burada örnek gövde yazalım:
    void rezervasyonYap(String zaman, int kisiSayisi) throws KapasiteHatasiException;

    void rezervasyonIptal(String zaman);

    default void musaitlikKontrolEt() {
        System.out.println("Müsaitlik durumu kontrol ediliyor...");
    }
}
