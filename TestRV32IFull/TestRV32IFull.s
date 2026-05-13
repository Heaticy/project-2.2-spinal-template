.global _start

.section .text

_start:
    # 0x20000_000 -> DMEM Base
    # s11 will += 4 for every test
    lui     s11, 0x20000

    # s2 =  51 = 0011 0011
    addi    s2, zero, 51
    # s3 =   5 = 0000 0101
    addi    s3, zero, 5 

    # s1 =   1 = 0000 0001
    addi    s1, zero, 1
    # s4 =   7 = 0000 0111
    addi    s4, zero, 7
    # s5 =  -7 = 1111 1001
    addi    s5, zero, -7

    # s6 = 0xCCCCEEFF = -858984705 = 3435982591
    # li -> lui + addi
    li      s6, 0xCCCCEEFF

    # 8 instruction above
    # offset = 32

    # >> auipc
    # 32 = offset + 0
    auipc   t0, 0
    sw      t0, 0(s11)
    addi    s11, s11, 4 

    # >> jalr
    # 48 = offset + 16
    jalr    t0, t0, 20
    addi    t0, zero, 0
    sw      t0, 0(s11)
    addi    s11, s11, 4 

    # >> jal
    # 64 = offset + 32
    jal     t0, _label_jal
    addi    t0, zero, 0
    _label_jal:
    sw      t0, 0(s11)
    addi    s11, s11, 4 

    # >> sw
    # 0xCCCCEEFF = -858984705
    sw      s6, 0(s11)
    addi    s10, s11, 0
    addi    s11, s11, 4 

    # >> sh
    # 0x0000EEFF = 61183
    sh      zero, 2(s11)
    sh      s6, 0(s11)
    addi    s11, s11, 4 

    # >> sb
    # 0x0000FF00 = 65280
    sb      zero, 3(s11)
    sb      zero, 2(s11)
    sb      zero, 0(s11)
    sb      s6, 1(s11)
    addi    s11, s11, 4 

    # >> lw
    # 0xCCCCEEFF = -858984705
    lw      t0, 0(s10)
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> lh
    # 0xFFFFEEFF = -4353
    lh      t0, 0(s10)
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> lb
    # 0xFFFFFFFF = -1
    lb      t0, 0(s10)
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> lhu
    # 0x0000EEFF = 61183
    lhu     t0, 0(s10)
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> lbu
    # 0x000000FF = 255
    lbu     t0, 0(s10)
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> add
    # 51 + 5 = 56
    add     t0, s2, s3
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> sub
    # 51 - 5 = 46
    sub     t0, s2, s3
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> sll
    # 7 << 1 = 14
    sll     t0, s4, s1
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> srl
    # 7 >> 1 = 3
    srl     t0, s4, s1
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> sra
    # -7 >>> 1 = -4
    sra     t0, s5, s1
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> xor
    # 51 ^ 5 = 54
    xor     t0, s2, s3
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> or
    # 51 | 5 = 55
    or      t0, s2, s3
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> and
    # 51 & 5 = 1
    and     t0, s2, s3
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> slt
    # 1 < 7 = 1
    slt     t0, s1, s4
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> sltu
    # -7 < 7 = 1
    slt     t0, s5, s4
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> addi
    # 51 - 10 = 41
    addi    t0, s2, -10
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> slli
    # 7 << 2 = 28
    slli    t0, s4, 2
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> srli
    # 7 >> 2 = 1
    srli    t0, s4, 2
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> srai
    # -7 >>> 2 = -2
    srai    t0, s5, 2
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> xori
    # 5 ^ 3 = 6
    xori    t0, s3, 3
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> ori
    # 5 | 3 = 7
    ori     t0, s3, 3
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> andi
    # 5 & 3 = 1
    andi    t0, s3, 3
    sw      t0, 0(s11)
    addi    s11, s11, 4
    
    # >> slti
    # 1 < 0 = 0
    slti    t0, s1, 0
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> sltiu
    # -7 < -9 = 0
    sltiu   t0, s5, -9
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> beq
    # (-7 == 7) = 0
    addi    t0, zero, 1
    beq     s5, s4, _label_beq
    addi    t0, zero, 0
    _label_beq:
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> bne
    # (-7 != 7) = 1
    addi    t0, zero, 1   
    bne     s5, s4, _label_bne
    addi    t0, zero, 0
    _label_bne:
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> blt
    # (-7 < 7)  = 1
    addi    t0, zero, 1
    blt     s5, s4, _label_blt
    addi    t0, zero, 0
    _label_blt:
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> bge
    # (-7 >= 7) = 0
    addi    t0, zero, 1   
    bge     s5, s4, _label_bge
    addi    t0, zero, 0
    _label_bge:
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> bltu
    # (1 < 7)   = 1
    addi    t0, zero, 1
    bltu    s1, s4, _label_bltu
    addi    t0, zero, 0
    _label_bltu:
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # >> bgeu
    # (1 >= 7)  = 0
    addi    t0, zero, 1   
    bgeu    s1, s4, _label_bgeu
    addi    t0, zero, 0
    _label_bgeu:
    sw      t0, 0(s11)
    addi    s11, s11, 4

    # Exit
    lui     s11, 0x60000
    addi    s10, zero, 1
    sw      s10, 0(s11)
