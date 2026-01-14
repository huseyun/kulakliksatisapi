package com.kulakyokedici.kulakliksitesi.objects.data;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
	
    @Embedded
    @AttributeOverrides(
    {
    @AttributeOverride(name = "addressLine1",
    column = @Column(name = "BILLING_ADDRESSLINE1")),
    @AttributeOverride(name = "addressLine2",
    column = @Column(name = "BILLING_ADDRESSLINE2")),
    @AttributeOverride(name = "zipCode",
    column = @Column(name = "BILLING_ZIPCODE", length = 5)),
    @AttributeOverride(name = "district",
    column = @Column(name = "BILLING_DISTRICT", length = 5)),
    @AttributeOverride(name = "city",
    column = @Column(name = "BILLING_CITY"))
    })
    protected Address billingAddress;
    
    @Embedded
    @AttributeOverrides(
    {
    @AttributeOverride(name = "addressLine1",
    column = @Column(name = "SHIPPING_ADDRESSLINE1")),
    @AttributeOverride(name = "addressLine2",
    column = @Column(name = "SHIPPING_ADDRESSLINE2")),
    @AttributeOverride(name = "zipCode",
    column = @Column(name = "SHIPPING_ZIPCODE", length = 5)),
    @AttributeOverride(name = "district",
    column = @Column(name = "SHIPPING_DISTRICT", length = 5)),
    @AttributeOverride(name = "city",
    column = @Column(name = "SHIPPING_CITY"))
    })
    protected Address shippingAddress;
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setFirstName(String firstname)
	{
		this.firstName = firstname;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public void fullUpdate(Shopper newShopper)
	{
		super.fullUpdate(newShopper);
		this.firstName = newShopper.getFirstName();
		this.lastName = newShopper.getLastName();
	}
}
