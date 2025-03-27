# --- Adding manufacturers
#ALTER TABLE `manufacturer` AUTO_INCREMENT = 1;
# CPU manufacturers
CALL usp_AddManufacturer('AMD');
CALL usp_AddManufacturer('Intel');
# GPU + motherboard manufacturers
CALL usp_AddManufacturer('GIGABYTE');
CALL usp_AddManufacturer('Asus');
CALL usp_AddManufacturer('ZOTAC');
# storage+RAM manufacturers
CALL usp_AddManufacturer('Kingston');
CALL usp_AddManufacturer('ADATA');
CALL usp_AddManufacturer('CORSAIR');
CALL usp_AddManufacturer('Western Digital');

# keyboard manufacturers (has asus from gpu)
CALL usp_AddManufacturer('Dell');
# mouse+headphone manufacturers
CALL usp_AddManufacturer('Genesis');
CALL usp_AddManufacturer('Razer');
CALL usp_AddManufacturer('HyperX');
CALL usp_AddManufacturer('Beats Electronics');

# console manufactures
CALL usp_AddManufacturer('Sony');
CALL usp_AddManufacturer('Microsoft Gaming'); #xbox

# game manufacturers
CALL usp_AddManufacturer('CD Projekt Red');
CALL usp_AddManufacturer('Remedy');
CALL usp_AddManufacturer('Rockstar');