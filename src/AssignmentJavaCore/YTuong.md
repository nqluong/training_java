### Ý tưởng
- Sử dụng mô hình producer-consumer để xử lý file đa luồng tận dụng tiết kiệm bộ nhớ.
- Tách mỗi một dòng trong file thành 4 phần timestamp, level, service, message lưu vào một object LogEntry.
- Sử dụng BlockingQueue để làm hàng đợi giữa producer và consumer.
- Producer đọc file và đưa từng dòng vào BlockingQueue.
- Consumer lấy từng dòng từ BlockingQueue, parse thành LogEntry và kiểm tra điều kiện filter
- Áp dụng filter theo thứ tự: level -> timestamp -> message (dừng luôn nếu có điều kiện không match)
- Tổng hợp kết quả từ các consumer
- Ghi ra file output nếu người dùng lựa chọn

[Producer: LogReader] ---> [readQueue] ---> [Consumers: parse + filter] ---> [writeQueue] ---> [Writer]
