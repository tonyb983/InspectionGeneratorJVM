package im.tony.google.services

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.docs.v1.Docs
import com.google.api.services.drive.Drive
import com.google.api.services.sheets.v4.Sheets
import im.tony.Const
import im.tony.MyApp
import im.tony.Resources
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader

internal abstract class GenericService<TService : AbstractGoogleJsonClient>(lazyCreator: Lazy<TService>) {
  private val service_: TService by lazyCreator
  val service: TService
    get() = service_
}

internal interface ServiceInitializer {
  val jsonFactory: JacksonFactory
  val httpTransport: NetHttpTransport
  val credentials: Credential
  fun createDrive(): Drive
  fun createDocs(): Docs
  fun createSheets(): Sheets
}

private val serviceInitializerImpl = object : ServiceInitializer {
  override val jsonFactory: JacksonFactory by lazy { impl.createJsonFactory() }
  override val httpTransport: NetHttpTransport by lazy { impl.createHttpTransport() }
  override val credentials: Credential by lazy { impl.createCredentials(httpTransport, jsonFactory) }

  override fun createDrive() = impl.createDrive()
  override fun createDocs() = impl.createDocs()
  override fun createSheets() = impl.createSheets()

  private val impl = object {
    /**
     * ### Create the Google [Drive] service.
     *
     * @return[Drive] The newly created sheets service.
     */
    fun createDrive(): Drive = Drive.Builder(httpTransport, jsonFactory, credentials)
      .setApplicationName(Const.ApplicationName)
      .build()

    /**
     * ### Create the [com.google.api.services.docs.v1].[Docs] service.
     *
     * @return[Docs] The newly created [Docs] service.
     */
    fun createDocs(): Docs = Docs.Builder(httpTransport, jsonFactory, credentials)
      .setApplicationName(Const.ApplicationName)
      .build()

    /**
     * ### Create the Google [Sheets] service.
     *
     * @return[Sheets] The newly created sheets service.
     */
    fun createSheets(): Sheets = Sheets.Builder(httpTransport, jsonFactory, credentials)
      .setApplicationName(Const.ApplicationName)
      .build()

    /**
     * ### Creates a [JsonFactory] that will be used by the application.
     * #### *Currently this is implemented as a **[JacksonFactory]**.*
     *
     *@return[JacksonFactory]
     */

    fun createJsonFactory(): JacksonFactory = JacksonFactory.getDefaultInstance()

    /**
     * ### Creates the [HttpTransport] that will be used by the application.
     * #### *Currently uses the implementation provided by **[GoogleNetHttpTransport]**.*
     *
     * @return[NetHttpTransport] Provided by [GoogleNetHttpTransport.newTrustedTransport]
     */
    fun createHttpTransport(): NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()

    /**
     * ### Creates the [Credential] object for this app to access google's service apis.
     *
     * @param[httpTrans] The [HttpTransport] implementation to be used for the [GoogleAuthorizationCodeFlow.Builder].
     * @param[jsonFac] The [JsonFactory] implementation to be used in [GoogleClientSecrets.load] and [GoogleAuthorizationCodeFlow.Builder]
     * @return[Credential] The application credential object.
     */
    fun createCredentials(httpTrans: HttpTransport, jsonFac: JsonFactory): Credential {
      val inStream = MyApp::class.java.getResourceAsStream(Resources.Creds.credentials)
        ?: throw FileNotFoundException("Resource not found: ${Resources.Creds.credentials}")

      val clientSecrets = GoogleClientSecrets.load(jsonFac, InputStreamReader(inStream))

      val flow = GoogleAuthorizationCodeFlow.Builder(httpTrans, jsonFac, clientSecrets, Const.ApplicationScopes)
        .setDataStoreFactory(FileDataStoreFactory(File(Const.TokensDirectoryPath)))
        .setAccessType("offline")
        .build()

      val receiver = LocalServerReceiver.Builder().setPort(Const.DefaultPort).build()
      return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }
  }
}

internal val ServiceCreator: ServiceInitializer = serviceInitializerImpl


