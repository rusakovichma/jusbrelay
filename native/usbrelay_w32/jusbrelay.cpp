#include "jusbrelay.h"
#include "usb_relay_device.h"
#include <string>

struct usb_relay_device_info *deviceList;

void throwJavaException(JNIEnv* pEnv, const char* pClazzName, const char* pMessage) {
	jclass clazz = pEnv->FindClass(pClazzName);
	if (clazz) {
		pEnv->ThrowNew(clazz, pMessage);
	}
	pEnv->DeleteLocalRef(clazz);
}

/*
* Class:     by_creepid_jusbrelay_NativeUsbRelayManager
* Method:    relayInit
* Signature: ()V
*/
JNIEXPORT void JNICALL Java_by_creepid_jusbrelay_NativeUsbRelayManager_relayInit
(JNIEnv *pEnv, jobject){
	int result = usb_relay_init();
	if (result < 0){
		const char* clazzName = "by/creepid/jusbrelay/UsbRelayException";
		const char* message = "Cannot init relay module";
		throwJavaException(pEnv, clazzName, message);
	}
}

/*
* Class:     by_creepid_jusbrelay_NativeUsbRelayManager
* Method:    relayExit
* Signature: ()V
*/
JNIEXPORT void JNICALL Java_by_creepid_jusbrelay_NativeUsbRelayManager_relayExit
(JNIEnv *pEnv, jobject){
	int result = usb_relay_exit();
	if (result < 0){
		const char* clazzName = "by/creepid/jusbrelay/UsbRelayException";
		const char* message = "Error while exit relay module";
		throwJavaException(pEnv, clazzName, message);
	}
}

/*
* Class:     by_creepid_jusbrelay_NativeUsbRelayManager
* Method:    deviceEnumerate
* Signature: ()[Lby/creepid/jusbrelay/UsbRelayDeviceInfo;
*/
JNIEXPORT jobjectArray JNICALL Java_by_creepid_jusbrelay_NativeUsbRelayManager_deviceEnumerate
(JNIEnv *env, jobject){
	jobjectArray ret = NULL;

	if (deviceList){
		usb_relay_device_free_enumerate(deviceList);
		deviceList = NULL;
	}

	deviceList = usb_relay_device_enumerate();


	if (deviceList){
		struct usb_relay_device_info *curr_device = deviceList;

		int len = 0;
		while (curr_device){
			len++;
			curr_device = curr_device->next;
		}

		curr_device = deviceList;

		jclass info_cls = env->FindClass("by/creepid/jusbrelay/UsbRelayDeviceInfo");
		jmethodID constr = env->GetMethodID(info_cls, "<init>", "()V");

		ret = (jobjectArray)env->NewObjectArray(len,info_cls,NULL);

		jmethodID set_path = env->GetMethodID(info_cls, "setDevicePath", "(Ljava/lang/String;)V");
		jmethodID set_serial = env->GetMethodID(info_cls, "setSerialNumber", "(Ljava/lang/String;)V");
		jmethodID set_type = env->GetMethodID(info_cls, "setDeviceType", "(Lby/creepid/jusbrelay/UsbRelayDeviceType;)V");

		jclass device_type_cls = env->FindClass("by/creepid/jusbrelay/UsbRelayDeviceType"); 

		int i = 0;
		while (curr_device){
			jobject info_obj = env->NewObject(info_cls, constr);

			jstring path_string = (jstring)(env->NewStringUTF((char*)curr_device->device_path));
			env->CallObjectMethod(info_obj,set_path,path_string);

			jstring serial_string = (jstring)(env->NewStringUTF((char*)curr_device->serial_number));
			env->CallObjectMethod(info_obj,set_serial,serial_string);

			usb_relay_device_type device_type = curr_device->type;
			jobject j_device_type = NULL;
			switch( device_type ) {
				case USB_RELAY_DEVICE_ONE_CHANNEL:{
					jfieldID type_field  = env->GetStaticFieldID(device_type_cls, "ONE_CHANNEL", "Lby/creepid/jusbrelay/UsbRelayDeviceType;");
					j_device_type = env->GetStaticObjectField(device_type_cls, type_field);
					}break;
				case USB_RELAY_DEVICE_TWO_CHANNEL:{
					jfieldID type_field  = env->GetStaticFieldID(device_type_cls, "TWO_CHANNEL", "Lby/creepid/jusbrelay/UsbRelayDeviceType;");
					j_device_type = env->GetStaticObjectField(device_type_cls, type_field);
					}break;
				case USB_RELAY_DEVICE_FOUR_CHANNEL:{
					jfieldID type_field  = env->GetStaticFieldID(device_type_cls, "FOUR_CHANNEL", "Lby/creepid/jusbrelay/UsbRelayDeviceType;");
					j_device_type = env->GetStaticObjectField(device_type_cls, type_field);
					}break;
				case USB_RELAY_DEVICE_EIGHT_CHANNEL:{
					jfieldID type_field  = env->GetStaticFieldID(device_type_cls, "EIGHT_CHANNEL", "Lby/creepid/jusbrelay/UsbRelayDeviceType;");
					j_device_type = env->GetStaticObjectField(device_type_cls, type_field);
					}break;
			}
			env->CallObjectMethod(info_obj,set_type,j_device_type);

			env->SetObjectArrayElement(ret,i,info_obj);
			curr_device = curr_device->next;
		}
	}else{
		const char* clazzName = "by/creepid/jusbrelay/UsbRelayException";
		const char* message = "Cannot enumerate devices";
		throwJavaException(env, clazzName, message);
		return NULL;
	}

	return ret;
}

/*
* Class:     by_creepid_jusbrelay_NativeUsbRelayManager
* Method:    deviceOpen
* Signature: (Ljava/lang/String;)Lby/creepid/jusbrelay/UsbRelayDeviceHandler;
*/
JNIEXPORT jobject JNICALL Java_by_creepid_jusbrelay_NativeUsbRelayManager_deviceOpen__Ljava_lang_String_2
(JNIEnv *env, jobject, jstring j_serial){

	if (deviceList){

		struct usb_relay_device_info *curr_device = deviceList;
		const char *serial = env->GetStringUTFChars(j_serial, 0);

		while (curr_device){
			const char *curr_serial = (char*)(curr_device->serial_number);
			if (strcmp(curr_serial, serial) == 0) {
				int device_handler = usb_relay_device_open_with_serial_number(serial, strlen(serial));
				
				jclass handler_cls = env->FindClass("by/creepid/jusbrelay/UsbRelayDeviceHandler");
				jmethodID constr = env->GetMethodID(handler_cls, "<init>", "(I)V");

				return env->NewObject(handler_cls, constr, device_handler);
			}
			
			curr_device = curr_device->next;
		}

	}else{
		const char* clazzName = "by/creepid/jusbrelay/UsbRelayException";
		const char* message = "Cannot get devices";
		throwJavaException(env, clazzName, message);
		return NULL;
	}

	return NULL;
}

/*
* Class:     by_creepid_jusbrelay_NativeUsbRelayManager
* Method:    deviceOpen
* Signature: (Lby/creepid/jusbrelay/UsbRelayDeviceInfo;)Lby/creepid/jusbrelay/UsbRelayDeviceHandler;
*/
JNIEXPORT jobject JNICALL Java_by_creepid_jusbrelay_NativeUsbRelayManager_deviceOpen__Lby_creepid_jusbrelay_UsbRelayDeviceInfo_2
(JNIEnv *env, jobject jobj, jobject j_device_info){

	jclass j_device_info_cls = env->GetObjectClass(j_device_info);
	jmethodID get_serial_method = env->GetMethodID(j_device_info_cls, "getSerialNumber", "()L");
	jstring serial =  (jstring) env->CallObjectMethod(j_device_info, get_serial_method);

	return Java_by_creepid_jusbrelay_NativeUsbRelayManager_deviceOpen__Ljava_lang_String_2(env, jobj, serial);
}

/*
* Class:     by_creepid_jusbrelay_NativeUsbRelayManager
* Method:    openRelayChannel
* Signature: (Lby/creepid/jusbrelay/UsbRelayDeviceHandler;I)V
*/
JNIEXPORT void JNICALL Java_by_creepid_jusbrelay_NativeUsbRelayManager_openRelayChannel
(JNIEnv *env, jobject, jobject j_handler, jint channel){

	jclass j_handler_cls = env->GetObjectClass(j_handler);
	jmethodID get_handler_method = env->GetMethodID(j_handler_cls, "getValue", "()I");
	jint handler = env->CallIntMethod(j_handler, get_handler_method );

	int ret = usb_relay_device_open_one_relay_channel(handler, channel+1);
	if (ret < 0){
		const char* clazzName = "by/creepid/jusbrelay/UsbRelayException";
		const char* message = "Open relay channel error";
		throwJavaException(env, clazzName, message);
	}
}

/*
* Class:     by_creepid_jusbrelay_NativeUsbRelayManager
* Method:    openRelayChannels
* Signature: (Lby/creepid/jusbrelay/UsbRelayDeviceHandler;)V
*/
JNIEXPORT void JNICALL Java_by_creepid_jusbrelay_NativeUsbRelayManager_openRelayChannels
(JNIEnv *env, jobject, jobject j_handler){
	jclass j_handler_cls = env->GetObjectClass(j_handler);
	jmethodID get_handler_method = env->GetMethodID(j_handler_cls, "getValue", "()I");
	jint handler = env->CallIntMethod(j_handler, get_handler_method );

	int ret = usb_relay_device_open_all_relay_channel(handler);
	if (ret < 0){
		const char* clazzName = "by/creepid/jusbrelay/UsbRelayException";
		const char* message = "Cannot open channels of relay";
		throwJavaException(env, clazzName, message);
	}
}

/*
* Class:     by_creepid_jusbrelay_NativeUsbRelayManager
* Method:    closeRelayChannel
* Signature: (Lby/creepid/jusbrelay/UsbRelayDeviceHandler;I)V
*/
JNIEXPORT void JNICALL Java_by_creepid_jusbrelay_NativeUsbRelayManager_closeRelayChannel
(JNIEnv *env, jobject, jobject j_handler, jint channel){
	jclass j_handler_cls = env->GetObjectClass(j_handler);
	jmethodID get_handler_method = env->GetMethodID(j_handler_cls, "getValue", "()I");
	jint handler = env->CallIntMethod(j_handler, get_handler_method );

	int ret = usb_relay_device_close_one_relay_channel(handler, channel+1);
	if (ret < 0){
		const char* clazzName = "by/creepid/jusbrelay/UsbRelayException";
		const char* message = "Close relay channel error";
		throwJavaException(env, clazzName, message);
	}
}

/*
* Class:     by_creepid_jusbrelay_NativeUsbRelayManager
* Method:    closeRelayChannels
* Signature: (Lby/creepid/jusbrelay/UsbRelayDeviceHandler;)V
*/
JNIEXPORT void JNICALL Java_by_creepid_jusbrelay_NativeUsbRelayManager_closeRelayChannels
(JNIEnv *env, jobject, jobject j_handler){
	jclass j_handler_cls = env->GetObjectClass(j_handler);
	jmethodID get_handler_method = env->GetMethodID(j_handler_cls, "getValue", "()I");
	jint handler = env->CallIntMethod(j_handler, get_handler_method );

	int ret = usb_relay_device_close_all_relay_channel(handler);
	if (ret < 0){
		const char* clazzName = "by/creepid/jusbrelay/UsbRelayException";
		const char* message = "Cannot close channels of relay";
		throwJavaException(env, clazzName, message);
	}
}

/*
* Class:     by_creepid_jusbrelay_NativeUsbRelayManager
* Method:    getStatus
* Signature: (Lby/creepid/jusbrelay/UsbRelayDeviceHandler;)[Lby/creepid/jusbrelay/UsbRelayStatus;
*/
JNIEXPORT jobjectArray JNICALL Java_by_creepid_jusbrelay_NativeUsbRelayManager_getStatus
(JNIEnv *env, jobject, jstring j_serial, jobject j_handler){
	jobjectArray ret = NULL;

	if (deviceList){

		struct usb_relay_device_info *curr_device = deviceList;
		const char *serial = env->GetStringUTFChars(j_serial, 0);

		while (curr_device){
			const char *curr_serial = (char*)(curr_device->serial_number);
			if (strcmp(curr_serial, serial) == 0) {

				jclass j_handler_cls = env->GetObjectClass(j_handler);
				jmethodID get_handler_method = env->GetMethodID(j_handler_cls, "getValue", "()I");
				jint handler = env->CallIntMethod(j_handler, get_handler_method );

				jclass status_cls = env->FindClass("by/creepid/jusbrelay/UsbRelayStatus");
				jmethodID constr = env->GetMethodID(status_cls, "<init>", "(IZ)V");
				
				unsigned int status = 0;
				if (0 == usb_relay_device_get_status(handler, &status))
				{
					ret = (jobjectArray)env->NewObjectArray(curr_device->type,status_cls,NULL);
					for (int i = 0; i < curr_device->type; i++)
					{
						bool opened = false;
						if ((1<<i) & status){
							opened = true;		
						}
						jobject status_obj = env->NewObject(status_cls, constr, i, opened);
						env->SetObjectArrayElement(ret,i,status_obj);
					}
				}

				return ret;
			}
			
			curr_device = curr_device->next;
		}

	}else{
		const char* clazzName = "by/creepid/jusbrelay/UsbRelayException";
		const char* message = "Cannot get devices";
		throwJavaException(env, clazzName, message);
		return NULL;
	}

	return ret;
}

/*
 * Class:     by_creepid_jusbrelay_NativeUsbRelayManager
 * Method:    closeRelay
 * Signature: (Lby/creepid/jusbrelay/UsbRelayDeviceHandler;)V
 */
JNIEXPORT void JNICALL Java_by_creepid_jusbrelay_NativeUsbRelayManager_closeRelay
(JNIEnv *env, jobject, jobject j_handler){
	jclass j_handler_cls = env->GetObjectClass(j_handler);
	jmethodID get_handler_method = env->GetMethodID(j_handler_cls, "getValue", "()I");
	jint handler = env->CallIntMethod(j_handler, get_handler_method );

	usb_relay_device_close(handler);
}