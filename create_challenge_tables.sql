-- Tạo bảng Challenge
CREATE TABLE Challenges (
    challengeID INT PRIMARY KEY AUTO_INCREMENT,
    challengerID INT NOT NULL,
    opponentID INT NOT NULL,
    gameID INT NOT NULL,
    betAmount INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    startTime DATETIME,
    endTime DATETIME,
    challengerScore INT,
    opponentScore INT,
    winnerID INT,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (challengerID) REFERENCES Users(userID),
    FOREIGN KEY (opponentID) REFERENCES Users(userID),
    FOREIGN KEY (gameID) REFERENCES Games(gameID),
    FOREIGN KEY (winnerID) REFERENCES Users(userID)
);

-- Tạo bảng ChallengeBet
CREATE TABLE ChallengeBets (
    betID INT PRIMARY KEY AUTO_INCREMENT,
    challengeID INT NOT NULL,
    userID INT NOT NULL,
    betOnUserID INT NOT NULL,
    betAmount INT NOT NULL,
    won BOOLEAN,
    payoutAmount INT DEFAULT 0,
    createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (challengeID) REFERENCES Challenges(challengeID),
    FOREIGN KEY (userID) REFERENCES Users(userID),
    FOREIGN KEY (betOnUserID) REFERENCES Users(userID)
);

-- Tạo index để tối ưu hiệu suất
CREATE INDEX idx_challenges_status ON Challenges(status);
CREATE INDEX idx_challenges_users ON Challenges(challengerID, opponentID);
CREATE INDEX idx_challenge_bets_challenge ON ChallengeBets(challengeID);
CREATE INDEX idx_challenge_bets_user ON ChallengeBets(userID);
CREATE INDEX idx_challenge_bets_unprocessed ON ChallengeBets(challengeID, won) WHERE won IS NULL; 