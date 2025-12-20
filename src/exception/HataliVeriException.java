package exception;

/**
 * Proje genelinde veri doğrulama hatalarında kullanılacak özel hata sınıfı.
 * GEREKSİNİM 7: Özel (Custom) Exception sınıfı.
 */
public class HataliVeriException extends Exception {
    public HataliVeriException(String mesaj) {
        super(mesaj);
    }
}