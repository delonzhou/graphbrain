package com.graphbrain.hgdb


abstract class Vertex {
  val id: String
  val edges: Set[String]
  val vtype: String
  
  def toMap: Map[String, Any]
  protected def toMapBase: Map[String, Any] = Map(("vtype" -> vtype), ("edges" -> iter2str(edges)))
  
  def setEdges(newEdges: Set[String]): Vertex

  def addEdge(edgeId: String): Vertex = setEdges(edges + edgeId)
  def delEdge(edgeId: String): Vertex = setEdges(edges - edgeId)

  protected def iter2str(iter: Iterable[String]) = {
    if (iter.size == 0)
      ""
    else
      (for (str <- iter)
        yield str.replace("$", "$1").replace(",", "$2")).reduceLeft(_ + "," + _)
  }
}


case class Edge(id: String, etype: String, edges: Set[String]=Set[String]()) extends Vertex {
  override val vtype: String = "edge"

  def this(etype:String, participants: Array[String]) =
    this((List[String](etype) ++ participants).reduceLeft(_ + " " + _),
      etype, Set[String]())

  override def toMap: Map[String, Any] = toMapBase ++ Map(("etype" -> etype))

  def setEdges(newEdges: Set[String]) = Edge(id, etype, newEdges)

  def participantIds = Edge.participantIds(id)
}

object Edge {
  def participantIds(edgeId: String) = {
    val tokens = edgeId.split(' ')
    for (i <- 1 until tokens.length) yield tokens(i)
  } 
} 


case class EdgeType(id: String, label: String, roles: List[String],
  rolen: String, edges: Set[String]=Set[String]()) extends Vertex {

  override val vtype: String = "etype"
  
  override def toMap: Map[String, Any] = toMapBase ++
    Map(("label" -> label), ("roles" -> iter2str(roles)), ("rolen" -> rolen))

  def setEdges(newEdges: Set[String]) = EdgeType(id, label, roles, rolen, newEdges)
}


case class TextNode(id: String, text: String, edges: Set[String]=Set[String]()) extends Vertex {
  override val vtype: String = "text"

  override def toMap: Map[String, Any] = toMapBase ++ Map(("text" -> text))

  def setEdges(newEdges: Set[String]) = TextNode(id, text, newEdges)
}


case class URLNode(id: String, url: String, edges: Set[String]=Set[String]()) extends Vertex {
  override val vtype: String = "url"

  override def toMap: Map[String, Any] = toMapBase ++ Map(("url" -> url))

  def setEdges(newEdges: Set[String]) = URLNode(id, url, newEdges)
}


case class ImageNode(id: String, url: String, edges: Set[String]=Set[String]()) extends Vertex {
  override val vtype: String = "image"

  override def toMap: Map[String, Any] = toMapBase ++ Map(("url" -> url))

  def setEdges(newEdges: Set[String]) = ImageNode(id, url, newEdges)
}


case class SourceNode(id: String, edges: Set[String]=Set[String]()) extends Vertex {
  override val vtype: String = "source"

  override def toMap: Map[String, Any] = toMapBase

  def setEdges(newEdges: Set[String]) = SourceNode(id, newEdges)
}


case class ErrorVertex(message: String) extends Vertex {
  override val vtype: String = "error"

  val id = ""
  val edges = Set[String]()

  override def toMap: Map[String, Any] = null

  def setEdges(newEdges: Set[String]) = ErrorVertex(message)
}