package com.cacau.slotCarsApp.models

data class ComicDataWrapper (val code : Int?, val status : String?, val copyright: String?, val attributionText : String?,
                             val attributionHTML : String? , val data : ComicDataContainer? , val etag: String?)

data class ComicDataContainer (val offset: Int?, val limit: Int?, val total : Int?, val count: Int?, val results: List<Comic>?)

data class Comic  (val id:  Int?, val digitalId: Int?, val title: String?, val issueNumber: Double?, val variantDescription: String?,
                   val description: String?, val modified: String?, val isbn : String?, val upc: String?, val diamondCode: String?, val ean: String?,
                   val format: String, val pageCount: Int?, val textObjects: List<TextObject>?, val resourceURI: String? , val urls : List<Url>?,
                   val series: SeriesSummary?, val variants: List<ComicSummary>?, val collections: List<ComicSummary>?, val collectedIssues: List<ComicSummary>?,
                   val dates: List<ComicDate>?, val price: List<ComicPrice>?, val thumbnail: Image?, val images: List<Image>?, val creators: CreatorList?,
                   val characters: CharacterList, val stories: StoryList?, val events: EventList?)

data class TextObject (val type: String?, val language: String?, val text: String?)

data class Url (val type: String?, val url: String?)

data class SeriesSummary (val resourceURI: String?, val name: String?)

data class ComicSummary (val resourceURI: String?, val name: String?)

data class ComicDate (val type: String?, val date: String?)

data class ComicPrice (val type: String?, val price: Float?)

data class Image (val path: String?, val extension: String?)

data class CreatorList (val available: Int?, val returned: Int?, val collectionURI: String?, val items: List<CreatorSummary>?)

data class CreatorSummary (val resourceURI: String?, val name: String? , val role: String?)

data class CharacterList (val available: Int?, val returned: Int?, val collectionURI: String?, val items: List<CharacterSummary>?)

data class CharacterSummary (val resourceURI: String?, val name: String? , val role: String?)

data class StoryList (val available: Int?, val returned: Int?, val collectionURI: String?, val items: List<StorySummary>?)

data class StorySummary (val resourceURI: String?, val name: String? , val type: String?)

data class EventList (val available: Int?, val returned: Int?, val collectionURI: String?, val items: List<EventSummary>?)

data class EventSummary (val resourceURI: String?, val name: String?)
