package com.boai.springboot2demo.Util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    private static void readExcel(String filePath) {
        String sqlTemplate = "INSERT INTO `gamma_rc`.`def_pre_loan_rule` (`rule_id`, `rule_type`, `rule_code`, `rule_show_name`, `rule_name`, `other_id`, `condition_description`, `cover_rule_cert_mobile`, `cover_rule_except_cert_mobile`, `cover_date_group`, `is_invisible`, `risk_level`, `score_coefficient`, `risk_score`, `rule_group`, `rule_sort`, `rule_source`, `risk_type`, `enabled`) " +
                "VALUES ('%s', NULL, '%s', '%s', '%s', '%s', NULL, NULL, NULL, NULL, NULL, '%s', '0', '%s', '%s', '%s', '%s', '%s', '%s');";
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new File(filePath));
            //获取sheet页
            XSSFSheet firstSheet = workbook.getSheetAt(1);
            //获取行数
            int rows = firstSheet.getPhysicalNumberOfRows();
            String ruleId = null, ruleCode = null, ruleShowName = null, ruleName = null,
                    description = null, riskLevel = null, riskScore = null, ruleGroup = null,
                    ruleSort = null, ruleSource = null, riskType = null, enabled = null;
            for (int i = 1; i < rows; i++) {
                XSSFRow row = firstSheet.getRow(i);
                for (int j = 0; j <= 11; j++) {
                    XSSFCell cell = row.getCell(j);
                    String value;
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            value = String.valueOf(cell.getNumericCellValue());
                            switch (j) {
                                case 0:
                                    ruleId = value;
                                    break;
                                case 5:
                                    riskLevel = value;
                                    break;
                                case 6:
                                    riskScore = value;
                                    break;
                                case 8:
                                    ruleSort = value;
                                    break;
                                case 10:
                                    riskType = value;
                                    break;
                                case 11:
                                    enabled = value;
                                    break;
                            }
                            break;
                        case STRING:
                            value = cell.getStringCellValue();
                            switch (j) {
                                case 1:
                                    ruleCode = value;
                                    break;
                                case 2:
                                    ruleShowName = value;
                                    break;
                                case 3:
                                    ruleName = value;
                                    break;
                                case 4:
                                    description = value;
                                    break;
                                case 7:
                                    ruleGroup = value;
                                    break;
                                case 9:
                                    ruleSource = value;
                                    break;
                            }
                            break;
                    }
//                    logger.info(String.format("第[%d]行,第[%d]单元格内容:[%s]", i, j,
//                            value));
                }
                String sql = String.format(sqlTemplate, ruleId, ruleCode, ruleShowName, ruleName, description, riskLevel, riskScore,
                        ruleGroup, ruleSort, ruleSource, riskType, enabled);
                logger.info(sql);
            }
        } catch (IOException | InvalidFormatException e) {
            logger.error("读取excel文件出错", e);
        }
    }

    public static void main(String[] args) {
        readExcel("D:\\rule.xlsx");
    }
}
