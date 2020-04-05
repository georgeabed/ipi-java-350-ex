package com.ipiecoles.java.java350.service;

import java.time.LocalDate;

import javax.persistence.EntityExistsException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
		Mockito.when(employeRepository.findByMatricule("M45679")).thenReturn(null); //  C00001  parce que c'est la premier lettre de commercial c'est C + le dernier matricule et comme il n'y a pas matricule c'est 0000 + 1 parce que il a été incrémenté

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

/*		
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
*/
/*
	@Test
	void testEmbaucheEmployeLimiteMatricule() { //Test EmployeException
		//Given
		//Quand la méthode findLastMatricule va être appelée, on veut qu'elle renvoie une valeur comme s'il 
		//y avait 99999 employés, dont le matricule le plus élevé est X99999
		Mockito.when(employeRepository.findLastMatricule()).thenReturn("99998");


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
*/
		@Test
	void testEmbaucheEmployeExistant() {  //Test EntityExistsException
		//Given
		Mockito.when(employeRepository.findLastMatricule()).thenReturn("55555");
		//Quand on va chercher si l'employe avec le matricule calculé existe, on veut que la méthode
		//renvoie un employé qui aurait été créé dans l'intervalle
		Mockito.when(employeRepository.findByMatricule(Mockito.anyString())).thenReturn(new Employe("Doe", "Jane", "M55555", null, null, null, null));
		
		//Given
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
}
