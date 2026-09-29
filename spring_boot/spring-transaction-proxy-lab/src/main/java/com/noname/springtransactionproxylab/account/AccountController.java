package com.noname.springtransactionproxylab.account;

import com.noname.springtransactionproxylab.dto.TransferRequest;
import com.noname.springtransactionproxylab.exception.TransferException;
import com.noname.springtransactionproxylab.transfer.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody String name) {
        accountService.create(name);

        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }

//    @PostMapping("/transfer")
//    public ResponseEntity<Void> transferAccount(@RequestBody TransferRequest transferRequest) {
//        transferService.transfer(transferRequest);
//        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
//    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferAccount(@RequestBody TransferRequest transferRequest) throws TransferException {
        transferService.transferCustomException(transferRequest);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }

}
