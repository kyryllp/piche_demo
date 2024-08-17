package com.account.manager.demo.service;

import com.account.manager.demo.dto.account.CreateAccountRequestDto;
import com.account.manager.demo.dto.account.AccountInfoResponseDto;

import java.util.List;

public interface AccountService {

    List<AccountInfoResponseDto> findAll();

    AccountInfoResponseDto findById(Long id);

    AccountInfoResponseDto create(CreateAccountRequestDto dto);

}
