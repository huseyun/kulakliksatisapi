# API Kontratı — kulakliksatisapi

> Backend (`kulakliksatisapi`) ile frontend (`kulakliksatisfrontend`) arasındaki tek doğruluk kaynağı. Her yeni/değişen endpoint bu dosyaya eklenir. Frontend AI'ı bu dosyayı referans olarak okur.
>
> **Güncelleme kuralı:** Backend'de bir endpoint eklendiğinde/değiştirildiğinde **aynı commit'te** bu dosya da güncellenir. Yoksa frontend yanlış DTO ile çalışmaya başlar, hata izlemek zorlaşır.

---

## ⚠️ Kritik Güvenlik Durumu (geçici not — düzeltildiğinde silinir)

Bu dosyadaki "Auth: ..." satırlarında belirtilen rol kısıtlamaları **şu an fiilen çalışmıyor.** Sebep:

1. `SecurityConfig`'de `@EnableMethodSecurity` annotation **eksik** → method-level `@PreAuthorize` kuralları sessizce ignore ediliyor.
2. SecurityConfig'de `anyRequest().permitAll()` var → URL seviyesinde sadece `/api/admin/**` korumalı (admin kontrolü ile), gerisi açık.
3. Admin controller'larının **hiçbirinde** `@PreAuthorize` yok — yani `@EnableMethodSecurity` eklense bile admin endpoint'leri yine `SecurityConfig`'deki URL filtresine bel bağlamış.

**Pratik sonuç:** Şu an admin endpoint'leri **dahil**, neredeyse tüm endpoint'lere kimliksiz erişilebilir. Frontend bu varsayımla **çalışmamalı** — frontend kendi tarafında yine token gönderdiği gibi yapsın, backend düzeltildikçe gerçek koruma devreye girecek.

Bu durum CLAUDE.md "Bilinen Sorunlar" §10/1'de tekrar belirtildi. Düzeltildiğinde bu uyarı kaldırılacak.

---

## Genel Konvansiyonlar

### URL Prefix
Tüm endpoint'ler `/api/` prefix'i ile başlar. Admin endpoint'leri `/api/admin/` altında.

⚠️ **Bilinen tutarsızlık:** Controller'larda `@RequestMapping` değeri çoğunlukla leading slash olmadan yazılmış (`"api/auth"`). Sadece `SellerController` doğru biçimde `"/api/sellers"` kullanıyor. Spring Boot ikisini de tolere ediyor, runtime'da sorun yok. Frontend yine `/api/...` ile çağırır.

### Authentication
- Public endpoint'ler hariç tüm istekler `Authorization: Bearer <jwt-token>` header'ı bekler.
- Token süresi: 24 saat.
- Token alma: `POST /api/auth/login`.

### Response Status Code'ları (genel kullanım)
- `200 OK` — Başarılı GET / bazı POST (⚠️ bazı POST'lar 201 yerine 200 dönüyor — bilinen tutarsızlık)
- `201 Created` — Başarılı POST (resource oluşturma) — `Location` header ile yeni resource URL'i
- `204 No Content` — Başarılı PUT / DELETE / boş body POST
- `400 Bad Request` — Validation hatası (`VALIDATION_ERROR`)
- `401 Unauthorized` — Token yok / geçersiz / süresi dolmuş (⚠️ özel handler yok, Spring default HTML)
- `403 Forbidden` — Token geçerli ama yetki yok (⚠️ `AccessDeniedException` handler yok, Spring default HTML)
- `404 Not Found` — Resource yok
- `500 Internal Server Error` — Sunucu hatası

### Standart Hata Response Yapısı
Tüm hata cevapları aşağıdaki yapıda döner (`ErrorResponse` record, `@JsonInclude(NON_NULL)`):

```json
{
  "statusCode": 404,
  "errorCode": "ITEM_NOT_FOUND",
  "message": "item, id:5 değeri ile bulunamadı.",
  "timestamp": "2026-05-16T10:30:00",
  "validationErrors": null
}
```

`validationErrors` sadece 400 validation hatalarında dolu olur:

```json
{
  "statusCode": 400,
  "errorCode": "VALIDATION_ERROR",
  "message": "validation hatası",
  "timestamp": "2026-05-16T10:30:00",
  "validationErrors": [
    { "field": "email", "message": "e-posta girilmelidir." }
  ]
}
```

### EErrorCode Enum
Frontend bu enum değerlerini izleyerek Türkçe kullanıcı mesajları üretir. Yeni hata kodu eklendiğinde bu liste güncellenir.

| Kod | HTTP Status | Kullanım yeri |
|---|---|---|
| `VALIDATION_ERROR` | 400 | `GlobalExceptionHandler.handleValidationException()` |
| `USER_NOT_FOUND` | 404 | `UserService` |
| `ADMIN_NOT_FOUND` | 404 | `AdminService` |
| `SELLER_NOT_FOUND` | 404 | `SellerService` (⚠️ ItemService.add() içinde **yanlışlıkla** ITEM_NOT_FOUND yerine bu kullanılıyor) |
| `SHOPPER_NOT_FOUND` | 404 | `ShopperService` |
| `ITEM_NOT_FOUND` | 404 | `ItemService` — get, update, addImages, delete (⚠️ getSellerById() içinde **yanlışlıkla** SELLER_NOT_FOUND yerine bu kullanılıyor) |
| `USERTYPE_NOT_FOUND` | 404 | `UserTypeService` |
| `CATEGORY_NOT_FOUND` | 404 | (henüz controller'ı yok) |
| `INTERNAL_SERVER_ERROR` | 500 | `StorageException`, genel `Exception` handler |
| `CART_NOT_FOUND` | 404 | `CartService` (addToCart, setItemQuantity, incrementCartItem, removeFromCart) — ⚠️ henüz endpoint exposed değil |
| `AUTOEQ_HEADPHONE_NOT_FOUND` | 404 | `AutoEQClientService.equalize` (FastAPI 404) |
| `AUTOEQ_INVALID_ID` | 400 | `AutoEQClientService.equalize` (FastAPI 400) |
| `AUTOEQ_NOT_SUPPORTED_FOR_PRODUCT` | 400 | `AutoEQService.equalize` (Item.autoeqId null/blank) |
| `AUTOEQ_SERVICE_UNAVAILABLE` | 502 | `AutoEQClientService` (FastAPI down / 5xx / network) |

⚠️ Şu an **storage-specific bir error code yok** — storage hataları `INTERNAL_SERVER_ERROR` dönüyor. İleride `STORAGE_ERROR` eklenebilir.

---

## 1. Auth Endpoint'leri

### `POST /api/auth/login` — Giriş
- **Controller:** `AuthController.login()`
- **Auth:** Public
- **Request body:** `AuthRequestDto` — ⚠️ class (record değil), validation **yok**, `@Valid` **yok**
```json
  { "username": "string", "password": "string" }
```
- **Response 200:** Ham JSON string (`ResponseEntity<String>`):
```json
  { "token": "eyJhbGciOi..." }
```
- **Hatalar:** 401 (yanlış username/password — Spring Security default HTML, JSON değil)
- ⚠️ **Bilinen sorun:** Manuel string concat ile JSON üretiliyor. Boş username/password ile istek atılabilir (validation yok).

### `POST /api/auth/register` — Shopper kaydı
- **Controller:** `AuthController.createShopper()`
- **Auth:** Public
- **Request body:** `ShopperCreateRequest`
- **Response 201:** `ShopperResponse` + `Location: /api/shoppers/{id}` header
- **Hatalar:** 400 (`VALIDATION_ERROR`)

---

## 2. User Endpoint'leri (cross-role)

### `PUT /api/users` — Şifre güncelle
- **Controller:** `UserController.updateUserPassword()`
- **Auth:** `@PreAuthorize("isAuthenticated()")` (⚠️ etkisiz — `@EnableMethodSecurity` eksik)
- **Request body:** `UserPasswordUpdateRequest`
```json
  { "password": "string (min 8 chars)" }
```
- **Response 204:** body yok
- **Hatalar:** 404 (`USER_NOT_FOUND`), 400 (`VALIDATION_ERROR`)

---

## 3. Item Endpoint'leri

`ItemController` — class-level `@PreAuthorize` yok, endpoint başına ayrı belirleniyor.

### `GET /api/items/search` — Arama
- **Controller:** `ItemController.searchItems()`
- **Auth:** Public
- **Query params:**
  - `keyword: String` (zorunlu) — Elasticsearch full-text search: title, description, brand
- **Response 200:** `List<ItemSummaryResponse>`
- **Hatalar:** yok (boş liste dönebilir)

### `GET /api/items/recommended` — Önerilen ürünler
- **Controller:** `ItemController.getRecommendedItems()`
- **Auth:** Public
- **Behavior:** `isRecommended=true` olan item'lar
- **Response 200:** `List<ItemSummaryResponse>`

### `GET /api/items/{id}` — Tek item detayı
- **Controller:** `ItemController.getItemById()`
- **Auth:** `hasRole('SELLER')` (⚠️ etkisiz)
- **Path:** `id: Long`
- **Response 200:** `ItemResponse`
- **Hatalar:** 404 (`ITEM_NOT_FOUND`)
- ⚠️ **Tasarım sorusu:** Neden sadece SELLER? SHOPPER bir kulaklığın detayını göremiyor. Frontend "ürün detay sayfası" yapacaksa bu kısıtın kaldırılması gerekecek.
- ⚠️ **Entity leak:** `ItemResponse.images` → `List<Image>` (entity, DTO değil). S3 key'ler frontend'e ham gönderiliyor.

### `POST /api/items` — Yeni item oluştur
- **Controller:** `ItemController.createItem()`
- **Auth:** `hasRole('SELLER')` (⚠️ etkisiz)
- **Request body:** `ItemCreateRequest` (⚠️ `price` alanı yok — bilinen sorun)
- **Response 201:** `ItemResponse` + `Location: /api/items/{id}` header
- **Hatalar:** 404 (⚠️ seller bulunamazsa `ITEM_NOT_FOUND` döner — **yanlış kod**, SELLER_NOT_FOUND olmalı), 400 (`VALIDATION_ERROR`)

### `POST /api/items/{id}/images` — Item'a fotoğraf yükle (seller versiyonu)
- **Controller:** `ItemController.updateItemImages()`
- **Auth:** `hasRole('SELLER')` + ownership check (token'daki username ile item.seller.username karşılaştırılır)
- **Path:** `id: Long`
- **Query params:** `isThumbnail: List<Boolean>` — **opsiyonel** (`required=false`)
- **Request body:** `multipart/form-data`
  - `files: List<MultipartFile>`
- **Response 204:** body yok
- **Hatalar:** 404 (`ITEM_NOT_FOUND`), 403 (başkasının item'ı — ⚠️ handler yok, Spring HTML), 500 (`INTERNAL_SERVER_ERROR` — storage)
- ⚠️ `isThumbnail` null gelirse `NPE` riski (kod `isThumbnail.get(i)` çağırıyor).
- ⚠️ `files.size != isThumbnail.size` ise `IndexOutOfBoundsException` riski.

### `PUT /api/items/{id}` — Item güncelle
- **Controller:** `ItemController.updateItem()`
- **Auth:** `hasRole('SELLER')` + ownership check
- **Path:** `id: Long`
- **Request body:** `ItemUpdateRequest` — ⚠️ **`@Valid` yok**, validation çalışmıyor
- **Response 204:** body yok
- **Hatalar:** 404 (`ITEM_NOT_FOUND`), 403 (ownership)

### `DELETE /api/items/{id}` — Item sil
- **Controller:** `ItemController.deleteItem()`
- **Auth:** `hasRole('SELLER')` (⚠️ etkisiz)
- **Path:** `id: Long`
- **Response 204**
- **Hatalar:** 404 (`ITEM_NOT_FOUND`)
- ⚠️ **GÜVENLİK AÇIĞI:** Ownership check **YOK** — herhangi bir SELLER başka SELLER'ın item'ını silebilir.

---

## 4. Seller Endpoint'leri

`SellerController` — class-level `@PreAuthorize("hasRole('SELLER')")` (⚠️ etkisiz, `@EnableMethodSecurity` eksik).

### `GET /api/sellers/me/items` — Mevcut seller'ın item'ları
- **Controller:** `SellerController.getItems()`
- **Auth:** SELLER (class-level, ⚠️ etkisiz)
- **Response 200:** `Set<ItemSummaryResponse>` (sıralı set, ID'ye göre)
- **Hatalar:** 404 (`SELLER_NOT_FOUND`)

### `PUT /api/sellers` — Seller detay güncelle
- **Controller:** `SellerController.updateSellerDetails()`
- **Auth:** SELLER (class-level, ⚠️ etkisiz)
- **Request body:** `SellerDetailsUpdateRequest`
```json
  { "companyName": "string" }
```
- **Response 204:** body yok
- **Hatalar:** 404 (`SELLER_NOT_FOUND`), 400 (`VALIDATION_ERROR`)

---

## 5. Shopper Endpoint'leri

`ShopperController` — class-level `@PreAuthorize("hasRole('SHOPPER')")` (⚠️ etkisiz).

### `GET /api/shoppers/me` — Mevcut shopper bilgisi
- **Controller:** `ShopperController.getCurrentUser()`
- **Auth:** SHOPPER (class-level, ⚠️ etkisiz)
- **Response 200:** `ShopperResponse`
- **Hatalar:** 404 (`SHOPPER_NOT_FOUND`)

### `PUT /api/shoppers/me` — Shopper detay güncelle
- **Controller:** `ShopperController.updateShopperDetails()`
- **Auth:** SHOPPER (class-level, ⚠️ etkisiz)
- **Request body:** `ShopperDetailsUpdateRequest`
```json
  { "firstName": "string", "lastName": "string" }
```
- **Response 204:** body yok
- **Hatalar:** 404 (`SHOPPER_NOT_FOUND`), 400 (`VALIDATION_ERROR`)
- ⚠️ **Not:** Adres güncelleme bu endpoint'te yok. Sadece firstName/lastName. Shopper'ın adres bilgisi backend'de var (`billingAddress`, `shippingAddress` embedded), ama güncelleme endpoint'i henüz yazılmamış.

---

Mükemmel, devam ediyorum. Bu ikinci bölüm, ilk bölümün bittiği yerden başlıyor. Dosyaya yapıştırırken ⏸ DEVAM EDİYOR satırını sileceksin, iki bölüm sorunsuz birleşecek.

markdown## 6. Admin — Item Endpoint'leri

`AdminItemController` — class-level `@PreAuthorize` **YOK**. URL bazlı koruma da `SecurityConfig`'de `/api/admin/**` üzerinden, ama `@EnableMethodSecurity` eksik olduğu için ⚠️ **şu an tamamen açık** (kimliksiz erişilebilir).

### `GET /api/admin/items` — Tüm item listesi (özet)
- **Controller:** `AdminItemController.getSummaryItemList()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Response 200:** `List<ItemSummaryResponse>`

### `PUT /api/admin/items/{id}` — Item güncelle (admin)
- **Controller:** `AdminItemController.updateItem()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `id: Long`
- **Request body:** `ItemUpdateRequest` — ⚠️ **`@Valid` yok** (aynı sorun seller endpoint'inde de var)
- **Response 204:** body yok
- **Hatalar:** 404 (`ITEM_NOT_FOUND`)

### `DELETE /api/admin/items/{id}` — Item sil (admin)
- **Controller:** `AdminItemController.deleteItem()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `id: Long`
- **Response 204**
- **Hatalar:** 404 (`ITEM_NOT_FOUND`)

### `POST /api/admin/items/{id}/images` — Item'a fotoğraf ekle (admin)
- **Controller:** `AdminItemController.addItemImages()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `id: Long`
- **Query params:** `isThumbnail: List<Boolean>` — **zorunlu** (seller versiyonunda opsiyonel — tutarsızlık)
- **Request body:** `multipart/form-data`
  - `files: List<MultipartFile>`
- **Response 204:** body yok
- **Hatalar:** 404 (`ITEM_NOT_FOUND`), 500 (`INTERNAL_SERVER_ERROR`)

---

## 7. Admin — Seller Endpoint'leri

`AdminSellerController` — class-level `@PreAuthorize` **YOK**. ⚠️ Şu an açık.

### `GET /api/admin/sellers` — Seller listesi/arama
- **Controller:** `AdminSellerController.getSeller()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Query params (tümü opsiyonel):**
  - `username: String`
  - `email: String`
  - `company_name: String`
- **Response 200:** `ResponseEntity<?>` — şekli parametreye göre değişir:
  - Hiçbir param verilmemişse → `List<SellerResponse>`
  - En az bir param verilmişse → `SellerResponse` (tek nesne)
- **Hatalar:** 404 (`SELLER_NOT_FOUND` — filtre eşleşmediğinde)
- ⚠️ **Tasarım sorunu:** `ResponseEntity<?>` wildcard kullanılmış. Frontend JSON'un array mi obje mi olduğunu runtime'da kontrol etmeli. İdeali iki ayrı endpoint olmasıydı.

### `GET /api/admin/sellers/{id}` — Tek seller (detaylı)
- **Controller:** `AdminSellerController.getSellerById()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `id: Long`
- **Response 200:** `SellerDetailedResponse` (items listesi de içerir)
- **Hatalar:** 404 (`SELLER_NOT_FOUND`)

### `GET /api/admin/sellers/{sellerId}/items` — Seller'ın item'ları
- **Controller:** `AdminSellerController.getItemsBySellerId()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `sellerId: Long`
- **Response 200:** `Set<ItemSummaryResponse>` (sıralı set)

### `POST /api/admin/sellers` — Yeni seller oluştur
- **Controller:** `AdminSellerController.addSeller()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Request body:** `SellerCreateRequest`
- **Response 200:** body yok — ⚠️ **201 olmalıydı** (resource creation), bilinen sorun
- **Hatalar:** 400 (`VALIDATION_ERROR`)

### `PUT /api/admin/sellers/{sellerId}` — Seller güncelle
- **Controller:** `AdminSellerController.updateSeller()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `sellerId: Long`
- **Request body:** `SellerUpdateRequest`
- **Response 204:** body yok
- **Hatalar:** 404 (`SELLER_NOT_FOUND`), 400 (`VALIDATION_ERROR`)

---

## 8. Admin — Shopper Endpoint'leri

`AdminShopperController` — class-level `@PreAuthorize` **YOK**. ⚠️ Şu an açık.

### `GET /api/admin/shoppers` — Shopper listesi/arama
- **Controller:** `AdminShopperController.getShopper()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Query params (tümü opsiyonel):**
  - `username: String`
  - `email: String`
- **Response 200:** `ResponseEntity<?>` — parametreye göre:
  - Param yok → `List<ShopperResponse>`
  - Param var → `ShopperResponse`
- **Hatalar:** 404 (`SHOPPER_NOT_FOUND`)

### `GET /api/admin/shoppers/{id}` — Tek shopper
- **Controller:** `AdminShopperController.getShopperById()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `id: Long`
- **Response 200:** `ShopperResponse`
- **Hatalar:** 404 (`SHOPPER_NOT_FOUND`)

### `POST /api/admin/shoppers` — Yeni shopper oluştur
- **Controller:** `AdminShopperController.addShopper()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Request body:** `ShopperCreateRequest`
- **Response 200:** body yok — ⚠️ **201 olmalıydı**
- **Hatalar:** 400 (`VALIDATION_ERROR`)

### `PUT /api/admin/shoppers/{shopperId}` — Shopper güncelle (kimlik bilgileri)
- **Controller:** `AdminShopperController.updateShopper()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `shopperId: Long`
- **Request body:** `ShopperUpdateRequest`
```json
  {
    "username": "string",
    "email": "string",
    "firstName": "string",
    "lastName": "string"
  }
```
- **Response 204:** body yok
- **Hatalar:** 404 (`SHOPPER_NOT_FOUND`), 400 (`VALIDATION_ERROR`)

### `PUT /api/admin/shoppers/{shopperId}/details` — Shopper detay güncelle
- **Controller:** `AdminShopperController.updateShopperDetails()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `shopperId: Long`
- **Request body:** `ShopperDetailsUpdateRequest`
```json
  { "firstName": "string", "lastName": "string" }
```
- **Response 204:** body yok
- **Hatalar:** 404 (`SHOPPER_NOT_FOUND`), 400 (`VALIDATION_ERROR`)

---

## 9. Admin — Admin Endpoint'leri

`AdminAdminController` — class-level `@PreAuthorize` **YOK**. ⚠️ Şu an açık.

### `GET /api/admin/admins` — Admin listesi/arama
- **Controller:** `AdminAdminController.getAdmin()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Query params (tümü opsiyonel):**
  - `username: String`
  - `email: String`
- **Response 200:** `ResponseEntity<?>`
  - Param yok → `List<AdminResponse>`
  - Param var → `AdminResponse`
- **Hatalar:** 404 (`ADMIN_NOT_FOUND`)

### `GET /api/admin/admins/{id}` — Tek admin
- **Controller:** `AdminAdminController.getAdminById()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `id: Long`
- **Response 200:** `AdminResponse`
- **Hatalar:** 404 (`ADMIN_NOT_FOUND`)

### `POST /api/admin/admins` — Yeni admin oluştur
- **Controller:** `AdminAdminController.addAdmin()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Request body:** `UserCreateRequest`
```json
  { "username": "string", "password": "string", "email": "string" }
```
- **Response 200:** body yok — ⚠️ **201 olmalıydı**
- **Hatalar:** 400 (`VALIDATION_ERROR`)

### `PUT /api/admin/admins/{adminId}` — Admin güncelle
- **Controller:** `AdminAdminController.updateAdmin()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `adminId: Long`
- **Request body:** `UserUpdateRequest`
```json
  { "username": "string (4-16 chars)", "email": "string" }
```
- **Response 204:** body yok
- **Hatalar:** 404 (`ADMIN_NOT_FOUND`), 400 (`VALIDATION_ERROR`)

---

## 10. Admin — Cross-User Endpoint'leri

`AdminUserController` — class-level `@PreAuthorize` **YOK**. ⚠️ Şu an açık.

### `GET /api/admin/users` — Tüm kullanıcı listesi (rol bağımsız)
- **Controller:** `AdminUserController.getUser()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Query params (tümü opsiyonel):**
  - `username: String`
  - `email: String`
- **Response 200:** `ResponseEntity<?>`
  - Param yok → `List<UserResponse>`
  - Param var → `UserResponse`
- **Hatalar:** 404 (`USER_NOT_FOUND`)

### `GET /api/admin/users/{id}` — Tek kullanıcı
- **Controller:** `AdminUserController.getUserById()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `id: Long`
- **Response 200:** `UserResponse`
- **Hatalar:** 404 (`USER_NOT_FOUND`)

### `PUT /api/admin/users/{id}` — Kullanıcı güncelle (genel)
- **Controller:** `AdminUserController.updateUser()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `id: Long`
- **Request body:** `UserUpdateRequest`
- **Response 204:** body yok
- **Hatalar:** 404 (`USER_NOT_FOUND`), 400 (`VALIDATION_ERROR`)

### `PUT /api/admin/users/{id}/password` — Kullanıcının şifresini sıfırla
- **Controller:** `AdminUserController.updateUserPassword()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `id: Long`
- **Request body:** `UserPasswordUpdateRequest`
- **Response 204:** body yok
- **Hatalar:** 404 (`USER_NOT_FOUND`), 400 (`VALIDATION_ERROR`)

### `DELETE /api/admin/users/{id}` — Kullanıcı sil
- **Controller:** `AdminUserController.deleteUser()`
- **Auth:** ADMIN (⚠️ şu an açık)
- **Path:** `id: Long`
- **Response 204**
- **Hatalar:** 404 (`USER_NOT_FOUND`)

---

## 11. AutoEQ Endpoint'leri

`AutoEQController` — class-level `@PreAuthorize` **yok**, tüm endpoint'ler **public** (auth gerekmez). FastAPI servisi (`AUTOEQ_URL` env, default `http://localhost:8000`) önünde proxy görevi görüyor.

⚠️ **Tasarım notu:** Şu an public — auth/rate limiting kararı verilmedi. `/equalize` her çağrıda DB read + FastAPI network çağrısı yapıyor, potansiyel DoS vektörü.

### `POST /api/autoeq/equalize` — Kullanıcı kulaklığını ürüne benzetecek EQ profili
- **Controller:** `AutoEQController.equalize()`
- **Auth:** Public
- **Request body:** `EqualizeRequest`
```json
  { "userHeadphoneId": "string", "productId": 5 }
```
- **Response 200:** `EqualizeResponse`
```json
  {
    "fs": 44100,
    "preampDb": -6.5,
    "filters": [
      { "type": "PK", "fc": 105.0, "q": 0.71, "gain": 2.3 }
    ]
  }
```
- **Hatalar:**
  - 400 (`VALIDATION_ERROR`)
  - 404 (`ITEM_NOT_FOUND`) — productId DB'de yok
  - 400 (`AUTOEQ_NOT_SUPPORTED_FOR_PRODUCT`) — `Item.autoeqId` boş/null
  - 404 (`AUTOEQ_HEADPHONE_NOT_FOUND`) — FastAPI 404 (kulaklık ölçümü bulunamadı)
  - 400 (`AUTOEQ_INVALID_ID`) — FastAPI 400 (geçersiz ID formatı)
  - 502 (`AUTOEQ_SERVICE_UNAVAILABLE`) — FastAPI down / 5xx / timeout

### `GET /api/autoeq/headphones` — Kulaklık arama (frontend autocomplete)
- **Controller:** `AutoEQController.searchHeadphones()`
- **Auth:** Public
- **Query params:**
  - `q: String` — opsiyonel (boş veya null ise ilk N alfabetik)
  - `limit: int` — opsiyonel, default `20`
- **Response 200:** `AutoEQSearchResponse`
```json
  {
    "results": [
      { "id": "oratory1990/over-ear/Sennheiser HD 800",
        "label": "Sennheiser HD 800",
        "form": "over-ear",
        "source": "oratory1990" }
    ],
    "total": 1
  }
```
- **Hatalar:** 502 (`AUTOEQ_SERVICE_UNAVAILABLE`)

---

## DTO Referansları

### Request DTO'lar

#### `AuthRequestDto`
⚠️ **class** (record değil), validation **yok**.

| Alan | Tip | Validation | Not |
|---|---|---|---|
| username | String | yok | package-private + getter |
| password | String | yok | package-private + getter |

#### `ShopperCreateRequest` (record)

| Alan | Tip | Validation |
|---|---|---|
| username | String | `@NotBlank` |
| password | String | `@NotBlank` |
| email | String | `@NotBlank`, `@Email` |

#### `ShopperUpdateRequest` (record)

| Alan | Tip | Validation | Not |
|---|---|---|---|
| username | String | `@NotBlank` | — |
| email | String | `@NotBlank` | ⚠️ `@Email` yok — tutarsız |
| firstName | String | `@NotBlank` | — |
| lastName | String | `@NotBlank` | — |

#### `ShopperDetailsUpdateRequest` (record)

| Alan | Tip | Validation |
|---|---|---|
| firstName | String | `@NotBlank` |
| lastName | String | `@NotBlank` |

#### `SellerCreateRequest` (record)

| Alan | Tip | Validation | Not |
|---|---|---|---|
| username | String | `@NotBlank` | — |
| password | String | `@NotBlank` | — |
| email | String | `@NotBlank` | ⚠️ `@Email` yok |
| companyName | String | `@Nullable` | opsiyonel |

#### `SellerUpdateRequest` (record)

| Alan | Tip | Validation | Not |
|---|---|---|---|
| username | String | `@NotBlank` | — |
| email | String | `@NotBlank` | ⚠️ `@Email` yok |
| companyName | String | `@Nullable` | — |

#### `SellerDetailsUpdateRequest` (record)

| Alan | Tip | Validation |
|---|---|---|
| companyName | String | `@NotBlank` |

#### `UserCreateRequest` (record)

| Alan | Tip | Validation |
|---|---|---|
| username | String | `@NotBlank` |
| password | String | `@NotBlank` |
| email | String | `@NotBlank`, `@Email` |

#### `UserUpdateRequest` (record)

| Alan | Tip | Validation | Not |
|---|---|---|---|
| username | String | `@NotBlank`, `@Size(min=4, max=16)` | tek `@Size` validation'lı DTO |
| email | String | `@NotBlank` | ⚠️ `@Email` yok |

#### `UserPasswordUpdateRequest` (record)

| Alan | Tip | Validation |
|---|---|---|
| password | String | `@NotBlank`, `@Size(min=8)` |

#### `ItemCreateRequest` (record)
⚠️ **`price` alanı eksik** — bilinen sorun (CLAUDE.md §10/3).

| Alan | Tip | Validation |
|---|---|---|
| name | String | `@NotBlank` |
| title | String | `@NotBlank` |
| brand | String | `@NotBlank` |
| description | String | `@Nullable` |

#### `ItemUpdateRequest` (record)
⚠️ **`price` alanı eksik**, **`@Valid` kullanılmıyor** endpoint'lerde.

| Alan | Tip | Validation |
|---|---|---|
| name | String | `@NotBlank` |
| title | String | `@NotBlank` |
| brand | String | `@NotBlank` |
| description | String | `@Nullable` |

#### `EqualizeRequest` (record)

| Alan | Tip | Validation |
|---|---|---|
| userHeadphoneId | String | `@NotBlank` |
| productId | Long | `@NotNull`, `@Positive` |

#### ⚠️ Ölü kod: `ItemImageCreateRequest`
Hiçbir endpoint kullanmıyor. Image upload'lar `multipart/form-data` ile `MultipartFile` olarak alınıyor.

---

### Response DTO'lar

#### `ShopperResponse` (record)

| Alan | Tip | Nullable |
|---|---|---|
| id | Long | Hayır |
| username | String | Hayır |
| email | String | Hayır |
| firstName | String | Evet |
| lastName | String | Evet |

#### `SellerResponse` (record)

| Alan | Tip | Nullable |
|---|---|---|
| id | Long | Hayır |
| username | String | Hayır |
| email | String | Hayır |
| companyName | String | Evet |

#### `SellerDetailedResponse` (record)

| Alan | Tip | Nullable |
|---|---|---|
| id | Long | Hayır |
| username | String | Hayır |
| email | String | Hayır |
| companyName | String | Evet |
| items | `Set<ItemSummaryResponse>` | Hayır (boş set olabilir) |

#### `AdminResponse` (record)

| Alan | Tip | Nullable |
|---|---|---|
| id | Long | Hayır |
| username | String | Hayır |
| email | String | Hayır |

#### `UserResponse` (record)

| Alan | Tip | Nullable | Not |
|---|---|---|---|
| id | Long | Hayır | — |
| username | String | Hayır | — |
| email | String | Hayır | — |
| userType | `Set<UserTypeResponse>` | Hayır | boş set olabilir |

#### `UserTypeResponse` (record)
`UserResponse` içinde nested kullanılır, direkt endpoint dönmez.

| Alan | Tip | Not |
|---|---|---|
| userType | `EUserType` | enum: `SHOPPER`, `SELLER`, `ADMIN` |

#### `ItemResponse` (record)

| Alan | Tip | Nullable | Not |
|---|---|---|---|
| id | Long | Hayır | — |
| title | String | Hayır | — |
| price | Double | Evet | — |
| description | String | Evet | — |
| seller | `SellerResponse` | Hayır | nested DTO |
| images | `List<Image>` | Hayır | ⚠️ **entity sızıyor** — DTO değil. Alanları: `originalKey`, `thumbnailKey`, `standardKey`, `isThumbnail`, `displayOrder` (S3 key'leri, tam URL değil) |
| autoeqId | String | Evet | AutoEQ profil ID (örn. `oratory1990/over-ear/Sennheiser HD 800`). null ise frontend "ses simülasyonu mevcut değil" göstermeli. |

#### `ItemSummaryResponse` (record)

| Alan | Tip | Nullable |
|---|---|---|
| id | Long | Hayır |
| title | String | Hayır |
| price | Double | Evet |
| isRecommended | boolean (primitive) | Hayır |
| thumbnailImageUrl | String | Evet |

#### `EqualizeResponse` (record)

| Alan | Tip | Not |
|---|---|---|
| fs | int | sample rate (örn. 44100) |
| preampDb | double | preamp gain (dB) |
| filters | `List<BiquadFilter>` | uygulanacak biquad filtre zinciri |

#### `BiquadFilter` (record)

| Alan | Tip | Not |
|---|---|---|
| type | String | filtre tipi (örn. `PK`, `LSC`, `HSC`) |
| fc | double | merkez frekans, Hz |
| q | double | Q faktörü |
| gain | double | dB |

#### `AutoEQSearchResponse` (record)

| Alan | Tip | Not |
|---|---|---|
| results | `List<AutoEQSearchEntry>` | — |
| total | int | toplam sonuç sayısı |

#### `AutoEQSearchEntry` (record)

| Alan | Tip | Not |
|---|---|---|
| id | String | AutoEQ data path (örn. `oratory1990/over-ear/Sennheiser HD 800`) — `Item.autoeqId` bu formattadır |
| label | String | kullanıcıya gösterilecek kulaklık adı |
| form | String | `in-ear`, `on-ear`, `over-ear` |
| source | String | ölçüm kaynağı (örn. `oratory1990`) |

#### ⚠️ Ölü kod: `ShopperDetailsResponse`
Hiçbir endpoint döndürmüyor. Planlanmış ama eklenmemiş.

---

## Bakım Notu

**Ne zaman güncellenir:**
- Yeni endpoint eklediğinde
- Mevcut endpoint'in request/response DTO'su değiştiğinde
- Yeni hata kodu (`EErrorCode`) eklediğinde
- Bir endpoint kaldırıldığında (silinmesi unutulmasın)

**Aynı commit'te güncellenir.** Yoksa frontend yanlış DTO ile çalışır.

**Nasıl güncellenir:**
- Manuel olarak (Eclipse veya VS Code ile dosyayı aç, ilgili bölümü değiştir)
- Yardım için `prompts/backend/new-endpoint-checklist.md` (Adım 4.7'de yazılacak)
- Yardım için `prompts/backend/review-changes.md` (Adım 4.7'de yazılacak) — son commit'leri tarayıp eksikleri raporlar

**Yakın vadede beklenen güncellemeler:**
- Cart endpoint'leri — Controller stub `/api/carts`'ta var, hiç endpoint exposed değil. Servis hazır (`CartService`).
- `ItemCreateRequest` ve `ItemUpdateRequest`'e `price` alanı eklenmesi
- `ItemController.getItemById` için SHOPPER erişim hakkı (ürün detay sayfası gereği)
- Shopper adres güncelleme endpoint'i
- AutoEQ endpoint'lerine auth/rate limiting eklenip eklenmeyeceği kararı

**Bu dosyadaki ⚠️ uyarıları silme zamanı:**
- `@EnableMethodSecurity` eklendiğinde → "Kritik Güvenlik Durumu" bölümü silinir, tüm "⚠️ şu an açık" notları silinir
- `ItemUpdateRequest` endpoint'lerine `@Valid` eklendiğinde → ilgili uyarılar silinir
- POST endpoint'leri 201'e döndürüldüğünde → ilgili uyarılar silinir
- Ölü DTO'lar (`ItemImageCreateRequest`, `ShopperDetailsResponse`) silindiğinde → "Ölü kod" bölümleri silinir