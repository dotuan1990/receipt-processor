package org.example.receiptprocessor.service;

import org.example.receiptprocessor.domain.Item;
import org.example.receiptprocessor.domain.Receipt;

import java.util.List;


public interface ReceiptService {
    Receipt processReceipt(String retailer, String purchaseDate, String purchaseTime, List<Item> items, double total);
    Receipt getReceiptById(String id);
}
