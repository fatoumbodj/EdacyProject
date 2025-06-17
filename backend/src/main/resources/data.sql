-- Utilisateur de démonstration
INSERT INTO users (email, password, first_name, last_name, created_at, updated_at)
VALUES ('demo@example.com', '$2a$10$N.EFrCLrCrIgdrGgpBE3P.dW.Nif8uGMRAMwQXfD.LKfGpJyOTJGy', 'Demo', 'User', NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

-- Livres de démonstration (littérature sénégalaise)
INSERT INTO books (title, author, isbn, category, status, description, publish_year, rating, user_id, created_at, updated_at)
SELECT
  'Une si longue lettre',
  'Mariama Bâ',
  '9782070370221',
  'Littérature',
  'AVAILABLE',
  'Roman épistolaire emblématique de la littérature africaine',
  1979,
  4.8,
  u.id,
  NOW(),
  NOW()
FROM users u WHERE u.email = 'demo@example.com'
ON CONFLICT DO NOTHING;

INSERT INTO books (title, author, isbn, category, status, description, publish_year, rating, user_id, created_at, updated_at)
SELECT
  'L''Aventure ambiguë',
  'Cheikh Hamidou Kane',
  '9782070361946',
  'Littérature',
  'BORROWED',
  'Un classique de la littérature sénégalaise sur la rencontre des cultures',
  1961,
  4.6,
  u.id,
  NOW(),
  NOW()
FROM users u WHERE u.email = 'demo@example.com'
ON CONFLICT DO NOTHING;

INSERT INTO books (title, author, isbn, category, status, description, publish_year, rating, user_id, created_at, updated_at)
SELECT
  'Le Docker noir',
  'Ousmane Sembène',
  '9782070367891',
  'Littérature',
  'AVAILABLE',
  'Premier roman du père du cinéma africain',
  1956,
  4.3,
  u.id,
  NOW(),
  NOW()
FROM users u WHERE u.email = 'demo@example.com'
ON CONFLICT DO NOTHING;

INSERT INTO books (title, author, isbn, category, status, description, publish_year, rating, user_id, created_at, updated_at)
SELECT
  'Xala',
  'Ousmane Sembène',
  '9782070415823',
  'Littérature',
  'RESERVED',
  'Satire sociale sur la bourgeoisie africaine post-coloniale',
  1973,
  4.4,
  u.id,
  NOW(),
  NOW()
FROM users u WHERE u.email = 'demo@example.com'
ON CONFLICT DO NOTHING;

INSERT INTO books (title, author, isbn, category, status, description, publish_year, rating, user_id, created_at, updated_at)
SELECT
  'Le Ventre de l''Atlantique',
  'Fatou Diome',
  '9782253107316',
  'Littérature',
  'AVAILABLE',
  'Roman sur l''immigration et l''identité franco-sénégalaise',
  2003,
  4.5,
  u.id,
  NOW(),
  NOW()
FROM users u WHERE u.email = 'demo@example.com'
ON CONFLICT DO NOTHING;
