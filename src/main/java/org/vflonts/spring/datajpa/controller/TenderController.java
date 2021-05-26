package org.vflonts.spring.datajpa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.vflonts.spring.datajpa.model.Tender;
import org.vflonts.spring.datajpa.repository.TenderRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TenderController {

	@Autowired
	TenderRepository tenderRepository;

	@GetMapping("/tenders")
	public ResponseEntity<List<Tender>> getAllTenders(@RequestParam(required = false) String title) {
		try {
			List<Tender> tenders = new ArrayList<Tender>();

			if (title == null)
				tenderRepository.findAll().forEach(tenders::add);
			else
				tenderRepository.findByTitleContaining(title).forEach(tenders::add);

			if (tenders.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tenders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/tenders/{id}")
	public ResponseEntity<Tender> getTenderById(@PathVariable("id") long id) {
		Optional<Tender> tenderData = tenderRepository.findById(id);

		if (tenderData.isPresent()) {
			return new ResponseEntity<>(tenderData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/tenders")
	public ResponseEntity<Tender> createTender(@RequestBody Tender tender) {
		try {
			Tender _tender = tenderRepository
					.save(new Tender(tender.getTitle(), tender.getDescription(), false));
			return new ResponseEntity<>(_tender, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/tenders/{id}")
	public ResponseEntity<Tender> updateTender(@PathVariable("id") long id, @RequestBody Tender tender) {
		Optional<Tender> tenderData = tenderRepository.findById(id);

		if (tenderData.isPresent()) {
			Tender _tender = tenderData.get();
			_tender.setTitle(tender.getTitle());
			_tender.setDescription(tender.getDescription());
			_tender.setPublished(tender.isPublished());
			return new ResponseEntity<>(tenderRepository.save(_tender), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/tenders/{id}")
	public ResponseEntity<HttpStatus> deleteTender(@PathVariable("id") long id) {
		try {
			tenderRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/tenders")
	public ResponseEntity<HttpStatus> deleteAllTenders() {
		try {
			tenderRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/tenders/published")
	public ResponseEntity<List<Tender>> findByPublished() {
		try {
			List<Tender> tenders = tenderRepository.findByPublished(true);

			if (tenders.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tenders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
