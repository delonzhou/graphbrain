package com.graphbrain.eco

import com.graphbrain.nlp.Lemmatiser

class Words(val words: Array[Word]=Array[Word]()) {
  def text = words.map(_.word).reduceLeft(_ + " " + _)

  def count = words.size

  override def toString =
    if (words.length > 0) words.map(_.toString).reduceLeft(_ + " " + _) else ""
}

object Words {
  val l = new Lemmatiser

  def empty = new Words()

  def fromString(s: String) =
    new Words(l.annotate(s).map(w => new Word(w._1, w._2, w._3)).toArray)
}