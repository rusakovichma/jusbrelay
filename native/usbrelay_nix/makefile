CC = gcc # C compiler
CFLAGS = -Wall -O3
LDFLAGS = -shared  # linking flags
RM = rm -f  # rm command

HIDDATA_LIB = hiddata_libusb01.so  # target lib
RELAY_LIB = usb_relay_device.so  # target lib

.PHONY: all
all: ${HIDDATA_LIB} ${RELAY_LIB}

$(HIDDATA_LIB):
	$(CC) ${CFLAGS} ${LDFLAGS} $(HIDDATA_LIB:.so=.c) -o $(HIDDATA_LIB)

${RELAY_LIB}:
	$(CC) ${CFLAGS} ${LDFLAGS} $(RELAY_LIB:.so=.c) -o $(RELAY_LIB)


.PHONY: clean
clean:
	-${RM} ${HIDDATA_LIB} ${RELAY_LIB}
