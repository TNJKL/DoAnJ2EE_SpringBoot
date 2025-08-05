-- ========================
-- Stream Tables
-- ========================

-- Bảng Streams để lưu thông tin stream
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

-- Bảng StreamViewers để theo dõi người xem
CREATE TABLE StreamViewers (
    ViewerID INT PRIMARY KEY IDENTITY,
    StreamID INT NOT NULL,
    UserID INT NOT NULL,
    JoinedAt DATETIME DEFAULT GETDATE(),
    LeftAt DATETIME,
    FOREIGN KEY (StreamID) REFERENCES Streams(StreamID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Bảng StreamChat để lưu tin nhắn chat
CREATE TABLE StreamChat (
    ChatID INT PRIMARY KEY IDENTITY,
    StreamID INT NOT NULL,
    UserID INT NOT NULL,
    Message NVARCHAR(500) NOT NULL,
    SentAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (StreamID) REFERENCES Streams(StreamID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Bảng StreamLikes để lưu lượt thích stream
CREATE TABLE StreamLikes (
    LikeID INT PRIMARY KEY IDENTITY,
    StreamID INT NOT NULL,
    UserID INT NOT NULL,
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (StreamID) REFERENCES Streams(StreamID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
); 