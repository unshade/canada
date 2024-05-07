STR_OUT      FILL    0x1000
unDebut
	 STMFD   R13!, {R10, LR} ; Save caller's frame pointer and return ASM address
	 MOV     R10, R13 ; Set up new static link
	 SUB     R13, R13, #4
	 STR     R11, [R13]
	 MOV     R11, R13 ; Set up new frame pointer
	 SUB     R13, R13, #4 ; Save space for choix in stack-frame
	 MOV     R0, #5 ; Load literal value in R0
	 STMFD   R13!, {R0} ; Save argument
	 MOV     R0, #10 ; Load literal value in R0
	 STMFD   R13!, {R0} ; Save argument
	 SUB     R13, R13, #4 ; Save space for return value
	 MOV     R9, R10
	 BL      perimetreRectangle ; Branch link to perimetreRectangle (it will save the return address in LR)
	 LDR     R0, [R13] ; Load return value
	 ADD     R13, R13, #4 * 3 ; Remove arguments and return value from stack
	 MOV     R9, R11
	 SUB     R9, R9, #4
	 STR     R0, [R9] ; Assign right expression (assuming result is in R0) to left variable choix
	 END     ; Program ends here
perimetreRectangle
	 STMFD   R13!, {R10, LR} ; Save caller's frame pointer and return ASM address
	 MOV     R10, R9 ; Set up new static link
	 SUB     R13, R13, #4
	 STR     R11, [R13]
	 MOV     R11, R13 ; Set up new frame pointer
	 LDR     R5, [R11, #4 * 5] ; Load parameter larg in R5
	 STMFD   R13!, {R5} ; Store parameter larg in stack-frame
	 LDR     R5, [R11, #4 * 4] ; Load parameter long in R5
	 STMFD   R13!, {R5} ; Store parameter long in stack-frame
	 SUB     R13, R13, #4 ; Save space for p in stack-frame
	 MOV     R0, #3 ; Load literal value in R0
	 STMFD   R13!, {R0} ; Store the right operand in the stack
	 MOV     R0, #2 ; Load literal value in R0
	 LDMFD   R13!, {R1} ; Load the right operand in R1
	 ADD     R0, R0, R1 ; Add operands
	 STMFD   R13!, {R0} ; Store the right operand in the stack
	 MOV     R0, #1 ; Load literal value in R0
	 LDMFD   R13!, {R1} ; Load the right operand in R1
	 ADD     R0, R0, R1 ; Add operands
	 MOV     R9, R11
	 SUB     R9, R9, #12
	 STR     R0, [R9] ; Assign right expression (assuming result is in R0) to left variable p
	 MOV     R0, #65 ; Load literal value in R0
	 SUB SP, SP, #4   ; réservez 4 octets pour le 0
	 MOV R1, #0
	 STR R1, [SP]
	 SUB SP, SP, #4   ; réservez 4 octets pour la valeur (ou plus)
	 STR R0, [SP]     ; stockez la valeur
	 MOV R0, SP       ; adresse de la valeur (ici SP, mais peut être n'importe quelle adresse)
	 BL println
	 ADD SP, SP, #8   ; libérez la pile
	 MOV     R9, R11
	 SUB     R9, R9, #12
	 LDR     R0, [R9] ; Load variable p in R0
	 STR     R0, [R11, #4 * 3] ; Store return value for in stack-frame
	 MOV     R13, R11 ; Restore frame pointer
	 LDR     R11, [R13] ; Restore caller's frame pointer
	 ADD     R13, R13, #4 ; Remove return value from stack
	 LDMFD   R13!, {R10, PC} ; Restore caller's frame pointer and return ASM address
println
	 STMFD   SP!, {LR, R0-R3}
	 MOV     R3, R0
	 LDR     R1, =STR_OUT ; address of the output buffer
PRINTLN_LOOP
	 LDRB    R2, [R0], #1
	 STRB    R2, [R1], #1
	 TST     R2, R2
	 BNE     PRINTLN_LOOP
	 MOV     R2, #10
	 STRB    R2, [R1, #-1]
	 MOV     R2, #0
	 STRB    R2, [R1]

;  we need to clear the output buffer
	 LDR     R1, =STR_OUT
	 MOV     R0, R3
CLEAN
	 LDRB    R2, [R0], #1
	 MOV     R3, #0
	 STRB    R3, [R1], #1
	 TST     R2, R2
	 BNE     CLEAN
;  clear 3 more
	 STRB    R3, [R1], #1
	 STRB    R3, [R1], #1

 STRB    R3, [R1], #1

	 LDMFD   SP!, {PC, R0-R3}
to_ascii
	 STMFD   SP!, {LR, R4-R7}
	 ; make it positive
	 MOV R7, R0
	 CMP     R0, #0
	 MOVGE   R6, R0
	 RSBLT   R6, R0, #0
	 MOV     R0, R6

	 MOV     R4, #0 ; Initialize digit counter

to_ascii_loop
	 MOV     R1, R0
	 MOV     R2, #10
	 BL      div32 ; R0 = R0 / 10, R1 = R0 % 10
	 ADD     R1, R1, #48 ; Convert digit to ASCII
	 STRB    R1, [R3, R4] ; Store the ASCII digit
	 ADD     R4, R4, #1 ; Increment digit counter
	 CMP     R0, #0
	 BNE     to_ascii_loop

	 ; add the sign if it was negative
	 CMP     R7, #0
	 MOVGE   R1, #0
	 MOVLT   R1, #45
	 STRB    R1, [R3, R4]
	 ADD     R4, R4, #1

	 LDMFD   SP!, {PC, R4-R7}

;       Integer division routine
;       Arguments:
;       R1 = Dividend
;       R2 = Divisor
;       Returns:
;       R0 = Quotient
;       R1 = Remainder
div32
	 STMFD   SP!, {LR, R2-R5}
	 MOV     R0, #0
	 MOV     R3, #0
	 CMP     R1, #0
	 RSBLT   R1, R1, #0
	 EORLT   R3, R3, #1
	 CMP     R2, #0
	 RSBLT   R2, R2, #0
	 EORLT   R3, R3, #1
	 MOV     R4, R2
	 MOV     R5, #1
div_max
	 LSL     R4, R4, #1
	 LSL     R5, R5, #1
	 CMP     R4, R1
	 BLE     div_max
div_loop
	 LSR     R4, R4, #1
	 LSR     R5, R5, #1
	 CMP     R4,R1
	 BGT     div_loop
	 ADD     R0, R0, R5
	 SUB     R1, R1, R4
	 CMP     R1, R2
	 BGE     div_loop
	 CMP     R3, #1
	 BNE     div_exit
	 CMP     R1, #0
	 ADDNE   R0, R0, #1
	 RSB     R0, R0, #0
	 RSB     R1, R1, #0
	 ADDNE   R1, R1, R2
div_exit
	 CMP     R0, #0
	 ADDEQ   R1, R1, R4
	 LDMFD   SP!, {PC, R2-R5}
