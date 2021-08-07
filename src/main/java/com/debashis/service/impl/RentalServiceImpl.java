package com.debashis.service.impl;

import com.debashis.exception.InventoryNotAvailable;
import com.debashis.model.*;
import com.debashis.model.response.VehicleAvailabilityResponse;
import com.debashis.model.response.VehiclePriceResponse;
import com.debashis.repo.BranchRepository;
import com.debashis.repo.VehicleInventoryRepository;
import com.debashis.repo.VehiclePriceRepository;
import com.debashis.repo.VehicleRepository;
import com.debashis.service.IBookingService;
import com.debashis.service.IRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl implements IRentalService {
    private BranchRepository branchRepository;
    private VehicleRepository vehicleRepository;
    private VehicleInventoryRepository vehicleInventoryRepository;
    private VehiclePriceRepository vehiclePriceRepository;
    private IBookingService iBookingService;
    @Autowired
    public RentalServiceImpl(BranchRepository branchRepository, VehicleRepository vehicleRepository,
                             VehicleInventoryRepository vehicleInventoryRepository, VehiclePriceRepository vehiclePriceRepository, IBookingService iBookingService) {
        this.branchRepository = branchRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehicleInventoryRepository = vehicleInventoryRepository;
        this.vehiclePriceRepository = vehiclePriceRepository;
        this.iBookingService = iBookingService;
    }

    /*
    * Should be a Paginated response, for simplicity did not implement
    * */
    @Transactional(readOnly = true)
    @Override
    public List<VehicleAvailabilityResponse> getAvailableVehicle(Long branchId, String slotId, long startDateEpoch, long endDateEpoch) {
        Branch branch = branchRepository.getById(branchId);
        Set<Vehicle> availableVehicleSet = branch.getVehicleSet().stream().filter(vehicle ->
                vehicle.getVehicleInventorySet().stream()
                        .noneMatch(vehicleInventory -> vehicleInventory.hasOverLap(slotId,startDateEpoch,endDateEpoch))
        ).collect(Collectors.toSet());
        Map<String,String> vehicleTypePriceMap = branch.getPriceSet().stream()
                .collect(Collectors.toMap(VehiclePrice::getVehicleType,VehiclePrice::getPrice));
        return availableVehicleSet.stream().map(vehicle -> VehicleAvailabilityResponse.builder()
                .branchName(branch.getName())
                .branchId(branchId)
                .price(vehicleTypePriceMap.get(vehicle.getType()))
                .vehicleId(vehicle.getVehicleId())
                .type(vehicle.getType())
                .build()).sorted(
                        /*
                        * We can either do the sorting here or we keep a sorted index on "pricing" table itself.
                        * Keeping the price sorting in the table is the best approach,
                        * but since no.of vehicle type in not much we can do sorting here
                        * */
                    Comparator.comparing(response -> new BigDecimal(response.getPrice()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public Booking rentVehicle(String vehicleType, String slotId, long startDateEpoch, long endDateEpoch,
                               Function<Map<String,Vehicle> , VehiclePriceResponse> selectionStrategy) {
//       The below entire set of queries can be avoided with a simple join query on Vehicle, VehicleInventory and VehiclePrice
        List<Vehicle> vehicleList = vehicleRepository.findByType(vehicleType);
        List<VehicleInventory> blockedInventory = vehicleInventoryRepository.findBlockedInventoryBulk(
                vehicleList.stream().map(Vehicle::getVehicleId).collect(Collectors.toList()),
                slotId,
                startDateEpoch,
                endDateEpoch
        );
        List<VehiclePrice> vehiclePriceList = vehiclePriceRepository.findByVehicleType(vehicleType);

        if(Objects.isNull(selectionStrategy)) // default strategy
            selectionStrategy = stringVehicleMap -> {
                String bestPrice = stringVehicleMap.keySet().stream().min(Comparator.comparing(price -> new BigDecimal(price))).get();
                return VehiclePriceResponse.builder().price(bestPrice).vehicle(stringVehicleMap.get(bestPrice)).build();
            };

        Map<String, Vehicle> priceVehicleMap = buildSelectorStrategyInput(vehicleList, blockedInventory, vehiclePriceList);

        if(priceVehicleMap.isEmpty())
            throw new InventoryNotAvailable("No vehicles available");

        VehiclePriceResponse selectedVehicleRes = selectionStrategy.apply(priceVehicleMap);

        return iBookingService.createBooking(
                selectedVehicleRes.getVehicle().getVehicleId(),
                selectedVehicleRes.getPrice(),
                slotId,startDateEpoch,endDateEpoch,"{}");
    }

    private Map<String, Vehicle> buildSelectorStrategyInput(List<Vehicle> vehicleList, List<VehicleInventory> blockedInventory, List<VehiclePrice> vehiclePriceList) {
        //        below data manipulation can be optimised using parallelStream
        Set<Long> blockedVehicleIdSet = blockedInventory.stream().map(VehicleInventory::getVehicle)
                .map(Vehicle::getVehicleId).collect(Collectors.toSet());
        List<Vehicle> availableVehicleList = vehicleList.stream()
                .filter(vehicle -> !blockedVehicleIdSet.contains(vehicle.getVehicleId()))
                .collect(Collectors.toList());
        Set<Long> availableVehicleBranchIdSet = availableVehicleList.stream()
                .map(Vehicle::getBranch).map(Branch::getBranchId)
                .collect(Collectors.toSet());
        return vehiclePriceList.stream()
                .filter(vehiclePrice -> availableVehicleBranchIdSet.contains(vehiclePrice.getBranch().getBranchId()))
                .collect(Collectors.toMap(vehiclePrice -> vehiclePrice.getPrice(),
                        vehiclePrice -> availableVehicleList.stream()
                                .filter(vehicle -> vehicle.getBranch().getBranchId()==vehiclePrice.getBranch().getBranchId())
                                .findFirst().get()
                ));
    }
}
