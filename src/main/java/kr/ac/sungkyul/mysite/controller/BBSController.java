package kr.ac.sungkyul.mysite.controller;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.ac.sungkyul.mysite.annotation.Auth;
import kr.ac.sungkyul.mysite.annotation.AuthUser;
import kr.ac.sungkyul.mysite.service.BBSService;
import kr.ac.sungkyul.mysite.vo.AttachFileVO;
import kr.ac.sungkyul.mysite.vo.BoardVO;
import kr.ac.sungkyul.mysite.vo.UserVo;

@Controller
@RequestMapping("/bbs")
public class BBSController {

	@Autowired
	private BBSService bbsService;

	@RequestMapping(value = "view2", method = RequestMethod.GET)
	public String view2() {

		return "board/view2";
	}
	
	
	// 상세
	@ResponseBody
	@RequestMapping(value = "readAjax", method = RequestMethod.POST)
	public BoardVO readBoardAjax(@RequestBody BoardVO vo) {
		
		System.out.println(vo.toString());
		return vo;
	}
	

	/*@ResponseBody
	@RequestMapping(value = "readAjax", method = RequestMethod.POST)
	public BoardVO readBoardAjax(int no) {
		BoardVO boardVO = bbsService.selectBoard(no);
    	return boardVO;	
	}
	*/
	
/*	// 상세
		@ResponseBody
		@RequestMapping(value = "readAjax", method = RequestMethod.POST)
		public BoardVO readBoardAjax(int no) {
			BoardVO boardVO = bbsService.selectBoard(no);
			return boardVO;
		}
*/	
	/*// 상세
	@ResponseBody
	@RequestMapping(value = "readAjax", method = RequestMethod.POST)
	public BoardVO readBoardAjax(@RequestBody BoardVO vo) {
		
		System.out.println(vo.toString());
		return vo;
	}*/
	
	
	// 상세
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String readBoard(int no, Model model) {
		BoardVO boardVO = bbsService.selectBoard(no);
		AttachFileVO attachFileVO = bbsService.selectAttachFileByNO(no);
		
		model.addAttribute("BoardVO", boardVO);
		model.addAttribute("attachFileVO", attachFileVO);
		
		return "board/view";
	}
	
	
	
	
	// 쓰기폼
	@Auth
	@RequestMapping(value = "write", method = RequestMethod.GET)
	public String write(@AuthUser UserVo authUser) {

		return "board/write";
	}

	// 글등록
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String registerBoard(BoardVO boardVo, MultipartFile file) throws Exception {
		bbsService.insertBoard(boardVo, file);
		System.out.println(file.getOriginalFilename().toString());
		return "redirect:list";
	}

	// 리스트
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String listBoard(Model model) {
		List<BoardVO> listBoard = bbsService.listBoard();
		System.out.println(listBoard.toString());

		model.addAttribute("listBoard", listBoard);
		return "board/list";
	}

	
	
	// 수정폼
	@RequestMapping(value = "modify", method = RequestMethod.GET)
	public String modifyBoard(int no, Model model) {
		BoardVO boardVO = bbsService.selectBoard(no);
		model.addAttribute("BoardVO", boardVO);
		return "board/modify";
	}

	// 수정
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String modifyBoard(BoardVO boardVO) {
		bbsService.updateBoard(boardVO);
		return "redirect:list";
	}

	// 삭제
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public String modifyBoard(int no) {
		System.out.println(no);
		bbsService.deleteBoard(no);
		return "redirect:list";
	}

	//파일다운로드
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public void downloadFile(int fNO, HttpServletResponse res) throws Exception {
		System.out.println(fNO);
		
		
		AttachFileVO attachFileVO = bbsService.selectAttachFileByFNO(fNO);
		
		String saveName = attachFileVO.getSaveName();
		String orgName = attachFileVO.getOrgName();
		    
		    
		    
		res.setContentType("application/download");
		res.setHeader("Content-disposition", "attachment; filename=\"" + URLEncoder.encode(orgName,"UTF-8") +"\"");
		OutputStream resOut = res.getOutputStream();
		
		FileInputStream fin = new FileInputStream("C:\\upload\\"+saveName);
		FileCopyUtils.copy(fin, resOut);
			
		fin.close();
		    
	}
	
	
	@RequestMapping(value = "list2", method = RequestMethod.GET)
	public String list2() {
		return "board/list2";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "listAjax", method = RequestMethod.POST)
	public List<BoardVO> listAjax(@RequestParam(value = "title")String title) {
		List<BoardVO> listBoard = bbsService.listBoard();
		System.out.println(listBoard.toString());
		return listBoard;
	}
	
}
