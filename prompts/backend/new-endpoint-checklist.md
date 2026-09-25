# Yeni Endpoint Belge Şablonu

## Ne işime yarar
Yeni bir endpoint yazdım, `api-contract.md`'ye eklenecek satırları AI hazırlasın. Hızlı, fokuslu.

## Ne zaman çalıştırırım
- Bir endpoint yazdım, commit'lemeden önce
- Toplu commit'leme alışkanlığım yoksa, her endpoint için ayrı

## Nasıl çalıştırırım
Backend repo'sunda Claude Code aç, prompt'u yapıştır, "Yeni endpoint: [METHOD] [path]" bilgisini ver.

---

## PROMPT

HİÇBİR DOSYA DEĞİŞTİRME. Sadece oku ve öner.
Yeni eklediğim endpoint: [BURAYA YAZ: örn. "POST /api/cart/items"]

İlgili controller method'unu, request/response DTO'larını, throw edilen exception'ları oku. Sonra docs/api-contract.md'nin yapısına uygun bir endpoint dokümantasyonu hazırla.

Çıktı şu formatta olsun (api-contract.md'ye doğrudan kopyalanabilir):

[METHOD] /path — Kısa açıklama
- Controller: ClassName.methodName()
- Auth: [Public / hasRole('X') / isAuthenticated() / yok]
- Path: [varsa: isim: tip — açıklama]
- Query params: [varsa: isim: tip — zorunlu/opsiyonel]
- Request body: [DTO ismi VEYA "yok" VEYA "multipart/form-data — alan listesi"]
- Response status: [200/201/204]
- Response body: [DTO ismi VEYA "yok"]
- Hatalar: [olası error code'lar + HTTP status]
- ⚠️ Not: [varsa dikkat çekici bir şey: validation eksik, ownership check yok, tutarsızlık, vs.]

Ek olarak:

- Eğer yeni bir DTO geldiyse, "DTO Referansları" bölümüne eklenecek tabloyu da hazırla.
- Eğer yeni bir EErrorCode geldiyse, "EErrorCode Enum" tablosuna eklenecek satırı söyle.
- Eğer api-contract.md'nin başındaki "⚠️ Kritik Güvenlik Durumu" notu yeni endpoint için de geçerliyse uyar.

KURALLAR:

- Sadece oku, dosyaya dokunma.
- Emin olmadığın yerde "⚠️ EMİN DEĞİLİM" yaz.
- Mevcut endpoint'lerin yazım stiline birebir uy.

---

## Çıktıyı nasıl kullanırım
1. AI'nin hazırladığı bloğu kopyala
2. `docs/api-contract.md`'de uygun bölüme (örn. "Item Endpoint'leri") yapıştır
3. Endpoint'in yazıldığı commit'e dahil et

## Token tüketimi
Tahmini: 1-3K (tek endpoint odaklı)