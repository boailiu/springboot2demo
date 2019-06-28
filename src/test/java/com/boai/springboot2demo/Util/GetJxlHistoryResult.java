package com.boai.springboot2demo.Util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GetJxlHistoryResult {

    private static final Logger logger = LoggerFactory.getLogger(GetJxlHistoryResult.class);

    public static void readGammaExcel() throws IOException, InvalidFormatException, JSONException {
        String filePath = "D:\\jxl.xlsx";
        String newFilePath = "D:\\newJxl.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook(new File(filePath));
        XSSFWorkbook newWorkbook = new XSSFWorkbook();
        XSSFSheet newSheet = newWorkbook.createSheet("jxl");
        XSSFSheet sheet = workbook.getSheet("jxl");
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 1; i < rows; i++) {
            XSSFRow newRow = newSheet.createRow(i);
            XSSFRow row = sheet.getRow(i);
            XSSFCell cell = row.getCell(6);
            XSSFCell urlCell = row.createCell(9);
            parseJxlResult(cell.getStringCellValue(), newRow);
        }
        newWorkbook.write(new FileOutputStream(newFilePath));
        workbook.close();
        newWorkbook.close();
    }

    public static void parseJxlResult(String resultString, XSSFRow newRow) throws JSONException {
        String requestUrl = "https://mi.juxinli.com/api/history_search?" +
                "client_secret=3201c90872e046c688a2caac1ff5cebb" +
                "&web_call=true" +
                "&access_token=4361b16b7c4b4ff1a1cc217005f12875";
        JSONObject result = new JSONObject(resultString);
        JSONObject data = result.optJSONObject("data");
        JSONObject userBasic = data.optJSONObject("user_basic");

        String userIdCard = userBasic.optString("user_idcard");
//        logger.info("userIdCard:" + userIdCard);
        XSSFCell firstCell = newRow.createCell(0);
        firstCell.setCellValue(userIdCard);
        requestUrl = requestUrl + "&id_card=" + userIdCard;

        String userName = userBasic.optString("user_name");
//        logger.info("userName:" + userName);
        XSSFCell secondCell = newRow.createCell(1);
        secondCell.setCellValue(userName);
        requestUrl = requestUrl + "&name=" + userName;

        String userPhone = userBasic.optString("user_phone");
//        logger.info("userPhone:" + userPhone);
        XSSFCell thirdCell = newRow.createCell(2);
        thirdCell.setCellValue(userPhone);
        requestUrl = requestUrl + "&phone=" + userPhone;

        String userGridId = data.optString("user_grid_id");
//        logger.info("userGridId:" + userGridId);
        XSSFCell fourthCell = newRow.createCell(3);
        fourthCell.setCellValue(userGridId);
        requestUrl = requestUrl + "&ugid=" + userGridId;
        logger.info(requestUrl);
    }
}
