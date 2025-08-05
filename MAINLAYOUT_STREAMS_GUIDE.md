# Live Streams trên MainLayout - Hướng dẫn sử dụng

## 🎯 **Tính năng mới:**

### 1. **LiveStreamsSection** - Hiển thị danh sách streamer đang stream
- **Vị trí**: Ngay trên danh sách games trong MainLayout
- **Chức năng**: 
  - Hiển thị tối đa 6 stream đang live
  - Auto refresh mỗi 15 giây
  - Hiển thị viewer count, streamer info, game title
  - Click để xem stream

### 2. **StreamViewerModal** - Modal xem stream
- **Chức năng**:
  - Xem stream trong modal popup
  - Chat real-time với streamer và viewers khác
  - Auto join/leave khi mở/đóng modal
  - Responsive design

## 🎨 **Thiết kế UI:**

### **LiveStreamsSection:**
```
┌─────────────────────────────────────────────────────────┐
│ 📺 Live Streams (3)                    [3 đang stream] │
├─────────────────────────────────────────────────────────┤
│ ┌─────────┐ ┌─────────┐ ┌─────────┐                  │
│ │ [LIVE]  │ │ [LIVE]  │ │ [LIVE]  │                  │
│ │ 👤 User │ │ 👤 User │ │ 👤 User │                  │
│ │ 🎮 Game │ │ 🎮 Game │ │ 🎮 Game │                  │
│ │ 5 viewers│ │ 12 views│ │ 2 views │                  │
│ └─────────┘ └─────────┘ └─────────┘                  │
└─────────────────────────────────────────────────────────┘
```

### **StreamViewerModal:**
```
┌─────────────────────────────────────────────────────────┐
│ Stream Title                    [5 người xem] [✕]     │
├─────────────────────────────────────────────────────────┤
│ ┌─────────────────────────┐ ┌─────────────────────────┐ │
│ │                         │ │ 💬 Chat                 │ │
│ │     Video Stream        │ │ ┌─────────────────────┐ │ │
│ │                         │ │ │ User1: Hello!       │ │ │
│ │                         │ │ │ User2: Nice game!   │ │ │
│ │                         │ │ │ [Type message...]   │ │ │
│ │                         │ │ └─────────────────────┘ │ │
│ └─────────────────────────┘ └─────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
```

## 🔧 **Cách sử dụng:**

### **Để xem stream:**
1. **Vào trang chủ** (MainLayout)
2. **Tìm section "Live Streams"** ở trên danh sách games
3. **Click vào card stream** bạn muốn xem
4. **Modal sẽ mở** với video stream và chat
5. **Chat với streamer** bằng cách nhập tin nhắn
6. **Đóng modal** bằng nút "Đóng" hoặc click bên ngoài

### **Để stream:**
1. **Vào trang game** cụ thể
2. **Chuyển sang tab "Stream Live"**
3. **Tạo stream** và bắt đầu stream
4. **Người khác sẽ thấy** stream của bạn trên MainLayout

## 📁 **Files đã tạo:**

1. **`LiveStreamsSection.jsx`** - Component hiển thị danh sách live streams
2. **`StreamViewerModal.jsx`** - Modal để xem stream và chat
3. **`MainLayout.jsx`** - Đã cập nhật để tích hợp 2 component trên

## 🎯 **Tính năng chính:**

### ✅ **LiveStreamsSection:**
- Auto fetch live streams mỗi 15 giây
- Hiển thị tối đa 6 streams
- Hover effects và animations
- Viewer count với format (1K, 2.5K)
- Streamer avatar và username
- Game title và description

### ✅ **StreamViewerModal:**
- Full-screen modal với video stream
- Real-time chat với streamer
- Auto join/leave viewer
- Responsive design
- Keyboard shortcuts (Enter để gửi chat)

### ✅ **Integration:**
- Tích hợp seamlessly với MainLayout
- Không ảnh hưởng đến performance
- Auto cleanup khi unmount
- Error handling

## 🚀 **Kết quả:**

Bây giờ MainLayout sẽ có:
1. **Section "Live Streams"** ngay trên danh sách games
2. **Click để xem stream** trong modal popup
3. **Chat real-time** với streamer và viewers
4. **UI đẹp mắt** với animations và hover effects
5. **Responsive design** cho mobile và desktop

Người dùng có thể dễ dàng khám phá và xem các stream đang live ngay từ trang chủ mà không cần vào từng game page! 