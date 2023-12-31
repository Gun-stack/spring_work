package com.codingrecipe.springex2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingrecipe.springex2.dao.MemberDao;
import com.codingrecipe.springex2.dto.Member;
@Service
public class MemberServiceImpl implements MemberService{
    @Autowired
	private MemberDao memberDao;

    public MemberDao getMemberDao() {
        return memberDao;
    }

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Member selectMember(String id) throws Exception {
        return memberDao.selectMember(id);
    }

    @Override
    public void insertMember(Member member) throws Exception {
        memberDao.insertMember(member);
    }
}
