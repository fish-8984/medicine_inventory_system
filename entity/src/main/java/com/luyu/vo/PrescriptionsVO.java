package com.luyu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionsVO {
    private Integer pendingCount;
    private Integer processingCount;
    private Integer doneCount;
}
