-- ========================
-- Coin System Tables
-- ========================

-- Bảng UserCoins để lưu coin của user
CREATE TABLE UserCoins (
    UserID INT PRIMARY KEY,
    CoinAmount INT DEFAULT 1000, -- Coin khởi đầu cho mỗi user
    UpdatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Bảng Challenges để lưu thách đấu
CREATE TABLE Challenges (
    ChallengeID INT PRIMARY KEY IDENTITY,
    ChallengerID INT NOT NULL,
    OpponentID INT NOT NULL,
    GameID INT NOT NULL,
    BetAmount INT NOT NULL, -- Số coin đặt cược
    Status VARCHAR(20) DEFAULT 'pending', -- pending/accepted/declined/active/finished
    StartTime DATETIME,
    EndTime DATETIME,
    ChallengerScore INT,
    OpponentScore INT,
    WinnerID INT,
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (ChallengerID) REFERENCES Users(UserID),
    FOREIGN KEY (OpponentID) REFERENCES Users(UserID),
    FOREIGN KEY (GameID) REFERENCES Games(GameID),
    FOREIGN KEY (WinnerID) REFERENCES Users(UserID)
);

-- Bảng ChallengeBets để lưu đặt cược của người xem
CREATE TABLE ChallengeBets (
    BetID INT PRIMARY KEY IDENTITY,
    ChallengeID INT NOT NULL,
    UserID INT NOT NULL,
    BetOnUserID INT NOT NULL, -- Đặt cược cho ai
    BetAmount INT NOT NULL,
    Won BIT, -- Có thắng không
    PayoutAmount INT, -- Số coin nhận được
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (ChallengeID) REFERENCES Challenges(ChallengeID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (BetOnUserID) REFERENCES Users(UserID)
);

-- Bảng CoinTransactions để lưu lịch sử giao dịch coin
CREATE TABLE CoinTransactions (
    TransactionID INT PRIMARY KEY IDENTITY,
    UserID INT NOT NULL,
    TransactionType VARCHAR(50) NOT NULL, -- bet, win, lose, admin_add, admin_subtract
    Amount INT NOT NULL, -- Số coin thay đổi (dương = nhận, âm = mất)
    BalanceAfter INT NOT NULL, -- Số dư sau giao dịch
    Description NVARCHAR(500),
    RelatedChallengeID INT, -- ID thách đấu liên quan (nếu có)
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (RelatedChallengeID) REFERENCES Challenges(ChallengeID)
);

-- Thêm coin cho tất cả user hiện có
INSERT INTO UserCoins (UserID, CoinAmount)
SELECT UserID, 1000 FROM Users
WHERE UserID NOT IN (SELECT UserID FROM UserCoins); 