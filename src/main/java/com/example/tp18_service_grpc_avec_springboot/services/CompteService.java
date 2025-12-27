package com.example.tp18_service_grpc_avec_springboot.services;


import com.example.tp18_service_grpc_avec_springboot.entities.Compte;
import com.example.tp18_service_grpc_avec_springboot.repositories.CompteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompteService {
    private final CompteRepository compteRepository;

    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    public List<Compte> findAllComptes() {
        return compteRepository.findAll();
    }

    public Compte findCompteById(String id) {
        return compteRepository.findById(id).orElse(null);
    }

    public Compte saveCompte(Compte compte) {
        return compteRepository.save(compte);
    }

    public void deleteCompte(String id) {
        compteRepository.deleteById(id);
    }
}
