import java.util.*;

class VeriDeposu<T extends Kisi> {
    private List<T> liste;
    private Map<Long, T> idMap; // Hızlı arama için ID haritası

    public VeriDeposu() {
        this.liste = new ArrayList<>();
        this.idMap = new HashMap<>();
    }

    public void ekle(T nesne) {
        liste.add(nesne);
        // JSON'daki 'id' alanını anahtar olarak kullanıyoruz
        idMap.put(nesne.getId(), nesne);
    }

    public void sil(long id) {
        if (idMap.containsKey(id)) {
            T nesne = idMap.get(id);
            liste.remove(nesne);
            idMap.remove(id);
        }
    }

    // VeriDeposu'ndaki ID ile arama metodu
    public T bul(long id) {
        return idMap.get(id); // O(1) hızında bulur
    }

    // Öğrenci numarasına göre arama (Ekstra metot: Map ID tutuyor ama biz No ile arayacağız)
    // Eğer JSON'daki "id" ile "no" farklıysa bu metoda ihtiyacımız var.


    public List<T> getListe() {
        return liste;
    }

    public void ismeGoreSirala() {
        Collections.sort(liste, (k1, k2) -> k1.getAd().compareTo(k2.getAd()));
    }
}