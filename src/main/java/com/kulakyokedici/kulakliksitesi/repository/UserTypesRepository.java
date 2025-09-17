package com.kulakyokedici.kulakliksitesi.repository;

import org.springframework.data.repository.CrudRepository;
import com.kulakyokedici.kulakliksitesi.objects.data.UserTypes;

// Bu interface, UserTypes tablosunda findById gibi temel veritabanı işlemlerini yapmamızı sağlar.
public interface UserTypesRepository extends CrudRepository<UserTypes, Long> {
}