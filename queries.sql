-- 6. Closed service requests with bill < 100
SELECT S.closeDate, S.comments, S.bill 
    FROM Service_Request S 
    WHERE S.closeDate IS NOT NULL AND S.bill < 100;

-- 7. List first and last name of customers having more than 20 different cars
SELECT Cu.firstName, Cu.lastName
    FROM Customer Cu, Cars Ca 
    WHERE Cu.cust_id = Ca.cust_id
    GROUP BY Cu.firstName, Cu.lastName
    HAVING COUNT(Ca.cust_id) > 20;

-- 8. List cars before year 1995 with odometer reading less than 50000
SELECT C.make, C.model, C.year 
    FROM Cars C, Service_Request S 
    WHERE C.vin = S.vin AND C.year < 1995 AND S.odometer < 50000;

-- 9. List the make, model and number of service requests for the first k cars with the highest number of service orders
SELECT Ca.make, Ca.model, COUNT(*) AS num_service_requests
    FROM Cars Ca, Service_Request SR 
    WHERE Ca.vin = SR.vin AND SR.closeDate IS NULL
    ORDER BY num_service_requests DESC LIMIT BY k;

-- 10. List customers and their total bill in desc order
SELECT T.cust_id, T.firstName, T.lastName, SUM(S.bill) as customerTotalBill 
    FROM Customer T, Service_Request S, Cars C 
    WHERE T.cust_id = C.cust_id AND C.vin = S.vin 
    GROUP BY T.cust_id 
    ORDER BY customerTotalBill DESC;