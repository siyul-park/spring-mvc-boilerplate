package io.github.siyual_park.model.comment

import io.github.siyual_park.model.Mapper

class CommentResponsePayloadMapper : Mapper<Comment, CommentResponsePayload> {
    override fun map(input: Comment) = CommentResponsePayload.from(input)
}
