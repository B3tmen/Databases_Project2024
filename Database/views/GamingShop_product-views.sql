# ================================== PC COMPONENTS VIEWS ==================================
# --- View for all products of category 'PC components' (including subcategories)
CREATE VIEW vw_pc_components_products AS
WITH RECURSIVE category_hierarchy AS (
    SELECT category_id FROM category WHERE name = 'PC components'
    
    UNION ALL
    
    SELECT c.category_id
    FROM category c
    INNER JOIN category_hierarchy ch ON c.fk_parent_category_id = ch.category_id
)
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
JOIN category_hierarchy ch ON pc.fk_category_id = ch.category_id;

# --- View all CPU's
CREATE VIEW vw_cpu_products AS
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
WHERE pc.fk_category_id = (
    SELECT category_id
    FROM category
    WHERE name = 'CPU'
);

# --- View all GPU's
CREATE VIEW vw_gpu_products AS
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
WHERE pc.fk_category_id = (
    SELECT category_id
    FROM category
    WHERE name = 'GPU'
);

# --- View all RAM
CREATE VIEW vw_ram_products AS
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
WHERE pc.fk_category_id = (
    SELECT category_id
    FROM category
    WHERE name = 'RAM'
);

# --- View all storage products (HDD's and SSD's)
CREATE VIEW vw_storage_products AS
WITH RECURSIVE storage_category_hierarchy AS (
    SELECT category_id FROM category WHERE name = 'Storage'
    
    UNION ALL
    
    SELECT c.category_id
    FROM category c
    INNER JOIN storage_category_hierarchy sch ON c.fk_parent_category_id = sch.category_id
)
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
JOIN storage_category_hierarchy sch ON pc.fk_category_id = sch.category_id;

# --- View all motherboards
CREATE VIEW vw_motherboard_products AS
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
WHERE pc.fk_category_id = (
    SELECT category_id
    FROM category
    WHERE name = 'Motherboard'
);

# ================================== PC COMPONENTS VIEWS ==================================


# ================================== PC PERIPHERALS VIEWS ==================================
# --- View all PC peripherals
CREATE VIEW vw_pc_peripherals_products AS
WITH RECURSIVE pc_peripherals_hierarchy AS (
	SELECT category_id FROM category WHERE name = 'PC peripherals'
    
    UNION ALL
    
    SELECT c.category_id FROM category c 
    INNER JOIN pc_peripherals_hierarchy pph ON c.fk_parent_category_id = pph.category_id
)
SELECT p.*
FROM product p
INNER JOIN product_category pc ON p.product_id = pc.fk_product_id
INNER JOIN pc_peripherals_hierarchy pph ON pc.fk_category_id = pph.category_id;

# --- View all keyboard products
CREATE VIEW vw_keyboard_products AS
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
WHERE pc.fk_category_id = (
    SELECT category_id
    FROM category
    WHERE name = 'Keyboards'
);

# --- View all mice products
CREATE VIEW vw_mice_products AS
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
WHERE pc.fk_category_id = (
    SELECT category_id
    FROM category
    WHERE name = 'Mice'
);

# --- View all Headphones products
CREATE VIEW vw_headphones_products AS
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
WHERE pc.fk_category_id = (
    SELECT category_id
    FROM category
    WHERE name = 'Headphones'
);

# ================================== PC PERIPHERALS VIEWS ==================================

# ================================== CONSOLE VIEWS ==================================
# --- View all consoles
CREATE VIEW vw_consoles_products AS
WITH RECURSIVE pc_consoles_hierarchy AS (
	SELECT category_id FROM category WHERE name = 'Consoles'
    
    UNION ALL
    
    SELECT c.category_id FROM category c 
    INNER JOIN pc_consoles_hierarchy pch ON c.fk_parent_category_id = pch.category_id
)
SELECT p.*
FROM product p
INNER JOIN product_category pc ON p.product_id = pc.fk_product_id
INNER JOIN pc_consoles_hierarchy pch ON pc.fk_category_id = pch.category_id;

# --- View all playstation products
CREATE VIEW vw_playstation_products AS
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
WHERE pc.fk_category_id = (
    SELECT category_id
    FROM category
    WHERE name = 'Playstation'
);

# --- View all xbox products
CREATE VIEW vw_xbox_products AS
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
WHERE pc.fk_category_id = (
    SELECT category_id
    FROM category
    WHERE name = 'XBox'
);

# ================================== CONSOLE VIEWS ==================================

# ================================== GAMES AND CABLES ==================================
# --- View all games
CREATE VIEW vw_games_products AS
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
WHERE pc.fk_category_id = (
    SELECT category_id
    FROM category
    WHERE name = 'Games'
);

# --- View all cables
CREATE VIEW vw_cables_products AS
SELECT p.*
FROM product p
JOIN product_category pc ON p.product_id = pc.fk_product_id
WHERE pc.fk_category_id = (
    SELECT category_id
    FROM category
    WHERE name = 'Cables and adapters'
);
# ================================== GAMES AND CABLES ==================================