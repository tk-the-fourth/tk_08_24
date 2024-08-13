package com.pointOfSale.src.service;

import com.pointOfSale.src.data.RentalAgreement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RentalServiceTest {
    private RentalService rentalService;
    @Autowired
    private ToolService toolService;

    public RentalServiceTest() {
    }

    @BeforeEach
    void setUp() {
        rentalService = new RentalService(toolService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void rentTool_scenario1_exception_caused_by_discount_101() {
        IllegalArgumentException thrown = (IllegalArgumentException) Assertions.assertThrows(IllegalArgumentException.class, () -> {
            rentalService.rentTool("JAKR", 5, 101.0, "9/3/15");
        });
        Assertions.assertTrue(thrown.getMessage().contains("Discount percentage must be 0 - 100."));
    }

    @Test
    void rentTool_scenario2_valid_3_day_rental_LADW() {
        RentalAgreement rentalAgreement = rentalService.rentTool("LADW", 3, 10.0, "7/2/20");
        Assertions.assertEquals(3, rentalAgreement.getRentalDays());
        Assertions.assertEquals(10.0, rentalAgreement.getDiscountPercentage());
        Assertions.assertEquals("LADW", rentalAgreement.getToolCode());
        Assertions.assertEquals("07/02/2020", rentalAgreement.getCheckoutDate());
    }

    @Test
    void rentTool_scenario3_valid_5_day_rental_CHNS() {
        RentalAgreement rentalAgreement = rentalService.rentTool("CHNS", 5, 25.0, "7/2/15");
        Assertions.assertEquals(5, rentalAgreement.getRentalDays());
        Assertions.assertEquals(25.0, rentalAgreement.getDiscountPercentage());
        Assertions.assertEquals("CHNS", rentalAgreement.getToolCode());
        Assertions.assertEquals("07/02/2015", rentalAgreement.getCheckoutDate());
    }

    @Test
    void rentTool_scenario4_exception_caused_by_discount_0_JAKD() {
        IllegalArgumentException thrown = (IllegalArgumentException)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            rentalService.rentTool("JAKD", 6, 0.0, "9/3/15");
        });
        Assertions.assertTrue(thrown.getMessage().contains("Discount percentage must be 0 - 100."));
    }

    @Test
    void rentTool_scenario5_exception_caused_by_discount_0_JAKR() {
        IllegalArgumentException thrown = (IllegalArgumentException)Assertions.assertThrows(IllegalArgumentException.class, () -> {
            rentalService.rentTool("JAKR", 9, 0.0, "7/2/15");
        });
        Assertions.assertTrue(thrown.getMessage().contains("Discount percentage must be 0 - 100."));
    }

    @Test
    void rentTool_scenario6_valid_4_day_rental_JAKR() {
        RentalAgreement rentalAgreement = rentalService.rentTool("JAKR", 4, 50.0, "7/2/20");
        Assertions.assertEquals(4, rentalAgreement.getRentalDays());
        Assertions.assertEquals(50.0, rentalAgreement.getDiscountPercentage());
        Assertions.assertEquals("JAKR", rentalAgreement.getToolCode());
        Assertions.assertEquals("07/02/2020", rentalAgreement.getCheckoutDate());
    }

    @Test
    void rentTool_scenario7_toolCode_not_found() {
        RentalAgreement rentalAgreement = rentalService.rentTool("NOPE", 4, 50.0, "7/2/20");
        Assertions.assertNull(rentalAgreement);
    }
}
