package com.codingrecipe.springex2.dao;

import com.codingrecipe.springex2.dto.Account;

import java.util.List;
import java.util.Map;

public interface AccountDao {
    void insertAccount(Account acc) throws  Exception;
    Account selectAccount(String id) throws Exception;
    void updateAccountBalance(Map<String, Object> param) throws Exception;
    List<Account> selectAccountList() throws Exception;
}
