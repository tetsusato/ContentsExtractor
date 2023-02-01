
import org.scalatest.*
import flatspec.*
import matchers.*
import org.scalactic.*
import org.scalatest.funsuite.AnyFunSuite
import better.files.*
//import scala.jdk.CollectionConverters.*
import scala.jdk.StreamConverters.*
import org.tinylog.Logger

class MaterialSpec extends AnyFunSuite:

  test("plain text should be extracted") {
    val data = "test strings".getBytes()
    val doc = Material("test.txt", "text/plain", data)
    //println(doc)
    //println(doc.contentsTypeId)
    assert(doc != null)
    assert(doc.extractor == "test strings")

  }
  test("dox should be extracted") {
    val file = "."/"docx.docx"
    //val rawData = file.lines().toScala(Array).flatMap(f => f.getBytes("UTF-8"))
    val data = file.bytes.toArray
    val doc = Material("docx.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", data)
    //println(doc)
    //println(doc.contentsTypeId)
    assert(doc != null)
    //println(doc.extractor)
    assert(doc.extractor == "docx document\n")

  }
  test("doc should be extracted") {
    val file = "."/"doc.doc"
    //val rawData = file.lines().toScala(Array).flatMap(f => f.getBytes("UTF-8"))
    val data = file.bytes.toArray
    assert(data.length == 27136)
    // Logger.tags("NOTICE").info("rawdata from the doc file = {}({})", data.toSeq, data.length)
    //println(s"blob data from the doc file = ${data.getBytes(0, data.length.toInt)}(${data.length})")
    val doc = Material("doc.doc", "application/msword", data)
    Logger.tags("NOTICE").info("doc={}", doc)
    Logger.tags("NOTICE").info("doc.extractor=[{}]", doc.extractor)
        Logger.tags("NOTICE").info("doc.extractor.size=[{}]", doc.extractor.size)
    Logger.tags("NOTICE").info("doc.contents.size=[{}]", doc.contents.size)
    println(doc.contentsTypeId)
    assert(doc != null)
    Logger.tags("NOTICE").info("extracted doc=[{}]", doc.extractor)
    assert(doc.extractor == "This is\ndoc document")

  }
  test("xls should be extracted") {
    val file = "."/"xls.xls"
    //val rawData = file.lines().toScala(Array).flatMap(f => f.getBytes("UTF-8"))
    val data = file.bytes.toArray
    assert(data.length == 31232)
    //println(s"rawdata from the doc file = ${rawData.toSeq}(${rawData.length})")
    //println(s"blob data from the doc file = ${data.getBytes(0, data.length.toInt)}(${data.length})")
    val doc = Material("xls.xls", "application/vnd.ms-excel", data)
    //println(doc)
    //println(doc.contentsTypeId)
    assert(doc != null)
    //println(doc.extractor)
    assert(doc.extractor == "xls document\n")

  }
  test("xlsx should be extracted") {
    val file = "."/"xlsx.xlsx"
    //val rawData = file.lines().toScala(Array).flatMap(f => f.getBytes("UTF-8"))
    val data = file.bytes.toArray
    assert(data.length == 9533)
    //println(s"rawdata from the doc file = ${rawData.toSeq}(${rawData.length})")
    //println(s"blob data from the doc file = ${data.getBytes(0, data.length.toInt)}(${data.length})")
    val doc = Material("xlsx.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", data)
    //println(doc)
    //println(doc.contentsTypeId)
    assert(doc != null)
    //println(doc.extractor)
    assert(doc.extractor == "xlsx document\n")

  }
  test("pdf should be extracted") {
    val file = "."/"pdf.pdf"
    //val rawData = file.lines().toScala(Array).flatMap(f => f.getBytes("UTF-8"))
    val data = file.bytes.toArray
    assert(data.length == 59249)
    //println(s"rawdata from the pdf file = ${rawData.toSeq}(${rawData.length})")
    //println(s"blob data from the doc file = ${data.getBytes(0, data.length.toInt)}(${data.length})")
    val doc = Material("pdf.pdf", "application/pdf", data)
    Logger.tags("NOTICE").info("pdf=[{}]", doc)
    Logger.tags("NOTICE").info("pdf text=[{}]", doc.extractor)
    //println(doc.contentsTypeId)
    assert(doc != null)
    //println(doc.extractor)
    //assert(doc.extractor == "pdf document \r\n")
    assert(doc.extractor == "pdf document \n")

  }
