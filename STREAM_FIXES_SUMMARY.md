# Stream Auto Share Fixes

## Vấn đề ban đầu:
1. **Auto share khi chuyển tab**: Khi chuyển sang tab "Stream Live", component `StreamPlayer` được mount lại và tự động gọi `startScreenCapture`
2. **Share nhiều lần**: Có thể do WebRTC connection bị reset hoặc media stream bị mất
3. **Share liên tục**: Lần đầu tiên share thì phải share liên tục cỡ 3 lần mới cho share

## Giải pháp đã thực hiện:

### 1. Sửa `VideoStream.jsx`:
- **Thêm prop `shouldStartCapture`**: Chỉ start screen capture khi prop này = true
- **Kiểm tra stream hiện tại**: Trước khi start capture, kiểm tra xem đã có `streamRef.current` chưa
- **Sử dụng `forwardRef`**: Để parent component có thể gọi methods
- **Cải thiện cleanup**: Chỉ stop track khi nó đã ended, không tự động stop khi unmount

### 2. Sửa `StreamPlayer.jsx`:
- **Thêm state `shouldStartCapture`**: Điều khiển khi nào VideoStream nên start capture
- **Chỉ start capture khi ấn nút**: Không tự động start khi mount component
- **Cải thiện logic start/stop**: 
  - `startStream()`: Set `shouldStartCapture = true`
  - `stopStream()`: Set `shouldStartCapture = false` và cleanup media stream

### 3. Logic mới:
```
1. User vào tab "Stream Live" → StreamPlayer mount → KHÔNG auto share
2. User ấn "Bắt đầu Stream" → setShouldStartCapture(true) → VideoStream start capture
3. User chuyển tab → StreamPlayer unmount → Media stream vẫn chạy
4. User quay lại tab → StreamPlayer mount → shouldStartCapture=false → KHÔNG share lại
5. User ấn "Dừng Stream" → setShouldStartCapture(false) → Stop media stream
```

## Kết quả mong đợi:
- ✅ Chỉ share màn hình 1 lần khi ấn nút "Bắt đầu Stream"
- ✅ Không bị bắt share lại khi chuyển tab
- ✅ Stream chỉ dừng khi ấn nút "Dừng Stream"
- ✅ Không bị share nhiều lần liên tiếp

## Files đã sửa:
1. `VideoStream.jsx` - Thêm logic kiểm tra và prop shouldStartCapture
2. `StreamPlayer.jsx` - Thêm state shouldStartCapture và cải thiện logic start/stop 