public class Ders {
    private String kod;
    private String ad;
    private int kredi;

    public Ders(String kod, String ad, int kredi) {
        this.kod = kod;
        this.ad = ad;
        this.kredi = kredi;
    }

    @Override
    public String toString() {
        return kod + " - " + ad + " (" + kredi + " Kredi)";
    }

    public String getAd() { return ad; }
    public String getKod() { return kod; }
    public int getKredi() { return kredi; }
}