package it.prova.gestionesmartphoneapp.service;

import java.util.List;

import javax.persistence.EntityManager;

import it.prova.gestionesmartphoneapp.dao.EntityManagerUtil;
import it.prova.gestionesmartphoneapp.dao.app.AppDAO;
import it.prova.gestionesmartphoneapp.dao.smartphone.SmartphoneDAO;
import it.prova.gestionesmartphoneapp.model.App;
import it.prova.gestionesmartphoneapp.model.Smartphone;

public class SmartphoneServiceImpl implements SmartphoneService {

	private SmartphoneDAO smartphoneDAO;
	private AppDAO appDAO;

	@Override
	public void setSmartphoneDAO(SmartphoneDAO smartphoneDAO) {
		this.smartphoneDAO = smartphoneDAO;
	}

	@Override
	public void setAppDAO(AppDAO appDAO) {
		this.appDAO = appDAO;
	}

	@Override
	public List<Smartphone> listAll() throws Exception {
		// questo è come una connection
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			// uso l'injection per il dao
			smartphoneDAO.setEntityManager(entityManager);

			// eseguo quello che realmente devo fare
			return smartphoneDAO.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public Smartphone caricaSingoloElemento(Long id) throws Exception {
		// questo è come una connection
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			// uso l'injection per il dao
			smartphoneDAO.setEntityManager(entityManager);

			// eseguo quello che realmente devo fare
			return smartphoneDAO.get(id);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public Smartphone caricaSingoloElementoEagerApps(Long id) throws Exception {
		// questo è come una connection
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			// uso l'injection per il dao
			smartphoneDAO.setEntityManager(entityManager);

			// eseguo quello che realmente devo fare
			return smartphoneDAO.findByIdFetchingApps(id);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public void aggiorna(Smartphone smartphoneInstance) throws Exception {
		// questo è come una connection
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			// questo è come il MyConnection.getConnection()
			entityManager.getTransaction().begin();

			// uso l'injection per il dao
			smartphoneDAO.setEntityManager(entityManager);

			// eseguo quello che realmente devo fare
			smartphoneDAO.update(smartphoneInstance);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public void inserisciNuovo(Smartphone smartphoneInstance) throws Exception {
		// questo è come una connection
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			// questo è come il MyConnection.getConnection()
			entityManager.getTransaction().begin();

			// uso l'injection per il dao
			smartphoneDAO.setEntityManager(entityManager);

			// eseguo quello che realmente devo fare
			smartphoneDAO.insert(smartphoneInstance);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public void rimuovi(Long idSmartphone) throws Exception {
		// questo è come una connection
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			// questo è come il MyConnection.getConnection()
			entityManager.getTransaction().begin();

			// uso l'injection per il dao
			smartphoneDAO.setEntityManager(entityManager);

			// eseguo quello che realmente devo fare
			smartphoneDAO.delete(smartphoneDAO.get(idSmartphone));

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public void aggiungiApp(Smartphone smartphoneInstance, App appInstance) throws Exception {
		// questo è come una connection
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			// questo è come il MyConnection.getConnection()
			entityManager.getTransaction().begin();

			// uso l'injection per il dao
			smartphoneDAO.setEntityManager(entityManager);

			// 'attacco' alla sessione di hibernate i due oggetti
			// così jpa capisce che se risulta presente quel smartphone non deve essere
			// inserito
			smartphoneInstance = entityManager.merge(smartphoneInstance);
			// attenzione che appInstance deve essere già presente (lo verifica dall'id)
			// se così non è viene lanciata un'eccezione
			appInstance = entityManager.merge(appInstance);

			smartphoneInstance.getApps().add(appInstance);
			// l'update non viene richiamato a mano in quanto
			// risulta automatico, infatti il contesto di persistenza
			// rileva che smartphoneInstance ora è dirty vale a dire che una sua
			// proprieta ha subito una modifica (vale anche per i Set ovviamente)
			// inoltre se risultano già collegati lo capisce automaticamente grazie agli id

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}

	}

	@Override
	public void creaECollegaSmartphoneEApp(Smartphone smartphoneTransientInstance, App appTransientInstance)
			throws Exception {
		// questo è come una connection
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			// questo è come il MyConnection.getConnection()
			entityManager.getTransaction().begin();

			// uso l'injection per il dao
			smartphoneDAO.setEntityManager(entityManager);

			// collego le due entità: questa cosa funziona grazie al fatto che ho
			// CascadeType.PERSIST, CascadeType.MERGE dentro l'owner della relazione
			// (Smartphone in
			// questo caso)
			smartphoneTransientInstance.getApps().add(appTransientInstance);

			// ********************** IMPORTANTE ****************************
			// se io rimuovo i cascade, non funziona più e si deve prima salvare il app
			// (tramite appDAO iniettando anch'esso) e poi
			// sfruttare i metodi addTo o removeFrom dentro Smartphone:
			// AppDAO appDAO = MyDaoFactory.getAppDAOInstance();
			// appDAO.setEntityManager(entityManager);
			// appDAO.insert(appTransientInstance);
			// smartphoneTransientInstance.addToApps(appTransientInstance);
			// in questo caso però se il app è già presente non ne tiene conto e
			// inserirebbe duplicati, ma è logico
			// ****************************************************************

			// inserisco il smartphone
			smartphoneDAO.insert(smartphoneTransientInstance);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}

	}
}
