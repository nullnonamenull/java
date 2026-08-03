package com.noname.producer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxMessage, UUID> {

    @Query(value = """
        select * from outbox_message
        where status = 'NEW' and next_attempt_at <= now()
        order by created_at
        limit :limit
        for update skip locked
    """, nativeQuery = true)
    List<OutboxMessage> lockBatch(int limit);

}
