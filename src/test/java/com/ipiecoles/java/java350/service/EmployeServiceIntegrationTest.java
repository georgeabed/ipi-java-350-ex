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
	
	@BeforeEach
	void setup() {
		employeRepository.deleteAll();
	}

		
}

