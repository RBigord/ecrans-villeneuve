# Les Écrans de Villeneuve — Architecture de la billetterie

Projet final du module **Concevoir une application numérique** (B3).

**Dépôt de l'équipe** : [_ecrans-villeneuve_](https://github.com/RBigord/ecrans-villeneuve.git)

## Équipe

| Membre | Étapes codées pendant le TP | Sections du DAT dont il ou elle est référent·e | Partie présentée à l'oral |
|---|---|---|---|
| Raphaël Bigord (`RBigord`) | Phase 1 : Event Storming, glossaire, bounded contexts, grille de décision, création du dépôt (pas d'étape de code) | 1, 2, 3, 4, 5, 9, 11 | Le problème et le domaine (context map, core domain, grille) |
| Mathis Valentin (`mathisv78510-hue`) | Phase 2 : étapes 1 à 5 (`Seance`, `Commande`, les trois services) et étape 6 (dépendance cachée corrigée) | 8, 10 | Le parcours « réserver et payer » et la décision 3 (concurrence) |
| Loulia Tsui (`loulia`) | Phase 3 : diagrammes de composants et de séquence ; étapes 7 et 8 (Spring Boot, ventes partenaires SortiesFlash) | 6, 7 | L'hexagone, les adapters Spring, l'ACL et une hypothèse |

## Organisation du dépôt

```
ecrans-villeneuve/
├── README.md                              ← ce fichier : l'équipe, le lien du dépôt, les jalons
├── code/
│   └── vente/                             ← le cœur de la Vente de places, en Java (TP du J4)
│       ├── pom.xml
│       ├── README.md
│       ├── fournis/                       ← les tests des étapes 7 et 8, à copier
│       └── src/
│           ├── main/java/com/ecrans/vente/    ← domaine, application, adapters, config
│           └── test/java/com/ecrans/vente/    ← un dossier de tests par étape, bonus, scénario
└── docs/
    ├── DAT-Ecrans-de-Villeneuve.docx      ← le Document d'Architecture Technique (11 sections)
    ├── DAT-Ecrans-de-Villeneuve.pdf       ← son export PDF, à produire pour le rendu
    └── diagrammes/                        ← par diagramme : sa source (.drawio, .puml…) et son export PNG
        ├── 1-contexte.drawio              (section 3)
        ├── 2-context-map.drawio           (section 4)
        ├── 3-composants-vente.drawio      (section 6)
        ├── 4-sequence-reserver-payer.drawio  (section 7)
        └── 5-modele-vente.drawio          (section 8)
```

## Conventions

- Les noms du code (`Seance`, `Commande`, `ReserverDesPlaces`…) sont ceux du glossaire et des diagrammes.
- Le DAT est un fichier Word : **une seule personne le modifie à la fois**. Prévenez les autres, faites *Update Project* avant, *Commit and Push* juste après.
- Un diagramme = une personne à la fois : on ne fusionne pas deux dessins.
- Chaque membre de l'équipe committe son travail.

## Jalons

- [x] **J4, fin de la phase 1** : glossaire, context map v0 et grille dans le DAT, poussés
- [x] **J4, fin de la phase 2** : tests verts pour les étapes 1 à 5, recherches de l'étape 6 sans résultat
- [x] **J4, fin de la phase 3** : `3-composants-vente` terminé, `4-sequence-reserver-payer` commencé
- [x] **Avant le J5** : étapes 7 et 8 faites (Spring Boot, ventes partenaires ; `ApiVenteTest` et `VentePartenaireApiTest` verts, Swagger répond), séquence terminée, DAT rédigé, composants mis à jour
- [x] **J5, finalisation** : dernières retouches, DAT exporté en PDF dans `docs/`, version taguée `v1.0` et poussée
- [x] **Rendu sur Moodle** avant les passages : le lien de ce dépôt (tag `v1.0` poussé, DAT en PDF dans `docs/`)
