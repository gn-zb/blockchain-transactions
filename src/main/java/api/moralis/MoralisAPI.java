package api.moralis;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dto.TransactionDTO;

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
    private Gson gson;

    public MoralisAPI() {
        this.gson = new Gson(); // gson ( having new data after run getWalletHistory)
    }

    // Method signature - takes wallet address and network, returns list of transaction objects
    public ArrayList<TransactionDTO> getWalletHistory(String address, String network) {
        // Create an instance of HttpClient for making requests
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // Empty list to store results
        ArrayList<TransactionDTO> transactions = new ArrayList<>();

        try {
            // BUILD THE API REQUEST
            // Construct the complete API URL by combining base URL with specific endpoint
            String endpoint = BASE_URL + "/wallets/" + address + "/history";

            // Create URI builder to add query parameters to URL
            URIBuilder builder = new URIBuilder(endpoint);

            // Add query parameters to the request
            builder.setParameter("address", address);
            builder.setParameter("chain", "eth");
            builder.setParameter("limit", "10");
            builder.setParameter("include_internal_transactions", "true");
            builder.setParameter("from_date", "2024-12-01");
            builder.setParameter("to_date", "2024-12-31");
            builder.setParameter("order", "DESC");

            // Build the final URI with parameters
            URI uri = builder.build();
            // Print for debugging
            System.out.println("Making request to: " + uri.toString());

            // MAKE THE HTTP REQUEST
            // Create HttpGet request object with the constructed URI
            HttpGet request = new HttpGet(uri);

            // Add required headers: Authentication key and telling the server that we want JSON response
            request.addHeader("X-API-Key", apiKey);
            request.addHeader("Accept", "application/json");

            // Send the request and get the response
            CloseableHttpResponse response = httpClient.execute(request);

            // PROCESS THE RESPONSE
            // Get HTTP status code
            int statusCode = response.getStatusLine().getStatusCode();
            //Print debug info
            System.out.println("Getting wallet history for: " + address + " on " + network);
            System.out.println("Response Status Code: " + statusCode);

            // Extracts the response body
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // convert response body to a string
                String result = EntityUtils.toString(entity);
                // print the response body
                System.out.println("Response Body: " + result);

                // Convert JSON string into Java JsonObject
                JsonObject jsonResponse = gson.fromJson(result, JsonObject.class);


                // Checks if response has "result" field, then gets the array of transactions
                if (jsonResponse.has("result")) {
                    JsonArray resultArray = jsonResponse.getAsJsonArray("result");

                    // Loops through each transaction in the array
                    for (JsonElement element : resultArray) {
                        JsonObject transaction = element.getAsJsonObject();

                        // Extract specific fields from each transaction from JSON and create TransactionDTO
                        String hash = transaction.get("hash").getAsString();
                        String fromAddress = transaction.get("from_address").getAsString();
                        String value = transaction.get("value").getAsString();
                        String blockTimestamp = transaction.get("block_timestamp").getAsString();

                        // Convert value from Wei to double
                        Double amount = Double.parseDouble(value);

                        // Creates a TransactionDTO object with the extracted data and adds it to the list
                        TransactionDTO transactionDTO = new TransactionDTO(hash, fromAddress, amount, blockTimestamp);
                        transactions.add(transactionDTO);
                    }

                    System.out.println("Parsed " + transactions.size() + " transactions");

                    // Print parsed transactions
                    for (TransactionDTO singleTransaction : transactions) {
                        System.out.println("Transaction: " + singleTransaction.getTransactionId() +
                                " | Amount: " + singleTransaction.getAmount() +
                                " | From: " + singleTransaction.getAddress());
                    }
                }

                // Returns the list of TransactionDTO objects
                return transactions;
            } else {
                System.out.println("No response body");
                return transactions;
            }
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
            return transactions;
        } catch (URISyntaxException e) {
            System.err.println("URI Syntax Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void getTokenBalances(String address) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            String endpoint = BASE_URL + "/wallets/" + address + "/tokens";

            URIBuilder builder = new URIBuilder(endpoint);
            builder.setParameter("chain", "eth");

            ArrayList<String> retrievedTokens = new ArrayList<>();
            retrievedTokens.add("0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48"); // USDC token contract
            retrievedTokens.add("0xdAC17F958D2ee523a2206206994597C13D831ec7"); // USDT token contract
            retrievedTokens.add("0x6810e776880C02933D47DB1b9fc05908e5386b96"); // GNO token contract
            retrievedTokens.add("0x6B175474E89094C44Da98b954EedeAC495271d0F"); // DAI token contract
            retrievedTokens.add("0x514910771AF9Ca656af840dff83E8264EcF986CA"); // LINK token contract
            retrievedTokens.add("0x0F5D2fB29fb7d3CFeE444a200298f468908cC942"); // MANA token contract

            for (int i = 0; i < retrievedTokens.size(); i++) {
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
