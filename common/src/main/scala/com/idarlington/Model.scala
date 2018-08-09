package com.idarlington

object Model {

  trait Messages {
    def key: String
  }

  case class Set(key: String, value: String) extends Messages

  case class Get(key: String) extends Messages

  case class Update(key: String, value: String) extends Messages

  case class Delete(key: String) extends Messages

  case class GetContent()

  object Messages {
    def unapply(m: Messages) = Some(m.key)
  }

}
