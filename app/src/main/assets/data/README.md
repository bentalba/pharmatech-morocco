# Hospital and Primary Care Data Files

## IMPORTANT: Excel Files Location

**For SONNET 4.5 or Future Developer:**

The Excel files required for this application are located on the local machine at:

```
C:\Users\LENOVO\Desktop\Pharmacie\
```

## Required Files

### 1. Hospital Data
**Source File:** `repartition-des-hopitaux-par-region-et-province-2022.xlsx`
**Copy to:** `app/src/main/assets/data/hospitals.xlsx`

This file contains data for all public hospitals in Morocco, organized by:
- 8 Hospital Types: HP, HR, HIR, HPr, HPsyP, CRO, HPsyR, CPU
- Regions and Provinces
- Contact information and addresses

### 2. Primary Care Data
**Source File:** `etablissements-de-soins-de-sante-primaire-2022.xlsx`
**Copy to:** `app/src/main/assets/data/primary_care.xlsx`

This file contains data for primary care facilities, including:
- 8 Facility Types: CSR-1, CSR-2, CSU-1, CSU-2, DR, LSP, CRSR, CDTMR
- Communes and locations
- Services offered

## How to Add the Files

### Step 1: Copy Files
```bash
# On Windows machine where files are located:
1. Navigate to: C:\Users\LENOVO\Desktop\Pharmacie\
2. Copy the two XLSX files
3. Rename them as specified above
4. Place them in: app/src/main/assets/data/
```

### Step 2: Verify Structure
```
app/src/main/assets/data/
├── README.md (this file)
├── hospitals.xlsx (to be added)
└── primary_care.xlsx (to be added)
```

### Step 3: Import Data
The app will automatically import the data when:
1. The AssetXlsxImporter class is called
2. On first launch or data refresh
3. Via manual import trigger in the app

## Data Format

### Hospital File Columns
- Column A: Region
- Column B: Province
- Column C: Hospital Name
- Column D: Type (HP/HR/HIR/HPr/HPsyP/CRO/HPsyR/CPU)
- Column E: Address (optional)
- Column F: Phone (optional)

### Primary Care File Columns
- Column A: Region
- Column B: Province
- Column C: Commune (optional)
- Column D: Facility Name
- Column E: Type (CSR-1/CSR-2/CSU-1/CSU-2/DR/LSP/CRSR/CDTMR)
- Column F: Address (optional)
- Column G: Phone (optional)

## Usage in Code

```kotlin
// Import from assets
val importer = AssetXlsxImporter(context)

// Import hospitals
val hospitals = importer.importHospitalsFromAssets("hospitals.xlsx")

// Import primary care facilities
val primaryCare = importer.importPrimaryCareFacilitiesFromAssets("primary_care.xlsx")

// Or import all at once
val (hospitals, primaryCare) = importer.importAllFromAssets()
```

## Notes for Next Developer

1. **File Location:** The source files are on the local machine. You'll need to copy them from there.
2. **File Names:** Must be exactly `hospitals.xlsx` and `primary_care.xlsx`
3. **Location:** Must be in `app/src/main/assets/data/`
4. **Geocoding:** The importer uses default coordinates (Morocco center). You may want to add geocoding later.
5. **Error Handling:** The importer has comprehensive error handling and uses Timber for logging.

## Future Enhancements

- [ ] Add geocoding service to convert addresses to coordinates
- [ ] Add data validation and quality checks
- [ ] Implement incremental updates instead of full import
- [ ] Add caching layer for imported data
- [ ] Create admin interface for data management

---

**Last Updated:** 2025-10-26
**Version:** 1.0.0
