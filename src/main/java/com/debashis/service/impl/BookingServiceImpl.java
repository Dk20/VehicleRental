package com.debashis.service.impl;

import com.debashis.exception.InventoryNotAvailable;
import com.debashis.model.Booking;
import com.debashis.repo.BookingRepository;
import com.debashis.service.IBookingService;
import com.debashis.service.IInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingServiceImpl implements IBookingService {

    private BookingRepository bookingRepository;
    private IInventoryService iInventoryService;
    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, IInventoryService iInventoryService) {
        this.bookingRepository = bookingRepository;
        this.iInventoryService = iInventoryService;
    }

    @Transactional(rollbackFor = {InventoryNotAvailable.class,Exception.class})
    @Override
    public Booking createBooking(long vehicleId, String amount, String slotId, long startDateEpoch, long endDateEpoch, String metadata) throws InventoryNotAvailable {
        Booking booking = bookingRepository.save(Booking.builder()
                .vehicleId(vehicleId).amount(amount)
                .slotId(slotId).startDateEpoch(startDateEpoch).endDateEpoch(endDateEpoch)
                .build());
        iInventoryService.blockInventory(booking.getBookingId(),vehicleId,slotId,startDateEpoch,endDateEpoch);
        return booking;
    }
}
