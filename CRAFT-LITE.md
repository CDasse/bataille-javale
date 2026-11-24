# CRAFT LITE : Bataille Javale

## Lancement
- **Auteur :** Charlie Roussey
- **Commande exacte pour exécuter les tests :**
  ```bash
  ./mvnw test
  ```
  *(ou `mvn test` si Maven est installé globalement, ou `.\mvnw.cmd test` sous l'invite de commande Windows)*

## 2. Diagnostic (tag lite-start)
- **Outil d'analyse :** SonarQube for IDE n'a pas été utilisé. Le diagnostic a été réalisé par revue de code manuelle approfondie et analyse statique des métriques du domaine métier.
- **Classe métier la plus complexe :** `Grid` (`school.coda.jn_charlie_clemence.bataillejavale.logique.models.Grid`)
  - **Nombre de lignes :** 107 lignes
  - **Nombre de méthodes :** 11 méthodes (1 constructeur + 10 méthodes publiques)
  - **Complexité cyclomatique estimée :** ~34 (selon McCabe), en particulier dans `canPlaceShip` (~12-13) avec des conditions composites de bornes et de vérification des cellules.

### Tableau des 3 code smells identifiés dans le code métier

| Nom du Smell | Localisation exacte | Explication concrète du problème |
| :--- | :--- | :--- |
| **Tight Coupling via `new` & Dépendance non-déterministe** | `school/coda/jn_charlie_clemence/bataillejavale/logique/models/BotPlayer.java` -> `BotPlayer#getNextMove` (et `BotPlayer#placeCpuShip`) | **Couplage & Testabilité impossible :** L'instanciation en dur `Random rand = new Random();` au sein de la méthode lie directement l'algorithme à l'aléa système non contrôlable. Impossible d'injecter une graine (`seed`) ou une doublure de test (Mock/Stub). Le comportement de repli aléatoire et la boucle `do ... while (playerGrid.isCellAlreadyTargeted(...))` sont non-déterministes et rendent les tests unitaires instables ou impossibles. |
| **Complex Conditional & Duplicated Logic** | `school/coda/jn_charlie_clemence/bataillejavale/logique/models/Grid.java` -> `Grid#canPlaceShip` (dupliqué avec `Grid#placeShip`) | **Lisibilité & Risque d'erreur :** `canPlaceShip` cumule des conditions booléennes asymétriques selon l'orientation puis réitère 5 prédicats dans sa boucle. De plus, le calcul des coordonnées de segments `(orientation == HORIZONTAL ? x + i : x)` et le parcours du navire sont dupliqués mot pour mot dans `placeShip`, multipliant le risque de désynchronisation. |
| **Feature Envy & Violation de "Tell, Don't Ask"** | `school/coda/jn_charlie_clemence/bataillejavale/logique/rules/Game.java` -> `Game#executeShot` (et `Game#nextHumanTurn`) | **Couplage fort & Mauvaise encapsulation :** `Game` manipule directement la structure interne de `Grid` via `Player` (`opponent.getGrid().shoot()`, `opponent.getGrid().getShipAt()`, `shipHit.isSunk()`, `player.getGrid().allShipsSunk()`). Au lieu de déléguer l'attaque au joueur, `Game` extrait la grille et fait le travail, ce qui complique les tests unitaires et viole la loi de Déméter. |

### Justification du choix du smell à refactorer en priorité
Le smell **`Tight Coupling via new` dans `BotPlayer#getNextMove`** a été choisi en priorité pour les raisons suivantes :
1. **Contrainte du Test Double :** L'instanciation directe de `new Random()` est le cas d'école parfait nécessitant une Inversion de Contrôle (IoC) par injection de dépendance, permettant d'isoler l'aléa via un Test Double (`StubRandom`) sans modifier le contrat externe.
2. **Richesse comportementale pour les tests unitaires :** La méthode gère deux comportements distincts : le mode traque (pile de cibles LIFO issue des tirs réussis précédents) et le mode chasse (fallback aléatoire avec évitement des cases déjà touchées). Cela offre un terrain idéal pour 5 tests unitaires nominaux/limites, 1 test paramétré et 1 test double.
3. **Sécurité du refactoring :** L'isolation de `Random` et la clarification des deux stratégies se font à comportement strictement constant sans effet de bord sur le reste du jeu.

## 3. Tests écrits
- **Classe testée :** `school.coda.jn_charlie_clemence.bataillejavale.logique.models.BotPlayer`
- **Fichier de test :** `src/test/java/school/coda/jn_charlie_clemence/bataillejavale/logique/models/BotPlayerTest.java`
- **Dépendances utilisées :** JUnit 5 Jupiter (`5.12.1`), AssertJ (`3.27.3`), Mockito (`5.16.0`).

### Liste des tests unitaires
1. `shouldReturnLastPushedTarget_whenNextTargetsContainsValidCoordinates` *(Nominal pile)* : vérifie que la dernière cible empilée suite à un tir réussi est dépilée en priorité.
2. `shouldSkipAlreadyTargetedCellsInStack_andReturnFirstAvailableTarget` *(Limite pile)* : vérifie qu'une coordonnée empilée déjà visée est ignorée pour renvoyer la première coordonnée libre suivante.
3. `shouldFallbackToRandomShot_whenAllStackedTargetsAreAlreadyTargeted` *(Limite pile épuisée)* : vérifie que si toutes les cibles empilées sont déjà visées, la pile est vidée et l'algorithme bascule sur le tir aléatoire.
4. `shouldUseRandomCoordinates_whenTargetStackIsEmpty` *(Nominal aléatoire)* : vérifie que lorsque la pile est initialement vide, le tir aléatoire est sollicité et ses coordonnées retournées.
5. `shouldRetryRandomCoordinates_whenFirstRandomTargetIsAlreadyTargeted` *(Limite collision aléatoire)* : vérifie que la boucle `do ... while` boucle lorsqu'une coordonnée déjà visée est tirée et sélectionne la coordonnée libre suivante.
6. `shouldNotPushTargets_whenAttackMisses` : vérifie qu'un tir manqué n'empile aucun voisin et déclenche le tir aléatoire.
7. `shouldClearStackedTargets_whenResetStrategyIsCalled` : vérifie que l'appel à `resetStrategy()` vide bien la pile de cibles.

### Détails du `@ParameterizedTest`
- **Méthode :** `shouldTargetCorrectAdjacentCell_whenHitAtVariousGridPositions(int hitX, int hitY, int expectedNextX, int expectedNextY)`
- **Source :** `@CsvSource` avec 4 jeux de données couvrant les cas limites de bordures et le cas nominal central :
  - `0, 0, 0, 1` : Tir réussi dans l'angle supérieur gauche $\rightarrow$ voisin inférieur sélectionné (voisins négatifs filtrés).
  - `0, 5, 0, 6` : Tir réussi sur le bord gauche $\rightarrow$ voisin inférieur sélectionné (voisin gauche négatif filtré).
  - `5, 5, 4, 5` : Tir réussi au centre de la grille $\rightarrow$ 4 voisins valides, le dernier empilé (gauche `[4, 5]`) est retourné.
  - `9, 9, 8, 9` : Tir réussi dans l'angle inférieur droit $\rightarrow$ voisin gauche sélectionné (voisins `x>=10` et `y>=10` filtrés).

### Détail du Test Double
- **Type :** `Stub` (`StubRandom extends Random`).
- **Rôle :** Fournit une séquence déterministe de coordonnées préprogrammées lors des appels successifs à `nextInt(bound)`.
- **Intérêt :** Évite les problématiques d'instrumentation bytecode / restrictions de modules Java 25 rencontrées lors du mock dynamique de classes internes de `java.base`, tout en évitant toute sur-spécification.

## 4. Refactoring réalisé
- **Techniques appliquées :**
  1. **Dependency Injection (IoC) :** Ajout d'un constructeur surchargé `BotPlayer(String, int, int, Random)` pour injecter l'aléa, le constructeur par défaut déléguant via `this(..., new Random())`.
  2. **Extract Method sur `getNextMove` :**
     - `pollTargetFromStack(Grid playerGrid)` : extrait le dépilage conditionnel de `nextTargets`.
     - `generateRandomTarget(Grid playerGrid)` : extrait la génération aléatoire et la boucle de non-collision.
  3. **Extract Method sur `recordResult` :**
     - `isWithinBoundsAndUntargeted(int x, int y, Grid playerGrid)` : extrait le prédicat composite de validation.
  4. **Extract Method sur `placeCpuShip` :**
     - `placeSingleShip(Ship ship)` : modularise le placement unitaire d'un navire.
  5. **Renommages & Nettoyage :** Renommage de `cardinalites` en `cardinalNeighbors`, suppression des variables locales redondantes `Random rand = this.random;`.

- **Tests de sécurisation :** La suite `BotPlayerTest` (11 tests au total) a validé le comportement à chaque étape.

- **Extrait Avant / Après (`BotPlayer#getNextMove`) :**
  ```java
  // AVANT
  @Override
  public int[] getNextMove(Grid playerGrid) {
      while (!nextTargets.empty()){
          int[] potentialTarget = nextTargets.peek();
          if (playerGrid.isCellAlreadyTargeted(potentialTarget[0], potentialTarget[1])) {
              nextTargets.pop();
          } else {
              return nextTargets.pop();
          }
      }

      Random rand = new Random();
      int maxX = this.getGrid().getWidth();
      int maxY = this.getGrid().getHeight();
      int targetedX;
      int targetedY;
      do {
          targetedX = rand.nextInt(maxX);
          targetedY = rand.nextInt(maxY);
      } while (playerGrid.isCellAlreadyTargeted(targetedX, targetedY));

      return new int[]{targetedX, targetedY};
  }

  // APRÈS
  @Override
  public int[] getNextMove(Grid playerGrid) {
      int[] targetFromStack = pollTargetFromStack(playerGrid);
      if (targetFromStack != null) {
          return targetFromStack;
      }
      return generateRandomTarget(playerGrid);
  }
  ```

- **Commits concernés :**
  - `9686fbf` : `test(bot): add unit and parameterized tests for BotPlayer target selection and random fallback`
  - `cb8e1ac` : `refactor(bot): extract target resolution and random generation methods in BotPlayer`

## 5. Ce qui reste à faire
- **Code smells non traités :**
  - `Grid#canPlaceShip` / `placeShip` : mutualiser la vérification des bornes et le calcul des coordonnées de parcours pour éliminer la duplication algorithmique.
  - `Game#executeShot` : appliquer le principe "Tell, Don't Ask" en confiant à `Player` la méthode `receiveAttack(x, y)` au lieu de laisser `Game` fouiller dans `Grid`.
- **Bugs et limites repérés sans correction silencieuse :**
  - *Bornes aléatoires erronées dans `BotPlayer#getNextMove` :* Utilisation de `this.getGrid().getWidth()` (grille du bot) au lieu de `playerGrid.getWidth()` (grille de l'adversaire). En cas de grilles asymétriques, le bot peut tirer hors bornes.
  - *Risque de boucle infinie (Livelock) dans `generateRandomTarget` :* Pas de condition d'arrêt si toutes les cases de la grille adverse ont été ciblées.
  - *Risque de boucle infinie dans `placeSingleShip` :* Pas de limite de tentatives ni de backtracking en cas de grille encombrée.
  - *Doublons dans `nextTargets` :* `recordResult` n'empêche pas d'empiler plusieurs fois la même case voisine si deux cases adjacentes ont été touchées.
  - *Structure obsolète :* Remplacer `Stack<int[]>` par `Deque<int[]>`.
- **Ce qui serait fait avec 1 heure de plus :**
  1. Corriger le bug des dimensions de grille dans `generateRandomTarget` en utilisant `playerGrid`.
  2. Ajouter un garde contre la boucle infinie dans `generateRandomTarget` (retourner un tableau vide ou lever une exception explicite si la grille est pleine).
  3. Remplacer `Stack` par `ArrayDeque` et éviter d'empiler des coordonnées déjà présentes dans `nextTargets`.
  4. Refactorer `Grid#canPlaceShip` et `Grid#placeShip` avec une méthode privée `getShipCoordinates(x, y, size, orientation)` pour supprimer la duplication.
  5. Écrire la suite de tests pour `GridTest` couvrant les placements valides, collisions et dépassements de bornes.
