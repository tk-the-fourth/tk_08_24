package com.pointOfSale.src.service;

import com.pointOfSale.src.data.RentalAgreement;
import com.pointOfSale.src.data.entity.Tool;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RentalService {
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("M/d/yy");
    private static final DateTimeFormatter OUTPUT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final String INVALID_DATE_FORMAT = "Date format must be mm/dd/yyyy";
    private static final String INVALID_RENTAL_DAYS = "Number of rental days must be greater than 0.";
    private static final String INVALID_DISCOUNT_PERCENTAGE = "Discount percentage must be 0 - 100.";
    private static final int MONTH_OF_JULY = 7;
    private static final int MONTH_OF_SEPTEMBER = 9;
    private static final int THIRD_DAY_OF_MONTH = 3;
    private static final int FOURTH_DAY_OF_MONTH = 4;
    private static final int FIFTH_DAY_OF_MONTH = 5;

    @Autowired
    private final ToolService toolService;

    public RentalAgreement rentTool(String toolCode, int rentalDays, double discountPercentage, String date) {
        LocalDate checkoutDate;
        try {
            checkoutDate = LocalDate.parse(date, INPUT_DATE_TIME_FORMATTER);
        } catch (DateTimeParseException var15) {
            throw new IllegalArgumentException(INVALID_DATE_FORMAT);
        }

        if (rentalDays < 1) {
            throw new IllegalArgumentException(INVALID_RENTAL_DAYS);
        }
        if (discountPercentage < 1 || discountPercentage > 100) {
            throw new IllegalArgumentException(INVALID_DISCOUNT_PERCENTAGE);
        }

        Tool toolRental = toolService.findByCode(toolCode);
        if (Objects.nonNull(toolRental)) {
            RentalAgreement rentalAgreement = RentalAgreement.builder()
                    .toolCode(toolRental.getCode())
                    .toolType(toolRental.getType())
                    .toolBrand(toolRental.getBrand())
                    .rentalDays(rentalDays).checkoutDate(checkoutDate.format(OUTPUT_DATE_TIME_FORMATTER))
                    .dueDate(calculateDueDate(rentalDays, checkoutDate))
                    .dailyRentalCharge(toolRental.getDailyCharge())
                    .discountPercentage(discountPercentage).build();

            int chargeDays = calculateChargeDays(rentalDays, checkoutDate, toolRental);
            rentalAgreement.setChargeDays(chargeDays);

            DecimalFormat decimalFormat = new DecimalFormat("###0.00");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

            double preDiscountCharge = chargeDays * toolRental.getDailyCharge();
            rentalAgreement.setPreDiscountCharge(decimalFormat.format(preDiscountCharge));

            double discountAmount = discountPercentage / 100.0 * preDiscountCharge;
            rentalAgreement.setDiscountAmount(decimalFormat.format(discountAmount));

            rentalAgreement.setFinalCharge(decimalFormat.format(preDiscountCharge - discountAmount));

            rentalAgreement.printAgreement();

            return rentalAgreement;
        } else {
            System.out.println("Tool code not found.");
            return null;
        }
    }

    private String calculateDueDate(int rentalDays, LocalDate checkoutDate) {
        return checkoutDate.plusDays(rentalDays - 1).format(OUTPUT_DATE_TIME_FORMATTER);
    }

    private int calculateChargeDays(int rentalDays, LocalDate checkoutDate, Tool tool) {
        AtomicInteger numberOfChargeDays = new AtomicInteger();
        List<LocalDate> checkoutDates = checkoutDate.datesUntil(checkoutDate.plusDays(rentalDays - 1)).collect(Collectors.toList());

        for (LocalDate date : checkoutDates) {
            if (dateIsWeekday(date)) {
                if (dateIsObservedHoliday(date)) {
                    if (tool.isHolidayCharge()) {
                        numberOfChargeDays.getAndIncrement();
                    }
                } else {
                    numberOfChargeDays.getAndIncrement();
                }
            }

            if (dateIsWeekend(date) && tool.isWeekendCharge()) {
                numberOfChargeDays.getAndIncrement();
            }
        }

        return numberOfChargeDays.get();
    }

    private boolean dateIsObservedHoliday(LocalDate date) {
        LocalDateTime localDateTime = date.atStartOfDay();
        int dayOfMonth = localDateTime.getDayOfMonth();
        int month = localDateTime.getMonth().getValue();
        return isFourthOfJulyObservationDate(date, month, dayOfMonth) || isLaborDay(date, month);
    }

    private static boolean isFourthOfJulyObservationDate(LocalDate date, int month, int dayOfMonth) {
        if (MONTH_OF_JULY == month) {
            if (FOURTH_DAY_OF_MONTH == dayOfMonth) {
                return true;
            }

            if (THIRD_DAY_OF_MONTH == dayOfMonth && DayOfWeek.FRIDAY.equals(date.getDayOfWeek())) {
                return true;
            }

            if (FIFTH_DAY_OF_MONTH == dayOfMonth && DayOfWeek.MONDAY.equals(date.getDayOfWeek())) {
                return true;
            }
        }

        return false;
    }

    private static boolean isLaborDay(LocalDate date, int month) {
        return MONTH_OF_SEPTEMBER == month ? date.equals(date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))) : false;
    }

    private boolean dateIsWeekend(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case SATURDAY, SUNDAY -> true;
            default -> false;
        };
    }

    private boolean dateIsWeekday(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> true;
            default -> false;
        };
    }
}