package com.weindependent.repository;

import com.weindependent.entity.SysSob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zetor
 * @date 2019-02-12
 */
public interface SysSobRepository extends JpaSpecificationExecutor<SysSob>, JpaRepository<SysSob, Integer> {

    @Query(value = "select * from t_system_sob where is_delete = 0", nativeQuery = true)
    List<SysSob> findSobs();
}
