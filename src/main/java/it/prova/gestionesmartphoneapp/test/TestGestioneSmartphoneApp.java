package it.prova.gestionesmartphoneapp.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import it.prova.gestionesmartphoneapp.dao.EntityManagerUtil;
import it.prova.gestionesmartphoneapp.model.App;
import it.prova.gestionesmartphoneapp.model.Smartphone;
import it.prova.gestionesmartphoneapp.service.AppService;
import it.prova.gestionesmartphoneapp.service.MyServiceFactory;
import it.prova.gestionesmartphoneapp.service.SmartphoneService;

public class TestGestioneSmartphoneApp {

	public static void main(String[] args) throws Exception {

		AppService appServiceInstance = MyServiceFactory.getAppServiceInstance();
		SmartphoneService smartphoneServiceInstance = MyServiceFactory.getSmartphoneServiceInstance();

		try {

			testInserimentoNuovoSmartphone(smartphoneServiceInstance);
			System.out.println(
					"in tabella smartphone ci sono " + smartphoneServiceInstance.listAll().size() + " elementi.");

			testModificaVersioneSmartphone(smartphoneServiceInstance);
			System.out.println(
					"in tabella smartphone ci sono " + smartphoneServiceInstance.listAll().size() + " elementi.");

			testInserimentoNuovaApp(appServiceInstance);
			System.out.println("in tabella app ci sono " + appServiceInstance.listAll().size() + " elementi.");

			testModificaVersioneApp(appServiceInstance);
			System.out.println("in tabella app ci sono " + appServiceInstance.listAll().size() + " elementi.");
			
			testCollegaSmartphoneApp(smartphoneServiceInstance,appServiceInstance);
			System.out.println("in tabella app ci sono " + appServiceInstance.listAll().size() + " elementi.");
			System.out.println(
					"in tabella smartphone ci sono " + smartphoneServiceInstance.listAll().size() + " elementi.");
			
			testRimuoviAppDaSmartphone(smartphoneServiceInstance,appServiceInstance);
			System.out.println("in tabella app ci sono " + appServiceInstance.listAll().size() + " elementi.");
			System.out.println(
					"in tabella smartphone ci sono " + smartphoneServiceInstance.listAll().size() + " elementi.");
			
			testRimozioneSmartphoneECheckApps(smartphoneServiceInstance,appServiceInstance);
			System.out.println("in tabella app ci sono " + appServiceInstance.listAll().size() + " elementi.");
			System.out.println(
					"in tabella smartphone ci sono " + smartphoneServiceInstance.listAll().size() + " elementi.");

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// questa è necessaria per chiudere tutte le connessioni quindi rilasciare il
			// main
			EntityManagerUtil.shutdown();
		}

	}

	private static void testInserimentoNuovoSmartphone(SmartphoneService smartphoneServiceInstance) throws Exception {
		System.out.println(".......testInserimentoNuovoSmartphone inizio.............");

		Smartphone smartphoneInstance = new Smartphone("apple", "s7", 500, "versione1");
		smartphoneServiceInstance.inserisciNuovo(smartphoneInstance);
		if (smartphoneInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovoSmartphone fallito ");

		System.out.println(".......testInserimentoNuovoSmartphone fine: PASSED.............");
	}

	private static void testModificaVersioneSmartphone(SmartphoneService smartphoneServiceInstance) throws Exception {

		Smartphone smartphone = smartphoneServiceInstance.caricaSingoloElemento(1L);
		smartphone.setVersioneOs("versione2");
		smartphoneServiceInstance.aggiorna(smartphone);
		if (!smartphoneServiceInstance.caricaSingoloElemento(1L).getVersioneOs().equals(smartphone.getVersioneOs()))
			throw new RuntimeException("testModifica fallito ");

		System.out.println(".......testModificaNuovoSmartphone fine: PASSED.............");
	}

	private static void testInserimentoNuovaApp(AppService appServiceInstance) throws Exception {
		System.out.println(".......testInserimentoNuovaApp inizio.............");

		Date dataInstallazione = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2010");
		Date dataUltimoAggiornamento = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2020");
		App appInstance = new App("moovit", dataInstallazione, dataUltimoAggiornamento, "versione1");
		appServiceInstance.inserisciNuovo(appInstance);
		if (appInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovaApp fallito ");

		System.out.println(".......testInserimentoNuovaApp fine: PASSED.............");
	}

	private static void testModificaVersioneApp(AppService appServiceInstance) throws Exception {

		Date dataUltimoAggiornamento = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022");
		App app = appServiceInstance.caricaSingoloElemento(1L);
		app.setVersione("versione2");
		app.setDataUltimoAggiornamento(dataUltimoAggiornamento);
		appServiceInstance.aggiorna(app);
		if (!appServiceInstance.caricaSingoloElemento(1L).getVersione().equals(app.getVersione()))
			throw new RuntimeException("testModifica fallito ");

		System.out.println(".......testModificaNuovoSmartphone fine: PASSED.............");
	}
	
	private static void testCollegaSmartphoneApp(SmartphoneService smartphoneServiceInstance, AppService appServiceInstance)
			throws Exception {
		System.out.println(".......testCollegaAppASmartphone inizio.............");

		// collego
		App nuovoApp= appServiceInstance.caricaSingoloElemento(1L);
		Smartphone smartphoneInstance= smartphoneServiceInstance.caricaSingoloElemento(1L);
		
		smartphoneServiceInstance.aggiungiApp(smartphoneInstance, nuovoApp);

		// ricarico eager per forzare il test
		Smartphone smartphoneReloaded = smartphoneServiceInstance.caricaSingoloElementoEagerApps(smartphoneInstance.getId());
		if (smartphoneReloaded.getApps().isEmpty())
			throw new RuntimeException("testCollegaAppASmartphone fallito: app non collegato ");
	}
	
	private static void testRimuoviAppDaSmartphone(SmartphoneService smartphoneServiceInstance, AppService appServiceInstance)
			throws Exception {
		System.out.println(".......testRimozioneAppASmartphone inizio.............");

		// collego
		App nuovoApp= appServiceInstance.caricaSingoloElemento(1L);
		Smartphone smartphoneInstance= smartphoneServiceInstance.caricaSingoloElementoEagerApps(1L);
		Set<App> appsPresentiSmartphone= smartphoneInstance.getApps();
		Set<App> listaAggiornataDiApp= new HashSet<>();
		
		for(App element: appsPresentiSmartphone) {
			if(element.getId()!=nuovoApp.getId())
				listaAggiornataDiApp.add(element);
		}
		
		smartphoneInstance.setApps(listaAggiornataDiApp);
		
		smartphoneServiceInstance.aggiorna(smartphoneInstance);

		// ricarico eager per forzare il test
		Smartphone smartphoneReloaded = smartphoneServiceInstance.caricaSingoloElementoEagerApps(smartphoneInstance.getId());
		if (!smartphoneReloaded.getApps().isEmpty())
			throw new RuntimeException("testRimozioneAppASmartphone fallito: app non collegato ");
		
		System.out.println(".......testRimozioneAppASmartphone passed.............");
	}
	
	private static void testRimozioneSmartphoneECheckApps(SmartphoneService smartphoneServiceInstance, AppService appServiceInstance)
			throws Exception {
		System.out.println(".......testRimozioneSmartphoneECheckApps inizio.............");

		// creo un smartphone e due apps
		Smartphone smartphoneInstanceX = new Smartphone("samsung", "s7",4000,"version3");
		smartphoneServiceInstance.inserisciNuovo(smartphoneInstanceX);
		Date dataInstallazione = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2010");
		Date dataUltimoAggiornamento = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2020");
		App app1 = new App("titok",dataInstallazione,dataUltimoAggiornamento,"versone2");
		appServiceInstance.inserisciNuovo(app1);
		App app2 = new App("app",dataInstallazione,dataUltimoAggiornamento,"versone2");
		appServiceInstance.inserisciNuovo(app2);
		smartphoneServiceInstance.aggiungiApp(smartphoneInstanceX, app1);
		smartphoneServiceInstance.aggiungiApp(smartphoneInstanceX, app2);

		// ricarico eager per forzare il test
		Smartphone smartphoneReloaded = smartphoneServiceInstance.caricaSingoloElementoEagerApps(smartphoneInstanceX.getId());
		if (smartphoneReloaded.getApps().size() != 2)
			throw new RuntimeException("testRimozioneSmartphoneECheckApps fallito: 2 apps e smartphone non collegati ");

		// rimuovo
		smartphoneServiceInstance.rimuovi(smartphoneReloaded.getId());

		// ricarico
		Smartphone smartphoneSupposedToBeRemoved = smartphoneServiceInstance.caricaSingoloElementoEagerApps(smartphoneInstanceX.getId());
		if (smartphoneSupposedToBeRemoved != null)
			throw new RuntimeException("testRimozioneSmartphoneECheckApps fallito: rimozione non avvenuta ");

		System.out.println(".......testRimozioneSmartphoneECheckApps fine: PASSED.............");
	}
}
