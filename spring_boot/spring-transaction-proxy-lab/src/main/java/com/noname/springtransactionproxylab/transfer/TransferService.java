package com.noname.springtransactionproxylab.transfer;

import com.noname.springtransactionproxylab.dto.TransferRequest;
import com.noname.springtransactionproxylab.exception.TransferException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferService {

//    private final AccountRepository accountRepository;
    private final TransactionalTransferService transactionalTransferService;


    public void transfer(final TransferRequest transferRequest) {
        transactionalTransferService.transactionalTransferWithRuntimeException(transferRequest);
    }

    public void transferCustomException(final TransferRequest transferRequest) throws TransferException {
        transactionalTransferService.transactionalTransferWithCustomException(transferRequest);
    }

    /*
        transkacja nie jest odpalana poniewaz wywolanie nie przeszlo przez bean proxy.
        aby transkacja zadzialala trzeba umiescic transakcyjna metode w osobnym beanie
        a wtedy wywolanie metody przejdzie najpierw przez spring proxy, który rozpocznie transakcje
        dzięki temu, że metoda została oznaczona adnotacją
     */
//    public void transfer(final TransferRequest transferRequest) {
//        transactionalTransfer(transferRequest);
//    }
//
//    @Transactional
//    public void transactionalTransfer(final TransferRequest transferRequest) {
//        var fromAcc = accountRepository.getReferenceById(transferRequest.from());
//        var toAcc = accountRepository.getReferenceById(transferRequest.to());
//
//        fromAcc.setAmount(fromAcc.getAmount().subtract(transferRequest.amount()));
//        accountRepository.save(fromAcc);
//        if (true) {
//            throw new RuntimeException();
//        }
//        toAcc.setAmount(toAcc.getAmount().add(transferRequest.amount()));
//        accountRepository.save(toAcc);
//    }


    /*
            metoda transfer rzuca wyjątkiem po zapisaniu zmian na koncie dłużnika
            ale przed dokonaniem zmian na koncie wierzyciela.

            transakcja nie jest założona (pomijajac transakcje zakladane domylnsie na save()) przez co zmiana jest dokonana
            tylko na jednym koncie -> dłużnika
     */
//    public void transfer(final TransferRequest transferRequest) {
//        var fromAcc = accountRepository.getReferenceById(transferRequest.from());
//        var toAcc = accountRepository.getReferenceById(transferRequest.to());
//
//        fromAcc.setAmount(fromAcc.getAmount().subtract(transferRequest.amount()));
//        accountRepository.save(fromAcc);
//        if (true) {
//            throw new RuntimeException();
//        }
//        toAcc.setAmount(toAcc.getAmount().add(transferRequest.amount()));
//        accountRepository.save(toAcc);
//    }

}
