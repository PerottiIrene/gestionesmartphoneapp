package it.prova.gestionesmartphoneapp.service;

import it.prova.gestionesmartphoneapp.dao.MyDaoFactory;

public class MyServiceFactory {

	private static AppService appServiceInstance = null;
	private static SmartphoneService smartphoneServiceInstance = null;

	public static AppService getAppServiceInstance() {
		if (appServiceInstance == null)
			appServiceInstance = new AppServiceImpl();

		appServiceInstance.setAppDAO(MyDaoFactory.getAppDAOInstance());

		return appServiceInstance;
	}

	public static SmartphoneService getSmartphoneServiceInstance() {
		if (smartphoneServiceInstance == null)
			smartphoneServiceInstance = new SmartphoneServiceImpl();

		smartphoneServiceInstance.setSmartphoneDAO(MyDaoFactory.getSmartphoneDAOInstance());

		return smartphoneServiceInstance;
	}
}
