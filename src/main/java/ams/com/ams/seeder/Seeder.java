package ams.com.ams.seeder;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import ams.com.ams.model.Asset;
import ams.com.ams.model.BorrowingRecord;
import ams.com.ams.model.Category;
import ams.com.ams.model.Department;
import ams.com.ams.model.User;
import ams.com.ams.repository.AssetRepository;
import ams.com.ams.repository.BorrowingRecordRepository;
import ams.com.ams.repository.CategoryRepository;
import ams.com.ams.repository.DepartmentRepository;
import ams.com.ams.repository.UserRepository;

@Component
public class Seeder implements CommandLineRunner {
	@Autowired
	UserRepository userRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	AssetRepository assetRepository;

	@Autowired
	DepartmentRepository departmentRepository;

	@Autowired
	BorrowingRecordRepository borrowingRecordRepository;

	@Override
	public void run(String... args) throws Exception {
		loadUserData();
		loadCategoryData();
		// loadAssetData();
		loadDepartmentData();
		loadBorrowingData();
	}

	private void loadUserData() {
		if (userRepository.count() == 0) {
			User manager1 = new User("manager1", BCrypt.hashpw("manager1", BCrypt.gensalt()), "Manager");
			User manager2 = new User("manager2", BCrypt.hashpw("manager2", BCrypt.gensalt()), "Manager");
			userRepository.save(manager1);
			userRepository.save(manager2);

			String[] employeeUsernames = { "employee1", "employee2", "employee3", "employee4", "employee5", "employee6",
					"employee7", "employee8" };
			for (String username : employeeUsernames) {
				User employee = new User(username, BCrypt.hashpw(username, BCrypt.gensalt()), "Employee");
				userRepository.save(employee);
			}
		}
	}

	private void loadCategoryData() {
		if (categoryRepository.count() == 0) {
			List<String> categoryNames = Arrays.asList("Laptop", "Mobile", "Tablet", "Printer", "Projector", "Desk",
					"Chair");
			for (String categoryName : categoryNames) {
				Category category = new Category(categoryName);
				categoryRepository.save(category);
			}
		}
	}

	private void loadAssetData() {
		if (assetRepository.count() == 0) {
			List<Category> categories = categoryRepository.findAll();
			for (Category category : categories) {
				for (int i = 1; i <= 5; i++) {
					String assetName = category.getName() + " " + ((char) (65 + i));
					Asset asset = new Asset(assetName, category);
					assetRepository.save(asset);
				}
			}
		}
	}

	private void loadDepartmentData() {
		if (departmentRepository.count() == 0) {
			List<User> employees = userRepository.findByRole("Employee");

			char departmentName = 'A';
			for (User employee : employees) {
            Department department = new Department();
            department.setName("Department " + departmentName);
            List<User> users = new ArrayList<>();
            users.add(employee);
			employee.setDepartment(department);
            department.setUsers(users);
            departmentRepository.save(department);
			userRepository.save(employee);
            departmentName++;
			}
		}
	}

	private void loadBorrowingData() {
    if (assetRepository.count() == 0) {
        List<Category> categories = categoryRepository.findAll();
        List<Department> departments = departmentRepository.findAll();
        
        for (Category category : categories) {
            for (int i = 1; i <= 5; i++) {
                String assetName = category.getName() + " " + ((char) (65 + i));
                Asset asset = new Asset(assetName, category);
				assetRepository.save(asset);
                if (!asset.getStatus().equals("AVAILABLE")) {
                    continue;
                }
                Random random = new Random();
                Department randomDepartment = departments.get(random.nextInt(departments.size()));
                BorrowingRecord borrowingRecord = new BorrowingRecord();
                borrowingRecord.setAsset(asset);
                borrowingRecord.setDepartment(randomDepartment);
                borrowingRecord.setStatus("IN USE" );
                asset.setStatus("IN USE: " + randomDepartment.getName());
                assetRepository.save(asset);
                borrowingRecordRepository.save(borrowingRecord);
            }
			for (int i = 6; i <= 10; i++) {
                String assetName = category.getName() + " " + ((char) (65 + i));
                Asset asset = new Asset(assetName, category);
				assetRepository.save(asset);
            }
        }
    }
}
}