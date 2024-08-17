package com.account.manager.demo.service.impl;

import com.account.manager.demo.dto.account.CreateAccountRequestDto;
import com.account.manager.demo.dto.account.AccountInfoResponseDto;
import com.account.manager.demo.entity.AccountEntity;
import com.account.manager.demo.exception.AccountNotFoundException;
import com.account.manager.demo.mapper.AccountMapper;
import com.account.manager.demo.repository.AccountRepository;
import com.account.manager.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public List<AccountInfoResponseDto> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toDto)
                .toList();
    }

    @Override
    public AccountInfoResponseDto findById(Long id) {
        AccountEntity entity = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        return accountMapper.toDto(entity);
    }

    @Override
    public AccountInfoResponseDto create(CreateAccountRequestDto dto) {
        AccountEntity entity = accountMapper.toEntity(dto);
        entity = accountRepository.save(entity);

        return accountMapper.toDto(entity);
    }
}
