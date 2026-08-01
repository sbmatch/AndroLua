-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留 Lua 相关类
-keep class org.keplerproject.** { *; }
-keep class org.apache.commons.io.** { *; }

-printmapping mapping.txt

-optimizationpasses 5

# 使用自定义字典混淆
-classobfuscationdictionary chinese_dictionary.txt
-obfuscationdictionary chinese_dictionary.txt
-packageobfuscationdictionary chinese_dictionary.txt