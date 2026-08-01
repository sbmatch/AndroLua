LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := lua
LOCAL_SRC_FILES := lapi.c lauxlib.c lbaselib.c lcode.c ldblib.c ldebug.c ldo.c ldump.c lfunc.c lgc.c linit.c liolib.c llex.c lmathlib.c lmem.c loadlib.c lobject.c lopcodes.c loslib.c lparser.c lstate.c lstring.c lstrlib.c ltable.c ltablib.c ltm.c lundump.c lvm.c lzio.c lcorolib.c lctype.c lutf8lib.c
LOCAL_LDLIBS    := -llog -lm -ldl  -fvisibility=default

LOCAL_CFLAGS := -DLUA_USE_LINUX -DLUA_USE_POPEN -Wl,-E

include $(BUILD_STATIC_LIBRARY)
