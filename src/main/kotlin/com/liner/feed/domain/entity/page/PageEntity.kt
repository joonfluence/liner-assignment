package com.liner.feed.domain.entity.page

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "pages")
class PageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val url: String,
    val title: String,
    val userId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
)
