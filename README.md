# 🚢 Bataille Javale

Bienvenue dans **Bataille Javale**, une application interactive du jeu de la Bataille Navale, développée en **Java**. Plongez au cœur de l'océan et affrontez une intelligence artificielle redoutable !

## Technologies utilisées
* **Langage :** Java (JDK)
* **Interface Graphique :** JavaFX & FXML
* **Architecture :** Modèle-Vue-Contrôleur (MVC) pour une séparation claire de la logique métier (`logique`) et de l'affichage (`TUI` et `GUI`).

## Fonctionnalités

### Fonctionnalités principales
* **Interface Graphique :** Développée avec JavaFX.
* **Placement tactique des navires :** Interface dédiée pour positionner votre flotte (Porte-avions, Cuirassé, Destroyer, Sous-marin, Patrouilleur) avec gestion de la rotation et de la prévisualisation.
* **Joueur vs Ordinateur :** Affrontez un Bot impitoyable.
* **Suivi de la flotte en direct :** Affichage en temps réel de l'état de vos navires et de ceux de l'adversaire (En vie / Coulé ☠️).
* **Écran de fin de partie :** Récapitulatif avec le vainqueur, le nombre de tours joués.

### Fonctionnalités supplémentaires
* **Grille personnalisable :** Choisissez la taille de votre champ de bataille (grilles allant de **8x8 jusqu'à 15x15**).
* **Mode Console (CLI) :** Une deuxième interface 100% textuelle est incluse pour jouer directement dans le terminal !
* **Immersion Sonore :** Intégration de musiques d'ambiance et d'effets sonores (tirs de canons, explosions, tirs à l'eau) pour des affrontements épiques (seulement pour la version GUI).

## Installation et Lancement

### Prérequis
* Avoir Java JDK installé.
* Avoir un IDE compatible JavaFX ou Maven configuré.

### Lancement via l'Interface Graphique (JavaFX)
1. Clonez ce dépôt sur votre machine locale
2. Lancez le launcher souhaité de l'application : `TUILauncher` pour le jeu en ligne de commande ou `GUI` pour l'interface graphique

## Comment jouer en interface graphique ?
Configurez votre partie : Choisissez la taille de la grille souhaitée.

Placez vos navires : Sélectionnez un navire, utilisez la touche R pour changer son orientation, et cliquez sur la grille pour le positionner.

Au combat : Une fois tous les navires placés, lancez la partie. Cliquez sur la grille ennemie (à droite) pour tirer.

Gagnez : Coulez toute la flotte adverse avant qu'elle ne détruise la vôtre !

## Auteurs
@CDasse, @Jean-Nicolas21 et @croussey10
