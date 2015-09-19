CC = gcc # C compiler
CFLAGS = -fpermissive -Wall -O3
LDFLAGS = -shared  # linking flags
RM = rm -f  # rm command

JDK_HOME = /usr/lib/jvm/java-7-openjdk-i386
RELAY_LIB = usb_relay_device.so  # target lib
JUSB_LIB = usbrelay.so
EXTRACT_DIR = /tmp/natives_lib_dir

.PHONY: all
all: ${RELAY_LIB} ${JUSB_LIB}

${RELAY_LIB}:
	$(CC) -I/usr/lib/i386-linux-gnu ${CFLAGS} ${LDFLAGS} hiddata_libusb01.c usb_relay_device.c -o $(RELAY_LIB) -lusb

${JUSB_LIB}:
	$(CC) -I${JDK_HOME}/include ${CFLAGS} ${LDFLAGS} jusbrelay.cpp -o $(JUSB_LIB) -l:${EXTRACT_DIR}/usb_relay_device.so

.PHONY: clean
clean:
	-${RM} ${RELAY_LIB} ${JUSB_LIB}
