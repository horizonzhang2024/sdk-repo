import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException

class LicenseGenerator(private val context: Context) {

    private val jsonFilePath = "license.json"
    private lateinit var jsonArray: JSONArray

    fun loadJsonArrayFromFile(): JSONArray {
        val file = getJsonFile()
        jsonArray = if (file.exists()) {
            try {
                val jsonContent = file.readText()
                JSONArray(jsonContent)
            } catch (e: IOException) {
                JSONArray()
            }
        } else {
            JSONArray()
        }
        return jsonArray
    }

    private fun saveJsonArrayToFile() {
        val file = getJsonFile()
        file.writeText(jsonArray.toString())
    }

    private fun generateLicense(length: Int): String {
        val chars = ('A'..'Z') + ('0'..'9')
        val license = StringBuilder(length)

        repeat(length) {
            val randomChar = chars.random()
            license.append(randomChar)
        }

        return license.toString()
    }

    private fun isLicenseUnique(license: String): Boolean {
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            if (jsonObject.getString("license") == license) {
                return false
            }
        }
        return true
    }

    private fun getJsonFile(): File {
        val projectRootDir = File(context.filesDir?.absolutePath + "/LicenseKey")
        if (!projectRootDir.exists()) {
            projectRootDir.mkdirs()
        }
        return File(projectRootDir, jsonFilePath);
    }


    fun generateLicenses(num: Int) {
        loadJsonArrayFromFile()
        val startIndex = jsonArray.length()
        for (i in startIndex until (num + jsonArray.length())) {
            var license: String
            do {
                license = generateLicense(10)
            } while (!isLicenseUnique(license))

            val jsonObject = JSONObject()
            jsonObject.put("license", license)
            jsonArray.put(jsonObject)
            Log.d("LicenseGenerator", "Generated license: $license")
        }
        saveJsonArrayToFile()
        Log.d("LicenseGenerator", "Licenses generated and saved to $jsonFilePath")
    }
}
















