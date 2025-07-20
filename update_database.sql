-- Thêm cột gameUrl và gameType vào bảng Games (nếu chưa có)
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Games' AND COLUMN_NAME = 'GameUrl')
BEGIN
    ALTER TABLE Games ADD GameUrl NVARCHAR(500);
END

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Games' AND COLUMN_NAME = 'GameType')
BEGIN
    ALTER TABLE Games ADD GameType NVARCHAR(50);
END

-- Cập nhật các game hiện có để có gameType mặc định
UPDATE Games 
SET GameType = 'placeholder' 
WHERE GameType IS NULL;

-- Thêm game "Rắn săn mồi" (nếu chưa có)
IF NOT EXISTS (SELECT * FROM Games WHERE Title = 'Rắn Săn Mồi')
BEGIN
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
END

-- Cập nhật game "Rắn Săn Mồi" nếu đã tồn tại
UPDATE Games 
SET GameType = 'snake',
    GameUrl = '/games/snake-game.html',
    Description = 'Game rắn săn mồi cổ điển với đồ họa đẹp mắt. Điều khiển rắn ăn thức ăn để phát triển và tránh va chạm với tường hoặc chính mình.'
WHERE Title = 'Rắn Săn Mồi';

-- Hiển thị kết quả
SELECT GameID, Title, GameType, GameUrl, IsApproved, IsVisible FROM Games; 