/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hellokoding.account.service;

import java.io.InputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 *
 * @author yw
 */
public interface FileService {
    
    boolean loadFile2Database(InputStream inputexcel)throws Exception;
    
    boolean loadTourguideFile2Database(InputStream inputexcel)throws Exception;
    
    XSSFWorkbook createOrderDetail(long orderid);
    
}
