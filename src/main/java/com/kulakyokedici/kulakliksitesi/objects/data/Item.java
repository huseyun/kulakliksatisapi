package com.kulakyokedici.kulakliksitesi.objects.data;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import jakarta.annotation.Nullable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "items")
@Indexed
public class Item implements Comparable<Item>
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Bu değer dışarıdan değiştirilemez (updatable=false), boş olamaz ve benzersizdir.
	@Column(name = "item_uuid", updatable = false, nullable = false, unique = true)
	private String itemUuid = java.util.UUID.randomUUID().toString();
	
	@Column(name = "name")
	@Nullable
	private String name;
	
	@Column(name = "title")
	@FullTextField
	@Nullable
	private String title;
	
	@Column(name = "brand")
	@KeywordField
	private String brand;
	
	@Nullable
	@Column(name = "item_price")
	private Double price;
	
	// uygulama içi fiyat hesaplama yapılacak.
	private transient Double priceAfterTax;
	
	@NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;
    
	@ElementCollection
	@CollectionTable(name = "images")
	@Nullable
	private List<Image> images;
	
	@Nullable
	@Column(name = "description")
	@FullTextField
	private String description;
	
	@Override
	public int compareTo(Item other)
	{
		return this.id.compareTo(other.id);
	}
	
	public Long getId()
	{
		return id;
	}
	
	public Seller getSeller()
	{
		return seller;
	}

	public void setSeller(Seller seller)
	{
		this.seller = seller;
	}

	public List<Image> getImages()
	{
		return images;
	}

	public void setImages(List<Image> images)
	{
		this.images = images;
	}

	public void setPrice(Double itemPrice)
	{
		this.price = itemPrice;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(String itemName)
	{
		this.name = itemName;
	}
    
    public Double getPrice()
    {
    	return price;
    }
	
    public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getBrand()
	{
		return brand;
	}

	public void setBrand(String brand)
	{
		this.brand = brand;
	}
	
	public String getItemUuid() {
		return itemUuid;
	}

	public void setItemUuid(String itemUuid) {
		this.itemUuid = itemUuid;
	}

}
