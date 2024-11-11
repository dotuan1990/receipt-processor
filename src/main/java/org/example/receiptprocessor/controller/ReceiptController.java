package org.example.receiptprocessor.controller;

import org.example.receiptprocessor.domain.Receipt;
import org.example.receiptprocessor.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    @Autowired
    private ReceiptService receiptService;
    @PostMapping("/process")
    public ResponseEntity<?> processReceipt(@RequestBody Receipt receipt) {
        Receipt processedReceipt = receiptService.processReceipt(
                receipt.getRetailer(),
                receipt.getPurchaseDate(),
                receipt.getPurchaseTime(),
                receipt.getItems(),
                receipt.getTotal()
        );
        Map<String, String> response = new HashMap<>();
        response.put("id", processedReceipt.getId());
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/{id}/points")
    public ResponseEntity<?> getPoints(@PathVariable String id) {
        Receipt receipt = receiptService.getReceiptById(id);

        if (receipt == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Integer> response = new HashMap<>();
        response.put("points", receipt.getPoints());
        return ResponseEntity.ok().body(response);
    }
}
