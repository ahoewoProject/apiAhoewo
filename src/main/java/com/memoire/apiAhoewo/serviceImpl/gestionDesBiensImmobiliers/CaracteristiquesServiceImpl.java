package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Caracteristiques;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.CaracteristiquesRepository;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.CaracteristiquesService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

@Service
public class CaracteristiquesServiceImpl implements CaracteristiquesService {
    @Autowired
    private BienImmobilierService bienImmobilierService;
    @Autowired
    private CaracteristiquesRepository caracteristiquesRepository;
    @Autowired
    private PersonneService personneService;

    @Override
    public Caracteristiques findByBienImmobilier(Long idBienImmobilier) {
        BienImmobilier bienImmobilier = bienImmobilierService.findById(idBienImmobilier);
        return caracteristiquesRepository.findByBienImmobilier(bienImmobilier);
    }

    @Override
    public Caracteristiques save(BienImmobilier bienImmobilier, Caracteristiques caracteristiques, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        caracteristiques.setBienImmobilier(bienImmobilier);
        existsCaracteristiquesToSave(caracteristiques);
        caracteristiques.setCreerLe(new Date());
        caracteristiques.setCreerPar(personne.getId());
        caracteristiques.setStatut(true);
        return caracteristiquesRepository.save(caracteristiques);
    }

    @Override
    public Caracteristiques update(Long id, Caracteristiques caracteristiques, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        Caracteristiques caracteristiques1 = findByBienImmobilier(id);
        existsCaracteristiquesToUpdate(caracteristiques, caracteristiques1);
        caracteristiques1.setModifierLe(new Date());
        caracteristiques1.setModifierPar(personne.getId());
        return caracteristiquesRepository.save(caracteristiques1);
    }

    private void existsCaracteristiquesToUpdate(Caracteristiques caracteristiques, Caracteristiques caracteristiques1) {
        if (caracteristiques.getNombreChambres() != null) {
            caracteristiques1.setNombreChambres(caracteristiques.getNombreChambres());
        } else if (caracteristiques.getNombreChambresSalon() != null) {
            caracteristiques1.setNombreChambresSalon(caracteristiques.getNombreChambresSalon());
        } else if (caracteristiques.getNombreBureaux() != null) {
            caracteristiques1.setNombreBureaux(caracteristiques.getNombreBureaux());
        } else if (caracteristiques.getNombreBoutiques() != null) {
            caracteristiques1.setNombreBoutiques(caracteristiques.getNombreBoutiques());
        } else if (caracteristiques.getNombreMagasins() != null) {
            caracteristiques1.setNombreMagasins(caracteristiques.getNombreMagasins());
        } else if (caracteristiques.getNombreEtages() != null) {
            caracteristiques1.setNombreEtages(caracteristiques.getNombreEtages());
        } else if (caracteristiques.getNombreSalons() != null) {
            caracteristiques1.setNombreSalons(caracteristiques.getNombreSalons());
        } else if (caracteristiques.getNombreGarages() != null) {
            caracteristiques1.setNombreGarages(caracteristiques.getNombreGarages());
        } else if (caracteristiques.getNombreCuisineInterne() != null) {
            caracteristiques1.setNombreCuisineInterne(caracteristiques.getNombreCuisineInterne());
        } else if (caracteristiques.getNombreCuisineExterne() != null) {
            caracteristiques1.setNombreCuisineExterne(caracteristiques.getNombreCuisineExterne());
        } else if (caracteristiques.getNombreAppartements() != null) {
            caracteristiques1.setNombreAppartements(caracteristiques.getNombreAppartements());
        } else if (caracteristiques.getNombreWCDoucheInterne() != null) {
            caracteristiques1.setNombreWCDoucheInterne(caracteristiques.getNombreWCDoucheInterne());
        } else if (caracteristiques.getNombreWCDoucheExterne() != null) {
            caracteristiques1.setNombreWCDoucheExterne(caracteristiques.getNombreWCDoucheExterne());
        } else if (caracteristiques.getEauTde() != null) {
            caracteristiques1.setEauTde(caracteristiques.getEauTde());
        } else if (caracteristiques.getEauForage() != null) {
            caracteristiques1.setEauForage(caracteristiques.getEauForage());
        } else if (caracteristiques.getElectriciteCeet() != null) {
            caracteristiques1.setElectriciteCeet(caracteristiques.getElectriciteCeet());
        } else if (caracteristiques.getWifi() != null) {
            caracteristiques1.setWifi(caracteristiques.getWifi());
        } else if (caracteristiques.getCuisineInterne() != null) {
            caracteristiques1.setCuisineInterne(caracteristiques.getCuisineInterne());
        } else if (caracteristiques.getCuisineExterne() != null) {
            caracteristiques1.setCuisineExterne(caracteristiques.getCuisineExterne());
        } else if (caracteristiques.getWcDoucheInterne() != null) {
            caracteristiques1.setWcDoucheInterne(caracteristiques.getWcDoucheInterne());
        } else if (caracteristiques.getWcDoucheExterne() != null) {
            caracteristiques1.setWcDoucheExterne(caracteristiques.getWcDoucheExterne());
        } else if (caracteristiques.getBalcon() != null) {
            caracteristiques1.setBalcon(caracteristiques.getBalcon());
        } else if (caracteristiques.getClimatisation() != null) {
            caracteristiques1.setClimatisation(caracteristiques.getClimatisation());
        } else if (caracteristiques.getPiscine() != null) {
            caracteristiques1.setPiscine(caracteristiques.getPiscine());
        } else if (caracteristiques.getParking() != null) {
            caracteristiques1.setParking(caracteristiques.getParking());
        } else if (caracteristiques.getJardin() != null) {
            caracteristiques1.setJardin(caracteristiques.getJardin());
        } else if (caracteristiques.getTerrasse() != null) {
            caracteristiques1.setTerrasse(caracteristiques.getTerrasse());
        } else if (caracteristiques.getAscenseur() != null) {
            caracteristiques1.setAscenseur(caracteristiques.getAscenseur());
        } else if (caracteristiques.getGarage() != null) {
            caracteristiques1.setGarage(caracteristiques.getGarage());
        } else if (caracteristiques.getBaieVitree() != null) {
            caracteristiques1.setBaieVitree(caracteristiques.getBaieVitree());
        } else if (caracteristiques.getSolCarelle() != null) {
            caracteristiques1.setSolCarelle(caracteristiques.getSolCarelle());
        } else if (caracteristiques.getCashPowerPersonnel() != null) {
            caracteristiques1.setCashPowerPersonnel(caracteristiques.getCashPowerPersonnel());
        } else if (caracteristiques.getCompteurAdditionnel() != null) {
            caracteristiques1.setCompteurAdditionnel(caracteristiques.getCompteurAdditionnel());
        } else if (caracteristiques.getCompteurEau() != null) {
            caracteristiques1.setCompteurEau(caracteristiques.getCompteurEau());
        } else if (caracteristiques.getPlafonne() != null) {
            caracteristiques1.setPlafonne(caracteristiques.getPlafonne());
        } else if (caracteristiques.getPlacard() != null) {
            caracteristiques1.setPlacard(caracteristiques.getPlacard());
        } else if (caracteristiques.getDalle() != null) {
            caracteristiques1.setDalle(caracteristiques.getDalle());
        } else if (caracteristiques.getaLetage() != null) {
            caracteristiques1.setaLetage(caracteristiques.getaLetage());
        } else if (caracteristiques.getNombrePlacards() != null) {
            caracteristiques1.setNombrePlacards(caracteristiques.getNombrePlacards());
        } else if (caracteristiques.getToiletteVisiteur() != null) {
            caracteristiques1.setToiletteVisiteur(caracteristiques.getToiletteVisiteur());
        }
    }

    private void existsCaracteristiquesToSave(Caracteristiques caracteristiques) {
        if (caracteristiques.getNombreChambres() != null) {
            caracteristiques.setNombreChambres(caracteristiques.getNombreChambres());
        } else if (caracteristiques.getNombreChambresSalon() != null) {
            caracteristiques.setNombreChambresSalon(caracteristiques.getNombreChambresSalon());
        } else if (caracteristiques.getNombreBureaux() != null) {
            caracteristiques.setNombreBureaux(caracteristiques.getNombreBureaux());
        } else if (caracteristiques.getNombreBoutiques() != null) {
            caracteristiques.setNombreBoutiques(caracteristiques.getNombreBoutiques());
        } else if (caracteristiques.getNombreMagasins() != null) {
            caracteristiques.setNombreMagasins(caracteristiques.getNombreMagasins());
        } else if (caracteristiques.getNombreEtages() != null) {
            caracteristiques.setNombreEtages(caracteristiques.getNombreEtages());
        } else if (caracteristiques.getNombreSalons() != null) {
            caracteristiques.setNombreSalons(caracteristiques.getNombreSalons());
        } else if (caracteristiques.getNombreGarages() != null) {
            caracteristiques.setNombreGarages(caracteristiques.getNombreGarages());
        } else if (caracteristiques.getNombreCuisineInterne() != null) {
            caracteristiques.setNombreCuisineInterne(caracteristiques.getNombreCuisineInterne());
        } else if (caracteristiques.getNombreCuisineExterne() != null) {
            caracteristiques.setNombreCuisineExterne(caracteristiques.getNombreCuisineExterne());
        } else if (caracteristiques.getNombreAppartements() != null) {
            caracteristiques.setNombreAppartements(caracteristiques.getNombreAppartements());
        } else if (caracteristiques.getNombreWCDoucheInterne() != null) {
            caracteristiques.setNombreWCDoucheInterne(caracteristiques.getNombreWCDoucheInterne());
        } else if (caracteristiques.getNombreWCDoucheExterne() != null) {
            caracteristiques.setNombreWCDoucheExterne(caracteristiques.getNombreWCDoucheExterne());
        } else if (caracteristiques.getEauTde() != null) {
            caracteristiques.setEauTde(caracteristiques.getEauTde());
        } else if (caracteristiques.getEauForage() != null) {
            caracteristiques.setEauForage(caracteristiques.getEauForage());
        } else if (caracteristiques.getElectriciteCeet() != null) {
            caracteristiques.setElectriciteCeet(caracteristiques.getElectriciteCeet());
        } else if (caracteristiques.getWifi() != null) {
            caracteristiques.setWifi(caracteristiques.getWifi());
        } else if (caracteristiques.getCuisineInterne() != null) {
            caracteristiques.setCuisineInterne(caracteristiques.getCuisineInterne());
        } else if (caracteristiques.getCuisineExterne() != null) {
            caracteristiques.setCuisineExterne(caracteristiques.getCuisineExterne());
        } else if (caracteristiques.getWcDoucheInterne() != null) {
            caracteristiques.setWcDoucheInterne(caracteristiques.getWcDoucheInterne());
        } else if (caracteristiques.getWcDoucheExterne() != null) {
            caracteristiques.setWcDoucheExterne(caracteristiques.getWcDoucheExterne());
        } else if (caracteristiques.getBalcon() != null) {
            caracteristiques.setBalcon(caracteristiques.getBalcon());
        } else if (caracteristiques.getClimatisation() != null) {
            caracteristiques.setClimatisation(caracteristiques.getClimatisation());
        } else if (caracteristiques.getPiscine() != null) {
            caracteristiques.setPiscine(caracteristiques.getPiscine());
        } else if (caracteristiques.getParking() != null) {
            caracteristiques.setParking(caracteristiques.getParking());
        } else if (caracteristiques.getJardin() != null) {
            caracteristiques.setJardin(caracteristiques.getJardin());
        } else if (caracteristiques.getTerrasse() != null) {
            caracteristiques.setTerrasse(caracteristiques.getTerrasse());
        } else if (caracteristiques.getAscenseur() != null) {
            caracteristiques.setAscenseur(caracteristiques.getAscenseur());
        } else if (caracteristiques.getGarage() != null) {
            caracteristiques.setGarage(caracteristiques.getGarage());
        } else if (caracteristiques.getBaieVitree() != null) {
            caracteristiques.setBaieVitree(caracteristiques.getBaieVitree());
        } else if (caracteristiques.getSolCarelle() != null) {
            caracteristiques.setSolCarelle(caracteristiques.getSolCarelle());
        } else if (caracteristiques.getCashPowerPersonnel() != null) {
            caracteristiques.setCashPowerPersonnel(caracteristiques.getCashPowerPersonnel());
        } else if (caracteristiques.getCompteurAdditionnel() != null) {
            caracteristiques.setCompteurAdditionnel(caracteristiques.getCompteurAdditionnel());
        } else if (caracteristiques.getCompteurEau() != null) {
            caracteristiques.setCompteurEau(caracteristiques.getCompteurEau());
        } else if (caracteristiques.getPlafonne() != null) {
            caracteristiques.setPlafonne(caracteristiques.getPlafonne());
        } else if (caracteristiques.getPlacard() != null) {
            caracteristiques.setPlacard(caracteristiques.getPlacard());
        } else if (caracteristiques.getDalle() != null) {
            caracteristiques.setDalle(caracteristiques.getDalle());
        } else if (caracteristiques.getaLetage() != null) {
            caracteristiques.setaLetage(caracteristiques.getaLetage());
        } else if (caracteristiques.getNombrePlacards() != null) {
            caracteristiques.setNombrePlacards(caracteristiques.getNombrePlacards());
        } else if (caracteristiques.getToiletteVisiteur() != null) {
            caracteristiques.setToiletteVisiteur(caracteristiques.getToiletteVisiteur());
        }
    }
}
