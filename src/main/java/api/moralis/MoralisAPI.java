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
    public void GetWalletHistory(){
        // Create an instance of HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {

            String walletAddress = "0x7DbfeD5686847113b527DC215DBA4E332DF8cc6c";
            String baseUrl = "https://deep-index.moralis.io/api/v2.2/wallets/" + walletAddress + "/history";
            // Create URI with query parameters using URIBuilder
            URIBuilder builder = new URIBuilder(baseUrl);


            // Add query parameters
            builder.setParameter("address", "0x7DbfeD5686847113b527DC215DBA4E332DF8cc6c");
            builder.setParameter("chain", "eth");
            builder.setParameter("limit", "1");


            // Build the URI with parameters
            URI uri = builder.build();

            // Create HttpGet request with the URI
            HttpGet request = new HttpGet(uri);

            request.addHeader("X-API-Key", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJub25jZSI6IjgyOWI3Y2U3LWYzMWUtNGJlNC1hM2Q3LWY4NDFkOGRkNzNhNiIsIm9yZ0lkIjoiNDQ1NjczIiwidXNlcklkIjoiNDU4NTQyIiwidHlwZUlkIjoiOTdjZThiZmEtYjExNC00YThkLTg4M2EtNTc2OTcyYmRhZWM3IiwidHlwZSI6IlBST0pFQ1QiLCJpYXQiOjE3NDY1OTQ4MjcsImV4cCI6NDkwMjM1NDgyN30.O_dNXACH90ubiU7sPxBkJTaRP0_kh1mGiInhmPqeEMU ");  // API key in custom header
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
                }
            } finally {
                // Close the response
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            // Close the client
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
