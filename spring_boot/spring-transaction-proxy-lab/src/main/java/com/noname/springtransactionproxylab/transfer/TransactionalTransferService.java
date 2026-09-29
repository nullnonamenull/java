package com.noname.springtransactionproxylab.transfer;

import com.noname.springtransactionproxylab.account.AccountRepository;
import com.noname.springtransactionproxylab.dto.TransferRequest;
import com.noname.springtransactionproxylab.exception.TransferException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionalTransferService {

    private final AccountRepository accountRepository;

    @Transactional
    public void transactionalTransferWithRuntimeException(final TransferRequest transferRequest) {
        /* TRANSACTION STARTED */
        var fromAcc = accountRepository.getReferenceById(transferRequest.from());
        var toAcc = accountRepository.getReferenceById(transferRequest.to());

        fromAcc.setAmount(fromAcc.getAmount().subtract(transferRequest.amount()));
        accountRepository.save(fromAcc);
        if (true) {
            throw new RuntimeException();
            /* TRANSACTION ROLLBACK */
        }
        toAcc.setAmount(toAcc.getAmount().add(transferRequest.amount()));
        accountRepository.save(toAcc);
        /* TRANSACTION COMMITED */
    }

    /*
         transakcja nie zadziała dla customowego exceptiona -> trzeba dodac rollbackFor jako parametr adnotacji

         domyslnie rollback jest dla Exception oraz RuntimeException
         checked exception zostaje zakoncoczny commitem!
     */
    @Transactional(rollbackFor = TransferException.class)
    public void transactionalTransferWithCustomException(final TransferRequest transferRequest) throws TransferException {
        /* TRANSACTION STARTED */
        var fromAcc = accountRepository.getReferenceById(transferRequest.from());
        var toAcc = accountRepository.getReferenceById(transferRequest.to());

        fromAcc.setAmount(fromAcc.getAmount().subtract(transferRequest.amount()));
        accountRepository.save(fromAcc);
        if (true) {
            throw new TransferException();
            /* TRANSACTION ROLLBACK */
        }
        toAcc.setAmount(toAcc.getAmount().add(transferRequest.amount()));
        accountRepository.save(toAcc);
        /* TRANSACTION COMMITED */
    }

}
