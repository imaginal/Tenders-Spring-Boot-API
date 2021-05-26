package org.vflonts.spring.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.vflonts.spring.datajpa.model.Tender;

public interface TenderRepository extends JpaRepository<Tender, Long> {
	List<Tender> findByPublished(boolean published);
	List<Tender> findByTitleContaining(String title);
}
