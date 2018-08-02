package com.idarlington

object KeyIdentifier {

  val partitions = 5

  def stringUnicodeDecimal(key: String): Option[Int] = {
    Option(key(0).toInt)
  }

  def identifier(num: Int): Int = {
    num % partitions
  }
}
