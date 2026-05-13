PROJECT:=Qin110

TOP:=Qin110Top

include magicleda/magicleda.mk

SRCS_XDC_EXTRA:=${LEDA_XDC}
SRCS_SPINAL_EXTRA:=${wildcard src/*.scala} ${LEDA_SPINAL}
PLATFORM:=${LEDA_PLATFORM}
FLASH:=${LEDA_FLASH}

ARGS?=TestRV32IPart
PREBUILT_DIR:=prebuilt
PREBUILT_BIN:=${PREBUILT_DIR}/${ARGS}.bin

TEST_RV:=command -v riscv-none-elf-as >/dev/null 2>&1 && command -v riscv-none-elf-objcopy >/dev/null 2>&1
ECHO_PREBUILT:=echo "@@@@ Using prebuilt binary @@@@@"
ECHO_MISSING:=echo "Missing RISC-V toolchain and prebuilt binary!" >&2

ifeq (${TARGET},${TOP})
PRE_BUILD_CMD:=\
if ${TEST_RV}; then \
	make -C ${ARGS}; \
elif test -f ${PREBUILT_BIN}; then \
	${ECHO_PREBUILT}; \
else \
	${ECHO_MISSING}; \
	exit 1; \
fi
endif

include makefile-spinal/makefile-spinal.mk

TASKS:=TestRV32IFull TestRV32IPart

TASK_TOP:=Qin110Top

.PHONY: ${TASKS}

${TASKS}: %:
	if ${TEST_RV}; then \
		make -C $*; \
	elif test -f ${PREBUILT_DIR}/$*.bin; then \
		${ECHO_PREBUILT}; \
	else \
		${ECHO_MISSING}; \
		exit 1; \
	fi
	${SBT} "runMain ${PROJECT}.${TASK_TOP}Sim $*"
	${NOTIFY_DONE}
