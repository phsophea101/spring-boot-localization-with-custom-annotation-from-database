package com.sample.spring.repository;

import com.sample.spring.entity.I18nEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface I18nRepository extends JpaRepository<I18nEntity, String> {
}
