package BillboardControlPanel.UnitTest;

import static BillboardControlPanel.Helper.ControllerHelper.*;

import BillboardControlPanel.Helper.ControllerHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static BillboardControlPanel.ServerUtilities.ServerRequestClient.*;
import static org.junit.jupiter.api.Assertions.*;

class ControllerHelperTest {
    String[][] mockSchedule = {
            {"0", "thu", "2020-05-24 09:22:31", "60"},
            {"1", "thu", "2020-05-24 09:22:31", "60"},
            {"2", "sat", "2020-05-24 09:22:31", "60"},
            {"3", "sun", "2020-05-24 09:22:31", "60"},
            {"4", "mon", "2020-05-24 09:22:31", "60"},
            {"5", "tue", "2020-05-24 09:22:31", "60"},
            {"6", "wed", "2020-05-24 09:22:31", "60"},
    };
    LocalDateTime localDateTime = LocalDateTime.of(2020, 5, 28, 6, 30);
    String expectedDay = "Thu";
    String expectedBillName = "thu";
    
    @Test
    public void getDayOfDateTest(){
        String day = getDayOfDate(localDateTime);
        Assertions.assertEquals(day, expectedDay);
    }

    @Test
    public void getDayScheduleTest(){
        String[][] daySchedule = getScheduleForSingleDay(mockSchedule, 1);
        Assertions.assertNotNull(daySchedule);
    }

    @Test
    public void getBillNameForADayTest(){
        String[][] billNames = getBillNamesFromScheduleSingleDay(mockSchedule, 0);
//        for (String[] string : billNames){
//            for (String item : string){
//                System.out.println("this = "+ item);
//            }
//        }
        Assertions.assertEquals(billNames[0][0], expectedBillName);
    }

}