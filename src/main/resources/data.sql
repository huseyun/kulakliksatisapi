-- Kullanıcı rolleri `ROLE_` öneki olmadan ekleniyor
INSERT INTO user_types (name) VALUES ('ADMIN'); -- ID: 1
INSERT INTO user_types (name) VALUES ('USER');  -- ID: 2
INSERT INTO user_types (name) VALUES ('GUEST'); -- ID: 3

-- Kullanıcılar ID belirtilmeden ve ŞİFRELERİ ENCODE EDİLMİŞ OLARAK ekleniyor
-- 'adminpass' şifresinin BCrypt ile şifrelenmiş hali
INSERT INTO all_users (username, password, email) VALUES ('admin', '$2a$10$AyS7eh/nqFmD2DEuf/9o2u2QJmHHcHbi9/UX5tWm.eZROJljCjACe', 'admin@admin.com');
-- 'sellerpass' şifresinin BCrypt ile şifrelenmiş hali
INSERT INTO all_users (username, password) VALUES ('seller_user', '$2a$12$n/w5SESDTVHTDq.gVzlrPOnqvQLIbmRJNG/3cOH8ShshTISfXQJjS', 'seller@seller.com');
-- 'shopperpass' şifresinin BCrypt ile şifrelenmiş hali
INSERT INTO all_users (username, password) VALUES ('shopper_user', '$2a$12$U.DwRVUfThGSF8xPG14jRO9QJ632WJJNXU81bXyJnOBONE1LH0G7G', 'shopper@shopper.com');


-- Alt tablolar için veriler ekleniyor (ID'ler üstteki sırayla eşleşmeli)
INSERT INTO sellers (id, company_name) VALUES (2, 'Teknoloji Dukkani');
INSERT INTO shoppers (id, first_name, last_name) VALUES (3, 'Ali', 'Veli');


-- Kullanıcılara roller atanıyor (ID'ler üstteki sırayla eşleşmeli)
INSERT INTO user_typelist (user_id, usertype_id) VALUES (1, 1); 
INSERT INTO user_typelist (user_id, usertype_id) VALUES (2, 2); 
INSERT INTO user_typelist (user_id, usertype_id) VALUES (3, 2);