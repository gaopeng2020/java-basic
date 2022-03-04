import org.junit.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {
    /**
     * Stream API（java.util.stream）把真正的函数式编程风格引入到Java中。这是目前为止对Java类库最好的补充，
     * Stream是Java8中处理集合的关键抽象概念，它对集合进执行非常复杂的查找、过滤和映射数据等操作。
     * Stream和Collection集合的区别：Collection是一种静态的内存数据结构，而Stream是有关计算的。
     * 前者是主要面向内存，存储在内存中，后者主要是面向CPU，通过CPU实现计算。
     * Stream的三个操作步骤：创建，中间操作，终止操作
     * 1. 创建：四种创建方式
     * 2. 中间操作：可以链式调用，中间操作是延迟的，只有遇到终止操作符中间操作才会被执行,
     *      一旦遇到终止操作符，则该流不可以再使用中间操作
     * a:筛选与切片
     * b:映射
     * c:排序
     * 3. 终止操作：
     * a.匹配与查找
     * b.规约
     * c.收集
     */

    /**
     * 1. 创建：四种创建方式
     * a.集合或数组提供stream或者parallelStream函数接口 Collection.stream ,Arrays.stream
     * b. 通过Stream.of(”abc“,"123","!@#") 创建
     * c. 通过Stream.iterator(”abc“,"123","!@#") 创建
     * d. 通过Stream.generate 生成
     */
    @Test
    public void streamCreationTest() {
        int[] intArray = new int[]{1, 2, 3, 4, 5};
        IntStream arrayStream = Arrays.stream(intArray);

        List<Integer> intList = Arrays.asList(6, 7, 8, 9, 10);
        Stream<Integer> listStream = intList.stream();

        Stream<String> streamOf = Stream.of("abc", "123", "!@#");

        //无限流，Stream.iterate，需要使用limit 限定数量
        Stream<Integer> streamIterator = Stream.iterate(1, value -> value + 1).limit(10);

        //无限流，Stream.generate，需要使用limit 限定数量
        Stream<Double> generateStream = Stream.generate(Math::random).limit(10);
    }

    /**
     * 流中间操作：筛选与切片
     * filter（Predicate p）接收Lambda，从流中过滤某些元素；
     * distinct（）去重，通过流所生成元素的hashCode（）和equals（）去除重复元素；
     * limit（long maxSize）断流，使其元素不超过给定数量,与skip(long n)互补
     * skip(long n)跳过前n个元素，返回一个扔掉了前n个元素的流。若流中元素不足n个，则返回一个空流。
     */
    @Test
    public void filterAndSection() {
        List<Employee> employees = Employee.getEmployees();
        Stream<Employee> filterStream = employees.stream();
        //filter : 筛选出符合某种规则的元素
        filterStream.filter(employee -> employee.getDepartment().equals("Development"))
                .forEach(System.out::println);

        System.out.println("================================");
        //distinct: 集合去重
        Stream<Employee> distinctStream = employees.stream();
        employees.add(Employee.builder().name("zhangsan").id(2).title("Sales Man").department("Sales").build());
        employees.forEach(System.out::println);
        System.out.println("==============去重之后==================");
        distinctStream.distinct().forEach(System.out::println);

        //limit:截断并返回前n个元素
        System.out.println("==============截取前三个元素==================");
        employees.stream().limit(3).forEach(System.out::println);

        //limit:截断并返回前n个元素
        System.out.println("==============去除前三个元素==================");
        employees.stream().skip(3).forEach(System.out::println);
    }

    /**
     * 中间操作：映射
     * <R> Stream<R> map(Function<? super T, ? extends R> mapper)映射：给一个输入t,经过特殊处理返回一个r
     * <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)展平映射，处理流中流，将所有流的流展平放在一个流中
     */
    @Test
    public void mapStreamTest() {
        List<String> list = Arrays.asList("aa", "bb", "cc", "dd", "ee", "FF");
        list.stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);

        Stream<String> nameStream = Employee.getEmployees().stream()
                .map(Employee::getName);
        nameStream.filter(str -> str.length() > 5).forEach(System.out::println);

        //流中流
        Stream<Stream<Character>> mapStream = list.stream().map(new Function<String, Stream<Character>>() {
            final List<Character> chars = new ArrayList<>();

            @Override
            public Stream<Character> apply(String s) {
                for (char c : s.toCharArray()) {
                    chars.add(c);
                }
                return chars.stream();
            }
        });
        mapStream.forEach(stream -> stream.forEach(System.out::println));

        //展平流
        Stream<Character> flatMapStream = list.stream().flatMap(new Function<String, Stream<Character>>() {
            final List<Character> chars = new ArrayList<>();

            @Override
            public Stream<Character> apply(String s) {
                for (char c : s.toCharArray()) {
                    chars.add(c);
                }
                return chars.stream();
            }
        });
        flatMapStream.forEach(System.out::println);
    }

    /**
     * 中间操作：排序
     * sorted（）产生一个新流，其中按自然顺序排序
     * sorted（Comparator com）产生一个新流，其中按比较器顺序排序
     */
    @Test
    public void sortStreamTest() {
        List<Integer> integers = Arrays.asList(2, 6, 100, -20, -66, 88, 99, 1, 0);
        integers.stream().sorted().forEach(System.out::println);

        List<Employee> employees = Employee.getEmployees();
        employees.stream().sorted(Comparator.comparingInt(Employee::getId))
                .forEach(System.out::println);
    }


    /**
     * 终止操作：匹配与查找
     * allMatch（Predicate p）检查集合中所有元素是否匹配某个规则
     * anyMatch（Predicate p）检查否至少一个元素匹配某个规则
     * noneMatch（Predicate p）检查集合中所有元素都不匹配某个规则
     * findFirst（）返回第一个元素
     * findAny（）返回当前流中的任意元素
     * count（）返回流中元素总数
     * max（Comparator c）返回流中最大值
     * min（Comparator c）返回流中最小值
     * forEach（Consumer c）内部选代
     */
    @Test
    public void matchAndFindTest() {
        List<Employee> employees = Employee.getEmployees();
        //allMatch（Predicate p）检查是集合所有元素是否匹配某个规则
        boolean isAllDev = employees.stream().allMatch(employee -> employee.getDepartment().equals("Development"));
        System.out.println("是否所有的员工都是开发部门 : " + isAllDev);

        //anyMatch（Predicate p）检查是否至少一个元素匹配某个规则
        boolean isAtLeastOneDev = employees.stream().anyMatch(employee -> employee.getDepartment().equals("Development"));
        System.out.println("是否至少有一个员工是开发部门 : " + isAtLeastOneDev);

        //noneMatch（Predicate p）检查集合中所有元素都不匹配某个规则
        boolean isIdBiggerThan100 = employees.stream().noneMatch(employee -> employee.getId() > 100);
        System.out.println("是否所有员工ID都不大于100号 : " + isIdBiggerThan100);

//        findFirst（）返回第一个元素
        Optional<Employee> firstEmployee = employees.stream().findFirst();
        System.out.println(firstEmployee.get());

//        findAny（）返回当前流中的任意一个元素
        Optional<Employee> anyEmployee = employees.stream().findAny();
        System.out.println(anyEmployee);

//        count（）返回流中元素总数
        System.out.println("employees.stream().count() = " + employees.stream().count());

//        max（Comparator c）返回流中最大值
        Optional<Integer> maxId = employees.stream().map(Employee::getId).max(Integer::compare);
        System.out.println("max = " + maxId);

//        min（Comparator c）返回流中最小值
        Optional<Employee> min = employees.stream().min(Comparator.comparingInt(e -> e.getName().length()));
        System.out.println("min = " + min);
    }


    /**
     * 规约（求和或者其他算数运算）
     */
    @Test
    public void reduceTest() {
        List<Employee> employees = Employee.getEmployees();
        Optional<Integer> reduce = employees.stream().map(Employee::getId).reduce(new BinaryOperator<Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        });
        System.out.println("reduce = " + reduce);

        Optional<Integer> reduce1 = employees.stream().map(Employee::getId).reduce((o1, o2) -> o1 + o2);
        System.out.println("reduce1 = " + reduce1);
    }

    /**
     * 收集：
     */
    @Test
    public void collectionTest() {
        List<Employee> employees = Employee.getEmployees();
        List<Employee> manageList = employees.stream().filter(e -> e.getTitle().equals("Manager")).collect(Collectors.toList());
        manageList.forEach(System.out::println);
    }


}
