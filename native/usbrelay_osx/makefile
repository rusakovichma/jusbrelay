CC = gcc # C compiler
CFLAGS = -dynamiclib -undefined suppress -flat_namespace
LDFLAGS =
RM = rm -f  # rm command

JDK_HOME = /System/Library/Frameworks/JavaVM.framework/Headers
RELAY_LIB = usb_relay_device.dylib  # target lib
JUSB_LIB = usbrelay.dylib
EXTRACT_DIR = .

.PHONY: all
all: ${RELAY_LIB} ${JUSB_LIB}

${RELAY_LIB}:
	$(CC) -I/System/Library/Frameworks/IOKit.framework/Headers/ ${CFLAGS} ${LDFLAGS} hiddata_osx.c usb_relay_device.c -o $(RELAY_LIB)

${JUSB_LIB}:
	$(CC) -I${JDK_HOME} ${CFLAGS} ${LDFLAGS} jusbrelay.cpp -o $(JUSB_LIB)

.PHONY: clean
clean:
	-${RM} ${RELAY_LIB} ${JUSB_LIB}
