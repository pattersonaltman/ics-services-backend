package com.cognixia.jump.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// will create new jwts
// pull up info from existing jwts
@Service
public class JwtUtil {
	
	// used with algorithm to hash/encode our token
	private final String SECRET_KEY = "jump";
	
	// get the username for this token
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	
	// get expiration date for this token
	public Date extractExpiration(String token) {
		
		// :: -> method reference
		//    -> pointer, access a method in a class
		//	  -> that way we can pass a method as an argument
		return extractClaim(token, Claims::getExpiration);
	}
	
	
	// takes a token and a claims resolver to find out what the claims are for that particular token
	// so find that data that was passed in through the token and be able to access it again (username, expiration date)
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver ) {
		
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	
	private Claims extractAllClaims(String token) {
		
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	
	// checks if the token has expired yet by checking the current date & time and comparing it to the expiration
	private Boolean isTokenExpired(String token) {
		
		return extractExpiration(token).before(new Date());
	}
	
	
	// returns those generated tokens after a successful authentication
	public String generateTokens(UserDetails userDetails) {
		
		// claims info/data you want to include in payload of token besides the user info
		Map<String, Object> claims = new HashMap<>();
		
		// returns token for user given along with any claims
		return createToken(claims, userDetails.getUsername());
	}
	
	// creates the token
	private String createToken(Map<String, Object> claims, String subject) {
		
		// sets claims
		// subject (user that is being authenticated)
		// set when the token was issued
		// set expiration when token expires and can be no longer used (here its set for 10 hrs)
		// sign it with particular algorithm and secret key that lets you know this token is authentic
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt( new Date( System.currentTimeMillis() ) )
				.setExpiration( new Date( System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 365 ) )
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	// will validate the token and check if the current token is for the right user requesting it and that the token isn't expired
	public Boolean validateToken(String token, UserDetails userDetails) {
		
		final String username = extractUsername(token);
		
		return ( username.equals( userDetails.getUsername() ) && !isTokenExpired(token) );
	}
}










