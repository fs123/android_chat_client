# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/fs/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontoptimize
#-optimizationpasses 2
-dontobfuscate
-dontpreverify
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses
-dontnote **
-verbose

-keep class com.typesafe.config.Config { *; }
-keep class com.typesafe.config.ConfigFactory { *; }
-keep class org.slf4j.Logger { *; }
-keep class org.slf4j.LoggerFactory { *; }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#Scala
-dontwarn scala.**

-keepclassmembers class * {
      ** MODULE$;
}

-keep class scala.Option
-keep class scala.Function1
-keep class scala.PartialFunction
#https://issues.scala-lang.org/browse/SI-5397
-keep class scala.collection.SeqLike {
    public protected *;
}
-keep class scala.Tuple*

-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

-keep class * implements org.xml.sax.EntityResolver

-keepclassmembers class * {
    ** MODULE$;
}

-keepclassmembernames class scala.concurrent.forkjoin.ForkJoinPool { *; }

-keepclassmembernames class scala.concurrent.forkjoin.ForkJoinWorkerThread {
    int base;
    int sp;
    int runState;
}

-keepclassmembernames class scala.concurrent.forkjoin.ForkJoinTask {
    int status;
}

-keepclassmembernames class scala.concurrent.forkjoin.LinkedTransferQueue { *; }

#Akka
-dontwarn org.jboss.netty.logging.**
-dontwarn org.osgi.**
-dontwarn javax.servlet.**
#-dontwarn org.jboss.netty.channel.socket.http.**

## Unsafe is there at runtime
-dontwarn sun.misc.Unsafe
-keep class sun.misc.Unsafe{
    *;
}

-keep class akka.** { *; }

-keep class com.google.protobuf.GeneratedMessage {
    *;
}
-keep class org.javatuples.** { *; }
-keep class org.jboss.**

-dontwarn org.jboss.netty.handler.codec.marshalling.**
-dontwarn org.jboss.netty.channel.socket.nio.**
-dontwarn org.jboss.netty.handler.codec.compression.JdkZlibEncoder
-dontwarn org.jboss.netty.handler.codec.spdy.SpdyHeaderBlockZlibCompressor
-dontwarn org.jboss.netty.channel.socket.http.HttpTunnelingServlet
-dontwarn org.jboss.modules.**
-dontwarn __redirected.**
-dontwarn org.slf4j.LoggerFactory
-dontwarn org.slf4j.MarkerFactory
-dontwarn org.slf4j.MDC
-dontwarn org.slf4j.impl.AndroidLogger
-dontwarn org.slf4j.impl.AndroidLoggerFactory