CREATE TABLE IF NOT EXISTS customers (
    id VARCHAR(64) PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    driving_experience INT NOT NULL
);

CREATE TABLE IF NOT EXISTS vehicles (
    reg_number VARCHAR(32) PRIMARY KEY,
    brand VARCHAR(64) NOT NULL,
    model VARCHAR(64) NOT NULL,
    category VARCHAR(16) NOT NULL,
    base_daily_rate DOUBLE NOT NULL,
    mileage_since_last_service INT NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id VARCHAR(64) PRIMARY KEY,
    customer_id VARCHAR(64) NOT NULL,
    vehicle_reg_number VARCHAR(32) NOT NULL,
    rent_days INT NOT NULL,
    status VARCHAR(16) NOT NULL,
    CONSTRAINT fk_booking_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_booking_vehicle FOREIGN KEY (vehicle_reg_number) REFERENCES vehicles(reg_number)
);