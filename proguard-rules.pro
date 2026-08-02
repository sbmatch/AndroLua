-dontpreverify

-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class org.keplerproject.luajava.** {
    *;
}
-keep class org.apache.commons.io.** {
    public <init>(...);
    public *;
}
-keep class com.github.sisong.** { 
    public <init>(...);
    public *; 
}
-keep class com.tencent.mmkv.MMKV  {
    public <init>(...);
    public *; 
}
-keep class xcrash.XCrash { 
    public <init>(...);
    public *; 
}
-keep class xcrash.TombstoneManager {
    public <init>(...);
    public *;
}
-keep class xcrash.TombstoneParser { 
    public <init>(...);
    public *; 
}

-keep class android.support.annotation.Keep
-keep public class com.google.android.vending.licensing.ILicensingService
-keep public class com.google.vending.licensing.ILicensingService
-keep,allowshrinking class * extends androidx.startup.Initializer
-keep class * implements androidx.versionedparcelable.VersionedParcelable
-keep public class androidx.versionedparcelable.ParcelImpl

-printmapping mapping.txt

-optimizationpasses 5

# 使用自定义字典混淆
-classobfuscationdictionary chinese_dictionary.txt
-obfuscationdictionary chinese_dictionary.txt
-packageobfuscationdictionary chinese_dictionary.txt