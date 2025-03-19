-- Var olan veritabanını sil ve yeniden oluştur
DROP DATABASE IF EXISTS fabric;
CREATE DATABASE fabric;

-- Kullanıcı ve yetkileri
GRANT ALL PRIVILEGES ON DATABASE fabric TO fabric;