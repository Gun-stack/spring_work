/**
 * 
 */
package com.kosta.board.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.board.dao.BoardDao;
import com.kosta.board.dto.Board;
import com.kosta.board.dto.FileVo;
import com.kosta.board.dto.PageInfo;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardDao boardDao;
	
	@Override
	public List<Board> boardListByPage(PageInfo pageInfo) throws Exception {
		int boardCount = boardDao.selectBoardCount();
		if(boardCount==0) return null;
		int allPage = (int)Math.ceil((double)boardCount/10);
		int startPage = (pageInfo.getCurPage()-1)/10*10+1;
		int endPage = Math.min(startPage+10-1,allPage);
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		if(pageInfo.getCurPage()>pageInfo.getAllPage()) pageInfo.setCurPage(allPage);
		int row = (pageInfo.getCurPage()-1)*10+1;
		
		return boardDao.selectBoardList(row-1);
	}

	@Override
	public Board writeBoard(Board board, MultipartFile file) throws Exception {
		
		if(file!=null && !file.isEmpty()) { //null이 먼저 앞으로 가야함 순서중요!!!
			String dir = "c:/kkw/upload/";
			FileVo fileVO = new FileVo();
			fileVO.setDirectory(dir);
			fileVO.setName(file.getOriginalFilename());
			fileVO.setSize(file.getSize());
			fileVO.setContenttype(file.getContentType());
			fileVO.setData(file.getBytes());
			boardDao.insertFile(fileVO);
			Integer num= fileVO.getNum();
			
			File uploadFile= new File(dir+num);	
			file.transferTo(uploadFile);
			board.setFileurl(num+"");
		}
		boardDao.insertBoard(board);
		return boardDao.selectBoard(board.getNum());
	}

	@Override
	public void fileView(Integer num, OutputStream out) throws Exception {
		try {
			FileVo fileVo = boardDao.selectFile(num);
//			FileCopyUtils.copy(fileVo.getData(), out); 데이타 뿌려주기
			
			FileInputStream fis = new FileInputStream(fileVo.getDirectory()+num);//폴더에서 가져오기 
			FileCopyUtils.copy(fis, out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public Board boardDetail(Integer num) throws Exception {
		boardDao.updateBoardViewCount(num);
		return boardDao.selectBoard(num);
	}

	@Override
	public Board boardModify(Board board, MultipartFile file) throws Exception {
		if(file!=null && !file.isEmpty()) { 
			String dir = "c:/kkw/upload/";
			FileVo fileVO = new FileVo();
			fileVO = boardDao.selectFile(board.getNum());
			if (fileVO !=null) {
				fileVO.setDirectory(dir);
				fileVO.setName(file.getOriginalFilename());
				fileVO.setSize(file.getSize());
				fileVO.setContenttype(file.getContentType());
				fileVO.setData(file.getBytes());
				
				boardDao.updateFile(fileVO);
				Integer num= fileVO.getNum();
				File uploadFile= new File(fileVO.getDirectory()+num);	
				file.transferTo(uploadFile);	
				board.setFileurl(num+"");
				
			}else {
				FileVo fileVO2 = new FileVo();
				fileVO2.setDirectory(dir);
				fileVO2.setName(file.getOriginalFilename());
				fileVO2.setSize(file.getSize());
				fileVO2.setContenttype(file.getContentType());
				fileVO2.setData(file.getBytes());
				
				boardDao.insertFile(fileVO2);
				Integer num= fileVO2.getNum();
				
				File uploadFile= new File(dir+num);	
				file.transferTo(uploadFile);	
				board.setFileurl(num+"");
			}
		}
			boardDao.updateBoard(board);
		return boardDao.selectBoard(board.getNum());
	}
	@Override
	public void boardDelete(Integer num) throws Exception {
		Board board = boardDao.selectBoard(num);
		if(board !=null) {	
			if (board.getFileurl() !=null && board.getFileurl()!="") {
				boardDao.deleteBoard(num);
				boardDao.deleteFile(Integer.parseInt(board.getFileurl()));
			}else {
				boardDao.deleteBoard(num);
			}
		}
	}
	
	
	@Override
	   public String boardLike(String id, Integer num) throws Exception {
	      Map<String, Object> param = new HashMap<>();
	      param.put("id", id);
	      param.put("num", num);
	      //1. boardlike 테이블에 데이터 있는지 확인(mumber_id, board_num)
	      Integer likenum = boardDao.selectBoardLike(param);
	      Map<String, Object> res = new HashMap<>();
	      
	      if(likenum==null) { //2-1.없으면
	         boardDao.insertBoardLike(param); //boardlike에 삽입
	         boardDao.plusBoardLikeCount(num); //board 테이블에 좋아요수 증가
	         res.put("select", true);
	      } else { //2-2.있으면
	         boardDao.deleteBoardLike(param); //boardlike에서 삭제
	         boardDao.minusBoardLikeCount(num); //board 테이블에 좋아요수 감소
	         res.put("select", false);
	      }
	      //3. 좋아요수
	      Integer likecount = boardDao.selectLikeCount(num);
	      res.put("likecount", likecount);
	      //4. 좋아요 수 리턴
	      JSONObject jsonObj = new JSONObject(res);
	      
	      return jsonObj.toString();
	   }

	@Override
	public boolean isBoardLike(String id, Integer num) throws Exception {
		 Map<String, Object> param = new HashMap<>();
		 param.put("id", id);
		 param.put("num", num);
		Integer likenum = boardDao.selectBoardLike(param);
		if (likenum==null) return false;
		return true;
	}

	@Override
	public void updateViewCount(Integer num) throws Exception {
		boardDao.updateBoardViewCount(num);
	}


}
