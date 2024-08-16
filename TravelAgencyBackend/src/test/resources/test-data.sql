INSERT INTO destinations (id, city_name, country_name) VALUES
(1L, 'Paris', 'France'),
(2L, 'New York', 'USA'),
(3L, 'Tokyo', 'Japan');

INSERT INTO users (id, first_name, last_name, username, email, phone, password) VALUES
(1L, 'John', 'Doe', 'johndoe', 'john.doe@example.com', '123-456-7890', 'password123'),
(2L, 'Jane', 'Smith', 'janesmith', 'jane.smith@example.com', '098-765-4321', 'password456'),
(3L, 'Admin', 'Admin', 'ADMIN', 'admin@gmail.com', '098-765-4321', '$2a$10$Okb0ij5LM36viM/cJD3eXuJazHV.GjJMplrtcAV1tkr7BILbDwx5G'),
(4L, 'Salesman', 'Salesman', 'SALESMAN', 'salesman@gmail.com', '098-765-4321', '$2a$10$SEJ.bIIacNnQe58hFBLZD.I5eKFJol35xrP2R24XSifsTAMepqWfS'),
(5L, 'Customer', 'Customer', 'CUSTOMER', 'customer@gmail.com', '098-765-4321', '$2a$10$p0Itl0d9q.sKKnq166or/uMHJF5UcBDPiOukkheH6hFrLM0Izr0su');

INSERT INTO roles (id, role_name) VALUES
(1, 'CUSTOMER'),
(2, 'ADMIN'),
(3,'SALESMAN');
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- John Doe as CUSTOMER
(2, 2), -- Jane Smith as ADMIN
(3,2),
(4,3),
(5,1);
INSERT INTO arrangements (id, date_from, date_to, description, free_seats, price_per_person, destination_id, user_id) VALUES
(1L, '2024-12-01', '2024-12-10', 'Christmas in Paris', 10, 500.00, 1, 1),
(2L, '2024-11-01', '2024-11-07', 'New York City Tour', 15, 300.00, 2, 2);

