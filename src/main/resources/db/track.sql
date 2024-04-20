CREATE TABLE track_mail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    to_email VARCHAR(255),
    track_email VARCHAR(255),
    subject VARCHAR(255)
);