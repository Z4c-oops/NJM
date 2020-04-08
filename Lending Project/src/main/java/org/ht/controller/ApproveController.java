package org.ht.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ht.pojo.Approveitem;
import org.ht.pojo.Certifrecord;
import org.ht.pojo.Clapplyfor;
import org.ht.pojo.Creditlimit;
import org.ht.pojo.Employee;
import org.ht.pojo.Userauditor;
import org.ht.pojo.Users;
import org.ht.service.ApproveService;
import org.ht.service.CertifrecordService;
import org.ht.service.ClapplyforService;
import org.ht.service.CreditlimitService;
import org.ht.service.EmployeeService;
import org.ht.service.InformationService;
import org.ht.service.UserauditorService;
import org.ht.service.UsersService;
import org.ht.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Name: ApproveController
 * @Description:Authentication Settings
 */
@Controller
@RequestMapping("approve")
public class ApproveController {
	String str = "WEB-INF/view/";
	@Autowired
	private ApproveService approveService;

	@Autowired
	private UsersService usersService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private CertifrecordService certifrecordService;
	@Autowired
	private UserauditorService userauditorService;
	@Autowired
	private InformationService informationService;
	@Autowired
	private ClapplyforService clapplyforService;
	@Autowired
	private CreditlimitService creditlimitService;
	@RequestMapping("traverseApproves")
	private String traverseApproves(Model model, Approveitem ai,
			@RequestParam(value = "currpage", required = false) String currpage) {

		int pagerow = 5;// 5 row per page
		int currpages = 1;// current page
		int totalpage = 0;// total page
		int totalrow = 0;// total row

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("aiid", ai.getAiid());
		parameters.put("ainame", ai.getAiname());
		parameters.put("aitype", ai.getAitype());
		parameters.put("aistate", ai.getAistate());
		List<Approveitem> approvesall = approveService.queryApproves(parameters);

		totalrow = approvesall.size();// get total row
		if (currpage != null && !"".equals(currpage)) {
			currpages = Integer.parseInt(currpage);
		}
		totalpage = (totalrow + pagerow - 1) / pagerow;

		if (currpages < 1) {
			currpages = 1;
		}
		if (currpages > totalpage) {
			currpages = totalpage;
		}
		
		Integer candp = (currpages - 1) * pagerow;
		parameters.put("pandc", pagerow);
		parameters.put("candp", candp);
		List<Approveitem> approves = approveService.queryApproves(parameters);
		// for (Approveitem approveitem : approves) {
		// System.out.println(approveitem.getApproveitemInfo());
		// }

		model.addAttribute("approveitems", approves);
		model.addAttribute("totalrow", totalrow);
		model.addAttribute("currpages", currpages);
		model.addAttribute("totalpage", totalpage);

		return str + "approvelist";
	}

	@RequestMapping("toaddApprove")
	private String toaddApprove() {

		return str + "approveadd";
	}

	@RequestMapping("addApprove")
	private ModelAndView addApprove(Approveitem ai) {
		// System.out.println("getComeInabandonApprove
		// Approveitem=="+ai.getApproveitemInfo());
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ainame", ai.getAiname());
		parameters.put("aitype", ai.getAitype());
		parameters.put("aistate", "1");
		approveService.addApproves(parameters);
		return new ModelAndView("redirect:traverseApproves.do");
	}

	@RequestMapping("toupdateApprove")
	private String toupdateApprove(Approveitem ai, Model model) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("aiid", ai.getAiid());
		parameters.put("ainame", ai.getAiname());
		parameters.put("aitype", ai.getAitype());
		parameters.put("aistate", ai.getAistate());
		List<Approveitem> approvesall = approveService.queryApproves(parameters);
		Approveitem approve = null;
		if (!approvesall.isEmpty()) {
			approve = approvesall.get(0);
			model.addAttribute("approve", approve);
		}

		return str + "approveupdate";
	}

	@RequestMapping("updateApprove")
	private ModelAndView updateApprove(Approveitem ai) {

		// System.out.println("getComeInabandonApprove aiid=="+ai.getAiid()+"
		// aistate=="+ai.getAistate());
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("aiid", ai.getAiid());
		parameters.put("ainame", ai.getAiname());
		parameters.put("aitype", ai.getAitype());
		parameters.put("aistate", ai.getAistate());
		approveService.updateApproves(parameters);
		return new ModelAndView("redirect:traverseApproves.do");
	}
	//Authentication Settings head
//==============================================================================
	
//==============================================================================
	//New user authentication information head
	@RequestMapping("newuserInfoList")
	private String newuserInfoList(Model model){
		//Query all new users
		List<Users> users = usersService.userList();
		
		List<Employee> employees = employeeService.findlist();
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		//Query out all user reviewers
		List<Userauditor> userauditors = userauditorService.queryUseraubitor(parameters);
	
		//System.out.println("userauditors.size==========================="+userauditors.size());
		//The audit records of all users are queried
		List<Certifrecord> certifrecords = certifrecordService.queryCertifrecord(parameters);
		List<Users> us = null;
		if(userauditors!=null){
			for (Userauditor userauditor : userauditors) {
				for(Users u : users){
					if(userauditor.getUserid()==u.getUid()){
						users.remove(u);
						break;
					}
					
				}
			}
			
		}
		//Query out the integral of unassigned auditor 1 and the number of items to be audited
		
		List<Certifrecord> cr = null;
		
		if(certifrecords!=null){
			cr =  new ArrayList<Certifrecord>();
			
			for (Users u : users) {
				Certifrecord cerrecord = new Certifrecord();
				int integral = 0;
				int ispass = 0;
				for (Certifrecord c : certifrecords) {
						if(u.getUid()==c.getCruserid()){
							if(c.getCrintegral()!=null){
								integral+=c.getCrintegral();
							}else{
								integral+=0;
							}
							
							if(c.getCrispass().equals("1")){
								ispass+=1;
							}
						}
						
					}
				//System.out.println("uname="+u.getUnickname()+"  integral: "+integral+" ispass: "+ispass);
				cerrecord.setCruserid(u.getUid());
				cerrecord.setCrusername(u.getUnickname());
				cerrecord.setCrintegral(integral);
				cerrecord.setCheckpend(ispass);
				cr.add(cerrecord);
				
				}

			}

		model.addAttribute("cr",cr);
		model.addAttribute("users",users);
		model.addAttribute("employees",employees);
		return str+"anewuserinfolist";
	}
		

		

	
	
	@RequestMapping("affirmCrauditor")
	@ResponseBody
	public String affirmCrauditor(Userauditor ua){
		String code="200";
		//System.out.println("getComeInaffirmCrauditor  auditorid=="+ua.getUauditorid()+"  auditor=="+ua.getUauditor()+" userid=="+ua.getUserid()+"  username=="+ua.getUsername());
		Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("userid",ua.getUserid());
			parameters.put("username",ua.getUsername());
			parameters.put("uauditorid",ua.getUauditorid());
			parameters.put("uauditor",ua.getUauditor());
			int addcode = userauditorService.addUserauditor(parameters);
			if(addcode<=0){
				code="400";
			}else{
				Map<String, Object> parameters1 = new HashMap<String, Object>();
				parameters1.put("crauditor",ua.getUauditor());
				parameters1.put("cruserid",ua.getUserid());
				int updatecode = certifrecordService.updateCertifrecord(parameters1);
			}

		
		
		return code;
	}
	//New user authentication information end
//==============================================================================
	
//==============================================================================
	//information authentication
	@RequestMapping("basicInfoApprove")
	private String basicInfoaudit(Model model,@RequestParam(value = "currpage", required = false) String currpage){
		int pagerow = 10;// 5 row per page
		int currpages = 1;// current page
		int totalpage = 0;// total page
		int totalrow = 0;// total row
		if (currpage != null && !"".equals(currpage)) {
			currpages = Integer.parseInt(currpage);
		}
		List<Users> user1 = usersService.userList();
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<Userauditor> userauditors = userauditorService.queryUseraubitor(parameters);
		totalrow = user1.size();// get total row
		
		totalpage = (totalrow + pagerow - 1) / pagerow;

		if (currpages < 1) {
			currpages = 1;
		}
		
			if (currpages > totalpage) {
				if(totalpage!=0){
				currpages = totalpage;
				}else{
					currpages =1;
				}
			}
		Integer candp = (currpages - 1) * pagerow;
		Integer pandc = pagerow;
		parameters.put("candp",candp);
		parameters.put("pandc",pandc);
		List<Users> users = usersService.queryUser(parameters);
		model.addAttribute("totalrow", totalrow);
		model.addAttribute("currpages", currpages);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("users", users);
		model.addAttribute("ua", userauditors);
		return str+"basicinfoList";
	}
	
	@RequestMapping("infoAuditByuser")
	private String infoAuditByuser(Model model,Certifrecord cr){
		//System.out.println("userid==="+cr.getCruserid());
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<Userauditor> userauditors = userauditorService.queryUseraubitor(parameters);
		parameters.put("cruserid",cr.getCruserid());
		parameters.put("id",cr.getCruserid());
		parameters.put("craiid",cr.getCraiid());
		List<Certifrecord> certifrecords = certifrecordService.queryCertifrecord(parameters);
		Users user =  informationService.query(parameters);
		
		List<Approveitem> approvesall = approveService.queryApproves(parameters);
		//System.out.println("user==="+user.getUname()+"  approvesall==="+approvesall.size()+"  certifrecords==="+certifrecords.size());
		model.addAttribute("certrecod",certifrecords);
		model.addAttribute("user",user);
		model.addAttribute("approve",approvesall);
		model.addAttribute("code",approvesall.size());
		model.addAttribute("useraud",userauditors);
		model.addAttribute("craiid",cr.getCraiid());
		return str+"basicuserapprove";
	}
	@RequestMapping("updateInfoAudit")
	@ResponseBody
	private String updateInfoAudit(Certifrecord cr){
		String code="200";
		//"cruserid":userid,"craiid":crtype,"crviewpoint":viewpoint,"crintegral":integral,"crispass":ispass,"crauditor":auditor
		//System.out.println("cruserid=="+cr.getCruserid()+"   craiid=="+cr.getCraiid()+"   crviewpoint=="+cr.getCrviewpoint()+"   crintegral=="+cr.getCrintegral()+"   crispass=="+cr.getCrispass()+"   crauditor=="+cr.getCrauditor());
		Map<String, Object> parameters = new HashMap<String, Object>();
		Date date = new Date();
		parameters.put("cruserid",cr.getCruserid());
		parameters.put("craiid",cr.getCraiid());
		parameters.put("crviewpoint",cr.getCrviewpoint());
		parameters.put("crintegral",cr.getCrintegral());
		parameters.put("crispass",cr.getCrispass());
		parameters.put("crauditor",cr.getCrauditor());
		parameters.put("crdate",date);
		int updateCode = certifrecordService.updateCertifrecord(parameters);
		if(updateCode <=0){
			code = "400";
		}
		return code;
	}
	@RequestMapping("approveStatistics")
	private String approveStatistics(Model model, Certifrecord cr,@RequestParam(value = "currpage", required = false) String currpage){
		int pagerow = 5;// 5 row per page
		int currpages = 1;// current page
		int totalpage = 0;// total page
		int totalrow = 0;// total row
		if (currpage != null && !"".equals(currpage)) {
			currpages = Integer.parseInt(currpage);
		}
		List<Users> users = usersService.userList();
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<Approveitem> approvesall = approveService.queryApproves(parameters);
		
		parameters.put("crusername",cr.getCrusername());
		parameters.put("craiid",cr.getCraiid());
		List<Certifrecord> crsize = certifrecordService.queryCertifrecord(parameters);
		totalrow = crsize.size();// get total row
		
		totalpage = (totalrow + pagerow - 1) / pagerow;

		if (currpages < 1) {
			currpages = 1;
		}
		
			if (currpages > totalpage) {
				if(totalpage!=0){
				currpages = totalpage;
				}else{
					currpages =1;
				}
			}
			
		
		
		
		Integer candp = (currpages - 1) * pagerow;
		parameters.put("pandc", pagerow);
		parameters.put("candp", candp);
		
		List<Certifrecord> certifrecords = certifrecordService.queryCertifrecord(parameters);
		model.addAttribute("users",users);
		model.addAttribute("approves",approvesall);
		model.addAttribute("cr",certifrecords);
		model.addAttribute("totalrow", totalrow);
		model.addAttribute("currpages", currpages);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("username",cr.getCrusername());
		model.addAttribute("apid", cr.getCraiid());
		//System.out.println("userssize=="+users.size()+"  certifrecordssize=="+certifrecords.size()+"  approvesallsize=="+approvesall.size());
		return str+"approvestatistics";
	}
	//Authentication info end
//============================================================================================================================================	
	
//============================================================================================================================================	
	//credit limit request
	@RequestMapping("limitApplyfor")
	private String limitApplyfor(Model model,
			@RequestParam(value = "currpage", required = false) String currpage,
			@RequestParam(value = "mindate", required = false) String mindate,
			@RequestParam(value = "clpuname", required = false) String clpuname,
			@RequestParam(value = "maxdate", required = false) String maxdate,
			@RequestParam(value = "clpstate", required = false) String clpstate){
		int pagerow = 10;// 10 row per page
		int currpages = 1;// current page
		int totalpage = 0;// total page
		int totalrow = 0;// total row
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		   Date date1 = null;
		   Date date2 = null;
		if (currpage != null && !"".equals(currpage)) {
			currpages = Integer.parseInt(currpage);
		}
		try {
			if(mindate != null && !mindate.equals("")){
				 date1 = format.parse(mindate);
			}
			if(maxdate != null && !maxdate.equals("")){
				 date2 = format.parse(maxdate);
			}
		   } catch (ParseException e) {
		    e.printStackTrace();
		   }
		
		Map<String, Object> pm = new HashMap<String, Object>();
		List<Certifrecord> certifrecords = certifrecordService.queryCertifrecord(pm);
		pm.put("clpuname",clpuname);
		pm.put("maxdate",date2);
		pm.put("mindate",date1);
		pm.put("clpstate",clpstate);
		List<Clapplyfor> sizes =  clapplyforService.queryClapplyfors(pm);
		
		totalrow = sizes.size();// get total row
		
		totalpage = (totalrow + pagerow - 1) / pagerow;

		if (currpages < 1) {
			currpages = 1;
		}
		
			if (currpages > totalpage) {
				if(totalpage!=0){
				currpages = totalpage;
				}else{
					currpages =1;
				}
			}
			
		Integer candp = (currpages - 1) * pagerow;
		pm.put("pandc", pagerow);
		pm.put("candp", candp);
		List<Clapplyfor> clapplyfors =  clapplyforService.queryClapplyfors(pm);
		model.addAttribute("cr",certifrecords);
		model.addAttribute("cps", clapplyfors);
		model.addAttribute("totalrow", totalrow);
		model.addAttribute("currpages", currpages);
		model.addAttribute("totalpage", totalpage);
		model.addAttribute("maxdate", maxdate);
		model.addAttribute("clpuname", clpuname);
		model.addAttribute("mindate", mindate);
		model.addAttribute("clpstate", clpstate);
		//System.out.println("getComeIn========================================================clapplyfors=="+clapplyfors.size());
		
		return str+"limitapplyforlist";
		
	}
	@RequestMapping("applyforApprove")
	@ResponseBody
	private String applyforApprove(Clapplyfor ca){
		String code="200";
		int updateCode =0;
		updateCode = clapplyforService.updateClapplyforState(BeanUtils.toMap(ca));
		//System.out.println("clpid=="+ca.getClpid()+"   clpubcid=="+ca.getClpubcid()+"   clpporiginal=="+ca.getClpporiginal()+"   clpf=="+ca.getClpf()+"   clpstate=="+ca.getClpstate());
		if(ca.getClpstate().equals("1")){//Review approved, modify the status of the application form, increase the credit limit
			
			Creditlimit creditlimit = new Creditlimit();
			creditlimit.setCllimit(ca.getClpporiginal()+ca.getClpf());
			creditlimit.setCrbankcard(ca.getClpubcid());
			int code1 = creditlimitService.updateCreditlimit(BeanUtils.toMap(creditlimit));
		}
		if(updateCode <=0){
			code = "400";
		}
		return code;
	}

}
