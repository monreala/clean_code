package com.carhire;

import com.carhire.importing.BookingImportService;
import com.carhire.importing.BookingImportServiceImpl;
import com.carhire.importing.CustomerImportService;
import com.carhire.importing.CustomerImportServiceImpl;
import com.carhire.importing.VehicleImportService;
import com.carhire.importing.VehicleImportServiceImpl;
import com.carhire.model.Booking;
import com.carhire.model.Customer;
import com.carhire.model.Vehicle;
import com.carhire.repository.BookingRepository;
import com.carhire.repository.CustomerRepository;
import com.carhire.repository.MySqlBookingRepository;
import com.carhire.repository.MySqlCustomerRepository;
import com.carhire.repository.MySqlVehicleRepository;
import com.carhire.repository.VehicleRepository;
import com.carhire.service.CustomerExperienceComparator;
import com.carhire.service.booking.BookingService;
import com.carhire.service.booking.BookingServiceImpl;
import com.carhire.service.pricing.DiscountProvider;
import com.carhire.service.pricing.PriceCalculator;
import com.carhire.service.pricing.PriceCalculatorImpl;
import com.carhire.service.pricing.StandardDiscountProvider;
import com.carhire.service.reader.BookingDraftCsvReader;
import com.carhire.service.reader.BookingDraftReader;
import com.carhire.service.reader.CustomerCsvReader;
import com.carhire.service.reader.CustomerReader;
import com.carhire.service.reader.VehicleCsvReader;
import com.carhire.service.reader.VehicleReader;
import com.carhire.service.validation.BookingValidator;
import com.carhire.service.validation.BookingValidatorImpl;
import com.carhire.service.validation.CustomerValidator;
import com.carhire.service.validation.CustomerValidatorImpl;
import com.carhire.service.validation.VehicleValidator;
import com.carhire.service.validation.VehicleValidatorImpl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String dataFile = "data.csv";

        CustomerReader customerReader = new CustomerCsvReader();
        VehicleReader vehicleReader = new VehicleCsvReader();
        BookingDraftReader bookingDraftReader = new BookingDraftCsvReader();

        CustomerValidator customerValidator = new CustomerValidatorImpl();
        VehicleValidator vehicleValidator = new VehicleValidatorImpl();
        BookingValidator bookingValidator = new BookingValidatorImpl(customerValidator, vehicleValidator);

        CustomerRepository customerRepository = new MySqlCustomerRepository();
        VehicleRepository vehicleRepository = new MySqlVehicleRepository();
        BookingRepository bookingRepository = new MySqlBookingRepository();

        DiscountProvider discountProvider = new StandardDiscountProvider();
        PriceCalculator priceCalculator = new PriceCalculatorImpl(bookingValidator, customerValidator, discountProvider);

        BookingService bookingService = new BookingServiceImpl(bookingValidator, bookingRepository);

        CustomerImportService customerImport = new CustomerImportServiceImpl(customerReader, customerValidator, customerRepository);
        VehicleImportService vehicleImport = new VehicleImportServiceImpl(vehicleReader, vehicleValidator, vehicleRepository);
        BookingImportService bookingImport = new BookingImportServiceImpl(bookingDraftReader, bookingService);

        System.out.println("=== Импорт клиентов ===");
        List<Customer> customers = customerImport.importValidCustomer(dataFile);

        System.out.println("\n=== Импорт автомобилей ===");
        List<Vehicle> vehicles = vehicleImport.importValidVehicles(dataFile);

        System.out.println("\n=== Импорт и подтверждение бронирований ===");
        Map<String, Customer> customersById = customers.stream()
                .collect(Collectors.toMap(Customer::getId, Function.identity()));
        Map<String, Vehicle> vehiclesByRegNumber = vehicles.stream()
                .collect(Collectors.toMap(Vehicle::getRegNumber, Function.identity()));
        List<Booking> confirmedBookings = bookingImport.importAndConfirmBookings(dataFile, customersById, vehiclesByRegNumber);

        System.out.println("\n=== Расчёт стоимости подтверждённых бронирований ===");
        for (Booking booking : confirmedBookings) {
            double total = priceCalculator.calculateTotalPrice(booking);
            System.out.printf("%s | %s | %s | %d дн. | итого: %.2f%n",
                    booking.getBookingId(),
                    booking.getCustomer().getFullName(),
                    booking.getVehicle().getBrand() + " " + booking.getVehicle().getModel(),
                    booking.getRentDays(),
                    total);
        }

        System.out.println("\n=== Клиенты по убыванию стажа ===");
        customers.sort(new CustomerExperienceComparator());
        for (Customer c : customers) {
            System.out.println(c.getFullName() + " | Возраст: " + c.getAge() + " | Стаж: " + c.getDrivingExperienceYears());
        }
    }
}