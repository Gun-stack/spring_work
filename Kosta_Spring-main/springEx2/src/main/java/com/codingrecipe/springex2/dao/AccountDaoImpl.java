package com.codingrecipe.springex2.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.codingrecipe.springex2.dto.Account;
@Repository
public class AccountDaoImpl implements AccountDao{
    // sqlSessionTemplate에 대한 의존성? 주입??
	@Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public void insertAccount(Account acc) throws Exception {
    	System.out.println("Dao" + acc.getId());
        sqlSessionTemplate.insert("mapper.account.insertAccount",acc);
    }

    @Override
    public Account selectAccount(String id) throws Exception {
        return sqlSessionTemplate.selectOne("mapper.account.selectAccount", id);
    }

    @Override
    @Transactional
    public void updateAccountBalance(Map<String, Object> param) throws Exception {
        sqlSessionTemplate.update("mapper.account.updateBalance", param);
    }

    @Override
    public List<Account> selectAccountList() throws Exception {
        return sqlSessionTemplate.selectList("mapper.account.selectAccountList");
    }
}
