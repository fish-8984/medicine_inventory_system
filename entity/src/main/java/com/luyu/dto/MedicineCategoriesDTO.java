package com.luyu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineCategoriesDTO {
    private Integer categoryId;
    private String categoryName;
    private Integer parentId;
    private Integer sortOrder;
}
