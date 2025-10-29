# SHIFAA Premium - Système de Pharmacie Marocain 🏥💊

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![Material 3](https://img.shields.io/badge/Design-Material%203-purple.svg)](https://m3.material.io)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](https://github.com/bentalba/pharmatech-morocco)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**SHIFAA Premium** est une application mobile complète de gestion pharmaceutique conçue spécifiquement pour le marché marocain. Alliant design premium et fonctionnalités avancées pour révolutionner l'accès aux services pharmaceutiques au Maroc.

---

## 📱 Aperçu

SHIFAA Premium offre un système intégré de gestion pharmaceutique avec:

- 🏥 **Annuaire Pharmacies** - Localisation des pharmacies de garde en temps réel
- 🏥 **Carte Hospitalière** - Géolocalisation des hôpitaux publics, privés et cliniques
- 💊 **Base Médicaments** - Catalogue complet des médicaments disponibles au Maroc
- 📊 **Portail Assurance** - Interface pour CNSS, CNOPS et mutuelles
- 👤 **Gestion Profil** - Compte utilisateur personnalisé avec historique
- ⏰ **Suivi Médication** - Rappels intelligents et suivi d'observance

---

## ✨ Fonctionnalités Principales

### 🏥 Pharmacies de Garde
- **Recherche géolocalisée** avec intégration Google Maps
- **Statut en temps réel** (ouvert/fermé/garde)
- **Service de livraison** indicateur pour les pharmacies offrant la livraison
- **Système de favoris** pour accès rapide
- **Navigation intégrée** avec itinéraires turn-by-turn
- **Filtres avancés** par ville, quartier, services

### 🏥 Carte Hospitalière
- **Hôpitaux publics** - CHU, hôpitaux régionaux, provinciaux
- **Secteur privé** - Cliniques et polycliniques privées
- **Filtres intelligents** par type, spécialité, services
- **Compteurs visuels** affichant nombre d'établissements par catégorie
- **Informations détaillées** - Contact, horaires, services disponibles
- **Import Excel** - Données hospitalières structurées (hospitals.xlsx, primary_care.xlsx)

### 💊 Gestion Médicaments
- **Base de données complète** avec informations médicaments Maroc
- **Recherche et filtres** par nom, catégorie, laboratoire
- **Classification** médicaments génériques/princeps
- **Informations posologiques** avec instructions détaillées
- **Alternatives génériques** pour économies
- **Scanner code-barres** pour recherche instantanée (à venir)

### 📊 Portail Assurance
- **Intégration CNSS** - Consultation remboursements employés secteur privé
- **Intégration CNOPS** - Suivi couverture fonctionnaires
- **Mutuelles privées** - Gestion multiple contrats assurance
- **Historique remboursements** avec statuts détaillés
- **Upload documents** pour demandes remboursement
- **Calcul automatique** taux couverture

### 👤 Gestion Profil
- **Compte invité** - Accès immédiat sans inscription
- **Authentification sécurisée** avec Firebase (Email, Google)
- **Profil utilisateur** - Ville, informations personnelles
- **Historique consultations** - Pharmacies, hôpitaux visités
- **Paramètres personnalisés** - Notifications, préférences
- **Favoris synchronisés** - Pharmacies et hôpitaux préférés

### ⏰ Suivi Médication
- **Rappels intelligents** adaptés à votre emploi du temps
- **Doses multiples** avec horaires personnalisables
- **Suivi d'adhérence** avec scoring par pourcentage
- **Historique prises** - Doses prises/manquées
- **Calendrier visuel** pour meilleure visibilité
- **Statistiques détaillées** - Graphiques adhérence

---

## 🏗️ Architecture Technique

### Stack Technologique
- **Language**: Kotlin 100%
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt (Dagger 2)
- **Base de données**: Room
- **Réseau**: Retrofit + OkHttp
- **Cartes**: Google Maps SDK
- **Import Excel**: Apache POI 5.2.5
- **Auth**: Firebase Authentication
- **Analytics**: Firebase Analytics
- **Crashlytics**: Firebase Crashlytics

### Configuration Build
```kotlin
minSdk = 26        // Android 8.0 Oreo
compileSdk = 34    // Android 14
targetSdk = 34     // Android 14
multiDexEnabled = true  // Support 65K+ méthodes
jvmTarget = "17"   // Java 17
```

### Dépendances Clés
```kotlin
// Compose & Material 3
androidx.compose.material3:material3:1.2.1
androidx.compose.ui:ui:1.6.7

// Architecture Components
androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0
androidx.navigation:navigation-compose:2.7.7
com.google.dagger:hilt-android:2.51.1

// Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Network
com.squareup.retrofit2:retrofit:2.11.0
com.squareup.okhttp3:logging-interceptor:4.12.0

// Excel Import
org.apache.poi:poi:5.2.5
org.apache.poi:poi-ooxml:5.2.5

// Maps
com.google.android.gms:play-services-maps:18.2.0
com.google.android.gms:play-services-location:21.2.0

// Firebase
com.google.firebase:firebase-auth:22.3.1
com.google.firebase:firebase-analytics:21.6.1
```

---

## 🎨 Design System - SHIFAA Premium

### Palette de Couleurs
```kotlin
// Or Premium (Couleur principale)
Gold = #D4AF37          // Or standard
GoldLight = #FFD700     // Or clair
GoldDark = #B8860B      // Or foncé

// Vert Pharmacie (Symbole médical)
PharmacyGreen = #2D5F3F // Vert principal
PharmacyGreenLight = #4A7C5D
PharmacyGreenDark = #1A3D28

// Bleu Turquoise (Accents)
TealDark = #1B4D52      // Turquoise foncé
TealMedium = #2C6B6F
TealLight = #3D8B8F

// Accents
Emerald = #50C878       // Émeraude
DarkGreen = #013220     // Vert très foncé

// Neutres
IvoryWhite = #FFFFF0    // Blanc ivoire
CreamWhite = #FFF8DC    // Blanc crème
CharcoalBlack = #1C1C1C // Noir charbon
WarmGray = #3E3E3E      // Gris chaud
```

### Principes Design
- **Premium**: Interface luxueuse avec or comme couleur signature
- **Médical**: Vert pharmacie pour crédibilité santé
- **Moderne**: Material 3 avec animations fluides
- **Accessible**: Contraste élevé, textes lisibles
- **Cohérent**: Design system unifié sur toutes les screens

---

## 📂 Structure du Projet

```
app/src/main/java/com/pharmatech/morocco/
├── MainActivity.kt
├── PharmaTechApp.kt
│
├── core/
│   ├── data/
│   │   ├── local/          # Room Database
│   │   ├── remote/         # API Services
│   │   └── repository/     # Repository implementations
│   ├── di/
│   │   ├── AppModule.kt    # Hilt modules
│   │   ├── DatabaseModule.kt
│   │   └── NetworkModule.kt
│   └── utils/
│       ├── Constants.kt
│       ├── Extensions.kt
│       └── NetworkResult.kt
│
├── features/
│   ├── auth/
│   │   ├── data/
│   │   │   └── repository/
│   │   ├── domain/
│   │   │   └── model/
│   │   └── presentation/
│   │       ├── SplashScreen.kt
│   │       ├── LoginScreen.kt
│   │       └── RegisterScreen.kt
│   │
│   ├── home/
│   │   ├── domain/
│   │   └── presentation/
│   │       └── HomeScreen.kt
│   │
│   ├── pharmacy/
│   │   ├── data/
│   │   ├── domain/
│   │   │   └── model/
│   │   │       └── OnCallPharmacy.kt
│   │   └── presentation/
│   │       └── PharmacyScreen.kt
│   │
│   ├── hospital/
│   │   ├── data/
│   │   ├── domain/
│   │   │   └── model/
│   │   │       └── Hospital.kt
│   │   └── presentation/
│   │       └── HospitalMapScreen.kt
│   │
│   ├── medication/
│   │   ├── data/
│   │   ├── domain/
│   │   └── presentation/
│   │       ├── MedicationScreen.kt
│   │       └── MedicationListScreen.kt
│   │
│   ├── insurance/
│   │   ├── data/
│   │   ├── domain/
│   │   └── presentation/
│   │       └── InsurancePortalScreen.kt
│   │
│   ├── tracker/
│   │   ├── data/
│   │   ├── domain/
│   │   └── presentation/
│   │       └── TrackerScreen.kt
│   │
│   └── profile/
│       ├── data/
│       ├── domain/
│       │   └── model/
│       │       ├── UserProfile.kt
│       │       └── GuestProfile.kt
│       └── presentation/
│           └── ProfileScreen.kt
│
└── ui/
    ├── navigation/
    │   ├── PharmaTechNavigation.kt
    │   └── Screen.kt
    ├── theme/
    │   ├── Color.kt
    │   ├── Theme.kt
    │   └── Type.kt
    └── components/         # Composables réutilisables
```

---

## 🚀 Installation et Configuration

### Prérequis
- Android Studio Ladybug | 2024.2.1 ou supérieur
- JDK 17 (recommandé: jbr-17.0.14)
- Android SDK API 26+ (minimum)
- Android SDK API 34 (compilation)
- Gradle 8.13
- Git

### Étapes d'Installation

1. **Cloner le repository**
```bash
git clone https://github.com/bentalba/pharmatech-morocco.git
cd pharmatech-morocco
```

2. **Configurer le SDK Android**
- Créer `local.properties` à la racine:
```properties
sdk.dir=/path/to/your/Android/Sdk
```

3. **Configurer Firebase**
- Télécharger `google-services.json` depuis Firebase Console
- Placer dans `app/` directory
- Note: Fichier déjà présent, mais utiliser votre propre config en production

4. **Configurer Google Maps API**
- Obtenir clé API Google Maps depuis Google Cloud Console
- Ajouter dans `AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_API_KEY_HERE"/>
```

5. **Build le projet**
```bash
# Première build (peut prendre 5-7 minutes)
./gradlew clean assembleDebug

# Ou directement dans Android Studio:
# Build > Make Project (Ctrl+F9)
```

6. **Lancer sur émulateur**
```bash
# Via Gradle
./gradlew installDebug

# Lancer l'app
adb shell am start -n com.pharmatech.morocco/.MainActivity
```

### Configuration Émulateur Recommandée
- **Device**: Medium Phone (6.0", 1080x2340)
- **System Image**: Android 14 (API 36) x86_64
- **RAM**: 2048 MB minimum
- **Storage**: 2048 MB minimum
- **Graphics**: Hardware - GLES 2.0

---

## 📊 Données et Assets

### Fichiers Excel Inclus
1. **hospitals.xlsx** (16 KB)
   - Données hôpitaux publics/privés Maroc
   - Localisation: `app/src/main/assets/data/`
   - Colonnes: nom, type, ville, adresse, téléphone, coordonnées GPS

2. **primary_care.xlsx** (120 KB)
   - Données centres soins primaires
   - Localisation: `app/src/main/assets/data/`
   - Colonnes: nom, type, région, province, commune, coordonnées

### Import Automatique
- Import prévu au premier lancement (à implémenter)
- Utilise Apache POI 5.2.5 pour parsing Excel
- Sauvegarde dans Room Database local

---

## 🧪 Tests

### Tests Unitaires
```bash
./gradlew test
```

### Tests Instrumentés
```bash
./gradlew connectedAndroidTest
```

### Couverture de Code
```bash
./gradlew jacocoTestReport
```

---

## 📝 Documentation Détaillée

- **[PROJECT_STATUS.md](PROJECT_STATUS.md)** - État actuel détaillé du projet
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - Architecture détaillée
- **[CHANGELOG.md](CHANGELOG.md)** - Historique des versions
- **[CONTRIBUTING.md](CONTRIBUTING.md)** - Guide de contribution

---

## 🔧 Scripts de Build

### Windows
```batch
BUILD_NOW.bat           # Build rapide
LaunchApp.bat          # Build + Install + Launch
SystemCheck.bat        # Vérifier environnement
```

### macOS/Linux
```bash
./gradlew assembleDebug
./gradlew installDebug
```

---

## 🐛 Résolution de Problèmes

### Build échoue à 51%
**Cause**: Problème DEX/MultiDex  
**Solution**: Déjà résolu via MultiDex enablement

### App crash au lancement
**Solution**: Vérifier logcat
```bash
adb logcat | grep "com.pharmatech.morocco"
```

### Émulateur ne démarre pas
**Solution**: 
```bash
# Redémarrer ADB
adb kill-server
adb start-server

# Ou redémarrer émulateur
```

---

## 📈 Roadmap

### Version 1.0 (Actuelle) - ✅ Complétée
- [x] Architecture MVVM + Clean
- [x] Navigation 6 tabs
- [x] Theme SHIFAA Premium
- [x] Screens principales (UI)
- [x] Build avec MultiDex
- [x] Installation sur émulateur

### Version 1.1 (Prochaine) - 🔄 En cours
- [ ] Authentification Firebase complète
- [ ] Import automatique données Excel
- [ ] Intégration Google Maps
- [ ] Permissions runtime
- [ ] Système de favoris persistant

### Version 1.2 - 📅 Planifiée
- [ ] Base de données médicaments complète
- [ ] Scanner code-barres médicaments
- [ ] Notifications push
- [ ] Mode offline complet
- [ ] Synchronisation cloud

### Version 2.0 - 🎯 Futur
- [ ] IA Symptom Checker
- [ ] AR Medication Viewer
- [ ] Téléconsultation
- [ ] Chatbot support
- [ ] Multi-langue (Français, Arabe, Amazigh)

---

## 👥 Contribution

Les contributions sont les bienvenues! Voir [CONTRIBUTING.md](CONTRIBUTING.md) pour détails.

### Processus de Contribution
1. Fork le projet
2. Créer branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit changements (`git commit -m 'feat: Add AmazingFeature'`)
4. Push vers branche (`git push origin feature/AmazingFeature`)
5. Ouvrir Pull Request

### Conventions de Commit
```
feat: Nouvelle fonctionnalité
fix: Correction bug
docs: Documentation
style: Formatage
refactor: Refactoring
test: Tests
chore: Maintenance
```

### Gestion des Branches

Pour maintenir un repository propre et organisé:

- **Branches principales**: `master` (production) et `dev` (développement)
- **Branches temporaires**: Utiliser le format `feature/nom`, `fix/nom`, etc.
- **Nettoyage**: Supprimer les branches après merge

📖 **Guide complet**: Voir [BRANCH_CLEANUP.md](BRANCH_CLEANUP.md) pour:
- Liste des branches actuelles et recommandations
- Script de nettoyage automatique (`cleanup-branches.sh`)
- Conventions de nommage des branches
- Règles de protection des branches

```bash
# Nettoyer les anciennes branches
./cleanup-branches.sh --dry-run  # Simulation
./cleanup-branches.sh            # Exécution réelle
```

---

## 📄 License

Ce projet est sous licence MIT - voir le fichier [LICENSE](LICENSE) pour détails.

---

## 📞 Contact

**Développeur**: bentalba  
**Email**: [contact via GitHub]  
**Repository**: [https://github.com/bentalba/pharmatech-morocco](https://github.com/bentalba/pharmatech-morocco)

---

## 🙏 Remerciements

- **Material 3** pour le design system
- **Firebase** pour backend services
- **Apache POI** pour import Excel
- **Google Maps** pour géolocalisation
- **Jetpack Compose** pour UI moderne

---

## 📊 Statistiques

![GitHub code size](https://img.shields.io/github/languages/code-size/bentalba/pharmatech-morocco)
![GitHub last commit](https://img.shields.io/github/last-commit/bentalba/pharmatech-morocco)
![GitHub issues](https://img.shields.io/github/issues/bentalba/pharmatech-morocco)

---

**SHIFAA Premium** - Révolutionner l'accès aux soins pharmaceutiques au Maroc 🇲🇦
