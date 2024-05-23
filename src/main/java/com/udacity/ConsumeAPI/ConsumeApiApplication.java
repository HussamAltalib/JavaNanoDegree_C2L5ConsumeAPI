package com.udacity.ConsumeAPI;

import com.udacity.ConsumeAPI.Entity.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class ConsumeApiApplication {

	private static final Logger log = LoggerFactory.getLogger(ConsumeApiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ConsumeApiApplication.class, args);
	}

	//Adding more code to disabled SSL verification
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		try {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
			SSLContext sslContext = SSLContexts.custom()
					.loadTrustMaterial(null, acceptingTrustStrategy)
					.build();
			CloseableHttpClient httpClient = HttpClients.custom()
					.setSSLContext(sslContext)
					.build();
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
			return new RestTemplate(factory);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			Quote[] quotes = restTemplate.getForObject(
					"https://api.breakingbadquotes.xyz/v1/quotes", Quote[].class);
			for (Quote quote : quotes) {
				log.info(quote.toString());
			}
		};
	}
}
