package com.spring.kaddem.services;

import com.spring.kaddem.dto.ContratDTO;
import com.spring.kaddem.entities.Contrat;
import com.spring.kaddem.entities.Etudiant;
import com.spring.kaddem.entities.Specialite;
import com.spring.kaddem.repositories.ContratRepository;
import com.spring.kaddem.repositories.EtudiantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
public class ContratServiceImpl implements  IContratService{


    ContratRepository contratRepository;
    EtudiantRepository etudiantRepository;

    @Override
    public List<Contrat> retrieveAllContrats() {
        return contratRepository.findAll();
    }


    @Override
    public Contrat retrieveContrat(Integer idContrat) {
        Optional<Contrat> contratOptional = contratRepository.findById(idContrat);

        if (contratOptional.isPresent()) {
            return contratOptional.get();
        } else {
            // Handle the case where the Contrat with the given ID was not found
            throw new EntityNotFoundException(" Contrat not found");
        }
    }

    @Override
    public void removeContrat(Integer idContrat) {
        log.info("debut methode removeContrat");
        contratRepository.deleteById(idContrat);
    }

    @Override
    public ContratDTO addUpdateContrat(ContratDTO c) {

        contratRepository.save(ContratDTO.toEntity(c));

        return c;
    }

    @Transactional
    public ContratDTO addAndAffectContratToEtudiant(ContratDTO contratDTO, String nomE, String prenomE) {
        long startTime = System.currentTimeMillis();
        log.info("Start Time: " + startTime);
        log.info("Start of addAndAffectContratToEtudiant method");

        Etudiant etudiant = etudiantRepository.findByNomEAndPrenomE(nomE, prenomE);

        if (etudiant == null) {
            log.error("Student not found with name: " + nomE + " " + prenomE);
            throw new EntityNotFoundException("Student not found with name: " + nomE + " " + prenomE);
        }

        // Number of active contracts
        int nbContratsActifs = etudiant.getContrats().size();

        if (nbContratsActifs >= 5) {
            log.info("Number of allowed contracts is reached");
            log.info("End of addAndAffectContratToEtudiant method");
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            log.info("End Time: " + endTime);
            log.info("Execution Time: " + executionTime + " ms");
            return contratDTO;
        }

        // Convert ContratDTO to Contrat entity if needed
        Contrat contrat = convertToEntity(contratDTO);

        // Save the contrat
        contrat.setEtudiant(etudiant);
        contrat = contratRepository.save(contrat);

        // Convert Contrat entity back to ContratDTO
        ContratDTO savedContratDTO = convertToDTO(contrat);

        log.info("End of addAndAffectContratToEtudiant method");
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("End Time: " + endTime);
        log.info("Execution Time: " + executionTime + " ms");

        return savedContratDTO;
    }

    private Contrat convertToEntity(ContratDTO contratDTO) {
        Contrat contrat = new Contrat();
        contrat.setDateDebutContrat(contratDTO.getDateDebutContrat());
        contrat.setDateFinContrat(contratDTO.getDateFinContrat());
        contrat.setMontantContrat(contratDTO.getMontantContrat());
        // Set other fields if there are more properties in ContratDTO

        return contrat;
    }

    private ContratDTO convertToDTO(Contrat contrat) {
        ContratDTO contratDTO = new ContratDTO();
        contratDTO.setDateDebutContrat(contrat.getDateDebutContrat());
        contratDTO.setDateFinContrat(contrat.getDateFinContrat());
        contratDTO.setMontantContrat(contrat.getMontantContrat());
        // Set other fields if there are more properties in ContratDTO

        return contratDTO;
    }

    public 	Integer nbContratsValides(Date startDate, Date endDate){
        return contratRepository.getnbContratsValides(startDate, endDate);
    }
/*
    public void retrieveAndUpdateStatusContrat(){
        log.info("debut methode retrieveAndUpdateStatusContrat");
        List<Contrat>contrats=contratRepository.findAll();
        log.info("total contrats :"+contrats.size());

        for (Contrat contrat : contrats) {
            log.info("id: "+contrat.getIdContrat());
            log.info("date fin"+contrat.getDateFinContrat());
            log.info("archived "+contrat.getArchived());

            Date dateSysteme = new Date();

            if (contrat.getArchived()==null || !contrat.getArchived()) {
                long timeDifferenceInMilliseconds = contrat.getDateFinContrat().getTime()-dateSysteme.getTime();
                long differenceInDays = (timeDifferenceInMilliseconds / (1000 * 60 * 60 * 24)) % 365;
                // il est préférable d'utiliser des méthodes prédéfinis de comparaison
                log.info("difference in days : "+differenceInDays);

                if (differenceInDays==15){  // pour 15 jours exactement
                    log.info(" Contrat Commencant le : " + contrat.getDateDebutContrat()+"pour l'etudiant "+contrat.getEtudiant().getNomE()+
                            " "+contrat.getEtudiant().getPrenomE()+"  va bientot s achever le "
                            +contrat.getDateFinContrat());
                }
                if (differenceInDays==0) {
                    log.info("jour j: " + contrat.getIdContrat());
                    contrat.setArchived(true);
                    contratRepository.save(contrat);
                }
            }

            log.info("debut methode retrieveAndUpdateStatusContrat");
        }
    }

 */
    public float getChiffreAffaireEntreDeuxDates(Date startDate, Date endDate){
        float timeDifferenceInMilliseconds =(float) endDate.getTime() - startDate.getTime();
        float differenceInDays = (timeDifferenceInMilliseconds / (1000 * 60 * 60 * 24)) % 365;
        float differenceInMonths =differenceInDays/30;
        List<Contrat> contrats=contratRepository.findAll();
        float chiffreAffaireEntreDeuxDates=0;
        float chiffreAffaireEntreDeuxDatesIA=0;
        float chiffreAffaireEntreDeuxDatesCloud=0;
        float chiffreAffaireEntreDeuxDatesReseau=0;
        float chiffreAffaireEntreDeuxDatesSecurite=0;

        for (Contrat contrat : contrats) {
            if (contrat.getSpecialite()== Specialite.IA){
                chiffreAffaireEntreDeuxDates+=(differenceInMonths*contrat.getMontantContrat());
                chiffreAffaireEntreDeuxDatesIA+=(differenceInMonths*contrat.getMontantContrat());

            } else if (contrat.getSpecialite()== Specialite.CLOUD) {
                chiffreAffaireEntreDeuxDates+=(differenceInMonths*contrat.getMontantContrat());
                chiffreAffaireEntreDeuxDatesCloud+=(differenceInMonths*contrat.getMontantContrat());
            }
            else if (contrat.getSpecialite()== Specialite.RESEAU) {
                chiffreAffaireEntreDeuxDates+=(differenceInMonths*contrat.getMontantContrat());
                chiffreAffaireEntreDeuxDatesReseau+=(differenceInMonths*contrat.getMontantContrat());

            }
            else if (contrat.getSpecialite()== Specialite.SECURITE)
            {
                chiffreAffaireEntreDeuxDates+=(differenceInMonths*contrat.getMontantContrat());
                chiffreAffaireEntreDeuxDatesSecurite+=(differenceInMonths*contrat.getMontantContrat());

            }
        }
        log.info("chiffreAffaireEntreDeuxDates: "+chiffreAffaireEntreDeuxDates);
        log.info("chiffreAffaireEntreDeuxDatesIA:" +chiffreAffaireEntreDeuxDatesIA);
        log.info("chiffreAffaireEntreDeuxDatesCloud "+chiffreAffaireEntreDeuxDatesCloud);
        log.info("chiffreAffaireEntreDeuxDatesReseau "+chiffreAffaireEntreDeuxDatesReseau);
        log.info("chiffreAffaireEntreDeuxDatesSecurite "+chiffreAffaireEntreDeuxDatesSecurite);
        return chiffreAffaireEntreDeuxDates;


    }



}
