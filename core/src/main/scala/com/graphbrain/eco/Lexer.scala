package com.graphbrain.eco

import com.graphbrain.eco.TokenType.TokenType
import scala.collection.mutable.ListBuffer

class Lexer(val input: String) {
  private var pos: Int = 0
  private var c: Char = input(pos)
  private val EOF: Char = (-1).toChar
  private var lastType = TokenType.Unknown
  private var parHeap = List[TokenType]()

  def tokens = {
    val toks = ListBuffer[Token]()

    var tok = nextToken()
    while (tok != null) {
      toks.append(tok)
      tok = nextToken()
    }

    toks.toList
  }

  private def nextToken(): Token = {
    if (c == EOF) {
      null
    }
    else {
      while (c.isWhitespace) consume()

      val nt = predict match {
        case TokenType.Symbol => tokSymbol
        case TokenType.Number => tokNumber
        case TokenType.String => tokString
        case TokenType.Consequence => tokConsequence
        case TokenType.LPar => tokLPar
        case TokenType.RPar => tokRPar
        case TokenType.LParamPar => tokLParamPar
        case TokenType.RParamPar => tokRParamPar
        case TokenType.LSPar => tokLSPar
        case TokenType.RSPar => tokRSPar
        case TokenType.Quote => tokQuote
        case TokenType.Colon => tokColon
        case TokenType.Plus => tokPlus
        case TokenType.Minus => tokMinus
        case TokenType.Mul => tokMul
        case TokenType.Div => tokDiv
      }

      lastType = nt.ttype

      nt
    }
  }

  private def consume() = {
    pos += 1
    if (pos >= input.length)
      c = EOF
    else
      c = input.charAt(pos)
  }

  private def onLastChar = pos >= input.length - 1

  private def predict: TokenType = {
    if (c.isLetter)
      TokenType.Symbol
    else if (c.isDigit)
      TokenType.Number
    else
      c match {
        case '"' => TokenType.String
        case '(' => {
          if (lastType == TokenType.Symbol) {
            parHeap = TokenType.LParamPar :: parHeap
            TokenType.LParamPar
          }
          else {
            parHeap = TokenType.LPar :: parHeap
            TokenType.LPar
          }
        }
        case ')' => {
          val openPar = parHeap.head
          parHeap = parHeap.drop(1)
          if (openPar == TokenType.LPar) TokenType.RPar else TokenType.RParamPar
        }
        case '[' => TokenType.LSPar
        case ']' => TokenType.RSPar
        case ':' => TokenType.Colon
        case '+' => TokenType.Plus
        case '-' => {
          if (onLastChar) {
            TokenType.Minus
          }
          else {
            val next = input(pos + 1)
            if (next.isDigit)
              TokenType.Number
            else
              next match {
                case '.' => TokenType.Number
                case '>' => TokenType.Consequence
                case _ => TokenType.Minus
              }
          }
        }
        case '*' => TokenType.Mul
        case '/' => TokenType.Div
        case '\'' => TokenType.Quote
        case _ => TokenType.Unknown
    }
  }

  private def tokSymbol: Token = {
    val sb = new StringBuilder(25)
    var done = false

    while (!done) {
      sb.append(c)
      consume()

      if ((!c.isLetter)
        && (!c.isDigit)
        && (c != '-')
        && (c != '_')) {

        done = true
      }
    }

    new Token(sb.toString(), TokenType.Symbol)
  }

  private def tokNumber: Token = {
    val sb = new StringBuilder(25)
    var done = false
    var dotSeen = false

    while (!done) {
      if (c == '.') dotSeen = true

      sb.append(c)
      consume()

      if ((!c.isDigit)
        && ((c != '.') || ((c == '.') && dotSeen))) {

        done = true
      }
    }

    new Token(sb.toString(), TokenType.Number)
  }

  private def tokString: Token = {
    consume()
    if (onLastChar) return new Token("", TokenType.String)

    val sb = new StringBuilder(25)
    var done = false

    while (!done) {
      sb.append(c)
      consume()

      if (c == '"') {

        done = true
      }
    }

    consume()

    new Token(sb.toString(), TokenType.String)
  }

  private def tokConsequence: Token = {
    consume()
    consume()
    new Token("->", TokenType.Consequence)
  }

  private def tokLPar: Token = {
    consume()
    new Token("(", TokenType.LPar)
  }

  private def tokRPar: Token = {
    consume()
    new Token(")", TokenType.RPar)
  }

  private def tokLParamPar: Token = {
    consume()
    new Token("(", TokenType.LParamPar)
  }

  private def tokRParamPar: Token = {
    consume()
    new Token(")", TokenType.RParamPar)
  }

  private def tokLSPar: Token = {
    consume()
    new Token("[", TokenType.LSPar)
  }

  private def tokRSPar: Token = {
    consume()
    new Token("]", TokenType.RSPar)
  }

  private def tokQuote: Token = {
    consume()
    new Token("'", TokenType.Quote)
  }

  private def tokColon: Token = {
    consume()
    new Token(":", TokenType.Colon)
  }

  private def tokPlus: Token = {
    consume()
    new Token("+", TokenType.Plus)
  }

  private def tokMinus: Token = {
    consume()
    new Token("-", TokenType.Minus)
  }

  private def tokMul: Token = {
    consume()
    new Token("-", TokenType.Mul)
  }

  private def tokDiv: Token = {
    consume()
    new Token("/", TokenType.Div)
  }
}

object Lexer {
  def main(args: Array[String]) = {
    val l = new Lexer("1 + 1")
    println(l.tokens)
  }
}