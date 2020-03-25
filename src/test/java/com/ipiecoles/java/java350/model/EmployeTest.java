package com.ipiecoles.java.java350.model;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmployeTest {
	
	@Test
	public void testNbAnneeAncienneteNow() {
		
		//Given
		Employe employe = new Employe();
		employe.setDateEmbauche(LocalDate.now());
		
		//When
		Integer nbAnnees = employe.getNombreAnneeAnciennete();
		
		//Then
		Assertions.assertThat (nbAnnees).isEqualTo(0);
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
			Assertions.assertThat (nbAnnees).isEqualTo(0);
		
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

}
