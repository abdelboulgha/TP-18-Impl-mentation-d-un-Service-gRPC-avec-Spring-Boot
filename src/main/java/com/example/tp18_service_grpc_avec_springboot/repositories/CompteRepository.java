package com.example.tp18_service_grpc_avec_springboot.repositories;




import com.example.tp18_service_grpc_avec_springboot.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteRepository extends JpaRepository<Compte, String> {
}
