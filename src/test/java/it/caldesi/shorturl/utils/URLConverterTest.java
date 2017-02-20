package it.caldesi.shorturl.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import it.caldesi.shorturl.utils.URLConverter;

public class URLConverterTest {

	private static URLConverter urlConverter = null;

	@BeforeClass
	public static void setUp() {
		urlConverter = new URLConverter();
	}

	@Test
	public void testCleanURL() {
		String url = "http://www.google.com/";
		String cleanedURL = URLConverter.cleanURL(url);

		assertEquals("http://www.google.com", cleanedURL);
	}

	@Test
	public void testConversion() {
		String url1 = "http://www.google.com";
		String cleanedURL1 = URLConverter.cleanURL(url1);
		String shortURL1 = urlConverter.createShortURL(url1);
		System.out.println("shortURL1: " + shortURL1);
		String originalURL1 = urlConverter.getOriginalURL(shortURL1);
		System.out.println("originalURL1: " + originalURL1);

		assertEquals(cleanedURL1, originalURL1);
		assertEquals(shortURL1, urlConverter.createShortURL(url1));

		String url2 = "http://www.caldesi.it";
		String cleanedURL2 = URLConverter.cleanURL(url2);
		String shortURL2 = urlConverter.createShortURL(url2);
		System.out.println("shortURL2: " + shortURL2);
		String originalURL2 = urlConverter.getOriginalURL(shortURL2);
		System.out.println("originalURL2: " + originalURL2);

		assertEquals(cleanedURL2, originalURL2);
		assertEquals(shortURL2, urlConverter.createShortURL(url2));

		assertNotEquals(shortURL1, shortURL2);
	}

}
