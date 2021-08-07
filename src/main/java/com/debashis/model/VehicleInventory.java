package com.debashis.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "inventory", indexes = {
        @Index(name = "booking_idx", columnList = "bookingId"), // multiple slots may be allowed hence not unique
        @Index(name = "vehicleId_slotId_idx", columnList = "vehicleId,slotId")
})
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
/*
 * If below record is present in the table, for a query(slot + date range), inventory is blocked
 * else inventory is free
 *
 * Upon booking a record is created. When booking is completed/inventory is released, record is deleted.
 *
 * Ideally, "soft-delete" should be applied with a status = {BLOCKED,FREE...},
 * but that would require clearing of older records via an
 * async worker which archives them on an object-store.
 * */
public class VehicleInventory {
    @Id
    @GeneratedValue
    private Long vehicleInventoryId;
    @ManyToOne
    @JoinColumn(name = "vehicleId", nullable = false)
    private Vehicle vehicle;
    /*
     * slotId= 0-23, hour slot
     * 15 = 3pm-4pm
     * */
    private String slotId;
    private Long bookingId;
    private long startDateEpoch;
    private long endDateEpoch;

    public boolean hasOverLap(String slotId, long startDateEpoch, long endDateEpoch) {
        return this.slotId.equals(slotId) &&
                ((this.startDateEpoch <= endDateEpoch && this.startDateEpoch >= startDateEpoch)
                        || (this.endDateEpoch <= endDateEpoch && this.endDateEpoch >= startDateEpoch));
    }

}
