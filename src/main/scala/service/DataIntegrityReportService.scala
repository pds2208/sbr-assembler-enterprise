package service

import global.AppParams
import org.apache.spark.sql.SparkSession
import spark.SparkSessionManager

trait DataIntegrityReportService extends SparkSessionManager{

  private def printCollection(coll:Seq[(String,(String,String))],columns:String,printableSize:Int = 50) = {

    val printableRange = 1 to printableSize
    def withinPrintableRange(count:Int) = printableRange.contains(count)
    def exceedsPrintableRange(count:Int) = count>printableSize
    val collSize = coll.length
    if (withinPrintableRange(collSize)){
      println(s"   $columns")
      println("    --------------------------------------------------------")
      coll.foreach(row => {
        println(s"   |${row._1} | ${row._2._1} |  ${row._2._2}  |")
        println("    --------------------------------------------------------")
      })
    }else if (exceedsPrintableRange(collSize)){
      println(s"   TOP $printableSize RESULTS")
      println(s"$columns")
      println("    --------------------------------------------------------")
      coll.take(printableSize).foreach(row => {
        println(s"   |${row._1} | ${row._2._1} |  ${row._2._2}  |")
        println("    --------------------------------------------------------")
      })
    }
  }

  def printReport(appconf:AppParams, printableSize:Int = 50) = withSpark(appconf){ implicit ss:SparkSession =>


    val report: DataReport = InputAnalyser.getData(appconf)
    val childlessEntsCount = report.childlessEntErns.length
    val orphanLusCount = report.lusOrphans.length
    val orphanLosCount = report.losOrphans.length

    println("data Integrity Report:")
    println("==================================")
    println(s"ENTERPRISE COUNT: ${report.entCount}")
    println(s"LEGAL UNITS COUNT: ${report.lusCount}")
    println(s"LOCAL UNITS COUNT: ${report.losCount}")
    println(s"CHILDLESS ENTERPRISES COUNT: $childlessEntsCount")
    println(s"ORPHAN LEGAL UNITS COUNT: $orphanLusCount")
    println(s"ORPHAN LOCAL UNITS COUNT: $orphanLosCount")
    if(report.childlessEntErns.nonEmpty) {
      println("CHILDLESS ENTERPRISE ERNs:")
      report.childlessEntErns.foreach(println)
    }
    if(report.lusOrphans.nonEmpty) {
      println("   ORPHAN LEGAL UNITs:")
      println("    --------------------------------------------------------")
      printCollection(report.lusOrphans,"|    ERN    |     UBRN     |           ROW KEY          |",printableSize)
    }
    if(report.losOrphans.nonEmpty) {
      println("   ORPHAN LOCAL UNITs:")
      println("    --------------------------------------------------------")
      printCollection(report.losOrphans, "|    ERN    |     LURN     |           ROW KEY          |", printableSize)
    }

  }
}
