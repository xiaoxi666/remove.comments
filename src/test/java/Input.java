import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author xiaoxi666
 * @date 2021-02-15 17:13
 * 我是 Javadoc 注释
 */
public class Input {
    /**
     * 我是 Javadoc 注释
     *
     * @param param1
     * @param param2
     */
    public static void someMethod(String param1,  // 参数注释
                                  String param2
//                                  String param3
        /* String param4 */) {
        // 我是单行注释
        int a = 1;
        /* 我是块注释 */
        int b = 2;
        int c = 3;
        String s1 = "// 我是字符串中的内容，不是注释";
        String s2 = "/* 我是字符串中的内容，不是注释 */";
        String s3 = "/** 我是字符串中的内容，不是注释 */";
        // 我是Java8 stream 的流式处理注释
        new ArrayList<>().stream()
            .filter(f -> f.hashCode() != 0)
            .collect(Collectors.toList());
        // 我是方法尾部注释
    }
    // 我是类尾部注释
}
// 我是最后的注释
