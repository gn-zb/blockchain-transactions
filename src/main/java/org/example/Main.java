package org.example;

import api.moralis.MoralisAPI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {
        MoralisAPI api = new MoralisAPI();
        api.GetWalletHistory();


    }
}

// Play around with these APIs

// Get wallet transactions: play around params - limit - from_date, to_date
// Get Token balances: https://docs.moralis.com/web3-data-api/evm/reference/wallet-api/get-wallet-token-balances-price?address=0xcB1C1FdE09f811B294172696404e88E658659905&chain=eth&token_addresses=[]
// How to set List<String> params, Boolean