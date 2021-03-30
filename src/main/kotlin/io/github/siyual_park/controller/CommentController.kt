package io.github.siyual_park.controller

import io.github.siyual_park.model.comment.CommentCreatePayload
import io.github.siyual_park.model.comment.CommentCreatePayloadMapper
import io.github.siyual_park.model.comment.CommentResponsePayload
import io.github.siyual_park.model.comment.CommentResponsePayloadMapper
import io.github.siyual_park.model.comment.CommentUpdatePayload
import io.github.siyual_park.repository.CommentRepository
import io.github.siyual_park.repository.patch.JsonMergePatchFactory
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Api
@RestController
@RequestMapping("/comments")
class CommentController(
    private val commentRepository: CommentRepository,
    private val commentResponsePayloadMapper: CommentResponsePayloadMapper,
    private val commentCreatePayloadMapper: CommentCreatePayloadMapper,
    private val jsonMergePatchFactory: JsonMergePatchFactory
) {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody payload: CommentCreatePayload): CommentResponsePayload {
        return commentRepository.create(commentCreatePayloadMapper.map(payload))
            .let { commentResponsePayloadMapper.map(it) }
    }

    @PatchMapping("/{comment-id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable("comment-id") id: String,
        @RequestBody payload: CommentUpdatePayload
    ): CommentResponsePayload {
        return commentRepository.updateById(id, jsonMergePatchFactory.create(payload))
            .let { commentResponsePayloadMapper.map(it) }
    }

    @GetMapping("/{comment-id}")
    @ResponseStatus(HttpStatus.OK)
    fun find(@PathVariable("comment-id") id: String): CommentResponsePayload {
        return commentRepository.findByIdOrFail(id)
            .let { commentResponsePayloadMapper.map(it) }
    }

    @DeleteMapping("/{comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("comment-id") id: String) {
        return commentRepository.deleteById(id)
    }
}
