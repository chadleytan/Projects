module project
	(
		CLOCK_50,						//	On Board 50 MHz
		// Your inputs and outputs here
        KEY,
		  LEDR,
		  HEX0,
		  HEX1,
		  HEX2,
		  HEX3,
		  HEX4,
		  HEX5,
		// The ports below are for the VGA output.  Do not change.
		VGA_CLK,   						//	VGA Clock
		VGA_HS,							//	VGA H_SYNC
		VGA_VS,							//	VGA V_SYNC
		VGA_BLANK_N,						//	VGA BLANK
		VGA_SYNC_N,						//	VGA SYNC
		VGA_R,   						//	VGA Red[9:0]
		VGA_G,	 						//	VGA Green[9:0]
		VGA_B   						//	VGA Blue[9:0]
	);

	input			CLOCK_50;				//	50 MHz
	input   [3:0]   KEY;
	output [6:0] HEX0, HEX1, HEX2, HEX3, HEX4, HEX5;
	output [9:0] LEDR;
	
	// Declare your inputs and outputs here
	// Do not change the following outputs
	output			VGA_CLK;   				//	VGA Clock      
	output			VGA_HS;					//	VGA H_SYNC
	output			VGA_VS;					//	VGA V_SYNC
	output			VGA_BLANK_N;				//	VGA BLANK
	output			VGA_SYNC_N;				//	VGA SYNC
	output	[9:0]	VGA_R;   				//	VGA Red[9:0]
	output	[9:0]	VGA_G;	 				//	VGA Green[9:0]
	output	[9:0]	VGA_B;   				//	VGA Blue[9:0]
	
	
	wire resetn;
	assign resetn = KEY[0];
	
	// Create the colour, x, y and writeEn wires that are inputs to the controller.
	wire [2:0] colour;
	wire [7:0] x;
	wire [7:0] y;
	wire writeEn;

	// Create an Instance of a VGA controller - there can be only one!
	// Define the number of colours as well as the initial background
	// image file (.MIF) for the controller.
	vga_adapter VGA(
			.resetn(resetn),
			.clock(CLOCK_50),
			.colour(colour),
			.x(x),
			.y(y),
			.plot(writeEn),
			/* Signals for the DAC to drive the monitor. */
			.VGA_R(VGA_R),
			.VGA_G(VGA_G),
			.VGA_B(VGA_B),
			.VGA_HS(VGA_HS),
			.VGA_VS(VGA_VS),
			.VGA_BLANK(VGA_BLANK_N),
			.VGA_SYNC(VGA_SYNC_N),
			.VGA_CLK(VGA_CLK));
		defparam VGA.RESOLUTION = "160x120";
		defparam VGA.MONOCHROME = "FALSE";
		
		
		defparam VGA.BITS_PER_COLOUR_CHANNEL = 1;
		defparam VGA.BACKGROUND_IMAGE = "black.mif";
			
	// Put your code here. Your code should produce signals x,y,colour and writeEn/plot
	// for the VGA controller, in addition to any other functionality your design may require.
    
	 //Register to calculate the highscore
	 reg [3:0] one_score, ten_score, hund_score, thou_score, tenthou_score, hundthou_score;
	 
	 //HEX display for the high score
	 hex_decoder h0 (one_score, HEX0);
	 hex_decoder h1 (ten_score, HEX1);
	 hex_decoder h2 (hund_score, HEX2);
	 hex_decoder h3 (thou_score, HEX3);
	 hex_decoder h4 (tenthou_score, HEX4);
	 hex_decoder h5 (hundthou_score, HEX5);
	 
	 assign LEDR[0] = DoneIn;
	 assign LEDR[1] = DoneGround;
	 assign LEDR[2] = DoneChar;
	 assign LEDR[3] = DoneOb1;
	 assign LEDR[4] = DoneOb8;
	 assign LEDR[5] = jumping;
	 assign LEDR[6] = DoneWait;
	 assign LEDR[7] = DoneDel;
	 
	 
	 //Used for delay counter
	 reg [19:0] c0;
	 reg [3:0] c1;
	 reg enable_1;
	 
    //Current position of the character
    reg [7:0] X_char;
    reg [6:0] Y_char;
	 reg [5:0] character_counter;

    //Current coordinates to be plotted and colour
    reg [7:0] X_current;
    reg [6:0] Y_current;
	 reg [2:0] C_current;

    //Counter to draw all the objects
    reg [7:0] X_counter;
    reg [6:0] Y_counter;
	 
    reg plot; //Stop game when low
 
    //register for the x and y coordinates of the the obstacle number
    reg [7:0] x1, x2, x3, x4, x5, x6, x7, x8;
    reg [7:0] y1, y2, y3, y4, y5, y6, y7, y8;
    
    //Registers to go to indicate when a state is finished
    reg DoneChar, DoneGround, DoneOb1, DoneOb2, DoneOb3, DoneOb4, DoneOb5, DoneOb6, DoneOb7, DoneOb8, DoneDel, Start, DoneWait, DoneIn; 

    //obstacles previous, current and next
    reg [2:0] p_obstacle; //previous obstacle
    reg [2:0] c_obstacle; //current obstacle
    reg [2:0] n_obstacle; //next obstacle
		
    //height of the current obstacle  
    reg [7:0] y_height;

    //height of the ground level 
    reg [7:0] y_ground;
    
	 reg [2:0] randomObstacle; 
	 
	 reg [6:0] Y_jump;
	 
	 reg  dead, jumping;
	 
	 
	 // assign x,y and colour values for VGA
	 assign x = X_current;
	 assign y = Y_current;
	 assign colour = C_current;
	 assign writeEn = plot;
	 
	 
	 localparam
	 GROUND = 3'b000, //ground level
	 ONE_BLOCK = 3'b001, //one block high
	 TWO_BLOCK = 3'b010, //two block high
	 THREE_BLOCK = 3'b011, //three block high
	 HOLE = 3'b100; //hole to fall in

    always @(posedge CLOCK_50)
    begin
		
		  if (~KEY[1] && ~jumping)
				jumping <= 1'b1;
		
		//Generate random number based on current obstacle
		if (c_obstacle == GROUND && randomObstacle > 3'b010) randomObstacle <= 3'b000;
		else if (c_obstacle == ONE_BLOCK && randomObstacle > 3'b011) randomObstacle <= 3'b000;
		else if (c_obstacle == TWO_BLOCK && randomObstacle > 3'b100) randomObstacle <= 3'b000;
		else if (c_obstacle == THREE_BLOCK && randomObstacle > 3'b100) randomObstacle <= 3'b000;
		else if (c_obstacle == HOLE && randomObstacle > 3'b011) randomObstacle <= 3'b000;
		else randomObstacle <= randomObstacle + 1'b1;
	 

        
			
			// STATE DO STUFF YEAHHHHH
			case(do_state)
			
					S_DO_INITIALIZE:
					begin
						//Initialize coordinates when game starts 
         
						//Initialize the jumping direction
						Y_jump <= 7'b0000000;
						jumping <= 1'b0;
						y_ground <= 7'b1100100;
						plot <= 1'b1;
						
						//Initialize the delay counter
						c0 <= 20'b11001011011100110101;
						c1 <= 4'b0000;
			
						//X and Y coordinates for the character
						X_char <= 8'b00011000;
						Y_char <= 7'b1100010;
						character_counter <= 6'b000000;

						X_counter <= 8'b00000000;
						Y_counter <= 7'b0000000;
						
						//X and Y coordinates for obstacles
						x1 <= 8'b00000000;
						x2 <= 8'b00010011;
						x3 <= 8'b00100111;
						x4 <= 8'b00111011;
						x5 <= 8'b01001111;
						x6 <= 8'b01100011;
						x7 <= 8'b01110111;
						x8 <= 8'b10001011;
				
						//First 8 obstacles will be ground obstacles
						y1 <= 7'b1110100;
						y2 <= 7'b1110100;
						y3 <= 7'b1110100;
						y4 <= 7'b1110100;
						y5 <= 7'b1110100;
						y6 <= 7'b1110100;
						y7 <= 7'b1110100;
						y8 <= 7'b1110100;
			
						//Initialize all done to be 0
						DoneChar <= 1'b0;
						DoneGround <= 1'b0; 
						DoneOb1 <= 1'b0; 
						DoneOb2 <= 1'b0; 
						DoneOb3 <= 1'b0; 
						DoneOb4 <= 1'b0; 
						DoneOb5 <= 1'b0; 
						DoneOb6 <= 1'b0; 
						DoneOb7 <= 1'b0; 
						DoneOb8 <= 1'b0; 
						DoneDel <= 1'b0; 
						DoneWait <= 1'b0;
						
						DoneIn <= 1'b1;
			
			
						//Set score
						one_score <= 4'b0000;
						ten_score <= 4'b0000;
						hund_score <= 4'b0000;
						thou_score <= 4'b0000;
						tenthou_score <= 4'b0000;
						hundthou_score <= 4'b0000;

						dead <= 1’b0;
			
						//Initially ground as the first obstacle
						c_obstacle <= GROUND;
					end //if initialize
					S_DO_GROUND:
					begin
						C_current <= 3'b111;
						//Once done drawing reset counter and indicate that its done
					   if (Y_counter == 7'b0010011)
						begin
							Y_counter <= 7'b0000000;
							X_counter <= 8'b00000000;
							DoneGround <= 1'b1;
							DoneDel <= 1'b0;
						end
						//Increment x and y counters
						else
						begin
							X_current <= X_counter;
							Y_current <= 7'b1110111 - Y_counter ;
						
							if (X_counter == 8'b10011111) 
							begin
								X_counter <= 8'b00000000;
								Y_counter <= Y_counter + 1'b1;
							end
							else X_counter <= X_counter + 1'b1;
							
						end //else block
					end // ground
					
					//DRAW CHARACTER
					S_DO_CHARACTER:
					begin
					   C_current <= 3'b100;
						
						if (character_counter == 6'b100111)
						begin 
							character_counter <= 6'b000000;
							DoneChar <= 1'b1;
						end
						else
						begin
							character_counter <= character_counter + 1'b1;
						end
						
						X_current <= X_char - character_counter[1:0];
						Y_current <= Y_char - character_counter[5:2];
					end // for character
					
					S_DO_CHARACTER_WAIT:
					begin
						if (Y_char > y_ground && Y_char < 7'b1101101) X_char <= X_char - 1'b1; //character is pushed back if blocked
						else if (X_char < 8'b00011000) X_char <= X_char + 1'b1; //character catches up if behind
						if (Y_char < 7'b1100100 || X_char == 8'b00000000) dead <= 1’b1; //player has lost
						
					end
					
					S_DO_JUMP:
					begin
						C_current <= 3'b100;
						if (jumping == 1'b1)
						begin
							if (Y_jump < 5'b11110)
							begin
								Y_jump <= Y_jump + 2'b10;
								jumping <= 1'b1;
								Y_char <= Y_char - 2'b11;
							end
							else
							begin
								jumping <= 1'b0;
								Y_jump <= 5'b00000;
							end
						end // if block
					end // for jumping
					
					S_DO_FALL:
					begin
						if (Y_char < y_ground - 1'b1)
							Y_char <= Y_char + 2'b10;
					end // for falling
						
					S_DO_FIRST_OBS:
					begin
						if (x1 == X_char + 1'b1) y_ground <= y1;
                
						//Object is done drawing 
						if ((y1 + Y_counter) == 7'b1100100 || (y1 - Y_counter) == 7'b1100100) 
						begin
							DoneOb1 <= 1'b1; //indicates that it is done drawing the obstacle
                    
                    //Resets counters
                    Y_counter <= 7'b0000000;
                    X_counter <= 8'b00000000;
						end //if done drawing   
						
						else //drawing the object
						begin
							//Increment y counter if x counter draws 20, otherwise increment x
							if (X_counter == 8'b00010011) 
							begin 
                        X_counter <= 8'b00000000;
                        Y_counter <= Y_counter + 1'b1;
                       
                    end
						  
                    else X_counter <= X_counter+ 1'b1;
                    
						  
                    //Set x and y to be drawn
                    X_current <= x1 + X_counter;
						  
						  if (y1 < 7'b1100100)  
						  begin
							Y_current <= y1 + Y_counter;
							C_current <= 3'b111;
						  end
						  else if (y1 > 7'b1100100)
						  begin
							C_current <= 3'b000;  
							Y_current <= y1 - Y_counter;
						  end
                   
						end // of else
					end // of first obstacle

					S_DO_SECOND_OBS:
					begin
						if (x2 == X_char + 1'b1) y_ground <= y2;
                
						//Object is done drawing 
						if ((y2 + Y_counter) == 7'b1100100 || (y2 - Y_counter) == 7'b1100100) 
						begin
							
							DoneOb2 <= 1'b1; //indicates that it is done drawing the obstacle
                    
                    //Resets counters
                    Y_counter <= 7'b0000000;
                    X_counter <= 8'b00000000;
						end //if done drawing   
						
						else //drawing the object
						begin
							//Increment y counter if x counter draws 20, otherwise increment x
							if (X_counter == 8'b00010011) 
							begin 
                        X_counter <= 8'b00000000;
                        Y_counter <= Y_counter + 1'b1;
                       
                    end
						  
                    else X_counter <= X_counter+ 1'b1;
                    
						  
                    //Set x and y to be drawn
                    X_current <= x2 + X_counter;
						  
						  if (y2 < 7'b1100100)  
						  begin
							Y_current <= y2 + Y_counter;
							C_current <= 3'b111;
						  end
						  else if (y2 > 7'b1100100)
						  begin
							C_current <= 3'b000;  
							Y_current <= y2 - Y_counter;
						  end
                   
						end // of else
					end // of first obstacle

					S_DO_THIRD_OBS:
					begin
						if (x3 == X_char + 1'b1) y_ground <= y3;
                
						//Object is done drawing 
						if ((y3 + Y_counter) == 7'b1100100 || (y3 - Y_counter) == 7'b1100100) 
						begin
							
							DoneOb3 <= 1'b1; //indicates that it is done drawing the obstacle
                    
                    //Resets counters
                    Y_counter <= 7'b0000000;
                    X_counter <= 8'b00000000;
						end //if done drawing   
						
						else //drawing the object
						begin
							//Increment y counter if x counter draws 20, otherwise increment x
							if (X_counter == 8'b00010011) 
							begin 
                        X_counter <= 8'b00000000;
                        Y_counter <= Y_counter + 1'b1;
                       
                    end
						  
                    else X_counter <= X_counter+ 1'b1;
                    
						  
                    //Set x and y to be drawn
                    X_current <= x3 + X_counter;
						  
						  if (y3 < 7'b1100100)  
						  begin
							Y_current <= y3 + Y_counter;
							C_current <= 3'b111;
						  end
						  else if (y3 > 7'b1100100)
						  begin
							C_current <= 3'b000;  
							Y_current <= y3 - Y_counter;
						  end
                   
						end // of else
					end // of first obstacle

					S_DO_FOURTH_OBS:
					begin
						if (x4 == X_char + 1'b1) y_ground <= y4;
                
						//Object is done drawing 
						if ((y4 + Y_counter) == 7'b1100100 || (y4 - Y_counter) == 7'b1100100) 
						begin
							
							DoneOb4 <= 1'b1; //indicates that it is done drawing the obstacle
                    
                    //Resets counters
                    Y_counter <= 7'b0000000;
                    X_counter <= 8'b00000000;
						end //if done drawing   
						
						else //drawing the object
						begin
							//Increment y counter if x counter draws 20, otherwise increment x
							if (X_counter == 8'b00010011) 
							begin 
                        X_counter <= 8'b00000000;
                        Y_counter <= Y_counter + 1'b1;
                       
                    end
						  
                    else X_counter <= X_counter+ 1'b1;
                    
						  
                    //Set x and y to be drawn
                    X_current <= x4 + X_counter;
						  
						  if (y4 < 7'b1100100)  
						  begin
							Y_current <= y4 + Y_counter;
							C_current <= 3'b111;
						  end
						  else if (y4 > 7'b1100100)
						  begin
							C_current <= 3'b000;  
							Y_current <= y4 - Y_counter;
						  end
                   
						end // of else
					end // of first obstacle

					S_DO_FIFTH_OBS:
					begin
						if (x5 == X_char + 1'b1) y_ground <= y5;
                
						//Object is done drawing 
						if ((y5 + Y_counter) == 7'b1100100 || (y5 - Y_counter) == 7'b1100100) 
						begin
							
							DoneOb5 <= 1'b1; //indicates that it is done drawing the obstacle
                    
                    //Resets counters
                    Y_counter <= 7'b0000000;
                    X_counter <= 8'b00000000;
						end //if done drawing   
						
						else //drawing the object
						begin
							//Increment y counter if x counter draws 20, otherwise increment x
							if (X_counter == 8'b00010011) 
							begin 
                        X_counter <= 8'b00000000;
                        Y_counter <= Y_counter + 1'b1;
                       
                    end
						  
                    else X_counter <= X_counter+ 1'b1;
                    
						  
                    //Set x and y to be drawn
                    X_current <= x5+ X_counter;
						  
						  if (y5 < 7'b1100100)  
						  begin
							Y_current <= y5 + Y_counter;
							C_current <= 3'b111;
						  end
						  else if (y5 > 7'b1100100)
						  begin
							C_current <= 3'b000;  
							Y_current <= y5 - Y_counter;
						  end
                   
						end // of else
					end // of first obstacle

					S_DO_SIXTH_OBS:
					begin
						if (x6 == X_char + 1'b1) y_ground <= y6;
                
						//Object is done drawing 
						if ((y6 + Y_counter) == 7'b1100100 || (y6 - Y_counter) == 7'b1100100) 
						begin
							
							DoneOb6 <= 1'b1; //indicates that it is done drawing the obstacle
                    
                    //Resets counters
                    Y_counter <= 7'b0000000;
                    X_counter <= 8'b00000000;
						end //if done drawing   
						
						else //drawing the object
						begin
							//Increment y counter if x counter draws 20, otherwise increment x
							if (X_counter == 8'b00010011) 
							begin 
                        X_counter <= 8'b00000000;
                        Y_counter <= Y_counter + 1'b1;
                       
                    end
						  
                    else X_counter <= X_counter+ 1'b1;
                    
						  
                    //Set x and y to be drawn
                    X_current <= x6 + X_counter;
						  
						  if (y6 < 7'b1100100)  
						  begin
							Y_current <= y6 + Y_counter;
							C_current <= 3'b111;
						  end
						  else if (y6 > 7'b1100100)
						  begin
							C_current <= 3'b000;  
							Y_current <= y6 - Y_counter;
						  end
                   
						end // of else
					end // of first obstacle

					S_DO_SEVENTH_OBS:
					begin
						if (x7 == X_char + 1'b1) y_ground <= y7;
                
						//Object is done drawing 
						if ((y7+ Y_counter) == 7'b1100100 || (y7 - Y_counter) == 7'b1100100) 
						begin
							
							DoneOb7 <= 1'b1; //indicates that it is done drawing the obstacle
                    
                    //Resets counters
                    Y_counter <= 7'b0000000;
                    X_counter <= 8'b00000000;
						end //if done drawing   
						
						else //drawing the object
						begin
							//Increment y counter if x counter draws 20, otherwise increment x
							if (X_counter == 8'b00010011) 
							begin 
                        X_counter <= 8'b00000000;
                        Y_counter <= Y_counter + 1'b1;
                       
                    end
						  
                    else X_counter <= X_counter+ 1'b1;
                    
						  
                    //Set x and y to be drawn
                    X_current <= x7 + X_counter;
						  
						  if (y7 < 7'b1100100)  
						  begin
							Y_current <= y7 + Y_counter;
							C_current <= 3'b111;
						  end
						  else if (y7 > 7'b1100100)
						  begin
							C_current <= 3'b000;  
							Y_current <= y7 - Y_counter;
						  end
                   
						end // of else
					end // of first obstacle
					S_DO_EIGHT_OBS:
					begin
						if (x8 == X_char + 1'b1) y_ground <= y8;
                
						//Object is done drawing 
						if ((y8+ Y_counter) == 7'b1100100 || (y8 - Y_counter) == 7'b1100100) 
						begin
							
							DoneOb8 <= 1'b1; //indicates that it is done drawing the obstacle
                    
                    //Resets counters
                    Y_counter <= 7'b0000000;
                    X_counter <= 8'b00000000;
						end //if done drawing   
						
						else //drawing the object
						begin
							//Increment y counter if x counter draws 20, otherwise increment x
							if (X_counter == 8'b00010011) 
							begin 
                        X_counter <= 8'b00000000;
                        Y_counter <= Y_counter + 1'b1;
                       
                    end
						  
                    else X_counter <= X_counter+ 1'b1;
                    
						  
                    //Set x and y to be drawn
                    X_current <= x8 + X_counter;
						  
						  if (y8 < 7'b1100100)  
						  begin
							Y_current <= y8 + Y_counter;
							C_current <= 3'b111;
						  end
						  else if (y8 > 7'b1100100)
						  begin
							C_current <= 3'b000;  
							Y_current <= y8 - Y_counter;
						  end
                   
						end // of else
					end // of first obstacle

					S_DO_WAIT:
					begin
						if (c1 == 4'b1100)
						begin
							c1 <= 4'b0000;
							DoneWait <= 1'b1;
						end
						else if (c1 < 4'b1100)
						begin
							if (c0 == 20'd0)
							begin
								c0 <= 20'b11001011011100110101;
								c1 <= c1 + 1;
							end
							else
								c0 <= c0 - 1'b1;
						end
					end // wait state
					
					S_DO_DELETE:
					begin
						C_current <= 3'b000;
						
						//Once done drawing reset counter and indicate that its done
					   if (Y_counter == 7'b1011001)
						begin
							Y_counter <= 7'b0000000;
							X_counter <= 8'b00000000;
							
							//Set everything back to 0
							DoneChar <= 1'b0;
							DoneGround <= 1'b0; 
							DoneOb1 <= 1'b0; 
							DoneOb2 <= 1'b0; 
							DoneOb3 <= 1'b0; 
							DoneOb4 <= 1'b0; 
							DoneOb5 <= 1'b0; 
							DoneOb6 <= 1'b0; 
							DoneOb7 <= 1'b0; 
							DoneOb8 <= 1'b0; 
							DoneWait <=1 'b0;
							DoneDel <= 1'b1; 
						end
						
						//Increment x and y counters
						else
						begin
							X_current <= X_counter;
							Y_current <= 7'b1110111 - Y_counter;
						
							if (X_counter == 8'b10011111) 
							begin
								X_counter <= 8'b00000000;
								Y_counter <= Y_counter + 1'b1;
							end
							else X_counter <= X_counter + 1'b1;
							
						end //else block
					end //delete state
					

					S_DO_NEXT_OBSTACLE:
					begin
					//Next obstacle logic for obstacle generator based on randomly generated number
        				case (c_obstacle)
 			   
					GROUND:
					begin
						y_height <= 7'b1100100;
						case(randomObstacle)
							0: n_obstacle <= GROUND;
							1: n_obstacle <= HOLE;
							2: n_obstacle <= ONE_BLOCK;
							default: n_obstacle <= GROUND;
						endcase // Ground
			   		end
				
					ONE_BLOCK:
					begin
						y_height <= 7'b1001111;
						case(randomObstacle)
							0: n_obstacle <= ONE_BLOCK;
							1: n_obstacle <= GROUND;
							2: n_obstacle <= HOLE;
							3: n_obstacle <= TWO_BLOCK;
							default: n_obstacle <= ONE_BLOCK;
						endcase // One Block
					end

					TWO_BLOCK:
					begin
						y_height <= 7'b0111011;
						case(randomObstacle)
							0: n_obstacle <= TWO_BLOCK;
							1: n_obstacle <= GROUND;
							2: n_obstacle <= HOLE;
							3: n_obstacle <= ONE_BLOCK;
							4: n_obstacle <= THREE_BLOCK;
							default: n_obstacle <= TWO_BLOCK;
						endcase //Two Block
					end

					THREE_BLOCK:
					begin
						y_height <= 7'b0100111;
						case(randomObstacle)
							0: n_obstacle <= THREE_BLOCK;
							1: n_obstacle <= GROUND;
							2: n_obstacle <= HOLE;
							3: n_obstacle <= ONE_BLOCK;
							4: n_obstacle <= TWO_BLOCK;
							default: n_obstacle <= THREE_BLOCK;
						endcase //Three Block
					end

					HOLE:
					begin
						y_height <= 7'b1110111;
						case(randomObstacle)
							0:
							begin
								if (p_obstacle == ONE_BLOCK) n_obstacle <= ONE_BLOCK;
								else if (p_obstacle == TWO_BLOCK) n_obstacle <= ONE_BLOCK;
								else if (p_obstacle == THREE_BLOCK) n_obstacle <= ONE_BLOCK;
								else n_obstacle <= GROUND; 
							end

							1:
							begin
								if (p_obstacle == TWO_BLOCK) n_obstacle <= TWO_BLOCK;
								else if (p_obstacle == THREE_BLOCK) n_obstacle <= TWO_BLOCK;
								else n_obstacle <= GROUND;
							end
				
							2:
							begin
								if (p_obstacle == THREE_BLOCK) n_obstacle <= THREE_BLOCK;
								else n_obstacle <= GROUND;
							end
				
							3:
							begin
								if(p_obstacle == THREE_BLOCK) n_obstacle <= HOLE;
							end

							default: n_obstacle <= GROUND;
						endcase // HOLE
					end
				
					default: n_obstacle <= GROUND;
					endcase //obstacles

					end //next obstacle

					S_DO_UPDATE_OBSTACLE:
					begin
					
								
					//Develop the next obstacle if any of the obstacles is done moving across the screen
                        		if (x1 == 0 || x2 == 0 || x3 == 0 || x4 == 0 || x5 == 0 || x6 == 0 || x7 == 0 || x8 == 0)
	               			begin
                        			p_obstacle <= c_obstacle;
	                			c_obstacle <= n_obstacle;
		 
		        			//Add 1 to the score
		 				one_score <= one_score + 1'b1;
					end // for next obstacle
					end //update obs state

					S_DO_UPDATE_COORD:
					begin
       					// X and Y coordinates of the 8 obstacles
        				if (x1 == 0) 
        				begin
            					x1 <= 8'b10110100;
	         				y1 <= y_height;				
        				end

        				if (x2 == 0) 
        				begin
           					x2 <= 8'b10110100;
	        				y2 <= y_height;				
        				end

        				if (x3 == 0) 
        				begin
            					x3 <= 8'b10110100;
						y3 <= y_height;				
        				end

        				if (x4 == 0) 
        				begin
            					x4 <= 8'b10110100;
						y4 <= y_height;				
        				end

        				if (x5 == 0) 
        				begin
            					x5 <= 8'b10110100;
						y5 <= y_height;				
        				end

        				if (x6 == 0) 
        				begin
            					x6 <= 8'b10110100;
						y6 <= y_height;				
        				end

		  			if (x7 == 0) 
        				begin
            					x7 <= 8'b10110100;
	         				y7 <= y_height;				
        				end
	
        				if (x8 == 0) 
       					begin
            					x8 <= 8'b10110100;
	         				y8 <= y_height;				
        				end
					end // update coord state
					
					S_DO_DECREMENT:
					begin
						x1 <= x1 - 1’b1;
						x2 <= x2 - 1’b1;
						x3 <= x3 - 1’b1;
						x4 <= x4 - 1’b1;
						x5 <= x5 - 1’b1;
						x6 <= x6 - 1’b1;
						x7 <= x7 - 1’b1;
						x8 <= x8 - 1’b1;

					end

					default:  X_counter <= X_counter;
		
			endcase // do stuff yeahhhhh
   
			//SCORE CALCULATE
			//Tens
			if (one_score == 4'b1010) 
			begin
				one_score <= 4'b0000;
				ten_score <= ten_score + 1'b1;
			end
			//Hundreds
			if (ten_score == 4'b1010) 
			begin
				ten_score <= 4'b0000;
				hund_score <= hund_score + 1'b1;
			end
			//Thousands
			if (hund_score == 4'b1010) 
			begin
				hund_score <= 4'b0000;
				thou_score <= thou_score + 1'b1;
			end
			//Ten Thousands
			if (thou_score == 4'b1010) 
			begin
				thou_score <= 4'b0000;
				tenthou_score <= tenthou_score + 1'b1;
			end
			//Hundred Thousands
			if (tenthou_score == 4'b1010) 
			begin
				tenthou_score <= 4'b0000;
				hundthou_score <= hundthou_score + 1'b1;
			end
			
	
	end // always block


   //current state and next state
   reg [4:0] do_state, todo_state;
	
   //STATES FOR DRAWING
   localparam S_DO_INITIALIZE = 5'b00000,   
		  S_DO_GROUND = 5'b00001, 
		  S_DO_CHARACTER = 5'b00010,
		  S_DO_CHARACTER_WAIT = 5'b00011,
		  S_DO_JUMP = 5'b00100,
		  S_DO_FALL = 5'b00101,
		  S_DO_FIRST_OBS = 5'b00110,
		  S_DO_SECOND_OBS = 5'b00111,
		  S_DO_THIRD_OBS = 5'b01000,
		  S_DO_FOURTH_OBS = 5'b01001,
		  S_DO_FIFTH_OBS = 5'b01010,
		  S_DO_SIXTH_OBS = 5'b01011,
		  S_DO_SEVENTH_OBS = 5'b01100,
		  S_DO_EIGHT_OBS = 5'b01101,
		  S_DO_WAIT = 5'b01110,
		  S_DO_DELETE = 5'b01111,
		  S_DO_NEXT_OBSTACLE = 5’b10000,
		  S_DO_UPDATE = 5'b10001,
                  S_DO_DECREMENT = 5'b10010;
		  S_DO_ALIVE = 5’b10011;
    

   //NEXT STATE LOGIC      
   always@(*)
	begin: state_table
        case(do_state)
				S_DO_INITIALIZE: todo_state = ~KEY[2] ? S_DO_GROUND : S_DO_INITIALIZE;
            S_DO_GROUND: todo_state = DoneGround ? S_DO_CHARACTER	: S_DO_GROUND; 
            S_DO_CHARACTER: todo_state = DoneChar ? S_DO_CHARACTER_WAIT : S_DO_CHARACTER; 
				S_DO_CHARACTER_WAIT: todo_state = jumping ? S_DO_JUMP : S_DO_FALL;
				S_DO_JUMP: todo_state = S_DO_FALL;
				S_DO_FALL: todo_state = S_DO_FIRST_OBS;
            S_DO_FIRST_OBS: todo_state = DoneOb1 ? S_DO_SECOND_OBS : S_DO_FIRST_OBS; 
				S_DO_SECOND_OBS: todo_state = DoneOb2 ? S_DO_THIRD_OBS : S_DO_SECOND_OBS; 
            S_DO_THIRD_OBS: todo_state = DoneOb3 ? S_DO_FOURTH_OBS : S_DO_THIRD_OBS; 
            S_DO_FOURTH_OBS: todo_state = DoneOb4 ? S_DO_FIFTH_OBS : S_DO_FOURTH_OBS; 
            S_DO_FIFTH_OBS: todo_state = DoneOb5 ? S_DO_SIXTH_OBS : S_DO_FIFTH_OBS; 
				S_DO_SIXTH_OBS: todo_state = DoneOb6 ? S_DO_SEVENTH_OBS : S_DO_SIXTH_OBS; 
				S_DO_SEVENTH_OBS: todo_state = DoneOb7 ? S_DO_EIGHT_OBS : S_DO_SEVENTH_OBS; 
            S_DO_EIGHT_OBS: todo_state = DoneOb8 ? S_DO_WAIT : S_DO_EIGHT_OBS; 
				S_DO_WAIT: todo_state = DoneWait ? S_DO_DELETE: S_DO_WAIT;
				S_DO_DELETE: todo_state = DoneDel ? S_DO_NEXT_OBSTACLE : S_DO_DELETE;
				S_DO_NEXT_OBSTACLE: todo_state = S_DO_UPDATE_OBSTACLE; 
				S_DO_UPDATE_OBSTACLE: todo_state = S_DO_UPDATE_COORD; 
				S_DO_UPDATE_COORD: todo = S_DO_DECREMENT;
				S_DO_DECREMENT: todo_state = S_DO_ALIVE;
				S_DO_ALIVE: todo_state = todo_state = dead ? S_DO_INITIALIZE : S_DO_GROUND;
            default: todo_state = S_DO_INITIALIZE;

        endcase //state logic
   end // always block
	
   //Go to next state
   always@(posedge CLOCK_50)
	begin
	    if(resetn == 1'b0)
	        do_state <= S_DO_INITIALIZE;
	    else
	        do_state <= todo_state;
   end // next state always block



endmodule

//7 Segment display
module hex_decoder(hex_digit, segments);
    input [3:0] hex_digit;
    output reg [6:0] segments;
   
    always @(*)
        case (hex_digit)
            4'h0: segments = 7'b100_0000;
            4'h1: segments = 7'b111_1001;
            4'h2: segments = 7'b010_0100;
            4'h3: segments = 7'b011_0000;
            4'h4: segments = 7'b001_1001;
            4'h5: segments = 7'b001_0010;
            4'h6: segments = 7'b000_0010;
            4'h7: segments = 7'b111_1000;
            4'h8: segments = 7'b000_0000;
            4'h9: segments = 7'b001_1000;
            4'hA: segments = 7'b000_1000;
            4'hB: segments = 7'b000_0011;
            4'hC: segments = 7'b100_0110;
            4'hD: segments = 7'b010_0001;
            4'hE: segments = 7'b000_0110;
            4'hF: segments = 7'b000_1110;   
            default: segments = 7'h7f;
        endcase
endmodule
				