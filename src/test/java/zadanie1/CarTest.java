package zadanie1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarTest {

    private HashMap<String, Long> yearMileage = new HashMap<>();

    @Mock
    private Car car;

    @Mock
    private CarDAO carDAO;

    @InjectMocks
    private CarService carService = new CarService();

    @BeforeEach
    void doBefore() {
        for (int i = 2000; i <= 2010; i++) {
            yearMileage.put(Integer.toString(i), (long) (i * 10));
        }

        when(carDAO.findById(anyLong())).thenReturn(car);
        when(car.getYearMileage()).thenReturn(yearMileage);
    }

    @Test
    void testYearMileageWholeRange() {
        long mileage = carService.findMileageBetweenYears(anyLong(), "2000", "2010");

        assertEquals(220550, mileage);
    }

    @Test
    void testYearMileageBeforeRange() {
        long mileage = carService.findMileageBetweenYears(anyLong(), "1990", "1999");

        assertEquals(0, mileage);
    }

    @Test
    void testYearMileageAfterRange() {
        long mileage = carService.findMileageBetweenYears(anyLong(), "2011", "2019");

        assertEquals(0, mileage);
    }

    @Test
    void testYearMileagePartlyRange() {
        long mileage = carService.findMileageBetweenYears(anyLong(), "1990", "1999");

        assertEquals(0, mileage);
    }
}
