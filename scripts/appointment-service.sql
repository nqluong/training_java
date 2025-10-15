CREATE TABLE public.appointments (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    doctor_user_id uuid NOT NULL,
    patient_user_id uuid NOT NULL,
    appointment_date date NOT NULL,
    consultation_fee numeric(10,2),
    reason character varying(255),
    notes character varying(255),
    doctor_notes character varying(255),
    status character varying(255) DEFAULT 'PENDING'::character varying,
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    slot_id uuid,
    CONSTRAINT appointments_status_check CHECK (((status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('CONFIRMED'::character varying)::text, ('CANCELLED'::character varying)::text, ('REJECTED'::character varying)::text, ('IN_PROGRESS'::character varying)::text, ('COMPLETED'::character varying)::text])))
);
COMMENT ON COLUMN public.appointments.appointment_date IS 'Ngày hẹn khám';
COMMENT ON COLUMN public.appointments.consultation_fee IS 'Phí khám bệnh cho cuộc hẹn này (VND)';
COMMENT ON COLUMN public.appointments.reason IS 'Lý do khám bệnh do bệnh nhân cung cấp';
COMMENT ON COLUMN public.appointments.notes IS 'Ghi chú chung về cuộc hẹn';
COMMENT ON COLUMN public.appointments.doctor_notes IS 'Ghi chú riêng của bác sĩ về cuộc hẹn';
COMMENT ON COLUMN public.appointments.status IS 'Trạng thái cuộc hẹn (PENDING, CONFIRMED, CANCELLED, COMPLETED)';
COMMENT ON COLUMN public.appointments.created_at IS 'Thời gian tạo lịch hẹn';
COMMENT ON COLUMN public.appointments.updated_at IS 'Thời gian cập nhật lịch hẹn lần cuối';

ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT appointments_pkey PRIMARY KEY (id);

CREATE INDEX idx_appointments_slot_id ON public.appointments USING btree (slot_id);


select * from appointments a 
where a.doctor_user_id  = '8b38e6c2-9cf0-46d0-842e-5c7a4fa031f9' or a.patient_user_id = '8b38e6c2-9cf0-46d0-842e-5c7a4fa031f9'

select *
from appointments a 
where a.status = 'CONFIRMED'

ALTER TABLE appointments
ADD COLUMN doctor_name varchar,
ADD COLUMN doctor_email varchar,
add column specialty_name varchar,
add column patient_name varchar,
add column patient_email varchar,
add column patient_phone varchar;

CREATE TABLE public.appointment_saga_state (
    id varchar,
    appointment_id uuid NOT NULL,
    status Varchar NOT NULL,
    current_step varchar NOT NULL,
    failure_reason varchar,
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP
);

select * from appointments a 
where a.appointment_date  = '2025-10-18'

select * from appointments a 
where a.slot_id = '599b9b05-cc0b-4487-a87b-300a0e6f6d2a'

select * from appointment_saga_state ass 