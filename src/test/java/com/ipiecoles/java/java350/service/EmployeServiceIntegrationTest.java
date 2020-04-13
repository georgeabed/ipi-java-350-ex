package com.ipiecoles.java.java350.service;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;

@SpringBootTest
public class EmployeServiceIntegrationTest {
	
	@Autowired
	EmployeService employeService;
	
	@Autowired
	EmployeRepository employeRepository;
	
	@Test
	void testEmbaucheEmploye() throws EmployeException {

			//Given
			Employe employeInitial = new Employe("Doe", "Jane", "C45678", LocalDate.now(), 2000d, 1, 1d);//nom, prenom, matricule, LocalDate, salaire, performance, tempsPartiel
			employeRepository.save(employeInitial);
			String nom = "Doe";
			String prenom = "John";
			Poste poste = Poste.MANAGER;
			NiveauEtude niveauEtude = NiveauEtude.LICENCE;
			Double tempsPartiel = 1.0;

			//When
			employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel); 

			//Then
			Employe employe = employeRepository.findByMatricule("M45679");

			Assertions.assertThat (employe.getNom()).isEqualTo(nom);
			Assertions.assertThat (employe.getPrenom()).isEqualTo(prenom);
			Assertions.assertThat (employe.getMatricule()).isEqualTo("M45679");
			Assertions.assertThat (employe.getTempsPartiel()).isEqualTo(tempsPartiel);
			Assertions.assertThat (employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
			Assertions.assertThat (employe.getDateEmbauche()).isEqualTo(LocalDate.now());
			//1521.22 * 1.2 * 1.0 = 1825.46
			Assertions.assertThat (employe.getSalaire()).isEqualTo(1825.46);
		}
	
	//* 2 : Si le chiffre d'affaire est inférieur entre 20% et 5% par rapport à l'ojectif fixé, il perd 2 de performance (dans la limite de la performance de base)
			@Test
			void testCalculPerformanceCommercialCas2() throws EmployeException {

				//Given
				//Quand on va chercher si l'employé avec la matricule existe, on veut que la méthode
				//renvoie l'employé correspondand
				String nom = "Doe";
				String prenom = "John";
				String matricule = "C12345";
				Long caTraite = 1800L;
				Long objectifCa = 2000L;
				Employe employeInitial = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0);   //(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel)
				employeRepository.save(employeInitial);
					
				//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

				//Then		performance = performanceMoyenne = 1
				Employe employe = employeRepository.findByMatricule(matricule);
				Assertions.assertThat (employe.getPerformance()).isEqualTo(1);

			}
			
			//* 3 : Si le chiffre d'affaire est entre -5% et +5% de l'objectif fixé, la performance reste la même.
			@Test
			void testCalculPerformanceCommercialCas3() throws EmployeException {

				//Given
				//Quand on va chercher si l'employé avec la matricule existe, on veut que la méthode
				//renvoie l'employé correspondand
				String nom = "Doe";
				String prenom = "John";
				String matricule = "C12345";
				Long caTraite = 2000L;
				Long objectifCa = 2000L;
				Employe employeInitial = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0);   //(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel)
				employeRepository.save(employeInitial);
				
		
				//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

				//Then        performance = performanceMoyenne = 1
				Employe employe = employeRepository.findByMatricule(matricule);
				Assertions.assertThat (employe.getPerformance()).isEqualTo(1);

			}
			
			// * 4 : Si le chiffre d'affaire est supérieur entre 5 et 20%, il gagne 1 de performance
			@Test
			void testCalculPerformanceCommercialCas4() throws EmployeException {

				//Given
				//Quand on va chercher si l'employé avec la matricule existe, on veut que la méthode
				//renvoie l'employé correspondand
				String nom = "Doe";
				String prenom = "John";
				String matricule = "C12345";
				Long caTraite = 2200L;
				Long objectifCa = 2000L;
				Employe employeInitial = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0);   //(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel)
				employeRepository.save(employeInitial);
				
		
				//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

				//Then			performance > performanceMoyenne
				Employe employe = employeRepository.findByMatricule(matricule);
				Assertions.assertThat (employe.getPerformance()).isEqualTo(3);

			}
		
		//  * 5 : Si le chiffre d'affaire est supérieur de plus de 20%, il gagne 4 de performance
			@Test
			void testCalculPerformanceCommercialCas5() throws EmployeException {

				//Given
				//Quand on va chercher si l'employé avec la matricule existe, on veut que la méthode
				//renvoie l'employé correspondand
				String nom = "Doe";
				String prenom = "John";
				String matricule = "C12345";
				Long caTraite = 2500L;
				Long objectifCa = 2000L;
				Employe employeInitial = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0);   //(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel)
				employeRepository.save(employeInitial);
				
		
				//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

				//Then	     performance > performanceMoyenne
				Employe employe = employeRepository.findByMatricule(matricule);
				Assertions.assertThat (employe.getPerformance()).isEqualTo(6);

			}
			
			
			 //* 1 : Si le chiffre d'affaire est inférieur de plus de 20% à l'objectif fixé, le commercial retombe à la performance de base
			@Test
			void testCalculPerformanceCommercialCas1() throws EmployeException {

				//Given
				//Quand on va chercher si l'employé avec la matricule existe, on veut que la méthode
				//renvoie l'employé correspondand
				String nom = "Doe";
				String prenom = "John";
				String matricule = "C12345";
				Long caTraite = 1000L;
				Long objectifCa = 2000L;
			
				Employe employeInitial = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0);   //(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel)
				employeRepository.save(employeInitial);
				
				//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

				//Then	   performance = performanceMoyenne = 1
				Employe employe = employeRepository.findByMatricule(matricule);
				Assertions.assertThat (employe.getPerformance()).isEqualTo(1);
			

			}


	
	@BeforeEach
	void setup() {
		employeRepository.deleteAll();
	}

		
}

