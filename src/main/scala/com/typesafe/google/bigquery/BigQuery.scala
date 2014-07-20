package com.typesafe.google.bigquery

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.bigquery.Bigquery
import com.google.api.services.bigquery.model.DatasetList
import collection.JavaConverters._

class BigQuery(val big: Bigquery) {

  def listDatasets( projectId: String) ={
    val datasetRequest = big.datasets().list(projectId)
    val datasetList = datasetRequest.execute()

    if (datasetList.getDatasets != null) {
      val datasets = datasetList.getDatasets
      System.out.println("Available datasets:\n")
      for (dataset <- datasets.asScala) {
        System.out.format("%s\n", dataset.getDatasetReference.getDatasetId)
      }
    }
  }

}

object BigQuery {
  def apply(credential: Credential): BigQuery = {
    val TRANSPORT = new NetHttpTransport()
    val JSON_FACTORY = new JacksonFactory()

    new BigQuery(new Bigquery(TRANSPORT, JSON_FACTORY, credential))
  }
}
