package controllers

import play.api.mvc.{ Controller, Action }
import play.api.http.MimeTypes

import java.io.ByteArrayOutputStream
import java.awt.image.BufferedImage
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider
import org.krysalis.barcode4j.impl.upcean.EAN13Bean
import java.lang.IllegalArgumentException

object Barcodes extends Controller {
  val ImageResolution = 144

  def barcode(ean: Long) = Action {
    val MimeType = "image/png"

    try {
      val result = ean13BarCode(ean, MimeType)
      Ok(result).as(MimeType)
    } catch {
      case e: IllegalArgumentException =>
        BadRequest("Couldn't generate barcode, error msg:" + e.getMessage)
    }

  }

  def ean13BarCode(ean: Long, mimeType: String): Array[Byte] = {
    var output: ByteArrayOutputStream = new ByteArrayOutputStream
    var canvas: BitmapCanvasProvider =
      new BitmapCanvasProvider(output, mimeType, ImageResolution,
        BufferedImage.TYPE_BYTE_BINARY, false, 0)

    val barCode = new EAN13Bean()
    barCode.generateBarcode(canvas, String valueOf ean)
    canvas.finish

    output.toByteArray
  }
}