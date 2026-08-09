package com.noname.producer;

import java.util.UUID;

public record OutboxRow(UUID id, String eventType, byte[] body) {
}