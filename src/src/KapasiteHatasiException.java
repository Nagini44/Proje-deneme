package src;

public class KapasiteHatasiException extends Exception{
    // Hata mesajını üst sınıfa (Exception) iletiyoruz
    public KapasiteHatasiException(String mesaj) {
        super(mesaj);
    }
}
