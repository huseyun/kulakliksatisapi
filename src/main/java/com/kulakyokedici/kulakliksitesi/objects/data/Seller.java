package com.kulakyokedici.kulakliksitesi.objects.data;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "sellers")
public class Seller extends User
{
	public Seller() {}
	
	public Seller(String username, String password)
	{
		super(username, password);
	}

	@Column(name = "company_name")
	private String company_name;
	
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Item> items;
	
    public List<Item> getItems() {
        return items;
    }
}