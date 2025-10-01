CREATE TABLE public.medical_records (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    appointment_id uuid NOT NULL,
    diagnosis character varying(255),
    prescription character varying(255),
    test_results character varying(255),
    follow_up_notes character varying(255),
    created_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone
);
COMMENT ON COLUMN public.medical_records.diagnosis IS 'Chẩn đoán bệnh của bác sĩ';
COMMENT ON COLUMN public.medical_records.prescription IS 'Đơn thuốc được kê';
COMMENT ON COLUMN public.medical_records.test_results IS 'Kết quả các xét nghiệm, chẩn đoán hình ảnh';
COMMENT ON COLUMN public.medical_records.follow_up_notes IS 'Ghi chú về lịch tái khám và theo dõi';
COMMENT ON COLUMN public.medical_records.created_at IS 'Thời gian tạo hồ sơ bệnh án';
COMMENT ON COLUMN public.medical_records.updated_at IS 'Thời gian cập nhật hồ sơ bệnh án lần cuối';

ALTER TABLE ONLY public.medical_records
    ADD CONSTRAINT medical_records_pkey PRIMARY KEY (id);