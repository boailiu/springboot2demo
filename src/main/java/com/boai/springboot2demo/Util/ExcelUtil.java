package com.boai.springboot2demo.Util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    private static Map<String, Object> ruleGroupMap = new HashMap<String, Object>() {
        {
            put("借款人信息异常", 1);
            put("借款人身份异常", 2);
            put("借款人命中高风险名单", 3);
            put("借款人命中关注名单", 4);
            put("借款人多头借贷及负债", 5);
            put("借款人行为异常", 6);
            put("借款人关系异常", 7);
            put("借款人场景异常", 8);
            put("贷款车辆信息异常", 9);
            put("贷款车辆命中高风险名单", 10);
            put("贷款车辆触及风险关系网", 11);
            put("贷款车辆场景异常", 12);
        }
    };

    private static Map<String, Object> ruleGroupMap2 = new HashMap<String, Object>() {
        {
            put("身份风险-借款人", 1);
            put("行为风险-借款人", 2);
            put("关系风险-借款人", 3);
            put("身份风险-车辆", 4);
            put("行为风险-车辆", 5);
            put("关系风险-车辆", 6);
            put("身份风险-联系人", 7);
            put("行为风险-联系人", 8);
            put("关系风险-联系人", 9);
        }
    };

    private static Map<String, Object> ruleSourceMap = new HashMap<String, Object>() {
        {
            put("CTH", "keen");
            put("JA", "JA");
            put("JXL", "juxinli");
            put("TD", "tongdun");
            put("TX", "tenxun");
            put("CDD", "keen");
        }
    };

    /**
     * excel 列需要按照固定顺序
     * 1.rule_id
     * 2.rule_code
     * 3.rule_showName
     * 4.rule_name
     * 5.other_id
     * 6.condition_description
     * 7.cover_rule_cert_mobile
     * 8.cover_rule_except_cert_mobile
     * 9.cover_date_group
     * 10.is_invisible
     * 11.risk_level
     * 12.risk_score
     * 13.rule_group
     * 14.rule_sort
     * 15.rule_source
     * 16.risk_type
     * 17.enabled
     */
    private static void readGammaRuleExcelAndSaveSql(String filePath, String destFilePath) {
        File file = new File(destFilePath);
        if (!file.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
        }

        FileOutputStream out = null;
        try {
/*            file.deleteOnExit();
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();*/
            out = new FileOutputStream(file);
        } catch (IOException e) {
            logger.error("创建目标文件失败", e);
        }


        String sqlTemplate = "INSERT INTO `gamma_rc`.`def_pre_loan_rule` (`rule_id`, `rule_type`, `rule_code`, " +
                "`rule_show_name`, `rule_name`, `other_id`, `condition_description`, `cover_rule_cert_mobile`, " +
                "`cover_rule_except_cert_mobile`, `cover_date_group`, `is_invisible`, `risk_level`, `score_coefficient`, " +
                "`risk_score`, `rule_group`, `rule_sort`, `rule_source`, `risk_type`, `enabled`,`rule_group2`) " +
                "VALUES ('%s', NULL, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '0', '%s', '%s', '%s', " +
                "'%s', '%s', '%s','%s');";
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new File(filePath));
            //获取sheet页
            XSSFSheet firstSheet = workbook.getSheet("Sheet1");
            //获取行数
            int rows = firstSheet.getPhysicalNumberOfRows();

            for (int i = 1; i < rows; i++) {
                XSSFRow row = firstSheet.getRow(i);
                String ruleId = null, ruleCode = null, ruleShowName = null, ruleName = null,
                        otherId = null /*对应的同盾id */, description = null,
                        coverRuleCertMobile = null /*身份证号覆盖手机号 */, coverRuleExceptCertMobile = null /*除身份证手机号的其他覆盖 */,
                        coverDateGroup = null, isInvisible = null,
                        riskLevel = null, riskScore = null, ruleGroup = null,
                        ruleSort = null, ruleSource = null, riskType = null, enabled = null,
                        ruleGroup2 = null;
                for (int j = 0; j <= 17; j++) {
                    XSSFCell cell = row.getCell(j);
                    if (cell == null) continue;
                    String stringValue;
                    double numericCellValue;
                    switch (cell.getCellType()) {
                        case NUMERIC:
//                            value = String.valueOf(cell.getNumericCellValue());
                            numericCellValue = cell.getNumericCellValue();
                            switch (j) {
                                case 0:
                                    ruleId = String.valueOf((int) numericCellValue);
                                    break;
                                case 4:
                                    otherId = String.valueOf((int) numericCellValue);
                                    break;
                                case 6:
                                    coverRuleCertMobile = String.valueOf((int) numericCellValue);
                                    break;
                                case 7:
                                    coverRuleExceptCertMobile = String.valueOf((int) numericCellValue);
                                    break;
                                case 8:
                                    coverDateGroup = String.valueOf((int) numericCellValue);
                                    break;
                                case 9:
                                    isInvisible = String.valueOf((int) numericCellValue);
                                    break;
                                case 11:
                                    riskScore = String.valueOf(numericCellValue);
                                    break;
                                case 13:
                                    ruleSort = String.valueOf((int) numericCellValue);
                                    break;
                                case 15:
                                    riskType = String.valueOf((int) numericCellValue);
                                    break;
                                case 16:
                                    enabled = String.valueOf((int) numericCellValue);
                                    break;
                            }
                            break;
                        case STRING:
                            stringValue = cell.getStringCellValue();
                            switch (j) {
                                case 1:
                                    ruleCode = stringValue;
                                    break;
                                case 2:
                                    ruleShowName = stringValue;
                                    break;
                                case 3:
                                    ruleName = stringValue;
                                    break;
                                case 5:
                                    description = stringValue;
                                    //数据库字段限制
                                    if (description.length() >= 190) {
                                        description = description.substring(0, 190);
                                    }
                                    break;
                                case 10:
                                    switch (stringValue) {
/*                                        case "1高风险":
                                            riskLevel = "3";
                                            break;
                                        case "2中风险":
                                            riskLevel = "2";
                                            break;
                                        case "3低风险":
                                            riskLevel = "1";
                                            break;*/
                                        case "3高风险":
                                            riskLevel = "3";
                                            break;
                                        case "2中风险":
                                            riskLevel = "2";
                                            break;
                                        case "1低风险":
                                            riskLevel = "1";
                                            break;
                                        default:
                                            riskLevel = stringValue;
                                            break;
                                    }
                                    break;
                                case 12:
                                    if (ruleGroupMap.keySet().contains(stringValue)) {
                                        ruleGroup = ruleGroupMap.getOrDefault(stringValue,
                                                                              "规则分组不正确").toString();
                                    } else {
                                        ruleGroup = stringValue;
                                    }
                                    break;
                                case 14:
                                    if (ruleSourceMap.keySet().contains(stringValue)) {
                                        ruleSource = ruleSourceMap.getOrDefault(stringValue,
                                                                                "规则来源不正确").toString();
                                    } else {
                                        ruleSource = stringValue;
                                    }
                                    break;
                                case 17:
                                    if (ruleGroupMap2.keySet().contains(stringValue)) {
                                        ruleGroup2 = ruleGroupMap2.getOrDefault(stringValue, "细分组别不正确").toString();
                                    } else {
                                        ruleGroup2 = stringValue;
                                    }
                            }
                            break;
                    }
//                    logger.info(String.format("第[%d]行,第[%d]单元格内容:[%s]", i, j,
//                            value));
                }
                String sql = String.format(sqlTemplate,
                                           ruleId, ruleCode, ruleShowName, ruleName, otherId, description,
                                           coverRuleCertMobile, coverRuleExceptCertMobile, coverDateGroup, isInvisible,
                                           riskLevel, riskScore,
                                           ruleGroup, ruleSort, ruleSource, riskType, enabled, ruleGroup2);
                sql = sql.replaceAll("'null'", "NULL");
                logger.info(sql);
                assert out != null;
                out.write(sql.getBytes(StandardCharsets.UTF_8));
                out.write(System.getProperty("line.separator").getBytes(StandardCharsets.UTF_8));
                out.write(System.getProperty("line.separator").getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException | InvalidFormatException e) {
            logger.error("读取excel文件出错", e);
        }
        try {
            assert out != null;
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.error("关闭文件出错", e);
        }
    }

    public static void main(String[] args) {
        String destFilePath = "D:\\sqlOutput\\sql.txt";
        String rulePath = "D:\\rule.xlsx";
        readGammaRuleExcelAndSaveSql(rulePath, destFilePath);
//        System.out.println(File.separator);
//        System.out.println(System.getProperty("line.separator"));
    }

    public List<String> getStringList() {
        return new ArrayList<>();
    }
}
