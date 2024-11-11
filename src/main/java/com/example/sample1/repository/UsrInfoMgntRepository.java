package com.example.sample1.repository;

import com.example.sample1.entity.UsrInfoMgnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsrInfoMgntRepository extends JpaRepository<UsrInfoMgnt, Long> {
       UsrInfoMgnt findByUsrIdAndTenantId(String usrId, String tenantId);

}
