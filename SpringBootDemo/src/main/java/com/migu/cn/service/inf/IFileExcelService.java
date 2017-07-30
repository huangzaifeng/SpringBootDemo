package com.migu.cn.service.inf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.migu.cn.webapi.data.ExcelRequest;

public interface IFileExcelService {
	 void readExcel(ExcelRequest msg, MultipartFile file)  throws Exception;

	void exportExcel(ExcelRequest msg, HttpServletRequest req, HttpServletResponse resp)throws Exception;
}
