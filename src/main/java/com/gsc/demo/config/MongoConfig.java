package com.gsc.demo.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

	/*
	 * @Bean public MongoClientOptions mongoClientOptions() {
	 * MongoClientOptions.Builder mongoClientOptions =
	 * MongoClientOptions.builder().sslInvalidHostNameAllowed(true).sslEnabled(true)
	 * ; try { InputStream inputStream = new
	 * ByteArrayInputStream(certificateDecoded.getBytes(StandardCharsets.UTF_8));
	 * CertificateFactory certificateFactory =
	 * CertificateFactory.getInstance("X.509"); X509Certificate caCert =
	 * (X509Certificate) certificateFactory.generateCertificate(inputStream);
	 * 
	 * TrustManagerFactory trustManagerFactory = TrustManagerFactory
	 * .getInstance(TrustManagerFactory.getDefaultAlgorithm()); KeyStore keyStore =
	 * KeyStore.getInstance(KeyStore.getDefaultType()); keyStore.load(null); // You
	 * don't need the KeyStore instance to come from a file.
	 * keyStore.setCertificateEntry("caCert", caCert);
	 * 
	 * trustManagerFactory.init(keyStore);
	 * 
	 * SSLContext sslContext = SSLContext.getInstance("TLS"); sslContext.init(null,
	 * trustManagerFactory.getTrustManagers(), null);
	 * mongoClientOptions.sslContext(sslContext);
	 * mongoClientOptions.sslInvalidHostNameAllowed(true); } catch (Exception e) {
	 * throw new IllegalStateException(e); }
	 * 
	 * return mongoClientOptions.build(); }
	 */

	@Bean
	public MongoClient mongo() {

//		Block<SslSettings.Builder> sslBuilder = builder ->{
//			try {
//				builder.enabled(true).context(getsslContext());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		};

		MongoCredential creds = MongoCredential.createCredential("GSC", "admin", "12345678".toCharArray());
		final ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/gscdemo");
		final MongoClientSettings mongoClientSettings = MongoClientSettings.builder().credential(creds)
				.applyConnectionString(connectionString).build();
//        MongoClient client = MongoClients.create("mongodb://GSC:12345678@localhost:27017");
		MongoClient client = MongoClients.create(mongoClientSettings);

		ListDatabasesIterable<Document> databases = client.listDatabases();
		databases.forEach(System.out::println);
		return client;
	}

	@Bean
	public MongoTemplate mongoTemplate(MongoClient mongoClient) {
		return new MongoTemplate(mongoClient, "gscdemo");
	}

	public SSLContext getsslContext() throws Exception {
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		try (InputStream in = new FileInputStream("C:\\Workspace\\Projects\\DOM-1\\MongoClientKeyCert.jks")) {
			keystore.load(in, "p12345".toCharArray());
		}
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keystore, "p12345".toCharArray());

		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		try (InputStream in = new FileInputStream("C:\\Workspace\\Projects\\DOM-1\\MongoRootKeyCert.jks")) {
			trustStore.load(in, "changeit".toCharArray());
		}
		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(trustStore);

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
		return sslContext;
	}

}
