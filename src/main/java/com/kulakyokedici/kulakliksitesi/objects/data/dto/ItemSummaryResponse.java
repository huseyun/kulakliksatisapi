package com.kulakyokedici.kulakliksitesi.objects.data.dto;

import java.util.Set;

import com.kulakyokedici.kulakliksitesi.objects.data.Image;

public record ItemSummaryResponse(
		String itemName,
		Double itemPrice,
		Set<Image> images)
{

}
