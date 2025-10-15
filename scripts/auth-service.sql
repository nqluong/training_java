--AUTH_SERVICE

CREATE TABLE public.invalidated_tokens (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    black_listed_at timestamp(6) without time zone,
    expires_at timestamp(6) without time zone NOT NULL,
    ip_address character varying(255),
    reason character varying(255),
    token_hash character varying(255) NOT NULL,
    token_type character varying(255) NOT NULL,
    user_agent character varying(255),
    user_id uuid NOT NULL,
    CONSTRAINT invalidated_tokens_token_type_check CHECK (((token_type)::text = ANY (ARRAY[('REFRESH_TOKEN'::character varying)::text, ('ACCESS_TOKEN'::character varying)::text])))
);
COMMENT ON COLUMN public.invalidated_tokens.black_listed_at IS 'Thời gian token bị đưa vào blacklist';
COMMENT ON COLUMN public.invalidated_tokens.expires_at IS 'Thời gian hết hạn gốc của token';
COMMENT ON COLUMN public.invalidated_tokens.ip_address IS 'Địa chỉ IP khi token bị vô hiệu hóa';
COMMENT ON COLUMN public.invalidated_tokens.reason IS 'Lý do vô hiệu hóa token';
COMMENT ON COLUMN public.invalidated_tokens.token_hash IS 'Hash của token bị vô hiệu hóa';
COMMENT ON COLUMN public.invalidated_tokens.token_type IS 'Loại token (ACCESS_TOKEN, REFRESH_TOKEN)';
COMMENT ON COLUMN public.invalidated_tokens.user_agent IS 'User agent của thiết bị khi token bị vô hiệu hóa';
COMMENT ON COLUMN public.invalidated_tokens.user_id IS 'Khóa ngoại tham chiếu đến người dùng trong bảng users';


CREATE TABLE public.password_reset_tokens (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    created_at timestamp(6) without time zone,
    expires_at timestamp(6) without time zone NOT NULL,
    is_used boolean DEFAULT false,
    token character varying(255) NOT NULL,
    user_id uuid
);
COMMENT ON COLUMN public.password_reset_tokens.created_at IS 'Thời gian tạo token';
COMMENT ON COLUMN public.password_reset_tokens.expires_at IS 'Thời gian hết hạn của token';
COMMENT ON COLUMN public.password_reset_tokens.is_used IS 'Trạng thái sử dụng token (true: đã sử dụng, false: chưa sử dụng)';
COMMENT ON COLUMN public.password_reset_tokens.token IS 'Token ngẫu nhiên để xác thực đặt lại mật khẩu';
COMMENT ON COLUMN public.password_reset_tokens.user_id IS 'Khóa ngoại tham chiếu đến người dùng trong bảng users';

CREATE TABLE public.roles (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    is_active boolean DEFAULT true,
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT roles_name_check CHECK (((name)::text = ANY (ARRAY[('ADMIN'::character varying)::text, ('DOCTOR'::character varying)::text, ('PATIENT'::character varying)::text])))
);
COMMENT ON COLUMN public.roles.name IS 'Tên vai trò (ADMIN, DOCTOR, PATIENT)';
COMMENT ON COLUMN public.roles.description IS 'Mô tả chi tiết về vai trò và quyền hạn';
COMMENT ON COLUMN public.roles.is_active IS 'Trạng thái kích hoạt của vai trò';
COMMENT ON COLUMN public.roles.created_at IS 'Thời gian tạo vai trò';
COMMENT ON COLUMN public.roles.updated_at IS 'Thời gian cập nhật vai trò lần cuối';


CREATE TABLE public.user_roles (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    role_id uuid NOT NULL,
    assigned_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    assigned_by uuid,
    is_active boolean DEFAULT true,
    expires_at timestamp(6) without time zone
);
COMMENT ON COLUMN public.user_roles.assigned_at IS 'Thời gian được gán vai trò';
COMMENT ON COLUMN public.user_roles.assigned_by IS 'ID người dùng thực hiện việc gán vai trò';
COMMENT ON COLUMN public.user_roles.is_active IS 'Trạng thái kích hoạt của vai trò được gán';
COMMENT ON COLUMN public.user_roles.expires_at IS 'Thời gian hết hạn vai trò (null nếu không có hạn)';
CREATE TABLE public.users (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    email character varying(255) NOT NULL,
    is_active boolean DEFAULT true,
    is_email_verified boolean DEFAULT false,
    password_hash character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    username character varying(100) NOT NULL,
    deleted_at timestamp without time zone,
    deleted_by uuid,
    tokens_invalid_before timestamp without time zone
);
COMMENT ON COLUMN public.users.created_at IS 'Thời gian tạo tài khoản';
COMMENT ON COLUMN public.users.email IS 'Địa chỉ email duy nhất của người dùng';
COMMENT ON COLUMN public.users.is_active IS 'Trạng thái hoạt động của tài khoản (true: hoạt động, false: bị khóa)';
COMMENT ON COLUMN public.users.is_email_verified IS 'Trạng thái xác thực email (true: đã xác thực, false: chưa xác thực)';
COMMENT ON COLUMN public.users.password_hash IS 'Mật khẩu đã được mã hóa bằng bcrypt hoặc thuật toán tương tự';
COMMENT ON COLUMN public.users.updated_at IS 'Thời gian cập nhật thông tin tài khoản lần cuối';
COMMENT ON COLUMN public.users.username IS 'Tên đăng nhập duy nhất của người dùng';



ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.password_reset_tokens
    ADD CONSTRAINT password_reset_tokens_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.invalidated_tokens
    ADD CONSTRAINT invalidated_tokens_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.roles
    ADD CONSTRAINT uk_roles_name UNIQUE (name);
ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT uk_user_roles UNIQUE (user_id, role_id);
ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_users_email UNIQUE (email);
ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_users_username UNIQUE (username);


ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk_user_roles_assigned_by FOREIGN KEY (assigned_by) REFERENCES public.users(id);
ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE;
ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;
ALTER TABLE ONLY public.invalidated_tokens
    ADD CONSTRAINT fk_invalidated_token_user FOREIGN KEY (user_id) REFERENCES public.users(id);
ALTER TABLE ONLY public.password_reset_tokens
    ADD CONSTRAINT fk_password_reset_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;



-- USER_PROFILE SERVICE


INSERT INTO public.roles (id,name,description,is_active,created_at,updated_at) VALUES
	 ('495adf9d-7fdf-4886-81f5-c91129ed56f8'::uuid,'ADMIN','Quản trị viên hệ thống',true,'2025-09-15 01:56:07.998621','2025-09-15 01:56:07.998621'),
	 ('20cc8d8d-055b-4077-953f-429af4c95e9d'::uuid,'DOCTOR','Bác sĩ',true,'2025-09-15 01:56:07.998621','2025-09-15 01:56:07.998621'),
	 ('6e64fe22-a593-4c2e-9fad-9ae101a47e82'::uuid,'PATIENT','Bệnh nhân',true,'2025-09-15 01:56:07.998621','2025-09-15 01:56:07.998621');

select count(*) from user



select *
from users u 
where u.id = '8d9a0088-597f-43fb-93bf-879e89be691f'
