CREATE OR REPLACE PROCEDURE generate_slots_for_range(
    doctor_id UUID,
    start_date DATE,
    end_date DATE
)
LANGUAGE plpgsql AS $$
DECLARE
    d DATE;
    schedule RECORD;
    t_start TIME;
    t_slot_end TIME;
BEGIN
    -- Lặp từng ngày trong khoảng
    d := start_date;
    WHILE d <= end_date LOOP
        -- Xóa slot cũ chưa có appointment trong ngày
        DELETE FROM doctor_available_slots s
        WHERE s.doctor_user_id = doctor_id
          AND s.slot_date = d
          AND NOT EXISTS (
              SELECT 1 FROM appointments a WHERE a.slot_id = s.id
          );

        -- Lấy schedule cho ngày d
        SELECT *
        INTO schedule
        FROM doctor_schedules
        WHERE doctor_user_id = doctor_id
          AND day_of_week = EXTRACT(DOW FROM d)   -- mapping thứ
          AND is_active = true
        LIMIT 1;

        -- Nếu có schedule thì tạo slot
        IF FOUND THEN
            t_start := schedule.start_time;
            WHILE t_start < schedule.end_time LOOP
                t_slot_end := t_start + (schedule.slot_duration || ' minutes')::interval;

                IF t_slot_end <= schedule.end_time THEN
                    INSERT INTO doctor_available_slots (
                        doctor_user_id, slot_date, start_time, end_time,
                        is_available, created_at, updated_at
                    )
                    VALUES (
                        doctor_id, d, t_start, t_slot_end, true, now(), now()
                    );
                END IF;

                -- Sang slot kế tiếp
                t_start := t_slot_end + (schedule.break_duration || ' minutes')::interval;
            END LOOP;
        END IF;

        -- Disable slot nào trùng lịch nghỉ
        UPDATE doctor_available_slots s
        SET is_available = false
        FROM doctor_absences a
        WHERE s.doctor_user_id = a.doctor_user_id
          AND s.slot_date = a.absence_date
          AND (
               (a.start_time IS NULL AND a.end_time IS NULL)
               OR (s.start_time >= a.start_time AND s.end_time <= a.end_time)
          );

        -- Update timestamp
        UPDATE doctor_available_slots
        SET updated_at = now()
        WHERE doctor_user_id = doctor_id
          AND slot_date = d;

        -- Sang ngày tiếp theo
        d := d + INTERVAL '1 day';
    END LOOP;
END;
$$;


CALL generate_slots_for_range('b3367ff5-0630-4d3a-93b5-813628cd058b', '2025-09-15', '2025-09-17');

select * from doctor_available_slots das where doctor_user_id = 'b3367ff5-0630-4d3a-93b5-813628cd058b'


select up.first_name , up.last_name 
from users u join user_roles ur on u.id  = ur.user_id  
join roles r on r.id  = ur.role_id  
join user_profiles up on up.user_id = u.id 
where r."name" = 'DOCTOR'



WITH RankedSlots AS (
            SELECT 
                das.id as slotId,
                das.doctor_user_id,
                das.slot_date as slotDate,
                das.start_time as startTime,
                das.end_time as endTime,
                das.is_available as isAvailable,
                ROW_NUMBER() OVER (PARTITION BY das.doctor_user_id ORDER BY das.slot_date, das.start_time) as slot_rank
            FROM doctor_available_slots das
            WHERE das.is_available = true
                AND das.slot_date BETWEEN '2025-09-15' AND '2025-09-16'
        )
        SELECT 
            u.id as userId,
            up.first_name as firstName,
            up.last_name as lastName,
            up.phone as phone,
            u.email as email,
            up.date_of_birth as dateOfBirth,
            up.avatar_url as avatarUrl,
            up.gender as gender,
            s.name as specialtyName,
            mp.license_number as licenseNumber,
            mp.qualification as qualification,
            mp.years_of_experience as yearsOfExperience,
            mp.consultation_fee as consultationFee,
            rs.slotId as slotId,
            rs.slotDate as slotDate,
            rs.startTime as startTime,
            rs.endTime as endTime,
            rs.isAvailable as isAvailable
        FROM users u
        INNER JOIN user_profiles up ON u.id = up.user_id
        INNER JOIN user_roles ur ON u.id = ur.user_id AND ur.is_active = true
        INNER JOIN roles r ON ur.role_id = r.id AND r.name = 'DOCTOR'
        INNER JOIN medical_profiles mp ON u.id = mp.user_id AND mp.is_doctor_approved = true
        LEFT JOIN specialties s ON mp.specialty_id = s.id AND s.is_active = true
        LEFT JOIN RankedSlots rs ON u.id = rs.doctor_user_id AND rs.slot_rank <= 3
        WHERE u.is_active = true 
            AND u.is_email_verified = true
        ORDER BY up.first_name, up.last_name, rs.slotDate, rs.startTime
        
        
        
        
        SELECT DISTINCT
        u.id as userId,
        up.first_name as firstName,
        up.last_name as lastName,
        up.phone as phone,
        u.email as email,
        up.date_of_birth as dateOfBirth,
        up.avatar_url as avatarUrl,
        up.gender as gender,
        s.name as specialtyName,
        mp.license_number as licenseNumber,
        mp.qualification as qualification,
        mp.years_of_experience as yearsOfExperience,
        mp.consultation_fee as consultationFee
    FROM users u
    INNER JOIN user_profiles up ON u.id = up.user_id
    INNER JOIN user_roles ur ON u.id = ur.user_id AND ur.is_active = true
    INNER JOIN roles r ON ur.role_id = r.id AND r.name = 'DOCTOR'
    INNER JOIN medical_profiles mp ON u.id = mp.user_id AND mp.is_doctor_approved = true
    LEFT JOIN specialties s ON mp.specialty_id = s.id AND s.is_active = true
    WHERE u.is_active = true 
        AND u.is_email_verified = true
        AND EXISTS (
            SELECT 1 FROM doctor_available_slots das
            WHERE das.doctor_user_id = u.id
                AND das.is_available = true
                AND das.slot_date BETWEEN '2025-09-15' AND '2025-09-16'
        )
    ORDER BY up.first_name, up.last_name
    