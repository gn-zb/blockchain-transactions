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
import java.util.ArrayList;

public class MoralisAPI {
    private final String BASE_URL = "https://deep-index.moralis.io/api/v2.2";
    private String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJub25jZSI6IjgyOWI3Y2U3LWYzMWUtNGJlNC1hM2Q3LWY4NDFkOGRkNzNhNiIsIm9yZ0lkIjoiNDQ1NjczIiwidXNlcklkIjoiNDU4NTQyIiwidHlwZUlkIjoiOTdjZThiZmEtYjExNC00YThkLTg4M2EtNTc2OTcyYmRhZWM3IiwidHlwZSI6IlBST0pFQ1QiLCJpYXQiOjE3NDY1OTQ4MjcsImV4cCI6NDkwMjM1NDgyN30.O_dNXACH90ubiU7sPxBkJTaRP0_kh1mGiInhmPqeEMU \n";
    private String walletAddress = "0x4838B106FCe9647Bdf1E7877BF73cE8B0BAD5f97";

    public void getWalletHistory() {
        // Create an instance of HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            String endpoint = BASE_URL + "/wallets/" + walletAddress + "/history";

            // Create URI with query parameters using URIBuilder
            URIBuilder builder = new URIBuilder(endpoint);

            // Add query parameters
            builder.setParameter("chain", "eth");
            builder.setParameter("limit", "10");
            builder.setParameter("include_internal_transactions", "true"); //HW
            builder.setParameter("from_date", "2024-12-01");               //HW
            builder.setParameter("to_date", "2024-12-31");                 //HW
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
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("Response Status Code: " + statusCode);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    System.out.println("Response Body: " + result);
                } else {
                    System.out.println("No response body");
                }

            } finally {
                response.close();
            }
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.err.println("URI Syntax Error: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                System.err.println("Error closing HttpClient: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void getTokenBalances() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            String endpoint = BASE_URL + "/wallets/" + walletAddress + "/tokens";

            URIBuilder builder = new URIBuilder(endpoint);
            builder.setParameter("chain", "eth");

            ArrayList<String> retrievedTokens = new ArrayList<>();
            retrievedTokens.add("0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48"); // USDC token contract
            retrievedTokens.add("0xdAC17F958D2ee523a2206206994597C13D831ec7"); // USDT token contract
            retrievedTokens.add("0x6810e776880C02933D47DB1b9fc05908e5386b96"); // GNO token contract
            retrievedTokens.add("0x6B175474E89094C44Da98b954EedeAC495271d0F"); // DAI token contract
            retrievedTokens.add("0x514910771AF9Ca656af840dff83E8264EcF986CA"); // LINK token contract
            retrievedTokens.add("0x0F5D2fB29fb7d3CFeE444a200298f468908cC942"); // MANA token contract

            for (int i = 0; i < retrievedTokens.size(); i++){
                builder.setParameter("token_addresses[" + i + "]", retrievedTokens.get(i));
            }

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.addHeader("X-API-Key", apiKey);
            request.addHeader("Accept", "application/json");

            CloseableHttpResponse response = httpClient.execute(request);

            try {
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("Response Status Code: " + statusCode);

                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    System.out.println("Response Body: " + result);
                } else {
                    System.out.println("No response body");
                }

            } finally {
                response.close();
            }
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.err.println("URI Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
