package com.kulakyokedici.kulakliksitesi.objects.data;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

//hibernate annotasyonu.
@Entity
//alt siniflarda nasil baglanacak? yeni tablo olusturup foreign key ile.
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "all_users")
public class User
{
	
	public User() {}
	
	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "username", unique = true)
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_typelist",
    	joinColumns = @JoinColumn(name = "user_id"),
    	inverseJoinColumns = @JoinColumn(name = "usertype_id"))
	private Set<UserTypes> userTypes = new HashSet<>();
    
    public String getPassword()
    {
    	return password;
    }
    
    public String getUsername()
    {
    	return username;
    }
    
    public Long getId()
    {
    	return id;
    }
    
    public String getEmail()
    {
    	return email;
    }
    
    public Set<UserTypes> getUserTypes()
    {
    	return userTypes;
    }
    
    public void setPassword(String password)
    {
    	this.password = password;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setUserTypes(Set<UserTypes> userTypes) {
        this.userTypes = userTypes;
    }
    
    public void resetId()
	{
		this.id = null;
	}
}
