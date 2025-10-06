package fr.dawan.gestionprojet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionprojetApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionprojetApplication.class, args);
	}


	/*

    FetchType.LAZY sur collections/relations à charge (évite fetch massif).
    Roles EAGER si tu veux les avoir tout de suite pour l’auth (ou charger explicitement via service).
    orphanRemoval=true + cascade ALL sur Project.tasks = composition logique (tâches vivent avec projet).
    @JsonIgnore / DTOs pour éviter d’exposer l’objet entier et boucle JSON (choisir DTOs pour API est la meilleure pratique).


	FetchType.EAGER et FetchType.LAZY

	LAZY : la relation n’est chargée que si on y accède (au moment de l’accès).
	Avantage : performance, évite de charger des données inutiles.
	Risque : LazyInitializationException si on accède à la relation hors d’une transaction / session Hibernate (p. ex. dans le controller après fermeture du contexte).

	EAGER : la relation est chargée immédiatement avec l’entité parent (via jointure SQL ou requêtes supplémentaires selon le provider).
	Avantage : on a tout de suite les données (utile pour petites collections telles que roles si on en a besoin tout de suite pour auth).
	Inconvénient : peut causer des requêtes lourdes ou le problème N+1 si on charge une liste d’entités avec des relations EAGER.

	Règle pratique :

	Mettre LAZY par défaut sur les collection (@OneToMany, @ManyToMany) et souvent aussi sur @ManyToOne (même si JPA met ManyToOne à EAGER par défaut — tu peux forcer LAZY).

	Charger explicitement via fetch join ou EntityGraph quand tu en as besoin (dans les repository/service).

	Exemple : charger user + roles via query JOIN FETCH dans UserRepository pour l’authentification au lieu d’avoir roles en EAGER partout.



	@ToString et @ToString.Exclude (Lombok) -- exclure des champs

	Lombok génère automatiquement toString(). Si toString() inclut des relations bidirectionnelles, tu peux créer :

	une récursion infinie (A -> B -> A -> B -> ...) → StackOverflowError.
	ou forcer le chargement d’un proxy LAZY dans un contexte inattendu (déclenche une requête SQL).

	Solution : @ToString.Exclude sur les champs relationnels (collections, @ManyToOne etc.).

	Exemple :

	@OneToMany(mappedBy="project")
	@ToString.Exclude
	private Set<Task> tasks;


	Bonne pratique : éviter d’inclure des collections ou relations longues dans toString().



	Relations JPA — rappel et bonnes pratiques

	Principales relations et leur sens

	@OneToMany / @ManyToOne
	Project 1 --- * Task : une Task appartient à un Project.
	Côté propriétaire (owning side) : Task (avec @ManyToOne et @JoinColumn).
	Côté inverse : Project a @OneToMany(mappedBy="project").
	Si Project compose les tâches : cascade = CascadeType.ALL + orphanRemoval = true -> supprimer un projet supprime ses tâches.

	@ManyToMany
	Exemple : User <-> Role ou Project <-> User (membres).
	Utilise une table d’association (@JoinTable). Choisir FetchType.LAZY en général.
	Owning side vs inverse side
	Owning side = la table qui possède la colonne FK (ex : Task.project_id).

	mappedBy indique le côté inverse (ne génère pas de colonne supplémentaire).

	Cascade et orphanRemoval

	CascadeType.PERSIST / MERGE / REMOVE / ALL : appliquer l’opération sur l’entité parent également aux entités enfants.

	Exemple : CascadeType.ALL sur Project.tasks pour créer/supprimer automatiquement les tasks.

	orphanRemoval = true : si on retire un Task de project.getTasks() et qu’il n’est plus référencé, Hibernate le supprime de la DB. Utile pour composition.

	Exemple :

	// côté Task (owning)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	// côté Project (inverse)
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	private Set<Task> tasks = new HashSet<>();

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) -- explication:

	But : permettre la désérialisation (lecture de la valeur lorsqu’un JSON est envoyé au serveur) mais empêcher la sérialisation
	(ne pas l’inclure dans la réponse JSON renvoyée au client).

	Usage typique : champ password.

	Le client envoie {"username":"toto","password":"secret"} → l’API peut lire password (WRITE_ONLY).

	Lorsqu’on renvoie l’objet User, le champ password n’est pas envoyé au client.

	Alternative : @JsonIgnore (empêche sérialisation et désérialisation par défaut).

	Si on veut accepter le mot de passe en input mais ne pas l’exposer en output, WRITE_ONLY est préférable.

	exemple :

	public class User {
		private String username;

		@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
		private String password;
	}

	Remarque : temporaire le temps d'avoir créé un DTO spécifique pour les échanges API : UserDTO sans password pour la sortie, RegisterDTO pour l’inscription (avec password).

 	*/

}
