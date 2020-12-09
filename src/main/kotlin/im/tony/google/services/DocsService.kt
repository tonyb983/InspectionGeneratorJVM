package im.tony.google.services

import com.google.api.services.docs.v1.Docs
import com.google.api.services.docs.v1.model.*
import com.google.api.services.docs.v1.model.Request as DocsRequest
import im.tony.Const
import im.tony.types.docsmodels.DocsModelDocument
import im.tony.utils.StringGetter
import im.tony.utils.asStringGetter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface GoogleDocsService {
  val violationTemplate: Document
  val noViolationTemplate: Document
}

private val DocsServiceImpl =
  object :
    GoogleDocsService,
    GenericService<Docs>(lazy { ServiceCreator.createDocs() }) {

    override val violationTemplate: Document by lazy { service.documents().get(Const.ViolationDocId).execute() }
    override val noViolationTemplate: Document by lazy { service.documents().get(Const.NoViolationDocId).execute() }

    fun createReplaceTextRequest(
      findText: String,
      matchCase: Boolean,
      replaceText: String
    ): DocsRequest = DocsRequest()
      .setReplaceAllText(
        ReplaceAllTextRequest()
          .setContainsText(SubstringMatchCriteria().setText(findText).setMatchCase(matchCase))
          .setReplaceText(replaceText)
      )

    fun createReplaceTextRequest(
      findText: String,
      matchCase: Boolean,
      replaceText: StringGetter
    ): DocsRequest = DocsRequest()
      .setReplaceAllText(
        ReplaceAllTextRequest()
          .setContainsText(SubstringMatchCriteria().setText(findText).setMatchCase(matchCase))
          .setReplaceText(replaceText.value)
      )

    fun createBatchUpdateRequest(
      vararg requests: DocsRequest
    ): BatchUpdateDocumentRequest =
      BatchUpdateDocumentRequest()
        .setRequests(requests.toMutableList())

    fun executeRequests(
      docId: String,
      vararg requests: DocsRequest
    ): BatchUpdateDocumentResponse = service
      .documents()
      .batchUpdate(docId, createBatchUpdateRequest(*requests))
      .execute()

    fun replaceText(documentId: String) {
      val customerName = "Alice"
      val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
      val date: String = formatter.format(LocalDate.now())

      val requests: MutableList<DocsRequest> = mutableListOf()
      requests.add(createReplaceTextRequest("{{customer-name}}", true, customerName.asStringGetter()))
      requests.add(createReplaceTextRequest("{{date}}", true, date))

      val body = BatchUpdateDocumentRequest()
      val d = service.documents().batchUpdate(documentId, body.setRequests(requests)).execute()

    }
  }

val DocsService: GoogleDocsService = DocsServiceImpl
