package com.noname.springtransactionproxylab.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(UUID from, UUID to, BigDecimal amount) {
}
