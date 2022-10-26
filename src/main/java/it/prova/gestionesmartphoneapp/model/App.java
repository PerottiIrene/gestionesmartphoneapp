package it.prova.gestionesmartphoneapp.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "app")
public class App {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "nome")
	private String nome;
	@Column(name = "datainstallazione")
	private Date dataInstallazione;
	@Column(name = "dataultimoaggiornamento")
	private Date dataUltimoAggiornamento;
	@Column(name = "versione")
	private String versione;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "apps")
	private Set<Smartphone> smartphones = new HashSet<Smartphone>();
	
	public App() {}

	public App(String nome, Date dataInstallazione, Date dataUltimoAggiornamento, String versione) {
		super();
		this.nome = nome;
		this.dataInstallazione = dataInstallazione;
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
		this.versione = versione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataInstallazione() {
		return dataInstallazione;
	}

	public void setDataInstallazione(Date dataInstallazione) {
		this.dataInstallazione = dataInstallazione;
	}

	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}

	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}

	public String getVersione() {
		return versione;
	}

	public void setVersione(String versione) {
		this.versione = versione;
	}

	public Set<Smartphone> getSmartphones() {
		return smartphones;
	}

	public void setSmartphones(Set<Smartphone> smartphones) {
		this.smartphones = smartphones;
	}

	
	

}
