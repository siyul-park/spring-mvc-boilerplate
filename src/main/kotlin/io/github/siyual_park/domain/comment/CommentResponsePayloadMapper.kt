package io.github.siyual_park.domain.comment

import io.github.siyual_park.domain.Mapper
import io.github.siyual_park.model.comment.Comment
import io.github.siyual_park.model.comment.CommentResponsePayload
import org.springframework.stereotype.Component

@Component
class CommentResponsePayloadMapper : Mapper<Comment, CommentResponsePayload> {
    override fun map(input: Comment) = CommentResponsePayload.from(input)
}
