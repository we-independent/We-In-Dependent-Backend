package com.weindependent.app.database.dataobject;


/**
 * 获取文章Category的名字
 * 
 * @author Hurely
 *    2025-04-5
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfoDO {
    private String name;
    private Integer id;
}

// public class CategoryNameDO {
//     private String name;

//     public CategoryNameDO(String name) {
//         this.name = name;
//     }

//     public String getName() {
//         return name;
//     }
// }