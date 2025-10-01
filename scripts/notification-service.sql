CREATE TABLE public.notifications (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    created_at timestamp(6) without time zone,
    message character varying(255) NOT NULL,
    sent_at timestamp(6) without time zone,
    status character varying(255),
    title character varying(255) NOT NULL,
    type character varying(255),
    user_id uuid,
    CONSTRAINT notifications_status_check CHECK (((status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('SENT'::character varying)::text, ('FAILED'::character varying)::text]))),
    CONSTRAINT notifications_type_check CHECK (((type)::text = ANY (ARRAY[('EMAIL'::character varying)::text, ('SMS'::character varying)::text, ('PUSH'::character varying)::text])))
);
COMMENT ON COLUMN public.notifications.created_at IS 'Thời gian tạo thông báo';
COMMENT ON COLUMN public.notifications.message IS 'Nội dung chi tiết của thông báo';
COMMENT ON COLUMN public.notifications.sent_at IS 'Thời gian gửi thông báo thành công';
COMMENT ON COLUMN public.notifications.status IS 'Trạng thái gửi thông báo (PENDING, SENT, FAILED)';
COMMENT ON COLUMN public.notifications.title IS 'Tiêu đề thông báo';
COMMENT ON COLUMN public.notifications.type IS 'Loại thông báo (EMAIL, SMS)';
COMMENT ON COLUMN public.notifications.user_id IS 'Khóa ngoại tham chiếu đến người nhận trong bảng users';

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);