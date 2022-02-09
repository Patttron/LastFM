package teach.meskills.lastfm

import com.google.gson.annotations.SerializedName

data class AudioResponseDTO(
    @SerializedName("tracks")
    val tracks: Tracks
)

data class Tracks(
    @SerializedName("track")
    val track: List<Track>?
)

data class Track(
    @SerializedName("artist")
    val artist: Artist,
    @SerializedName("image")
    val image: List<Image>,
    @SerializedName("name")
    val name: String
)

data class Artist(
    @SerializedName("name")
    val name: String,
)

data class Image(
    @SerializedName("size")
    val size: String,
    @SerializedName("#text")
    val text: String
)


