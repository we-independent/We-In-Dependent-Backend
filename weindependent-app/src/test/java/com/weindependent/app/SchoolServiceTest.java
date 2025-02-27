package com.weindependent.app;
import com.weindependent.app.database.mapper.weindependent.*;
import com.weindependent.app.service.impl.SchoolServiceImpl;
import com.weindependent.app.vo.SchoolVO;
import com.weindependent.app.database.dataobject.SchoolDO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SchoolServiceTest {

    @Mock
    private SchoolMapper schoolMapper; // Mock数据库访问

    @InjectMocks
    private SchoolServiceImpl schoolService; // 自动注入mock依赖

    @Test
    public void testGetNearbySchools() {
        // 创建模拟数据
        List<SchoolDO> mockSchools = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            SchoolDO school = new SchoolDO();
            school.setName("School " + i);
            school.setLatitude(40.7128 + i * 0.001);
            school.setLongitude(-74.0060 + i * 0.001);
            mockSchools.add(school);
        }

        // Mock数据库查询方法
        Mockito.when(schoolMapper.findNearbySchools(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble()))
               .thenReturn(mockSchools);

        // 调用测试方法
        List<SchoolVO> schools = schoolService.getNearbySchools(40.7128, -74.0060, 1.0);

        // 验证测试结果
        assertNotNull(schools);
        assertEquals(10, schools.size());
        for (int i = 0; i < 10; i++) {
            assertEquals("School " + (i + 1), schools.get(i).getName());
        }
    }
}
