package org.example.receiptprocessor.service;

import org.example.receiptprocessor.domain.Item;
import org.example.receiptprocessor.domain.Receipt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    // receiptStore is mock database
    private final Map<String, Receipt> receiptStore = new HashMap<>();

    @Override
    public Receipt processReceipt(String retailer, String purchaseDate, String purchaseTime, List<Item> items, double total) {
        // Generate a unique ID for the receipt
        String receiptKey = generateReceiptKey(retailer, purchaseDate, purchaseTime, items, total);
        String id = UUID.nameUUIDFromBytes(receiptKey.getBytes()).toString();
        if(receiptStore.containsKey(id)) {
            return receiptStore.get(id);
        }
        // Create the new receipt
        Receipt receipt = new Receipt(id, retailer, purchaseDate, purchaseTime, items, total,0);

        // Calculate points based on the rules
        int points = calculatePoints(receipt);
        receipt.setPoints(points);

        // Store the receipt in memory
        receiptStore.put(id, receipt);

        return receipt;
    }
    private String generateReceiptKey(String retailer, String purchaseDate, String purchaseTime, List<Item> items, double total) {
        // Create a unique key based on receipt attributes (excluding ID)
        return (retailer +purchaseDate + purchaseTime + items.hashCode() + total);
    }
    @Override
    public Receipt getReceiptById(String id) {
        return receiptStore.get(id);
    }
    // Calculate points based on the receipt details
    private int calculatePoints(Receipt receipt) {
        int points = 0;

        // Rule 1: 1 point for every alphanumeric character in the retailer name
        points += (int) receipt.getRetailer().chars().filter(Character::isLetterOrDigit).count();

        // Rule 2: 50 points if the total is a round dollar amount
        if (receipt.getTotal() % 1 == 0) {
            points += 50;
        }

        // Rule 3: 25 points if the total is a multiple of 0.25
        if (receipt.getTotal()%1==0) {
            points += 25;
        }

        // Rule 4: 5 points for every two items on the receipt
        points += (receipt.getItems().size() / 2) * 5;

        // Rule 5: Points based on item descriptions
        points += calculateItemDescriptionPoints(receipt.getItems());

        // Rule 6: 6 points if the day in the purchase date is odd
        if (isPurchaseDayOdd(receipt.getPurchaseDate())) {
            points += 6;
        }

        // Rule 7: 10 points if the purchase time is between 2:00pm and 4:00pm
        if (isBetween2And4pm(receipt.getPurchaseTime())) {
            points += 10;
        }

        return points;
    }

    // Rule 5: Calculate points based on item description length
    private int calculateItemDescriptionPoints(List<Item> items) {
        int points = 0;
        for (Item item : items) {
            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                // Rule 5: Multiply the price by 0.2 and round up to the nearest integer
                int itemPoints = (int) Math.ceil(item.getPrice() * 0.2);
                points += itemPoints;
            }
        }
        return points;
    }

    // Rule 6: Check if the day in the purchase date is odd
    private boolean isPurchaseDayOdd(String purchaseDate) {
        String[] dateParts = purchaseDate.split("-");
        int day = Integer.parseInt(dateParts[2]);
        return day % 2 != 0;
    }

    // Rule 7: Check if the purchase time is between 2:00pm and 4:00pm
    private boolean isBetween2And4pm(String purchaseTime) {
        String[] timeParts = purchaseTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        return (hour == 14 && minute >= 0) || (hour == 15 && minute < 60);
    }
}
