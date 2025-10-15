-- DROP PROCEDURE public.generate_slots_for_range(uuid, date, date);

CREATE OR REPLACE PROCEDURE public.generate_slots_for_range(IN doctor_id uuid, IN start_date date, IN end_date date)
 LANGUAGE plpgsql
AS $procedure$
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
          AND s.slot_date = d;
--          AND NOT EXISTS (
--              SELECT 1 FROM appointments a WHERE a.slot_id = s.id
--          );

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
$procedure$
;


CREATE TABLE public.doctor_absences (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    doctor_user_id uuid NOT NULL,
    absence_date date NOT NULL,
    start_time time(6) without time zone,
    end_time time(6) without time zone,
    reason character varying(255),
    notes character varying(255),
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE public.doctor_available_slots (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    doctor_user_id uuid NOT NULL,
    slot_date date NOT NULL,
    start_time time(6) without time zone NOT NULL,
    end_time time(6) without time zone NOT NULL,
    is_available boolean DEFAULT true,
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN public.doctor_available_slots.slot_date IS 'Ngày cụ thể của slot thời gian';
COMMENT ON COLUMN public.doctor_available_slots.start_time IS 'Thời gian bắt đầu của slot';
COMMENT ON COLUMN public.doctor_available_slots.end_time IS 'Thời gian kết thúc của slot';
COMMENT ON COLUMN public.doctor_available_slots.is_available IS 'Trạng thái khả dụng của slot (true: có thể đặt, false: đã đặt hoặc bác sĩ nghỉ)';
COMMENT ON COLUMN public.doctor_available_slots.created_at IS 'Thời gian tạo slot';
COMMENT ON COLUMN public.doctor_available_slots.updated_at IS 'Thời gian cập nhật slot lần cuối';
CREATE TABLE public.doctor_schedules (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    doctor_user_id uuid NOT NULL,
    day_of_week integer NOT NULL,
    start_time time(6) without time zone NOT NULL,
    end_time time(6) without time zone NOT NULL,
    slot_duration integer DEFAULT 30,
    is_active boolean DEFAULT true,
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    break_duration integer DEFAULT 5,
    max_appointments_per_day integer,
    max_appointments_per_slot integer DEFAULT 1,
    notes character varying(255),
    timezone character varying(255) DEFAULT 'UTC'::character varying,
    CONSTRAINT doctor_schedules_day_check CHECK (((day_of_week >= 1) AND (day_of_week <= 7)))
);
COMMENT ON COLUMN public.doctor_schedules.day_of_week IS 'Thứ trong tuần (1=Thứ 2, 2=Thứ 3, ..., 7=Chủ nhật)';
COMMENT ON COLUMN public.doctor_schedules.start_time IS 'Thời gian bắt đầu làm việc trong ngày';
COMMENT ON COLUMN public.doctor_schedules.end_time IS 'Thời gian kết thúc làm việc trong ngày';
COMMENT ON COLUMN public.doctor_schedules.slot_duration IS 'Thời lượng mỗi slot khám (phút), mặc định 30 phút';
COMMENT ON COLUMN public.doctor_schedules.is_active IS 'Trạng thái kích hoạt của lịch làm việc';
COMMENT ON COLUMN public.doctor_schedules.created_at IS 'Thời gian tạo lịch làm việc';
COMMENT ON COLUMN public.doctor_schedules.updated_at IS 'Thời gian cập nhật lịch làm việc lần cuối';
COMMENT ON COLUMN public.doctor_schedules.break_duration IS 'Thời gian nghỉ giữa các slot (phút), mặc định 5 phút';
COMMENT ON COLUMN public.doctor_schedules.max_appointments_per_day IS 'Số lượng tối đa lịch hẹn trong một ngày';
COMMENT ON COLUMN public.doctor_schedules.max_appointments_per_slot IS 'Số lượng tối đa lịch hẹn trong một slot, mặc định 1';
COMMENT ON COLUMN public.doctor_schedules.notes IS 'Ghi chú đặc biệt về lịch làm việc';
COMMENT ON COLUMN public.doctor_schedules.timezone IS 'Múi giờ của lịch làm việc, mặc định UTC';


ALTER TABLE ONLY public.doctor_absences
    ADD CONSTRAINT doctor_absences_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.doctor_available_slots
    ADD CONSTRAINT doctor_available_slots_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.doctor_schedules
    ADD CONSTRAINT doctor_schedules_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.doctor_absences
    ADD CONSTRAINT uk_doctor_absences UNIQUE (doctor_user_id, absence_date, start_time, end_time);
ALTER TABLE ONLY public.doctor_schedules
    ADD CONSTRAINT uk_doctor_schedules UNIQUE (doctor_user_id, day_of_week, start_time);
ALTER TABLE ONLY public.doctor_available_slots
    ADD CONSTRAINT uk_doctor_slots UNIQUE (doctor_user_id, slot_date, start_time);

CREATE INDEX doctor_schedules_slot_duration_idx ON public.doctor_schedules USING btree (slot_duration);


ALTER TABLE doctor_available_slots 
ADD COLUMN version BIGINT DEFAULT 0,
ADD COLUMN reserved_by UUID,
ADD COLUMN reserved_at TIMESTAMP;

-- Tạo index cho optimistic locking
CREATE INDEX idx_slot_version ON doctor_available_slots(id, version);

-- Tạo table mới slot_reservations
CREATE TABLE slot_reservations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slot_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    idempotency_key VARCHAR(100) NOT NULL UNIQUE,
    reserved_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    confirmed BOOLEAN NOT NULL DEFAULT false,
    confirmed_at TIMESTAMP,
    cancellation_reason VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE UNIQUE INDEX idx_idempotency_key ON slot_reservations(idempotency_key);
CREATE INDEX idx_slot_active ON slot_reservations(slot_id, active);
CREATE INDEX idx_expires_at ON slot_reservations(expires_at, active);
CREATE INDEX idx_patient_active ON slot_reservations(patient_id, active);

-- Foreign key (optional, nếu muốn referential integrity)
ALTER TABLE slot_reservations 
ADD CONSTRAINT fk_slot_reservations_slot 
FOREIGN KEY (slot_id) REFERENCES doctor_available_slots(id) ON DELETE CASCADE;


select *
from doctor_available_slots das 
where das.slot_date > now()

select *
from doctor_available_slots das inner join slot_reservations sr 
on das.id = sr.slot_id 
where das.id = 'fc54e626-1163-4c88-9e49-38f3426dbdaa'

select * from doctor_available_slots das 
where das.id = '599b9b05-cc0b-4487-a87b-300a0e6f6d2a'


select 

8d9a0088-597f-43fb-93bf-879e89be691f


