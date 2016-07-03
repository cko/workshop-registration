package models

import play.api.libs.json.Json
import java.sql.Timestamp
import java.time.Instant

case class Workshop(
  id: Option[Long],
  regstart: String,
  regend: String,
  regmax: Int,
  when: String,
  title: String,
  description: String)
