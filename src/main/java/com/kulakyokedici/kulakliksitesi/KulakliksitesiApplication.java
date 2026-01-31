/*
 * TODO jwtservice, jwtauthenticationfilter, nasil calisiyor ogren
 * TODO admin sayfası, kullanıcı eklesin, kullanıcı silsin.
 * TODO Itemlere girismeye basla.
 * TODO TAMAM user'i abstract hale getirmeye calis. mantikli mi arastir.
 * TODO TAMAM DTO paternini butun siniflara uygula.
 * TODO TAMAM (bean validation, dto validation) veri objelerini, dogrulamalarini, nasil olacaklarini kurgula.
 * TODO TAMAM (dto'lar) saticiya gore item getirme sonsuz dongusunu duzelt
 * TODO TAMAM  mapper'larin circular reference isine cozum bul.
 * TODO TAMAM exception implementasyonunu düzgün hale getir.
 * TODO TAMAM exception handling mekanizmasini olustur.
 * TODO TAMAM full update ve detailupdate fonksiyonlarini, DTO'larla eszaman implement etmeye basla.
 * TODO TAMAM update controller'lerini, url'den id alacak sekilde duzenle.
 * TODO TAMAM güncelleme işlerinin hatalarını düzelt.
 * TODO TAMAM veri objelerini baştan sona oku, düzenle.
 * TODO TAMAM DTO paternini entegre etmeye çalış.
 * TODO TAMAM postman testlerini olustur.
 */

package com.kulakyokedici.kulakliksitesi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KulakliksitesiApplication 
{

	public static void main(String[] args) 
	{
		SpringApplication.run(KulakliksitesiApplication.class, args);
	}

}
