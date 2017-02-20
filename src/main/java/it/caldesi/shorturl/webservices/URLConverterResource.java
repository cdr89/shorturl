package it.caldesi.shorturl.webservices;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.caldesi.shorturl.utils.URLConverter;

@Path("/")
public class URLConverterResource {

	final static Logger logger = LoggerFactory.getLogger(URLConverterResource.class);

	private static final URLConverter urlConverter = new URLConverter(); // TODO
																			// initialized
																			// by
																			// configuration
																			// (Listener
																			// on
																			// startup);

	private static final String DEFAULT_PROTOCOL = "http://";

	@Context
	HttpServletRequest httpRequest;

	@GET
	@Path("/convert")
	@Produces(MediaType.APPLICATION_JSON)
	public Response convertURL(@QueryParam("url") String originalURL) {
		String shortURL = urlConverter.createShortURL(originalURL);

		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("originalURL", originalURL);
		jsonResponse.put("shortURL", shortURL);

		return Response.status(200).entity(jsonResponse.toString()).build();

	}

	@GET
	@Path("/{shortId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response redirectToOriginal(@PathParam("shortId") String shortId) {
		String originalURL = urlConverter.getOriginalURL(shortId);

		URL url;
		try {
			if (!originalURL.matches("^(https?|ftp)://.*$"))
				originalURL = DEFAULT_PROTOCOL + originalURL;
			url = new URL(originalURL);
		} catch (MalformedURLException e) {
			logger.error("error converting URL", e);
			return Response.status(404).entity("Wrong URL format").type(MediaType.APPLICATION_JSON).build();
		}

		URI redirectURI;
		try {
			redirectURI = url.toURI();
		} catch (URISyntaxException e) {
			logger.error("error converting URL", e);
			return Response.status(404).entity("Wrong URI format").type(MediaType.APPLICATION_JSON).build();
		}

		return Response.seeOther(redirectURI).build(); // HTTP 301 permanent
														// redirect
		// Response.temporaryRedirect(originalURL); // HTTP 302 temporary
		// redirect
	}
}
