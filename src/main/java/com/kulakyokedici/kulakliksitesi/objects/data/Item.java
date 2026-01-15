package com.kulakyokedici.kulakliksitesi.objects.data;

import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeOverride;
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
public class Item implements Comparable<Item>
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "item_name")
	@Nullable
	private String itemName;
	
	@Nullable
	@Column(name = "item_price")
	private Double itemPrice;
	
	private transient Double priceAfterTax;
	
	@NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;
    
	@ElementCollection
	@CollectionTable(name = "image")
	@Nullable
	@AttributeOverride(
			name = "url",
			column = @Column(name = "image_url"))
	private Set<Image> images = new HashSet<>();
	
    public Seller getSeller()
	{
		return seller;
	}

	public void setSeller(Seller seller)
	{
		this.seller = seller;
	}

	public Set<Image> getImages()
	{
		return images;
	}

	public void setImages(Set<Image> images)
	{
		this.images = images;
	}

	public void setItemPrice(Double itemPrice)
	{
		this.itemPrice = itemPrice;
	}

	public String getItemName()
	{
		return itemName;
	}
	
	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}
    
    public Double getItemPrice()
    {
    	return itemPrice;
    }

	@Override
	public int compareTo(Item other)
	{
		return this.id.compareTo(other.id);
	}
}
