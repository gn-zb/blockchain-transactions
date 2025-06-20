package api.moralis;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MoralisAPI {
    private final String BASE_URL = "https://deep-index.moralis.io/api/v2.2";
    private String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJub25jZSI6IjgyOWI3Y2U3LWYzMWUtNGJlNC1hM2Q3LWY4NDFkOGRkNzNhNiIsIm9yZ0lkIjoiNDQ1NjczIiwidXNlcklkIjoiNDU4NTQyIiwidHlwZUlkIjoiOTdjZThiZmEtYjExNC00YThkLTg4M2EtNTc2OTcyYmRhZWM3IiwidHlwZSI6IlBST0pFQ1QiLCJpYXQiOjE3NDY1OTQ4MjcsImV4cCI6NDkwMjM1NDgyN30.O_dNXACH90ubiU7sPxBkJTaRP0_kh1mGiInhmPqeEMU \n";

    public void getWalletHistory(){
        // Create an instance of HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            String walletAddress = "0x7DbfeD5686847113b527DC215DBA4E332DF8cc6c";
            String endpoint = BASE_URL + "/wallets/" + walletAddress + "/history";

            // Create URI with query parameters using URIBuilder
            URIBuilder builder = new URIBuilder(endpoint);

            // Add query parameters
            builder.setParameter("chain", "eth");
            builder.setParameter("limit", "10");
            builder.setParameter("include_internal_transactions", "true");
            builder.setParameter("from_date", "2024-12-01");
            builder.setParameter("to_date", "2024-12-31");
            builder.setParameter("order", "DESC");

            // Build the URI with parameters
            URI uri = builder.build();
            System.out.println("Making request to: " + uri.toString());

            // Create HttpGet request with the URI
            HttpGet request = new HttpGet(uri);

            request.addHeader("X-API-Key", apiKey);
            request.addHeader("Accept", "application/json");

            // Execute the request and get the response
            CloseableHttpResponse response = httpClient.execute(request);

            try {
                // Get the status code from the response
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("Response Status Code: " + statusCode);

                // Get the response entity (body)
                HttpEntity entity = response.getEntity();

                // If response has a body, read it as a string
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    System.out.println("Response Body: " + result);
                } else {
                    System.out.println("No response body");
                }
            } finally {
                // Close the response
                response.close();
            }
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.err.println("URI Syntax Error: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            // Close the client
            try {
                httpClient.close();
            } catch (IOException e) {
                System.err.println("Error closing HttpClient: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}