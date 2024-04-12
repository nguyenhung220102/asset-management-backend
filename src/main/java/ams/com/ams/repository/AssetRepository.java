package ams.com.ams.repository;

import ams.com.ams.model.Asset;
import ams.com.ams.model.Department;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
      List<Asset> findByCategoryId(Long category_id);
      List<Asset> findByStatus(String status);
      List<Asset> findByDepartmentIsNull();
      List<Asset> findByDepartment(Department department);

}
