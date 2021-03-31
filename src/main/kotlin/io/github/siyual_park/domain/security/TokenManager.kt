package io.github.siyual_park.domain.security

import io.github.siyual_park.confg.TokenProperty
import io.github.siyual_park.domain.scope.ScopeFetchExecutor
import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.model.token.Token
import io.github.siyual_park.model.user.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Component
class TokenManager(
    tokenProperty: TokenProperty,
    private val scopeFetchExecutor: ScopeFetchExecutor
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(tokenProperty.secret.toByteArray())

    fun generateToken(user: User, expiresIn: Long, scope: Set<ScopeToken>? = null) {
        val finalScope = if (scope != null) {
            val allScope = scopeFetchExecutor.execute(user)
            scope.filter { allScope.contains(it) }.toSet()
        } else {
            scopeFetchExecutor.execute(user, 0)
        }

        Token(
            user.id!!,
            Instant.now().plus(Duration.ofSeconds(expiresIn)),
            finalScope
        )
    }

    @Cacheable("TokenManager.encode(Token)")
    fun encode(token: Token): String {
        return Jwts.builder()
            .claim("jti", token.id)
            .claim("sub", token.userId)
            .claim("scope", token.scope.joinToString(" ") { it.name })
            .setIssuedAt(Date.from(token.createdAt))
            .setExpiration(Date.from(token.expiredAt))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    @Cacheable("TokenManager.decode(String)")
    fun decode(token: String): Token {
        val jwt = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parse(token)
        val body = jwt.body as Claims

        return Token(
            id = body["jti"] as String,
            userId = body["sub"] as String,
            scope = scopeFetchExecutor.execute(body["scope"] as String),
            createdAt = Instant.ofEpochSecond((body["iat"] as Int).toLong()),
            expiredAt = Instant.ofEpochSecond((body["exp"] as Int).toLong()),
        )
    }
}
