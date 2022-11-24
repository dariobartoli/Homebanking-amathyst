package com.mindhub.homebanking.dtos;

import java.time.LocalDateTime;

public class PdfDTO {

    private LocalDateTime dateFrom;

    private LocalDateTime dateTo;

    private String accountNumber;

    public PdfDTO(LocalDateTime dateFrom, LocalDateTime dateTo, String accountNumber) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.accountNumber = accountNumber;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
