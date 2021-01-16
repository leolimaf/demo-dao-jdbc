package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Program2 {

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);
        Scanner scan = new Scanner(System.in);

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== Test 1: department findById ===");
        Department department = departmentDao.findById(2);
        System.out.println(department);

        System.out.println("\n=== Test 2: department findAll ===");
        List<Department> departments = departmentDao.findAll();
        departments.forEach(System.out::println);

        System.out.println("\n=== Test 3: department insert ===");
        Department department1 = new Department(null, "Games");
        departmentDao.insert(department1);
        System.out.println("Inserted! New id: " + department1.getId());

        System.out.println("\n=== Test 4: department update ===");
        Department department2 = departmentDao.findById(5);
        department2.setName("Games and Stuff");
        departmentDao.update(department2);
        System.out.println("Update completed!");

        System.out.println("\n=== Test 5: department delete ===");
        System.out.print("Enter id for delete test: ");
        int id = scan.nextInt();
        departmentDao.deleteById(id);
        System.out.println("Delete completed!");

        scan.close();
    }
}
