import enums.Department;
import enums.Position;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class ForFile {
    public List<Employee> employeeList;
    public List<Employee> processedEmployees;
    public Map<Department, List<Employee>> departmentSummaryMap;
    public List<Employee> newEmployeesList;
    public Set<Employee> uniqueEmployeesSet;
public Map<Department, Double> avarageSalaryMap;
    public Map<Position, Long> countsForPosition;
    public Map<Department, Map<Position, List<Employee>>> groupForDepartmentAndPosition;


    public ForFile() {
        employeeList = new ArrayList<>();
        processedEmployees = new ArrayList<>();
        newEmployeesList = new ArrayList<>();
        uniqueEmployeesSet = new HashSet<>();

    }

    public void readData(String fileName) {//1
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] ayirma = line.split(",");
                int id = Integer.parseInt(ayirma[0].trim());
                String name = ayirma[1].trim();
                String surname = ayirma[2].trim();
                int age = Integer.parseInt(ayirma[3].trim());
                double salary = Double.valueOf(ayirma[4].trim());
                Department department = Department.valueOf(ayirma[5].trim());
                boolean isEmployee = Boolean.parseBoolean(ayirma[6].trim());
                LocalDate start = LocalDate.parse(ayirma[7].trim());
                String email = ayirma[8].trim();
                String phone = ayirma[9].trim();
                Position position = Position.valueOf(ayirma[10].trim());
                Employee employee = new Employee(id, name, surname, age, salary, department, isEmployee, start, email,
                        phone, position);
                employeeList.add(employee);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeProcessed(String fileName) {//2,3
        employeeList.stream().filter(employee -> employee.getPosition() != Position.INTERN)
                .sorted(Comparator.comparing(Employee::getStartDate)).map(employee -> {
                    if (employee.getDepartment() == Department.IT &&
                            Period.between(employee.getStartDate(), LocalDate.now()).getYears() > 5) {
                        employee.setSalary(employee.getSalary()*1.10);
                    }
                    return employee;
                }).forEach(processedEmployees::add);
        System.out.println("Processed employees:");
        for (Employee employee : processedEmployees) {
            System.out.println(employee);
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (Employee employee : processedEmployees) {
                bufferedWriter.write(employee + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void departmentSummary(String fileName) {//4
        departmentSummaryMap = employeeList.stream().collect(Collectors.groupingBy(Employee::getDepartment));

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<Department, List<Employee>> employee : departmentSummaryMap.entrySet())
                bufferedWriter.write(employee.getKey() + " " + employee.getValue() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void newEmployees(String fileName) {//4
        newEmployeesList = employeeList.stream()
                .filter(employee -> Period.between(employee.getStartDate(), LocalDate.now()).getYears() < 3)
                .collect(Collectors.toList());
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (Employee employee : newEmployeesList) {
                bufferedWriter.write(employee + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void uniqueEmployees(String fileName){//5
        for(Employee employee:employeeList) {
            uniqueEmployeesSet.add(employee);
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (Employee employee : uniqueEmployeesSet) {
                bufferedWriter.write(employee + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void avarageSalaryByDepartment(String fileName) {//6
        avarageSalaryMap = employeeList.stream().collect(Collectors.groupingBy(Employee::getDepartment,
                Collectors.summingDouble(Employee::getSalary)));

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<Department, Double> employee : avarageSalaryMap.entrySet())
                bufferedWriter.write(employee.getKey() + " " + employee.getValue() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void longestServingEmployee(String fileName) {//7
    Optional<Employee> aLong= employeeList.stream().min(Comparator.comparing(Employee::getStartDate));
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
           bufferedWriter.write(String.valueOf(aLong.get()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void forPosition(String fileName) {//8
        countsForPosition = employeeList.stream().collect(Collectors.groupingBy(Employee::getPosition,
                Collectors.counting()));

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<Position,Long> employee : countsForPosition.entrySet())
                bufferedWriter.write(employee.getKey() + " " + employee.getValue() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void forDepartmentAndPosition(String fileName) {//9
        groupForDepartmentAndPosition = employeeList.stream().collect(Collectors.groupingBy(Employee::getDepartment,
                Collectors.groupingBy(Employee::getPosition)));

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<Department, Map<Position, List<Employee>>> departmentEntry : groupForDepartmentAndPosition.entrySet()) {
                Department department = departmentEntry.getKey();
                bufferedWriter.write("Department: " + department.name() + "\n");

                for (Map.Entry<Position, List<Employee>> positionEntry : departmentEntry.getValue().entrySet()) {
                    Position position = positionEntry.getKey();
                    bufferedWriter.write("Department:" + position.name() + "\n");

                    for (Employee employee : positionEntry.getValue()) {
                        bufferedWriter.write(employee+"\n");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
