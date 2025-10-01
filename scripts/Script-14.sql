select *
from payments p inner join appointments a on p.appointment_id  = a.id 
inner join doctor_available_slots das on das.id = a.slot_id 
where p.payment_method = 'VNPAY' and p.payment_status = 'REFUNDED'

select *
from payments p 
where p.id = '1d396a62-7569-41a8-9450-af4eb0246eea'

select *
from doctor_available_slots das 
inner join appointments a on das.id  = a.slot_id 
inner join payments p on a.id = p.appointment_id 
where p.id = '136f7d2a-6443-4760-850e-b63b8a6b3577'


select u.id,  up.first_name , up.last_name 
from users u inner join user_roles ur on u.id = ur.user_id 
inner join roles r on ur.role_id = r.id 
inner join user_profiles up on up.user_id = u.id 
where r."name" = 'PATIENT'


select * from notifications n 

select *
from payments p inner join appointments a on p.appointment_id  = a.id 
inner join doctor_available_slots das on das.id = a.slot_id 
inner join users u on u.id = a.doctor_user_id 
inner join user_roles ur on ur.user_id = u.id 
inner join roles r on ur.role_id = r.id 
where a.id ='1b724e98-4e0d-40e5-9f0a-cb40320c9f62'

select *
from appointments a 
where a.id ='0a48691b-b4d8-4460-a45b-18a35dae1ecd'

select *


select *
from doctor_available_slots das 
where das.id = '814b415b-a7c2-4131-aa50-4ecf879d1464'

select *
from doctor_available_slots das
inner join users u on u.id = das.doctor_user_id 
inner join user_roles ur on ur.user_id = u.id 
inner join roles r on ur.role_id = r.id 
where das.doctor_user_id  = '3138379f-83a0-460c-99f1-6c71757e9b0d' and das.slot_date ='2025-09-29'

select mr.id, a.id as appointmentId, a.appointment_date as appointmentDate,
	das.start_time as appointmentTime, a.status as appointmentStatus,
	a.consultation_fee as consultationFee, a.reason as appointmentReason,
	a.notes as appointmentNotes, a.doctor_notes as doctorNotes,
	u.id as doctorId, concat(d.first_name, ' ',d.last_name ) as doctorName,
	u.email as doctorEmail, s."name" as doctorSpecialty, s.id as doctorSpecialtyCode,
	mp.license_number as doctorLicenseNumber, mp.qualification as doctorQualification,
	mp.years_of_experience as doctocYearsOfExperience, mp.bio as doctorBio,
	u2.id as patientId, concat(up2.first_name , ' ', up2.last_name ) as patientName,
	u2.email as patientEmail, up2.phone as patientPhone, up2.date_of_birth as patientDateOfBirth,
	up2.gender as patientGender, mp2.blood_type as patientBloodType, mp2.allergies as patientAllergies,
	mp2.medical_history as patientMedicalHistory, mp2.emergency_contact_name as patientEmergencyContactName,
	mp2.emergency_contact_phone as patientEmergencyContactPhone, mr.diagnosis,
	mr.prescription, mr.test_results , mr.follow_up_notes , mr.created_at , mr.updated_at ,
	concat(d.first_name ,' ', d.last_name ) as createBy,
	concat(d.first_name ,' ', d.last_name ) as lastUpdateBy
FROM medical_records mr 
LEFT JOIN appointments a  on mr.appointment_id = a.id
left join users u on u.id = a.doctor_user_id 
left join user_profiles d on d.user_id = u.id 
left join medical_profiles mp on mp.user_id = u.id 
left join specialties s on s.id = mp.specialty_id 
left join doctor_available_slots das on das.id = a.slot_id 
left join users u2 on u2.id = a.patient_user_id 
left join medical_profiles mp2 on mp2.user_id = u2.id 
left join user_profiles up2 on up2.user_id = u2.id 
where a.id = 'b8943a17-850b-4ba2-890e-a0ad964abd0a'

select mr.id, a.id as appointmentId, a.appointment_date as appointmentDate,
	das.start_time as appointmentTime, a.status as appointmentStatus,
	a.consultation_fee as consultationFee, 
	u.id as doctorId, concat(d.first_name, ' ',d.last_name ) as doctorName,
	s."name" as doctorSpecialty, mp.license_number as doctorLicenseNumber,
	u2.id as patientId, concat(up2.first_name , ' ', up2.last_name ) as patientName,
	cast(extract(year from current_date)- extract(year from up2.date_of_birth) as Integer) as patientAge,
	up2.gender as patientGender,
	CASE WHEN LENGTH(mr.diagnosis) > 100 
                THEN CONCAT(SUBSTRING(mr.diagnosis, 1, 97), '...') 
                ELSE mr.diagnosis END as diagnosisSummary,
           CASE WHEN LENGTH(mr.prescription) > 100 
                THEN CONCAT(SUBSTRING(mr.prescription, 1, 97), '...') 
                ELSE mr.prescription END as prescriptionSummary,
           CASE WHEN mr.test_results IS NOT NULL AND LENGTH(mr.test_results) > 0 THEN true ELSE false END as hasTestResults,
           CASE WHEN mr.follow_up_notes IS NOT NULL AND LENGTH(mr.follow_up_notes) > 0 THEN true ELSE false END as hasFollowUpNotes,
           mr.created_at as createdAt,
           mr.updated_at as updatedAt,
           CONCAT(d.first_name, ' ', d.last_name) as createdByDoctor,
           true as canEdit,
           true as canView,
           CASE WHEN mr.created_at >= (CURRENT_DATE - interval '7 days') THEN true ELSE false END as isRecent
FROM medical_records mr 
LEFT JOIN appointments a  on mr.appointment_id = a.id
left join users u on u.id = a.doctor_user_id 
left join user_profiles d on d.user_id = u.id 
left join medical_profiles mp on mp.user_id = u.id 
left join specialties s on s.id = mp.specialty_id 
left join doctor_available_slots das on das.id = a.slot_id 
left join users u2 on u2.id = a.patient_user_id 
left join medical_profiles mp2 on mp2.user_id = u2.id 
left join user_profiles up2 on up2.user_id = u2.id 
where u.id = '8d9a0088-597f-43fb-93bf-879e89be691f'




alter table appointments
drop CONSTRAINT appointments_status_check 

