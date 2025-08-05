# Hướng Dẫn Nhanh - Chức Năng Stream

## Bước 1: Cài Đặt Database

Chạy file `create_stream_tables.sql` trong database của bạn để tạo các bảng cần thiết.

## Bước 2: Backend (Spring Boot)

1. **Thêm dependency WebSocket vào `pom.xml`:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

2. **Copy các file sau vào project:**
   - `src/main/java/com/WebSiteChoiGame/DoAnJ2EE/entity/Stream.java`
   - `src/main/java/com/WebSiteChoiGame/DoAnJ2EE/entity/StreamViewer.java`
   - `src/main/java/com/WebSiteChoiGame/DoAnJ2EE/entity/StreamChat.java`
   - `src/main/java/com/WebSiteChoiGame/DoAnJ2EE/dto/StreamDTO.java`
   - `src/main/java/com/WebSiteChoiGame/DoAnJ2EE/dto/StreamChatDTO.java`
   - `src/main/java/com/WebSiteChoiGame/DoAnJ2EE/repository/StreamRepository.java`
   - `src/main/java/com/WebSiteChoiGame/DoAnJ2EE/repository/StreamViewerRepository.java`
   - `src/main/java/com/WebSiteChoiGame/DoAnJ2EE/repository/StreamChatRepository.java`
   - `src/main/java/com/WebSiteChoiGame/DoAnJ2EE/service/StreamService.java`
   - `src/main/java/com/WebSiteChoiGame/DoAnJ2EE/service/StreamServiceImpl.java`
   - `src/main/java/com/WebSiteChoiGame/DoAnJ2EE/controller/StreamController.java`
   - `src/main/java/com/WebSiteChoiGame/DoAnJ2EE/config/WebSocketConfig.java`

3. **Khởi động Spring Boot:**
```bash
mvn spring-boot:run
```

## Bước 3: Frontend (React)

1. **Copy các component vào project React:**
   - `StreamPlayer.jsx` - Component để stream
   - `LiveStreams.jsx` - Component hiển thị danh sách stream

2. **Cập nhật `GamePage.jsx`:**
   - Thêm imports cho các component stream
   - Thêm tabs cho stream functionality
   - Thêm state và handlers cho stream

3. **Cài đặt dependencies (nếu chưa có):**
```bash
npm install @mui/material @mui/icons-material axios
```

## Bước 4: Test Chức Năng

### Test Stream:
1. Vào trang game bất kỳ
2. Chuyển sang tab "Stream Live"
3. Nhấn "Tạo Stream"
4. Điền tiêu đề và mô tả
5. Nhấn "Bắt đầu Stream"
6. Cho phép trình duyệt capture màn hình

### Test Xem Stream:
1. Vào trang game
2. Chuyển sang tab "Xem Stream"
3. Chọn stream muốn xem
4. Thử gửi tin nhắn chat

## API Testing

### Tạo Stream:
```bash
curl -X POST http://localhost:8080/api/streams/create \
  -H "Content-Type: application/json" \
  -d '{
    "streamerId": 1,
    "gameId": 1,
    "title": "Test Stream",
    "description": "Testing stream functionality"
  }'
```

### Lấy Danh Sách Stream Live:
```bash
curl http://localhost:8080/api/streams/live
```

### Bắt Đầu Stream:
```bash
curl -X POST http://localhost:8080/api/streams/1/start
```

## Lưu Ý Quan Trọng

1. **HTTPS Required:** Chức năng capture màn hình cần HTTPS
2. **Browser Support:** Cần trình duyệt hỗ trợ WebRTC
3. **User Authentication:** Đảm bảo user đã đăng nhập
4. **Network:** Cần kết nối internet ổn định

## Troubleshooting

### Lỗi thường gặp:

1. **"getDisplayMedia is not defined":**
   - Cần HTTPS
   - Kiểm tra browser support

2. **"Stream not found":**
   - Kiểm tra streamId
   - Kiểm tra database connection

3. **"WebSocket connection failed":**
   - Kiểm tra WebSocket config
   - Kiểm tra CORS settings

## Next Steps

Sau khi test thành công, bạn có thể:
1. Thêm authentication cho stream
2. Cải thiện UI/UX
3. Thêm tính năng recording
4. Tối ưu performance
5. Thêm mobile support 