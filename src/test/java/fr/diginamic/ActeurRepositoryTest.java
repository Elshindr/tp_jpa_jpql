package fr.diginamic;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Test;

public class ActeurRepositoryTest {
	
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("movie_db");
	private EntityManager em = emf.createEntityManager();
	
	/**
	 * Extraire tous les acteurs triés dans l'ordre alphabétique des identités
	 */
	@Test
	public void testExtraireActeursTriesParIdentite() {
		
		TypedQuery<Acteur> query = em.createQuery("SELECT a FROM Acteur a ORDER BY identite", Acteur.class);
		List<Acteur> acteurs = query.getResultList();
		
		assertEquals(1137, acteurs.size());
		assertEquals("A.J. Danna", acteurs.get(0).getIdentite());
	}
	
	/**
	 * Extraire l'actrice appelée Marion Cotillard
	 */
	@Test
	public void testExtraireActeursParIdentite() {
		TypedQuery<Acteur> query = em.createQuery("SELECT a FROM Acteur a WHERE a.identite = :identite", Acteur.class);
		query.setParameter("identite", "Marion Cotillard");
		List<Acteur> acteurs = query.getResultList();
		
		assertEquals(1, acteurs.size());
		assertEquals("Marion Cotillard", acteurs.get(0).getIdentite());
	}
	
	/**
	 * Extraire la liste des acteurs dont l'année de naissance est 1985.
	 * Astuce: fonction year(...)
	 */
	@Test
	public void testExtraireActeursParAnneeNaissance() {
		LocalDate ldate = LocalDate.of(1985, 1, 1);

		TypedQuery<Acteur> query = em.createQuery("SELECT a FROM Acteur a WHERE YEAR(a.anniversaire) LIKE :annee", Acteur.class);
		query.setParameter("annee", ldate.getYear());
		List<Acteur> acteurs = query.getResultList();
		
		assertEquals(10, acteurs.size());
	}
	
	/**
	 * Extraire la liste des actrices ayant joué le rôle d'Harley QUINN
	 */
	@Test
	public void testExtraireActeursParRole() {
		
		TypedQuery<Acteur> query = em.createQuery("SELECT a FROM Acteur a JOIN a.roles r WHERE r.nom= :role", Acteur.class);
		query.setParameter("role", "Harley QUINN");
		List<Acteur> acteurs = query.getResultList();

		
		assertEquals(2, acteurs.size());
		assertEquals("Margot Robbie", acteurs.get(0).getIdentite());
		assertEquals("Margot Robbie", acteurs.get(1).getIdentite());
	}
	
	/**
	 * Extraire la liste de tous les acteurs ayant joué dans un film paru en 2015.
	 */
	@Test
	public void testExtraireActeursParFilmParuAnnee() {
		LocalDate ldate = LocalDate.of(2015, 1, 1);

		TypedQuery<Acteur> query = em.createQuery("SELECT a FROM Acteur a JOIN a.roles r WHERE r.film.annee LIKE :date", Acteur.class);
		query.setParameter("date", ldate.getYear());
		List<Acteur> acteurs = query.getResultList();
		assertEquals(140, acteurs.size());
	}
	
	/**
	 * Extraire la liste de tous les acteurs ayant joué dans un film français
	 * Astuce: mot clé distinct
	 */
	@Test
	public void testExtraireActeursParPays() {
		TypedQuery<Acteur> query = em.createQuery("SELECT DISTINCT a FROM Acteur a JOIN a.roles r JOIN r.film.pays p WHERE p.nom = :pays", Acteur.class);
		query.setParameter("pays", "France");
		List<Acteur> acteurs = query.getResultList();
		assertEquals(158, acteurs.size());
	}
	
	/**
	 * Extraire la liste de tous les acteurs ayant joué dans un film français paru en 2017
	 * Astuce: mot clé distinct
	 */
	@Test
	public void testExtraireActeursParListePaysEtAnnee() {
		LocalDate ldate = LocalDate.of(2017, 1, 1);
		TypedQuery<Acteur> query = em.createQuery("SELECT DISTINCT a FROM Acteur a JOIN a.roles r JOIN r.film.pays p WHERE p.nom = :pays AND r.film.annee = :date", Acteur.class);
		query.setParameter("pays", "France");
		query.setParameter("date", ldate.getYear());
		List<Acteur> acteurs = query.getResultList();
		assertEquals(24, acteurs.size());
	}
	
	/**
	 * Extraire la liste de tous les acteurs ayant joué dans un film réalisé par Ridley Scott 
	 * entre les années 2010 et 2020
	 * Astuce: mot clé distinct
	 */
	@Test
	public void testExtraireParRealisateurEntreAnnee() {
		TypedQuery<Acteur> query = em.createQuery("SELECT DISTINCT a FROM Acteur a JOIN a.roles r JOIN r.film.realisateurs re WHERE re.identite = :nom", Acteur.class);
		query.setParameter("nom", "Ridley Scott");
		List<Acteur> acteurs = query.getResultList();

		for(Acteur act : acteurs){
			System.out.println(act.getIdentite() );
		}
		assertEquals(28, acteurs.size());
	}
}