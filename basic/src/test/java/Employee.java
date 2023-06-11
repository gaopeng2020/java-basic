import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private String name;
    private int id;
    private String title;
    private String department;

    public static List<Employee> getEmployees() {
        List<Employee> list = new ArrayList<>();
        list.add(Employee.builder().name("zhangsan").id(2).title("Sales Man").department("Sales").build());
        list.add(Employee.builder().name("xiaohei").id(1).title("Manager").department("Management").build());
        list.add(Employee.builder().name("xiaoma").id(4).title("lawyer").department("Business").build());
        list.add(Employee.builder().name("lisi").id(3).title("Sales Man").department("Sales").build());
        list.add(Employee.builder().name("xiaohua").id(6).title("Engineer").department("Development").build());
        list.add(Employee.builder().name("xiaoming").id(5).title("Engineer").department("Development").build());
        return list;
    }
}
