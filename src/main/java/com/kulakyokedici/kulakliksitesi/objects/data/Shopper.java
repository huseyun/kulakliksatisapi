package com.kulakyokedici.kulakliksitesi.objects.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "shoppers")
public class Shopper extends User
{
	public Shopper() {}
	
	public Shopper(String username, String password)
	{
		super(username, password);
	}

	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
}
