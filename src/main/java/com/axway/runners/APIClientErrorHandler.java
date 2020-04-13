package com.axway.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class APIClientErrorHandler implements ResponseErrorHandler {


	private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
	private static final Logger logger = LoggerFactory.getLogger(APIClientErrorHandler.class.getName());


	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		int statusCode = response.getRawStatusCode();
		String result = new BufferedReader(new InputStreamReader(response.getBody()))
				.lines().collect(Collectors.joining("\n"));
		logger.error("Error message  : {}", result);
		if( statusCode >= 200 && statusCode <= 210){
			logger.info("Success case");
		}else{
			throw new APIClientExcepton(result);
		}


	}

	@Override
	public boolean hasError(ClientHttpResponse arg0) throws IOException {
		return errorHandler.hasError(arg0);
	}

}