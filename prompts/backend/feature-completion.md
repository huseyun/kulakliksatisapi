# Feature Tamamlanma Kontrolü

## Ne işime yarar
Bir feature/domain'in tamamen tamamlandığını ilan etmeden önce, dosyaların hepsinin uyumlu güncellendiğini doğrula. Eksik kalan belge varsa AI sana söyler.

## Ne zaman çalıştırırım
- Yeni bir feature/domain tamamen bittiğinde (örn. "Cart domain'i bitti")
- Major refactor bittiğinde
- Sprint kapanışı gibi büyük checkpoint'lerde

## Nasıl çalıştırırım
Backend repo'sunda Claude Code aç, prompt'u yapıştır, feature adını yaz.

---

## PROMPT

HİÇBİR DOSYA DEĞİŞTİRME. Sadece oku ve raporla.
Tamamlanan feature: [BURAYA YAZ: örn. "Cart domain'i"]
Bu feature'la ilgili son N commit'i incele. Aşağıdaki kontrol listesinde her maddeyi doğrula:
1. Kod tarafı

- [ ] Yeni entity'ler var mı? Hepsi @Entity annotation'lı mı?
- [ ] Yeni repository'ler var mı? Naming convention'a uygun mu?
- [ ] Yeni service'ler var mı?
- [ ] Yeni controller endpoint'leri var mı?
- [ ] Yeni DTO'lar var mı? Request DTO'larda @Valid + validation annotation'ları var mı?
- [ ] Yeni hata kodu (EErrorCode) eklendi mi?
- [ ] Yeni exception class'ı eklendi mi?
- [ ] GlobalExceptionHandler güncellendi mi?

2. Belge tarafı

- [ ] api-contract.md'de yeni endpoint'ler var mı?
- [ ] api-contract.md'de yeni DTO'lar var mı?
- [ ] api-contract.md'de yeni EErrorCode'lar var mı?
- [ ] CLAUDE.md "Domain Modeli" güncellenmiş mi?
- [ ] CLAUDE.md "Henüz yazılmamış domain'ler" listesinden çıkarıldı mı?
- [ ] CLAUDE.md "Yakın Vade Roadmap" güncellendi mi?
- [ ] PROJECT-STATUS.md "Tamamlanmış olanlar" listesine eklendi mi?
- [ ] PROJECT-STATUS.md "Yakın Vade Roadmap"te checkbox işaretli mi?
- [ ] PROJECT-STATUS.md "Geçmiş Kilometre Taşları"na eklendi mi?

3. Test/doğrulama

- [ ] Yeni endpoint'ler Postman/curl ile manuel test edildi mi? (sen söyle, ben bilemem)
- [ ] Bilinen sorunlar listesinden bu feature ile çözülen var mı?

ÇIKTI:

Yapılmış olanlar ✅
[Kontrol listesinden tamamlananlar]

Eksik kalanlar ⚠️
[Yapılmamış maddeler, her biri için "şu güncelleme gerekli" şeklinde]

Önerilerim
[Eksiklerin nasıl tamamlanacağı, taslak metinler dahil]

KURALLAR:

- Gerçekten dosyalara bak, varsayma.
- Eksik bir şey yoksa "Her şey tamam ✅" yaz.

---

## Çıktıyı nasıl kullanırım
1. AI'nin "Eksik" dediği maddelere odaklan
2. Önerdiği güncellemeleri elle uygula
3. Hepsini "feature kapanışı" commit'inde topla

## Token tüketimi
Tahmini: 5-12K