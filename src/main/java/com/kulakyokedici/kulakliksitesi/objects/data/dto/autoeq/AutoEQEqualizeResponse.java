package com.kulakyokedici.kulakliksitesi.objects.data.dto.autoeq;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AutoEQEqualizeResponse(
		@JsonProperty("source_id") String sourceId,
		@JsonProperty("target_id") String targetId,
		int fs,
		@JsonProperty("preamp_db") double preampDb,
		List<AutoEQBiquadFilter> filters)
{

}
