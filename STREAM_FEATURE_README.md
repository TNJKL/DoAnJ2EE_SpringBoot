# Chức Năng Stream Màn Hình - Website Game

## Tổng Quan

Chức năng stream màn hình cho phép người dùng phát trực tiếp màn hình của họ khi chơi game, cho phép người khác xem và tương tác thông qua chat real-time.

## Tính Năng Chính

### 1. Tạo và Quản Lý Stream
- Tạo stream mới với tiêu đề và mô tả
- Bắt đầu/dừng stream
- Capture màn hình tự động
- Hiển thị số lượng người xem real-time

### 2. Xem Stream
- Danh sách stream đang live
- Xem stream theo game cụ thể
- Chat real-time với streamer và người xem khác
- Hiển thị thông tin streamer và game

### 3. Tương Tác
- Gửi tin nhắn chat
- Hiển thị số lượng người xem
- Thông báo khi có người join/leave stream

## Cấu Trúc Database

### Bảng Streams
```sql
CREATE TABLE Streams (
    StreamID INT PRIMARY KEY IDENTITY,
    StreamerID INT NOT NULL,
    GameID INT NOT NULL,
    Title NVARCHAR(200) NOT NULL,
    Description NVARCHAR(500),
    StreamKey NVARCHAR(255) UNIQUE NOT NULL,
    IsLive BIT DEFAULT 0,
    ViewerCount INT DEFAULT 0,
    StartedAt DATETIME,
    EndedAt DATETIME,
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (StreamerID) REFERENCES Users(UserID),
    FOREIGN KEY (GameID) REFERENCES Games(GameID)
);
```

### Bảng StreamViewers
```sql
CREATE TABLE StreamViewers (
    ViewerID INT PRIMARY KEY IDENTITY,
    StreamID INT NOT NULL,
    UserID INT NOT NULL,
    JoinedAt DATETIME DEFAULT GETDATE(),
    LeftAt DATETIME,
    FOREIGN KEY (StreamID) REFERENCES Streams(StreamID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
```

### Bảng StreamChat
```sql
CREATE TABLE StreamChat (
    ChatID INT PRIMARY KEY IDENTITY,
    StreamID INT NOT NULL,
    UserID INT NOT NULL,
    Message NVARCHAR(500) NOT NULL,
    SentAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (StreamID) REFERENCES Streams(StreamID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
```

## API Endpoints

### Stream Management
- `POST /api/streams/create` - Tạo stream mới
- `POST /api/streams/{streamId}/start` - Bắt đầu stream
- `POST /api/streams/{streamId}/stop` - Dừng stream
- `GET /api/streams/{streamId}` - Lấy thông tin stream
- `DELETE /api/streams/{streamId}` - Xóa stream

### Live Streams
- `GET /api/streams/live` - Lấy danh sách stream đang live
- `GET /api/streams/live/game/{gameId}` - Lấy stream đang live theo game
- `GET /api/streams/live/streamer/{streamerId}` - Lấy stream đang live của streamer

### Viewers
- `POST /api/streams/{streamId}/viewers` - Thêm viewer
- `DELETE /api/streams/{streamId}/viewers/{userId}` - Xóa viewer
- `GET /api/streams/{streamId}/viewers/count` - Lấy số lượng viewer

### Chat
- `POST /api/streams/{streamId}/chat` - Gửi tin nhắn chat
- `GET /api/streams/{streamId}/chat` - Lấy tin nhắn chat

## Cài Đặt và Sử Dụng

### Backend (Spring Boot)

1. **Thêm dependencies vào pom.xml:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

2. **Chạy SQL script để tạo bảng:**
```bash
# Chạy file create_stream_tables.sql
```

3. **Khởi động ứng dụng:**
```bash
mvn spring-boot:run
```

### Frontend (React)

1. **Copy các component vào project React:**
   - `StreamPlayer.jsx` - Component chính để stream
   - `LiveStreams.jsx` - Component hiển thị danh sách stream

2. **Cập nhật GamePage.jsx:**
   - Thêm tabs cho stream
   - Import các component stream
   - Thêm logic xử lý stream

3. **Cài đặt dependencies:**
```bash
npm install @mui/material @mui/icons-material axios
```

## Cách Sử Dụng

### Để Stream Game:

1. Vào trang game cụ thể
2. Chuyển sang tab "Stream Live"
3. Nhấn "Tạo Stream" và điền thông tin
4. Nhấn "Bắt đầu Stream" để bắt đầu
5. Cho phép trình duyệt capture màn hình
6. Stream sẽ hiển thị cho người khác xem

### Để Xem Stream:

1. Vào trang game
2. Chuyển sang tab "Xem Stream"
3. Chọn stream muốn xem
4. Có thể chat với streamer và người xem khác

## Tính Năng Nâng Cao

### WebRTC Integration
- Sử dụng WebRTC để stream video real-time
- Peer-to-peer connection cho chất lượng cao
- STUN server để NAT traversal

### Real-time Updates
- WebSocket cho chat real-time
- Polling để cập nhật viewer count
- Auto-refresh danh sách stream

### Security
- Stream key duy nhất cho mỗi stream
- Authentication cho streamer
- Rate limiting cho chat

## Lưu Ý Kỹ Thuật

1. **Browser Compatibility:**
   - Cần HTTPS để sử dụng `getDisplayMedia()`
   - Hỗ trợ WebRTC và WebSocket

2. **Performance:**
   - Stream quality phụ thuộc vào bandwidth
   - Có thể điều chỉnh video quality

3. **Scalability:**
   - Có thể thêm media server (như Ant Media, Wowza)
   - Load balancing cho nhiều stream

## Troubleshooting

### Lỗi thường gặp:

1. **Không thể capture màn hình:**
   - Kiểm tra HTTPS
   - Cho phép quyền truy cập màn hình

2. **Stream không hiển thị:**
   - Kiểm tra WebRTC support
   - Kiểm tra network connection

3. **Chat không hoạt động:**
   - Kiểm tra WebSocket connection
   - Kiểm tra backend API

## Phát Triển Tương Lai

1. **Tính năng có thể thêm:**
   - Stream recording
   - Stream highlights
   - Donation system
   - Stream categories
   - Moderation tools

2. **Cải thiện performance:**
   - Adaptive bitrate streaming
   - CDN integration
   - Better video compression

3. **Mobile support:**
   - Mobile app cho stream
   - Mobile-friendly UI
   - Push notifications 