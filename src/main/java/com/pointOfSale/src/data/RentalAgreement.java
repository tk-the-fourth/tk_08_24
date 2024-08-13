package com.pointOfSale.src.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RentalAgreement {
    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private String checkoutDate;
    private String dueDate;
    private double dailyRentalCharge;
    private int chargeDays;
    private String preDiscountCharge;
    private double discountPercentage;
    private String discountAmount;
    private String finalCharge;

    public static RentalAgreementBuilder builder() {
        return new RentalAgreementBuilder();
    }

    public void printAgreement() {
        System.out.println(
                "Tool Code: " + this.toolCode + "\n" +
                "Tool Type: " + this.toolType + "\n" +
                "Tool Brand: " + this.toolBrand + "\n" +
                "Rental days: " + this.rentalDays + "\n" +
                "Check out date: " + this.checkoutDate + "\n" +
                "Due date: " + this.dueDate + "\n" +
                "Charge days: " + this.chargeDays + "\n" +
                "Pre-discount charge:$" + this.preDiscountCharge + "\n" +
                "Discount percent: " + (int)this.discountPercentage + "%\n" +
                "Discount amount:$" + this.discountAmount + "\n" +
                "Final Charge:$" + this.finalCharge);
    }
}