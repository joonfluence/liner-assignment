package com.liner.feed.domain.entity.page

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "pages")
class PageEntity(
  @Id
  @GeneratedValue
  val id: Long = 0,
  val url: String,
  val title: String,
  val userId: Long,
)
