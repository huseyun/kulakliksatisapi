package com.kulakyokedici.kulakliksitesi.repository;

import org.springframework.data.repository.CrudRepository;
import com.kulakyokedici.kulakliksitesi.objects.data.UserType;

// Bu interface, UserTypes tablosunda findById gibi temel veritabanı işlemlerini yapmamızı sağlar.
public interface UserTypesRepository extends CrudRepository<UserType, Long> {
	public UserType findByName(String name);
}