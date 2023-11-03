package com.kosta.board.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kosta.board.dto.Member;
import com.kosta.board.service.MemberService;

@Controller
public class MemberController {
	
	 @Autowired
	 private MemberService memberService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("id") String id, @RequestParam("password") String password
			, HttpSession session, Model model) {
		  try{
	            Member member = memberService.login(id,password);
	            if(member == null || !member.getPassword().equals(password)){
	                model.addAttribute("err", "로그인 실패");
	                return "error";
	            }
	            session.setAttribute("user", member);
	            return "main";
	        }catch (Exception e){
	            e.printStackTrace();
	            System.out.println(e.getMessage());
	            System.out.println(e.getCause());
	            model.addAttribute("err", "에러 발생");
	            return "error";
	        }
	}
	
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join() {
		return "join";
	}
	
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(@RequestParam String id, @RequestParam String name,  
			@RequestParam String password, @RequestParam String email, 
			@RequestParam String address, Model model) {
		 Member member = new Member(id, name, password, email, address);
		 try{
			 if (memberService.idCheck(member.getId())=="noexist") 		
	            memberService.join(member);
	            return "login";
	        }catch (Exception e){
	            e.printStackTrace();
	            System.out.println(e.getMessage());
	            System.out.println(e.getCause());
	            model.addAttribute("err", "에러 발생");
	            return "error";
	        }
	}
	 @RequestMapping(value = "/logout",method = RequestMethod.GET)
	 public String logout(HttpSession session) {
		 session.removeAttribute("user");		
		 return"main";
	 }
	 
		 @RequestMapping(value = "/idcheck" , method = RequestMethod.POST)
		 @ResponseBody
		public String idcheck(@RequestParam ("id") String id) {
			try {
				return memberService.idCheck(id);
			} catch (Exception e) {
				e.printStackTrace();
			return "error";}
			}
	
}
