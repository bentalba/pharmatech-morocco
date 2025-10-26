package com.pharmatech.morocco.features.hospital.data

import android.content.Context
import com.pharmatech.morocco.features.hospital.domain.model.*
import org.apache.poi.ss.usermodel.WorkbookFactory
import timber.log.Timber
import java.io.File
import java.io.FileInputStream

/**
 * Utility class to import hospital and primary care facility data from XLSX files
 *
 * Excel file paths on local machine:
 * - Hospitals: C:\Users\LENOVO\Desktop\Pharmacie\repartition-des-hopitaux-par-region-et-province-2022.xlsx
 * - Primary Care: C:\Users\LENOVO\Desktop\Pharmacie\etablissements-de-soins-de-sante-primaire-2022.xlsx
 *
 * IMPORTANT FOR SONNET 4.5:
 * 1. These files are located on the local machine at the paths above
 * 2. Copy these files to: app/src/main/assets/data/
 * 3. Rename them to:
 *    - hospitals.xlsx
 *    - primary_care.xlsx
 * 4. Use AssetXlsxImporter class for Android import
 */
object XlsxDataImporter {

    /**
     * Import hospitals from XLSX file
     * Expected columns: Region, Province, Hospital Name, Type (HP/HR/HIR/etc), Address, Phone
     */
    fun importHospitalsFromXlsx(filePath: String): List<Hospital> {
        val hospitals = mutableListOf<Hospital>()

        try {
            val file = File(filePath)
            if (!file.exists()) {
                Timber.e("Hospital file not found at: $filePath")
                return emptyList()
            }

            FileInputStream(file).use { fis ->
                val workbook = WorkbookFactory.create(fis)
                val sheet = workbook.getSheetAt(0) // First sheet

                // Skip header row
                for (rowIndex in 1 until sheet.physicalNumberOfRows) {
                    val row = sheet.getRow(rowIndex) ?: continue

                    try {
                        val region = row.getCell(0)?.stringCellValue ?: ""
                        val province = row.getCell(1)?.stringCellValue ?: ""
                        val hospitalName = row.getCell(2)?.stringCellValue ?: ""
                        val typeAbbr = row.getCell(3)?.stringCellValue ?: ""
                        val address = row.getCell(4)?.stringCellValue
                        val phone = row.getCell(5)?.stringCellValue

                        // Determine hospital type
                        val hospitalType = HospitalType.fromAbbreviation(typeAbbr) ?: HospitalType.HP

                        // For now, use default coordinates (will be geocoded later)
                        val hospital = Hospital(
                            id = "hospital_${region}_${province}_${rowIndex}",
                            name = hospitalName,
                            type = hospitalType,
                            region = region,
                            province = province,
                            city = province, // Use province as city for now
                            address = address,
                            latitude = 33.5731, // Default to Morocco center
                            longitude = -7.5898,
                            phoneNumber = phone
                        )

                        hospitals.add(hospital)
                        HealthFacilityDatabase.addHospital(hospital)

                    } catch (e: Exception) {
                        Timber.e(e, "Error parsing hospital row $rowIndex")
                    }
                }

                workbook.close()
            }

            Timber.i("Successfully imported ${hospitals.size} hospitals")

        } catch (e: Exception) {
            Timber.e(e, "Error importing hospitals from: $filePath")
        }

        return hospitals
    }

    /**
     * Import primary care facilities from XLSX file
     * Expected columns: Region, Province, Commune, Facility Name, Type (CSR-1/CSU-2/etc), Address
     */
    fun importPrimaryCareFacilitiesFromXlsx(filePath: String): List<PrimaryCareFacility> {
        val facilities = mutableListOf<PrimaryCareFacility>()

        try {
            val file = File(filePath)
            if (!file.exists()) {
                Timber.e("Primary care file not found at: $filePath")
                return emptyList()
            }

            FileInputStream(file).use { fis ->
                val workbook = WorkbookFactory.create(fis)
                val sheet = workbook.getSheetAt(0) // First sheet

                // Skip header row
                for (rowIndex in 1 until sheet.physicalNumberOfRows) {
                    val row = sheet.getRow(rowIndex) ?: continue

                    try {
                        val region = row.getCell(0)?.stringCellValue ?: ""
                        val province = row.getCell(1)?.stringCellValue ?: ""
                        val commune = row.getCell(2)?.stringCellValue
                        val facilityName = row.getCell(3)?.stringCellValue ?: ""
                        val typeAbbr = row.getCell(4)?.stringCellValue ?: ""
                        val address = row.getCell(5)?.stringCellValue
                        val phone = row.getCell(6)?.stringCellValue

                        // Determine facility type
                        val facilityType = PrimaryCareType.fromAbbreviation(typeAbbr) ?: PrimaryCareType.CSR1

                        // For now, use default coordinates (will be geocoded later)
                        val facility = PrimaryCareFacility(
                            id = "primarycare_${region}_${province}_${rowIndex}",
                            name = facilityName,
                            type = facilityType,
                            region = region,
                            province = province,
                            commune = commune,
                            address = address,
                            latitude = 33.5731, // Default to Morocco center
                            longitude = -7.5898,
                            phoneNumber = phone
                        )

                        facilities.add(facility)
                        HealthFacilityDatabase.addPrimaryCareFacility(facility)

                    } catch (e: Exception) {
                        Timber.e(e, "Error parsing primary care row $rowIndex")
                    }
                }

                workbook.close()
            }

            Timber.i("Successfully imported ${facilities.size} primary care facilities")

        } catch (e: Exception) {
            Timber.e(e, "Error importing primary care from: $filePath")
        }

        return facilities
    }

    /**
     * Import both hospital and primary care data
     */
    fun importAllHealthFacilities(
        hospitalsFilePath: String,
        primaryCareFilePath: String
    ): Pair<List<Hospital>, List<PrimaryCareFacility>> {
        val hospitals = importHospitalsFromXlsx(hospitalsFilePath)
        val primaryCare = importPrimaryCareFacilitiesFromXlsx(primaryCareFilePath)
        return Pair(hospitals, primaryCare)
    }
}

/**
 * Android-specific importer that reads from app's assets folder
 *
 * USAGE INSTRUCTIONS FOR SONNET 4.5:
 * 1. Copy XLSX files from C:\Users\LENOVO\Desktop\Pharmacie\ to app/src/main/assets/data/
 * 2. Rename:
 *    - repartition-des-hopitaux-par-region-et-province-2022.xlsx → hospitals.xlsx
 *    - etablissements-de-soins-de-sante-primaire-2022.xlsx → primary_care.xlsx
 * 3. Call this importer from your app initialization code
 */
class AssetXlsxImporter(private val context: Context) {

    /**
     * Import hospitals from assets folder
     * Place the XLSX file in app/src/main/assets/data/hospitals.xlsx
     */
    fun importHospitalsFromAssets(fileName: String = "hospitals.xlsx"): List<Hospital> {
        val hospitals = mutableListOf<Hospital>()

        try {
            context.assets.open("data/$fileName").use { inputStream ->
                val workbook = WorkbookFactory.create(inputStream)
                val sheet = workbook.getSheetAt(0)

                // Skip header row
                for (rowIndex in 1 until sheet.physicalNumberOfRows) {
                    val row = sheet.getRow(rowIndex) ?: continue

                    try {
                        val region = row.getCell(0)?.stringCellValue ?: ""
                        val province = row.getCell(1)?.stringCellValue ?: ""
                        val hospitalName = row.getCell(2)?.stringCellValue ?: ""
                        val typeAbbr = row.getCell(3)?.stringCellValue ?: ""
                        val address = row.getCell(4)?.stringCellValue
                        val phone = row.getCell(5)?.stringCellValue

                        val hospitalType = HospitalType.fromAbbreviation(typeAbbr) ?: HospitalType.HP

                        val hospital = Hospital(
                            id = "hospital_${region}_${province}_${rowIndex}",
                            name = hospitalName,
                            type = hospitalType,
                            region = region,
                            province = province,
                            city = province,
                            address = address,
                            latitude = 33.5731,
                            longitude = -7.5898,
                            phoneNumber = phone
                        )

                        hospitals.add(hospital)
                        HealthFacilityDatabase.addHospital(hospital)

                    } catch (e: Exception) {
                        Timber.e(e, "Error parsing hospital row $rowIndex")
                    }
                }

                workbook.close()
            }

            Timber.i("Successfully imported ${hospitals.size} hospitals from assets")

        } catch (e: Exception) {
            Timber.e(e, "Error importing hospitals from assets: $fileName")
        }

        return hospitals
    }

    /**
     * Import primary care facilities from assets folder
     * Place the XLSX file in app/src/main/assets/data/primary_care.xlsx
     */
    fun importPrimaryCareFacilitiesFromAssets(fileName: String = "primary_care.xlsx"): List<PrimaryCareFacility> {
        val facilities = mutableListOf<PrimaryCareFacility>()

        try {
            context.assets.open("data/$fileName").use { inputStream ->
                val workbook = WorkbookFactory.create(inputStream)
                val sheet = workbook.getSheetAt(0)

                // Skip header row
                for (rowIndex in 1 until sheet.physicalNumberOfRows) {
                    val row = sheet.getRow(rowIndex) ?: continue

                    try {
                        val region = row.getCell(0)?.stringCellValue ?: ""
                        val province = row.getCell(1)?.stringCellValue ?: ""
                        val commune = row.getCell(2)?.stringCellValue
                        val facilityName = row.getCell(3)?.stringCellValue ?: ""
                        val typeAbbr = row.getCell(4)?.stringCellValue ?: ""
                        val address = row.getCell(5)?.stringCellValue
                        val phone = row.getCell(6)?.stringCellValue

                        val facilityType = PrimaryCareType.fromAbbreviation(typeAbbr) ?: PrimaryCareType.CSR1

                        val facility = PrimaryCareFacility(
                            id = "primarycare_${region}_${province}_${rowIndex}",
                            name = facilityName,
                            type = facilityType,
                            region = region,
                            province = province,
                            commune = commune,
                            address = address,
                            latitude = 33.5731,
                            longitude = -7.5898,
                            phoneNumber = phone
                        )

                        facilities.add(facility)
                        HealthFacilityDatabase.addPrimaryCareFacility(facility)

                    } catch (e: Exception) {
                        Timber.e(e, "Error parsing primary care row $rowIndex")
                    }
                }

                workbook.close()
            }

            Timber.i("Successfully imported ${facilities.size} primary care facilities from assets")

        } catch (e: Exception) {
            Timber.e(e, "Error importing primary care from assets: $fileName")
        }

        return facilities
    }

    /**
     * Import all health facilities from assets
     */
    fun importAllFromAssets(): Pair<List<Hospital>, List<PrimaryCareFacility>> {
        val hospitals = importHospitalsFromAssets()
        val primaryCare = importPrimaryCareFacilitiesFromAssets()
        return Pair(hospitals, primaryCare)
    }
}
