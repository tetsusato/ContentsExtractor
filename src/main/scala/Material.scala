import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.hwpf.extractor.WordExtractor
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hssf.extractor.ExcelExtractor
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xssf.extractor.XSSFExcelExtractor
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.pdmodel.PDDocument
import org.tinylog.Logger

import java.io.ByteArrayInputStream
import scala.util.matching.Regex


case class Material(fileName: String, contentsType: String, contents: Array[Byte]):
  enum supportedContentsType:
    case docx
    case doc
    case txt
    case xls
    case xlsx
    case pdf
    case unsupported

  val docxString = new Regex("^(application/vnd.openxmlformats-officedocument.wordprocessingml.document)$")
  val docString = new Regex("^(application/msword)$")
  val txtString = new Regex("^(text/plain)$")
  val xlsString = new Regex("^(application/vnd.ms-excel)$")
  val xlsxString = new Regex("^(application/vnd.openxmlformats-officedocument.spreadsheetml.sheet)$")
  val pdfString = new Regex("^(application/pdf)$")
  val contentsTypeId =
    contentsType match {
      case docxString(s)
      => supportedContentsType.docx
      case docString(s)
      => supportedContentsType.doc
      case txtString(s)
        => supportedContentsType.txt
      case xlsString(s)
      => supportedContentsType.xls
      case xlsxString(s)
      => supportedContentsType.xlsx
      case pdfString(s)
      => supportedContentsType.pdf
      case _
        => supportedContentsType.unsupported

    }
  def XWPFExtractor(content: Array[Byte]) =
    val is = new ByteArrayInputStream(content)
    var text = ""
    try {
      val doc = new XWPFDocument(is)
      val extractor = new XWPFWordExtractor(doc)
      text = extractor.getText().toString
      //println(s"class = ${extractor.getText().getClass}")
      //println(s"text = ${extractor.getText().toString}")
    } catch {
      case e: Throwable => println(e)
    }
    text
//  def removeLast
  def HWPFExtractor(content: Array[Byte]) =
    val is = new ByteArrayInputStream(content)
    var text = ""
    try {
      val doc = new HWPFDocument(is)
      val extractor = new WordExtractor(doc)
      //Logger.tags("NOTICE").debug("getText=[{}]", extractor.getText())
      //Logger.tags("NOTICE").debug("getDocument={}", extractor.getDocument())
      //Logger.tags("NOTICE").debug("getDocument={}", extractor.getDocument().characterLength())
      //Logger.tags("NOTICE").debug("getDocument.getText=[{}]", extractor.getDocument().getText())
      //text = extractor.getText().toSeq.toString().replaceAll("\\r\\n", "") // なんでWindowsの改行コードが入るのかは謎
      text = extractor.getText().toSeq.toString().replaceAll("\\n+$", "") // なんでWindowsの改行コードが入るのかは謎
      //println(s"class = ${extractor.getText().getClass}")
      //println(s"text = ${text}(${text.length})")
      //println(s"text dump = ${text.map(f => "%02x".format(f.toInt))}(${text.length})")

    } catch {
      case e: Throwable => println(e)
    }
    text
  def HSSFExtractor(content: Array[Byte]) =
    val is = new ByteArrayInputStream(content)
    var text = ""
    try {
      val wb = new HSSFWorkbook(is)
      val extractor = new ExcelExtractor(wb)
      extractor.setIncludeHeadersFooters(false)
      extractor.setIncludeSheetNames(false)
      //text = extractor.getText().toSeq.toString().replaceAll("\\r\\n", "") // なんでWindowsの改行コードが入るのかは謎
      text = extractor.getText().toSeq.toString()
      //println(s"class = ${extractor.getText().getClass}")
      //println(s"text = ${text}(${text.length})")
      //println(s"text dump = ${text.map(f => "%02x".format(f.toInt))}(${text.length})")
      //println(s"text dump = (cunked)")
    } catch {
      case e: Throwable => println(e)
    }
    text
  def XSSFExtractor(content: Array[Byte]) =
    val is = new ByteArrayInputStream(content)
    var text = ""
    try {
      val wb = new XSSFWorkbook(is)
      val extractor = new XSSFExcelExtractor(wb)
      extractor.setIncludeHeadersFooters(false)
      extractor.setIncludeSheetNames(false)
      //text = extractor.getText().toSeq.toString().replaceAll("\\r\\n", "") // なんでWindowsの改行コードが入るのかは謎
      text = extractor.getText().toSeq.toString()
      //println(s"class = ${extractor.getText().getClass}")
      //println(s"text = ${text}(${text.length})")
      //println(s"text dump = ${text.map(f => "%02x".format(f.toInt))}(${text.length})")
      //println(s"text dump = (cunked)")
    } catch {
      case e: Throwable => println(e)
    }
    text
  def PDFExtractor(content: Array[Byte]) =
    //val is = new ByteArrayInputStream(content)
    val stripper = new PDFTextStripper()
    var text = ""
    val document =
      try
        Some(PDDocument.load(content))
      catch
        case e: Exception => Logger.tags("NOTICE").error("ERROR={}", e)
                             e.printStackTrace()
                             None
    document match
      case Some(document) =>
        //println(document.getNumberOfPages())
        for p <- Range(1, document.getNumberOfPages()+1) do
        //println(s"---------------------------- page=${p}")
          stripper.setStartPage(p)
          stripper.setEndPage(p)
          text += stripper.getText(document)
        document.close()
      case None => text = "*"
    text

  def extractor: String =
    val len = contents.length.toInt
    Logger.tags("NOTICE", "INFO").info("contents size = {}", len)
    //println(contents.getBytes(1, 10).map(f => f.toChar).toSeq)
    //val content = contents.getBytes(1, len)
    if len > 2 then
      Logger.tags("NOTICE", "INFO")
                   .info("header = {}{}", contents(0).toChar, contents(1).toChar)
    val is = new ByteArrayInputStream(contents)

    val text = contentsTypeId match {
      case supportedContentsType.docx => XWPFExtractor(contents)
      case supportedContentsType.txt => contents.foldLeft("")((f, g) => f + g.toChar)
      case supportedContentsType.doc => HWPFExtractor(contents)
      case supportedContentsType.xls => HSSFExtractor(contents)
      case supportedContentsType.xlsx => XSSFExtractor(contents)
      case supportedContentsType.pdf => PDFExtractor(contents)
      case supportedContentsType.unsupported =>
        s"${contentsType} is not supported."

    }
    text
