package com.kulakyokedici.kulakliksitesi.objects.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User
{
	public Admin() {}

	public Admin(String username, String password)
	{
		super(username, password);
	}
	
}
