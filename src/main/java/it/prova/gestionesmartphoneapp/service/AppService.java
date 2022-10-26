package it.prova.gestionesmartphoneapp.service;

import java.util.List;

import it.prova.gestionesmartphoneapp.dao.app.AppDAO;
import it.prova.gestionesmartphoneapp.dao.smartphone.SmartphoneDAO;
import it.prova.gestionesmartphoneapp.model.App;
import it.prova.gestionesmartphoneapp.model.Smartphone;

public interface AppService {
	
	public void setAppDAO(AppDAO appDAO);
	public void setSmartphoneDAO(SmartphoneDAO smartphoneDAO);
	
	public List<App> listAll() throws Exception;

	public App caricaSingoloElemento(Long id) throws Exception;
	
	public App caricaSingoloElementoEagerSmartphones(Long id) throws Exception;

	public void aggiorna(App appInstance) throws Exception;

	public void inserisciNuovo(App appInstance) throws Exception;

	public void rimuovi(Long idApp) throws Exception;

	public void aggiungiSmartphone(App appInstance, Smartphone smartphoneInstance) throws Exception;
	
	public void creaECollegaAppESmartphone(App appTransientInstance, Smartphone smartphoneTransientInstance) throws Exception;

}
