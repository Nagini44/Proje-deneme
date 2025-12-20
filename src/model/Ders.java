package model;

import exception.HataliVeriException;

public class Ders {
    private String kod;
    private String ad;
    private int kredi;

    public Ders(String kod, String ad, int kredi) {
        this.kod = kod;
        this.ad = ad;
        this.kredi = kredi;
    }
    public Ders() {
        // Varsayılan değerler
        this.kod = "Tanımsız";
        this.ad = "İsimsiz Ders";
        this.kredi = 0;
    }

    @Override
    public String toString() {
        return kod + " - " + ad + " (" + kredi + " Kredi)";
    }

    public String getAd() { return ad; }
    public String getKod() { return kod; }
    public int getKredi() { return kredi; }
    public void setKredi(int kredi) throws HataliVeriException {
        if (kredi < 1 || kredi > 10) {
            throw new HataliVeriException("Ders kredisi 1-10 arasında olmalıdır.");
        }
        this.kredi = kredi;
    }
}