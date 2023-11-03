package com.kosta.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kosta.board.dto.Board;
import com.kosta.board.dto.Member;
import com.kosta.board.dto.PageInfo;
import com.kosta.board.service.BoardService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main() {
		
		return "main";
	}
	
	@RequestMapping(value="/boardlist", method=RequestMethod.GET)
	public ModelAndView boardList(@RequestParam(value="page", required=false, defaultValue = "1") Integer page) {
		ModelAndView mav = new ModelAndView();
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<Board> boardList = boardService.boardListByPage(pageInfo);
			mav.addObject("pageInfo",pageInfo);
			mav.addObject("boardList", boardList);
			mav.setViewName("boardlist");
		} catch(Exception e) {
			e.printStackTrace();
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value = "/boardwrite", method = RequestMethod.GET)
	public String boardwrite() {
		return "writeform";
	}
	@RequestMapping(value = "/boardwrite", method = RequestMethod.POST)
	public ModelAndView boardwrite(@ModelAttribute Board board, @RequestParam("file") MultipartFile file) {
		ModelAndView mav = new ModelAndView();
		try {
			Board writeboard =  boardService.writeBoard(board, file);
			mav.addObject("board",writeboard);
			mav.setViewName("detailform");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err","글등록오류");
			mav.setViewName("error");
		}
		return mav;
	}
	@RequestMapping(value = "/image/{num}")
	public void imageview(@PathVariable Integer num, HttpServletResponse response) {
		try {
			boardService.fileView(num, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	
	@RequestMapping(value = "/boarddetail", method = RequestMethod.GET)
	public ModelAndView boarddetail(@RequestParam("num") Integer num  ,HttpSession session ) {
		ModelAndView mav = new ModelAndView();
		try {
				Board board = boardService.boardDetail(num); // 글 불러오기
				mav.addObject("board",board);
				Member user =(Member)session.getAttribute("user");
				if (user !=null) {
					Boolean isLike = boardService.isBoardLike(user.getId(),num); //세션 유저의 좋아요 정보 찾아서 가져오기
					mav.addObject("select",isLike); // jsp에 불리언 값으로전송 
					}
				mav.setViewName("detailform");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err","글 조회 오류");
			mav.setViewName("error");
		}
		return mav;
	}
	@RequestMapping(value = "/boardmodify", method = RequestMethod.GET)
	public ModelAndView boardmodify(@RequestParam("num") Integer num) {
		ModelAndView mav = new ModelAndView();
		try {
			Board board = boardService.boardDetail(num);
			mav.addObject("board",board);
			mav.setViewName("modifyform");
			
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err","글 조회 오류");
			mav.setViewName("error");
		}
		return mav;

	}
	
	@RequestMapping(value = "/boardmodify", method = RequestMethod.POST)
	public ModelAndView boardmodify(@ModelAttribute Board board, @RequestParam("file") MultipartFile file  ) {
		ModelAndView mav = new ModelAndView();
		try {
				Board updateBoard = boardService.boardModify(board,file);
				mav.addObject("board",updateBoard);
				mav.setViewName("redirect:/boarddetail?num="+board.getNum());
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err","글 조회 오류");
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value = "/boarddelete", method = RequestMethod.GET)
	public String boarddelete(@RequestParam("num") Integer num, @RequestParam("page") Integer page  ) {
		ModelAndView mav = new ModelAndView();
		try {
				boardService.boardDelete(num);
				return "redirect:/boardlist?page="+page;
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err","글 삭제 오류");
			return "error";

		}
	}
	
	@RequestMapping(value = "/like" , method = RequestMethod.POST)
	@ResponseBody
	public String boardlike(@RequestParam ("num") Integer num, HttpSession session) {
		try {
			Member user = (Member)session.getAttribute("user");
			return boardService.boardLike(user.getId(),num);
		} catch (Exception e) {
			e.printStackTrace();
		return "error";}
		}
	
	
	
}
