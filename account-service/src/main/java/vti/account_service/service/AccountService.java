package vti.account_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vti.account_service.entity.Account;
import vti.account_service.repository.IAccountRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final IAccountRepository acRepository;

    @Override
    public List<Account> getListAccounts() {
        return acRepository.findAll();
    }
}
