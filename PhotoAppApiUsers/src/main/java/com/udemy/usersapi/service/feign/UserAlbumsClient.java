package com.udemy.usersapi.service.feign;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.udemy.usersapi.model.AlbumResponse;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumsFallbackFactory.class)
public interface UserAlbumsClient {

	@GetMapping("/users/{userId}/albumss")
	public List<AlbumResponse> getAlbums(@PathVariable String userId);

}

// This below method of the class will be called as a fallback method when the album microservice fails!
@Component
class AlbumsFallbackFactory implements FallbackFactory<UserAlbumsClient> {

	@Override
	public UserAlbumsClient create(Throwable cause) {
		return new AlbumServiceFallback(cause);
	}
}

class AlbumServiceFallback implements UserAlbumsClient {

	Logger logger = LoggerFactory.getLogger(AlbumServiceFallback.class);

	private final Throwable cause;

	public AlbumServiceFallback(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public List<AlbumResponse> getAlbums(String userId) {
		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			logger.error("Probably the album microservice is down...");
		} else {
			logger.error(cause.getLocalizedMessage());
		}
		return Collections.emptyList();
	}

}
