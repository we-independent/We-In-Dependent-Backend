package com.weindependent.repository;

import com.weindependent.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


/**
 * @author zetor
 * @date 2019-02-12
 */
public interface SysUserRepository extends JpaSpecificationExecutor<SysUser>, JpaRepository<SysUser, Integer> {

    SysUser findByUsername(String username);

    /**
     * @param account
     * @param sob
     * @return
     */
    @Query(value = "select u.* from t_system_user u left join t_system_usersob us on u.account = us.account and us.sob = ?2 where u.account = ?1", nativeQuery = true)
    SysUser qryUser(String account, Integer sob);
}
