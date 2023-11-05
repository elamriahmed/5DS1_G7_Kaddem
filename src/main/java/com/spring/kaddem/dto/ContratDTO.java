package com.spring.kaddem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.kaddem.entities.Contrat;
import com.spring.kaddem.entities.Etudiant;
import com.spring.kaddem.entities.Specialite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContratDTO {
    private Integer idContrat;
    private Date dateDebutContrat;
    private Date dateFinContrat;
    private Specialite specialite;
    private Boolean archived;
    private Integer montantContrat;
    @JsonIgnore
    private Etudiant etudiant;



    public static Contrat toEntity(ContratDTO contratDto) {
        if (contratDto == null) {
            return null;
        }
        Etudiant etudiant1 = contratDto.getEtudiant();
        return Contrat.builder()
                .idContrat(contratDto.getIdContrat())
                .dateDebutContrat(contratDto.getDateDebutContrat())
                .dateFinContrat(contratDto.getDateFinContrat())
                .montantContrat(contratDto.getMontantContrat())
                .archived(contratDto.getArchived())
                .specialite(contratDto.getSpecialite())
                .etudiant(etudiant1)
                .build();
    }
    public static ContratDTO toDto(Contrat contrat){
        if(contrat==null){
            return null;
        }
        Etudiant etudiantDto = contrat.getEtudiant();


        return  ContratDTO.builder()
                .idContrat(contrat.getIdContrat())
                .dateDebutContrat(contrat.getDateDebutContrat())
                .dateFinContrat(contrat.getDateFinContrat())
                .montantContrat(contrat.getMontantContrat())
                .archived(contrat.getArchived())
                .specialite(contrat.getSpecialite())
                .etudiant(etudiantDto)
                .build();
    }

}
