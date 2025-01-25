package com.liner.feed.domain.entity.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity (
  @Id
  @GeneratedValue
  val id: UUID = UUID.randomUUID(),
  val name: String,
  val username: String,
)