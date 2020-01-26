package com.udemy.usersapi.service.feign.responsehandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;

//This method can be used to handle all the feign exceptions..
//@Component
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {

		switch (response.status()) {
		case 400:
			break;
		case 404:
			if (methodKey.contains("getAlbums"))
				return new ResponseStatusException(HttpStatus.valueOf(response.status()),
						"User Albums are not found..");
			break;
		default:
			return new Exception(response.reason());
		}

		return null;
	}

}
