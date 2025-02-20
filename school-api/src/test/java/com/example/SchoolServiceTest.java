package com.example;

import com.example.models.School;
import com.example.repo.SchoolRepository;
import com.example.service.SchoolService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)  // Specify the configuration class
public class SchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    // Inject the mock repository into the service
    @InjectMocks
    private SchoolService schoolService; // SchoolService instance will be injected

    @Test
    public void testGetNearbySchools() {
        // Mock the data: let's say 10 nearby schools with unique names
        List<School> mockSchools = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            School school = new School();
            school.setName("School " + i);
            school.setLatitude(40.7128 + i * 0.001);  // Adjust latitudes slightly
            school.setLongitude(-74.0060 + i * 0.001);  // Adjust longitudes slightly
            mockSchools.add(school);
        }

        // Mock the repository call to return the list of schools
        Mockito.when(schoolRepository.findNearbySchools(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble()))
               .thenReturn(mockSchools);

        // Call the service method on the injected instance
        List<School> schools = schoolService.getNearbySchools(40.7128, -74.0060, 1.0);

        // Assert the results
        assertNotNull(schools);
        assertEquals(10, schools.size());  // Verify that 10 schools are returned
        for (int i = 0; i < 10; i++) {
            assertEquals("School " + (i + 1), schools.get(i).getName());  // Verify school names
        }
    }
}
