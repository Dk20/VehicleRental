package com.debashis.service.impl;

import com.debashis.model.Booking;
import com.debashis.model.Branch;
import com.debashis.model.response.VehicleAvailabilityResponse;
import com.debashis.repo.BranchRepository;
import com.debashis.service.IInventoryService;
import com.debashis.service.IOnboardingService;
import com.debashis.service.IRentalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class RentalServiceImplTest {
    @Autowired
    private IOnboardingService iOnboardingService;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private IRentalService iRentalService;
    @Autowired
    private IInventoryService iInventoryService;
//    @Autowired
//    private VehicleInventoryRepository vehicleInventoryRepository;


    @Test
    public void test1(){
        Branch branch = iOnboardingService.createBranch("branch_A","Bangalore","{}");
        iOnboardingService.createVehicle("SUV","{}",branch.getBranchId());
        iOnboardingService.createVehicle("SEDAN","{}",branch.getBranchId());
        iOnboardingService.createVehicle("SEDAN","{}",branch.getBranchId());

        iOnboardingService.createPrice("SUV","200.00",branch.getBranchId());
        iOnboardingService.createPrice("SEDAN","150.00",branch.getBranchId());

        Branch branch2 = iOnboardingService.createBranch("branch_B","Bangalore","{}");
        iOnboardingService.createVehicle("SUV","{}",branch2.getBranchId());
        iOnboardingService.createVehicle("SUV","{}",branch2.getBranchId());
        iOnboardingService.createVehicle("SEDAN","{}",branch2.getBranchId());

        iOnboardingService.createPrice("SUV","210.00",branch2.getBranchId());
        iOnboardingService.createPrice("SEDAN","180.00",branch2.getBranchId());
        System.out.println("Data Added:");
        System.out.println(branchRepository.findAll());

        // testing on same date hence epoch does not matter...
        String slotId1 = "0";long startDateEpoch = 0L;long endDateEpoch = 0L;

        printAvailableVehicle(branch, slotId1, startDateEpoch, endDateEpoch);

        Booking booking = iRentalService.rentVehicle("SEDAN",slotId1,startDateEpoch,endDateEpoch,null);
        System.out.println(booking);
        booking = iRentalService.rentVehicle("SEDAN",slotId1,startDateEpoch,endDateEpoch,null);
        System.out.println(booking);

        printAvailableVehicle(branch, slotId1, startDateEpoch, endDateEpoch);
        iInventoryService.releaseInventory(booking.getBookingId());
        System.out.println("releasing inventory for last booking");
        printAvailableVehicle(branch, slotId1, startDateEpoch, endDateEpoch);
    }

    private void printAvailableVehicle(Branch branch, String slotId1, long startDateEpoch, long endDateEpoch) {
        List<VehicleAvailabilityResponse> availabilityResponses = iRentalService.getAvailableVehicle(
                branch.getBranchId(),slotId1,startDateEpoch,endDateEpoch
        );

        System.out.println("\nAvailable Vehicles:\n"+availabilityResponses+"\n");
    }

}