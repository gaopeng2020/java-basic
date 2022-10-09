package test;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Simple example of JNA interface mapping and usage.
 */
public class HelloWorld {


    public interface CLibrary extends StdCallLibrary {
        // C库的映射
        CLibrary INSTANCE = Native.load((Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);

        //声明C库的方法（Java中需要使用的），这里声明C中的一个打印方法
        void printf(String format, Object... args);

        double cosh(double value);
        double pow( double x, double y );
    }

    @Structure.FieldOrder({ "wYear", "wMonth", "wDayOfWeek", "wDay", "wHour", "wMinute", "wSecond", "wMilliseconds" })
    public interface Kernel32 extends StdCallLibrary
    {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);
        public static class SYSTEMTIME extends Structure {
            public short wYear;
            public short wMonth;
            public short wDayOfWeek;
            public short wDay;
            public short wHour;
            public short wMinute;
            public short wSecond;
            public short wMilliseconds;
        }

        void GetSystemTime (SYSTEMTIME result);

    }


    public static void main(String[] args) {
        //调用c库方法
        CLibrary.INSTANCE.printf("Hello, World\n");
        CLibrary.INSTANCE.printf("cosh", CLibrary.INSTANCE.cosh(0));
        System.out.println("CLibrary.INSTANCE.pow(2,3) = " + CLibrary.INSTANCE.pow(2, 3));

        Kernel32 lib = Kernel32.INSTANCE;
        Kernel32.SYSTEMTIME time = new Kernel32.SYSTEMTIME();
        lib.GetSystemTime(time);
//        System.out.println ("Year is "+time.wYear);
//        System.out.println ("Month is "+time.wMonth);
//        System.out.println ("Day of Week is "+time.wDayOfWeek);
//        System.out.println ("Day is "+time.wDay);
//        System.out.println ("Hour is "+time.wHour);
//        System.out.println ("Minute is "+time.wMinute);
//        System.out.println ("Second is "+time.wSecond);
//        System.out.println ("Milliseconds are "+time.wMilliseconds);

    }
}
