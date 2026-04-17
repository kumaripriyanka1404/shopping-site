-- 1. Create role (user)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_roles WHERE rolname = 'shop_admin'
    ) THEN
        CREATE ROLE shop_admin
            WITH LOGIN
            PASSWORD 'shop_admin';
    END IF;
END
$$;

-- 2. Create database owned by that role
SELECT 'CREATE DATABASE shopping_site OWNER shop_admin ENCODING ''UTF8'''
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'shopping_site'
)\gexec

-- 3. Connect to the new database (psql only)
\c shopping_site

-- 4. Grant full access on schema
GRANT ALL ON SCHEMA public TO shop_admin;

-- 5. Grant access to existing objects
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO shop_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO shop_admin;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO shop_admin;

-- 6. Grant access to future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON TABLES TO shop_admin;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON SEQUENCES TO shop_admin;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON FUNCTIONS TO shop_admin;