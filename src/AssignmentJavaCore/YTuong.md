### Ý tưởng
- Sử dụng mô hình producer-consumer để xử lý file đa luồng tận dụng tiết kiệm bộ nhớ.
- Tách mỗi một dòng trong file thành 4 phần timestamp, level, service, message lưu vào một object LogEntry.
- Sử dụng BlockingQueue để làm hàng đợi giữa producer và consumer.
- Producer đọc file và đưa từng dòng vào BlockingQueue.
- Consumer lấy từng dòng từ BlockingQueue, parse thành LogEntry và kiểm tra điều kiện filter
- Áp dụng filter theo thứ tự: level -> timestamp -> message (dừng luôn nếu có điều kiện không match)
- Tổng hợp kết quả từ các consumer rồi ghi ra file output.

### Sơ đồ luồng

[Producer: LogReader] ---> [readQueue] ---> [Consumers: parse + filter] ---> [writeQueue] ---> [Writer: LogWriter]

### Các bước thực hiện
 - Input: file log app.log có 800000 dòng theo cấu trúc `[timestamp] [level] [service] - message`
 - Output: file text chứa kết quả lọc theo điều kiện filter.

1. Đọc file log line by line sử dụng BufferedReader.
2. Sử dụng BlockingQueue để làm hàng đợi giữa producer và consumer.
3. Producer đưa từng dòng vào BlockingQueue.
4. Consumer lấy từng dòng từ BlockingQueue, parse thành LogEntry và kiểm tra điều kiện filter
5. Áp dụng filter theo thứ tự: level -> timestamp -> message (dừng luôn nếu có điều kiện không match)
6. Tổng hợp kết quả từ các consumer
7. Sử dụng logwriter để ghi ra file output.

