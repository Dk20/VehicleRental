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
    @Test
    public void driverTest(){
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
        /*
        * Req: covered
        * 1. Onboard a new branch with available vehicles
          2. Onboard new vehicle(s) of an existing type to a particular branch
        * */
        System.out.println(branchRepository.findAll());

        // testing on same date hence epoch does not matter...
        String slotId1 = "0";long startDateEpoch = 0L;long endDateEpoch = 0L;

        // Req: covered
        // 4. Display available vehicles for a given branch sorted on price
        printAvailableVehicle(branch, slotId1, startDateEpoch, endDateEpoch);

        // Req: covered
        // 3. Rent a vehicle for a time slot and a vehicle type(the lowest price as the default choice extendable to any other strategy).
        Booking booking = iRentalService.rentVehicle("SEDAN",slotId1,startDateEpoch,endDateEpoch,null);
        System.out.println(booking);
        booking = iRentalService.rentVehicle("SEDAN",slotId1,startDateEpoch,endDateEpoch,null);
        System.out.println(booking);

        printAvailableVehicle(branch, slotId1, startDateEpoch, endDateEpoch);

        // Req: covered
        // 5. The vehicle will have to be dropped at the same branch where it was picked up.
        iInventoryService.releaseInventory(booking.getBookingId());
        System.out.println("releasing inventory for last booking");

        printAvailableVehicle(branch, slotId1, startDateEpoch, endDateEpoch);

        // todo: Dynamic pricing – demand vs supply. If 80% of cars in a particular branch are booked, increase the price by 10%.
        // Idea is to send an internal event(or simply call an async func) when trigger condition is met.
        // This event when consumed should update the price.
    }

    private void printAvailableVehicle(Branch branch, String slotId1, long startDateEpoch, long endDateEpoch) {
        List<VehicleAvailabilityResponse> availabilityResponses = iRentalService.getAvailableVehicle(
                branch.getBranchId(),slotId1,startDateEpoch,endDateEpoch
        );

        System.out.println("\nAvailable Vehicles:\n"+availabilityResponses+"\n");
    }


}