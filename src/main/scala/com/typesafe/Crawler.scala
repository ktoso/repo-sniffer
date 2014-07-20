package com.typesafe

import com.google.api.services.bigquery.Bigquery
import com.google.api.services.bigquery.model.{Job, JobConfiguration, JobConfigurationQuery, JobReference}
import com.typesafe.google.auth.GAuth
import com.typesafe.google.bigquery.{SQLs, BigQuery}

object Crawler extends App {

  val ProjectId = "akka-build"

  val credential = GAuth.auth()

  val query = BigQuery(credential)

  import scala.collection.JavaConversions._

  val querySql = SQLs.allScalaProjects
  val jobId = startQuery(query.big, ProjectId, querySql)

  // Poll for Query Results, return result output
  val completedJob = checkQueryResults(query.big, ProjectId, jobId)

  // Return and display the results of the Query Job
  displayQueryResults(query.big, ProjectId, completedJob)


  def startQuery(bigquery: Bigquery, projectId: String, querySql: String) = {
    System.out.format("\nInserting Query Job: %s\n", querySql)

    val job = new Job()
    val config = new JobConfiguration()
    val queryConfig = new JobConfigurationQuery()
    config.setQuery(queryConfig)

    job.setConfiguration(config)
    queryConfig.setQuery(querySql)

    val insert = bigquery.jobs().insert(projectId, job)
    insert.setProjectId(projectId)
    val jobId = insert.execute().getJobReference

    println(s"\nJob ID of Query Job is: ${jobId.getJobId}")

    jobId
  }

  def checkQueryResults(bigquery: Bigquery, projectId: String, jobId: JobReference): Job = {
    // Variables to keep track of total query time
    val startTime = System.currentTimeMillis()
    var elapsedTime = 0L

    while (true) {
      val pollJob = bigquery.jobs().get(projectId, jobId.getJobId).execute()
      elapsedTime = System.currentTimeMillis() - startTime
      println(s"Job status (${elapsedTime}ms) ${jobId.getJobId}: ${pollJob.getStatus.getState}")

      if (pollJob.getStatus.getState.equals("DONE")) {
        return pollJob
      }
      // Pause execution for one second before polling job status again, to
      // reduce unnecessary calls to the BigQUery API and lower overall
      // application bandwidth.
      Thread.sleep(1000)
    }
    null
  }

  def displayQueryResults(bigquery: Bigquery, projectId: String, completedJob: Job) {
    val queryResult = bigquery.jobs()
      .getQueryResults(
        projectId, completedJob
      .getJobReference
      .getJobId
      ).execute()
    val rows = queryResult.getRows
    print("\nQuery Results:\n------------\n")

    for (row <- rows) {
      for (field <- row.getF) {
        print("%-50s".format(field.getV))
      }
      println()
    }
  }

}
