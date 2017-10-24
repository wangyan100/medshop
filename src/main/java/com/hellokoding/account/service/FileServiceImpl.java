/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hellokoding.account.service;

import com.hellokoding.account.model.ExcelLine;
import com.hellokoding.account.model.Order;
import com.hellokoding.account.model.OrderDetail;
import com.hellokoding.account.model.Product;
import com.hellokoding.account.repository.MedShopDao;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author yw
 */
@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private MedShopDao medShopDao;

    @Transactional
    public boolean loadFile2Database(InputStream productexcel) {
        boolean loaded = false;
        //InputStream productexcel = null;

        try {
            // WorkbookFactory.create(productexcel); 
            Workbook wb = WorkbookFactory.create(productexcel);;
            Sheet sheet = wb.getSheetAt(0);       // first sheet
            //Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            int rowCount = 0;
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                if (rowCount++ == 0) {
                    //skip first line
                    continue;
                }

                ExcelLine line = new ExcelLine();
                line.setRowCount(rowCount);
                for (int cellCount = 1; cellCount <= row.getLastCellNum(); cellCount++) {

                    Cell cell = row.getCell(cellCount - 1, Row.CREATE_NULL_AS_BLANK);

                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_FORMULA:
                            logger.debug("rowCount : " + rowCount + " cellCount: " + cellCount + " Formula  " + cell.getNumericCellValue());
                            break;

                        case Cell.CELL_TYPE_NUMERIC:
                            if (cellCount == 1) {
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                //it is PZN should be string format
                                logger.debug("rowCount : " + rowCount + " cellCount: " + cellCount + " String " + cell.getStringCellValue());
                                line.setPzn(cell.getStringCellValue().trim());
                            }
                            break;

                        case Cell.CELL_TYPE_STRING:
                            logger.debug("rowCount : " + rowCount + " cellCount: " + cellCount + " STRING " + cell.getStringCellValue());
                            if (cellCount == 1) {
                                line.setPzn(cell.getStringCellValue().trim());
                            } else if (cellCount == 2) {
                                line.setGermanName(cell.getStringCellValue().trim());
                            } else if (cellCount == 3) {
                                line.setChineseName(cell.getStringCellValue().trim());
                            } else if (cellCount == 4) {
                                line.setUnit(cell.getStringCellValue().trim());
                            } else if (cellCount == 5) {
                                line.setPrice(cell.getStringCellValue().trim());
                            }
                            break;

                    }

                }

                // one line is convert to Excelline
                if (line.getPzn() != null) {

                    Product product = medShopDao.getProductByPZN(line.getPzn());
                    if (product != null) {
                        logger.debug(line.getPzn() + "xxx is update !!!");
                        //it is update
                        product.setChineseName(line.getChineseName());
                        product.setGermanName(line.getGermanName());
                        product.setPrice(line.getPrice());
                        product.setUnit(line.getUnit());
                        medShopDao.updateProduct(product);
                    } else {
                        //it is new one
                        product = new Product();
                        product.setPzn(line.getPzn());
                        product.setChineseName(line.getChineseName());
                        product.setGermanName(line.getGermanName());
                        product.setPrice(line.getPrice());
                        product.setUnit(line.getUnit());
                        medShopDao.saveProduct(product);
                    }

                }

            }

            loaded = true;

        } catch (Exception e) {
            loaded = false;
            logger.error(e.getMessage(), e);
        }

        return loaded;
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public XSSFWorkbook createOrderDetail(long orderid) {
        Order order = medShopDao.getOrder(orderid);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Bestellung");
        XSSFCellStyle my_style = workbook.createCellStyle();
        XSSFFont my_font = workbook.createFont();
        my_style.setFont(my_font);

        sheet.autoSizeColumn(0, true);
        sheet.autoSizeColumn(1, true);
        sheet.autoSizeColumn(2, true);
        sheet.autoSizeColumn(3, true);

        //first line
        java.util.List<java.util.ArrayList<String>> lists = new ArrayList<ArrayList<String>>();

        java.util.ArrayList<String> line = new java.util.ArrayList<String>();
        line.add("");
        line.add("D&C Health. BestellungNr.");
        line.add(order.getOrderNumber());

        line.add("An Apotheke:");
        line.add(order.getShopname());

        lists.add(line);

        line = new java.util.ArrayList<String>();
        line.add("");
        line.add("Abholendatum:");
        line.add(order.getPickupDate().toString() + " " + order.getPickupTime());

        lists.add(line);

        line = new java.util.ArrayList<String>();
        line.add("");
        line.add("ReiseleiterName导游名:");
        line.add(order.getTourGuideName());
        lists.add(line);

        line = new java.util.ArrayList<String>();
        line.add("");
        line.add("ReiseleiterID导游号:");
        line.add(order.getTourGuideID());
        lists.add(line);

        line = new java.util.ArrayList<String>();
        line.add("");
        line.add("Name des Kunden游客:");
        line.add(order.getTouristName());
        lists.add(line);
        
        line = new java.util.ArrayList<String>();
        line.add("");
        lists.add(line);

        line = new java.util.ArrayList<String>();
        line.add("");
        line.add("PZN:");
        line.add("German:");
        line.add("Menge:");
        line.add("Chinese:");
        lists.add(line);

        int i = 0;

        for (OrderDetail orderDetail : order.getOrderdetails()) {
            line = new java.util.ArrayList<String>();
            line.add("" + (++i));
            line.add(orderDetail.getPzn());
            line.add(orderDetail.getGermanName());
            line.add("" + orderDetail.getAmount());
            line.add(orderDetail.getChineseName());
            lists.add(line);
        }

        int rowNum = 0;
        for (ArrayList<String> list : lists) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (String field : list) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                }
            }

        }
        sheet.getRow(0).getCell(1).setCellStyle(my_style);
        sheet.getRow(0).getCell(3).setCellStyle(my_style);
        sheet.getRow(1).getCell(1).setCellStyle(my_style);
        sheet.getRow(2).getCell(1).setCellStyle(my_style);
        sheet.getRow(3).getCell(1).setCellStyle(my_style);
        sheet.getRow(4).getCell(1).setCellStyle(my_style);
        sheet.getRow(6).getCell(1).setCellStyle(my_style);
        sheet.getRow(6).getCell(2).setCellStyle(my_style);
        sheet.getRow(6).getCell(3).setCellStyle(my_style);
        sheet.getRow(6).getCell(4).setCellStyle(my_style);
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        return workbook;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
