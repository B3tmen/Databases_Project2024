ALTER TABLE `category` AUTO_INCREMENT = 1;
# --- Adding categories
CALL usp_AddParentCategory('PC components');  		#1
	CALL usp_AddSubCategory('CPU', 1);
    CALL usp_AddSubCategory('GPU', 1);
    CALL usp_AddSubCategory('RAM', 1);
    CALL usp_AddSubCategory('Storage', 1);			#5
		CALL usp_AddSubCategory('HDD', 5);
		CALL usp_AddSubCategory('SSD', 5);
    CALL usp_AddSubCategory('Motherboard', 1); 
CALL usp_AddParentCategory('PC peripherals');   	#9 
	CALL usp_AddSubCategory('Keyboards', 9);
	CALL usp_AddSubCategory('Mice', 9);
	CALL usp_AddSubCategory('Headphones', 9);
CALL usp_AddParentCategory('Consoles');				#13
	CALL usp_AddSubCategory('Playstation', 13);
	CALL usp_AddSubCategory('XBox', 13);
CALL usp_AddParentCategory('Games');				#16
CALL usp_AddParentCategory('Cables and adapters');	#17