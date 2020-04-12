package com.ipiecoles.java.java350.service;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.EntityExistsException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;


@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {

	//@Autowired : ça permet la récupération de service par spring mais on a dit qu'on ne veut pas chargé le contexte de spring et utilisation de vrai de base de données donc on va utilisé les annotations de Mockito
	@InjectMocks 
	EmployeService employeService;

	@Mock // EmployeRepositor il va être simuler
	EmployeRepository employeRepository;
	
	@BeforeEach
	void setup() {
		//Réinitialisation des mocks
		MockitoAnnotations.initMocks(this.getClass());
	}

	@Test
	void embaucheEmploye0Employe() throws EmployeException {

		//Given
		//Quand la méthode findLastMatricule va être appelée, on veut qu'elle renvoie null
		//pour simuler une base employé vide
		Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);

		//Quand on va chercher si l'employé avec la matricule calculé existe, on veut que la méthode
		//renvoie null
		Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(null); //  C00001  parce que c'est la premier lettre de commercial c'est C + le dernier matricule et comme il n'y a pas matricule c'est 0000 + 1 parce que il a été incrémenté
		
		//Quand on fait une save d'un employé, on renvoie exactement l'employé passé en paramètre du save
		Mockito.when(employeRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());


		//When
		employeService.embaucheEmploye("Doe", "John", Poste.COMMERCIAL, NiveauEtude.LICENCE, 1.0); 

		//Then
		
		
	}
	


	@Test
	void embaucheEmployeXEmployes() throws EmployeException {  //Plusieur employe dans la base

		//Given
		//Quand la méthode findLastMatricule va être appelée, on veut qu'elle renvoie une valeur comme s'il 
		//y avait plusieur employés, dont le matricule le plus élevé est C45678
		Mockito.when(employeRepository.findLastMatricule()).thenReturn("45678");

		//Quand on va chercher si l'employé avec la matricule calculé existe, on veut que la méthode
		//renvoie null
		Mockito.when(employeRepository.findByMatricule("M45679")).thenReturn(null); 

		//Quand on fait une save d'un employé, on renvoie exactement l'employé passé en paramètre du save
		Mockito.when(employeRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

		String nom = "Doe";
		String prenom = "John";
		Poste poste = Poste.MANAGER;
		NiveauEtude niveauEtude = NiveauEtude.LICENCE;
		Double tempsPartiel = 1.0;

		//When
		employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel); 

		//Then
		//Employe employe = employeRepository.findByMatricule("M45679");
		ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
		//On vérifie que la méthode save a bien été appelée sur employeRepository, et on capture le paramètre
		Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
		Employe employe = employeArgumentCaptor.getValue();

		Assertions.assertThat (employe.getNom()).isEqualTo(nom);
		Assertions.assertThat (employe.getPrenom()).isEqualTo(prenom);
		Assertions.assertThat (employe.getMatricule()).isEqualTo("M45679");
		Assertions.assertThat (employe.getTempsPartiel()).isEqualTo(tempsPartiel);
		Assertions.assertThat (employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
		Assertions.assertThat (employe.getDateEmbauche()).isEqualTo(LocalDate.now());
		//1521.22 * 1.2 * 1.0 = 1825.46
		Assertions.assertThat (employe.getSalaire()).isEqualTo(1825.46);
	}

	
	void testExceptionNormal() {
		//Given


		try {
			//When
			employeService.embaucheEmploye(null, null, null, null, null);
			Assertions.fail("Aurait du lancer une exception");  //au cas ou l'appele de service ne lève pas une exception 

		} catch (Exception e) {
			//Then
			//Vérifie que l'exception levée est de type EmployeException
			Assertions.assertThat (e).isInstanceOf(EmployeException.class);
			//Vérifie le contenu du message
			Assertions.assertThat (e.getMessage()).isEqualTo("Message de l'erreur");

		}

	}


	@Test
	void testEmbaucheEmployeLimiteMatricule() { //Test EmployeException
		//Given
		//Quand la méthode findLastMatricule va être appelée, on veut qu'elle renvoie une valeur comme s'il 
		//y avait 99999 employés, dont le matricule le plus élevé est X99999
		Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");


		try {
			//When
			employeService.embaucheEmploye("Doe", "John", Poste.MANAGER, NiveauEtude.BTS_IUT, 1.0);
			Assertions.fail("Aurait du lancer une exception");  //au cas ou l'appele de service ne lève pas une exception 

		} catch (Exception e) {
			//Then
			//Vérifie que l'exception levée est de type EmployeException
			Assertions.assertThat (e).isInstanceOf(EmployeException.class);
			//Vérifie le contenu du message
			Assertions.assertThat (e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");

		}

	}

	
		@Test
	void testEmbaucheEmployeExistant() {  //Test EntityExistsException
		//Given
		Mockito.when(employeRepository.findLastMatricule()).thenReturn("55555");
		//Quand on va chercher si l'employe avec le matricule calculé existe, on veut que la méthode
		//renvoie un employé qui aurait été créé dans l'intervalle
		Mockito.when(employeRepository.findByMatricule(Mockito.anyString())).thenReturn(new Employe("Doe", "Jane", "M55555", null, null, null, null));
		
		
		Assertions.assertThatThrownBy(() -> {
					//When
					employeService.embaucheEmploye("Doe", "John", Poste.MANAGER, NiveauEtude.BTS_IUT, 1.0);
				})//Then
					.isInstanceOf(EntityExistsException.class).hasMessage("L'employé de matricule M55556 existe déjà en BDD");	
	}

/*	
	void testExceptionJava8() {
		//Given
		Assertions.assertThatThrownBy(() -> {
			//When
			employeService.embaucheEmploye(null, null, null, null, null);
		})//Then
				.isInstanceOf(EmployeException.class).hasMessage("Message de l'erreur");

	}
*/
		
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
			Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0);   //(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel)
			Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
				
			//When
			employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

			//Then
			ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
			//On vérifie que la méthode save a bien été appelée sur employeRepository, et on capture le paramètre
			Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
			employe = employeArgumentCaptor.getValue();
			
			Assertions.assertThat (employe.getPerformance()).isEqualTo(2);

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
			Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0);   //(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel)
			Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
			
	
			//When
			employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

			//Then
			ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
			//On vérifie que la méthode save a bien été appelée sur employeRepository, et on capture le paramètre
			Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
			employe = employeArgumentCaptor.getValue();
			
			Assertions.assertThat (employe.getPerformance()).isEqualTo(2);

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
			Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0);   //(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel)
			Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
			
	
			//When
			employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

			//Then
			ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
			//On vérifie que la méthode save a bien été appelée sur employeRepository, et on capture le paramètre
			Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
			employe = employeArgumentCaptor.getValue();
			
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
			Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0);   //(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel)
			Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
			
	
			//When
			employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

			//Then
			ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
			//On vérifie que la méthode save a bien été appelée sur employeRepository, et on capture le paramètre
			Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
			employe = employeArgumentCaptor.getValue();
			
			Assertions.assertThat (employe.getPerformance()).isEqualTo(6);

		}


	    
			@Test
			void testCalculPerformanceCommercialCaTraite0() throws EmployeException {

				//Given
				//Quand on va chercher si l'employé avec la matricule existe, on veut que la méthode
				//renvoie l'employé correspondand
				String nom = "Doe";
				String prenom = "John";
				String matricule = "C12345";
				Long caTraite = 0L;
				Long objectifCa = 2000L;
			
				Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0);   //(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel)
				Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
				
				//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

				//Then
				ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
				//On vérifie que la méthode save a bien été appelée sur employeRepository, et on capture le paramètre
				Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
				employe = employeArgumentCaptor.getValue();
				
				// => Cas 1
				Assertions.assertThat (employe.getPerformance()).isEqualTo(2);
			

			}
			

			@Test
			void testCalculPerformanceCommercialObjectifCa0() throws EmployeException {

				//Given
				//Quand on va chercher si l'employé avec la matricule existe, on veut que la méthode
				//renvoie l'employé correspondand
				String nom = "Doe";
				String prenom = "John";
				String matricule = "C12345";
				Long caTraite = 2000L;
				Long objectifCa = 0L;
			
				Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0);   //(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel)
				Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
				
				//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

				//Then
				ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
				//On vérifie que la méthode save a bien été appelée sur employeRepository, et on capture le paramètre
				Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
				employe = employeArgumentCaptor.getValue();
				
				// => Cas 5
				Assertions.assertThat (employe.getPerformance()).isEqualTo(6);
			

			}

			
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
			
				Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0);   //(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel)
				Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
				
				//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

				//Then
				ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
				//On vérifie que la méthode save a bien été appelée sur employeRepository, et on capture le paramètre
				Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
				employe = employeArgumentCaptor.getValue();
				
				Assertions.assertThat (employe.getPerformance()).isEqualTo(2);
			

			}

		
		@Test
		void testExceptionPerformanceCommercialCaTraiteNegatif() {

			//Given
			String matricule = "C12345";
			Long caTraite = -2000L;
			Long objectifCa = 2000L;
			
			Assertions.assertThatThrownBy(() -> {
						//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
					})//Then
							.isInstanceOf(EmployeException.class).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");	
		}

	
			@Test
		void testExceptionPerformanceCommercialCaTraiteNull() {

			//Given
			String matricule = "C12345";
			Long caTraite = null;
			Long objectifCa = 2000L;
			
			Assertions.assertThatThrownBy(() -> {
						//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
					})//Then
							.isInstanceOf(EmployeException.class).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");	
		}
		
		@Test
		void testExceptionPerformanceCommercialObjectifCaNull() {

			//Given
			String matricule = "C12345";
			Long caTraite = 2000L;
			Long objectifCa = null;		
			Assertions.assertThatThrownBy(() -> {
						//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
					})//Then
							.isInstanceOf(EmployeException.class).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");	
		}					
		
	@Test
	void testExceptionPerformanceCommercialObjectifCaNegatif() {

		//Given
		String matricule = "C12345";
		Long caTraite = 2000L;
		Long objectifCa = -2000L;		
		Assertions.assertThatThrownBy(() -> {
					//When
			employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
				})//Then
						.isInstanceOf(EmployeException.class).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");	
	}	
		
		@Test
		void testExceptionPerformanceCommercialMatriculeNull() {

			//Given
		
			String matricule = null;
			Long caTraite = 2000L;
			Long objectifCa = 2000L;
			
			Assertions.assertThatThrownBy(() -> {
						//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
					})//Then
							.isInstanceOf(EmployeException.class).hasMessage("Le matricule ne peut être null et doit commencer par un C !");	
		}

	
	@Test
	void testExceptionPerformanceCommercialAutreMatricule() {

		//Given
	
		String matricule = "M12345";
		Long caTraite = 2000L;
		Long objectifCa = 2000L;
		
		Assertions.assertThatThrownBy(() -> {
					//When
			employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
				})//Then
						.isInstanceOf(EmployeException.class).hasMessage("Le matricule ne peut être null et doit commencer par un C !");	
	}
	
	
		@Test
		void testExceptionPerformanceCommercialNotExist() { 

			//Given
			String matricule = "C12345";
			Long caTraite = 2000L;
			Long objectifCa = 2000L;
	
			Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(null);
			
			Assertions.assertThatThrownBy(() -> {
						//When
				employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
					})//Then
							.isInstanceOf(EmployeException.class).hasMessage("Le matricule " + matricule + " n'existe pas !");	
		}

}
