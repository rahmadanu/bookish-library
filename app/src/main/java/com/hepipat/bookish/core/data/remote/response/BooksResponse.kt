package com.hepipat.bookish.core.data.remote.response


import com.google.gson.annotations.SerializedName

data class BooksResponse(
    @SerializedName("items")
    var items: List<Item> = listOf(),
    @SerializedName("kind")
    var kind: String = "",
    @SerializedName("totalItems")
    var totalItems: Int = 0,
) {
    data class Item(
        @SerializedName("accessInfo")
        var accessInfo: AccessInfo = AccessInfo(),
        @SerializedName("etag")
        var etag: String = "",
        @SerializedName("id")
        var id: String = "",
        @SerializedName("kind")
        var kind: String = "",
        @SerializedName("saleInfo")
        var saleInfo: SaleInfo = SaleInfo(),
        @SerializedName("searchInfo")
        var searchInfo: SearchInfo = SearchInfo(),
        @SerializedName("selfLink")
        var selfLink: String = "",
        @SerializedName("volumeInfo")
        var volumeInfo: VolumeInfo = VolumeInfo(),
    ) {
        data class AccessInfo(
            @SerializedName("accessViewStatus")
            var accessViewStatus: String = "",
            @SerializedName("country")
            var country: String = "",
            @SerializedName("embeddable")
            var embeddable: Boolean = false,
            @SerializedName("epub")
            var epub: Epub = Epub(),
            @SerializedName("pdf")
            var pdf: Pdf = Pdf(),
            @SerializedName("publicDomain")
            var publicDomain: Boolean = false,
            @SerializedName("quoteSharingAllowed")
            var quoteSharingAllowed: Boolean = false,
            @SerializedName("textToSpeechPermission")
            var textToSpeechPermission: String = "",
            @SerializedName("viewability")
            var viewability: String = "",
            @SerializedName("webReaderLink")
            var webReaderLink: String = "",
        ) {
            data class Epub(
                @SerializedName("isAvailable")
                var isAvailable: Boolean = false,
            )

            data class Pdf(
                @SerializedName("isAvailable")
                var isAvailable: Boolean = false,
            )
        }

        data class SaleInfo(
            @SerializedName("country")
            var country: String = "",
            @SerializedName("isEbook")
            var isEbook: Boolean = false,
            @SerializedName("saleability")
            var saleability: String = "",
        )

        data class SearchInfo(
            @SerializedName("textSnippet")
            var textSnippet: String = "",
        )

        data class VolumeInfo(
            @SerializedName("allowAnonLogging")
            var allowAnonLogging: Boolean = false,
            @SerializedName("authors")
            var authors: List<String> = listOf(),
            @SerializedName("canonicalVolumeLink")
            var canonicalVolumeLink: String = "",
            @SerializedName("contentVersion")
            var contentVersion: String = "",
            @SerializedName("description")
            var description: String = "",
            @SerializedName("industryIdentifiers")
            var industryIdentifiers: List<IndustryIdentifier> = listOf(),
            @SerializedName("infoLink")
            var infoLink: String = "",
            @SerializedName("language")
            var language: String = "",
            @SerializedName("maturityRating")
            var maturityRating: String = "",
            @SerializedName("pageCount")
            var pageCount: Int = 0,
            @SerializedName("panelizationSummary")
            var panelizationSummary: PanelizationSummary = PanelizationSummary(),
            @SerializedName("previewLink")
            var previewLink: String = "",
            @SerializedName("printType")
            var printType: String = "",
            @SerializedName("publishedDate")
            var publishedDate: String = "",
            @SerializedName("readingModes")
            var readingModes: ReadingModes = ReadingModes(),
            @SerializedName("title")
            var title: String = "",
        ) {
            data class IndustryIdentifier(
                @SerializedName("identifier")
                var identifier: String = "",
                @SerializedName("type")
                var type: String = "",
            )

            data class PanelizationSummary(
                @SerializedName("containsEpubBubbles")
                var containsEpubBubbles: Boolean = false,
                @SerializedName("containsImageBubbles")
                var containsImageBubbles: Boolean = false,
            )

            data class ReadingModes(
                @SerializedName("image")
                var image: Boolean = false,
                @SerializedName("text")
                var text: Boolean = false,
            )
        }
    }
}