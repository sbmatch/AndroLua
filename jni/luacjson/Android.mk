LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES += $(LOCAL_PATH)/../lua
LOCAL_MODULE     := cjson
LOCAL_SRC_FILES  := lua_cjson.c strbuf.c g_fmt.c dtoa.c
LOCAL_STATIC_LIBRARIES := liblua
LOCAL_CFLAGS     := -DUSE_INTERNAL_FPCONV -DMULTIPLE_THREADS
include $(BUILD_SHARED_LIBRARY)
