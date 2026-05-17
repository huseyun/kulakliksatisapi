# Bilinen Sorunlar Senkronizasyonu

## Ne işime yarar
CLAUDE.md "Bilinen Sorunlar" ve PROJECT-STATUS.md "Teknik Borç Detay Listesi" — bu iki liste gerçekten mevcut kodla tutarlı mı? Çözülmüş bir sorun hâlâ listede mi duruyor? Yeni bir sorun kayda alınmadı mı?

## Ne zaman çalıştırırım
- Haftada bir (pazar gecesi sağlık kontrolü)
- Büyük bir refactor sonrası
- Birçok küçük bug fix biriktikten sonra

## Nasıl çalıştırırım
Backend repo'sunda Claude Code aç, prompt'u yapıştır.

---

## PROMPT

HİÇBİR DOSYA DEĞİŞTİRME. Sadece oku ve raporla.

Görev: CLAUDE.md ve docs/PROJECT-STATUS.md'deki bilinen sorunlar listelerini gerçek kodla karşılaştır.

CLAUDE.md §10 "Bilinen Sorunlar" bölümünü oku.

docs/PROJECT-STATUS.md "Teknik Borç Detay Listesi" bölümünü oku.

Sonra her madde için:

1. Mevcut kodda hâlâ var mı? (ilgili dosyayı incele)
2. Çözülmüş mü?
3. Hâlâ var ama yer/tanım değişmiş mi?

Yeni keşifler:

Listede olmayan ama mevcut kodda gözüne çarpan yeni bir tutarsızlık/sorun var mı?

ÇIKTI:

Çözülmüş gibi görünenler (listeden silinmeli)
[Madde + neden çözüldüğü gerekçesi]

Hâlâ duranlar
[Madde — sadece sayı/onay]

Tanım değişmiş olabilir (sözcükler güncellenmeli)
[Eski tanım → öneri yeni tanım]

Yeni keşifler (listeye eklenmeli)
[Madde + nerede gördüğün + öncelik tahminin: kritik/orta/düşük]

KURALLAR:

- Çözüldü demeden önce gerçekten dosyaya bak, varsayma.
- Yeni keşiflerde abartma — her küçük stil tercihi "sorun" değildir.
- Emin değilsen "⚠️ EMİN DEĞİLİM" yaz.

---

## Çıktıyı nasıl kullanırım
1. "Çözülmüş gibi görünenler" → CLAUDE.md ve PROJECT-STATUS.md'den o maddeleri sil
2. "Yeni keşifler" → ilgili listeye ekle
3. "Tanım değişmiş" → metni güncelle

## Token tüketimi
Tahmini: 5-10K (proje büyüdükçe artar)