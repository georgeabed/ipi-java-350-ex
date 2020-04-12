package com.ipiecoles.java.java350.model;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.ipiecoles.java.java350.exception.EmployeException;

public class EmployeTest {
	
	@Test
	public void testNbAnneeAncienneteNow() {
		
		//Given
		Employe employe = new Employe();
		employe.setDateEmbauche(LocalDate.now());
		
		//When
		Integer nbAnnees = employe.getNombreAnneeAnciennete();  //comme ma methode n'est pas static je ne peut pas faire ça Employe.get..donc je doit créer un instance de employe
		
		//Then
		Assertions.assertThat (nbAnnees).isEqualTo(0);  //la variable nbAnnees que je vais vérifie
	}
	
	@Test
	public void testNbAnneeAncienneteNowMinus2() {
			
			//Given
			Employe employe = new Employe();
			employe.setDateEmbauche(LocalDate.now().minusYears(2));
			
			//When
			Integer nbAnnees = employe.getNombreAnneeAnciennete();
			
			//Then
			Assertions.assertThat (nbAnnees).isEqualTo(2);  
		
	}
	
	@Test
	public void testNbAnneeAncienneteNowPlus3() {
			
			//Given
			Employe employe = new Employe();
			employe.setDateEmbauche(LocalDate.now().plusYears(3));
			
			//When
			Integer nbAnnees = employe.getNombreAnneeAnciennete();
			
			//Then
			Assertions.assertThat (nbAnnees).isEqualTo(0);     // parce qu'il n'y a pas encore date dembauche Now
		
	}
	
	@Test
	public void testNbAnneeAncienneteNowNull() {
			
			//Given
			Employe employe = new Employe();
			employe.setDateEmbauche(null);
			
			//When
			Integer nbAnnees = employe.getNombreAnneeAnciennete();
			
			//Then
			Assertions.assertThat (nbAnnees).isEqualTo(0);
		
	}
	
	//test unitaire normale
	@Test
	public void testGetPrimeAnnuelleCommercialPleinTempsPerfBase() {
		
		//Given
		Employe employe = new Employe();
		employe.setMatricule("C12345"); // pour le commercial on a le matricule qui commance par C
		employe.setTempsPartiel(1.0);    // pour le PleinTemps c'est 1
		employe.setDateEmbauche(LocalDate.now());
		employe.setPerformance(Entreprise.PERFORMANCE_BASE); // la performance de base qui égale à 1
		
		//When
		Double prime = employe.getPrimeAnnuelle();
		
		//Then
		Assertions.assertThat(prime).isEqualTo(1000d); // Parce que on a PRIME_BASE = 1000d
		
		
	}

/*
	  // test unitaire paramettre avant qu'on a fait une coverage par mutation  // On prend que la situation PleinTemps
	@ParameterizedTest
	@CsvSource({
		"'C12345', 1.0, 0, 1, 1000.0", //Je ne peux pas mettre PERFORMANCE_BASE, je doix le mettre en dure qui est égale à 1 dans CsvSource
		"'M12345', 1.0, 0, 1, 1700.0",  // Pour les managers, on a le matricule qui commance par M
		"'C12345', 1.0, 0, 2, 2300.0",  //Pour une personne qui a une PERFORMANCE diférent et pas de PERFORMANCE_BASE
		
	})
	public void testGetPrimeAnnuelle(String matricule, Double tempsPartiel, Integer nbAnneeAnciennete, Integer performance, Double primeCalculee) {  //ici je mis seulement GetPrimeAnnuelle et pas GetPrimeAnnuelleCommercialPleinTempsPerfBase parce qu'il prend tous les cas
		
		//Given
		Employe employe = new Employe();
		employe.setMatricule(matricule);
		employe.setTempsPartiel(tempsPartiel);
		employe.setDateEmbauche(LocalDate.now().minusYears(nbAnneeAnciennete));
		employe.setPerformance(performance);
		
		//When
		Double prime = employe.getPrimeAnnuelle();
		
		//Then
		Assertions.assertThat(prime).isEqualTo(primeCalculee);
		
		
	}
	
*/

	 // test unitaire paramettre après qu'on a fait une coverage par mutation pour mon teste soit plus précis en prenant la situation la personne en demi temps
		@ParameterizedTest
		@CsvSource({
			"'C12345', 1.0, 0, 1, 1000.0", //Je ne peux pas mettre PERFORMANCE_BASE, je doix le mettre en dure qui est égale à 1 dans CsvSource
			"'C12345', 0.5, 0, 1, 500.0",   // les gens en demi temps
			"'M12345', 1.0, 0, 1, 1700.0",  // Pour les managers, on a le matricule qui commance par M
			"'C12345', 1.0, 0, 2, 2300.0",  //Pour une personne qui a une PERFORMANCE diférent et pas de PERFORMANCE_BASE
			
		})
		public void testGetPrimeAnnuelle(String matricule, Double tempsPartiel, Integer nbAnneeAnciennete, Integer performance, Double primeCalculee) {  //ici je mis seulement GetPrimeAnnuelle et pas GetPrimeAnnuelleCommercialPleinTempsPerfBase parce qu'il prend tous les cas
			
			//Given
			Employe employe = new Employe();
			employe.setMatricule(matricule);
			employe.setTempsPartiel(tempsPartiel);
			employe.setDateEmbauche(LocalDate.now().minusYears(nbAnneeAnciennete));
			employe.setPerformance(performance);
			
			//When
			Double prime = employe.getPrimeAnnuelle();
			
			//Then
			Assertions.assertThat(prime).isEqualTo(primeCalculee);
			
			
		}
		
		@Test
		public void testAugmenterSalairePourcentage() throws EmployeException {
				
				//Given
				Double pourcentage = 20d;
				Employe employe = new Employe();  
				employe.setSalaire(1000d);
				
				//When
				employe.augmenterSalaire(pourcentage);
				
				//Then
				Double salaire = employe.getSalaire();
				Assertions.assertThat(employe.getSalaire()).isEqualTo(1200d);
			
		}
		
		@Test
		public void testAugmenterSalairePourcentage0() throws EmployeException {
				
				//Given
				Double pourcentage = 0d;
				Employe employe = new Employe(); 
				employe.setSalaire(1000d);
				
				//When
				employe.augmenterSalaire(pourcentage);
				
				//Then
				Double salaire = employe.getSalaire();
				Assertions.assertThat(employe.getSalaire()).isEqualTo(salaire);
			
		}
		
	
	@Test
	public void testAugmenterSalaire0() throws EmployeException {
			
			//Given
			Double pourcentage = 20d;
			Employe employe = new Employe();
			employe.setSalaire(0d);
			
			//When
			employe.augmenterSalaire(pourcentage);
			
			//Then
			Assertions.assertThat(employe.getSalaire()).isEqualTo(0);
		
	}

	
		
		@Test
		void testExceptionAugmenterSalaireNull() {

			//Given
			Double pourcentage = 20d;
			Employe employe = new Employe();
			employe.setSalaire(null);
			
			Assertions.assertThatThrownBy(() -> {
						//When
				employe.augmenterSalaire(pourcentage);
					})//Then
							.isInstanceOf(EmployeException.class).hasMessage("Salaire non renseigné ou négatif !");	
		}
		
		@Test
		void testExceptionAugmenterSalaireNegatif() {

			//Given
			Double pourcentage = 20d;
			Employe employe = new Employe();
			employe.setSalaire(-1000d);
			
			Assertions.assertThatThrownBy(() -> {
						//When
				employe.augmenterSalaire(pourcentage);
					})//Then
							.isInstanceOf(EmployeException.class).hasMessage("Salaire non renseigné ou négatif !");	
		}
		
		@Test
		void testExceptionAugmenterSalairePourcentageNull() {

			//Given
			Employe employe = new Employe();
			employe.setSalaire(1000d);
			Assertions.assertThatThrownBy(() -> {
						//When
				employe.augmenterSalaire(null);
					})//Then
							.isInstanceOf(EmployeException.class).hasMessage("pourcentage non renseigné ou négatif !");	
		}
		
		@Test
		void testExceptionAugmenterSalairePourcentageNegatif() {

			//Given
			Double pourcentage = -20d;
			Employe employe = new Employe();
			employe.setSalaire(1000d);
			Assertions.assertThatThrownBy(() -> {
						//When
				employe.augmenterSalaire(pourcentage);
					})//Then
							.isInstanceOf(EmployeException.class).hasMessage("pourcentage non renseigné ou négatif !");	
		}
		
		
		
	
	

}
