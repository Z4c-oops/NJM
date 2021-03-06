package org.ht.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ht.pojo.Notice;
import org.ht.service.NoticeService;
import org.ht.service.UsersService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Website message notification control layer
 */
@Controller
@RequestMapping("notice")
public class NoticeController {
	@Resource
	private NoticeService noticeService;
	@Resource
	private UsersService uService;

	// @RequestMapping("tohoutai")
	// public String tohoutai(){
	//
	// return "WEB-INF/view/indexs";
	// }

	// Add a page to the background
	@RequestMapping("toadd")
	public String tohoutai() {

		return "WEB-INF/view/noticeadd";

	}

	// add homepage picture
	@RequestMapping("addtupian")
	public String addtupian() {

		return "WEB-INF/view/noticeaddtupian";

	}
//sss
	//Query home page picture
	@RequestMapping("toaddlisttupian")
	public String toaddlisttupian(Model model, String ids) {
		model.addAttribute("list", noticeService.noticelist(ids));
		return "WEB-INF/view/noticeaddlisttupian";
	}

	// query notice
	@RequestMapping("toaddlist")
	public String toaddlist() {

		return "WEB-INF/view/noticeaddlist";
	}
	


	@RequestMapping("nottop")
	public String nottop(HttpServletRequest request,@RequestParam(value = "ids") Integer ids,@RequestParam(value="isd")Integer isd) {
		noticeService.noticshiji(ids);
		jiazai(request);

		if ( 6==isd) {
			return "redirect:toaddlisttupian.do?ids=6";
		} 
		
	
		return "redirect:notlists.do?ids="+isd;
	}
		
	// front dest(list page)
	@RequestMapping("notlist")
	public String notlist(Model model,
			@RequestParam(value = "ids", required = false) String ids) {
		model.addAttribute("list", noticeService.noticelist(ids));
		model.addAttribute("idss", ids + "");
		if ("3".equals(ids)) {
			return "informgltd";
		}else if ("4".equals(ids)) {
			return "informhzhb";
		}else if ("5".equals(ids)) {
			return "informtdfc";
		}
		return "inform";
	}

	// back side query
	@RequestMapping("notlists")
	// public String notlists(Model model) {
	public String notlists(HttpServletRequest request,Model model, String ids) {
		List list =noticeService.noticelist(ids);
		model.addAttribute("list", list);
		jiazai(request);
		return "WEB-INF/view/noticeaddlist";
	}

	// Enquiry single details
	@RequestMapping("notget")
	public String notget(Model model, @RequestParam(value = "ids") Integer ids) {
		model.addAttribute("notice", noticeService.noticeget(ids));
		return "informsel";
	}

	@RequestMapping("notdel")
	public String notdel(HttpServletRequest request,@RequestParam(value = "ids") Integer ids,@RequestParam(value="isd")Integer isd) {
		noticeService.noticedel(ids);
		jiazai(request);

		if ( 6==isd) {
			return "redirect:toaddlisttupian.do?ids=6";
		} 
		
	
		return "redirect:notlists.do?ids="+isd;
	}

	@RequestMapping("noticeupds")
	public String noticeupds(@RequestParam(value = "ids") Integer ids,
			Model model) {
		Notice notice = noticeService.noticeget(ids);
		model.addAttribute("not", notice);
		return "WEB-INF/view/noticeupdate";
	}

	// Add in the background
	@RequestMapping("notadd")
	public String notadd(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "ufile", required = false) MultipartFile file,
			Model model, Notice notice) {
		String path = request.getSession().getServletContext()
				.getRealPath("file");// Get the upload path
		String fileName = file.getOriginalFilename();// Get the uploaded file name
		File targetFile = new File(path, fileName);//  Create a file object uploaded to the server
		try {
			file.transferTo(targetFile);// file transfer 
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String imgUrl = request.getContextPath() + "/file/" + fileName;

		notice.setNoticepicture(imgUrl);
		noticeService.noticeadd(notice);

		jiazai(request);
//Sos
		if ("6".equals(notice.getNoticetype())) {
			return "redirect:toaddlisttupian.do?ids=" + notice.getNoticetype();
		}
		return "redirect:notlists.do?ids=" + notice.getNoticetype();
	}

	@RequestMapping("notupd")
	public String notupd(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "ufile", required = true) MultipartFile file,
			Model model, Notice notice) {
		
		if (file.getSize() != 0) {
			String path = request.getSession().getServletContext()
					.getRealPath("file");// Get the upload path
			String fileName = file.getOriginalFilename();// Get the uploaded file name
			File targetFile = new File(path, fileName);// Create a file object uploaded to the server
			try {
				file.transferTo(targetFile);// file transfer
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String imgUrl = request.getContextPath() + "/file/" + fileName;
			notice.setNoticepicture(imgUrl);
			
		}
		noticeService.noticeupds(notice);
		jiazai(request);
		
		if ("6".equals(notice.getNoticetype())) {
			return "redirect:toaddlisttupian.do?ids=" + notice.getNoticetype();
		}
		return "redirect:notlists.do?ids=" + notice.getNoticetype();
	}

	// modify&update
	@RequestMapping("sgetno")
	public String getno(Model model, Integer ids) {
//		notgets(model, ids);
		Notice notice = noticeService.noticeget(ids);
		model.addAttribute("nots", notice);
		return "WEB-INF/view/noticeupdate";
	}

	@RequestMapping("noticetop5")
	public String noticetop5(HttpServletRequest request) {
		jiazai(request);
		return "redirect:/invest/recommendShow.do";
	}

	// 重新加载top5
	private void jiazai(HttpServletRequest request) {
		List<Notice> list = noticeService.noticetop5();
		List<Notice> lists = noticeService.noticetop5meiti();
		List<Notice> listss = noticeService.noticetop5sy();
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>top5");
		ServletContext context = request.getSession().getServletContext();
		context.setAttribute("listss", list);
		context.setAttribute("meiti", lists);
		context.setAttribute("sy", listss);
		context.setAttribute("size", uService.userList().size());
	}

	private void notgets(Model model, Integer ids) {
		Notice notice = noticeService.noticeget(ids);
		model.addAttribute("nots", notice);
	}

}
