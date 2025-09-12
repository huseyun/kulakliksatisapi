package com.kulakyokedici.kulakliksitesi.config.database;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProductionDatabaseConfig
{
	@Bean
	public DataSource dataSource()
	{
		return DataSourceBuilder.create()
				.driverClassName("org.postgresql.Driver")
				.url("jdbc:postgresql://localhost:5432/proddb")
				.username("famanas")
				.password("qwe")
				.build();
	}
}
