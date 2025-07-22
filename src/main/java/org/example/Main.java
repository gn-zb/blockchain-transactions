package org.example;

import api.moralis.MoralisAPI;
import dto.TransactionDTO;
import database.TransactionDAO;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        MoralisAPI api = new MoralisAPI();
        TransactionDAO dao = new TransactionDAO(); // database object

        String giangAddress = "0x4838B106FCe9647Bdf1E7877BF73cE8B0BAD5f97";
        String network = "eth";
        // Get wallet history and store in ArrayList
        ArrayList<TransactionDTO> transactions = api.getWalletHistory(giangAddress, network);

        // Save to database
        dao.saveTransactions(transactions);
        System.out.println("API data saved to PostgreSQL database");

    }
}