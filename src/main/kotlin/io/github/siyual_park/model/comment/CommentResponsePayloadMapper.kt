package io.github.siyual_park.model.comment

import io.github.siyual_park.model.Mapper
import org.springframework.stereotype.Component

@Component
class CommentResponsePayloadMapper : Mapper<Comment, CommentResponsePayload> {
    override fun map(input: Comment) = CommentResponsePayload.from(input)
}
