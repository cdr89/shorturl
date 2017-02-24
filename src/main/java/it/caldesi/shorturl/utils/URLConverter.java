package it.caldesi.shorturl.utils;

import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLConverter {

	final static Logger logger = LoggerFactory.getLogger(URLConverter.class);

	private static final char[] base62alphabeth = { //
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', //
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', //
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' //
	};

	private HashMap<String, String> keyMap; // (key, originalURL)
	private HashMap<String, String> valueMap; // (originalURL, key)

	private Random rand; // To generate random integers

	private int keyLength; // The key length in URL defaults to 8

	private String domain;

	public URLConverter() {
		keyMap = new HashMap<String, String>();
		valueMap = new HashMap<String, String>();
		rand = new Random();
		keyLength = 8;

		domain = "http://localhost:8080/shorturl-webapp";
	}

	public URLConverter(int keyLength, String domain) {
		this();
		this.keyLength = keyLength;

		if (!domain.trim().isEmpty()) {
			this.domain = domain;
		}
	}

	public String createShortURL(String originalURL) {
		StringBuilder stringBuilder = new StringBuilder();

		if (valueMap.containsKey(originalURL)) {
			stringBuilder.append(domain);
			stringBuilder.append('/');
			stringBuilder.append(valueMap.get(originalURL));

			logger.info("getting EXISTING url: " + originalURL + " -> " + stringBuilder.toString());
		} else {
			stringBuilder.append(domain);
			stringBuilder.append('/');
			stringBuilder.append(getKey(originalURL));

			logger.info("getting NEW url: " + originalURL + " -> " + stringBuilder.toString());
		}

		return stringBuilder.toString();
	}

	private String getKey(String originalURL) {
		String key;
		key = generateRandomKey();
		keyMap.put(key, originalURL);
		valueMap.put(originalURL, key);

		return key;
	}

	private String generateRandomKey() {
		String key = "";
		boolean flag = true;

		while (flag) {
			key = "";
			for (int i = 0; i < keyLength; i++) {
				key += base62alphabeth[rand.nextInt(62)];
			}

			if (!keyMap.containsKey(key)) {
				flag = false;
			} else {
				logger.warn("Key collision! " + key);
			}
		}

		return key;
	}

	public String getOriginalURL(String shortId) {
		return keyMap.get(shortId);
	}

}
