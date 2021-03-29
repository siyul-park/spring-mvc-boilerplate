package io.github.siyual_park.controller

import io.github.siyual_park.model.comment.Comment
import io.github.siyual_park.model.comment.CommentCreatePayload
import io.github.siyual_park.model.comment.CommentCreatePayloadMapper
import io.github.siyual_park.model.comment.CommentUpdatePayload
import io.github.siyual_park.repository.ArticleRepository
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
@RequestMapping("/comment")
class CommentController(
    private val commentRepository: CommentRepository,
    articleRepository: ArticleRepository,
    private val jsonMergePatchFactory: JsonMergePatchFactory
) {
    private val commentCreatePayloadMapper = CommentCreatePayloadMapper(articleRepository)

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody payload: CommentCreatePayload): Comment {
        return commentRepository.create(commentCreatePayloadMapper.map(payload))
    }

    @PatchMapping("/{comment-id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable("comment-id") id: String,
        @RequestBody payload: CommentUpdatePayload
    ): Comment {
        return commentRepository.updateByIdOrFail(id, jsonMergePatchFactory.create(payload))
    }

    @GetMapping("/{comment-id}")
    @ResponseStatus(HttpStatus.OK)
    fun find(@PathVariable("comment-id") id: String): Comment {
        return commentRepository.findByIdOrFail(id)
    }

    @DeleteMapping("/{comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("comment-id") id: String) {
        return commentRepository.deleteByIdOrFail(id)
    }
}
