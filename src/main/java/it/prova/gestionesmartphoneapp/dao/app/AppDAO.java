package it.prova.gestionesmartphoneapp.dao.app;

import it.prova.gestionesmartphoneapp.dao.IBaseDAO;
import it.prova.gestionesmartphoneapp.model.App;
import it.prova.gestionesmartphoneapp.model.Smartphone;

public interface AppDAO extends IBaseDAO<App>{
	
	public App findByIdFetchingSmartphones(Long id) throws Exception;

}
