package com.debashis.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="booking")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Booking {
    public enum BookingStatus{
        CREATED, //requested but not paid
        CONFIRMED, // paid
        STARTED,
        COMPLETED,
        CANCELLED,
    }
    @Id
    @GeneratedValue
    private Long bookingId;
    private BookingStatus status; //there should be a separate fulfillment lifecycle, but using status for now

//    below inventory and pricing can be a list = lineItems ;
//    As in user should be able to book multiple slots for multiple dates , continuous or non-continuous
//
//    But we are going with single slot booking here.
//
//    Not keeping referential integrity here; As ideally booking table should be deployed in a separate database;
    private Long vehicleId;
    private String amount;
    private String slotId;
    private long startDateEpoch;
    private long endDateEpoch;

    private String metadata; // extra data like customer details, coupons etc.

    // audit fields
    private String createdBy;
    private long createdAt;
    private String updatedBy;
    private long updatedAt;
}
