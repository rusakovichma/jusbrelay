CC = gcc # C compiler
CFLAGS = -fpermissive -Wall -O3
LDFLAGS = -shared  # linking flags
RM = rm -f  # rm command

JDK_HOME = /usr/lib/jvm/java-7-openjdk-i386
HIDDATA_LIB = hiddata_libusb01.so  # target lib
RELAY_LIB = usb_relay_device.so  # target lib
JUSB_LIB = usbrelay.so

.PHONY: all
all: ${HIDDATA_LIB} ${RELAY_LIB} ${JUSB_LIB}

$(HIDDATA_LIB):
	$(CC) ${CFLAGS} ${LDFLAGS} $(HIDDATA_LIB:.so=.c) -o $(HIDDATA_LIB)

${RELAY_LIB}:
	$(CC) ${CFLAGS} ${LDFLAGS} $(RELAY_LIB:.so=.c) -o $(RELAY_LIB)

${JUSB_LIB}:
	$(CC) -I${JDK_HOME}/include ${CFLAGS} ${LDFLAGS} jusbrelay.cpp -o $(JUSB_LIB)


.PHONY: clean
clean:
	-${RM} ${HIDDATA_LIB} ${RELAY_LIB}
