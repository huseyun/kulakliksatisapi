package com.kulakyokedici.kulakliksitesi.repository;

import org.springframework.data.repository.CrudRepository;

import com.kulakyokedici.kulakliksitesi.objects.data.EUserType;
import com.kulakyokedici.kulakliksitesi.objects.data.UserType;

// Bu interface, UserTypes tablosunda findById gibi temel veritabanı işlemlerini yapmamızı sağlar.
public interface UserTypeRepository extends CrudRepository<UserType, Long> {
	public UserType findByName(EUserType roleAdmin);
}