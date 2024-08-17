package com.account.manager.demo.repository;

import com.account.manager.demo.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

}
