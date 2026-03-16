DROP INDEX idx_cust_id;
DROP INDEX idx_request_bill;
DROP INDEX idx_cars_year;
DROP INDEX idx_cars_vin;

CREATE INDEX idx_cars_vin
    ON Cars
    USING BTREE
    (vin);

CREATE INDEX idx_cars_year
    ON Cars
    USING BTREE
    (year);

CREATE INDEX idx_request_bill
    ON Service_Request
    USING BTREE
    (bill);
    
CREATE INDEX idx_cust_id
    ON Customer
    USING BTREE
    (cust_id);