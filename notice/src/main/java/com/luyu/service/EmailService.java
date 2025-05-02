package com.luyu.service;

import com.luyu.properties.EmailProperties;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final EmailProperties emailProperties;

    public void sendInventoryAlert(String email, List<String> batchNoList, List<Integer> currentQuantityList, List<Integer> minStockList,
                                   List<Long> medicineIdList, List<String> medicineName) {
        if (batchNoList.isEmpty()) {
            return; // 无数据不发送
        }

        try {
            byte[] excelData = generateInventoryExcel(batchNoList, currentQuantityList, minStockList, medicineIdList, medicineName);
            sendEmailWithAttachment(email,"库存不足预警通知", "附件中为库存低于最小库存的药品列表。", "库存提醒.xlsx", excelData);
        } catch (IOException e) {
            throw new RuntimeException("生成Excel失败", e);
        }
    }

    public void sendMedicationAlert(String email, List<String> batchNoList, List<Long> medicineIdList,
                                    List<String> medicineName, List<String> remainingDates) {
        if (batchNoList.isEmpty()) {
            return; // 无数据不发送
        }

        try {
            byte[] excelData = generateMedicationExcel(batchNoList, medicineIdList, medicineName, remainingDates);
            sendEmailWithAttachment(email, "药品效期预警通知", "附件中为近效期或已过期的药品列表。", "药物警报.xlsx", excelData);
        } catch (IOException e) {
            throw new RuntimeException("生成Excel失败", e);
        }
    }

    private byte[] generateInventoryExcel(List<String> batchNoList, List<Integer> currentQuantityList, List<Integer> minStockList,
                                          List<Long> medicineIdList, List<String> medicineName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("库存不足药品");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            createCell(headerRow, 0, "批次号");
            createCell(headerRow, 1, "当前库存");
            createCell(headerRow, 2, "最低库存");
            createCell(headerRow, 3, "药品ID");
            createCell(headerRow, 4, "药品名称");

            // 填充数据
            for (int i = 0; i < batchNoList.size(); i++) {
                Row row = sheet.createRow(i + 1);
                createCell(row, 0, batchNoList.get(i));
                createCell(row, 1, currentQuantityList.get(i));
                createCell(row, 2, minStockList.get(i));
                createCell(row, 3, medicineIdList.get(i));
                createCell(row, 4, medicineName.get(i));
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private byte[] generateMedicationExcel(List<String> batchNoList, List<Long> medicineIdList,
                                           List<String> medicineName, List<String> remainingDates) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("药品效期");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            createCell(headerRow, 0, "批次号");
            createCell(headerRow, 1, "药品ID");
            createCell(headerRow, 2, "药品名称");
            createCell(headerRow, 3, "剩余/过期天数");

            // 填充数据
            for (int i = 0; i < batchNoList.size(); i++) {
                Row row = sheet.createRow(i + 1);
                createCell(row, 0, batchNoList.get(i));
                createCell(row, 1, medicineIdList.get(i));
                createCell(row, 2, medicineName.get(i));
                createCell(row, 3, remainingDates.get(i));
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void createCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }

    private void createCell(Row row, int column, Integer value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }

    private void createCell(Row row, int column, Long value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }

    private void sendEmailWithAttachment(String to, String subject, String text, String attachmentName, byte[] attachment) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailProperties.from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            // 添加Excel附件
            helper.addAttachment(attachmentName, new ByteArrayResource(attachment), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("发送邮件失败", e);
        }
    }
}