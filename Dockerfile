# 1. AŞAMA: Build Aşaması (Maven ile JAR üretme)
# Neden: Bilgisayarında Maven kurulu olmak zorunda kalmasın, Docker kendi içinde derlesin.
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

# Çalışma klasörünü ayarla
WORKDIR /app

# Sadece pom.xml'i kopyala ve bağımlılıkları indir.
# Neden: Kod değişse bile kütüphaneler değişmediyse bu adımı önbellekten (cache) hızlıca geçsin diye.
COPY pom.xml .
RUN mvn dependency:go-offline

# Şimdi kaynak kodları kopyala
COPY src ./src

# Projeyi paketle (Testleri atlıyoruz çünkü veritabanı bağlantısı yoksa testler patlayabilir)
RUN mvn package -DskipTests

# -----------------------------------------------------------------------------

# 2. AŞAMA: Run Aşaması (Uygulamayı Çalıştırma)
# Neden: İlk aşamadaki Maven dosyaları çok yer kaplar. Sadece çalışacak Java (JRE) bize yeter.
# java.version 21 olduğu için 21-jre seçiyoruz.
FROM eclipse-temurin:21-jre-alpine

# Çalışma klasörünü ayarla
WORKDIR /app

# İlk aşamada (builder) üretilen JAR dosyasını buraya kopyala.
# target klasöründeki dosya ismini pom.xml'den biliyoruz.
COPY --from=builder /app/target/*.jar app.jar

# Uygulama hangi porttan konuşuyor? (Bilgi amaçlı)
EXPOSE 8080

# Konteyner ayağa kalktığında çalışacak komut:
ENTRYPOINT ["java", "-jar", "app.jar"]