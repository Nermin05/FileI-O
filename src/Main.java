//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
 ForFile forFile=new ForFile();
 forFile.readData("src\\data\\employees.txt");
//        System.out.println("Employees:");
//        for(Employee employee:forFile.employeeList){
//            System.out.println(employee);
//        }
        forFile.writeProcessed("src\\data\\processed_employees.txt");
        forFile.departmentSummary("src\\data\\processed_employees.txt");
        forFile.newEmployees("src\\data\\new_employees.txt");
        forFile.uniqueEmployees("src\\data\\unique_employees.txt");
        forFile.avarageSalaryByDepartment("src\\data\\average_salary_by_department.txt");
        forFile.longestServingEmployee("src\\data\\longest_serving_employee.txt");
forFile.forPosition("src\\data\\employees_by_position.txt.");
    }
}