package Util;

import Exceptions.LLCLIException;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LLWebUtil {
    public static InputStream getCanonicalZip(String labName) throws LLCLIException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:9000/lab/canonical/"+labName))
                    .GET()
                    .header("product_key", LLPropsUtil.getPkey())
                    .build();
        } catch (URISyntaxException e) {
            throw new LLCLIException("There was some error retrieving your lab from the cloud.");
        }
        InputStream is = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(HttpResponse::body).join();
        return is;
    }
    public static InputStream getSavedZip(String labName) throws LLCLIException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:9000/lab/saved/"+labName))
                    .GET()
                    .header("product_key", LLPropsUtil.getPkey())
                    .build();
        } catch (URISyntaxException e) {
            throw new LLCLIException("There was some error retrieving your lab from the cloud.");
        }
        InputStream is = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(HttpResponse::body).join();
        return is;
    }
}