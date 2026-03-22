package com.kulakyokedici.kulakliksitesi.objects.data.dto.response;

import java.util.List;

public record ItemSummaryResponse(
		String title,
		Double price,
		List<String> images)
{}
