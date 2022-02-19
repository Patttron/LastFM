package teach.meskills.lastfm
import com.google.gson.annotations.SerializedName

data class JsonResponse(
    @SerializedName("tracks")
    val tracks: Tracks
) {
    data class Tracks(
        @SerializedName("track")
        val track: List<Track>
    ) {
        data class Track(
            @SerializedName("artist")
            val artist: Artist,
            @SerializedName("duration")
            val duration: String,
            @SerializedName("image")
            val image: List<Image>,
            @SerializedName("listeners")
            val listeners: String,
            @SerializedName("mbid")
            val mbid: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("playcount")
            val playcount: String,
            @SerializedName("streamable")
            val streamable: Streamable,
            @SerializedName("url")
            val url: String
        ) {
            data class Artist(
                @SerializedName("mbid")
                val mbid: String,
                @SerializedName("name")
                val name: String,
                @SerializedName("url")
                val url: String
            )

            data class Image(
                @SerializedName("size")
                val size: String,
                @SerializedName("#text")
                val text: String
            )

            data class Streamable(
                @SerializedName("fulltrack")
                val fulltrack: String,
                @SerializedName("#text")
                val text: String
            )
        }
    }
}
