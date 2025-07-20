-- Cập nhật bảng ScoreBoard để hỗ trợ hệ thống lưu điểm và leaderboard
-- Thêm cột lastPlayed vào bảng ScoreBoard (nếu chưa có)
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'ScoreBoard' AND COLUMN_NAME = 'lastPlayed')
BEGIN
    ALTER TABLE ScoreBoard ADD lastPlayed DATETIME;
END

-- Dọn dẹp duplicate records trong ScoreBoard
-- Giữ lại record có điểm cao nhất cho mỗi cặp user-game
WITH DuplicateScores AS (
    SELECT 
        userID, 
        gameID, 
        scoreID,
        highScore,
        ROW_NUMBER() OVER (
            PARTITION BY userID, gameID 
            ORDER BY highScore DESC, scoreID ASC
        ) as rn
    FROM ScoreBoard
)
DELETE FROM ScoreBoard 
WHERE scoreID IN (
    SELECT scoreID 
    FROM DuplicateScores 
    WHERE rn > 1
);

-- Đảm bảo có index cho việc tìm kiếm nhanh
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_ScoreBoard_User_Game')
BEGIN
    CREATE INDEX IX_ScoreBoard_User_Game ON ScoreBoard (userID, gameID);
END

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_ScoreBoard_HighScore_Desc')
BEGIN
    CREATE INDEX IX_ScoreBoard_HighScore_Desc ON ScoreBoard (highScore DESC);
END

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_ScoreBoard_Game_HighScore_Desc')
BEGIN
    CREATE INDEX IX_ScoreBoard_Game_HighScore_Desc ON ScoreBoard (gameID, highScore DESC);
END

-- Hiển thị cấu trúc bảng ScoreBoard
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'ScoreBoard'
ORDER BY ORDINAL_POSITION;

-- Hiển thị các index của bảng ScoreBoard
SELECT 
    i.name AS IndexName,
    i.type_desc AS IndexType,
    STRING_AGG(c.name, ', ') AS ColumnNames
FROM sys.indexes i
INNER JOIN sys.index_columns ic ON i.object_id = ic.object_id AND i.index_id = ic.index_id
INNER JOIN sys.columns c ON ic.object_id = c.object_id AND ic.column_id = c.column_id
WHERE i.object_id = OBJECT_ID('ScoreBoard')
GROUP BY i.name, i.type_desc;

-- Hiển thị thống kê duplicate records (nếu có)
SELECT 
    userID, 
    gameID, 
    COUNT(*) as duplicate_count
FROM ScoreBoard 
GROUP BY userID, gameID 
HAVING COUNT(*) > 1
ORDER BY duplicate_count DESC; 