package com.kulakyokedici.kulakliksitesi.objects.data.dto.autoeq;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AutoEQEqualizeRequest(
		@JsonProperty("source_id") String sourceId,
		@JsonProperty("target_id") String targetId)
{
	
}
