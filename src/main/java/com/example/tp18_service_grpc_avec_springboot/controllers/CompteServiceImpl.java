package com.example.tp18_service_grpc_avec_springboot.controllers;

import com.example.tp18_service_grpc_avec_springboot.entities.Compte;
import com.example.tp18_service_grpc_avec_springboot.services.CompteService;
import com.example.tp18_service_grpc_avec_springboot.stubs.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class CompteServiceImpl extends CompteServiceGrpc.CompteServiceImplBase {
    private final CompteService compteService;

    public CompteServiceImpl(CompteService compteService) {
        this.compteService = compteService;
    }

    @Override
    public void allComptes(GetAllComptesRequest request,
                           StreamObserver<GetAllComptesResponse> responseObserver) {
        List<Compte> comptesEntities = compteService.findAllComptes();

        List<com.example.tp18_service_grpc_avec_springboot.stubs.Compte> comptesGrpc = comptesEntities.stream()
                .map(compte -> com.example.tp18_service_grpc_avec_springboot.stubs.Compte.newBuilder()
                        .setId(compte.getId())
                        .setSolde(compte.getSolde())
                        .setDateCreation(compte.getDateCreation())
                        .setType(TypeCompte.valueOf(compte.getType()))
                        .build())
                .collect(Collectors.toList());

        GetAllComptesResponse response = GetAllComptesResponse.newBuilder()
                .addAllComptes(comptesGrpc)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void compteById(GetCompteByIdRequest request,
                           StreamObserver<GetCompteByIdResponse> responseObserver) {
        Compte compteEntity = compteService.findCompteById(request.getId());

        if (compteEntity != null) {
            com.example.tp18_service_grpc_avec_springboot.stubs.Compte compteGrpc =
                    com.example.tp18_service_grpc_avec_springboot.stubs.Compte.newBuilder()
                            .setId(compteEntity.getId())
                            .setSolde(compteEntity.getSolde())
                            .setDateCreation(compteEntity.getDateCreation())
                            .setType(TypeCompte.valueOf(compteEntity.getType()))
                            .build();

            GetCompteByIdResponse response = GetCompteByIdResponse.newBuilder()
                    .setCompte(compteGrpc)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(
                    io.grpc.Status.NOT_FOUND
                            .withDescription("Compte avec l'ID " + request.getId() + " non trouve")
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void totalSolde(GetTotalSoldeRequest request,
                           StreamObserver<GetTotalSoldeResponse> responseObserver) {
        List<Compte> comptes = compteService.findAllComptes();

        int count = comptes.size();
        float sum = 0;

        for (Compte compte : comptes) {
            sum += compte.getSolde();
        }

        float average = count > 0 ? sum / count : 0;

        SoldeStats stats = SoldeStats.newBuilder()
                .setCount(count)
                .setSum(sum)
                .setAverage(average)
                .build();

        GetTotalSoldeResponse response = GetTotalSoldeResponse.newBuilder()
                .setStats(stats)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void saveCompte(SaveCompteRequest request,
                           StreamObserver<SaveCompteResponse> responseObserver) {
        CompteRequest compteReq = request.getCompte();

        Compte compteEntity = new Compte();
        compteEntity.setSolde(compteReq.getSolde());
        compteEntity.setDateCreation(compteReq.getDateCreation());
        compteEntity.setType(compteReq.getType().name());

        Compte savedCompte = compteService.saveCompte(compteEntity);

        com.example.tp18_service_grpc_avec_springboot.stubs.Compte compteGrpc =
                com.example.tp18_service_grpc_avec_springboot.stubs.Compte.newBuilder()
                        .setId(savedCompte.getId())
                        .setSolde(savedCompte.getSolde())
                        .setDateCreation(savedCompte.getDateCreation())
                        .setType(TypeCompte.valueOf(savedCompte.getType()))
                        .build();

        SaveCompteResponse response = SaveCompteResponse.newBuilder()
                .setCompte(compteGrpc)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}