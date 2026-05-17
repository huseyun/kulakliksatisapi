# Proje Durumu — kulakliksatisapi

> Bu dosya backend tarafının yaşayan durumunu tutar. AI bu dosyayı **otomatik okumaz**, sadece sen "duruma bak" dediğinde okur.  
> **Son güncelleme:** 16 Mayıs 2026  
> **Güncelleme sahibi:** Hüseyin

---

## Şu Anki Odak

**Aktif feature:** _(henüz başlanmadı — bir sonraki: Cart domain)_  
**Aktif teknik borç:** `@EnableMethodSecurity` eklenmesi (kritik güvenlik)

**Bu hafta yapılması beklenen:**
- Backend ve frontend için CLAUDE.md altyapısının kurulması ✅ (devam ediyor — Faz 0+1)
- `@EnableMethodSecurity` eklenip endpoint'lerin Postman ile doğrulanması
- `ItemController.deleteItem` ownership check eklenmesi
- `ItemCreateRequest` ve `ItemUpdateRequest`'e `price` alanı eklenmesi

---

## Mevcut Olgunluk

### Tamamlanmış olanlar (kullanılabilir durumda)

- ✅ Spring Boot 3.5.5 + Java 21 proje iskelesi (Maven)
- ✅ PostgreSQL bağlantısı + JPA/Hibernate
- ✅ Entity hiyerarşisi: `User` (abstract) → `Admin`, `Seller`, `Shopper` (joined inheritance)
- ✅ `Item`, `Category`, `Image (Embeddable)`, `Address (Embeddable)`, `UserType` modelleri
- ✅ JJWT 0.11.5 ile JWT üretimi/doğrulaması (HS256, 24 saat)
- ✅ `JwtAuthenticationFilter` + `CustomUserDetailsService`
- ✅ `GlobalExceptionHandler` (3 handler: validation, BaseException, generic)
- ✅ `EErrorCode` enum yapısı (9 kod tanımlı)
- ✅ Manuel mapper'lar (MapStruct yok, bilinçli)
- ✅ Hibernate Search + Elasticsearch entegrasyonu (`Item` indeksli)
- ✅ MinIO/S3 storage (S3Config, BucketInitializer, StorageService)
- ✅ Thumbnailator ile fotoğraf işleme
- ✅ DataSeeder (UserType, Category, test kullanıcıları)
- ✅ ~30 endpoint (auth, item, seller, shopper, user, admin × 5)

### Kısmen var ama eksik/buglu

- 🟡 **Güvenlik:** `@EnableMethodSecurity` eksik → `@PreAuthorize` etkisiz, sonuç olarak endpoint'ler fiilen açık
- 🟡 **Item create/update:** `price` alanı eksik
- 🟡 **`deleteItem`:** Ownership check yok
- 🟡 **Validation:** `AuthRequestDto` validation'sız + `@Valid` eksik; `ItemUpdateRequest` endpoint'lerinde `@Valid` yok
- 🟡 **Auth response:** Manuel JSON string (record olmalıydı)
- 🟡 **Email validation:** Create DTO'larda `@Email` var, Update DTO'larda yok
- 🟡 **Admin endpoint'leri:** `@PreAuthorize` yok, sadece URL bazlı koruma var (o da etkisiz şu an)
- 🟡 **Hata yönetimi:** `AccessDeniedException` / `AuthenticationException` handler yok → Spring default HTML
- 🟡 **`GlobalExceptionHandler`:** Catch-all `ex.getMessage()` direkt expose ediyor (güvenlik)

### Henüz başlanmamış

- ❌ Cart / Sepet domain
- ❌ Order / Sipariş domain
- ❌ Payment / Ödeme entegrasyonu
- ❌ Review / Rating
- ❌ Stock / Inventory (Item'da `quantity` bile yok)
- ❌ Notification
- ❌ AutoEQ entegrasyonu (FastAPI servisi)
- ❌ Test altyapısı (sadece boş `contextLoads`)
- ❌ Profile yönetimi (`application-dev.properties`, `application-prod.properties`)
- ❌ Pagination (repository'ler `CrudRepository`, `JpaRepository` değil)
- ❌ Soft delete
- ❌ Logging stratejisi (sadece default Spring logging)

---

## Yakın Vade Roadmap (öncelik sırasıyla)

### 1. Güvenlik temizliği (en yüksek öncelik)
**Hedef:** Mevcut güvenlik açıklarını kapatmak.

- [ ] `@EnableMethodSecurity` ekle
- [ ] Tüm endpoint'lerin auth davranışını Postman ile test et
- [ ] `ItemController.deleteItem` ownership check ekle
- [ ] Admin controller'larına `@PreAuthorize("hasRole('ADMIN')")` ekle (class-level)
- [ ] `AccessDeniedException` ve `AuthenticationException` için handler ekle
- [ ] `GlobalExceptionHandler` catch-all'da `ex.getMessage()` expose etmesin

### 2. Cart / Sepet domain
**Hedef:** Frontend'in sepet UI'sı yapabilmesi için backend hazır olmalı.

- [ ] `CartItem` entity (Shopper ile ManyToOne, Item ile ManyToOne, quantity)
- [ ] `CartItemRepository`
- [ ] `CartService`
- [ ] `CartController` — endpoint'ler:
  - `GET /api/cart/me` — kendi sepetim
  - `POST /api/cart/items` — sepete ekle
  - `PUT /api/cart/items/{id}` — adet güncelle
  - `DELETE /api/cart/items/{id}` — çıkar
- [ ] CartItemResponse DTO
- [ ] api-contract.md güncelle

### 3. AutoEQ entegrasyonu (FastAPI köprüsü)
**Hedef:** Sitenin ana vaadi (kulaklık ses simülasyonu) için altyapı.

**Mimari kararı henüz kesinleşmedi, ama tercih edilen yön:**
- ⭐ Seçenek A: Frontend → Spring Boot → FastAPI (proxy) — _şu an öne çıkan seçenek_
- Seçenek B: Frontend → FastAPI doğrudan (Spring Boot dışında)
- Seçenek C: Frontend AutoEQ verilerini GitHub repository'sinden direkt çeker, FastAPI sadece arama/cache servisi

**Bu kararı verirken konuşulacak:**
- Cache stratejisi (her test öncesi GitHub'a istek atılırsa yavaş olur)
- Auth gereksinimleri (AutoEQ verisi public mi, kullanıcı-bağlı mı?)
- Kulaklık eşleştirme (sitedeki Item ↔ AutoEQ data nasıl bağlanacak)

### 4. Item create/update DTO'larını düzelt
- [ ] `price` alanı ekle (`@NotNull`, `@Positive`)
- [ ] `category` alanı ekle (şu an create'te yok, mecburi olmalı)
- [ ] `ItemUpdateRequest` endpoint'lerinde `@Valid` ekle
- [ ] Email validation'ı Update DTO'larında da zorunlu yap

---

## Uzun Vade Roadmap (öncelik henüz net değil)

- Order / sipariş süreci
- Payment entegrasyonu (Iyzico? Stripe? — karar verilmedi)
- Review / Rating sistemi
- Stock / quantity yönetimi
- Notification sistemi (email + in-app)
- Test altyapısı (en azından kritik flow'lar için integration test)
- Profile yönetimi (dev/prod ayrımı)
- Pagination (CrudRepository → JpaRepository geçişi)
- Production deployment stratejisi (Docker, CI/CD, monitoring)

---

## Teknik Borç Detay Listesi

CLAUDE.md §10'da kısa özetlenmiş halleri var, bu daha detaylı bir takip listesi.

### Kritik (güvenlik / fonksiyonellik)
1. `@EnableMethodSecurity` ekle
2. `ItemController.deleteItem` ownership check
3. `ItemCreateRequest` / `ItemUpdateRequest` → `price` alanı
4. `AuthRequestDto` → validation + `@Valid` kullanımı
5. `ItemUpdateRequest` endpoint'lerinde `@Valid` yok
6. `AccessDeniedException` / `AuthenticationException` handler
7. `GlobalExceptionHandler` catch-all hata mesajı leak

### Orta öncelik (tutarlılık)
8. `Update*Request` DTO'larında `@Email` yok
9. `anyRequest().permitAll()` + method-level karışımı
10. Login response → record kullan
11. Admin POST endpoint'leri 200 yerine 201 dönsün
12. `isThumbnail` query param tutarsızlığı (seller opsiyonel, admin zorunlu)
13. `ItemResponse.images` → `List<Image>` entity sızıyor, DTO'ya çevrilmeli
14. `ItemService` içinde 2 yerde yanlış error code (SELLER_NOT_FOUND ↔ ITEM_NOT_FOUND yer değişmiş)
15. Validation mesajları TR/EN karışık
16. `Seller.items` `FetchType.EAGER` → LAZY veya `@EntityGraph`
17. Repository'ler `CrudRepository` → `JpaRepository` (pagination için)
18. URL prefix leading slash tutarsızlığı (sadece SellerController doğru)

### Düşük öncelik (stil / temizlik)
19. Mapper'larda S3 URL oluşturma copy-paste
20. Admin controller'larda ortak `@RequestMapping("/api/admin")` base eksik
21. Record canonical constructor'lar gereksiz yere yeniden yazılmış
22. `ResponseEntity<?>` wildcard kullanımı admin endpoint'lerde
23. `storageProperties.getAllBuckets()` her çağrıda yeni HashMap
24. `getExtension()` iki yerde (ItemService + StorageService)
25. `User.resetId()` kullanılmıyor
26. `S3Config` region hardcoded `EU_CENTRAL_1`
27. `JwtService` / `JwtAuthenticationFilter` başında `// AI` yorumu
28. `@Autowired` kullanımı tutarsız (Spring 3.x'te tek constructor için gereksiz)
29. Ölü DTO'lar: `ItemImageCreateRequest`, `ShopperDetailsResponse`
30. `ddl-auto=create` + profile yokluğu (prod için tehlikeli)

---

## Geçmiş Kilometre Taşları (kabaca, kronolojik)

> Bu bölüm "ne yapıldı" tarihçesidir, detaylı git history yerine geçer. Yeni milestone'lar buraya en üste eklenir.

- **2026-05-16** — Roo Code'dan Claude Code'a geçiş, CLAUDE.md altyapısı kuruldu (Faz 0+1)
- **(Roo Code dönemi — Mayıs 2026 öncesi)** — Auth, JWT, GlobalExceptionHandler, EErrorCode, Item CRUD, Admin endpoint'leri, S3/MinIO storage, Hibernate Search/Elasticsearch entegrasyonu

---

## Bakım Notu

**Ne zaman güncellenir:**
- Bir milestone tamamlandığında → "Geçmiş Kilometre Taşları"na ekle
- Yeni bir feature başladığında → "Şu Anki Odak" güncelle
- Bir teknik borç çözüldüğünde → "Teknik Borç Detay Listesi"nden sil
- Yakın vade roadmap'te bir madde tamamlandığında → checkbox işaretle, sonra "Tamamlanmış olanlar"a taşı
- Haftada bir kez sağlık kontrolü (`prompts/_shared/health-check.md` ile — Adım 4.7'de yazılacak)

**Bu dosya AI tarafından otomatik okunmaz.** AI'a "şu an nerede kaldık" veya "durumu özetle" gibi soru sorduğunda manuel olarak okutursun: _"`docs/PROJECT-STATUS.md`'yi oku, sonra konuşalım"_ gibi.

**Şişme uyarısı:** Bu dosya 250 satırı geçerse büyük ihtimal **arşivlenecek detay birikmiş** demektir. "Geçmiş Kilometre Taşları" bölümünde son 3 ayı tut, daha eskileri silebilirsin (git history'de zaten var).