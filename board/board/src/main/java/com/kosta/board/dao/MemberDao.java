package com.kosta.board.dao;

import com.kosta.board.dto.Member;

public interface MemberDao {
	Member selectMember(String id) throws Exception;
	Member insertMember(Member member) throws Exception;
}
