package com.debashis.exception;

public class InventoryNotAvailable extends RuntimeException {
    public InventoryNotAvailable(String message) {
        super(message);
    }
}
