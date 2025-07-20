-- Thêm cột gameUrl và gameType vào bảng Games
ALTER TABLE Games ADD GameUrl NVARCHAR(500);
ALTER TABLE Games ADD GameType NVARCHAR(50);

-- Thêm game "Rắn săn mồi" vào database
INSERT INTO Games (Title, Description, ThumbnailUrl, GameUrl, GameType, GenreID, CreatedBy, IsApproved, IsVisible, CreatedAt)
VALUES (
    'Rắn Săn Mồi',
    'Game rắn săn mồi cổ điển với đồ họa đẹp mắt. Điều khiển rắn ăn thức ăn để phát triển và tránh va chạm với tường hoặc chính mình.',
    '/uploads/snake-game-thumbnail.jpg',
    '/games/snake-game.html',
    'snake',
    1, -- Giả sử GenreID = 1 là "Giải đố"
    1, -- Giả sử UserID = 1 là admin
    1, -- IsApproved = true
    1, -- IsVisible = true
    GETDATE()
);

-- Cập nhật các game hiện có để có gameType mặc định
UPDATE Games 
SET GameType = 'placeholder' 
WHERE GameType IS NULL; 