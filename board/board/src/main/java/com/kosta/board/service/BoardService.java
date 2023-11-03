package com.kosta.board.service;

import java.io.OutputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kosta.board.dto.Board;
import com.kosta.board.dto.PageInfo;

public interface BoardService {
	List<Board> boardListByPage(PageInfo pageInfo) throws Exception;
	Board writeBoard(Board board, MultipartFile file) throws Exception;
	void fileView(Integer num,OutputStream out) throws Exception;
	Board boardDetail(Integer num) throws Exception;
	Board boardModify(Board board , MultipartFile file) throws Exception;
	
	void boardDelete(Integer num) throws Exception;
	

	String boardLike(String id, Integer num) throws Exception;
	boolean isBoardLike(String id, Integer num) throws Exception;
	void updateViewCount(Integer num)throws Exception;
}
