-- Tạo bảng ScoreBoard nếu chưa tồn tại
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'ScoreBoard')
BEGIN
    CREATE TABLE ScoreBoard (
        scoreID INT IDENTITY(1,1) PRIMARY KEY,
        userID INT NOT NULL,
        gameID INT NOT NULL,
        highScore INT NOT NULL,
        lastPlayed DATETIME,
        FOREIGN KEY (userID) REFERENCES Users(userID),
        FOREIGN KEY (gameID) REFERENCES Games(gameID)
    );
    
    PRINT 'Bảng ScoreBoard đã được tạo thành công!';
END
ELSE
BEGIN
    PRINT 'Bảng ScoreBoard đã tồn tại!';
END

-- Hiển thị cấu trúc bảng ScoreBoard
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'ScoreBoard'
ORDER BY ORDINAL_POSITION; 