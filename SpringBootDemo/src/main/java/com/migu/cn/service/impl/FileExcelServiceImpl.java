package com.migu.cn.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.migu.cn.dao.entity.ProjectCriteria;
import com.migu.cn.dao.entity.ProjectDO;
import com.migu.cn.dao.inf.ProjectDOMapper;
import com.migu.cn.service.inf.IFileExcelService;
import com.migu.cn.utils.ExcelUtil;
import com.migu.cn.utils.ServiceException;
import com.migu.cn.webapi.data.ExcelRequest;

@Service("FileExcelServiceImpl")
public class FileExcelServiceImpl implements IFileExcelService {
	@Resource(name="ProjectDOMapper")
	private ProjectDOMapper projectDao;
	
	private static int dataStartRow = 1;
	private int totalRows; // sheet中总行数
	private static int totalCells; // 每一行总单元格数

	@Override
	public void readExcel(ExcelRequest msg, MultipartFile file) throws Exception {
		List<ProjectCriteria> readExcelToObj=(List<ProjectCriteria>) readExcelToObj(file);
		for(ProjectCriteria projectCriteria:readExcelToObj){
			projectDao.insertSelective(projectCriteria);
		}

	}
	
	@Override
	public void exportExcel(ExcelRequest msg, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<String> titles =getExcelTitles();
		 XSSFWorkbook workbook = new XSSFWorkbook();
	     Map<String, Object> model = new HashMap<String, Object>();
	     model.put("titles", titles);
	     List<ProjectDO> varList=new ArrayList<ProjectDO>();
	     ProjectDO projectCriteria=projectDao.selectByPrimaryKey(57);
	     varList.add(projectCriteria);
	     buildExcelDocument(model,workbook,req,resp);
		
	}

	private List<?> readExcelToObj(MultipartFile file) throws Exception {
		if (file == null || ExcelUtil.EMPTY.equals(file.getOriginalFilename().trim())) {
			return null;
		} else {
			String postfix = ExcelUtil.getPostfix(file.getOriginalFilename());
			if (!ExcelUtil.EMPTY.equals(postfix)) {
				if (ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(file);
				} else if (ExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsx(file);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	private List<ProjectCriteria> readXls(MultipartFile file) throws Exception {
		List<ProjectCriteria> list = new ArrayList<ProjectCriteria>();
		// IO流读取文件
		InputStream input = null;
		HSSFWorkbook wb = null;
		try {
			input = file.getInputStream();
			// 创建文档
			wb = new HSSFWorkbook(input);
			HSSFSheet hssfSheet = wb.getSheetAt(0);
			totalRows = hssfSheet.getLastRowNum();

			// 读取Row,从第二行开始
			for (int rowNum = dataStartRow; rowNum <= totalRows; rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				ProjectCriteria fun = new ProjectCriteria();
				if (hssfRow != null) {
					if (hssfRow.getLastCellNum() > 0) {
						totalCells = hssfRow.getLastCellNum();
					} else {
						totalCells = totalRows;
					}
					// 读取列，从第一列开始
					for (short c = 0; c < totalCells; c++) {
						HSSFCell cell = hssfRow.getCell(c);
						String trim = ExcelUtil.getHValue(cell).trim();

						switch (c) {
						case 0:
							fun.setPid(Integer.valueOf(trim));
							break;
						case 1:
							fun.setName(trim);
							break;
						default:
							break;
						}

					}
					list.add(fun);
				}
			}
			return list;
		} catch (IOException e) {
			throw new ServiceException("Excel文件格式有误！");
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				throw new ServiceException("Excel文件格式有误！");
			}
		}
	}

	private List<ProjectCriteria> readXlsx(MultipartFile file) throws Exception {
		List<ProjectCriteria> list = new ArrayList<ProjectCriteria>();
		// IO流读取文件
		InputStream input = null;
		XSSFWorkbook wb = null;
		ArrayList<String> rowList = null;
		try {
			input = file.getInputStream();
			// 创建文档
			wb = new XSSFWorkbook(input);
			// 读取sheet(页)

			XSSFSheet xssfSheet = wb.getSheetAt(0);

			totalRows = xssfSheet.getLastRowNum();
			// 读取Row,从第二行开始
			for (int rowNum = dataStartRow; rowNum <= totalRows; rowNum++) {
				XSSFRow hssfRow = xssfSheet.getRow(rowNum);
				ProjectCriteria fun = new ProjectCriteria();
				if (hssfRow != null) {
					if (hssfRow.getLastCellNum() > 0) {
						totalCells = hssfRow.getLastCellNum();
					} else {
						totalCells = totalRows;
					}
					// 读取列，从第一列开始
					for (short c = 0; c < totalCells; c++) {
						XSSFCell cell = hssfRow.getCell(c);
						String trim = ExcelUtil.getXValue(cell).trim();
						switch (c) {
						case 0:
							fun.setPid(Integer.valueOf(trim));
							break;
						case 1:
							fun.setName(trim);
							break;
						default:
							break;
						}
					}
					list.add(fun);
				}
			}

			return list;
		} catch (IOException e) {
			throw new ServiceException("Excel文件格式有误！");
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				throw new ServiceException("Excel文件格式有误！");
			}
		}
	}

	
	
	 private List<String> getExcelTitles() {
	        List<String> titles = new ArrayList<String>();
	        titles.add("id");
	        titles.add("项目名称");
	        return titles;
	    }
	 
	 private void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
	            HttpServletResponse response) throws Exception {
	        XSSFSheet sheet;
	        XSSFCell cell;
	        response.reset();
	        response.setContentType("application/octet-stream;charset=utf-8");
	        response.setCharacterEncoding("UTF-8");
	        response.setHeader("Content-Disposition", "attachment;filename=pcExcel.xlsx");
	        sheet = workbook.createSheet("sheet1");

	        List<String> titles = (List<String>) model.get("titles");
	        int len = titles.size();
	        XSSFCellStyle headerStyle = workbook.createCellStyle(); // 标题样式
	        headerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
	        headerStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
	        XSSFFont headerFont = workbook.createFont(); // 标题字体
	        headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	        headerFont.setFontHeightInPoints((short) 11);
	        headerStyle.setFont(headerFont);
	        headerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
	        headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
	        headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
	        headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
	        headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
	        short width = 20, height = 25 * 20;
	        sheet.setDefaultColumnWidth(width);

	        XSSFRow row = sheet.createRow(0);
	        for (int i = 0; i < len; i++) { // 设置标题
	            String title = titles.get(i);
	            cell = row.createCell(i);
	            cell.setCellValue(title);
	            cell.setCellStyle(headerStyle);
	        }
	        sheet.getRow(0).setHeight(height);

	        XSSFCellStyle contentStyle = workbook.createCellStyle(); // 内容样式
	        contentStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
	        contentStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
	        contentStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
	        contentStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
	        contentStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
	        List<ProjectDO> varList = (List<ProjectDO>) model.get("varList");

	        int varCount = varList.size();
	        for (int i = 0; i < varCount; i++) {
	            XSSFRow rowH = sheet.createRow(i + 1);
	            ProjectDO fb = varList.get(i);
	            for (int j = 0; j < len; j++) {
	                cell = rowH.createCell(j);
	                switch (j) {
	                case 0:
	                    cell.setCellStyle(contentStyle);
	                    cell.setCellValue(fb.getPid());
	                    break;
	                case 1:
	                    cell.setCellStyle(contentStyle);
	                    cell.setCellValue(fb.getName());
	                    break;
	                default:
	                    break;
	                }
	            }
	        }
	        

	        ServletOutputStream os = response.getOutputStream();
	        workbook.write(os);
	        os.flush();
	        os.close();
	    }

}
