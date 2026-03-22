package com.github.campaignmanager.exception;

public class WrongBidAmountException extends RuntimeException {
    public WrongBidAmountException(String message) {
        super(message);
    }
}