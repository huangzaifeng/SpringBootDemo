package com.migu.cn.webapi;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.migu.cn.service.inf.IFileExcelService;
import com.migu.cn.webapi.data.ExcelRequest;

@Controller
@RequestMapping(value = "/cdyuheng", produces = "application/json;charset=UTF-8")
public class FileExcelController extends BaseController {
	@Resource(name="FileExcelServiceImpl")
	private IFileExcelService fileExcelService;
	
	 @ResponseBody  
     @RequestMapping(value = "/exportExcel")
     public String exportExcel(HttpServletRequest req, HttpServletResponse resp,String sessionId) throws Exception{
         ExcelRequest msg = ParseMsg(req, ExcelRequest.class);
         fileExcelService.exportExcel(msg, req, resp);
         return "";
     }
	
	
	
	/**
	* @Title: 读取excel文件入库
	* @Description: TODO
	* @param @param file
	* @param @param req
	* @param @param resp
	* @param @param sessionId
	* @param @return
	* @param @throws Exception  
	* @return String
	* @author le
	* @throws
	*/ 
	@ResponseBody  
    @RequestMapping(value = "/readExcel")
    public String readExcel(@RequestParam(value="excelFile") MultipartFile file, 
            HttpServletRequest req, HttpServletResponse resp,String sessionId) throws Exception{
        ExcelRequest msg = ParseMsg(req, ExcelRequest.class);
        fileExcelService.readExcel(msg, file);
        return "";
    }
}
