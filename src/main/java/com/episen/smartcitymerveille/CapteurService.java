package com.episen.smartcitymerveille;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CapteurService {
	
	@Autowired
    private CapteurRepository repo;
     
    public List<Capteur> listAll() {
        return repo.findAll();
    }
     
    public void save(Capteur capteur) {
        repo.save(capteur);
    }
     
    public Capteur get(long id) {
        return repo.findById(id).get();
    }
     
    public void delete(long id) {
        repo.deleteById(id);
    }

}
