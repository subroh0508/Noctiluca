package noctiluca.network.mastodon.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import noctiluca.network.mastodon.data.mediaattachment.NetworkMediaAttachment
import noctiluca.network.mastodon.data.mediaattachment.NetworkMediaAttachmentMeta

@Suppress("MagicNumber")
object MediaAttachmentSerializer : KSerializer<NetworkMediaAttachment> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("NetworkMediaAttachment") {
            element<String>("id")
            element<String>("type")
            element<String>("url")
            element<String>("preview_url")
            element<String?>("remote_url")
            element<NetworkMediaAttachmentMeta>("meta")
            element<String?>("description")
            element<String?>("blurhash")
        }

    override fun serialize(encoder: Encoder, value: NetworkMediaAttachment) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.id)
            encodeStringElement(descriptor, 1, value.type)
            encodeStringElement(descriptor, 2, value.url)
            encodeStringElement(descriptor, 3, value.previewUrl)
            encodeNullableStringElement(4, value.remoteUrl)
            encodeMeta(5, value.meta)
            encodeNullableStringElement(6, value.description)
            encodeNullableStringElement(7, value.blurhash)
        }
    }

    override fun deserialize(decoder: Decoder): NetworkMediaAttachment {
        var id: String? = null
        var type: String? = null
        var url: String? = null
        var previewUrl: String? = null
        var remoteUrl: String? = null
        var meta: NetworkMediaAttachmentMeta? = null
        var description: String? = null
        var blurhash: String? = null

        decoder.decodeStructure(descriptor) {
            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break@loop
                    0 -> id = decodeStringElement(descriptor, index)
                    1 -> type = decodeStringElement(descriptor, index)
                    2 -> url = decodeStringElement(descriptor, index)
                    3 -> previewUrl = decodeStringElement(descriptor, index)
                    4 -> remoteUrl = decodeStringNullableElement(index)
                    5 -> meta = decodeMeta(Type.valueOf(requireNotNull(type)), index)
                    6 -> description = decodeStringNullableElement(index)
                    7 -> blurhash = decodeStringNullableElement(index)
                    else -> throw IllegalArgumentException("Unexpected index: $index")
                }
            }
        }

        return NetworkMediaAttachment(
            id = requireNotNull(id),
            type = requireNotNull(type),
            url = requireNotNull(url),
            previewUrl = requireNotNull(previewUrl),
            remoteUrl = remoteUrl,
            meta = meta!!,
            description = description,
            blurhash = blurhash,
        )
    }

    @Suppress("EnumEntryNameCase", "EnumNaming")
    private enum class Type { image, video, gifv, audio }

    @OptIn(ExperimentalSerializationApi::class)
    private fun CompositeEncoder.encodeNullableStringElement(
        index: Int,
        value: String?,
    ) = value?.let {
        encodeStringElement(descriptor, index, it)
    } ?: encodeNullableSerializableElement(
        descriptor,
        index,
        String.serializer().nullable,
        null
    )

    private fun CompositeEncoder.encodeMeta(
        index: Int,
        meta: NetworkMediaAttachmentMeta,
    ) = when (meta) {
        is NetworkMediaAttachmentMeta.Image -> encodeSerializableElement(
            descriptor,
            index,
            NetworkMediaAttachmentMeta.Image.serializer(),
            meta
        )

        is NetworkMediaAttachmentMeta.Video -> encodeSerializableElement(
            descriptor,
            index,
            NetworkMediaAttachmentMeta.Video.serializer(),
            meta
        )

        is NetworkMediaAttachmentMeta.GifV -> encodeSerializableElement(
            descriptor,
            index,
            NetworkMediaAttachmentMeta.GifV.serializer(),
            meta
        )

        is NetworkMediaAttachmentMeta.Audio -> encodeSerializableElement(
            descriptor,
            index,
            NetworkMediaAttachmentMeta.Audio.serializer(),
            meta
        )
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun CompositeDecoder.decodeStringNullableElement(
        index: Int,
    ) = decodeNullableSerializableElement(
        descriptor,
        index,
        String.serializer().nullable,
    )

    private fun CompositeDecoder.decodeMeta(
        type: Type,
        index: Int,
    ) = when (type) {
        Type.image -> decodeSerializableElement(
            descriptor,
            index,
            NetworkMediaAttachmentMeta.Image.serializer()
        )

        Type.video -> decodeSerializableElement(
            descriptor,
            index,
            NetworkMediaAttachmentMeta.Video.serializer()
        )

        Type.gifv -> decodeSerializableElement(
            descriptor,
            index,
            NetworkMediaAttachmentMeta.GifV.serializer()
        )

        Type.audio -> decodeSerializableElement(
            descriptor,
            index,
            NetworkMediaAttachmentMeta.Audio.serializer()
        )
    }
}
