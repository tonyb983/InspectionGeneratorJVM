package im.tony.google.services

import com.google.api.services.docs.v1.Docs
import com.google.api.services.docs.v1.model.*
import im.tony.Const
import im.tony.utils.StringGetter
import im.tony.utils.asStringGetter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.google.api.services.docs.v1.model.Request as DocsRequest

public interface GoogleDocsService {
  public val violationTemplate: Document
  public val noViolationTemplate: Document

  public fun createReplaceTextRequest(
    findText: String,
    matchCase: Boolean,
    replaceText: String
  ): DocsRequest = createReplaceTextRequest(findText, matchCase, StringGetter.create(replaceText))

  public fun createReplaceTextRequest(
    findText: String,
    matchCase: Boolean,
    replaceText: StringGetter
  ): DocsRequest

  public fun createBatchUpdateRequest(
    vararg requests: DocsRequest
  ): BatchUpdateDocumentRequest

  public fun executeRequests(
    docId: String,
    vararg requests: DocsRequest
  ): BatchUpdateDocumentResponse
}

private val DocsServiceImpl =
  object :
    GoogleDocsService,
    GenericService<Docs>(lazy { ServiceCreator.createDocs() }) {

    override val violationTemplate: Document by lazy { service.documents().get(Const.ViolationDocId).execute() }
    override val noViolationTemplate: Document by lazy { service.documents().get(Const.NoViolationDocId).execute() }

    override fun createReplaceTextRequest(
      findText: String,
      matchCase: Boolean,
      replaceText: String
    ): DocsRequest = DocsRequest()
      .setReplaceAllText(
        ReplaceAllTextRequest()
          .setContainsText(SubstringMatchCriteria().setText(findText).setMatchCase(matchCase))
          .setReplaceText(replaceText)
      )

    override fun createReplaceTextRequest(
      findText: String,
      matchCase: Boolean,
      replaceText: StringGetter
    ): DocsRequest = DocsRequest()
      .setReplaceAllText(
        ReplaceAllTextRequest()
          .setContainsText(SubstringMatchCriteria().setText(findText).setMatchCase(matchCase))
          .setReplaceText(replaceText.value)
      )

    override fun createBatchUpdateRequest(
      vararg requests: DocsRequest
    ): BatchUpdateDocumentRequest =
      BatchUpdateDocumentRequest()
        .setRequests(requests.toMutableList())

    override fun executeRequests(
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

public val DocsService: GoogleDocsService = DocsServiceImpl
