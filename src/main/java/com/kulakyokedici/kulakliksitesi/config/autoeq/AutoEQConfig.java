package com.kulakyokedici.kulakliksitesi.config.autoeq;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;


@Configuration
public class AutoEQConfig
{
	
	@Value("${autoeq.service.url}")
	private String autoeqServiceUrl;
	
	@Bean
	public RestClient autoeqRestClient()
	{
		ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings
				.defaults()
				.withConnectTimeout(Duration.ofSeconds(3))
				.withReadTimeout(Duration.ofSeconds(15));
		
		ClientHttpRequestFactory factory = ClientHttpRequestFactoryBuilder
				.detect()
				.build(settings);
		
		return RestClient
				.builder()
				.baseUrl(autoeqServiceUrl)
				.requestFactory(factory)
				.build();
	}
}
