CREATE TABLE public.user_profiles (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    address character varying(255),
    avatar_url character varying(255),
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    date_of_birth date,
    first_name character varying(100) NOT NULL,
    gender character varying(255),
    last_name character varying(100) NOT NULL,
    phone character varying(20),
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    user_id uuid,
    CONSTRAINT user_profiles_gender_check CHECK (((gender)::text = ANY (ARRAY[('MALE'::character varying)::text, ('FEMALE'::character varying)::text])))
);
COMMENT ON COLUMN public.user_profiles.address IS 'Địa chỉ đầy đủ của người dùng';
COMMENT ON COLUMN public.user_profiles.avatar_url IS 'URL ảnh đại diện của người dùng';
COMMENT ON COLUMN public.user_profiles.created_at IS 'Thời gian tạo hồ sơ';
COMMENT ON COLUMN public.user_profiles.date_of_birth IS 'Ngày sinh của người dùng';
COMMENT ON COLUMN public.user_profiles.first_name IS 'Họ của người dùng';
COMMENT ON COLUMN public.user_profiles.gender IS 'Giới tính (MALE, FEMALE, OTHER)';
COMMENT ON COLUMN public.user_profiles.last_name IS 'Tên của người dùng';
COMMENT ON COLUMN public.user_profiles.phone IS 'Số điện thoại liên lạc';
COMMENT ON COLUMN public.user_profiles.updated_at IS 'Thời gian cập nhật hồ sơ lần cuối';

CREATE TABLE public.specialties (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    created_at timestamp(6) without time zone,
    description text,
    is_active boolean,
    name character varying(100) NOT NULL,
    updated_at timestamp(6) without time zone
);
COMMENT ON COLUMN public.specialties.created_at IS 'Thời gian tạo chuyên khoa';
COMMENT ON COLUMN public.specialties.description IS 'Mô tả chi tiết về chuyên khoa';
COMMENT ON COLUMN public.specialties.is_active IS 'Trạng thái kích hoạt của chuyên khoa';
COMMENT ON COLUMN public.specialties.name IS 'Tên chuyên khoa (Tim mạch, Nhi khoa, Thần kinh, v.v.)';
COMMENT ON COLUMN public.specialties.updated_at IS 'Thời gian cập nhật chuyên khoa lần cuối';

CREATE TABLE public.medical_profiles (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    license_number character varying(50),
    specialty_id uuid,
    qualification character varying(255),
    years_of_experience integer,
    consultation_fee numeric(10,2),
    bio character varying(255),
    is_doctor_approved boolean DEFAULT false,
    blood_type character varying(255),
    allergies character varying(255),
    medical_history character varying(255),
    emergency_contact_name character varying(100),
    emergency_contact_phone character varying(20),
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON COLUMN public.medical_profiles.license_number IS 'Số giấy phép hành nghề (chỉ dành cho bác sĩ)';
COMMENT ON COLUMN public.medical_profiles.specialty_id IS 'Khóa ngoại tham chiếu đến chuyên khoa (chỉ dành cho bác sĩ)';
COMMENT ON COLUMN public.medical_profiles.qualification IS 'Trình độ chuyên môn và bằng cấp của bác sĩ';
COMMENT ON COLUMN public.medical_profiles.years_of_experience IS 'Số năm kinh nghiệm hành nghề';
COMMENT ON COLUMN public.medical_profiles.consultation_fee IS 'Phí khám bệnh (VND)';
COMMENT ON COLUMN public.medical_profiles.bio IS 'Tiểu sử và giới thiệu chi tiết của bác sĩ';
COMMENT ON COLUMN public.medical_profiles.is_doctor_approved IS 'Trạng thái phê duyệt của bác sĩ bởi admin';
COMMENT ON COLUMN public.medical_profiles.blood_type IS 'Nhóm máu (chỉ dành cho bệnh nhân)';
COMMENT ON COLUMN public.medical_profiles.allergies IS 'Danh sách các dị ứng (chỉ dành cho bệnh nhân)';
COMMENT ON COLUMN public.medical_profiles.medical_history IS 'Tiền sử bệnh án (chỉ dành cho bệnh nhân)';
COMMENT ON COLUMN public.medical_profiles.emergency_contact_name IS 'Tên người liên hệ khẩn cấp';
COMMENT ON COLUMN public.medical_profiles.emergency_contact_phone IS 'Số điện thoại liên hệ khẩn cấp';
COMMENT ON COLUMN public.medical_profiles.created_at IS 'Thời gian tạo hồ sơ y tế';
COMMENT ON COLUMN public.medical_profiles.updated_at IS 'Thời gian cập nhật hồ sơ y tế lần cuối';


ALTER TABLE ONLY public.medical_profiles
    ADD CONSTRAINT medical_profiles_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.specialties
    ADD CONSTRAINT specialties_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.medical_profiles
    ADD CONSTRAINT uk_medical_profiles_license UNIQUE (license_number);
ALTER TABLE ONLY public.medical_profiles
    ADD CONSTRAINT uk_medical_profiles_user UNIQUE (user_id);
ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT uk_user_profiles_user UNIQUE (user_id);
ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.medical_profiles
    ADD CONSTRAINT fk_medical_profiles_specialty FOREIGN KEY (specialty_id) REFERENCES public.specialties(id) ON DELETE SET NULL;