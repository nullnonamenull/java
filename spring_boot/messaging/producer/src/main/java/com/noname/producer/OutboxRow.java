package com.noname.producer;

import java.util.Map;
import java.util.UUID;

public record OutboxRow(UUID id, String eventType, Map<String, Object> payload) {
}