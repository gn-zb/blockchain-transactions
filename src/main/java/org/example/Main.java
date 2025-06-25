package org.example;

import api.moralis.MoralisAPI;

public class Main {
    public static void main(String[] args) {
        MoralisAPI api = new MoralisAPI();
        api.getWalletHistory();
        api.getTokenBalances();
    }
}