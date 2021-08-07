package com.debashis.service;

import com.debashis.exception.InventoryNotAvailable;
import com.debashis.model.Booking;

public interface IBookingService {
    Booking createBooking(long vehicleId,String amount,String slotId,long startDateEpoch,
                          long endDateEpoch,String metadata) throws InventoryNotAvailable;
}
