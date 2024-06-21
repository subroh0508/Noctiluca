package noctiluca.network.mastodon.spec.serializer

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.be
import io.kotest.matchers.should
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import noctiluca.network.mastodon.data.mediaattachment.NetworkMediaAttachment
import noctiluca.network.mastodon.data.mediaattachment.NetworkMediaAttachmentMeta
import noctiluca.network.mastodon.json.*

@OptIn(ExperimentalSerializationApi::class)
class MediaAttachmentSerializerSpec : DescribeSpec({
    val json = Json {
        explicitNulls = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    describe("#serialize") {
        withData(
            nameFn = { (data, _) -> "should return ${data.type} json" },
            Image to JSON_IMAGE,
            Video to JSON_VIDEO,
            GifV to JSON_GIFV,
            Audio to JSON_AUDIO,
        ) { (data, expected) ->
            json.encodeToString(data) should be(expected)
        }
    }

    describe("#deserialize") {
        withData(
            nameFn = { (json, expected) ->
                if (json.contains("text_url")) {
                    "should return ${expected.type} object (<3.5)"
                } else {
                    "should return ${expected.type} object (>=3.5)"
                }
            },
            JSON_IMAGE_3_4_10 to Image,
            JSON_VIDEO_3_4_10 to Video,
            JSON_GIFV_3_4_10 to GifV,
            JSON_AUDIO_3_4_10 to Audio,
            JSON_IMAGE to Image,
            JSON_VIDEO to Video,
            JSON_GIFV to GifV,
            JSON_AUDIO to Audio,
        ) { (data, expected) ->
            println(data)
            json.decodeFromString<NetworkMediaAttachment>(data) should be(expected)
        }
    }
})

private val Image = NetworkMediaAttachment(
    id = "22345792",
    type = "image",
    url = "https://files.mastodon.social/media_attachments/files/022/345/792/original/57859aede991da25.jpeg",
    previewUrl = "https://files.mastodon.social/media_attachments/files/022/345/792/small/57859aede991da25.jpeg",
    remoteUrl = null,
    meta = NetworkMediaAttachmentMeta.Image(
        original = NetworkMediaAttachmentMeta.ImageOriginal(
            width = 640,
            height = 480,
            size = "640x480",
            aspect = 1.3333333333333333,
        ),
        small = NetworkMediaAttachmentMeta.Small(
            width = 461,
            height = 346,
            size = "461x346",
            aspect = 1.3323699421965318,
        ),
        focus = NetworkMediaAttachmentMeta.Focus(
            x = -0.27,
            y = 0.51,
        ),
    ),
    description = "test media description",
    blurhash = "UFBWY:8_0Jxv4mx]t8t64.%M-:IUWGWAt6M",
)

val Video = NetworkMediaAttachment(
    id = "22546306",
    type = "video",
    url = "https://files.mastodon.social/media_attachments/files/022/546/306/original/dab9a597f68b9745.mp4",
    previewUrl = "https://files.mastodon.social/media_attachments/files/022/546/306/small/dab9a597f68b9745.png",
    remoteUrl = null,
    meta = NetworkMediaAttachmentMeta.Video(
        length = "0:01:28.65",
        duration = 88.65,
        fps = 24,
        size = "1280x720",
        width = 1280,
        height = 720,
        aspect = 1.7777777777777777,
        audioEncode = "aac (LC) (mp4a / 0x6134706D)",
        audioBitrate = "44100 Hz",
        audioChannels = "stereo",
        original = NetworkMediaAttachmentMeta.VideoOriginal(
            width = 1280,
            height = 720,
            frameRate = "6159375/249269",
            duration = 88.654,
            bitrate = 862056,
        ),
        small = NetworkMediaAttachmentMeta.Small(
            width = 400,
            height = 225,
            size = "400x225",
            aspect = 1.7777777777777777,
        ),
    ),
    description = null,
    blurhash = "U58E0g8_0M.94T?bIr00?bD%NGoM?bD%oLt7",
)

val GifV = NetworkMediaAttachment(
    id = "21130559",
    type = "gifv",
    url = "https://files.mastodon.social/media_attachments/files/021/130/559/original/bc84838f77991326.mp4",
    previewUrl = "https://files.mastodon.social/media_attachments/files/021/130/559/small/bc84838f77991326.png",
    remoteUrl = null,
    meta = NetworkMediaAttachmentMeta.GifV(
        length = "0:00:01.11",
        duration = 1.11,
        fps = 33,
        size = "600x332",
        width = 600,
        height = 332,
        aspect = 1.8072289156626506,
        original = NetworkMediaAttachmentMeta.VideoOriginal(
            width = 600,
            height = 332,
            frameRate = "100/3",
            duration = 1.11,
            bitrate = 1627639,
        ),
        small = NetworkMediaAttachmentMeta.Small(
            width = 400,
            height = 221,
            size = "400x221",
            aspect = 1.8099547511312217,
        ),
    ),
    description = null,
    blurhash = "URHT%Jm,2a1d%MRO%LozkrNH${'$'}*n*oMn${'$'}Rjt7",
)

val Audio = NetworkMediaAttachment(
    id = "21165404",
    type = "audio",
    url = "https://files.mastodon.social/media_attachments/files/021/165/404/original/a31a4a46cd713cd2.mp3",
    previewUrl = "https://files.mastodon.social/media_attachments/files/021/165/404/small/a31a4a46cd713cd2.mp3",
    remoteUrl = null,
    meta = NetworkMediaAttachmentMeta.Audio(
        length = "0:06:42.86",
        duration = 402.86,
        audioEncode = "mp3",
        audioBitrate = "44100 Hz",
        audioChannels = "stereo",
        original = NetworkMediaAttachmentMeta.AudioOriginal(
            duration = 402.860408,
            bitrate = 166290,
        ),
    ),
    description = null,
    blurhash = null,
)
