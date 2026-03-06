
-- 1. Bảng Payments (Thanh toán)
CREATE TABLE payments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          subscription_id BIGINT NOT NULL,
                          amount DOUBLE NOT NULL,
                          payment_date DATETIME NOT NULL,
                          payment_method VARCHAR(50),
                          status VARCHAR(20) NOT NULL, -- PENDING, COMPLETED, FAILED
                          CONSTRAINT fk_payment_sub FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
);

-- 2. Bảng Health Metrics (Chỉ số sức khỏe)
CREATE TABLE health_metrics (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                member_id BIGINT NOT NULL,
                                weight DOUBLE NOT NULL,
                                height DOUBLE NOT NULL,
                                bmi DOUBLE,
                                recorded_date DATE NOT NULL,
                                CONSTRAINT fk_health_member FOREIGN KEY (member_id) REFERENCES members(id)
);

-- 3. Bảng Workout Logs (Nhật ký tập luyện)
CREATE TABLE workout_logs (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              member_id BIGINT NOT NULL,
                              exercise_name VARCHAR(100) NOT NULL,
                              sets INT,
                              reps INT,
                              calories_burned DOUBLE,
                              workout_date DATE NOT NULL,
                              CONSTRAINT fk_workout_member FOREIGN KEY (member_id) REFERENCES members(id)
);

-- 4. Bảng AI Recommendations (Tư vấn từ AI)
CREATE TABLE ai_recommendations (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    member_id BIGINT NOT NULL,
                                    generated_plan_json TEXT NOT NULL, -- Lưu trữ toàn bộ phác đồ định dạng JSON
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT fk_ai_member FOREIGN KEY (member_id) REFERENCES members(id)
);

-- Bổ sung cột ảnh cho các bảng cũ
ALTER TABLE members ADD COLUMN avatar_url VARCHAR(255);
ALTER TABLE packages ADD COLUMN thumbnail_url VARCHAR(255);