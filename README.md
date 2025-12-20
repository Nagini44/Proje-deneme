Bu proje, mevcut `Main.java` mantığını bozmadan Swing tabanlı basit ve şık bir kullanıcı arayüzü sunar.

Özet
- Yeni GUI giriş noktası: `GuiMain` (Swing ile yapılmıştır).
- Mevcut servisler (`JsonIslemleri`, `VeriDeposu`, model sınıfları) kullanılarak veriler yüklenir ve kaydedilir.
- Arayüz üç ana rolü destekler: Öğrenci, Akademisyen, İdari Personel.

Hızlı çalışma talimatları (macOS / zsh)
1) Derleme:

```bash
mkdir -p out && javac -d out $(find src -name "*.java")
```

2) GUI çalıştırma:

```bash
java -cp out GuiMain
```

Notlar
- JSON dosyaları (ogrenciler.json, akademisyenler.json, idariPersonel.json) proje kökünde beklenir; yoksa ilgili uyarılar konsola basılır.
- Öğrencilerin transkriptleri çalışma dizinine `{ogrenciNo}_transkript.txt` olarak yazılır (GUI üzerinden "Transkript Oluştur" düğmesi ile tetiklenir).
- Akademisyenlerin not girişi GUI üzerinden yapılır ve değişiklikler `ogrenciler.json` dosyasına kaydedilir.

Kısıtlar ve geliştirme önerileri
- Şu anda GUI, Main.java'deki tüm komut satırı mantığını birebir taşımaz; kullanıcı akışını daha görsel şekilde sunar fakat aynı iş mantığını kullanır.
- Daha gelişmiş özellikler (oturum açma/parola, yetkilendirme, detaylı hata dialogları, tema) sonraki adımlar olabilir.

Dosyalar
- `src/GuiMain.java` : Yeni Swing arayüzü.

İletişim
- Arayüzde bir hata veya eksik fonksiyon görürseniz paylaşın, gereksinime göre genişleteyim.

