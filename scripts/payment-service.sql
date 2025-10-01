CREATE TABLE public.payments (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    amount numeric(10,2) NOT NULL,
    created_at timestamp(6) without time zone,
    notes character varying(255),
    payment_date timestamp(6) without time zone,
    payment_method character varying(255),
    payment_status character varying(255),
    transaction_id character varying(255),
    updated_at timestamp(6) without time zone,
    appointment_id uuid,
    payment_type character varying(255),
    gateway_transaction_id character varying,
    payment_url character varying,
    gateway_response character varying,
    refunded_amount numeric(10,2),
    refund_transaction_id character varying,
    gateway_refund_id character varying,
    refund_reason text,
    refund_date timestamp without time zone,
    refund_gateway_response text,
    CONSTRAINT payments_check CHECK (((payment_type)::text = ANY (ARRAY[('DEPOSIT'::character varying)::text, ('REMAINING'::character varying)::text, ('FULL'::character varying)::text, ('REFUND'::character varying)::text]))),
    CONSTRAINT payments_payment_method_check CHECK (((payment_method)::text = ANY (ARRAY[('CASH'::character varying)::text, ('CREDIT_CARD'::character varying)::text, ('BANK_TRANSFER'::character varying)::text, ('VNPAY'::character varying)::text]))),
    CONSTRAINT payments_payment_status_check CHECK (((payment_status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('PROCESSING'::character varying)::text, ('COMPLETED'::character varying)::text, ('PAID'::character varying)::text, ('FAILED'::character varying)::text, ('REFUNDED'::character varying)::text, ('CANCELLED'::character varying)::text])))
);
COMMENT ON COLUMN public.payments.id IS 'Khóa chính UUID của giao dịch thanh toán';
COMMENT ON COLUMN public.payments.amount IS 'Số tiền thanh toán (VND)';
COMMENT ON COLUMN public.payments.created_at IS 'Thời gian tạo bản ghi thanh toán';
COMMENT ON COLUMN public.payments.notes IS 'Ghi chú về giao dịch thanh toán';
COMMENT ON COLUMN public.payments.payment_date IS 'Thời gian thực hiện thanh toán';
COMMENT ON COLUMN public.payments.payment_method IS 'Phương thức thanh toán (CASH, CREDIT_CARD, BANK_TRANSFER, VNPAY)';
COMMENT ON COLUMN public.payments.payment_status IS 'Trạng thái thanh toán (PENDING, PAID, FAILED, REFUNDED)';
COMMENT ON COLUMN public.payments.transaction_id IS 'Mã giao dịch từ cổng thanh toán (nếu có)';

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (id);
COMMENT ON COLUMN public.payments.updated_at IS 'Thời gian cập nhật bản ghi thanh toán lần cuối';
COMMENT ON COLUMN public.payments.appointment_id IS 'Khóa ngoại tham chiếu đến lịch hẹn trong bảng appointments';