package com.noname.springtransactionpropagationlab.auditLogs.service;

import com.noname.springtransactionpropagationlab.auditLogs.domain.AuditLog;
import com.noname.springtransactionpropagationlab.auditLogs.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

//    @Transactional // REQUIRED default
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAudit(UUID id, String name, String status) {
        var auditLog = new AuditLog();
        auditLog.setId(UUID.randomUUID());
        auditLog.setMessage("Created " + name + " with status " + status + "and id " + id);
        auditLogRepository.save(auditLog);
//        if (true) {
//            throw new RuntimeException();
//        }
    }
}
