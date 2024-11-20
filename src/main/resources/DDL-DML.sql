
--CREATE TABLE user (
--user_id bigint AUTO_INCREMENT PRIMARY KEY
--, wallet_balance double not null);

INSERT INTO user(wallet_balance) VALUES ('test_user', 50000);

CREATE TABLE user_wallet (
user_id bigint PRIMARY KEY
, currency varchar(10) PRIMARY KEY not null
, amount double not null
--, FOREIGN KEY (user_id) REFERENCES user(user_id)
);

INSERT INTO user_wallet (user_id, currency, amount) VALUES 
(1, 'USDT', 50000)
, (1, 'ETH', 0)
, (1, 'BTC', 0)
;

CREATE TABLE best_price (
ccy_pair varchar(10) PRIMARY KEY -- 'ETHUSDT' or 'BTCUSDT' 
, date timestamp 
, bid_price double not null
, ask_price double not null
);

CREATE TABLE user_trade (
user_id varchar PRIMARY KEY 
, trade_dt timestamp PRIMARY KEY DEFAULT CURRENT_TIMESTAMP 
, trade_type varchar(10) not null -- 'BUY' or 'SELL'
, ccy_pair varchar(10) not null -- 'ETHUSDT' or 'BTCUSDT' 
, quantity integer not null
, price double not null
, FOREIGN KEY (user_id) REFERENCES user(user_id)
);
