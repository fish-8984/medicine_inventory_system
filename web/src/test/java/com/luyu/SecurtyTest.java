package com.luyu;

import com.luyu.properties.EmailProperties;
import com.luyu.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class SecurtyTest {
    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailProperties emailProperties;
    @Test
    public void test(){
        List<String> RemainingDatesBatchNo = new ArrayList<>();
        List<Long> medicineIdList = new ArrayList<>();
        List<String> medicineName = new ArrayList<>();
        List<String> RemainingDates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 假设当前日期为2023-04-01，您可以使用 LocalDate.now() 获取实际当前日期
        LocalDate currentDate = LocalDate.now();

        for (int j = 0; j < 100; j++) {
            medicineIdList.add((long) j);
            medicineName.add("感冒灵");
            LocalDate expirationDate = LocalDate.parse("2021-01-01", formatter); // 固定到期日期
            long daysUntilExpiration = java.time.temporal.ChronoUnit.DAYS.between(currentDate, expirationDate);

            if (daysUntilExpiration >= 0) {
                // 未过期
                if (daysUntilExpiration < 30) {
                    RemainingDates.add("剩余" + daysUntilExpiration + "天");
                } else {
                    RemainingDates.add("未过期");
                }
            } else {
                // 已过期
                RemainingDates.add("已过期" + Math.abs(daysUntilExpiration) + "天");
            }
            RemainingDatesBatchNo.add("123456");
        }

//        // 打印结果以验证
//        for (int i = 0; i < medicineName.size(); i++) {
//            System.out.println("药品名称: " + medicineName.get(i) +
//                    ", 批号: " + RemainingDatesBatchNo.get(i) +
//                    ", 剩余天数: " + RemainingDates.get(i));
//        }
        emailService.sendMedicationAlert(emailProperties.to, RemainingDatesBatchNo, medicineIdList, medicineName, RemainingDates);
    }

    @Test
    public void test1(){
        List<String> batchNoList = new ArrayList<>();
        List<Integer> currentQuantityList = new ArrayList<>();
        List<Long> medicineIdList = new ArrayList<>();
        List<String> medicineName = new ArrayList<>();
        List<Integer> minStockList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            medicineIdList.add((long) i);
            medicineName.add("感冒灵");
            Random random = new Random();
            int currentQuantity = random.nextInt(100);
            batchNoList.add("123456");
            minStockList.add(10);
            currentQuantityList.add(currentQuantity);
        }
        emailService.sendInventoryAlert(emailProperties.to, batchNoList, currentQuantityList, minStockList, medicineIdList, medicineName);
    }
}