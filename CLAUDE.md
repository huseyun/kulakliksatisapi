# kulakliksatisapi — Backend Proje Bağlamı (Claude için)

> Bu dosya, Claude Code'un bu repository'de çalışırken uyacağı kuralları, projenin mevcut durumunu ve geliştirme felsefesini tanımlar. Her oturumun başında otomatik okunur.

---

## 🔴 MUTLAK KURAL — TARTIŞMA YOK

**Bu repository'deki HİÇBİR dosya, kullanıcının açık ve özel onayı olmadan değiştirilmez, oluşturulmaz veya silinmez.**

- AI'ın bu repo'daki rolü: **okuma, analiz, açıklama, danışmanlık, çözüm önerisi sunma.**
- Backend implementasyonunu **her zaman kullanıcı kendisi yapar.**
- "Bunu düzelteyim mi?" gibi sorulardan önce çözümü açıkla, kullanıcı uygulasın.
- Kullanıcı "değiştir/uygula" demediği sürece dosyaya dokunma.
- Bu kural koşulsuzdur. Bağlam ne olursa olsun geçerlidir. Hata olduğunu düşündüğün durumlarda bile.

**Tek istisna:** Kullanıcı **bu oturumda ve bu görev için** açıkça "şu dosyayı şu şekilde değiştir" gibi spesifik bir onay verirse, sadece o spesifik değişiklik yapılır. Genelleştirilmiş "düzelt" tarzı izinler kabul edilmez.

---

## 1. Proje Özeti

**Adı:** kulakliksatisapi  
**Türü:** Kulaklık satış platformu backend'i  
**Asıl vaadi (sitenin):** Kullanıcı satın almadan önce, ürün sayfasındayken kendi kulaklığını listeden seçerek, AutoEQ verisi + Web Audio API ile o kulaklığın "kendi kulaklığına göre nasıl ses vereceğini" preset müzikle test edebilsin.

Bu vaadin backend tarafındaki yansıması: gelecekte FastAPI (AutoEQ servisi) ile entegrasyon, ürün bilgilerine AutoEQ profil referansı eklenmesi.

**Geliştirme aşaması:** Erken/öğrenme aşaması. Production'a hazır değil.

---

## 2. Teknik Stack

- **Dil:** Java 21
- **Framework:** Spring Boot 3.5.5
- **Build:** Maven
- **Database:** PostgreSQL
- **ORM:** JPA / Hibernate
- **Security:** Spring Security 6.x + JJWT 0.11.5 (HS256)
- **Search:** Hibernate Search 7.1.0 + Elasticsearch
- **Storage:** MinIO (dev) / AWS S3 (prod) — `software.amazon.awssdk:s3` 2.25.60
- **Image processing:** Thumbnailator 0.4.20
- **Utility:** Lombok
- **Test:** Spring Boot Test (yapılandırılmış, fiilen kullanılmıyor)

**Modern Java syntax serbest:** record, pattern matching, sealed class, text blocks, switch expression kullanılabilir.

---

## 3. Klasör Yapısı

src/main/java/com/kulakyokedici/kulakliksitesi/
├── KulakliksitesiApplication.java
├── config/              ← Spring config, security, S3, web (CORS), exception handler
├── controller/          ← REST endpoints (admin/ alt klasörü ayrı)
├── mapper/              ← Manuel DTO mapping (MapStruct YOK)
├── objects/             ← ⚠️ "entity/model" yerine bu isim kullanılmış
│   ├── data/            ← JPA entity'leri
│   │   └── dto/         ← Request/Response record'ları
│   ├── exception/       ← Custom exception'lar + ErrorResponse DTO
│   └── security/        ← SecurityUser, AuthRequestDto
├── repository/          ← Spring Data (CrudRepository — JpaRepository DEĞİL)
└── service/             ← İş mantığı (security/ alt klasörü ayrı)

**Katmanlaştırma:** Klasik Controller → Service → Repository + ayrı Mapper. Özel pattern (CQRS, hexagonal) yok.

---

## 4. Naming Convention'lar

- **Entity:** `PascalCase`, suffix yok (`User`, `Item`, `Seller`)
- **Enum:** `E` prefix (`ECategory`, `EUserType`, `EErrorCode`)
- **DTO:** Java record, `*Request` / `*Response` suffix
- **Repository:** `*Repository`
- **Service:** `*Service`
- **Controller:** `*Controller`
- **Admin controller:** `Admin*Controller` (`admin/` alt klasöründe)

---

## 5. Domain Modeli (mevcut entity'ler)

User (abstract, @Inheritance JOINED, tablo: all_users)
├── Admin
├── Seller (companyName, OneToMany→Item EAGER)
└── Shopper (firstName, lastName, @Embedded billingAddress, shippingAddress)
User ↔ UserType (ManyToMany, join: user_typelist)
UserType: EUserType enum (SHOPPER, SELLER, ADMIN)
Item (id, itemUuid UUID, name, title, brand, price, priceAfterTax transient,
isRecommended, description,
ManyToOne→Seller, ManyToOne→Category,
@ElementCollection→Image)
Image (@Embeddable): originalKey, thumbnailKey, standardKey, isThumbnail, displayOrder
Address (@Embeddable): addressLine1/2, district, city, zipCode
Category: ECategory enum (IN_EAR, ON_EAR, OVER_EAR)

**Henüz yazılmamış domain'ler (frontend'in farkında olması gereken):**
- Cart / Basket (sepet) — **yakın vadede yazılacak**
- Order / Purchase (sipariş)
- Payment (ödeme)
- Review / Rating (yorum/puan)
- Stock / Inventory (stok — şu an Item'da `quantity` bile yok)
- Notification (bildirim)
- AutoEQ entegrasyonu (FastAPI servisi ile köprü) — **yakın vadede yazılacak**

---

## 6. API Yaklaşımı

- **Stil:** REST, JSON, `/api` prefix
- **DTO kullanımı:** Entity asla doğrudan dönmez, daima Mapper üzerinden DTO/record dönülür.
- **Validation:** Controller'larda `@Valid @RequestBody`, DTO record'larında `@NotBlank`, `@Email`, `@Size`.
- **Hata response yapısı:**
```json
  {
    "statusCode": 404,
    "errorCode": "ITEM_NOT_FOUND",
    "message": "...",
    "timestamp": "ISO-8601",
    "validationErrors": null
  }
```
- **Endpoint listesi detayları:** `docs/api-contract.md` dosyasında. Bu kontrat frontend'in tek doğruluk kaynağıdır.

---

## 7. Hata Yönetimi

**`@RestControllerAdvice` (`GlobalExceptionHandler`)** 3 handler içerir:
1. `MethodArgumentNotValidException` → 400 + field-level validation errors
2. `BaseException` ve alt sınıflar (`ResourceNotFoundException`, `StorageException`) → exception'ın taşıdığı `HttpStatus` + `EErrorCode`
3. Genel `Exception` → 500 (⚠️ şu an `ex.getMessage()` direkt expose ediliyor — bilinen sorun, aşağıda)

**EErrorCode enum** yeni hata türleri için kullanılır. Frontend bu enum'a göre Türkçe mesaj üretir.

---

## 8. Auth / Security

- **Stateless** session, JWT tabanlı (HS256, 24 saat geçerlilik).
- `JwtAuthenticationFilter` → `UsernamePasswordAuthenticationFilter` öncesi.
- `DaoAuthenticationProvider` + `BCryptPasswordEncoder`.
- `SecurityUser` = `UserDetails` implementasyonu.
- Roller: `ROLE_SHOPPER`, `ROLE_SELLER`, `ROLE_ADMIN`.
- `@PreAuthorize` method seviyesinde kullanılıyor (⚠️ aşağıya bak — kritik sorun).

---

## 9. Configuration

- `application.properties` tek dosya, **profile yönetimi yok** (dev/prod ayrımı bekliyor).
- Hassas bilgiler env variable ile (`${JWT_SECRET_KEY}`, `${DB_PASSWORD}`).
- `spring.jpa.hibernate.ddl-auto=create` → ⚠️ her restart'ta DB sıfırlanır (dev için uygun, prod için tehlikeli).
- `DataSeeder` her ayağa kalkışta UserType'ları, Category'leri ve test kullanıcılarını oluşturur (idempotent).
- Test kullanıcılar: `admin/adminpass`, `seller/sellerpass`, `shopper/shopperpass`.

---

## 10. 🟠 Bilinen Sorunlar / Tedavi Bekleyen Tutarsızlıklar

Bu liste **canlı bir liste**. Sorun çözüldükçe maddeyi listeden çıkar.

### Kritik (yakında düzeltilecek)

1. **`@EnableMethodSecurity` annotation eksik** → `SecurityConfig`'de yok. Sonuç: tüm `@PreAuthorize` kuralları sessizce **ignore ediliyor**. `anyRequest().permitAll()` ile birleşince admin dışı endpoint'ler fiilen tamamen açık. Düzeltme: annotation eklenip her endpoint'in auth durumu Postman ile teyit edilmeli.
2. **`ItemController.deleteItem`'da ownership check yok** → herhangi bir SELLER başka bir SELLER'ın item'ını silebilir. `updateItem`'da kontrol var, `delete`'te yok.
3. **`ItemCreateRequest` ve `ItemUpdateRequest`'te `price` alanı yok** → muhtemelen unutulmuş. Fiyat sadece `DataSeeder`'da `setPrice()` ile set ediliyor.

### Orta öncelikli

4. **`anyRequest().permitAll()` + method-level security karışımı** → tutarsız yaklaşım. Ya URL seviyesinde `authenticated()` ya da method security'e tam güven.
5. **Login response manuel JSON string** → `"{\"token\":\"" + token + "\"}"` yerine record kullanılmalı.
6. **`GlobalExceptionHandler` catch-all → `ex.getMessage()`'i direkt expose ediyor** → güvenlik açığı (internal mesaj kullanıcıya gidiyor).
7. **`AccessDeniedException` / `AuthenticationException` handler yok** → Spring Security default HTML 401/403 dönüyor, JSON değil. Frontend bu durumda hatalı parse eder.
8. **Validation mesajları TR/EN karışık** (`"username is too short or long"` vs `"kullanici adi bos olamaz."`) → bir dil seçilmeli.
9. **`Seller.items` `FetchType.EAGER`** → bir seller yüklendiğinde tüm item'ları gelir. Performans sorunu adayı.
10. **Repository'ler `CrudRepository` extends ediyor** → pagination yok. `JpaRepository`'ye geçilmeli.

### Düşük öncelikli

11. Mapper'larda S3 URL oluşturma kodu copy-paste (ItemMapper + SellerMapper).
12. Admin controller'larında ortak `@RequestMapping("/api/admin")` base eksik.
13. Record canonical constructor'ları gereksiz yere yeniden yazılmış (ItemResponse, SellerResponse vs.).
14. `ResponseEntity<?>` wildcard kullanımı admin controller'larda — ayrı endpoint'ler daha temiz olur.
15. `@RequestMapping` leading slash tutarsız (bazı yerlerde `/api/...`, bazı yerlerde `api/...`).
16. `storageProperties.getAllBuckets()` her çağrıda yeni HashMap.
17. `getExtension()` metodu iki yerde (ItemService + StorageService).
18. `User.resetId()` kullanılmıyor.
19. `S3Config` region hardcoded `EU_CENTRAL_1`.
20. `JwtService` / `JwtAuthenticationFilter` dosyalarının başında `// AI` yorumu — AI tarafından yazılmış olduğunu işaretliyor.
21. `@Autowired` kullanımı tutarsız (Spring Boot 3.x'te tek constructor için gereksiz).

---

## 11. Yakın Vade Roadmap

**Sırasıyla:**
1. **Cart / Sepet domain'i** — CartItem entity, CartService, sepete ekle/çıkar endpoint'leri.
2. **AutoEQ entegrasyonu (FastAPI ↔ Spring Boot köprüsü)** — Ayrı bir FastAPI servisi AutoEQ verilerini servis edecek, Spring Boot bunu kullanıcıya proxy'leyecek (veya frontend doğrudan FastAPI'yi konuşacak — karar verilmedi).
3. Yukarıdaki "kritik" bilinen sorunlar (en azından `@EnableMethodSecurity` + ownership check) bu domain'lerden önce düzeltilmeli.

**Sonra (öncelik sırası belirsiz):** Order, Payment, Stock, Review, Notification.

---

## 12. Geliştirme Yardımcısı Olarak AI'ın Rolü

Kullanıcı backend kodunu kendi yazıyor. AI'ın bu repo'da yaptığı işler:

- ✅ Kullanıcının takıldığı bir hata için stack trace analiz etmek, neden meydana geldiğini açıklamak
- ✅ Bir özellik için "şöyle yapsam mı böyle yapsam mı" tartışmalarında alternatif sunmak
- ✅ Kod review etmek, "şu satırda şu sorun olabilir" tarzı geri bildirim vermek
- ✅ Tutarsızlıkları işaret etmek
- ✅ Kütüphane / API kullanımı hakkında danışmanlık
- ✅ Kullanıcı talep ettiğinde kod taslağı **göstermek** (uygulamak değil)
- ✅ Bu dosyayı veya `docs/api-contract.md`'yi güncellemek için **öneri vermek** (uygulamayı kullanıcı yapar)
- ❌ Yazma, değiştirme, silme yapmak (kesinlikle yasak)

---

## 13. Önemli Notlar (AI'ın hatırlaması gereken)

- **MinIO production'da AWS S3 ile değiştirilebilir** — `S3Config` `forcePathStyle=true` ile MinIO uyumlu.
- **Sadece `Item` entity `@Indexed`** — Hibernate Search şu an sadece title/description/brand'i indeksliyor.
- **`priceAfterTax` transient alan boş** — hesaplama mantığı henüz yazılmamış.
- **`ImageRepository` yok** — Image'lar Item üzerinden yönetilir.
- **`Item implements Comparable<Item>`** (id'ye göre) ve `Seller.items` bir `TreeSet`. Sıralama zinciri tutarlı.
- **Frontend ile entegrasyon:** Frontend Angular (ayrı repo: `kulakliksatisfrontend`). Backend'i tüketir, asla backend'e dokunmaz.
<!-- test -->
