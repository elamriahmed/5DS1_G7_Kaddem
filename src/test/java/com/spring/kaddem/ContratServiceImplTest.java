package com.spring.kaddem;

import com.spring.kaddem.dto.ContratDTO;
import com.spring.kaddem.entities.Contrat;
import com.spring.kaddem.entities.Etudiant;
import com.spring.kaddem.entities.Specialite;
import com.spring.kaddem.repositories.ContratRepository;
import com.spring.kaddem.repositories.EtudiantRepository;
import com.spring.kaddem.services.IContratService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import org.springframework.boot.test.mock.mockito.MockBean;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Slf4j
class ContratServiceImplTest {

    @MockBean
	ContratRepository contratRepository;

	@Autowired
	IContratService contratService;
	@MockBean
	private EtudiantRepository etudiantRepository;
	@MockBean
	private Contrat contrat;
	private List<Contrat> contrats;
	@BeforeEach
	void setUp() {
		contrat = new Contrat();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		try {
			Date dateDebutContrat = dateFormat.parse("2022/04/22");
			Date dateFinContrat = dateFormat.parse("2022/06/24");

			contrat.setDateDebutContrat(dateDebutContrat);
			contrat.setDateFinContrat(dateFinContrat);
			contrat.setMontantContrat(1000);
			contrat.setArchived(true);
			contrat.setSpecialite(Specialite.IA);
		} catch (ParseException e) {
			// Handle the ParseException, e.g., log it or throw a custom exception
			e.printStackTrace();
		}
	}
	@BeforeEach
	void setContrats(){
		contrats = new ArrayList<>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		try {
			Contrat contrat1 = new Contrat();
			contrat1.setDateDebutContrat(dateFormat.parse("2022/04/29"));
			contrat1.setDateFinContrat(dateFormat.parse("2022/06/30"));
			contrat1.setMontantContrat(2000);
			contrat1.setArchived(true);
			contrat1.setSpecialite(Specialite.SECURITE);
			contrats.add(contrat1);

			Contrat contrat2 = new Contrat();
			contrat2.setDateDebutContrat(dateFormat.parse("2022/04/29"));
			contrat2.setDateFinContrat(dateFormat.parse("2022/06/30"));
			contrat2.setMontantContrat(3000);
			contrat2.setArchived(true);
			contrat2.setSpecialite(Specialite.CLOUD);
			contrats.add(contrat2);
		} catch (ParseException e) {
			// Handle the ParseException, e.g., log it or throw a custom exception
			e.printStackTrace();
		}
	}



	@Test
	void retrieveAllEtudiants() {
		setContrats();
		when(contratRepository.findAll()).thenReturn(contrats);
		List<Contrat> retrievedetudiants = contratService.retrieveAllContrats();
		assertEquals(contrats, retrievedetudiants);

	}


	@Test
	void addContrat() {
		ContratDTO newcontratDto = new ContratDTO();
		String startDateStr = "2022/04/29";
		String endDateStr = "2022/06/30";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Date startDate = dateFormat.parse(startDateStr);
			Date endDate = dateFormat.parse(endDateStr);

			newcontratDto.setDateDebutContrat(startDate);
			newcontratDto.setDateFinContrat(endDate);
			newcontratDto.setArchived(true);
			newcontratDto.setMontantContrat(3000);
			newcontratDto.setSpecialite(Specialite.CLOUD);

			when(contratRepository.save(any(Contrat.class))).thenAnswer(invocation -> {
				Contrat saveContrat = invocation.getArgument(0);
				saveContrat.setIdContrat(1); // Set the ID as it would be generated during save
				return saveContrat;
			});

		ContratDTO addContratDto = contratService.addUpdateContrat(newcontratDto);

		verify(contratRepository).save(any(Contrat.class));
			String formattedStartDate = dateFormat.format(addContratDto.getDateDebutContrat());
			String formattedEndDate = dateFormat.format(addContratDto.getDateFinContrat());

			// Perform assertions with the formatted dates
			assertEquals("2022/04/29", formattedStartDate);
			assertEquals("2022/06/30", formattedEndDate);
			assertEquals(3000, addContratDto.getMontantContrat());
			assertTrue(addContratDto.getArchived());
			assertEquals(Specialite.CLOUD, addContratDto.getSpecialite());
		} catch (ParseException e) {
			fail("Failed to parse date: " + e.getMessage());
		}
	}

	@Test
	void updateContrat() {
		// Create a ContratDTO object with updated values
		ContratDTO updatedContratDto = new ContratDTO();
		String startDateStr = "2022/05/15";
		String endDateStr = "2022/07/20";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		try {
			Date startDate = dateFormat.parse(startDateStr);
			Date endDate = dateFormat.parse(endDateStr);
			updatedContratDto.setIdContrat(1);
			updatedContratDto.setDateDebutContrat(startDate);
			updatedContratDto.setDateFinContrat(endDate);
			updatedContratDto.setArchived(false); // Set to false to simulate an update
			updatedContratDto.setMontantContrat(2500); // Updated montantContrat value
			updatedContratDto.setSpecialite(Specialite.RESEAU); // Updated specialite value

			// Mock the behavior of contratRepository.save() to return the updated Contrat object
			when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

			// Call the updateContrat method
			ContratDTO updatedContrat = contratService.addUpdateContrat(updatedContratDto);

			verify(contratRepository).save(any(Contrat.class));

			// Perform assertions with the updated ContratDTO object
			assertEquals(1, updatedContrat.getIdContrat());
			assertEquals(startDate, updatedContrat.getDateDebutContrat());
			assertEquals(endDate, updatedContrat.getDateFinContrat());
			assertEquals(2500, updatedContrat.getMontantContrat());
			assertFalse(updatedContrat.getArchived()); // Expecting archived to be false after the update
			assertEquals(Specialite.RESEAU, updatedContrat.getSpecialite());
		} catch (ParseException e) {
			fail("Failed to parse date: " + e.getMessage());
		}

	}
	@Test
	void retrieveContrat() {


		Contrat contrat1 = new Contrat();
		contrat1.setIdContrat(1);
		when(contratRepository.findById(1)).thenReturn(Optional.of(contrat1));
		Contrat retrievedContrat = contratService.retrieveContrat(1);
		verify(contratRepository).findById(1);
		assertEquals(contrat1, retrievedContrat);

	}
	@Test
	void removeEtudiant() {
		int ContratIdToRemove = 1;
		contratService.removeContrat(ContratIdToRemove);
		verify(contratRepository).deleteById(ContratIdToRemove);
	}
/*
	@Test
	void testAddAndAffectContratToEtudiant() {

		String nomE = "John";
		String prenomE = "Doe";

		Etudiant etudiant = new Etudiant();

		ContratDTO contratDto = new ContratDTO();
		String startDateStr = "2022/04/29";
		String endDateStr = "2022/06/30";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Date startDate = dateFormat.parse(startDateStr);
			Date endDate = dateFormat.parse(endDateStr);

			contratDto.setDateDebutContrat(startDate);
			contratDto.setDateFinContrat(endDate);
			contratDto.setArchived(true);
			contratDto.setMontantContrat(3000);
			contratDto.setSpecialite(Specialite.CLOUD);

			when(etudiantRepository.findByNomEAndPrenomE(nomE, prenomE)).thenReturn(etudiant);
			when(contratRepository.save(any(Contrat.class))).thenAnswer(invocation -> {
				Contrat saveContrat = invocation.getArgument(0);
				saveContrat.setIdContrat(1); // Set the ID as it would be generated during save
				return saveContrat;
			});

			ContratDTO result = contratService.addAndAffectContratToEtudiant(contratDto, nomE, prenomE);

			verify(contratRepository).save(any(Contrat.class));
			String formattedStartDate = dateFormat.format(result.getDateDebutContrat());
			String formattedEndDate = dateFormat.format(result.getDateFinContrat());

			// Perform assertions with the formatted dates
			assertEquals("2022/04/29", formattedStartDate);
			assertEquals("2022/06/30", formattedEndDate);
			assertEquals(3000, result.getMontantContrat());
			assertTrue(result.getArchived());
			assertEquals(Specialite.CLOUD, result.getSpecialite());
		} catch (ParseException e) {
			fail("Failed to parse date: " + e.getMessage());
		}
	}

 */









}
