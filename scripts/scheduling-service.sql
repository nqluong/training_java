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

