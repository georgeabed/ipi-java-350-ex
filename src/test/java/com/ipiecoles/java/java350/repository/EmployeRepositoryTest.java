package com.ipiecoles.java.java350.repository;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ipiecoles.java.java350.model.Employe;

@SpringBootTest
public class EmployeRepositoryTest {
	
	@Autowired
	EmployeRepository employeRepository; // EmployeRepository c'est une interface je ne peux pas faire EmployeRepository employeRepository = new EmployeRepository donc je utilise @Autowired pour dire à spring donne moi employeRepository  
	
	@BeforeEach
	void setUp() {
		employeRepository.deleteAll();
	}

	@Test
	void findLastMatricule0Employe() { //quand le base vide

		//Given
		
		
		//When
		String lastMatricule = employeRepository.findLastMatricule();   // ex: on a les matricule C12345 et M65432 cette fonction ignore le premier caractère et il va récupérer la valeur numérique le 5 dernier caractère de matricule et quand le nouveau employe est embouché il prend ledernier matricule et il ajoute plus un pour le nouveau employe  


		//Then
		Assertions.assertThat (lastMatricule).isNull();

	}

	@Test
	void findLastMatricule3Employe() { //avec plusieur employes

		//Given
		Employe e1 = new Employe("Doe", "John", "T12345", LocalDate.now(), 2000d, 1, 1.0); // nom, prenom, matricule(Technicien), LocalDate.now(), salaire, performance, tempsPartiel 
		Employe e2 = new Employe("Doe", "Jane", "C67890", LocalDate.now(), 2000d, 1, 1.0);  // il va renvoie la valeur 67890 le plus haut dans le matricule
		Employe e3 = new Employe("Doe", "Jane", "M45678", LocalDate.now(), 2000d, 1, 1.0);
		
		employeRepository.save(e1);
		employeRepository.save(e2);
		employeRepository.save(e3);
		
		//When
		String lastMatricule = employeRepository.findLastMatricule(); 


		//Then
		Assertions.assertThat (lastMatricule).isEqualTo("67890");

	}
	
	
	@Test
	void testAvgPerformanceWhereMatriculeStartsWith() { 

		//Given
		Employe e1 = new Employe("Doe", "John", "C12345", LocalDate.now(), 2000d, 2, 1.0); 
		Employe e2 = new Employe("Doe", "Jane", "C67890", LocalDate.now(), 2000d, 2, 1.0);  
		Employe e3 = new Employe("Doe", "Jane", "C45678", LocalDate.now(), 2000d, 2, 1.0);
		
		employeRepository.save(e1);
		employeRepository.save(e2);
		employeRepository.save(e3);
		
		//When
		Double avgPerformance = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");  


		//Then
		Assertions.assertThat (avgPerformance).isEqualTo(2);

	}



}
