# Backend Değişiklik İncelemesi — Dosya Güncelleme Önerileri

## Ne işime yarar
Son N commit'ten beri backend'de ne değişti, projeyle ilgili belge dosyalarında (CLAUDE.md, api-contract.md, PROJECT-STATUS.md) ne güncellenmesi gerekiyor — AI sana diff/öneri verir, sen elle uygularsın. Hiçbir dosya AI tarafından yazılmaz.

## Ne zaman çalıştırırım
- Gün sonu (birikmiş değişiklikler varsa)
- Bir feature/domain parçası tamamlandığında
- 3-5 commit biriktiğinde
- Yeni endpoint, yeni entity, yeni kütüphane eklendikten sonra

## Nasıl çalıştırırım
Backend repo'sunda `claude` ile yeni oturum aç, aşağıdaki prompt'u yapıştır. Sen "son 5 commit'i" veya "bugünden beri" gibi belirtebilirsin.

---

## PROMPT

ÖNCE OKU: Bu repository'de HİÇBİR dosyayı değiştirme, oluşturma veya silme. Sadece OKU, ANALİZ ET, ÖNER. CLAUDE.md §MUTLAK KURAL gereği.

Görev: Son [BURAYA YAZ: "5 commit" veya "bugünden beri" veya "şu commit hash'inden beri"] backend'de yapılan değişiklikleri incele ve aşağıdaki dosyalarda hangi güncellemelerin gerektiğini raporla:

1. CLAUDE.md

- "Domain Modeli" bölümü güncellenmeli mi? (yeni entity, ilişki değişikliği)
- "Henüz yazılmamış domain'ler" listesinden çıkarılması gereken var mı?
- "Bilinen Sorunlar" listesinden çıkarılması gereken (yani çözülmüş) madde var mı?
- "Teknik Stack" değişti mi? (yeni kütüphane, versiyon yükseltme)
- "Yakın Vade Roadmap" güncellenmeli mi?


2. docs/api-contract.md

- Yeni endpoint eklendi mi? (bölüm + endpoint formatı)
- Mevcut endpoint'in request/response DTO'su değişti mi?
- Yeni hata kodu (EErrorCode) eklendi mi?
- "Kritik Güvenlik Durumu" bölümündeki uyarılardan kalkması gereken var mı?
- Yeni DTO eklendi mi? "DTO Referansları" bölümüne eklenmeli mi?


3. docs/PROJECT-STATUS.md

- "Şu Anki Odak" güncellenmeli mi?
- "Tamamlanmış olanlar"a eklenecek var mı?
- "Kısmen var ama eksik/buglu" listesinden çıkarılacak var mı?
- "Yakın Vade Roadmap" checkbox'larından işaretlenecek var mı?
- "Teknik Borç Detay Listesi"nden silinecek var mı?
- "Geçmiş Kilometre Taşları"na eklenecek bir şey var mı?



ÇIKTI FORMATI:

Değişikliklerin özeti
[1-2 cümle: ne değişti]

CLAUDE.md güncelleme önerileri
[Her madde için: "şu satır şöyle değişsin" formatında diff benzeri öneri. Hiçbir öneri yoksa "Güncelleme gerekmez" yaz.]

api-contract.md güncelleme önerileri
[Aynı format. Endpoint eklenmesi gerekiyorsa tam endpoint formatında taslak ver.]

PROJECT-STATUS.md güncelleme önerileri
[Aynı format.]

Notum (varsa)
[Dikkat çeken bir şey, kullanıcının fark etmemiş olabileceği bir nokta, vs.]

KURALLAR:

- HİÇBİR dosyaya dokunma. Sadece git log, git diff, git show ile incele.
- Her önerinin gerekçesini kısa söyle ("yeni endpoint eklenmiş için").
- Emin olmadığın yerde "⚠️ EMİN DEĞİLİM" yaz.

---

## Çıktıyı nasıl kullanırım
1. AI'nin önerilerini oku
2. Onayladıklarını Eclipse veya VS Code'da elle uygula
3. Üç dosyayı aynı commit'e dahil et (gerçek kod değişikliğiyle birlikte)

## Token tüketimi
Tahmini: 3-8K (değişiklik miktarına göre). Pro plan için kabul edilebilir.