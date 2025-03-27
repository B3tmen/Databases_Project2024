# ===================================================================
# ALTER TABLE `product` AUTO_INCREMENT = 2;
# ALTER TABLE `discount` AUTO_INCREMENT = 1;

/* Reference
	IN p_name VARCHAR(45), 
    IN p_price DECIMAL(6, 2), 
    IN p_quantity_available INT, 
    IN p_description TEXT(4000), 
    IN p_warranty_months INT,
    IN p_image_url VARCHAR(300),
    IN p_fk_discount_id INT, 
    IN p_fk_employee_id INT,
    IN p_fk_manufacturer_id INT
*/

# --- Adding products
# ========= CPU's ===========
CALL usp_AddProduct('Ryzen 5 3600', 149.90, 10, 'AMD CPU 100 100000031box Ryzen 5 3600 6C 12T 4200MHz 36MB 65W AM4 Wraith Stealth.',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\components\\processors\\amd-ryzen-5-3600.jpeg', NULL, 2, 1);	#1
CALL usp_AddProductCategoryJunction(1, 2, @out_fk_product_id);

CALL usp_AddDiscount(50, CURDATE(), DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 7 DAY), '%Y-%m-%d'), 2);
CALL usp_AddProduct('Ryzen 9 7950X', 1259.90, 10, 'AMD Ryzen 9 7950X Hexadeca-core (16 Core) 4.50 GHz Processor, 64 MB L3 Cache, 16 MB L2 Cache, 64-bit Processing, 5.70 GHz Overclocking Speed, 5 nm, Socket AM5, Radeon Graphics Graphics, 170 W, 32 Threads.',
36, NULL, 1, 2, 1);	#2
CALL usp_AddProductCategoryJunction(2, 2, @out_fk_product_id);

CALL usp_AddProduct('Core i9-9900K', 1397.00, 10, 'The Intel i9-9900K is an 8 core, 16 thread, unlocked 9th generation Coffee Lake processor. It has a base / boost clocks of 3.6 / 4.7 GHz and a single-core boost of 5.0 GHz (the highest frequency achieved yet from this class of Intel CPU). It features 16 MB of cache, a 95W TDP and Intel UHD 630 graphics. The 9900K is compatible with the new Z390 chipset, and subject to a BIOS update, is also compatible with the older Z370 chipset.',
36, NULL, NULL, 2, 2);	#3
CALL usp_AddProductCategoryJunction(3, 2, @out_fk_product_id);


# ========= GPU's ===========
CALL usp_AddProduct('NVIDIA Geforce GTX 1650 SUPER', 539.90, 5, 'Powered by GeForce GTX 1650 Super integrated with 4GB GDDR6 WINDFORCE 2x cooling system with alternate spinning fans intuitive controls with a or us engine 4GB 128-bit GDDR5 1 x HDMI, 1 x DisplayPort PCI Express 3.0 x16. Power requirement: 350W.',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\components\\graphics_cards\\gigabyte_gtx-1650-super.png', NULL, 2, 3);	#4
CALL usp_AddProductCategoryJunction(4, 3, @out_fk_product_id);


# ========= RAM ===========
CALL usp_AddProduct('VENGEANCE RGB PRO', 134.90, 20, 'Corsair VENGEANCE RGB PRO DDR4 32GB (2x16GB) 3200MHz CL16 Intel XMP 2.0 iCUE Compatible Computer Memory - Black (CMW32GX4M2E3200C16)',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\components\\ram\\corsair_vengeance-rgb-pro.png', NULL, 2, 8);	#5
CALL usp_AddProductCategoryJunction(5, 4, @out_fk_product_id);


# ========= Storage ===========
CALL usp_AddProduct('2TB WD Black', 179.90, 15, 'Western Digital 2TB WD Black Performance Internal Hard Drive HDD - 7200 RPM, SATA 6 Gb/s, 64 MB Cache, 3.5" - WD2003FZEX',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\components\\storage\\wd_2TB-black.jpg', NULL, 2, 9);	#6
CALL usp_AddProductCategoryJunction(6, 5, @out_fk_product_id);

CALL usp_AddProduct('XPG SX6000 Pro 512GB', 99.90, 15, 'XPG SX6000 Pro 512GB PCIe 3D NAND PCIe Gen3x4 M.2 2280 NVMe 1.3 R/W up to 2100/1500MB/s SSD (ASX6000PNP-512GT-C)',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\components\\storage\\adata_xpg-sx6000-nvme.jpg', NULL, 2, 7);	#7
CALL usp_AddProductCategoryJunction(7, 7, @out_fk_product_id); 


# ========= Motherboards ===========
CALL usp_AddProduct('B650E Aorus Master', 827.90, 10, 'GIGABYTE B650E AORUS Master(AM5/ LGA1718/ AMDB650/ ATX/ 5-YearWarranty/DDR5/ QuadM.2/ PCIe 5.0, USB 3.2 Gen2X2 Type-C/AMD WiFi 6E/ Intel 2.5GbE LAN/Q-Flash Plus/EZ-Latch Plus/Gaming Motherboard)',
60, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\components\\mother_boards\\gigabyte_b650e-aorus-master.jpg', NULL, 2, 3);	#8
CALL usp_AddProductCategoryJunction(8, 8, @out_fk_product_id); 


# ========= Keyboards ===========
CALL usp_AddProduct('ROG Strix Scope II 96 Wireless', 289.90, 15, 'ASUS ROG Strix Scope II 96 Wireless Gaming Keyboard, Tri-Mode Connection, Dampening Foam & Switch-Dampening Pads, Hot-Swappable Pre-lubed ROG NX Snow Switches, PBT Keycaps, RGB-Black',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\peripherals\\keyboards\\asus_rog-strix-scope-ii-96-wireless.jpg', NULL, 2, 4);	#9
CALL usp_AddProductCategoryJunction(9, 10, @out_fk_product_id); 


# ========= Mice ===========
CALL usp_AddProduct('Krypton 500 Gaming Mouse', 54.90, 10, 'Genesis Krypton 500. Element format: Right Hand. Sensoric: Optical, Interface: USB, Motion resolution: 7200dpi, Number of frames per second: 4600 IPS, button type: Press buttons pressoirs, number of keys: 6, Roller type: . Power Supply: Cable. Mouse weight: 98g Color: RGB-black',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\peripherals\\mice\\genesis_krypton-500.jpg', NULL, 2, 11);	#10
CALL usp_AddProductCategoryJunction(10, 11, @out_fk_product_id); 


# ========= Headphones ===========
CALL usp_AddProduct('HyperX Cloud II 7.1 Gaming Headset - Red', 143.90, 10, 'HyperX Cloud II features a digitally enhanced, noise-cancelling microphone with automatic gain control function and echo cancelling enabled via the USB sound board. It provides clearer voice quality and reduced background noise, where voice volume automatically increases as in-game sound gets louder, to optimize team communication and in-game chat in intense battles. HyperX Cloud II is certified by TeamSpeak and optimised for Skype and other leading chat applications.',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\peripherals\\headphones\\hp_hyperx-cloud-ii.jpg', NULL, 2, 11);	#11
CALL usp_AddProductCategoryJunction(11, 12, @out_fk_product_id); 


# ========= Playstation ===========
CALL usp_AddProduct('PlayStation 5 Slim D chassis', 1249.00, 5, 'The PS5 console unleashes new gaming possibilities that you never anticipated. Experience lightning fast loading with an ultra-high speed SSD, deeper immersion with support for haptic feedback, adaptive triggers, and 3D Audio*, and an all-new generation of incredible PlayStation games. Lightning Speed - Harness the power of a custom CPU, GPU, and SSD with Integrated I/O that rewrite the rules of what a PlayStation console can do. ',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\consoles\\playstation\\sony_ps5-slim-d.png', NULL, 2, 15);	#12
CALL usp_AddProductCategoryJunction(12, 14, @out_fk_product_id); 


# ========= XBOX ===========
CALL usp_AddProduct('Xbox Series X', 899.90, 5, 'Microsoft Xbox Series X - Includes Xbox Wireless Controller - Up to 120 frames per second - 16GB RAM 1TB SSD - Experience True 4K Gaming',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\consoles\\xbox\\microsoft_xbox-series-x.png', NULL, 2, 16);	#13
CALL usp_AddProductCategoryJunction(13, 15, @out_fk_product_id); 


# ========= Games ===========
CALL usp_AddProduct('Helldivers 2', 77.90, 10, 'HELLDIVERS 2 is a 3rd person squad-based shooter that sees the elite forces of the Helldivers battling to win an intergalactic struggle to rid the galaxy of the rising alien threats.',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\games\\sony_helldivers2.png', NULL, 2, 15);	#14
CALL usp_AddProductCategoryJunction(14, 16, @out_fk_product_id); 

CALL usp_AddProduct('ALAN WAKE 2', 97.90, 10, 'Alan Wake 2 is a 2023 survival horror video game developed by Remedy Entertainment and published by Epic Games Publishing. The sequel to Alan Wake (2010) and Controls AWE expansion (2019), the story follows best-selling novelist Alan Wake, who has been trapped in an alternate dimension for 13 years, as he attempts to escape by writing a horror story involving an FBI Special Agent named Saga Anderson.',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\games\\remedy_alan-wake-2.png', NULL, 2, 18);	#15
CALL usp_AddProductCategoryJunction(15, 16, @out_fk_product_id); 

# ========= Cables ===========
CALL usp_AddProduct('Highwings 4K 8K 10K HDMI Cable', 19.90, 20, 'Highwings 4K 8K 10K HDMI Cable 48Gbps 6.6FT/2M, Certified Ultra High Speed HDMI® Cable Braided Cord-4K@120Hz 8K@60Hz, DTS:X, HDCP 2.2 & 2.3, HDR 10 Compatible with Roku TV/PS5/HDTV/Blu-ray',
36, 'D:\\SKOLA MOODLE\\ETF\\GRADIVO PO GODINAMA\\Treca godina\\VI semestar\\Baze podataka\\Projektni\\2024\\GamingShop\\src\\org\\unibl\\etf\\resources\\media\\images\\products\\cables\\highwings_HDMI-4k-8k-10k.jpg', NULL, 2, 6);	#16
CALL usp_AddProductCategoryJunction(16, 17, @out_fk_product_id); 